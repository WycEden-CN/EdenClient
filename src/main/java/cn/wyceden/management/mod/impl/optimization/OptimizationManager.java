package cn.wyceden.management.mod.impl.optimization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import cn.wyceden.management.mod.impl.settings.SystemSettings;
import cn.wyceden.utils.SystemSpecs;
import cn.wyceden.utils.performance.JvmArgsOptimizer;
import cn.wyceden.utils.performance.PerformanceProfile;
import net.fabricmc.loader.api.FabricLoader;

/**
 * 为内置的优化模组（sodium / iris / lithium / entityculling / immediatelyfast 等）
 * 按所选/自动推断的档位写入“平衡画质与性能”的配置。
 *
 * 行为：
 * - 档位来源：系统设置中的“性能模式”（自动 / 平衡 / 性能优先 / 画质优先）。
 * - 自动档会依据本机物理内存推断（见 {@link PerformanceProfile}）。
 * - 仅在“目标档位变化”时重写配置（首次启动或用户在设置中切换档位），
 *   之后尊重用户的手动微调，不会每次启动覆盖。
 *
 * 扩展：新增某个模组的配置写入，在 {@link #applyToMods(Path, PerformanceProfile)} 中追加分支即可。
 */
public class OptimizationManager {

	private static final String MARKER = "eden/perf_profile.marker";

	public static void apply() {
		PerformanceProfile requested = readMode();
		PerformanceProfile effective = requested.resolve(SystemSpecs.getTotalPhysicalMemoryMB());

		Path configDir = FabricLoader.getInstance().getConfigDir();
		Path marker = configDir.resolve(MARKER);
		String last = readMarker(marker);

		if (last != null && last.equals(effective.name())) {
			logRecommendation(requested, effective);
			return; // 目标档位未变，尊重用户手动配置
		}

		applyToMods(configDir, effective);
		writeMarker(marker, effective.name());
		logRecommendation(requested, effective);
	}

	private static void applyToMods(Path configDir, PerformanceProfile profile) {
		FabricLoader loader = FabricLoader.getInstance();
		if (loader.getModContainer("sodium").isPresent()) {
			writeSodium(configDir, profile);
		}
		if (loader.getModContainer("sodium-extra").isPresent()) {
			writeSodiumExtra(configDir, profile);
		}
		if (loader.getModContainer("iris").isPresent()) {
			writeIris(configDir, profile);
		}
		if (loader.getModContainer("lithium").isPresent()) {
			writeLithium(configDir, profile);
		}
		if (loader.getModContainer("entityculling").isPresent()) {
			writeEntityCulling(configDir, profile);
		}
		if (loader.getModContainer("immediatelyfast").isPresent()) {
			writeImmediatelyFast(configDir, profile);
		}
		// viafabricplus / in-game-account-switcher 为功能性模组，无“画质/性能”预设，仅需启用（已随 jar 内置）。
	}

	/** Sodium：按档位写入 sodium-options.json（未知键会被 Sodium 忽略并回退默认，写错不会崩溃）。 */
	private static void writeSodium(Path configDir, PerformanceProfile p) {
		String g = (p == PerformanceProfile.PERFORMANCE) ? "FAST"
				: (p == PerformanceProfile.QUALITY ? "FANCY" : "DEFAULT");
		String clouds = (p == PerformanceProfile.PERFORMANCE) ? "OFF"
				: (p == PerformanceProfile.QUALITY ? "FANCY" : "DEFAULT");
		String leaves = (p == PerformanceProfile.PERFORMANCE) ? "FAST"
				: (p == PerformanceProfile.QUALITY ? "FANCY" : "DEFAULT");
		String biome = (p == PerformanceProfile.PERFORMANCE) ? "OFF"
				: (p == PerformanceProfile.QUALITY ? "HIGH" : "DEFAULT");
		String mipmap = (p == PerformanceProfile.PERFORMANCE) ? "OFF"
				: (p == PerformanceProfile.QUALITY ? "HIGH" : "DEFAULT");
		String animVisible = (p == PerformanceProfile.PERFORMANCE) ? "ON" : "DEFAULT";
		String entityCulling = (p == PerformanceProfile.PERFORMANCE) ? "ON" : "DEFAULT";

		String json = "{\n" +
				"  \"version\": 1,\n" +
				"  \"options\": {\n" +
				"    \"quality\": {\n" +
				"      \"graphics_quality\": \"" + g + "\",\n" +
				"      \"cloud_quality\": \"" + clouds + "\",\n" +
				"      \"weather_quality\": \"" + g + "\",\n" +
				"      \"leaves_quality\": \"" + leaves + "\",\n" +
				"      \"biome_blend\": \"" + biome + "\",\n" +
				"      \"mipmap_quality\": \"" + mipmap + "\",\n" +
				"      \"max_msaa\": \"DEFAULT\"\n" +
				"    },\n" +
				"    \"advanced\": {\n" +
				"      \"use_vertex_clustering\": \"DEFAULT\",\n" +
				"      \"use_chunk_multilayer_draw\": \"DEFAULT\",\n" +
				"      \"animate_only_visible_textures\": \"" + animVisible + "\",\n" +
				"      \"use_entity_culling\": \"" + entityCulling + "\",\n" +
				"      \"use_block_face_culling\": \"DEFAULT\",\n" +
				"      \"use_fog_occlusion\": \"DEFAULT\",\n" +
				"      \"use_sky_fixed_direction\": \"DEFAULT\",\n" +
				"      \"use_zoom_checker\": \"DEFAULT\",\n" +
				"      \"use_translucent_face_sorting\": \"DEFAULT\",\n" +
				"      \"use_partial_face_sorting\": \"DEFAULT\",\n" +
				"      \"use_explicit_sequence_update\": \"DEFAULT\"\n" +
				"    },\n" +
				"    \"performance\": {\n" +
				"      \"always_defer_chunk_updates\": \"DEFAULT\",\n" +
				"      \"chunk_update_threads\": \"DEFAULT\",\n" +
				"      \"use_synchronous_chunk_builds\": \"DEFAULT\",\n" +
				"      \"chunk_build_buffers\": \"DEFAULT\",\n" +
				"      \"chunk_build_time_budget\": \"DEFAULT\"\n" +
				"    }\n" +
				"  }\n" +
				"}\n";

		writeFile(configDir.resolve("sodium-options.json"), json);
	}

	/** Lithium：默认即全开优化；性能优先时保留全部（锂本身极致轻量），此处仅保证配置文件存在。 */
	private static void writeLithium(Path configDir, PerformanceProfile p) {
		String body = "# Eden auto-balanced profile: " + p.name() + "\n" +
				"config_version=1\n" +
				"mixins_enabled=true\n";
		writeFile(configDir.resolve("lithium.properties"), body);
	}

	/** EntityCulling：平衡档开启实体/粒子剔除；性能优先进一步强化。 */
	private static void writeEntityCulling(Path configDir, PerformanceProfile p) {
		boolean aggressive = p == PerformanceProfile.PERFORMANCE;
		String body = "# Eden auto-balanced profile: " + p.name() + "\n" +
				"enabled=true\n" +
				"cullingInterval=" + (aggressive ? 1 : 2) + "\n" +
				"particles=true\n" +
				"blockentities=" + (aggressive ? "true" : "true") + "\n";
		writeFile(configDir.resolve("entityculling.properties"), body);
	}

	/** ImmediatelyFast：在平衡/性能档开启快速渲染路径。 */
	private static void writeImmediatelyFast(Path configDir, PerformanceProfile p) {
		boolean on = p != PerformanceProfile.QUALITY;
		String body = "# Eden auto-balanced profile: " + p.name() + "\n" +
				"fast_buffer_upload=" + on + "\n" +
				"fast_text=" + on + "\n" +
				"fast_text_efficiency=true\n" +
				"fast_world_screen=true\n";
		writeFile(configDir.resolve("immediatelyfast.properties"), body);
	}

	/** Iris：自身无“画质/性能”预设（取决于所加载的着色器），仅稳妥开启原生内存分配器。其余交给用户与着色器。 */
	private static void writeIris(Path configDir, PerformanceProfile p) {
		String body = "# Eden auto-balanced profile: " + p.name() + "\n" +
				"native_memory_allocator=true\n";
		writeFile(configDir.resolve("iris.properties"), body);
	}

	/** Sodium Extra：其扩展选项随档位调整（性能优先时关闭部分额外特效以提速，画质优先时尽量开启）。 */
	private static void writeSodiumExtra(Path configDir, PerformanceProfile p) {
		boolean perf = p == PerformanceProfile.PERFORMANCE;
		String detail = (p == PerformanceProfile.QUALITY) ? "FANCY" : (perf ? "FAST" : "DEFAULT");
		String fastSearch = (p == PerformanceProfile.QUALITY) ? "DEFAULT" : "ON";
		String zoomFovFix = "DEFAULT";
		String json = "{\n" +
				"  \"version\": 1,\n" +
				"  \"options\": {\n" +
				"    \"extra\": {\n" +
				"      \"use_super_secret_settings\": \"DEFAULT\",\n" +
				"      \"animate_only_visible_textures\": \"" + detail + "\",\n" +
				"      \"use_fast_creative_search\": \"" + fastSearch + "\",\n" +
				"      \"prevent_shader_scrolling\": \"DEFAULT\",\n" +
				"      \"use_zoom_fov_fix\": \"" + zoomFovFix + "\"\n" +
				"    }\n" +
				"  }\n" +
				"}\n";
		writeFile(configDir.resolve("sodium-extra-options.json"), json);
	}

	private static void writeFile(Path path, String content) {
		try {
			Files.write(path, content.getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		} catch (IOException e) {
			// 配置写入失败不应影响客户端启动
		}
	}

	private static void logRecommendation(PerformanceProfile requested, PerformanceProfile effective) {
		System.out.println("[Eden] Performance profile: " + effective + " (requested=" + requested + ")");
		System.out.println("[Eden] Recommended JVM args: " + JvmArgsOptimizer.recommendJvmArgs(requested));
	}

	private static PerformanceProfile readMode() {
		String opt = SystemSettings.getInstance().getPerformanceModeSetting().getOption();
		if (SystemSettings.PM_PERFORMANCE.equals(opt)) {
			return PerformanceProfile.PERFORMANCE;
		}
		if (SystemSettings.PM_QUALITY.equals(opt)) {
			return PerformanceProfile.QUALITY;
		}
		if (SystemSettings.PM_BALANCED.equals(opt)) {
			return PerformanceProfile.BALANCED;
		}
		return PerformanceProfile.AUTO;
	}

	private static String readMarker(Path marker) {
		try {
			if (Files.exists(marker)) {
				return new String(Files.readAllBytes(marker), StandardCharsets.UTF_8).trim();
			}
		} catch (IOException ignored) {
		}
		return null;
	}

	private static void writeMarker(Path marker, String value) {
		try {
			Files.createDirectories(marker.getParent());
			Files.write(marker, value.getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		} catch (IOException ignored) {
		}
	}
}
