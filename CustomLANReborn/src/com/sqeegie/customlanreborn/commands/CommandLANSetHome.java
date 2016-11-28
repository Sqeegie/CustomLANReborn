package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class CommandLANSetHome
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "sethome";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());'
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/sethome";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        EntityPlayerMP var3 = CommandLANSetHome.getCommandSenderAsPlayer((ICommandSender)var1);
        if (var2.length == 0) {
            if (var3.worldObj != null) {
                ChunkCoordinates var10 = var3.getPlayerCoordinates();
                var3.setSpawnChunk(var10, true);
                //CommandLANSetHome.notifyAdmins((ICommandSender)var1, (String)"Your home has been set to this location.", (Object[])new Object[]{var3.getDisplayName(), var10.posX, var10.posY, var10.posZ});
                var1.addChatMessage(new ChatComponentText("Your home has been set to this location."));
            }
        } else {
            throw new WrongUsageException("/sethome", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

