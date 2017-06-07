package com.singland.console.mbean.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.singland.console.mbean.TomcatLog;

public class ShellCmdHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShellCmdHelper.class);
	
	/**
	 * systemd available start from RHEL 6.7/CentOS 7.1
	 * @return
	 */
	private static boolean isSupportSystemd() {
		Properties cfgProp = new Properties();
		try {
			cfgProp.load(TomcatLog.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String env = cfgProp.getProperty("env");
		LOGGER.debug(env);
		if ("uat".equalsIgnoreCase(env)) {
			return true;
		}
		return false;
	}
	
	public static String exec(String... cmds) {
		String result = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmds);
			proc.waitFor();
			InputStream is = proc.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(is));
			result = read.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getProcessStatus(String proc) {
		LOGGER.debug("iEsbMBean.getProcessStatus isSupportSystemd: " + isSupportSystemd());
		String status = "";
		if (isSupportSystemd()) {
			String cmd = String.format("systemctl status %s | grep 'Active:'", proc);
			String[] cmds = {"/bin/sh", "-c", cmd};
			String result = exec(cmds);
			if (result.contains("running")) {
				status = "STARTED";
			} else if (result.contains("failed") || result.contains("inactive")) {
				status = "STOPPED";
			}
		} else {
			String cmd = String.format("service %s status", proc);
			String[] cmds = {"/bin/sh", "-c", cmd};
			String result = exec(cmds);
			if (result.contains("running") || result.contains("started")) {
				status = "STARTED";
			} else if (result.contains("stopped")) {
				status = "STOPPED";
			}
		}
		return status;
	}
	
	public static String getHost() {
		// -c indicates it's a command
		String[] cmds = {"/bin/sh", "-c", "ifconfig eth0 | grep 'inet' | awk '{print $2}'"};
		String host = exec(cmds);
		/**
		 * RHEL is addr:xx.xx.xx.xx
		 */
		if (host != null && host.contains("addr:")) {
			host = host.replace("addr:", "");
		}
		return host;
	}
	
	public static void startStopProcess(String action, String proc) {
		String cmd = String.format("service %s %s", proc, action);
		if (isSupportSystemd()) {
			cmd = String.format("systemctl %s %s", action, proc);
		}
		String[] cmds = {"/bin/sh", "-c", cmd};
		exec(cmds);
	}

}
