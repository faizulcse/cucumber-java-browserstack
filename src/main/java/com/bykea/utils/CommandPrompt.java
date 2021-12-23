package com.bykea.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandPrompt {
	Process p;
	ProcessBuilder builder;

	public String runCommand(String command) {
		String os = System.getProperty("os.name");
		StringBuilder allLine = new StringBuilder();
		try {
			if (!os.contains("Windows")) {
				p = Runtime.getRuntime().exec(command);
			} else {
				builder = new ProcessBuilder("cmd.exe", "/c", command);
				builder.redirectErrorStream(true);
				Thread.sleep(1000);
				p = builder.start();
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				allLine.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allLine.toString();
	}

	public static void main(String[] args) throws Exception {
//		String error = " port 4723";
//		String process = new CommandPrompt().runCommand("netstat -aof | findstr :" + error.split(" port ")[1]).split("\n")[0];
//		String killCommand = System.getProperty("os.name").contains("Windows") ? "taskkill /f /im " : "sudo kill -15 ";
//		System.out.println(new CommandPrompt().runCommand(killCommand + process.split("LISTENING")[1].trim()));
	}
}
