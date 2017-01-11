package com.sqeegie.customlanreborn.handlers;

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
public class ConfigHandler implements IModGuiFactory {
	public void initialize(Minecraft minecraftInstance) {
	}

	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiCustomLan.class;
	}

	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element) {
		return null;
	}

	public static class GuiCustomLan extends GuiConfig {
		private static File optionsCLP = CustomLANReborn.options;
		private static File optionsServer = CustomLANReborn.optionsServer;
		// Runtime values
		private static String serverMOTD = "Minecraft Server";
		private static String serverIP = "0.0.0.0";
		private static int serverPort = 25565;
		private static int serverMaxPlayers = 16;
		private static boolean serverOnlineMode = false;
		private static boolean serverCanSpawnAnimals = true;
		private static boolean serverCanSpawnNPCs = true;
		private static boolean serverAllowPvP = true;
		private static boolean serverAllowFlight = false;
		private static boolean isDevMsgsEnabled = false;
		// Default values
		private final static String serverMOTDRef = "Minecraft Server";
		private final static String serverIPRef = "0.0.0.0";
		private final static int serverPortRef = 25565;
		private final static int serverMaxPlayersRef = 16;
		private final static boolean serverOnlineModeRef = false;
		private final static boolean serverCanSpawnAnimalsRef = true;
		private final static boolean serverCanSpawnNPCsRef = true;
		private final static boolean serverAllowPvPRef = true;
		private final static boolean serverAllowFlightRef = false;
		private final static boolean isDevMsgsEnabledRef = false;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public GuiCustomLan(GuiScreen parent) {
			super(parent, new ConfigElement(CustomLANReborn.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "CustomLAN Reborn Configuration", true, false, "CustomLAN Reborn Configuration");
		}

		public void initGui() {
			checkOptions();
			super.initGui();
		}

		protected void actionPerformed(GuiButton par1GuiButton) {
			if ((par1GuiButton.enabled) && (par1GuiButton.id == 267)) {
				saveOptions();
				mc.displayGuiScreen(new PermissionsHandler(this));
			}
			super.actionPerformed(par1GuiButton);
		}

		public void drawScreen(int par1, int par2, float par3) {
			super.drawScreen(par1, par2, par3);
		}

		public void onGuiClosed() {
			saveOptions();
			Keyboard.enableRepeatEvents(false);
		}

		public static String getServerMOTD() {
			checkOptions();
			return serverMOTD;
		}

		public static int getServerPort() {
			checkOptions();
			return serverPort;
		}

		public static String getServerIP() {
			checkOptions();
			return serverIP;
		}

		public static int getServerMaxPlayers() {
			checkOptions();
			return serverMaxPlayers;
		}

		public static boolean getServerOnlineMode() {
			checkOptions();
			return serverOnlineMode;
		}

		public static boolean getServerAllowPvP() {
			checkOptions();
			return serverAllowPvP;
		}

		public static boolean getServerCanSpawnNPCs() {
			checkOptions();
			return serverCanSpawnNPCs;
		}

		public static boolean getServerCanSpawnAnimals() {
			checkOptions();
			return serverCanSpawnAnimals;
		}

		public static boolean getServerAllowFlight() {
			checkOptions();
			return serverAllowFlight;
		}

		public static boolean isDevMsgsEnabled() {
			checkOptions();
			return isDevMsgsEnabled;
		}

		public static void setServerPort(Integer port) {
			serverPort = port;
			saveOptions();
		}

		public static void setServerIP(String IP) {
			serverIP = IP;
			saveOptions();
		}

		public static String getDefaultServerIP() {
			return serverIPRef;
		}

		public static int getDefaultServerPort() {
			return serverPortRef;
		}

		private static void checkOptions() {
			if (!optionsCLP.exists()) {
				try {
					syncConfig();
					saveOptions();
				}
				catch (Exception e) {
					System.out.println("Cannot save CustomLAN Reborn configs.");
				}
			}
			if (!optionsServer.exists()) {
				try {
					new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/").mkdirs();
					optionsServer.createNewFile();
					saveOptions();
				}
				catch (Exception e) {
					System.out.println("Cannot save CustomLAN Reborn endpoint.");
				}
			}
			loadOptions();
		}

		public static void saveOptions() {
			try {
				PrintWriter var1 = new PrintWriter(new FileWriter(optionsServer));
				var1.println("serverIP=" + serverIP);
				var1.println("serverPort=" + serverPort);
				var1.close();

				syncConfig();
			}
			catch (Exception var3) {
				System.out.println("Failed to save MOTD!");
				var3.printStackTrace();
			}
		}

		public static void loadOptions() {
			try {
				if (!optionsServer.exists()) {
					return;
				}
				BufferedReader var1 = new BufferedReader(new FileReader(optionsServer));
				String var2 = "";
				while ((var2 = var1.readLine()) != null) {
					try {
						String[] var3 = var2.split("=");
						if (var3[0].equals("serverIP")) {
							serverIP = var3[1];
						}
						if (var3[0].equals("serverPort")) {
							serverPort = Integer.parseInt(var3[1]);
						}
					}
					catch (Exception var5) {
						System.out.println("Skipping bad option: " + var2.split("=")[0]);
					}
				}
				KeyBinding.resetKeyBindingArrayAndHash();
				var1.close();
			}
			catch (Exception var6) {
				System.out.println("Failed to load options");
				var6.printStackTrace();
			}
			syncConfig();
		}

		public static void setServerIP() {
			if (serverIP == "0.0.0.0") {
				try {
					serverIP = String.valueOf(InetAddress.getLocalHost().getHostAddress());
				}
				catch (UnknownHostException e) {
					System.out.println("Cannot get the local host address. Setting it to '0.0.0.0'.");
					serverIP = "0.0.0.0";
				}
			}
		}

		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if (modID.equals("CustomLAN")) {
				syncConfig();
			}
		}

		public static void syncConfig() {
			ConfigHandler.GuiCustomLan.serverMOTD = CustomLANReborn.config.getString("serverMOTD", "general", ConfigHandler.GuiCustomLan.serverMOTDRef, "");
			ConfigHandler.GuiCustomLan.serverMaxPlayers = CustomLANReborn.config.getInt("serverMaxPlayers", "general", ConfigHandler.GuiCustomLan.serverMaxPlayersRef, 1, Integer.MAX_VALUE, "");
			ConfigHandler.GuiCustomLan.serverOnlineMode = CustomLANReborn.config.getBoolean("serverOnlineMode", "general", ConfigHandler.GuiCustomLan.serverOnlineModeRef, "");
			ConfigHandler.GuiCustomLan.serverCanSpawnAnimals = CustomLANReborn.config.getBoolean("serverCanSpawnAnimals", "general", ConfigHandler.GuiCustomLan.serverCanSpawnAnimalsRef, "");
			ConfigHandler.GuiCustomLan.serverCanSpawnNPCs = CustomLANReborn.config.getBoolean("serverCanSpawnNPCs", "general", ConfigHandler.GuiCustomLan.serverCanSpawnNPCsRef, "");
			ConfigHandler.GuiCustomLan.serverAllowPvP = CustomLANReborn.config.getBoolean("serverAllowPvP", "general", ConfigHandler.GuiCustomLan.serverAllowPvPRef, "");
			ConfigHandler.GuiCustomLan.serverAllowFlight = CustomLANReborn.config.getBoolean("serverAllowFlight", "general", ConfigHandler.GuiCustomLan.serverAllowFlightRef, "");
			ConfigHandler.GuiCustomLan.isDevMsgsEnabled = CustomLANReborn.config.getBoolean("isExtraDebugMsgsEnabled", "general", ConfigHandler.GuiCustomLan.isDevMsgsEnabledRef, "");

			if (CustomLANReborn.config.hasChanged()) {
				CustomLANReborn.config.save();
			}
		}
	}
}
