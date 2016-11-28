package com.sqeegie.customlanreborn.commands;

import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandLANBase extends CommandBase {
	
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    	// TODO: Remove commented out code in each canCommandSenderUse method in each command class.
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
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
