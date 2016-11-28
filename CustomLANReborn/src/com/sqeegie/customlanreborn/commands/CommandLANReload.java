package com.sqeegie.customlanreborn.commands;
/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 */


import java.util.List;

import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import com.sqeegie.customlanreborn.config.GuiShareToLAN;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandLANReload
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "reload";
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
        return "/reload";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            throw new WrongUsageException("/reload", new Object[0]);
        }
        CommandLANStop.terminateServer(var1);
        GuiShareToLAN.shareToLAN();
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}
