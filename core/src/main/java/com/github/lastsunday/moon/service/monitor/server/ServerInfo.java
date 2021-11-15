package com.github.lastsunday.moon.service.monitor.server;

import java.util.LinkedList;
import java.util.List;

public class ServerInfo {

	/**
	 * CPU相关信息
	 */
	protected Cpu cpu = new Cpu();

	/**
	 * 內存相关信息
	 */
	protected Mem mem = new Mem();

	/**
	 * JVM相关信息
	 */
	protected Jvm jvm = new Jvm();

	/**
	 * 服务器相关信息
	 */
	protected Sys sys = new Sys();

	/**
	 * 磁盘相关信息
	 */
	protected List<SysFile> sysFiles = new LinkedList<SysFile>();

	public Cpu getCpu() {
		return cpu;
	}

	public void setCpu(Cpu cpu) {
		this.cpu = cpu;
	}

	public Mem getMem() {
		return mem;
	}

	public void setMem(Mem mem) {
		this.mem = mem;
	}

	public Jvm getJvm() {
		return jvm;
	}

	public void setJvm(Jvm jvm) {
		this.jvm = jvm;
	}

	public Sys getSys() {
		return sys;
	}

	public void setSys(Sys sys) {
		this.sys = sys;
	}

	public List<SysFile> getSysFiles() {
		return sysFiles;
	}

	public void setSysFiles(List<SysFile> sysFiles) {
		this.sysFiles = sysFiles;
	}

}
