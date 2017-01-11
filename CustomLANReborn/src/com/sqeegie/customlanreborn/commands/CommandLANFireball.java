package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandLANFireball
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "fireball";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/fireball";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            throw new WrongUsageException("/fireball", new Object[0]);
        }
        EntityPlayerMP entityplayer = CommandLANFireball.getCommandSenderAsPlayer((ICommandSender)var1);
        WorldServer world = MinecraftServer.getServer().worldServers[0];
        Vec3 look = entityplayer.getLookVec();
        EntityLargeFireball fireball = new EntityLargeFireball((World)world, (EntityLivingBase)entityplayer, 1.0, 1.0, 1.0);
        fireball.setPosition(entityplayer.posX + look.xCoord * 5.0, entityplayer.posY + look.yCoord * 5.0, entityplayer.posZ + look.zCoord * 5.0);
        fireball.accelerationX = look.xCoord * 0.1;
        fireball.accelerationY = look.yCoord * 0.1;
        fireball.accelerationZ = look.zCoord * 0.1;
        world.spawnEntityInWorld((Entity)fireball);
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

