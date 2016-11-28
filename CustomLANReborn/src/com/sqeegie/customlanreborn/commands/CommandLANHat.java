/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.PlayerNotFoundException
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.management.ServerConfigurationManager
 */
package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CommandLANHat
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "hat";
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
        return "/hat";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length == 0 || var2.length == 1) {
            EntityPlayerMP var3 = null;
            if (var2.length == 1) {
                String[] players = MinecraftServer.getServer().getAllUsernames();
                try {
                    for (String s : players) {
                        if (!s.toLowerCase().startsWith(var2[0].toLowerCase())) continue;
                        var2[0] = CustomLANReborn.getPlayerByUsername(s).getDisplayName();
                        break;
                    }
                    var3 = CommandLANHat.getPlayer((ICommandSender)var1, (String)var2[0]);
                }
                catch (Exception e) {
                    return;
                }
                if (var3 == null) {
                    throw new PlayerNotFoundException();
                }
            } else if (var2.length == 0) {
                var3 = CommandLANHat.getCommandSenderAsPlayer((ICommandSender)var1);
            }
            ItemStack itemInHand = var3.getCurrentEquippedItem();
            ItemStack existingHelm = var3.inventory.armorInventory[3];
            int itemInHandSlot = var3.inventory.currentItem;
            if (var3.inventory.mainInventory[itemInHandSlot] != null) {
                if (existingHelm != null) {
                    String itemInHandName = var3.getCurrentEquippedItem().getDisplayName();
                    var3.inventory.mainInventory[itemInHandSlot] = null;
                    var3.inventory.armorInventory[3] = itemInHand;
                    var3.inventory.mainInventory[var3.inventory.getFirstEmptyStack()] = existingHelm;
                    //CommandLANHat.notifyAdmins((ICommandSender)var1, (String)("You are now wearing " + itemInHandName + " as a hat."), (Object[])new Object[0]);
                    var1.addChatMessage(new ChatComponentText("You are now wearing " + itemInHandName + " as a hat."));
                } else {
                    String itemInHandName = var3.getCurrentEquippedItem().getDisplayName();
                    var3.inventory.mainInventory[itemInHandSlot] = null;
                    var3.inventory.armorInventory[3] = itemInHand;
                    //CommandLANHat.notifyAdmins((ICommandSender)var1, (String)("You are now wearing " + itemInHandName + " as a hat."), (Object[])new Object[0]);
                    var1.addChatMessage(new ChatComponentText("You are now wearing " + itemInHandName + " as a hat."));
                }
            } else {
                //CommandLANHat.notifyAdmins((ICommandSender)var1, (String)"You must have something to wear as a hat!", (Object[])new Object[0]);
                var1.addChatMessage(new ChatComponentText("You must have something to wear as a hat!"));
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

