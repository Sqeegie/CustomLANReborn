package com.sqeegie.customlanreborn.commands;
/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.management.ServerConfigurationManager
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 */


import java.util.List;

import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANPM
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "pm";
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
        return "/pm <PlayerName> <Message>";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length >= 2) {
            EntityPlayerMP var3 = null;
            String[] players = MinecraftServer.getServer().getAllUsernames();
            try {
                for (String s : players) {
                    if (!s.toLowerCase().startsWith(var2[0].toLowerCase())) continue;
                    var3 = CustomLANReborn.getPlayerByUsername(s.toLowerCase());
                    break;
                }
                var3.sendPlayerAbilities();
            }
            catch (Exception e) {
                var1.addChatMessage((IChatComponent)new ChatComponentTranslation("Could not find a player starting with \"" + var2[0] + "\".", new Object[0]));
                return;
            }
            EntityPlayerMP sender = CommandLANPM.getCommandSenderAsPlayer((ICommandSender)var1);
            StringBuilder builder = new StringBuilder();
            for (String string : var2) {
                if (builder.length() > 0) {
                    builder.append(" ");
                }
                builder.append(string);
            }
            String out = builder.toString().replace(var3.getDisplayName() + " ", "");
            try {
                var3.addChatMessage((IChatComponent)new ChatComponentTranslation("PM with " + sender.getDisplayName() + ": " + out, new Object[0]));
                sender.addChatMessage((IChatComponent)new ChatComponentTranslation("PM with " + var3.getDisplayName() + ": " + out, new Object[0]));
            }
            catch (Exception e) {
                sender.addChatMessage((IChatComponent)new ChatComponentTranslation("Could not find player " + var2[0], new Object[0]));
            }
        } else {
            throw new WrongUsageException("/pm <PlayerName> <Message>", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return var2.length != 1 && var2.length != 2 ? null : CommandLANPM.getListOfStringsMatchingLastWord((String[])var2, (String[])MinecraftServer.getServer().getAllUsernames());
    }
}

