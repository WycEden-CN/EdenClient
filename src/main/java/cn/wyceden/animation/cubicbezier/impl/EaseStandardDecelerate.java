package cn.wyceden.animation.cubicbezier.impl;

import cn.wyceden.animation.cubicbezier.CubicBezier;

public class EaseStandardDecelerate extends CubicBezier {

	public EaseStandardDecelerate(float duration, float start, float end) {
		super(0F, 0F, 0F, 1F, duration, start, end);
	}
}
