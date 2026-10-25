package cn.wyceden.utils.performance;

/**
 * 性能/画质档位。AUTO 会依据本机物理内存自动推断：
 * 内存较小（&lt;4GB）倾向性能优先，内存充足（&gt;12GB）倾向画质优先，中间为平衡。
 */
public enum PerformanceProfile {
	AUTO,
	BALANCED,
	PERFORMANCE,
	QUALITY;

	public PerformanceProfile resolve(long totalRamMB) {
		if (this != AUTO) {
			return this;
		}
		if (totalRamMB <= 0) {
			return BALANCED;
		}
		if (totalRamMB < 4096) {
			return PERFORMANCE;
		}
		if (totalRamMB > 12288) {
			return QUALITY;
		}
		return BALANCED;
	}
}
