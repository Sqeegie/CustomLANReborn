package com.sqeegie.customlanreborn.handlers;

import java.lang.reflect.Field;
import java.net.InetAddress;

import com.sqeegie.customlanreborn.core.CustomLANReborn;
import com.sqeegie.customlanreborn.util.CommonUtil;

import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class LANHandler {
	
	public static String gameMode = "survival";
	public static boolean allowCommands = false;
	
	public static void shareToLAN() {
		
		try {
			IntegratedServer server = CommonUtil.mc().getIntegratedServer();

			IChatComponent msg;
			Field isPublicField = null;
			Field lanServerPingField = null;

			CommonUtil.devMsg("Attempting to open LAN server on " + ConfigHandler.GuiCustomLan.getServerIP() + ":" + ConfigHandler.GuiCustomLan.getServerPort());
			server.func_147137_ag().addLanEndpoint(InetAddress.getByName(ConfigHandler.GuiCustomLan.getServerIP()), ConfigHandler.GuiCustomLan.getServerPort());
			try {
				isPublicField = IntegratedServer.class.getDeclaredField("field_71346_p"); // Old obf name?
				lanServerPingField = IntegratedServer.class.getDeclaredField("field_71345_q"); // Old obf name?
			}
			catch (Exception e) {
				try {
					isPublicField = IntegratedServer.class.getDeclaredField("l"); // Old obf name?
					lanServerPingField = IntegratedServer.class.getDeclaredField("m"); // Old obf name?
				}
				catch (Exception e2) {
					try {
						isPublicField = IntegratedServer.class.getDeclaredField("isPublic");
						lanServerPingField = IntegratedServer.class.getDeclaredField("lanServerPing");
					}
					catch (Exception e3) {
						CommonUtil.sendChatMsg("Unable to access private fields. Are you using the wrong version?");
					}
				}
			}
			try {
				isPublicField.setAccessible(true);
				lanServerPingField.setAccessible(true);
				isPublicField.set((Object) server, true);
				ThreadLanServerPing TLSP = new ThreadLanServerPing(server.getMOTD(), "" + ConfigHandler.GuiCustomLan.getServerPort() + "");
				lanServerPingField.set((Object) server, (Object) TLSP);
				TLSP.start();
				server.getConfigurationManager().func_152604_a(WorldSettings.GameType.getByName((String) gameMode)); // Set gamemode
				server.getConfigurationManager().setCommandsAllowedForAll(allowCommands); // Set command permissions
				msg = new ChatComponentTranslation("Started on " + ConfigHandler.GuiCustomLan.getServerIP() + ":" + ConfigHandler.GuiCustomLan.getServerPort(), new Object[0]);
			}
			catch (Exception e) {
				msg = new ChatComponentTranslation("Either you've entered incorrect information, or a server is already running on this port.");
				e.printStackTrace();
			}
			CommonUtil.mc().ingameGUI.getChatGUI().printChatMessage(msg);
		}
		catch (Exception e) {
			CommonUtil.sendChatMsg("Either you've entered incorrect information, or a server is already running on this port.");
			CommonUtil.devMsg("Failed to open LAN server! See details below:");
			if (ConfigHandler.GuiCustomLan.isDevMsgsEnabled()) {
				e.printStackTrace();
			}
			else {
				System.out.println(e);
			}
		}
	}
}
