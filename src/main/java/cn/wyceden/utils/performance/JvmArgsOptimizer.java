package cn.wyceden.utils.performance;

import cn.wyceden.utils.SystemSpecs;

/**
 * 依据本机规格自动推荐 JVM 启动参数（而非固定写死）。
 * 内存 &ge;8GB 使用 ZGC，否则使用 G1GC，并配套 Aikar 风格调优参数；
 * 堆大小按可用内存的一定比例分配并设上限，避免贪心导致系统卡顿。
 */
public class JvmArgsOptimizer {

	public static String recommendJvmArgs(PerformanceProfile profile) {
		long ramMB = SystemSpecs.getTotalPhysicalMemoryMB();
		PerformanceProfile p = profile.resolve(ramMB);
		long heapMB = computeHeapMB(ramMB, p);
		boolean highMem = ramMB >= 8192;

		StringBuilder sb = new StringBuilder();
		sb.append("-Xmx").append(heapMB).append("M ");
		sb.append("-Xms").append(Math.max(1024, heapMB / 2)).append("M ");
		sb.append("-XX:+UnlockExperimentalVMOptions ");
		if (highMem) {
			sb.append("-XX:+UseZGC -XX:+DisableExplicitGC -XX:+AlwaysPreTouch ")
				.append("-XX:+PerfDisableSharedMem -XX:-ZUncommit -XX:+ParallelRefProcEnabled");
		} else {
			sb.append("-XX:+UseG1GC -XX:+DisableExplicitGC -XX:+AlwaysPreTouch ")
				.append("-XX:+PerfDisableSharedMem -XX:+ParallelRefProcEnabled ")
				.append("-XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 ")
				.append("-XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M");
		}
		return sb.toString().trim();
	}

	private static long computeHeapMB(long ramMB, PerformanceProfile p) {
		long usable = Math.max(1024, ramMB - 2048); // 预留 2GB 给系统与其他程序
		double frac = (p == PerformanceProfile.PERFORMANCE) ? 0.8
				: (p == PerformanceProfile.QUALITY ? 0.5 : 0.65);
		long heap = (long) (usable * frac);
		long cap = (p == PerformanceProfile.QUALITY) ? 12288 : 10240;
		if (heap > cap) {
			heap = cap;
		}
		if (heap < 1024) {
			heap = 1024;
		}
		return heap;
	}
}
