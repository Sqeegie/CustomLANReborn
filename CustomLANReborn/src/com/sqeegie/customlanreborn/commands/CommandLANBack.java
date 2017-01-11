package com.sqeegie.customlanreborn.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANBack
extends CommandParent {
    @Override
    public String getCommandName() {
        return "back";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    	return (PermissionsHandler.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(getRequiredPermissionLevel(), getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/back";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length == 0) {
            EntityPlayerMP player = CommandLANBack.getCommandSenderAsPlayer((ICommandSender)var1);
            File folder = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/lastpos/");
            File posFile = new File(folder, player.getCommandSenderName() + ".pos");
            if (posFile.exists()) {
                try {
                    FileInputStream FIS = new FileInputStream(posFile);
                    Properties prop = new Properties();
                    prop.load(FIS);
                    String tempLastPosX = prop.getProperty("x");
                    String tempLastPosY = prop.getProperty("y");
                    String tempLastPosZ = prop.getProperty("z");
                    String tempLastPosYaw = prop.getProperty("yaw");
                    String tempLastPosPitch = prop.getProperty("pitch");
                    double lastPosX = Double.parseDouble(tempLastPosX);
                    double lastPosY = Double.parseDouble(tempLastPosY);
                    double lastPosZ = Double.parseDouble(tempLastPosZ);
                    float lastPosYaw = Float.parseFloat(tempLastPosYaw);
                    float lastPosPitch = Float.parseFloat(tempLastPosPitch);
                    player.playerNetServerHandler.setPlayerLocation(lastPosX, lastPosY, lastPosZ, lastPosYaw, lastPosPitch);
                }
                catch (IOException e) {
                    player.addChatMessage((IChatComponent)new ChatComponentTranslation("Error. Could not get your last death point.", new Object[0]));
                }
            } else {
                player.addChatMessage((IChatComponent)new ChatComponentTranslation("You do not have a saved death point.", new Object[0]));
            }
        } else {
            throw new WrongUsageException("/back", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

