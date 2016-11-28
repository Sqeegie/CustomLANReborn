package com.sqeegie.customlanreborn.commands;

import java.util.Date;
import java.util.List;

import com.mojang.authlib.GameProfile;
import com.sqeegie.customlanreborn.config.GuiCustomLANRebornPermissions;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;

public class CommandLANBan
extends CommandLANBase {
	@Override
	public String getCommandName() {
		return "ban";
	}

	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return "commands.ban.usage";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
		//return GuiCustomLANRebornPermissions.canSenderUse(this.getCommandName()) || par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
		return (GuiCustomLANRebornPermissions.canSenderUse(getCommandName(), par1ICommandSender)) || (par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName()));
	}

	@Override
	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length >= 1 && par2ArrayOfStr[0].length() > 0)
		{
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(par2ArrayOfStr[0]);

			if (gameprofile == null)
			{
				throw new CommandException("commands.ban.failed", new Object[] {par2ArrayOfStr[0]});
			}
			else
			{
				String s = null;

				if (par2ArrayOfStr.length >= 2)
				{
					s = func_147178_a(par1ICommandSender, par2ArrayOfStr, 1).getUnformattedText();
				}

				UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, par1ICommandSender.getCommandSenderName(), (Date)null, s);
				minecraftserver.getConfigurationManager().func_152608_h().func_152687_a(userlistbansentry);
				EntityPlayerMP entityplayermp = minecraftserver.getConfigurationManager().func_152612_a(par2ArrayOfStr[0]);

				if (entityplayermp != null)
				{
					entityplayermp.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
				}

				func_152373_a(par1ICommandSender, this, "commands.ban.success", new Object[] {par2ArrayOfStr[0]});
			}
		}
		else
		{
			throw new WrongUsageException("commands.ban.usage", new Object[0]);
		}
	}

	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return par2ArrayOfStr.length >= 1 ? CommandLANBan.getListOfStringsMatchingLastWord((String[])par2ArrayOfStr, (String[])MinecraftServer.getServer().getAllUsernames()) : null;
	}
}

