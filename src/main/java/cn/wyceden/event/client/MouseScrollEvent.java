package cn.wyceden.event.client;

import cn.wyceden.event.Event;

public class MouseScrollEvent extends Event {

	private final double amount;

	public MouseScrollEvent(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}
}
