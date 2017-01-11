package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.handlers.PermissionsHandler;
import com.sqeegie.customlanreborn.util.PlayerUtil;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANKick
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "kick";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "commands.kick.usage";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        if (par2ArrayOfStr.length > 0 && par2ArrayOfStr[0].length() > 1) {
            EntityPlayerMP var3 = PlayerUtil.getPlayerByUsername(par2ArrayOfStr[0]);
            EntityPlayerMP sender = CommandLANKick.getCommandSenderAsPlayer((ICommandSender)par1ICommandSender);
            if (!par2ArrayOfStr[0].equalsIgnoreCase(sender.mcServer.getServerOwner())) {
                String var4 = "You have been kicked from this server by " + sender.getDisplayName() + ".";
                boolean var5 = false;
                if (var3 == null) {
                    throw new PlayerNotFoundException();
                }
                if (par2ArrayOfStr.length >= 2) {
                    var4 = "You have been kicked from this server by " + sender.getDisplayName() + ".\nReason: " + CommandLANKick.func_82360_a((ICommandSender)par1ICommandSender, (String[])par2ArrayOfStr, (int)1);
                    var5 = true;
                }
                var3.playerNetServerHandler.kickPlayerFromServer(var4);
                if (var5) {
                    //CommandLANKick.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.kick.success.reason", (Object[])new Object[]{var3.getDisplayName(), var4});
                	par1ICommandSender.addChatMessage((IChatComponent)new ChatComponentTranslation("Kicked successfully.", new Object[0]));
                } else {
                    //CommandLANKick.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.kick.success", (Object[])new Object[]{var3.getDisplayName()});
                	par1ICommandSender.addChatMessage((IChatComponent)new ChatComponentTranslation("Kicked successfully.", new Object[0]));
                }
            } else {
                par1ICommandSender.addChatMessage((IChatComponent)new ChatComponentTranslation("You cannot kick the owner of this server.", new Object[0]));
            }
        } else {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        return par2ArrayOfStr.length >= 1 ? CommandLANKick.getListOfStringsMatchingLastWord((String[])par2ArrayOfStr, (String[])MinecraftServer.getServer().getAllUsernames()) : null;
    }
}

