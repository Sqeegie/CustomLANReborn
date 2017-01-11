package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.handlers.PermissionsHandler;
import com.sqeegie.customlanreborn.util.PlayerUtil;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;

public class CommandLANEat
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "eat";
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
        return "/eat [OptionalPlayerName]";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        block9 : {
            try {
                if (var2.length == 0) {
                    EntityPlayerMP var3 = CommandLANEat.getCommandSenderAsPlayer((ICommandSender)var1);
                    if (var3.capabilities.isCreativeMode) {
                        //CommandLANEat.notifyAdmins((ICommandSender)var1, (String)"Cannot eat while you are in Creative mode.", (Object[])new Object[]{var3.getDisplayName()});
                        var1.addChatMessage(new ChatComponentText("Cannot eat while you are in Creative mode."));
                        return;
                    }
                    var3.getFoodStats().setFoodLevel(40);
                    //CommandLANEat.notifyAdmins((ICommandSender)var1, (String)"You've been fed.", (Object[])new Object[]{var3.getDisplayName()});
                    var1.addChatMessage(new ChatComponentText("You've been fed."));
                    break block9;
                }
                if (var2.length < 1 || var2[0].length() <= 0) break block9;
                EntityPlayerMP var3 = null;
                String[] players = MinecraftServer.getServer().getAllUsernames();
                try {
                    for (String s : players) {
                        if (!s.toLowerCase().startsWith(var2[0].toLowerCase())) continue;
                        var2[0] = PlayerUtil.getPlayerByUsername(s).getDisplayName();
                        break;
                    }
                    var3 = CommandLANEat.getPlayer((ICommandSender)var1, (String)var2[0]);
                }
                catch (Exception e) {
                    return;
                }
                if (var3 == null) {
                    throw new PlayerNotFoundException();
                }
                if (var3.capabilities.isCreativeMode) {
                    //CommandLANEat.notifyAdmins((ICommandSender)var1, (String)("You cannot feed " + var2[0] + " while he is in Creative mode."), (Object[])new Object[]{var3.getDisplayName()});
                    var1.addChatMessage(new ChatComponentText("You cannot feed " + var2[0] + " while he is in Creative mode."));
                    return;
                }
                var3.getFoodStats().setFoodLevel(40);
                //CommandLANEat.notifyAdmins((ICommandSender)var1, (String)("Succesfully fed " + var2[0] + "."), (Object[])new Object[]{var3.getDisplayName()});
                var1.addChatMessage(new ChatComponentText("Succesfully fed " + var2[0] + "."));
                var3.addChatMessage((IChatComponent)new ChatComponentTranslation(var1.getCommandSenderName() + " has fed you.", new Object[0]));
            }
            catch (Exception e) {
                throw new WrongUsageException("/heal [OptionalPlayerName]", new Object[0]);
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        return var2.length != 1 && var2.length != 2 ? null : CommandLANEat.getListOfStringsMatchingLastWord((String[])var2, (String[])MinecraftServer.getServer().getAllUsernames());
    }

    public boolean isUsernameIndex(int var1) {
        return var1 == 0;
    }
}

