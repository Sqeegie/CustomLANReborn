package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.handlers.PermissionsHandler;
import com.sqeegie.customlanreborn.util.PlayerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandLANLightning
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "lightning";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
    	 return "/lightning [me|player] Smite yourself, another player, or the spot you are looking at.";
    }

    /*
    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        WorldServer worldServer;
        WorldClient world;
        if (var2.length == 0) {
            EntityPlayerMP entityplayer = CommandLANLightning.getCommandSenderAsPlayer((ICommandSender)var1);
            world = Minecraft.getMinecraft().theWorld;
            worldServer = MinecraftServer.getServer().worldServers[0];
            ChunkPosition hitVec = this.getPlayerLookingAtBlock(entityplayer, 125.0f);
            if (hitVec == null) {
                return;
            }
        } else {
            throw new WrongUsageException("/lightning", new Object[0]);
        }
        EntityLightningBolt entitybolt = new EntityLightningBolt((World)world, (double)hitVec.chunkPosX, (double)hitVec.chunkPosY, (double)hitVec.chunkPosZ);
        EntityLightningBolt entitybolt2 = new EntityLightningBolt((World)worldServer, (double)hitVec.chunkPosX, (double)hitVec.chunkPosY, (double)hitVec.chunkPosZ);
        entitybolt.setLocationAndAngles((double)hitVec.chunkPosX, (double)hitVec.chunkPosY, (double)hitVec.chunkPosZ, 0.0f, 0.0f);
        world.spawnEntityInWorld((Entity)entitybolt);
        worldServer.spawnEntityInWorld((Entity)entitybolt2);
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
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
    */
    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
    	
    	EntityPlayerMP senderz = PlayerUtil.getPlayerByUsername(sender.getCommandSenderName());
        if (args.length == 1)
        {
            if (args[0].toLowerCase().equals("me"))
            {
                senderz.worldObj.addWeatherEffect(new EntityLightningBolt(senderz.worldObj, senderz.posX, senderz.posY, senderz.posZ));
                //ChatOutputHandler.chatConfirmation(sender, "Was that really a good idea?");
                //sender.addChatMessage((IChatComponent)new ChatComponentTranslation("Was that really a good idea?", new Object[0]));
            }
            else
            {
                EntityPlayerMP player = PlayerUtil.getPlayerByMatchOrUsername(sender, args[0]);
                if (player != null)
                {
                    player.worldObj.addWeatherEffect(new EntityLightningBolt(player.worldObj, player.posX, player.posY, player.posZ));
                    //ChatOutputHandler.chatConfirmation(sender, "You should feel bad about doing that.");
                }
                else
                    //throw new TranslatedCommandException("Player %s does not exist, or is not online.", args[0]);
                    sender.addChatMessage((IChatComponent)new ChatComponentTranslation("Player " + args[0] + " does not exist, or is not online.", new Object[0]));
            }
        }
        else if (args.length > 1)
        {
            if (args.length != 3)
            {
                //throw new TranslatedCommandException("Need coordinates X, Y, Z.");
                sender.addChatMessage((IChatComponent)new ChatComponentTranslation("Need coordinates X, Y, Z.", new Object[0]));
            }
            int x = Integer.valueOf(args[0]);
            int y = Integer.valueOf(args[1]);
            int z = Integer.valueOf(args[2]);
            senderz.worldObj.addWeatherEffect(new EntityLightningBolt(senderz.worldObj, x, y, z));
            //ChatOutputHandler.chatConfirmation(sender, "I hope that didn't start a fire.");
        }
        else
        {
            MovingObjectPosition mop = PlayerUtil.getPlayerLookingSpot(senderz, 500);
            if (mop == null)
            {
                //ChatOutputHandler.chatError(sender, "You must first look at the ground!");
                sender.addChatMessage((IChatComponent)new ChatComponentTranslation("You must first look at the ground!", new Object[0]));
            }
            else
            {
                senderz.worldObj.addWeatherEffect(new EntityLightningBolt(senderz.worldObj, mop.blockX, mop.blockY, mop.blockZ));
                //ChatOutputHandler.chatConfirmation(sender, "I hope that didn't start a fire.");
            }
        }
}
}

