package cn.wyceden.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/** 运行时采集本机硬件规格，用于自动推断最优 JVM 参数与性能档位。 */
public class SystemSpecs {

	/** 物理内存总量（MB），采集失败回退 4096。 */
	public static long getTotalPhysicalMemoryMB() {
		try {
			OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
			return bean.getTotalPhysicalMemorySize() / (1024L * 1024L);
		} catch (Throwable t) {
			return 4096;
		}
	}

	public static int getAvailableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static String getOSName() {
		return System.getProperty("os.name", "unknown");
	}
}
