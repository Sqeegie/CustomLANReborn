package com.sqeegie.customlanreborn.commands;
/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.network.NetHandlerPlayServer
 *  net.minecraft.network.NetworkSystem
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.integrated.IntegratedServer
 *  net.minecraft.server.management.ServerConfigurationManager
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 */


import java.lang.reflect.Field;
import java.util.List;

import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkSystem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANStop
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "stop";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || (par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName()));
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/stop";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length == 0) {
            if (CommandLANStop.terminateServer(var1).booleanValue()) {
                var1.addChatMessage((IChatComponent)new ChatComponentTranslation("Server stopped successfully.", new Object[0]));
            }
        } else {
            throw new WrongUsageException("/stop", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }

    public static Boolean terminateServer(ICommandSender var1) {
        block11 : {
            Field isPublicField = null;
            try {
                isPublicField = IntegratedServer.class.getDeclaredField("field_71346_p");
            }
            catch (Exception e) {
                try {
                    isPublicField = IntegratedServer.class.getDeclaredField("l");
                }
                catch (Exception e2) {
                    try {
                        isPublicField = IntegratedServer.class.getDeclaredField("isPublic");
                    }
                    catch (Exception e3) {
                        var1.addChatMessage((IChatComponent)new ChatComponentTranslation("Unable to access private fields. Are you using the wrong version?", new Object[0]));
                        return false;
                    }
                }
            }
            isPublicField.setAccessible(true);
            try {
                if (isPublicField.getBoolean((Object)Minecraft.getMinecraft().getIntegratedServer())) {
                    isPublicField.set((Object)Minecraft.getMinecraft().getIntegratedServer(), false);
                    break block11;
                }
                var1.addChatMessage((IChatComponent)new ChatComponentTranslation("No server is currently running.", new Object[0]));
                return false;
            }
            catch (IllegalAccessException e) {
                var1.addChatMessage((IChatComponent)new ChatComponentTranslation("Unable to access private fields. Are you using the wrong version?", new Object[0]));
                e.printStackTrace();
                return false;
            }
        }
        if (MinecraftServer.getServer().isServerRunning()) {
            String[] arr = Minecraft.getMinecraft().getIntegratedServer().getAllUsernames();
            for (int i = 0; i < arr.length; ++i) {
                if (arr[i] == Minecraft.getMinecraft().getIntegratedServer().getServerOwner()) continue;
                EntityPlayerMP player = CustomLANReborn.getPlayerByUsername(arr[i]);
                player.playerNetServerHandler.kickPlayerFromServer("Server is shutting down.");
            }
        }
        Minecraft.getMinecraft().getIntegratedServer().func_147137_ag().terminateEndpoints();
        return true;
    }
}

