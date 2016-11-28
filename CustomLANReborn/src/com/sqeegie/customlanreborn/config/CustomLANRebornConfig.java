package com.sqeegie.customlanreborn.config;

import com.sqeegie.customlanreborn.core.CustomLANReborn;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class CustomLANRebornConfig implements IModGuiFactory
{
	public void initialize(Minecraft minecraftInstance) {}

	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return GuiCustomLan.class;
	}

	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}

	public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element)
	{
		return null;
	}

	public static class GuiCustomLan
	extends GuiConfig
	{
		private static File optionsCLP = CustomLANReborn.options;
		private static File optionsServer = CustomLANReborn.optionsServer;
		public static String serverMOTD = "Minecraft Server";
		public static String serverIP = "0.0.0.0";
		public static int serverPort = 25565;
		public static int serverMaxPlayers = 16;
		public static boolean serverOnlineMode = false;
		public static boolean serverCanSpawnAnimals = true;
		public static boolean serverCanSpawnNPCs = true;
		public static boolean serverAllowPvP = true;
		public static boolean serverAllowFlight = false;
		public static String serverMOTDRef = "Minecraft Server";
		public static String serverIPRef = "0.0.0.0";
		public static int serverPortRef = 25565;
		public static int serverMaxPlayersRef = 16;
		public static boolean serverOnlineModeRef = false;
		public static boolean serverCanSpawnAnimalsRef = true;
		public static boolean serverCanSpawnNPCsRef = true;
		public static boolean serverAllowPvPRef = true;
		public static boolean serverAllowFlightRef = false;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public GuiCustomLan(GuiScreen parent) {
			super(parent, new ConfigElement(CustomLANReborn.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "CustomLAN Reborn Configuration", true, false, "CustomLAN Reborn Configuration");        
		}

		public void initGui()
		{
			checkOptions();

			super.initGui();
		}

		protected void actionPerformed(GuiButton par1GuiButton)
		{
			if ((par1GuiButton.enabled) && 
					(par1GuiButton.id == 267))
			{
				saveOptions();
				mc.displayGuiScreen(new GuiCustomLANRebornPermissions(this));
			}
			super.actionPerformed(par1GuiButton);
		}

		public void drawScreen(int par1, int par2, float par3)
		{
			super.drawScreen(par1, par2, par3);
		}

		public void onGuiClosed()
		{
			saveOptions();
			Keyboard.enableRepeatEvents(false);
		}

		public static String getServerMOTD()
		{
			checkOptions();
			return serverMOTD;
		}

		public static int getServerPort()
		{
			checkOptions();
			return serverPort;
		}

		public static String getServerIP()
		{
			checkOptions();
			return serverIP;
		}

		public static int getServerMaxPlayers()
		{
			checkOptions();
			return serverMaxPlayers;
		}

		public static boolean getServerOnlineMode()
		{
			checkOptions();
			return serverOnlineMode;
		}

		public static boolean getServerAllowPvP()
		{
			checkOptions();
			return serverAllowPvP;
		}

		public static boolean getServerCanSpawnNPCs()
		{
			checkOptions();
			return serverCanSpawnNPCs;
		}

		public static boolean getServerCanSpawnAnimals()
		{
			checkOptions();
			return serverCanSpawnAnimals;
		}

		public static boolean getServerAllowFlight()
		{
			checkOptions();
			return serverAllowFlight;
		}

		private static void checkOptions()
		{
			if (!optionsCLP.exists()) {
				try
				{
					CustomLANReborn.syncConfig();
					saveOptions();
				}
				catch (Exception e)
				{
					System.out.println("Cannot save CustomLAN Reborn configs.");
				}
			}
			if (!optionsServer.exists()) {
				try
				{
					new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/").mkdirs();
					optionsServer.createNewFile();
					saveOptions();
				}
				catch (Exception e)
				{
					System.out.println("Cannot save CustomLAN Reborn endpoint.");
				}
			}
			loadOptions();
		}

		public static void saveOptions()
		{
			try
			{
				PrintWriter var1 = new PrintWriter(new FileWriter(optionsServer));
				var1.println("serverIP=" + serverIP);
				var1.println("serverPort=" + serverPort);
				var1.close();

				CustomLANReborn.syncConfig();
			}
			catch (Exception var3)
			{
				System.out.println("Failed to save MOTD!");
				var3.printStackTrace();
			}
		}

		public static void loadOptions()
		{
			try
			{
				if (!optionsServer.exists()) {
					return;
				}
				BufferedReader var1 = new BufferedReader(new FileReader(optionsServer));
				String var2 = "";
				while ((var2 = var1.readLine()) != null) {
					try
					{
						String[] var3 = var2.split("=");
						if (var3[0].equals("serverIP")) {
							serverIP = var3[1];
						}
						if (var3[0].equals("serverPort")) {
							serverPort = Integer.parseInt(var3[1]);
						}
					}
					catch (Exception var5)
					{
						System.out.println("Skipping bad option: " + var2.split("=")[0]);
					}
				}
				KeyBinding.resetKeyBindingArrayAndHash();
				var1.close();
			}
			catch (Exception var6)
			{
				System.out.println("Failed to load options");
				var6.printStackTrace();
			}
			CustomLANReborn.syncConfig();
		}

		public static void setServerIP()
		{
			if (serverIP == "0.0.0.0") {
				try
				{
					serverIP = String.valueOf(InetAddress.getLocalHost().getHostAddress());
				}
				catch (UnknownHostException e)
				{
					System.out.println("Cannot get the local host address. Setting it to '0.0.0.0'.");
					serverIP = "0.0.0.0";
				}
			}
		}

		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
		{
			if (modID.equals("customlanreborn")) {
				CustomLANReborn.syncConfig();
			}
		}
	}
}
