package com.sqeegie.customlanreborn.commands;

import java.util.List;

import com.sqeegie.customlanreborn.commands.CommandLANBase;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CommandLANTp
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "tp";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "commands.tp.usage";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        //return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
        return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        EntityPlayerMP var3;
        String[] players = MinecraftServer.getServer().getAllUsernames();
        if (par2ArrayOfStr.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        if (par2ArrayOfStr.length != 2 && par2ArrayOfStr.length != 4) {
            var3 = CommandLANTp.getCommandSenderAsPlayer((ICommandSender)par1ICommandSender);
        } else {
            try {
                for (String s : players) {
                    if (!s.toLowerCase().startsWith(par2ArrayOfStr[0].toLowerCase())) continue;
                    par2ArrayOfStr[0] = CustomLANReborn.getPlayerByUsername(s).getDisplayName();
                    break;
                }
            }
            catch (Exception e) {
                return;
            }
            var3 = CommandLANTp.getPlayer((ICommandSender)par1ICommandSender, (String)par2ArrayOfStr[0]);
            if (var3 == null) {
                throw new PlayerNotFoundException();
            }
        }
        if (par2ArrayOfStr.length != 3 && par2ArrayOfStr.length != 4) {
            if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2) {
                try {
                    for (String s : players) {
                        if (!s.toLowerCase().startsWith(par2ArrayOfStr[par2ArrayOfStr.length - 1].toLowerCase())) continue;
                        par2ArrayOfStr[par2ArrayOfStr.length - 1] = CustomLANReborn.getPlayerByUsername(s.toLowerCase()).getDisplayName().toLowerCase();
                        break;
                    }
                }
                catch (Exception e) {
                    return;
                }
                EntityPlayerMP var11 = CommandLANTp.getPlayer((ICommandSender)par1ICommandSender, (String)par2ArrayOfStr[par2ArrayOfStr.length - 1]);
                if (var11 == null) {
                    throw new PlayerNotFoundException();
                }
                if (var11.worldObj != var3.worldObj) {
                    //CommandLANTp.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.tp.notSameDimension", (Object[])new Object[0]);
                    par1ICommandSender.addChatMessage(new ChatComponentText("commands.tp.notSameDimension"));
                	return;
                }
                var3.playerNetServerHandler.setPlayerLocation(var11.posX, var11.posY, var11.posZ, var11.rotationYaw, var11.rotationPitch);
                //CommandLANTp.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.tp.success", (Object[])new Object[]{var3.getCommandSenderName(), var11.getCommandSenderName()});
                par1ICommandSender.addChatMessage(new ChatComponentText("commands.tp.success"));
            }
        } else if (var3.worldObj != null) {
            int var4 = par2ArrayOfStr.length - 3;
            double var5 = this.func_82368_a(par1ICommandSender, var3.posX, par2ArrayOfStr[var4++]);
            double var7 = this.func_82367_a(par1ICommandSender, var3.posY, par2ArrayOfStr[var4++], 0, 0);
            double var9 = this.func_82368_a(par1ICommandSender, var3.posZ, par2ArrayOfStr[var4++]);
            var3.setPositionAndUpdate(var5, var7, var9);
            //CommandLANTp.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.tp.success.coordinates", (Object[])new Object[]{var3.getCommandSenderName(), var5, var7, var9});
            par1ICommandSender.addChatMessage(new ChatComponentText("commands.tp.success.coordinates"));
        }
    }

    private double func_82368_a(ICommandSender par1ICommandSender, double par2, String par4Str) {
        return this.func_82367_a(par1ICommandSender, par2, par4Str, -30000000, 30000000);
    }

    private double func_82367_a(ICommandSender par1ICommandSender, double par2, String par4Str, int par5, int par6) {
        double var8;
        boolean var7 = par4Str.startsWith("~");
        double d = var8 = var7 ? par2 : 0.0;
        if (!var7 || par4Str.length() > 1) {
            boolean var10 = par4Str.contains(".");
            if (var7) {
                par4Str = par4Str.substring(1);
            }
            var8 += CommandLANTp.parseDouble((ICommandSender)par1ICommandSender, (String)par4Str);
            if (!var10 && !var7) {
                var8 += 0.5;
            }
        }
        if (par5 != 0 || par6 != 0) {
            if (var8 < (double)par5) {
                throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[]{var8, par5});
            }
            if (var8 > (double)par6) {
                throw new NumberInvalidException("commands.generic.double.tooBig", new Object[]{var8, par6});
            }
        }
        return var8;
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        return par2ArrayOfStr.length != 1 && par2ArrayOfStr.length != 2 ? null : CommandLANTp.getListOfStringsMatchingLastWord((String[])par2ArrayOfStr, (String[])MinecraftServer.getServer().getAllUsernames());
    }

    public boolean isUsernameIndex(int par1) {
        return par1 == 0;
    }
}

