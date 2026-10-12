package cn.wyceden.ui.component.handler.impl;

import cn.wyceden.libraries.material3.hct.Hct;
import cn.wyceden.ui.component.handler.ComponentHandler;

public abstract class HctColorPickerHandler extends ComponentHandler {
	public abstract void onPicking(Hct hct);
}