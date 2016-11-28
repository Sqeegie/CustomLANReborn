package com.sqeegie.customlanreborn.commands;
/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.ChunkPosition
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */


import java.util.List;

import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CommandLANExplosion
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "explosion";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || (par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName()));
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/explosion";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
    	/*
        World worldServer;
        EntityPlayerMP entityplayer;
        if (var2.length == 0) {
            entityplayer = CommandLANExplosion.getCommandSenderAsPlayer((ICommandSender)var1);
            worldServer = MinecraftServer.getServer().getEntityWorld();
            ChunkPosition hitVec = this.getPlayerLookingAtBlock(entityplayer, 125.0f);
            if (hitVec == null) {
                return;
            }
        } else {
            throw new WrongUsageException("/explosion", new Object[0]);
        }
        worldServer.newExplosion((Entity)entityplayer, (double)hitVec.chunkPosX, (double)hitVec.chunkPosY, (double)hitVec.chunkPosZ, 5.0f, true, true);
        */
        World worldServer = MinecraftServer.getServer().getEntityWorld();
        EntityPlayerMP player = CustomLANReborn.getPlayerByUsername(var1.getCommandSenderName());
        MovingObjectPosition mop = CustomLANReborn.getPlayerLookingSpot(player, 500);
        if (mop == null)
        {
            var1.addChatMessage((IChatComponent)new ChatComponentTranslation("You must first look at the ground!", new Object[0]));
        }
        else
        {
            worldServer.newExplosion((Entity)player, mop.blockX, mop.blockY, mop.blockZ, 4.0f, true, true);
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public ChunkPosition getPlayerLookingAtBlock(EntityPlayerMP player, float reach) {
        Vec3 vec3d = Vec3.createVectorHelper((double)player.posX, (double)(player.posY + 1.62 - (double)player.yOffset), (double)player.posZ);
        Vec3 vec3d1 = player.getLook(1.0f);
        Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * (double)reach, vec3d1.yCoord * (double)reach, vec3d1.zCoord * (double)reach);
        MovingObjectPosition hit = player.worldObj.rayTraceBlocks(vec3d, vec3d2);
        if (hit == null) {
            return null;
        }
        return new ChunkPosition(hit.blockX, hit.blockY, hit.blockZ);
    }
}

