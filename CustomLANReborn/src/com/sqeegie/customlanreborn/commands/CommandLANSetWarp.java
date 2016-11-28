/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.world.World
 */
package com.sqeegie.customlanreborn.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class CommandLANSetWarp
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "setwarp";
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
        return "/setwarp <warppoint>";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            try {
                EntityPlayerMP var3 = CommandLANSetWarp.getCommandSenderAsPlayer((ICommandSender)var1);
                File folder = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/warppoints/" + var3.worldObj.getSeed() + "/");
                File var5 = new File(folder, var2[0]);
                Properties var6 = new Properties();
                if (!var5.exists()) {
                    try {
                        folder.mkdirs();
                        var5.createNewFile();
                    }
                    catch (IOException e) {
                        var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Could not create warppoint files.", new Object[0]));
                        return;
                    }
                }
                FileOutputStream FOS = new FileOutputStream(var5);
                var6.setProperty("x", String.valueOf(var3.posX));
                var6.setProperty("y", String.valueOf(var3.posY));
                var6.setProperty("z", String.valueOf(var3.posZ));
                var6.store(FOS, (String)null);
                FOS.close();
                var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Warppoint \"" + var2[0] + "\" has been saved.", new Object[0]));
            }
            catch (IOException var8) {
                var8.printStackTrace();
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return null;
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

