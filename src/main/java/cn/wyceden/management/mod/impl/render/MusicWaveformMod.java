package cn.wyceden.management.mod.impl.render;

import cn.wyceden.Eden;
import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.RenderSkiaEvent;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.management.music.Music;
import cn.wyceden.management.music.MusicManager;
import cn.wyceden.management.music.MusicPlayer;
import cn.wyceden.skia.Skia;
import cn.wyceden.skia.font.Icon;
import cn.wyceden.utils.ColorUtils;

public class MusicWaveformMod extends Mod {

	public MusicWaveformMod() {
		super("mod.musicwaveform.name", "mod.musicwaveform.description", Icon.AIRWAVE, ModCategory.RENDER);
	}

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {

		MusicManager musicManager = Eden.getInstance().getMusicManager();
		Music m = musicManager.getCurrentMusic();

		int offsetX = 0;

		if (musicManager.isPlaying()) {

			for (int i = 0; i < MusicPlayer.SPECTRUM_BANDS; i++) {

				MusicPlayer.ANIMATIONS[i].onTick(MusicPlayer.VISUALIZER[i], 10);
				Skia.drawRect(offsetX, client.getWindow().getScaledHeight() + MusicPlayer.ANIMATIONS[i].getValue(), 10,
						client.getWindow().getScaledHeight(), ColorUtils.applyAlpha(m.getColor(), 80));

				offsetX += 10;
			}
		}
	};
}
