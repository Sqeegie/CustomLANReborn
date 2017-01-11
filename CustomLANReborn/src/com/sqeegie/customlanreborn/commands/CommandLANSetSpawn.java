package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandLANSetSpawn
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "setspawn";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/setspawn";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            throw new WrongUsageException("/setspawn", new Object[0]);
        }
        EntityPlayerMP var3 = CommandLANSetSpawn.getCommandSenderAsPlayer((ICommandSender)var1);
        World world = var3.worldObj;
        WorldServer world2 = MinecraftServer.getServer().worldServers[0];
        ChunkCoordinates current = var3.getPlayerCoordinates();
        world.setSpawnLocation(current.posX, current.posY, current.posZ);
        world2.setSpawnLocation(current.posX, current.posY, current.posZ);
        ChunkCoordinates spawn = world.getSpawnPoint();
        var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Spawn coordinates set to (" + spawn.posX + ", " + spawn.posY + ", " + spawn.posZ + ").", new Object[0]));
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }
}

