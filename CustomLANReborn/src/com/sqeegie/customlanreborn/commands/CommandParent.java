package com.sqeegie.customlanreborn.commands;

import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandParent extends CommandBase {
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
		return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(getRequiredPermissionLevel(), getCommandName());
	}

	public String getCommandName() {
		return null;
	}

	public String getCommandUsage(ICommandSender var1) {
		return null;
	}

	public void processCommand(ICommandSender var1, String[] var2) {
	}
}
