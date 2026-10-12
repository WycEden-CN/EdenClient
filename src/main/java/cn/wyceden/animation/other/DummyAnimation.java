package cn.wyceden.animation.other;

import cn.wyceden.animation.Animation;
import cn.wyceden.animation.Delta;

public class DummyAnimation extends Animation {

	private float value;

	public DummyAnimation(float duration, float value) {
		super(duration, value, value);
		this.value = value;
	}

	public DummyAnimation(float value) {
		this(1, value);
	}

	public DummyAnimation() {
		this(1, 0);
	}

	@Override
	public float getValue() {
		timePassed += Delta.getDeltaTime();
		return value;
	}

	@Override
	protected float animate(float x) {
		return x;
	}
}
