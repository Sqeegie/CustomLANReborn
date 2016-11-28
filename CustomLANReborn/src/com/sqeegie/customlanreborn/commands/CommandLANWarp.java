/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.world.World
 */
package com.sqeegie.customlanreborn.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandLANWarp
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "warp";
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
        return "/warp <warppoint>";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length == 1) {
            EntityPlayerMP var3 = CommandLANWarp.getCommandSenderAsPlayer((ICommandSender)var1);
            Properties var4 = new Properties();
            File folder = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/warppoints/" + var3.worldObj.getSeed() + "/");
            File var8 = new File(folder, var2[0]);
            try {
                FileInputStream FIS = new FileInputStream(var8);
                var4.load(FIS);
                String var9 = var4.getProperty("x");
                String var10 = var4.getProperty("y");
                String var11 = var4.getProperty("z");
                int var12 = Integer.parseInt(var9.substring(0, var9.indexOf(".")));
                int var13 = Integer.parseInt(var10.substring(0, var10.indexOf(".")));
                int var14 = Integer.parseInt(var11.substring(0, var11.indexOf(".")));
                var3.setPositionAndUpdate((double)var12, (double)var13, (double)var14);
                FIS.close();
                //CommandLANWarp.notifyAdmins((ICommandSender)var1, (String)("Warped to warppoint \"" + var2[0] + "\"."), (Object[])new Object[]{var3.getCommandSenderName()});
                var1.addChatMessage(new ChatComponentText("Warped to \"" + var2[0] + "\"."));
            }
            catch (IOException var15) {
                //CommandLANWarp.notifyAdmins((ICommandSender)var1, (String)("Could not find warppoint \"" + var2[0] + "\"."), (Object[])new Object[]{var3.getCommandSenderName()});
                var1.addChatMessage(new ChatComponentText("Cannot find warp \"" + var2[0] + "\"."));
            }
        } 
        else {
            throw new WrongUsageException("/warp <warppoint>", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        try {
            EntityPlayerMP var3 = CommandLANWarp.getCommandSenderAsPlayer((ICommandSender)var1);
            File folder = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/warppoints/" + var3.worldObj.getSeed() + "/");
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(folder.list()));
            return list;
        }
        catch (Exception e) {
            return null;
        }
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

