package cn.wyceden.management.mod.api.hud.design.impl;

import java.awt.Color;

import cn.wyceden.Eden;
import cn.wyceden.management.color.api.ColorPalette;
import cn.wyceden.management.mod.api.hud.design.HUDDesign;
import cn.wyceden.skia.Skia;
import cn.wyceden.utils.ColorUtils;

import io.github.humbleui.skija.Font;

public class MaterialYouDesign extends HUDDesign {

	public MaterialYouDesign() {
		super("design.materialyou");
	}

	@Override
	public void drawBackground(float x, float y, float width, float height, float radius) {

		ColorPalette palette = Eden.getInstance().getColorManager().getPalette();

		Skia.drawRoundedBlur(x, y, width, height, radius);
		Skia.drawShadow(x, y, width, height, radius);
		Skia.drawGradientRoundedRect(x, y, width, height, radius,
				ColorUtils.applyAlpha(palette.getPrimaryContainer(), 180),
				ColorUtils.applyAlpha(palette.getTertiaryContainer(), 180));
	}

	@Override
	public void drawText(String text, float x, float y, Font font) {
		Skia.drawText(text, x, y, getTextColor(), font);
	}

	@Override
	public Color getTextColor() {
		return Color.WHITE;
	}

	@Override
	public Color getOnTextColor() {
		return Color.BLACK;
	}
}
