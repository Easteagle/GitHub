package com.quyiyuan.exportDataToExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * 描述：配置信息类
 * 作者：fengqianning 
 * 时间：2016年8月8日下午3:02:24
 * 类名: Config
 * </pre>
 */
public class Config {

	/**
	 * 导入文本文件的路径
	 */
	public static String FILE_PATH = "";

	/**
	 * 导出Excel文件的路径
	 */
	public static String EXCEL_PATH = "";

	/**
	 * 时间节点
	 */
	public static String TIME_LINE = "19:30";

	/**
	 * 补助金额
	 */
	public static int AMOUNT = 15;

	/**
	 * 用户姓名
	 */
	public static String USERNAME = "";

	/**
	 * 所属部门
	 */
	public static String DEPARTMENT = "APP研发部";

	/**
	 * 模版文件所在位置
	 */
	public static final String TEMPLATE_NAME = "formwork.xls";

	/**
	 * 配置文件名称
	 */
	private static final String PROPERTIES_FILE_NAME = "config.properties";

	/**
	 * 配置文件所在路径
	 */
	private static String PROPERTIES_FILE_PATH = "";

	/**
	 * 配置文件实例
	 */
	private static final Properties properties = new Properties();

	static {
		loadConfig();
	}

	/**
	 * <pre>
	 * 描述：加载配置文件内容
	 * 作者：fengqianning 
	 * 时间：2016年8月8日下午3:10:33
	 * returnType：void
	 * </pre>
	 * 
	 * @throws IOException
	 */
	private static void loadConfig() {
		String jarPath = System.getProperty("user.dir");
		InputStream inputStream =null;
		try {
			if (!StringUtils.isBlank(jarPath)) {
				PROPERTIES_FILE_PATH = jarPath + File.separator;
				EXCEL_PATH = PROPERTIES_FILE_PATH;
			}
			inputStream = new FileInputStream(PROPERTIES_FILE_PATH + PROPERTIES_FILE_NAME);
			if (inputStream != null) {
				// 读取到配置文件时，直接使用配置文件赋值
				properties.load(inputStream);
				String username = properties.getProperty("USERNAME");
				if (!StringUtils.isBlank(username)) {
					USERNAME = username;
				}
				String department = properties.getProperty("DEPARTMENT");
				if (!StringUtils.isBlank(department)) {
					DEPARTMENT = department;
				}
				String timeLine = properties.getProperty("TIME_LINE");
				if (!StringUtils.isBlank(timeLine)) {
					TIME_LINE = timeLine;
				}
				String amount = properties.getProperty("AMOUNT");
				if (!StringUtils.isBlank(amount)) {
					AMOUNT = Integer.parseInt(amount);
				}
				String filePath = properties.getProperty("FILE_PATH");
				if (!StringUtils.isBlank(filePath)) {
					FILE_PATH = filePath;
				}
				String excelPath = properties.getProperty("EXCEL_PATH");
				if (!StringUtils.isBlank(excelPath)) {
					if (!excelPath.endsWith("\\")
							&& !excelPath.endsWith(File.separator)) {
						excelPath = excelPath + File.separator;
					}
					EXCEL_PATH = excelPath;
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public static void saveConfig(String usernameNew, String departmentNew,
			String amountNew, String timeLineNew, String txtAddressNew,
			String excelAddressNew) {
		if (!StringUtils.isBlank(usernameNew)) {
			USERNAME = usernameNew;
			properties.put("USERNAME", USERNAME);
		}
		if (!StringUtils.isBlank(departmentNew)) {
			DEPARTMENT = departmentNew;
			properties.put("DEPARTMENT", DEPARTMENT);
		}
		if (!StringUtils.isBlank(amountNew)) {
			try {
				AMOUNT = Integer.parseInt(amountNew);
				properties.put("AMOUNT", amountNew);
			} catch (NumberFormatException e) {
			}
		}
		if (!StringUtils.isBlank(timeLineNew)) {
			try {
				new SimpleDateFormat("HH:mm").parse(timeLineNew);
				TIME_LINE = timeLineNew;
				properties.put("TIME_LINE", TIME_LINE);
			} catch (ParseException e) {
			}
		}
		if (!StringUtils.isBlank(txtAddressNew)) {
			FILE_PATH = txtAddressNew;
			properties.put("FILE_PATH", FILE_PATH);
		}
		if (!StringUtils.isBlank(excelAddressNew)) {
			if (!excelAddressNew.endsWith("\\")
					&& !excelAddressNew.endsWith(File.separator)) {
				excelAddressNew = excelAddressNew + File.separator;
			}
			EXCEL_PATH = excelAddressNew;
			properties.put("EXCEL_PATH", EXCEL_PATH);
		}
		try {
			FileOutputStream os = new FileOutputStream(new File(PROPERTIES_FILE_PATH + PROPERTIES_FILE_NAME));
			properties.store(os, "save config file");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

}
