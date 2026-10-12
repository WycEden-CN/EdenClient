package cn.wyceden.animation.cubicbezier.impl;

import cn.wyceden.animation.cubicbezier.CubicBezier;

public class EaseStandardAccelerate extends CubicBezier {

	public EaseStandardAccelerate(float duration, float start, float end) {
		super(0.3F, 0F, 1F, 1F, duration, start, end);
	}
}
