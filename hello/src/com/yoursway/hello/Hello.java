package com.yoursway.hello;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hello {

	private static String XMLHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	private static int counter = 0;
	private static BufferedReader reader;
	private static BufferedWriter writer;
	private static Socket socket;

	private static int lineNo;

	private static String currentCmd;

	private static String lastRunCommand;
	private static int lastRunTransactionId;

	private static void hello(String className, String methodName) throws IOException {
		System.out.println(String.format("Invoking %s in %s", methodName, className));
		sendStatus(lastRunCommand, lastRunTransactionId, "break");
		processPacketsUntilRunIsFound();
		try {
			Class<?> klass = Class.forName(className);
			Method method = klass.getMethod(methodName, new Class<?>[0]);
			method.setAccessible(true);
			method.invoke(null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Args: ");
		for (String s : args)
			System.out.println(s);
		if (args.length < 2) {
			usage();
			return;
		}
		String file = args[0];
		String runMode = args[1];
		boolean isDebug = runMode.equals("debug");
		int port;
		String sessionId;
		if (isDebug) {
			if (args.length < 4) {
				usage();
				return;
			}
			port = Integer.parseInt(args[2]);
			sessionId = args[3];

			connectToDbgpServer(port, sessionId, file);
		}
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			try {
				lineNo = 0;
				while (null != (currentCmd = reader.readLine())) {
					if (currentCmd.startsWith("hello ")) {
						String name = currentCmd.substring(6);
						int pos = name.lastIndexOf('.');
						String className = name.substring(0, pos);
						String methodName = name.substring(pos + 1);
						hello(className, methodName);
					} else if (currentCmd.startsWith("bye")) {
						System.out.println("Bye " + currentCmd.substring(3).trim());
					} else {
						System.err.println("Unknown command: " + currentCmd);
					}
					lineNo++;
				}
			} finally {
				fileReader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void sendPacket(String packet) throws IOException {
		writer.write(packet.length() + "");
		writer.write(0);
		writer.write(packet);
		writer.write(0);
		writer.flush();
	}

	private static String readPacket() throws IOException {
		String s = "";
		while (true) {
			int c = reader.read();
			if (c == 0 || c == -1)
				return s;
			s += (char) c;
		}
	}

	private static void connectToDbgpServer(int port, String sessionId,
			String file) {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress("localhost", port));
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			writer = new BufferedWriter(new OutputStreamWriter(outputStream));

			String initPacket = XMLHEADER
					+ "<init appid=\"hello\" idekey=\""
					+ sessionId
					+ "\" session=\""
					+ sessionId
					+ "\" thread=\"main\" parent=\"\" language=\"hello\" protocol_version=\"1.0\" fileuri=\""
					+ "file://" + file + "\"></init>";

			sendPacket(initPacket);

			processPacketsUntilRunIsFound();
			System.out.println("Connected");
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void processPacketsUntilRunIsFound() throws IOException {
		while (true) {
			String cmd = readPacket();
			System.out.println("GOT CMD: " + cmd);
			handleCommand(cmd);
			if (cmd == null)
				break;
			if (cmd.startsWith("run")) {
				break;
			}
		}
	}

	private static void handleCommand(String cmd) throws IOException {
		List<String> args = new ArrayList<String>();
		Map<String, String> values = new HashMap<String, String>();
		String[] components = cmd.split("\\s+");
		for (int i = 0; i < components.length; i++) {
			String component = components[i];
			if (component.charAt(0) == '-') {
				if (i + 1 < components.length
						&& components[i + 1].charAt(0) != '-') {
					values.put(component, components[i + 1]);
					i += 1;
				}
			} else {
				args.add(component);
			}
		}
		String command = args.remove(0);
		int transactionId = Integer.parseInt(values.get("-i"));

		if (command.equalsIgnoreCase("feature_set")) {
			sendPacket(String.format(
					"<response command=\"%s\" transaction_id=\"%d\"/>",
					command, transactionId));
		} else if (command.equalsIgnoreCase("feature_get")) {
			sendPacket(String
					.format(
							"<response command=\"%s\" transaction_id=\"%d\" feature_name=\"%s\" supported=\"0\" />",
							command, transactionId, values.get("-n")));
		} else if (command.equalsIgnoreCase("run")) {
			lastRunCommand = command;
			lastRunTransactionId = transactionId;
		} else {
			System.out.println("Ignoring: " + command);
			sendPacket(String
					.format(
							"<response command=\"%s\" transaction_id=\"%d\" success=\"1\" />",
							command, transactionId));
		}
	}

	private static void sendStatus(String command, int transactionId,
			String status) throws IOException {
		sendPacket(String
				.format(
						"<response command=\"%s\" transaction_id=\"%d\" status=\"%s\" reason=\"ok\" />",
						command, transactionId, status));
	}

	private static void usage() {
		System.out
				.println("Usage:\n\n\tjava <your java options go here> Hello <file to launch> <run|debug> [<port> <sessionId>]");
	}

}
