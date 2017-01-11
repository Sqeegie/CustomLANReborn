package com.sqeegie.customlanreborn.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class CommandLANRemoveWarp
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "removewarp";
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
        return "/removewarp <warppoint>";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length != 0) {
            EntityPlayerMP var3 = CommandLANRemoveWarp.getCommandSenderAsPlayer((ICommandSender)var1);
            File folder = new File(Minecraft.getMinecraft().mcDataDir + "/CustomLAN/warppoints/" + var3.worldObj.getSeed() + "/");
            File var5 = new File(folder, var2[0]);
            Properties var6 = new Properties();
            if (!var5.exists()) {
                var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Could not find warppoint \"" + var2[0] + "\".", new Object[0]));
                return;
            }
            if (var5.delete()) {
                var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Warppoint \"" + var2[0] + "\" has been removed.", new Object[0]));
            } else {
                var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Could not remove \"" + var2[0] + "\".", new Object[0]));
            }
        } else {
            throw new WrongUsageException("/removewarp <warppoint>", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        try {
            EntityPlayerMP var3 = CommandLANRemoveWarp.getCommandSenderAsPlayer((ICommandSender)var1);
            File folder = new File(Minecraft.getMinecraft().mcDataDir + "/warppoints/" + var3.worldObj.getSeed() + "/");
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

