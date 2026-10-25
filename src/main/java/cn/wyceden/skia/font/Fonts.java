package cn.wyceden.skia.font;

import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontVariation;
import io.github.humbleui.skija.Typeface;

public class Fonts {

	private static final String REGULAR = "Inter-Regular-CJKsc.ttf";
	private static final String MEDIUM = "Inter-Medium-CJKsc.ttf";
	private static final String ICON_FILL = "MaterialSymbolsRounded_Fill.ttf";
	private static final String ICON = "MaterialSymbolsRounded.ttf";

	private static final String CJK_VF = "MiSansVF.ttf";

	private static final Typeface REGULAR_TYPEFACE = FontHelper.getTypeface(REGULAR);
	private static final Typeface MEDIUM_TYPEFACE = FontHelper.getTypeface(MEDIUM);

	private static Typeface cjkTypeface;

	public static void loadAll() {
		FontHelper.preloadFonts(REGULAR, MEDIUM, ICON_FILL, ICON);
	}

	public static Font getRegular(float size) {
		return new Font(REGULAR_TYPEFACE, size);
	}

	public static Font getMedium(float size) {
		return new Font(MEDIUM_TYPEFACE, size);
	}

	public static Font getIconFill(float size) {
		return FontHelper.load(ICON_FILL, size);
	}

	public static Font getIcon(float size) {
		return FontHelper.load(ICON, size);
	}

	/**
	 * 返回与 baseFont 同字号、同字重的 MiSans 可变字体（MiSansVF.ttf）实例，用于渲染中文（CJK）字形。
	 * 通过 makeClone(new FontVariation("wght", weight)) 按字重取实例，支持任意字重；
	 * 若字体文件缺失则回退到 baseFont（Inter，其已含 CJK 子集），避免崩溃。
	 */
	public static Font getCJKFont(Font baseFont) {
		try {
			if (cjkTypeface == null) {
				cjkTypeface = FontHelper.getTypeface(CJK_VF);
			}
			int weight = baseFont.getTypeface() == MEDIUM_TYPEFACE ? 500 : 400;
			Typeface instance = cjkTypeface.makeClone(new FontVariation("wght", weight));
			return new Font(instance, baseFont.getSize());
		} catch (IllegalArgumentException e) {
			return baseFont;
		}
	}
}
