package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANFly
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "fly";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/fly [OptionalPlayerName]";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        block9 : {
            try {
                if (var2.length == 0) {
                    EntityPlayerMP player = CommandLANFly.getCommandSenderAsPlayer((ICommandSender)var1);
                    if (player.capabilities.isCreativeMode) {
                        //CommandLANFly.notifyAdmins((ICommandSender)var1, (String)"The fly command does not work while you are in Creative mode.", (Object[])new Object[0]);
                        player.addChatMessage((IChatComponent)new ChatComponentTranslation("The fly command does not work while you are in Creative mode."));
                        return;
                    }
                    player.capabilities.allowFlying = !player.capabilities.allowFlying;
                    player.capabilities.isFlying = false;
                    player.sendPlayerAbilities();
                    //CommandLANFly.notifyAdmins((ICommandSender)var1, (String)("Flying is now " + (player.capabilities.allowFlying ? "enabled." : "disabled.")), (Object[])new Object[0]);
                    player.addChatMessage((IChatComponent)new ChatComponentTranslation("Flying is now " + (player.capabilities.allowFlying ? "enabled." : "disabled.")));
                    break block9;
                }
                if (var2.length < 1 || var2[0].length() <= 0) break block9;
                EntityPlayerMP player = null;
                String[] players = MinecraftServer.getServer().getAllUsernames();
                try {
                    for (String s : players) {
                        if (!s.toLowerCase().startsWith(var2[0].toLowerCase())) continue;
                        var2[0] = CustomLANReborn.getPlayerByUsername(s).getDisplayName();
                        break;
                    }
                    player = CommandLANFly.getPlayer((ICommandSender)var1, (String)var2[0]);
                }
                catch (Exception e) {
                    return;
                }
                if (player == null) {
                    throw new PlayerNotFoundException();
                }
                if (player.capabilities.isCreativeMode) {
                    //CommandLANFly.notifyAdmins((ICommandSender)var1, (String)("The fly command does not work while " + player.getDisplayName() + " is in Creative mode."), (Object[])new Object[0]);
                    player.addChatMessage((IChatComponent)new ChatComponentTranslation("The fly command does not work while " + player.getDisplayName() + " is in Creative mode."));
                	return;
                }
                player.capabilities.allowFlying = !player.capabilities.allowFlying;
                player.capabilities.isFlying = false;
                player.sendPlayerAbilities();
                //CommandLANFly.notifyAdmins((ICommandSender)var1, (String)("Flying is now " + (player.capabilities.allowFlying ? "enabled" : "disabled") + " for " + player.getDisplayName() + "."), (Object[])new Object[0]);
                player.addChatMessage((IChatComponent)new ChatComponentTranslation("Flying is now " + (player.capabilities.allowFlying ? "enabled" : "disabled") + " for " + player.getDisplayName() + "."));
            }
            catch (Exception e) {
                throw new WrongUsageException("/fly [OptionalPlayerName]", new Object[0]);
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return var2.length != 1 && var2.length != 2 ? null : CommandLANFly.getListOfStringsMatchingLastWord((String[])var2, (String[])MinecraftServer.getServer().getAllUsernames());
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

