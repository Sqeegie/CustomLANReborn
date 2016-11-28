package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;

public class CommandLANHome
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "home";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/home";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            throw new WrongUsageException("/home", new Object[0]);
        }
        EntityPlayerMP var3 = CommandLANHome.getCommandSenderAsPlayer((ICommandSender)var1);
        ChunkCoordinates var4 = var3.getBedLocation(var3.dimension);
        var3.playerNetServerHandler.setPlayerLocation((double)var4.posX, (double)var4.posY, (double)var4.posZ, var3.rotationYaw, var3.rotationPitch);
        var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Welcome home, " + var3.getDisplayName() + ".", new Object[0]));
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }
}

