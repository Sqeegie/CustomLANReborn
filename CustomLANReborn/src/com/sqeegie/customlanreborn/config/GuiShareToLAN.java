package com.sqeegie.customlanreborn.config;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldSettings;

import org.lwjgl.input.Keyboard;

@SideOnly(value=Side.CLIENT)
public class GuiShareToLAN
extends GuiScreen {
    private final GuiScreen parentScreen;
    private GuiButton buttonAllowCommandsToggle;
    private GuiButton buttonGameMode;
    private GuiButton buttonSetDefaultIP;
    public static GuiTextField textfieldPort;
    public static GuiTextField textfieldIP;
    private static String gameMode;
    public static boolean allowCommands;

    public GuiShareToLAN(GuiScreen par1GuiScreen) {
        this.parentScreen = par1GuiScreen;
    }

    @SuppressWarnings("unchecked")
	public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height - 28, 150, 20, StatCollector.translateToLocal((String)"lanServer.start")));
        this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height - 28, 150, 20, StatCollector.translateToLocal((String)"gui.cancel")));
        this.buttonGameMode = new GuiButton(104, this.width / 2 - 155, 100, 150, 20, StatCollector.translateToLocal((String)"selectWorld.gameMode"));
        this.buttonList.add(this.buttonGameMode);
        this.buttonAllowCommandsToggle = new GuiButton(103, this.width / 2 + 5, 100, 150, 20, StatCollector.translateToLocal((String)"selectWorld.allowCommands"));
        this.buttonList.add(this.buttonAllowCommandsToggle);
        this.buttonSetDefaultIP = new GuiButton(105, this.width / 2 + 135, this.buttonGameMode.yPosition + 35, 20, 20, "...");
        this.buttonList.add(this.buttonSetDefaultIP);
        textfieldPort = new GuiTextField(this.fontRendererObj, this.width / 2 - 155, this.buttonGameMode.yPosition + 35, 80, 20);
        textfieldPort.setText(String.valueOf(CustomLANRebornConfig.GuiCustomLan.getServerPort()));
        textfieldPort.setCanLoseFocus(true);
        textfieldPort.setFocused(true);
        textfieldPort.setMaxStringLength(5);
        textfieldIP = new GuiTextField(this.fontRendererObj, this.width / 2 + 5, this.buttonGameMode.yPosition + 35, 150, 20);
        textfieldIP.setText(CustomLANRebornConfig.GuiCustomLan.getServerIP());
        textfieldIP.setCanLoseFocus(true);
        textfieldIP.setMaxStringLength(15);
        this.func_74088_g();
    }

    private void func_74088_g() {
        this.buttonGameMode.displayString = I18n.format((String)"selectWorld.gameMode", (Object[])new Object[0]) + " " + I18n.format((String)new StringBuilder().append("selectWorld.gameMode.").append(gameMode).toString(), (Object[])new Object[0]);
        this.buttonAllowCommandsToggle.displayString = I18n.format((String)"selectWorld.allowCommands", (Object[])new Object[0]) + " ";
        this.buttonAllowCommandsToggle.displayString = allowCommands ? this.buttonAllowCommandsToggle.displayString + I18n.format((String)"options.on", (Object[])new Object[0]) : this.buttonAllowCommandsToggle.displayString + I18n.format((String)"options.off", (Object[])new Object[0]);
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton.id == 102) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (par1GuiButton.id == 104) {
            gameMode = gameMode.equals("survival") ? "creative" : (gameMode.equals("creative") ? "adventure" : "survival");
            this.func_74088_g();
        } else if (par1GuiButton.id == 103) {
            allowCommands = !allowCommands;
            this.func_74088_g();
        } else if (par1GuiButton.id == 101) {
            this.mc.displayGuiScreen((GuiScreen)null);
            GuiShareToLAN.shareToLAN();
        } else if (par1GuiButton.id == 105) {
            try {
                textfieldIP.setText(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
            }
            catch (UnknownHostException e) {
                System.out.println("Cannot get the local host address. Setting it to '0.0.0.0'. Info: " + e);
                textfieldIP.setText("0.0.0.0");
            }
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal((String)"lanServer.title"), this.width / 2, 50, 16777215);
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal((String)"lanServer.otherPlayers"), this.width / 2, 82, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Port", this.width / 2 - 115, this.buttonGameMode.yPosition + 25, 10526880);
        this.drawCenteredString(this.fontRendererObj, "IP Address", this.width / 2 + 80, this.buttonGameMode.yPosition + 25, 10526880);
        textfieldPort.drawTextBox();
        textfieldIP.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    protected void drawScreen(int par1, int par2, int par3) {
        super.drawScreen(par1, par2, (float)par3);
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
    	CustomLANRebornConfig.GuiCustomLan.serverIP = textfieldIP.getText();
        if (textfieldPort.getText().isEmpty()) {
        	CustomLANRebornConfig.GuiCustomLan.serverPort = 25565;
            this.mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentTranslation("Port field is empty. Setting it to '25565'.", new Object[0]));
        } else {
        	CustomLANRebornConfig.GuiCustomLan.serverPort = Integer.valueOf(textfieldPort.getText());
        }
        CustomLANRebornConfig.GuiCustomLan.saveOptions();
        Keyboard.enableRepeatEvents((boolean)false);
    }

    protected void keyTyped(char par1, int par2) {
        if (textfieldPort.isFocused()) {
            textfieldPort.textboxKeyTyped(par1, par2);
        } else if (textfieldIP.isFocused()) {
            textfieldIP.textboxKeyTyped(par1, par2);
        }
        if (par1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        ((GuiButton)this.buttonList.get((int)0)).enabled = textfieldPort.getText().length() > 0;
        ((GuiButton)this.buttonList.get((int)0)).enabled = textfieldIP.getText().length() > 0;
    }

    public static void shareToLAN() {
        try {
            IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
            
            IChatComponent var3;
            Field isPublicField = null;
            Field lanServerPingField = null;
            
            server.func_147137_ag().addLanEndpoint(InetAddress.getByName(CustomLANRebornConfig.GuiCustomLan.getServerIP()), CustomLANRebornConfig.GuiCustomLan.getServerPort());
            try {
                isPublicField = IntegratedServer.class.getDeclaredField("field_71346_p");
                lanServerPingField = IntegratedServer.class.getDeclaredField("field_71345_q");
            }
            catch (Exception e) {
                try {
                    isPublicField = IntegratedServer.class.getDeclaredField("l");
                    lanServerPingField = IntegratedServer.class.getDeclaredField("m");
                }
                catch (Exception e2) {
                    try {
                        isPublicField = IntegratedServer.class.getDeclaredField("isPublic");
                        lanServerPingField = IntegratedServer.class.getDeclaredField("lanServerPing");
                    }
                    catch (Exception e3) {
                        var3 = new ChatComponentTranslation("Unable to access private fields. Are you using the wrong version?", new Object[0]);
                    }
                }
            }
            try {
                isPublicField.setAccessible(true);
                lanServerPingField.setAccessible(true);
                isPublicField.set((Object)server, true);
                ThreadLanServerPing TLSP = new ThreadLanServerPing(server.getMOTD(), "" + CustomLANRebornConfig.GuiCustomLan.getServerPort() + "");
                lanServerPingField.set((Object)server, (Object)TLSP);
                TLSP.start(); 
                server.getConfigurationManager().func_152604_a(WorldSettings.GameType.getByName((String)gameMode));
                server.getConfigurationManager().setCommandsAllowedForAll(allowCommands);
                //String var2 = "" + CustomLANRebornConfig.GuiCustomLan.getServerPort();
                var3 = new ChatComponentTranslation("Started on " + CustomLANRebornConfig.GuiCustomLan.getServerIP() + ":" + CustomLANRebornConfig.GuiCustomLan.getServerPort(), new Object[0]);
            }
            catch (Exception e) {
                var3 = new ChatComponentTranslation("Either you've entered incorrect information, or a server is already running on this port.");
                e.printStackTrace();
            }
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(var3);
        }
        catch (Exception e) {
            Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentTranslation("Either you've entered incorrect information, or a server is already running on this port.", new Object[0]));
            System.out.println(e);
        }
    }

    static {
        gameMode = "survival";
        allowCommands = false;
    }
}