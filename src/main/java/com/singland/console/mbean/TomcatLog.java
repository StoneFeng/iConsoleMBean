package com.singland.console.mbean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.singland.console.mbean.utils.ShellCmdHelper;
import com.singland.console.vo.TomcatLogFileVo;

public class TomcatLog implements TomcatLogMBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatLog.class);
	
	private String getTomcatLogDir(String subTomcatName) {
		String dir = "";
		Properties properties = new Properties();
		try {
			properties.load(TomcatLog.class.getClassLoader().getResourceAsStream("config.properties"));
			dir = properties.getProperty("esb.console.mbean.tomcat.log.dir");
			// replace placeholder with tomcat1, tomcat2, tomcat3
			dir = dir.replace("placeholder", subTomcatName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dir;
	}
	
	private long fromByte2MB(long b) {
		return b/1000;
	}

	@Override
	public List<TomcatLogFileVo> listTomcatLogFiles(String subTomcatName) {
		List<TomcatLogFileVo> list = new ArrayList<TomcatLogFileVo>();
		File dir = new File(getTomcatLogDir(subTomcatName));
		if (dir.isDirectory()) {
			File [] files = dir.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					String absDir = f.getAbsolutePath();
					absDir = absDir.replaceAll("\\\\", "\\\\\\\\");
					TomcatLogFileVo vo = new TomcatLogFileVo();
					vo.setLogName(f.getName());
					vo.setAbsolutePath(absDir);
					vo.setLogSize(String.valueOf(fromByte2MB(f.length())));
					list.add(vo);
				}
			}
		}
		list.sort(new TomcatLogFileVo());
		return list;
	}

	@Override
	public String printSingleLogFile(String path) {
		String content = "";
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			content = new String(encoded, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public void clearLog(String path) {
		String cmd = String.format(">%s", path);
		String[] cmds = {"/bin/sh", "-c", cmd};
		ShellCmdHelper.exec(cmds);
	}

	@Override
	public void deleteLog(String path) {
		String cmd = String.format("rm -f %s", path);
		String[] cmds = {"/bin/sh", "-c", cmd};
		ShellCmdHelper.exec(cmds);
	}

	@Override
	public void deleteLogBatch(String path) {
		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
		String [] pathArr = path.split(",");
		List<String> pathList = Arrays.asList(pathArr);
		for (String pathStr : pathList) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					String cmd = String.format("rm -f %s", pathStr);
					String[] cmds = {"/bin/sh", "-c", cmd};
					ShellCmdHelper.exec(cmds);
				}
			});
		}
		exec.shutdown();
		while (true) {
			if (exec.isTerminated()) {
				break;
			} else {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		LOGGER.info(String.format("Logs %s was removed from server", path));
	}

}
