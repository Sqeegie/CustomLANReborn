package com.sqeegie.customlanreborn.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.handlers.PermissionsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class CommandLANWarpList
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "warplist";
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
        return "/warplist";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        block5 : {
            if (var2.length == 0) {
                EntityPlayerMP var3 = CommandLANWarpList.getCommandSenderAsPlayer((ICommandSender)var1);
                try {
                    File folder = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/warppoints/" + var3.worldObj.getSeed() + "/");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(folder.list()));
                    if (!list.isEmpty()) {
                        var3.addChatMessage((IChatComponent)new ChatComponentTranslation("Saved warppoints are: \u00a76" + list.toString().replace("[", "").replace("]", ""), new Object[0]));
                        break block5;
                    }
                    var3.addChatMessage((IChatComponent)new ChatComponentTranslation("You have no saved warppoints.", new Object[0]));
                }
                catch (Exception e) {
                    var3.addChatMessage((IChatComponent)new ChatComponentTranslation("You have no saved warppoints.", new Object[0]));
                }
            } else {
                throw new WrongUsageException("/warplist", new Object[0]);
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

