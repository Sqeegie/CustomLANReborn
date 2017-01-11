package com.sqeegie.customlanreborn.commands;

import java.util.Locale;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class CommandLANGetTime
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "gettime";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/gettime";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length == 0) {
            EntityPlayerMP sender = CommandLANGetTime.getCommandSenderAsPlayer((ICommandSender)var1);
            try {
                String ampm;
                long worldDayLong = sender.worldObj.getTotalWorldTime() / 24000;
                String worldDay = String.format(Locale.ENGLISH, "%d", worldDayLong);
                long time = sender.worldObj.getWorldTime();
                long hour = (time / 1000 + 6) % 24 + 1;
                long minute = time % 1000 * 60 / 1000;
                if (hour > 13) {
                    hour -= 12;
                    ampm = "PM";
                } else {
                    ampm = "AM";
                }
                String worldTime = String.format(Locale.ENGLISH, "%2d:%02d", hour, minute);
                sender.addChatMessage((IChatComponent)new ChatComponentTranslation("Day " + worldDay + ", " + worldTime.replace(" ", "") + " " + ampm, new Object[0]));
            }
            catch (Exception e) {}
        } else {
            throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
        }
    }
}

