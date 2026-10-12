package cn.wyceden.ui.component.impl;

import java.io.File;

import org.lwjgl.glfw.GLFW;

import cn.wyceden.Eden;
import cn.wyceden.management.color.api.ColorPalette;
import cn.wyceden.skia.Skia;
import cn.wyceden.skia.font.Fonts;
import cn.wyceden.ui.component.Component;
import cn.wyceden.ui.component.api.PressAnimation;
import cn.wyceden.ui.component.handler.impl.FileSelectorHandler;
import cn.wyceden.utils.Multithreading;
import cn.wyceden.utils.file.dialog.EdenFileDialog;
import cn.wyceden.utils.language.I18n;
import cn.wyceden.utils.mouse.MouseUtils;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;

public class FileSelector extends Component {

	private PressAnimation pressAnimation = new PressAnimation();
	private String[] extensions;
	private File file;

	public FileSelector(float x, float y, File file, String[] extensions) {
		super(x, y);
		width = 126;
		height = 32;
		this.file = file;
		this.extensions = extensions;
	}

	@Override
	public void draw(double mouseX, double mouseY) {

		ColorPalette palette = Eden.getInstance().getColorManager().getPalette();

		Skia.drawRoundedRect(x, y, width, height, 12, palette.getPrimary());
		Skia.save();
		Skia.clip(x, y, width, height, 12);
		pressAnimation.draw(x, y, width, height, palette.getPrimaryContainer(), 0.12F);
		Skia.restore();

		String fileName = file != null ? file.getName() : "None";

		Skia.drawFullCenteredText(fileName, x + (width / 2), y + (height / 2), palette.getSurface(),
				Fonts.getMedium(14));
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			pressAnimation.onPressed(mouseX, mouseY, x, y);
			Multithreading.runAsync(() -> {

				ObjectObjectImmutablePair<Boolean, File> p = EdenFileDialog.chooseFile(I18n.get("text.selectfile"), extensions);

				if (p.left()) {
					file = p.right();
					if (handler instanceof FileSelectorHandler) {
						((FileSelectorHandler) handler).onSelect(file);
					}
				}
			});
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		pressAnimation.onReleased(mouseX, mouseY, x, y);
	}
}
