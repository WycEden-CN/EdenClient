package cn.wyceden.management.mod.impl.hud;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.RenderSkiaEvent;
import cn.wyceden.management.mod.api.hud.SimpleHUDMod;
import cn.wyceden.skia.font.Icon;
import com.viaversion.viafabricplus.ViaFabricPlus;

public class ProtocolVersionMod extends SimpleHUDMod {

	public ProtocolVersionMod() {
		super("mod.protocolversion.name", "mod.protocolversion.description", Icon.BRING_YOUR_OWN_IP);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return "Protocol Version: " + ViaFabricPlus.getImpl().getTargetVersion().getName();
	}

	@Override
	public String getIcon() {
		return Icon.BRING_YOUR_OWN_IP;
	}
}
