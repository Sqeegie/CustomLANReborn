package com.sqeegie.customlanreborn.commands;
/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.management.ServerConfigurationManager
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mojang.authlib.GameProfile;
import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandLANDeop extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "deop";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "commands.deop.usage";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
    	/*
        block6 : {
            if (par2ArrayOfStr.length == 1 && par2ArrayOfStr[0].length() > 0) {
                try {
                    EntityPlayerMP sender = CommandLANDeop.getCommandSenderAsPlayer((ICommandSender)par1ICommandSender);
                    //if (!par2ArrayOfStr[0].equalsIgnoreCase(MinecraftServer.getServer().getServerOwner())) {
                        File var3 = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/", "ops.cfg");
                        this.removeOp(var3, par2ArrayOfStr[0]);
                        FileReader reader = new FileReader(var3);
                        BufferedReader var4 = new BufferedReader(reader);
                        String var5 = "";
                        String var6 = "";
                        while ((var5 = var4.readLine()) != null) {
                            var6 = var6 + var5 + "\r\n";
                        }
                        var4.close();
                        String var7 = var6.replaceAll(par2ArrayOfStr[0], "");
                        var3.delete();
                        FileWriter var8 = new FileWriter(var3);
                        var8.write(var7);
                        var8.close();
                        reader.close();
                        EntityPlayerMP player = CustomLANReborn.getPlayerByUsername(par2ArrayOfStr[0]);
                        CustomLANReborn.removeOp(player);
                        CustomLANReborn.loadOps();
                        //CommandLANDeop.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.deop.success", (Object[])new Object[]{par2ArrayOfStr[0]});
                        sender.addChatMessage((IChatComponent)new ChatComponentTranslation("Deopped " + player.getDisplayName(), new Object[0]));
                        break block6;
                    //}
                    //sender.addChatMessage((IChatComponent)new ChatComponentTranslation("You cannot deop the owner of the server.", new Object[0]));
                }
                catch (IOException var9) {
                    var9.printStackTrace();
                }
            } else {
                throw new WrongUsageException("commands.deop.usage", new Object[0]);
            }
        }
        */
    	if (par2ArrayOfStr.length == 1 && par2ArrayOfStr[0].length() > 0)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.getConfigurationManager().func_152603_m().func_152700_a(par2ArrayOfStr[0]);

            if (gameprofile == null)
            {
                throw new CommandException("commands.deop.failed", new Object[] {par2ArrayOfStr[0]});
            }
            else
            {
                minecraftserver.getConfigurationManager().func_152610_b(gameprofile);
                func_152373_a(par1ICommandSender, this, "commands.deop.success", new Object[] {par2ArrayOfStr[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.deop.usage", new Object[0]);
        }
    }
    
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getConfigurationManager().func_152606_n()) : null;
    }

    /*
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        Iterator i = CustomLANReborn.getOps().iterator();
        ArrayList result = new ArrayList();
        while (i.hasNext()) {
            result.add(i.next());
        }
        result.remove(MinecraftServer.getServer().getServerOwner().toLowerCase());
        return par2ArrayOfStr.length == 1 ? result : null;
    }
    */
    /*
    private void removeOp(File myFile, String strngToDelete) throws IOException {
        try {
            File inputFile = myFile;
            if (!inputFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }
            File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().equalsIgnoreCase(strngToDelete)) continue;
                pw.println(line);
                pw.flush();
            }
            pw.close();
            br.close();
            if (!inputFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename file");
            }
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    */
}

