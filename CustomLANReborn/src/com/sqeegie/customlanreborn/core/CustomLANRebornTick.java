package com.sqeegie.customlanreborn.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import com.sqeegie.customlanreborn.config.GuiShareToLAN;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CustomLANRebornTick {
	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent event) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiShareToLan) {
			//GuiScreen screen = Minecraft.getMinecraft().currentScreen;
			Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiShareToLAN((GuiScreen)new GuiIngameMenu()));
		} 
		else if (Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu) {
			// empty if block
		}
	}
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (MinecraftServer.getServer().isServerRunning()) {
			try {
				String[] arr = Minecraft.getMinecraft().getIntegratedServer().getAllUsernames();
				for (int i = 0; i < arr.length; ++i) {
					EntityPlayerMP player = CustomLANReborn.getPlayerByUsername(arr[i]);
					String lastplayer = null;
					if (!player.isDead || lastplayer == player.getDisplayName()) continue;
					lastplayer = player.getDisplayName();
					try {
						File folder = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/lastPos/");
						File posFile = new File(folder, player.getDisplayName() + ".pos");
						Properties prop = new Properties();
						if (!posFile.exists()) {
							folder.mkdirs();
							posFile.createNewFile();
						}
						FileOutputStream FOS = new FileOutputStream(posFile);
						prop.setProperty("x", String.valueOf(player.posX));
						prop.setProperty("y", String.valueOf(player.posY));
						prop.setProperty("z", String.valueOf(player.posZ));
						prop.setProperty("yaw", String.valueOf(player.cameraYaw));
						prop.setProperty("pitch", String.valueOf(player.cameraPitch));
						prop.store(FOS, null);
						FOS.close();
						continue;
					}
					catch (Exception e) {
						player.addChatMessage((IChatComponent)new ChatComponentTranslation("Could not save last death location. /Back command will not be usable.", new Object[0]));
						System.out.println(e);
					}
				}
			}
			catch (Exception e) {
				// empty catch block
			}
		}
	}
}

