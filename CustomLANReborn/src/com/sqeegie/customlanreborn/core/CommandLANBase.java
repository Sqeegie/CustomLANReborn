package com.sqeegie.customlanreborn.core;

import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandLANBase extends CommandBase
{
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(getRequiredPermissionLevel(), getCommandName());
	}

	public String getCommandName()
	{
		return null;
	}

	public String getCommandUsage(ICommandSender var1)
	{
		return null;
	}

	public void processCommand(ICommandSender var1, String[] var2) {}
}
