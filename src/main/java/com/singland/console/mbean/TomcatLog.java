package com.singland.console.mbean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.singland.console.mbean.utils.ShellCmdHelper;
import com.singland.console.vo.TomcatLogFileVo;

public class TomcatLog implements TomcatLogMBean {
	
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

}
