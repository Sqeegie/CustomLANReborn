package com.sqeegie.customlanreborn.commands;

import com.sqeegie.customlanreborn.handlers.PermissionsHandler;
import com.sqeegie.customlanreborn.util.PlayerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANBroadcast
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "broadcast";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/broadcast";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String string : var2) {
                if (builder.length() > 0) {
                    builder.append(" ");
                }
                builder.append(string);
            }
            String[] arr = Minecraft.getMinecraft().getIntegratedServer().getAllUsernames();
            for (int i = 0; i < arr.length; ++i) {
                EntityPlayerMP player = PlayerUtil.getPlayerByUsername(arr[i]);
                player.addChatMessage((IChatComponent)new ChatComponentTranslation("\u00a74[BROADCAST] \u00A7f" + builder.toString(), new Object[0]));
            }
        } else {
            throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
        }
    }
}

