/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 */
package com.sqeegie.customlanreborn.commands;

import com.sqeegie.customlanreborn.commands.CommandLANBase;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANViewMOTD
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "viewmotd";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/viewmotd";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
        }
        EntityPlayerMP sender = CommandLANViewMOTD.getCommandSenderAsPlayer((ICommandSender)var1);
        sender.addChatMessage((IChatComponent)new ChatComponentTranslation("MOTD: " + MinecraftServer.getServer().getMOTD(), new Object[0]));
    }
}

