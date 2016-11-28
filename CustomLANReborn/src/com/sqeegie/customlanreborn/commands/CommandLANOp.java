package com.sqeegie.customlanreborn.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mojang.authlib.GameProfile;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CommandLANOp extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "op";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "commands.op.usage";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        /*
    	if (par2ArrayOfStr.length == 1 && par2ArrayOfStr[0].length() > 0) {
        	EntityPlayerMP player = CustomLANReborn.getPlayerByUsername(par2ArrayOfStr[0]);
            CustomLANReborn.addOp(player);
            EntityPlayerMP var3 = CommandLANOp.getCommandSenderAsPlayer((ICommandSender)par1ICommandSender);
            File var4 = new File(Minecraft.getMinecraft().mcDataDir.getAbsoluteFile() + "/config/CustomLANReborn/", "ops.cfg");
            try {
                BufferedWriter var5 = new BufferedWriter(new FileWriter(var4, true));
                var5.append(par2ArrayOfStr[0] + "\n");
                var5.close();
            }
            catch (IOException var6) {
                var6.printStackTrace();
            }
        } else {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
        CustomLANReborn.loadOps();
        //CommandLANOp.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.op.success", (Object[])new Object[]{par2ArrayOfStr[0]});
        par1ICommandSender.addChatMessage(new ChatComponentText("Successfully opped " + par2ArrayOfStr[0] + "."));
        */
    	if (par2ArrayOfStr.length == 1 && par2ArrayOfStr[0].length() > 0)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(par2ArrayOfStr[0]);

            if (gameprofile == null)
            {
                throw new CommandException("commands.op.failed", new Object[] {par2ArrayOfStr[0]});
            }
            else
            {
                minecraftserver.getConfigurationManager().func_152605_a(gameprofile);
                func_152373_a(par1ICommandSender, this, "commands.op.success", new Object[] {par2ArrayOfStr[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
    	/*
        if (par2ArrayOfStr.length == 1) {
            String var3 = par2ArrayOfStr[par2ArrayOfStr.length - 1];
            ArrayList<String> var4 = new ArrayList<String>();
            for (String var8 : MinecraftServer.getServer().getAllUsernames()) {
            	GameProfile var82 = CustomLANReborn.getGameProfileByUsername(var8);
                if (MinecraftServer.getServer().getConfigurationManager().func_152596_g(var82) || !CommandLANOp.doesStringStartWith((String)var3, (String)var8)) continue;
                var4.add(var8);
            }
            return var4;
        }
        return null;
        */
        if (par2ArrayOfStr.length == 1)
        {
            String s = par2ArrayOfStr[par2ArrayOfStr.length - 1];
            ArrayList arraylist = new ArrayList();
            GameProfile[] agameprofile = MinecraftServer.getServer().func_152357_F();
            int i = agameprofile.length;

            for (int j = 0; j < i; ++j)
            {
                GameProfile gameprofile = agameprofile[j];

                if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(gameprofile) && doesStringStartWith(s, gameprofile.getName()))
                {
                    arraylist.add(gameprofile.getName());
                }
            }

            return arraylist;
        }
        else
        {
            return null;
        }
    }
}

