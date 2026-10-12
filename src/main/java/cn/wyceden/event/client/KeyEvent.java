package cn.wyceden.event.client;

import cn.wyceden.event.Event;

public class KeyEvent extends Event {

	private final int keyCode;

	public KeyEvent(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
