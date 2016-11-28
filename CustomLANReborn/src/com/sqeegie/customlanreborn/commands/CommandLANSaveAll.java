/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.management.ServerConfigurationManager
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.IProgressUpdate
 *  net.minecraft.world.MinecraftException
 *  net.minecraft.world.WorldServer
 */
package com.sqeegie.customlanreborn.commands;

import com.sqeegie.customlanreborn.commands.CommandLANBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandLANSaveAll
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "save-all";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        MinecraftServer var3 = MinecraftServer.getServer();
        par1ICommandSender.addChatMessage((IChatComponent)new ChatComponentTranslation(I18n.format((String)"commands.save.start", (Object[])new Object[0]), new Object[0]));
        if (var3.getConfigurationManager() != null) {
            var3.getConfigurationManager().saveAllPlayerData();
        }
        try {
            for (int var4 = 0; var4 < var3.worldServers.length; ++var4) {
                if (var3.worldServers[var4] == null) continue;
                WorldServer var5 = var3.worldServers[var4];
                boolean var6 = var5.levelSaving;
                var5.levelSaving = false;
                var5.saveAllChunks(true, (IProgressUpdate)null);
                var5.levelSaving = var6;
            }
        }
        catch (MinecraftException var7) {
            //CommandLANSaveAll.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.save.failed", (Object[])new Object[]{var7.getMessage()});
            par1ICommandSender.addChatMessage(new ChatComponentText("commands.save.failed"));
            return;
        }
        //CommandLANSaveAll.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.save.success", (Object[])new Object[0]);
        par1ICommandSender.addChatMessage(new ChatComponentText("commands.save.success"));
    }
}

