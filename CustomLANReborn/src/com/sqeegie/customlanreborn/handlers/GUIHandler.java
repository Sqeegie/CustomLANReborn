package com.sqeegie.customlanreborn.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.sqeegie.customlanreborn.util.CommonUtil;

@SideOnly(value = Side.CLIENT)
public class GUIHandler extends GuiScreen {

	private final GuiScreen parentScreen;
	private GuiButton buttonAllowCommandsToggle;
	private GuiButton buttonGameMode;
	private GuiButton buttonSetDefaultIP;
	public static GuiTextField textfieldPort;
	public static GuiTextField textfieldIP;

	public GUIHandler(GuiScreen guiScreen) {
		parentScreen = guiScreen;
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20, StatCollector.translateToLocal((String) "lanServer.start")));
		buttonList.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20, StatCollector.translateToLocal((String) "gui.cancel")));
		// Gamemode button
		buttonGameMode = new GuiButton(104, width / 2 - 155, 100, 150, 20, StatCollector.translateToLocal((String) "selectWorld.gameMode"));
		buttonList.add(buttonGameMode);
		// Enable Cheats button
		buttonAllowCommandsToggle = new GuiButton(103, width / 2 + 5, 100, 150, 20, StatCollector.translateToLocal((String) "selectWorld.allowCommands"));
		buttonList.add(buttonAllowCommandsToggle);
		// IP Parser button
		buttonSetDefaultIP = new GuiButton(105, width / 2 + 135, buttonGameMode.yPosition + 35, 20, 20, "...");
		buttonList.add(buttonSetDefaultIP);
		// Port input field's properties
		textfieldPort = new GuiTextField(fontRendererObj, width / 2 - 155, buttonGameMode.yPosition + 35, 80, 20);
		textfieldPort.setText(String.valueOf(ConfigHandler.GuiCustomLan.getServerPort()));
		textfieldPort.setCanLoseFocus(true);
		textfieldPort.setFocused(true);
		textfieldPort.setMaxStringLength(5);
		// IP input field's properties
		textfieldIP = new GuiTextField(fontRendererObj, width / 2 + 5, buttonGameMode.yPosition + 35, 150, 20);
		textfieldIP.setText(ConfigHandler.GuiCustomLan.getServerIP());
		textfieldIP.setCanLoseFocus(true);
		textfieldIP.setMaxStringLength(15);
		refreshButtons();
	}

	private void refreshButtons() {
		buttonGameMode.displayString = I18n.format((String) "selectWorld.gameMode", (Object[]) new Object[0]) + " " + I18n.format((String) new StringBuilder().append("selectWorld.gameMode.").append(LANHandler.gameMode).toString(), (Object[]) new Object[0]);
		buttonAllowCommandsToggle.displayString = I18n.format((String) "selectWorld.allowCommands", (Object[]) new Object[0]) + " ";
		buttonAllowCommandsToggle.displayString = LANHandler.allowCommands ? buttonAllowCommandsToggle.displayString + I18n.format((String) "options.on", (Object[]) new Object[0]) : buttonAllowCommandsToggle.displayString + I18n.format((String) "options.off", (Object[]) new Object[0]);
		((GuiButton) buttonList.get((int) 0)).enabled = textfieldPort.getText().length() > 0;
		((GuiButton) buttonList.get((int) 0)).enabled = textfieldIP.getText().length() > 0;
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		// Button IDs
		final int parseIPAddress = 105;
		final int shareToLANButton = 101;
		final int enableCheatsButton = 103;
		final int switchGamemode = 104;
		final int cancelButton = 102;

		if (par1GuiButton.id == cancelButton) {
			mc.displayGuiScreen(parentScreen);
		}
		else if (par1GuiButton.id == switchGamemode) {
			LANHandler.gameMode = LANHandler.gameMode.equals("survival") ? "creative" : (LANHandler.gameMode.equals("creative") ? "adventure" : "survival");
			refreshButtons();
		}
		else if (par1GuiButton.id == enableCheatsButton) {
			LANHandler.allowCommands = !LANHandler.allowCommands;
			refreshButtons();
		}
		else if (par1GuiButton.id == shareToLANButton) {
			mc.displayGuiScreen((GuiScreen) null);
			LANHandler.shareToLAN();
		}
		else if (par1GuiButton.id == parseIPAddress) {
			try {
				String IP = CommonUtil.getLocalHostLANAddress().toString();
				if (IP != null) {
					if (IP.startsWith("/")) {
						IP = IP.replaceFirst("/", "");
					}
				}
				else {
					IP = "0.0.0.0";
				}
				textfieldIP.setText(String.valueOf(IP));
			}
			catch (UnknownHostException e) {
				System.out.println("Cannot get the local host address. Setting it to '0.0.0.0'. Info: " + e);
				textfieldIP.setText("0.0.0.0");
			}
		}
		refreshButtons();
	}

	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, StatCollector.translateToLocal((String) "lanServer.title"), width / 2, 50, 16777215);
		drawCenteredString(fontRendererObj, StatCollector.translateToLocal((String) "lanServer.otherPlayers"), width / 2, 82, 16777215);
		drawCenteredString(fontRendererObj, "Port", width / 2 - 115, buttonGameMode.yPosition + 25, 10526880);
		drawCenteredString(fontRendererObj, "IP Address", width / 2 + 80, buttonGameMode.yPosition + 25, 10526880);
		textfieldPort.drawTextBox();
		textfieldIP.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	protected void drawScreen(int par1, int par2, int par3) {
		super.drawScreen(par1, par2, (float) par3);
		textfieldPort.mouseClicked(par1, par2, par3);
		textfieldIP.mouseClicked(par1, par2, par3);
	}

	public void updateScreen() {
		textfieldPort.updateCursorCounter();
		textfieldIP.updateCursorCounter();
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		textfieldPort.mouseClicked(par1, par2, par3);
		textfieldIP.mouseClicked(par1, par2, par3);
	}

	public void onGuiClosed() {
		// Saves imputed IP address
		String IP = textfieldIP.getText();
		ConfigHandler.GuiCustomLan.setServerIP(IP);

		// Saves imputed port
		if (textfieldPort.getText().isEmpty()) {
			ConfigHandler.GuiCustomLan.setServerPort(ConfigHandler.GuiCustomLan.getDefaultServerPort());
			mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("Port field is empty. Setting it to default port '" + ConfigHandler.GuiCustomLan.getDefaultServerPort() + ".'", new Object[0]));
		}
		else {
			int port = Integer.valueOf(textfieldPort.getText());
			ConfigHandler.GuiCustomLan.setServerPort(port);
		}

		Keyboard.enableRepeatEvents((boolean) false);
	}

	protected void keyTyped(char par1, int par2) {
		if (textfieldPort.isFocused()) { // Enter a character into the port's textbox if said textbox is selected and something is typed
			textfieldPort.textboxKeyTyped(par1, par2);
		}
		else if (textfieldIP.isFocused()) { // Enter a character into the IP's textbox if said textbox is selected and something is typed
			textfieldIP.textboxKeyTyped(par1, par2);
		}
		if (par1 == '\r') {
			actionPerformed((GuiButton) buttonList.get(0));
		}
		((GuiButton) buttonList.get((int) 0)).enabled = textfieldPort.getText().length() > 0;
		((GuiButton) buttonList.get((int) 0)).enabled = textfieldIP.getText().length() > 0;
	}
}

/** Handles the "next page" button for the in-game permissions editor */
@SideOnly(Side.CLIENT)
class GuiNextPage extends GuiButton {

	private final boolean useRightArrow;

	public GuiNextPage(int ID, int posX, int posY, boolean par4) {
		super(ID, posX, posY, 23, 13, null);
		useRightArrow = par4;
	}

	public void drawButton(Minecraft par1Minecraft, int posX, int posY) {
		if (visible) {
			boolean var4 = (posX >= xPosition) && (posY >= yPosition) && (posX < xPosition + width) && (posY < yPosition + height);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			par1Minecraft.renderEngine.bindTexture(new ResourceLocation("customlanreborn:textures/cstmlan.png"));
			int var5 = 0;
			int var6 = 192;
			if (var4) {
				var5 += 23;
			}
			if (!useRightArrow) {
				var6 += 13;
			}
			drawTexturedModalRect(xPosition, yPosition, var5, var6, 23, 13);
		}
	}
}
