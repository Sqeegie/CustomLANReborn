package com.sqeegie.customlanreborn.core;

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
import com.sqeegie.customlanreborn.handlers.*;
import com.sqeegie.customlanreborn.util.CommonUtil;
import com.sqeegie.customlanreborn.util.PlayerUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

@Mod(modid = CustomLANReborn.MODID, name = CustomLANReborn.NAME, useMetadata = true, version = CustomLANReborn.VERSION, guiFactory = "com.sqeegie.customlanreborn.handlers.ConfigHandler")
public class CustomLANReborn {

	public static final String MODID = "CustomLAN";
	public static final String NAME = "CustomLAN Reborn";
	public static final String VERSION = "2.4.2";
	
	public static Configuration config;
	public static File optionsServer = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/", "lastendpoint.cfg");
	public static File options = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/", "options.cfg");
	public static Logger logger;

	@Instance
	public static CustomLANReborn instance = new CustomLANReborn();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		logger.info("Loading CustomLAN Reborn..");

		FMLCommonHandler.instance().bus().register(instance);
	    FMLCommonHandler.instance().bus().register(new TickHandler());
		config = new Configuration(options);
		ConfigHandler.GuiCustomLan.syncConfig();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		logger.info("Loaded CustomLAN Reborn.");
	}

	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		CustomLANReborn.registerCommands();
		PermissionsHandler.initMap();
		PermissionsHandler.checkOptions();
		
		event.getServer().getConfigurationManager().getBannedIPs().func_152686_a(true);
		event.getServer().getConfigurationManager().func_152608_h().func_152686_a(true);
		PermissionsHandler.loadOps();
	}

	@EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
		setServerConfigOptions();
		
		Field maxPlayersField = null;
		try {
			maxPlayersField = ServerConfigurationManager.class.getDeclaredField("field_72405_c"); // Old obf name?
		}
		catch (Exception e) {
			try {
				maxPlayersField = ServerConfigurationManager.class.getDeclaredField("b"); // Old obf name?
			}
			catch (Exception e2) {
				try {
					maxPlayersField = ServerConfigurationManager.class.getDeclaredField("maxPlayers");
				}
				catch (Exception e3) {
					CustomLANReborn.logger.error("Unable to access private fields. Are you using the wrong version?");
				}
			}
		}
		try {
			maxPlayersField.setAccessible(true);
			maxPlayersField.set(CommonUtil.mcServer().getConfigurationManager(), Integer.valueOf(ConfigHandler.GuiCustomLan.getServerMaxPlayers()));
			CustomLANReborn.logger.info("Max players set to " + ConfigHandler.GuiCustomLan.getServerMaxPlayers());
		}
		catch (IllegalAccessException e) {
			CustomLANReborn.logger.error("Unable to set max players: " + e);
		}
	}
	
	private void setServerConfigOptions() {
		CommonUtil.mcServer().setAllowFlight(ConfigHandler.GuiCustomLan.getServerAllowFlight());
		CommonUtil.mcServer().setAllowPvp(ConfigHandler.GuiCustomLan.getServerAllowPvP());
		CommonUtil.mcServer().setCanSpawnAnimals(ConfigHandler.GuiCustomLan.getServerCanSpawnAnimals());
		CommonUtil.mcServer().setCanSpawnNPCs(ConfigHandler.GuiCustomLan.getServerCanSpawnNPCs());
		CommonUtil.mcServer().setMOTD(ConfigHandler.GuiCustomLan.getServerMOTD());
		CommonUtil.mcServer().setOnlineMode(ConfigHandler.GuiCustomLan.getServerOnlineMode());
	}
	
	public static void registerCommands() {
		logger.info("Registering commands..");
		ICommandManager commandManager = CommonUtil.mcServer().getCommandManager();
		ServerCommandManager SCM = (ServerCommandManager) commandManager;
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
