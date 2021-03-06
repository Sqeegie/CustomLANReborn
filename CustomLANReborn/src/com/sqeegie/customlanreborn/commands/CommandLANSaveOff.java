package com.sqeegie.customlanreborn.commands;

import com.sqeegie.customlanreborn.commands.CommandLANBase;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;

public class CommandLANSaveOff
extends CommandLANBase {
    @Override
    public String getCommandName() {
        return "save-off";
    }

    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        MinecraftServer var3 = MinecraftServer.getServer();
        for (int var4 = 0; var4 < var3.worldServers.length; ++var4) {
            if (var3.worldServers[var4] == null) continue;
            WorldServer var5 = var3.worldServers[var4];
            var5.levelSaving = true;
        }
        //CommandLANSaveOff.notifyAdmins((ICommandSender)par1ICommandSender, (String)"commands.save.disabled", (Object[])new Object[0]);
        par1ICommandSender.addChatMessage(new ChatComponentText("commands.save.disabled"));
    }
}

