package com.sqeegie.customlanreborn.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mojang.authlib.GameProfile;
import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanList;
import net.minecraft.server.management.ServerConfigurationManager;

public class CommandLANUnban
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "unban";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/unban <player>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 1 || var2[0].length() <= 0) {
            throw new WrongUsageException("/unban <player>", new Object[0]);
        }
        
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        GameProfile gameprofile = minecraftserver.getConfigurationManager().func_152608_h().func_152703_a(var2[0]);

        if (gameprofile == null)
        {
            throw new CommandException("commands.unban.failed", new Object[] {var2[0]});
        }
        else
        {
            minecraftserver.getConfigurationManager().func_152608_h().func_152684_c(gameprofile);
            func_152373_a(var1, this, "commands.unban.success", new Object[] {var2[0]});
        }
        
        
        
        //MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().remove(var2[0]);
        //CommandLANUnban.notifyAdmins((ICommandSender)var1, (String)("Player " + var2[0] + " has been unbanned."), (Object[])new Object[]{var2[0]});
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        //return var2.length == 1 ? CommandLANUnban.getListOfStringsFromIterableMatchingLastWord((String[])var2, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getBannedList().keySet()) : null;
        return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152685_a()) : null;
    }
}

