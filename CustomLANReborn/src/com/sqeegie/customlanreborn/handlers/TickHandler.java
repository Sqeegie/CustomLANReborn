package com.sqeegie.customlanreborn.handlers;

import com.sqeegie.customlanreborn.handlers.GUIHandler;
import com.sqeegie.customlanreborn.util.PlayerUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class TickHandler {
	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent event) {
		if ((Minecraft.getMinecraft().currentScreen instanceof GuiShareToLan)) {
			Minecraft.getMinecraft().displayGuiScreen(new GUIHandler(new GuiIngameMenu()));
		}
		else if (!(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)) {
		}
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (MinecraftServer.getServer().isServerRunning()) {
			try {
				String[] arr = Minecraft.getMinecraft().getIntegratedServer().getAllUsernames();
				for (int i = 0; i < arr.length; i++) {
					EntityPlayerMP player = PlayerUtil.getPlayerByUsername(arr[i]);
					String lastplayer = null;
					if ((player.isDead) && (lastplayer != player.getDisplayName())) {
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
						}
						catch (Exception e) {
							player.addChatMessage(new ChatComponentTranslation("Could not save last death location. /Back command will not be usable.", new Object[0]));
							System.out.println(e);
						}
					}
				}
			}
			catch (Exception localException1) {
			}
		}
	}
}
