/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.ChunkCoordinates
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.world.WorldServer
 */
package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;

public class CommandLANSpawn
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "spawn";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || (par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName()));
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/spawn";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            throw new WrongUsageException("/spawn", new Object[0]);
        }
        EntityPlayerMP var3 = CommandLANSpawn.getCommandSenderAsPlayer((ICommandSender)var1);
        WorldServer world = MinecraftServer.getServer().worldServers[0];
        ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
        double x = (double)chunkcoordinates.posX + 0.5;
        double y = chunkcoordinates.posY + 1;
        double z = (double)chunkcoordinates.posZ + 0.5;
        var3.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
        var3.setPositionAndUpdate(x, y, z);
        var3.setAngles(0.0f, 0.0f);
        var3.addChatMessage((IChatComponent)new ChatComponentTranslation("You have been teleported to spawn.", new Object[0]));
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

