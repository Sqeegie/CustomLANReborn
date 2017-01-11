package com.sqeegie.customlanreborn.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.sqeegie.customlanreborn.core.CustomLANReborn;
import com.sqeegie.customlanreborn.handlers.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class CommonUtil {

	/** Returns the Minecraft Server instance */
	public static MinecraftServer mcServer() {
		return MinecraftServer.getServer();
	}

	/** Returns the Minecraft instance */
	public static Minecraft mc() {
		return Minecraft.getMinecraft();
	}

	/** Prints developer debug message. */
	public static void devMsg(String msg) {
		if (ConfigHandler.GuiCustomLan.isDevMsgsEnabled()) { // Check if option is enabled in config
			CustomLANReborn.logger.info(msg);
		}
	}

	/** Send the player a message */
	public static void sendChatMsg(String msg) {
		mc().thePlayer.addChatMessage(new ChatComponentTranslation(msg, new Object[0]));
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {

						if (inetAddr.isSiteLocalAddress()) {
							return inetAddr;
						}
						else if (candidateAddress == null) {
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress;
			}
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		}
		catch (Exception e) {
			System.out.println("Cannot get the local host address. Setting it to '0.0.0.0'. Info: " + e);
			return null;
		}
	}
}
