package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANReloadOps
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "reloadops";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        return par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/reloadops";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            throw new WrongUsageException("/reloadops", new Object[0]);
        }
        PermissionsHandler.loadOps();
        //var1.addChatMessage((IChatComponent)new ChatComponentTranslation("Reloaded ops. Current ops are: " + MinecraftServer.getServer().getConfigurationManager().getOps().toString().replace("]", "").replace("[", ""), new Object[0]));
        var1.addChatMessage((IChatComponent)new ChatComponentTranslation("Reloaded ops. Current ops are: " + PermissionsHandler.getOps().toString(), new Object[0]));
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

}

