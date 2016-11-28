package com.sqeegie.customlanreborn.config;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiNextPage
extends GuiButton
{
	private final boolean useRightArrow;

	public GuiNextPage(int ID, int posX, int posY, boolean par4)
	{
		super(ID, posX, posY, 23, 13, null);
		useRightArrow = par4;
	}

	public void drawButton(Minecraft par1Minecraft, int posX, int posY)
	{
		if (visible)
		{
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
