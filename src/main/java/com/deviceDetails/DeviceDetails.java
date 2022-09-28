package com.deviceDetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import com.utility.LoggingUtils;

public class DeviceDetails {

	public static String outputText;
	public static String outputText1;
	public static String DeviceModel;
	static String runs = "";
	static String cmd = "";
	static String cmd2 = "";
	static String AppDetails = "";
	static ArrayList<String> devices = new ArrayList<String>();
	static ArrayList<String> deviceManufacturer = new ArrayList<String>();
	static HashSet<String> hs = new HashSet<String>();
	static ArrayList<String> deviceOS = new ArrayList<String>();
	static HashSet<String> hsOS = new HashSet<String>();
	public static String OEM;

	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger("rootLogger");
	static LoggingUtils logger = new LoggingUtils();

	public static String getAppDetails(String str) {
		try {
			getListOfDevicesConnected();
			String cmd = "";
			if (AppDetails.isEmpty()) {
				cmd = "adb -s " + devices.get(0) + " shell \"dumpsys package " + str + " | grep versionName\"";
				AppDetails = str;
			} else if (!AppDetails.isEmpty()) {
				cmd = "adb -s " + devices.get(1) + " shell \"dumpsys package " + str + " | grep versionName\"";
			}
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((DeviceModel = br.readLine()) != null) {
				logger.info("App Details :: " + DeviceModel.trim());
				break;
			}
		} catch (Exception e) {
		}

		return DeviceModel;
	}
	
	public static String getAppVersion(String packageName) {

		try {
			cmd = "adb shell \"dumpsys package " + packageName + " | grep versionName\"";
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((DeviceModel = br.readLine()) != null) {
				logger.info("Build Version : " + DeviceModel.trim());
				return DeviceModel.trim();
			}
		} catch (Exception e) {
			
		}
		return "";
	}

	public static void getTheDeviceManufacturer() {
		devices.removeAll(devices);
		deviceManufacturer.removeAll(deviceManufacturer);
		getListOfDevicesConnected();
		try {
			for (int i = 0; i <= getListOfDevicesConnected().size() - 1; i++) {
				String cmd3 = "adb -s " + devices.get(i) + " shell getprop ro.product.manufacturer";
				Process process = Runtime.getRuntime().exec(cmd3);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				deviceManufacturer.add(br.readLine());
				OEM = deviceManufacturer.get(0);
			}
			hs.addAll(deviceManufacturer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getTheOSVersion() {
		try {
			String cmd1 = "adb shell getprop ro.build.version.release";
			Process p1 = Runtime.getRuntime().exec(cmd1);
			BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			while ((outputText1 = br.readLine()) != null) {
				logger.info("OS Version :: " + outputText1.toString());
				Thread.sleep(3000);
				break;
			}

		} catch (Exception e) {
		}
		return outputText1;
	}

	public static void removePermisson(String packagename) {
		String cmd2 = "adb shell pm clear " + packagename;
		try {
			Runtime.getRuntime().exec(cmd2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getListOfDevicesConnected() {
		try {
			String cmd2 = "adb devices";
			Process p1 = Runtime.getRuntime().exec(cmd2);
			BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			String s = "";
			Thread.sleep(1000);
			devices.removeAll(devices);
			while (!(s = br.readLine()).isEmpty()) {
				if (!s.equals("List of devices attached")) {
					devices.add(s.replaceAll("device", "").trim());
				}
			}

			return devices;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return devices;
	}

	public static String deviceNames() {
		ArrayList<String> a = new ArrayList<String>();
		a.addAll(hs);
		return a.get(0);
	}

	public static String deviceOS() {
		ArrayList<String> a = new ArrayList<String>();
		a.addAll(hsOS);
			return a.get(0);
	}

	public static void getTheDeviceOSVersion() {

		devices.removeAll(devices);
		deviceOS.removeAll(deviceOS);
		getListOfDevicesConnected();

		try {
			for (int i = 0; i <= getListOfDevicesConnected().size() - 1; i++) {
				String cmd3 = "adb -s " + devices.get(i) + " shell getprop ro.build.version.release";
				Process process = Runtime.getRuntime().exec(cmd3);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				deviceOS.add(br.readLine());
			}
			hsOS.addAll(deviceOS);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String DeviceInfo() {

		String DeviceInfo;
		String getDeviceName = deviceNames();
		String getOSName = deviceOS();
		DeviceInfo = "Device Name - " + getDeviceName + " Version - " + getOSName;
		logger.info("Device Name : "+getDeviceName+"\n"+"OS Version : "+getOSName);
		return DeviceInfo;
	}

	
	public static String getListOfDevicesConnected1() {
		String deviceID = null;
		try {
			String cmd2 = "adb devices";
			Process p1 = Runtime.getRuntime().exec(cmd2);
			BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			String s = "";
			Thread.sleep(1000);
			while (!(s = br.readLine()).isEmpty()) {
				if (!s.equals("List of devices attached")) {
					if(!s.contains(".")) {
						deviceID = s.replaceAll("device", "").trim();
						System.out.println(deviceID);
					}
				}
			}
			return deviceID;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return deviceID;
	}
	
	public static void main(String[] args) {
//		getListOfDevicesConnected1();
//		appSize();
		Test();
	}
	
	
	public static void deviceDetails() {
		String cmd3 = "adb shell getprop ro.product.manufacturer";
		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd3);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String DeviceName = br.readLine();
			logger.info("Device Name : "+DeviceName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getTheOSVersion();
	}
	
	@SuppressWarnings("unused")
	public static void appSize() {
		String cmd = "adb shell pm path com.graymatrix.did";
		Process process;
		String pathAPK = null;
		String SizeAfterInstalling = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			pathAPK = br.readLine().replaceAll("package:", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(pathAPK);
		String cmd1 = "adb shell stat "+pathAPK+" |grep Size";
		Process p1;
		try {
			p1 = Runtime.getRuntime().exec(cmd1);
			BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			SizeAfterInstalling = br.readLine().split("	")[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void Test() {
		File f = new File("news.easynews.com.newsrc");
		System.out.println(f.length());
		System.out.println((f.length()) + " Mb");
		long MEGABYTE = 1024L * 1024L;
		System.out.println(f.length() / MEGABYTE);
	}
}
