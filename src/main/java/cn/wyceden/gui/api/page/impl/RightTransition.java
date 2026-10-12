package cn.wyceden.gui.api.page.impl;

import cn.wyceden.animation.Animation;
import cn.wyceden.gui.api.page.GuiTransition;

public class RightTransition extends GuiTransition {

	public RightTransition(boolean consecutive) {
		super(consecutive);
	}

	@Override
	public float[] onTransition(Animation animation) {

		float progress = animation.getValue();
		float x = 0;

		if (animation.getEnd() == 1) {
			x = 1 - progress;
		} else {
			x = 1 + -progress;
		}

		return new float[] { x, 0 };
	}
}
