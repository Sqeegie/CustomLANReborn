package com.sqeegie.customlanreborn.config;

import com.sqeegie.customlanreborn.core.CustomLANReborn;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class GuiCustomLANRebornPermissions extends GuiScreen
{
	private static File optionsPermissions = new File(Minecraft.getMinecraft().mcDataDir + "/config/CustomLANReborn/", "perms.cfg");
	private static int incrementHeight = 0;
	private static int heightGo = -2;
	private static int widthNumber = 0;
	private static Map<String, Integer> permMap = new LinkedHashMap();
	private static Map<String, GuiButton> btnMap = new LinkedHashMap();
	private static int OPOnly = 2;
	private static int Everyone = 0;
	private static int page = 1;
	private static int btnID = 200;
	private GuiNextPage btnNext;
	private final GuiScreen theGuiScreen;
	private static boolean busy = false;
	private static Timer timer = new Timer();

	public GuiCustomLANRebornPermissions(GuiScreen par1GuiScreen)
	{
		initMap();
		btnMap.clear();
		btnID = 200;
		page = 1;
		theGuiScreen = par1GuiScreen;
	}

	public static void initMap()
	{
		permMap.put("back", Integer.valueOf(Everyone));
		permMap.put("ban", Integer.valueOf(OPOnly));
		permMap.put("unban", Integer.valueOf(OPOnly));
		permMap.put("banlist", Integer.valueOf(OPOnly));
		permMap.put("kick", Integer.valueOf(OPOnly));
		permMap.put("tp", Integer.valueOf(OPOnly));
		permMap.put("sethome", Integer.valueOf(Everyone));
		permMap.put("home", Integer.valueOf(Everyone));
		permMap.put("setspawn", Integer.valueOf(OPOnly));
		permMap.put("spawn", Integer.valueOf(Everyone));
		permMap.put("setwarp", Integer.valueOf(OPOnly));
		permMap.put("removewarp", Integer.valueOf(OPOnly));
		permMap.put("warplist", Integer.valueOf(OPOnly));
		permMap.put("warp", Integer.valueOf(Everyone));
		permMap.put("pm", Integer.valueOf(Everyone));
		permMap.put("fly", Integer.valueOf(OPOnly));
		permMap.put("god", Integer.valueOf(OPOnly));
		permMap.put("hat", Integer.valueOf(Everyone));
		permMap.put("heal", Integer.valueOf(OPOnly));
		permMap.put("eat", Integer.valueOf(OPOnly));
		permMap.put("fireball", Integer.valueOf(OPOnly));
		permMap.put("lightning", Integer.valueOf(OPOnly));
		permMap.put("explosion", Integer.valueOf(OPOnly));
		permMap.put("spawnmob", Integer.valueOf(OPOnly));
		permMap.put("broadcast", Integer.valueOf(OPOnly));
		permMap.put("gettime", Integer.valueOf(OPOnly));
		permMap.put("viewmotd", Integer.valueOf(Everyone));
	}

	public void initGui()
	{
		buttonList.clear();
		incrementHeight = 0;
		heightGo = -2;
		widthNumber = 0;
		checkOptions();
		buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done", new Object[0])));
		for (Map.Entry<String, Integer> entry : permMap.entrySet()) {
			if (!btnMap.containsKey(entry.getKey()))
			{
				if (btnID >= 200 + page * 14) {
					break;
				}
				GuiButton currentBtn = new GuiButton(++btnID, getWidth(), getHeight(), 150, 20, "/" + (String)entry.getKey() + ":");
				buttonList.add(currentBtn);
				btnMap.put((String)entry.getKey(), currentBtn);
			}
		}
		btnNext = new GuiNextPage(1, width / 2 - 155 + 0 + 288, height / 6 + 0 - 5 + 168, true);
		buttonList.add(btnNext);
		updateNames();
		if (buttonList.size() == 2)
		{
			btnMap.clear();
			btnID = 200;
			page = 1;
			initGui();
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if ((par1GuiButton.enabled) && (!busy))
		{
			if (par1GuiButton.id == 200)
			{
				saveOptions();
				mc.displayGuiScreen(theGuiScreen);
			}
			for (Map.Entry<String, GuiButton> entry : btnMap.entrySet()) {
				if (par1GuiButton.id == entry.getValue().id)
				{
					permMap.put((String)entry.getKey(), Integer.valueOf(2 - ((Integer)permMap.get(entry.getKey())).intValue()));
					break;
				}
			}
			if (par1GuiButton.id == 1)
			{
				saveOptions();
				page += 1;
				initGui();
			}
			updateNames();
			busy = true;
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					GuiCustomLANRebornPermissions.busy = false;
				}
			}, 50L);
		}
	}

	public void drawScreen(int par1, int par2, float par3)
	{
		int var2 = 0;
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "CustomLAN Reborn Command Permissions", width / 2, height / 6 + 24 * (var2 >> 1) - 30, 16777215);
		super.drawScreen(par1, par2, par3);
	}

	private void updateNames()
	{
		for (Map.Entry<String, Integer> entry : permMap.entrySet()) {
			try
			{
				GuiCustomLANRebornPermissions.btnMap.get((Object)entry.getKey()).displayString = ("/" + (String)entry.getKey() + ": " + (((Integer)entry.getValue()).intValue() == 2 ? "Ops" : "Everyone"));
			}
			catch (Exception localException) {}
		}
	}

	public static void checkOptions()
	{
		if (!optionsPermissions.exists()) {
			try
			{
				optionsPermissions.getParentFile().mkdir();
				optionsPermissions.createNewFile();
				saveOptions();
			}
			catch (Exception e)
			{
				CustomLANReborn.logger.error("Cannot save CustomLAN Reborn Port configs.");
			}
		}
		loadOptions();
	}

	public static void saveOptions()
	{
		try
		{
			PrintWriter var1 = new PrintWriter(new FileWriter(optionsPermissions));
			for (Map.Entry<String, Integer> entry : permMap.entrySet()) {
				var1.println((String)entry.getKey() + ":" + entry.getValue());
			}
			var1.close();
		}
		catch (Exception var3)
		{
			CustomLANReborn.logger.error("Failed to save the permissions file.");
			var3.printStackTrace();
		}
	}

	public static void loadOptions()
	{
		try
		{
			if (!optionsPermissions.exists())
			{
				checkOptions();
				return;
			}
			BufferedReader var1 = new BufferedReader(new FileReader(optionsPermissions));
			String var2 = "";
			while ((var2 = var1.readLine()) != null) {
				try
				{
					String[] var3 = var2.split(":");
					if (permMap.containsKey(var3[0])) {
						permMap.put(var3[0], Integer.valueOf(Integer.parseInt(var3[1])));
					}
				}
				catch (Exception var5)
				{
					CustomLANReborn.logger.error("Skipping bad option: " + var2.split(":")[0]);
				}
			}
			KeyBinding.resetKeyBindingArrayAndHash();
			var1.close();
		}
		catch (Exception var6)
		{
			CustomLANReborn.logger.error("Failed to load options");
			var6.printStackTrace();
		}
	}

	public static boolean checkPerm(String Command, ICommandSender sender)
	{
		if (((Integer)permMap.get(Command.toLowerCase())).intValue() == Everyone)
		{
			CustomLANReborn.logger.info("Everyone - true");
			return true;
		}
		if (((Integer)permMap.get(Command.toLowerCase())).intValue() == OPOnly)
		{
			if (CustomLANReborn.opslist.contains(sender.getCommandSenderName()))
			{
				CustomLANReborn.logger.info("OPOnly - found name in list - true");
				return true;
			}
			CustomLANReborn.logger.info("OPOnly - didn't find name in list - false");
			return false;
		}
		return false;
	}

	public static boolean canSenderUse(String Command, ICommandSender sender)
	{
		checkOptions();

		CustomLANReborn.logger.info("Checking perms for command " + Command + " and for player " + sender.getCommandSenderName() + ".");
		for (Map.Entry<String, Integer> entry : permMap.entrySet()) {
			if (Command.equalsIgnoreCase((String)entry.getKey()))
			{
				if (((Integer)entry.getValue()).intValue() == Everyone)
				{
					CustomLANReborn.logger.info("Everyone - true");
					return true;
				}
				if (((Integer)entry.getValue()).intValue() == OPOnly)
				{
					if (CustomLANReborn.opslist.contains(sender.getCommandSenderName()))
					{
						CustomLANReborn.logger.info("OPOnly - found name in list - true");
						return true;
					}
					CustomLANReborn.logger.info("OPOnly - didn't find name in list - false");
					return false;
				}
			}
		}
		if (((Command.equalsIgnoreCase("ban")) || (Command.equalsIgnoreCase("banip")) || (Command.equalsIgnoreCase("ban-ip"))) && (((Integer)permMap.get("Ban")).intValue() == Everyone)) {
			return true;
		}
		if (((Command.equalsIgnoreCase("ban")) || (Command.equalsIgnoreCase("banip")) || (Command.equalsIgnoreCase("ban-ip"))) && (((Integer)permMap.get("Ban")).intValue() == OPOnly))
		{
			if (CustomLANReborn.opslist.contains(sender.getCommandSenderName())) {
				return true;
			}
			return false;
		}
		if (((Command.equalsIgnoreCase("unban")) || (Command.equalsIgnoreCase("unbanip")) || (Command.equalsIgnoreCase("unban-ip"))) && (((Integer)permMap.get("Unban")).intValue() == Everyone)) {
			return true;
		}
		if (((Command.equalsIgnoreCase("unban")) || (Command.equalsIgnoreCase("unbanip")) || (Command.equalsIgnoreCase("unban-ip"))) && (((Integer)permMap.get("Unban")).intValue() == OPOnly))
		{
			if (CustomLANReborn.opslist.contains(sender.getCommandSenderName())) {
				return true;
			}
			return false;
		}
		return false;
	}

	public void onGuiClosed() {}

	private int getHeight()
	{
		int base = height / 6 + 0 - 5;
		if (heightGo % 2 == 0) {}
		heightGo += 1;
		return base + (incrementHeight = (int)(incrementHeight + 10.5D));
	}

	private int getWidth()
	{
		if (widthNumber == 1)
		{
			widthNumber = 0;
			return width / 2 - 155 + 0 + 160;
		}
		widthNumber = 1;
		return width / 2 - 155 + 0 - 2;
	}
}
