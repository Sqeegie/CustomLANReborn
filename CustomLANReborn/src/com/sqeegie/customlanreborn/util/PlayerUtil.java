package com.sqeegie.customlanreborn.util;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.mojang.authlib.GameProfile;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class PlayerUtil {
	
	private static Field field_152661_c;
	private static Method func_152668_a;
	
	public static EntityPlayerMP getPlayerByUsername(String username) {
		MinecraftServer mc = MinecraftServer.getServer();
		if (mc == null) {
			return null;
		}
		ServerConfigurationManager configurationManager = mc.getConfigurationManager();
		return configurationManager == null ? null : configurationManager.func_152612_a(username);
	}

	public static EntityPlayerMP getPlayerByMatchOrUsername(ICommandSender sender, String match) {
		EntityPlayerMP player = PlayerSelector.matchOnePlayer(sender, match);
		if (player != null) {
			return player;
		}
		return getPlayerByUsername(match);
	}

	public static GameProfile getGameProfileByUsername(String username) {
		EntityPlayer player = getPlayerByUsername(username);
		if (player != null) {
			if (!player.getGameProfile().isComplete()) {
				return new GameProfile(UUID.nameUUIDFromBytes(username.getBytes()), player.getCommandSenderName());
			}
			return player.getGameProfile();
		}
		return new GameProfile(UUID.nameUUIDFromBytes(username.getBytes()), username);
	}

	public static MovingObjectPosition getPlayerLookingSpot(EntityPlayer player) {
		if ((player instanceof EntityPlayerMP)) {
			return getPlayerLookingSpot(player, ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance());
		}
		return getPlayerLookingSpot(player, 5.0D);
	}

	public static MovingObjectPosition getPlayerLookingSpot(EntityPlayer player, double maxDistance) {
		Vec3 lookAt = player.getLook(1.0F);
		Vec3 playerPos = Vec3.createVectorHelper(player.posX, player.posY + (player.getEyeHeight() - player.getDefaultEyeHeight()), player.posZ);
		Vec3 pos1 = playerPos.addVector(0.0D, player.getEyeHeight(), 0.0D);
		Vec3 pos2 = pos1.addVector(lookAt.xCoord * maxDistance, lookAt.yCoord * maxDistance, lookAt.zCoord * maxDistance);
		return player.worldObj.rayTraceBlocks(pos1, pos2);
	}

	public static GameProfile getGameProfile(String username) {
		EntityPlayer player = getPlayerByUsername(username);
		if (player != null) {
			return player.getGameProfile();
		}
		username = username.toLowerCase(Locale.ROOT);
		try {
			Object cacheEntry = ((Map) field_152661_c.get(CommonUtil.mcServer().func_152358_ax())).get(username);
			if (cacheEntry != null) {
				return (GameProfile) func_152668_a.invoke(cacheEntry, new Object[0]);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return CommonUtil.mcServer().func_152358_ax().func_152655_a(username);
	}

	public static boolean isPlayerOP(String username) {
		GameProfile prof = getGameProfile(username);
		//return (prof != null) && (CommonUtil.mcServer().getConfigurationManager().func_152596_g(prof));
		return (CommonUtil.mcServer().getConfigurationManager().func_152596_g(prof));
	}

	public static boolean isPlayerOwner(String username) {
		return (CommonUtil.mcServer().isSinglePlayer()) && (CommonUtil.mcServer().getServerOwner().equalsIgnoreCase(username));
	}
	
}
