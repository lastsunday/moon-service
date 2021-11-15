package com.github.lastsunday.moon.service;

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.github.lastsunday.moon.service.monitor.server.Arith;
import com.github.lastsunday.moon.service.monitor.server.Cpu;
import com.github.lastsunday.moon.service.monitor.server.Jvm;
import com.github.lastsunday.moon.service.monitor.server.Mem;
import com.github.lastsunday.moon.service.monitor.server.ServerInfo;
import com.github.lastsunday.moon.service.monitor.server.Sys;
import com.github.lastsunday.moon.service.monitor.server.SysFile;
import com.github.lastsunday.service.core.util.IpUtils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

@Service
public class MonitorServiceImpl implements MonitorService {

	private static final int OSHI_WAIT_SECOND = 1000;

	@Override
	public ServerInfo fetchServerInfo() {
		ServerInfo result = new ServerInfo();
		try {
			SystemInfo si = new SystemInfo();
			HardwareAbstractionLayer hal = si.getHardware();
			result.setCpu(fetchCpuInfo(hal.getProcessor()));
			result.setJvm(fetchJvmInfo());
			result.setMem(fetchMemInfo(hal.getMemory()));
			result.setSys(fetchSysInfo());
			result.setSysFiles(fetchSysFiles(si.getOperatingSystem()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 设置CPU信息
	 */
	private Cpu fetchCpuInfo(CentralProcessor processor) {
		Cpu cpu = new Cpu();
		// CPU信息
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		Util.sleep(OSHI_WAIT_SECOND);
		long[] ticks = processor.getSystemCpuLoadTicks();
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
		cpu.setCpuNum(processor.getLogicalProcessorCount());
		cpu.setTotal(Arith.round(Arith.mul(totalCpu, 100), 2));
		cpu.setSys(Arith.round(Arith.mul(cSys / totalCpu, 100), 2));
		cpu.setUsed(Arith.round(Arith.mul(user / totalCpu, 100), 2));
		cpu.setWait(Arith.round(Arith.mul(iowait / totalCpu, 100), 2));
		cpu.setFree(Arith.round(Arith.mul(idle / totalCpu, 100), 2));
		return cpu;
	}

	private Mem fetchMemInfo(GlobalMemory memory) {
		Mem mem = new Mem();
		long total = memory.getTotal();
		long available = memory.getAvailable();
		mem.setTotal(Arith.div(total, (1024 * 1024 * 1024), 2));
		mem.setUsed(Arith.div(total - available, (1024 * 1024 * 1024), 2));
		mem.setFree(Arith.div(available, (1024 * 1024 * 1024), 2));
		mem.setUsage(Arith.mul(Arith.div(available, total, 4), 100));
		return mem;
	}

	private Sys fetchSysInfo() {
		Sys sys = new Sys();
		Properties props = System.getProperties();
		sys.setComputerName(IpUtils.getHostName());
		sys.setComputerIp(IpUtils.getHostIp());
		sys.setOsName(props.getProperty("os.name"));
		sys.setOsArch(props.getProperty("os.arch"));
		sys.setUserDir(props.getProperty("user.dir"));
		return sys;
	}

	private Jvm fetchJvmInfo() throws UnknownHostException {
		Jvm jvm = new Jvm();
		Properties props = System.getProperties();
		long total = Runtime.getRuntime().totalMemory();
		long max = Runtime.getRuntime().maxMemory();
		long free = Runtime.getRuntime().freeMemory();
		long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		jvm.setTotal(total);
		jvm.setMax(Arith.div(max, (1024 * 1024), 2));
		jvm.setFree(free);
		jvm.setVersion(props.getProperty("java.version"));
		jvm.setHome(props.getProperty("java.home"));
		jvm.setName(ManagementFactory.getRuntimeMXBean().getVmName());
		jvm.setStartTime(new Date(startTime));
		jvm.setRunTime(new Date().getTime() - startTime);
		jvm.setUsed(Arith.div(total - free, (1024 * 1024), 2));
		jvm.setUsage(Arith.mul(Arith.div(total - free, jvm.getTotal(), 4), 100));
		return jvm;
	}

	private List<SysFile> fetchSysFiles(OperatingSystem os) {
		List<SysFile> result = new ArrayList<SysFile>();
		FileSystem fileSystem = os.getFileSystem();
		List<OSFileStore> fsList = fileSystem.getFileStores();
		for (OSFileStore fs : fsList) {
			long free = fs.getUsableSpace();
			long total = fs.getTotalSpace();
			long used = total - free;
			SysFile sysFile = new SysFile();
			sysFile.setDirName(fs.getMount());
			sysFile.setSysTypeName(fs.getType());
			sysFile.setTypeName(fs.getName());
			sysFile.setTotal(total);
			sysFile.setFree(free);
			sysFile.setUsed(used);
			sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
			result.add(sysFile);
		}
		return result;
	}

}
