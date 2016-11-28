package com.sqeegie.customlanreborn.core;

import com.mojang.authlib.GameProfile;
import com.sqeegie.customlanreborn.commands.CommandLANBack;
import com.sqeegie.customlanreborn.commands.CommandLANBroadcast;
import com.sqeegie.customlanreborn.commands.CommandLANEat;
import com.sqeegie.customlanreborn.commands.CommandLANExplosion;
import com.sqeegie.customlanreborn.commands.CommandLANFireball;
import com.sqeegie.customlanreborn.commands.CommandLANFly;
import com.sqeegie.customlanreborn.commands.CommandLANGetTime;
import com.sqeegie.customlanreborn.commands.CommandLANGod;
import com.sqeegie.customlanreborn.commands.CommandLANHat;
import com.sqeegie.customlanreborn.commands.CommandLANHeal;
import com.sqeegie.customlanreborn.commands.CommandLANHome;
import com.sqeegie.customlanreborn.commands.CommandLANKick;
import com.sqeegie.customlanreborn.commands.CommandLANLightning;
import com.sqeegie.customlanreborn.commands.CommandLANPM;
import com.sqeegie.customlanreborn.commands.CommandLANReload;
import com.sqeegie.customlanreborn.commands.CommandLANRemoveWarp;
import com.sqeegie.customlanreborn.commands.CommandLANSaveAll;
import com.sqeegie.customlanreborn.commands.CommandLANSaveOff;
import com.sqeegie.customlanreborn.commands.CommandLANSaveOn;
import com.sqeegie.customlanreborn.commands.CommandLANSetHome;
import com.sqeegie.customlanreborn.commands.CommandLANSetSpawn;
import com.sqeegie.customlanreborn.commands.CommandLANSetWarp;
import com.sqeegie.customlanreborn.commands.CommandLANSpawn;
import com.sqeegie.customlanreborn.commands.CommandLANStop;
import com.sqeegie.customlanreborn.commands.CommandLANTp;
import com.sqeegie.customlanreborn.commands.CommandLANViewMOTD;
import com.sqeegie.customlanreborn.commands.CommandLANWarp;
import com.sqeegie.customlanreborn.commands.CommandLANWarpList;
import com.sqeegie.customlanreborn.config.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanList;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

@Mod(modid="customlanreborn", name="CustomLAN Reborn", useMetadata=true, version="2.4.2", guiFactory="com.sqeegie.customlanreborn.config.CustomLANRebornConfig")
public class CustomLANReborn
{
	public static File optionsServer = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/", "lastendpoint.cfg");
	public static File options = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/", "options.cfg");
	public static Logger logger;
	public static File ops = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/", "ops.cfg");
	public static Set opslist = new HashSet();
	public static Configuration config;
	public static File bannedPlayersFile = ServerConfigurationManager.field_152613_a;
	public static File bannedIPsFile = ServerConfigurationManager.field_152614_b;
	public static File opsFile = ServerConfigurationManager.field_152615_c;
	public static UserListBans bannedPlayers;
	private static BanList bannedIPs;
	@Mod.Instance
	public static CustomLANReborn instance = new CustomLANReborn();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		logger.info("Loading CustomLAN Reborn..");

		FMLCommonHandler.instance().bus().register(new CustomLANRebornTick());
		FMLCommonHandler.instance().bus().register(instance);
		config = new Configuration(options);
		syncConfig();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		logger.info("Loaded CustomLAN Reborn.");
	}

	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		registerCommands();
		GuiCustomLANRebornPermissions.initMap();
		GuiCustomLANRebornPermissions.checkOptions();

		bannedPlayers = new UserListBans(bannedPlayersFile);
		bannedIPs = new BanList(bannedIPsFile);
		bannedPlayers.func_152686_a(true);
		bannedIPs.func_152686_a(true);
		loadOps();
	}

	@Mod.EventHandler
	public void onServerStarted(FMLServerStartedEvent event)
	{
		MinecraftServer server = MinecraftServer.getServer();
		server.setAllowFlight(CustomLANRebornConfig.GuiCustomLan.getServerAllowFlight());
		server.setAllowPvp(CustomLANRebornConfig.GuiCustomLan.getServerAllowPvP());
		server.setCanSpawnAnimals(CustomLANRebornConfig.GuiCustomLan.getServerCanSpawnAnimals());
		server.setCanSpawnNPCs(CustomLANRebornConfig.GuiCustomLan.getServerCanSpawnNPCs());
		server.setMOTD(CustomLANRebornConfig.GuiCustomLan.getServerMOTD());
		server.setOnlineMode(CustomLANRebornConfig.GuiCustomLan.getServerOnlineMode());
		Field maxPlayersField = null;
		try
		{
			maxPlayersField = ServerConfigurationManager.class.getDeclaredField("field_72405_c");
		}
		catch (Exception e)
		{
			try
			{
				maxPlayersField = ServerConfigurationManager.class.getDeclaredField("b");
			}
			catch (Exception e2)
			{
				try
				{
					maxPlayersField = ServerConfigurationManager.class.getDeclaredField("maxPlayers");
				}
				catch (Exception e3)
				{
					logger.error("Unable to access private fields. Are you using the wrong version?");
				}
			}
		}
		try
		{
			maxPlayersField.setAccessible(true);
			maxPlayersField.set(server.getConfigurationManager(), Integer.valueOf(CustomLANRebornConfig.GuiCustomLan.getServerMaxPlayers()));
			logger.info("Max players set to " + CustomLANRebornConfig.GuiCustomLan.getServerMaxPlayers());
		}
		catch (IllegalAccessException e)
		{
			logger.error("Unable to set max players: " + e);
		}
	}

	public static EntityPlayerMP getPlayerByUsername(String username)
	{
		MinecraftServer mc = MinecraftServer.getServer();
		if (mc == null) {
			return null;
		}
		ServerConfigurationManager configurationManager = mc.getConfigurationManager();
		return configurationManager == null ? null : configurationManager.func_152612_a(username);
	}

	public static EntityPlayerMP getPlayerByMatchOrUsername(ICommandSender sender, String match)
	{
		EntityPlayerMP player = PlayerSelector.matchOnePlayer(sender, match);
		if (player != null) {
			return player;
		}
		return getPlayerByUsername(match);
	}

	public static GameProfile getGameProfileByUsername(String username)
	{
		EntityPlayer player = getPlayerByUsername(username);
		if (player != null)
		{
			if (!player.getGameProfile().isComplete()) {
				return new GameProfile(UUID.nameUUIDFromBytes(username.getBytes()), player.getCommandSenderName());
			}
			return player.getGameProfile();
		}
		return new GameProfile(UUID.nameUUIDFromBytes(username.getBytes()), username);
	}

	public static MovingObjectPosition getPlayerLookingSpot(EntityPlayer player)
	{
		if ((player instanceof EntityPlayerMP)) {
			return getPlayerLookingSpot(player, ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance());
		}
		return getPlayerLookingSpot(player, 5.0D);
	}

	public static MovingObjectPosition getPlayerLookingSpot(EntityPlayer player, double maxDistance)
	{
		Vec3 lookAt = player.getLook(1.0F);
		Vec3 playerPos = Vec3.createVectorHelper(player.posX, player.posY + (player.getEyeHeight() - player.getDefaultEyeHeight()), player.posZ);
		Vec3 pos1 = playerPos.addVector(0.0D, player.getEyeHeight(), 0.0D);
		Vec3 pos2 = pos1.addVector(lookAt.xCoord * maxDistance, lookAt.yCoord * maxDistance, lookAt.zCoord * maxDistance);
		return player.worldObj.rayTraceBlocks(pos1, pos2);
	}

	public static void addOp(String playername)
	{
		MinecraftServer mcServer = MinecraftServer.getServer();
		GameProfile gameprofile = getGameProfileByUsername(playername);
		if (gameprofile != null) {
			MinecraftServer.getServer().getConfigurationManager().func_152605_a(gameprofile);
		} else {
			logger.error("Failed to add op to vanilla OPs list.");
		}
		opslist.add(playername);
	}

	public static void removeOp(String playername)
	{
		EntityPlayerMP player = getPlayerByUsername(playername);
		MinecraftServer.getServer().getConfigurationManager().func_152610_b(player.getGameProfile());
		opslist.remove(playername);
	}

	public static UserListOps getOps()
	{
		return MinecraftServer.getServer().getConfigurationManager().func_152603_m();
	}

	public static void loadOps()
	{
		try
		{
			logger.info("Checking OPs...");

			MinecraftServer mcServer = MinecraftServer.getServer();

			addOp(mcServer.getServerOwner());
			logger.info("Owner " + mcServer.getServerOwner() + " registered.");

			File folder = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/");
			File var3 = ops;
			if (!var3.exists())
			{
				folder.mkdirs();
				var3.createNewFile();
				logger.info("OPs checked.");
				return;
			}
			FileReader var4 = new FileReader(var3);
			BufferedReader var5 = new BufferedReader(var4);
			String var7 = null;
			while ((var7 = var5.readLine()) != null) {
				try
				{
					addOp(var7);
					logger.info("Loaded OP " + var7 + ".");
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.error("Failed to load an op.");
				}
			}
			var5.close();
			var4.close();

			logger.info("OPs checked.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Could not load OPs");
		}
	}

	public static void loadBanned() {}

	public static void syncConfig()
	{
		CustomLANRebornConfig.GuiCustomLan.serverMOTD = config.getString("serverMOTD", "general", CustomLANRebornConfig.GuiCustomLan.serverMOTDRef, "");
		CustomLANRebornConfig.GuiCustomLan.serverMaxPlayers = config.getInt("serverMaxPlayers", "general", CustomLANRebornConfig.GuiCustomLan.serverMaxPlayersRef, 1, Integer.MAX_VALUE, "");
		CustomLANRebornConfig.GuiCustomLan.serverOnlineMode = config.getBoolean("serverOnlineMode", "general", CustomLANRebornConfig.GuiCustomLan.serverOnlineModeRef, "");
		CustomLANRebornConfig.GuiCustomLan.serverCanSpawnAnimals = config.getBoolean("serverCanSpawnAnimals", "general", CustomLANRebornConfig.GuiCustomLan.serverCanSpawnAnimalsRef, "");
		CustomLANRebornConfig.GuiCustomLan.serverCanSpawnNPCs = config.getBoolean("serverCanSpawnNPCs", "general", CustomLANRebornConfig.GuiCustomLan.serverCanSpawnNPCsRef, "");
		CustomLANRebornConfig.GuiCustomLan.serverAllowPvP = config.getBoolean("serverAllowPvP", "general", CustomLANRebornConfig.GuiCustomLan.serverAllowPvPRef, "");
		CustomLANRebornConfig.GuiCustomLan.serverAllowFlight = config.getBoolean("serverAllowFlight", "general", CustomLANRebornConfig.GuiCustomLan.serverAllowFlightRef, "");
		if (config.hasChanged()) {
			config.save();
		}
	}

	private void registerCommands()
	{
		logger.info("Registering commands..");
		ICommandManager commandManager = MinecraftServer.getServer().getCommandManager();
		ServerCommandManager SCM = (ServerCommandManager)commandManager;
		SCM.registerCommand(new CommandLANBack());

		SCM.registerCommand(new CommandLANBroadcast());

		SCM.registerCommand(new CommandLANEat());
		SCM.registerCommand(new CommandLANExplosion());
		SCM.registerCommand(new CommandLANFireball());
		SCM.registerCommand(new CommandLANFly());
		SCM.registerCommand(new CommandLANGetTime());
		SCM.registerCommand(new CommandLANGod());
		SCM.registerCommand(new CommandLANHat());
		SCM.registerCommand(new CommandLANHeal());
		SCM.registerCommand(new CommandLANHome());
		SCM.registerCommand(new CommandLANKick());
		SCM.registerCommand(new CommandLANLightning());

		SCM.registerCommand(new CommandLANPM());
		SCM.registerCommand(new CommandLANReload());

		SCM.registerCommand(new CommandLANRemoveWarp());
		SCM.registerCommand(new CommandLANSaveAll());
		SCM.registerCommand(new CommandLANSaveOff());
		SCM.registerCommand(new CommandLANSaveOn());
		SCM.registerCommand(new CommandLANSetHome());
		SCM.registerCommand(new CommandLANSetWarp());
		SCM.registerCommand(new CommandLANSetSpawn());
		SCM.registerCommand(new CommandLANSpawn());

		SCM.registerCommand(new CommandLANStop());
		SCM.registerCommand(new CommandLANTp());

		SCM.registerCommand(new CommandLANViewMOTD());
		SCM.registerCommand(new CommandLANWarp());
		SCM.registerCommand(new CommandLANWarpList());
		logger.info("Commands registered successfully.");
	}
}
