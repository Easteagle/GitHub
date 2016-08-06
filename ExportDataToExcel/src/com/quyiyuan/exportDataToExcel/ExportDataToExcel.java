package com.quyiyuan.exportDataToExcel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExportDataToExcel {

	/**
	 * 
	 */
	public static final String FILE_PATH = "C:\\Users\\lenovo\\Desktop\\1.txt";

	/**
	 * 
	 */
	public static final String EXCEL_PATH = "C:\\Users\\lenovo\\Desktop\\";

	/**
	 * 
	 */
	public static final String TIME_LINE = "19:30";

	/**
	 * 
	 */
	public static final int AMOUNT = 15;

	/**
	 * 
	 */
	public static final String USERNAME = "冯乾宁";

	/**
	 * 
	 */
	public static final String DEPARTMENT = "APP研发部";

	/**
	 * 
	 */
	public static final String TEMPLATE_NAME = "formwork.xls";

	/**
	 * 
	 */
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 */
	public static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 */
	public static final SimpleDateFormat SDF_HHMMSS = new SimpleDateFormat("HH:mm:ss");

	public static void main(String[] args) throws Exception {
		ExportDataToExcel exportDataToExcel = new ExportDataToExcel();
		BufferedReader br = exportDataToExcel.getBufferedReaderByFile(FILE_PATH);
		if (br == null) {
			System.out.println("没有发现文件");
		} else {
			List<Map<Integer, DayEntity>> dataMapList = exportDataToExcel.readFile(br);
//			System.out.println(dataMapList);
			if (dataMapList != null && dataMapList.size() > 0) {
				for (Map<Integer, DayEntity> dataMap : dataMapList) {
					exportDataToExcel.dataExportToExcel(dataMap);
				}
			}
		}
	}

	private BufferedReader getBufferedReaderByFile(String filePath) throws FileNotFoundException {
		BufferedReader br = null;
		if (StringUtils.isBlank(filePath)) {
			return null;
		} else {
			File file = new File(filePath);
			if (file.exists()) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			}
		}
		return br;
	}

	@SuppressWarnings("deprecation")
	private List<Map<Integer, DayEntity>> readFile(BufferedReader br) throws IOException, ParseException {
		List<Map<Integer, DayEntity>> mapList = new ArrayList<Map<Integer, DayEntity>>();
		Map<Integer, DayEntity> map = null;
		int mounth = -1;
		String temp = "";
		while ((temp = br.readLine()) != null) {
			DayEntity dayEntity = getDayEntityByString(temp);
			if (dayEntity != null) {
				if (mounth != dayEntity.getStartDate().getMonth()) {
					if (map != null && !map.isEmpty()) {
						mapList.add(map);
					}
					map = new TreeMap<Integer, DayEntity>(new Comparator<Integer>() {
						@Override
						public int compare(Integer key1, Integer key2) {
							return key1.compareTo(key2);
						}
					});
					map.put(dayEntity.getNumber(), dayEntity);
					mounth = dayEntity.getStartDate().getMonth();
				} else {
					map.put(dayEntity.getNumber(), dayEntity);
				}
			}
		}
		if (map != null && !map.isEmpty()) {
			mapList.add(map);
		}
		if (br != null) {
			br.close();
		}
		return mapList;
	}

	private DayEntity getDayEntityByString(String data) throws ParseException {
		DayEntity dayEntity = null;
		if (data.indexOf("-") > 0 && data.indexOf(":") > 10) {
			data = analysisString(data).trim();
			ParsePosition position = new ParsePosition(0);
			Date parseDate = SDF_YYYYMMDD.parse(data, position);
			data = data.substring(position.getIndex()).trim();
			if (data.indexOf(":") > 0 && data.length() > 4) {
				position = new ParsePosition(0);
				Date parseStartTime = SDF_HHMMSS.parse(data, position);
				data = data.substring(position.getIndex()).trim();
				Date parseEndTime = null;
				if (data.indexOf(":") > 0 && data.length() > 4) {
					position = new ParsePosition(0);
					parseEndTime = SDF_HHMMSS.parse(data, position);
				}
				dayEntity = new DayEntity();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(parseDate);
				dayEntity.setNumber(calendar.get(Calendar.DAY_OF_MONTH));
				dayEntity.setWeekName(getWeekNameByDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
				String startDate = SDF_YYYYMMDD.format(parseDate) + " " + SDF_HHMMSS.format(parseStartTime);
				dayEntity.setStartDate(SDF.parse(startDate));
				if (parseEndTime != null) {
					String endDate = SDF_YYYYMMDD.format(parseDate) + " " + SDF_HHMMSS.format(parseEndTime);
					dayEntity.setEndDate(SDF.parse(endDate));
				}
			}
		}
		return dayEntity;
	}

	private String analysisString(String sourceString) {
		if (StringUtils.isBlank(sourceString)) {
			return sourceString;
		}
		StringBuilder resultString = new StringBuilder();
		for (char oneChar : sourceString.toCharArray()) {
			switch (oneChar) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '-':
			case ':':
			case ' ':
				resultString.append(oneChar);
				break;
			default:
				break;
			}
		}
		return resultString.toString();
	}

	private String getWeekNameByDayOfWeek(int dayOfWeek) {
		String weekName = "";
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			weekName = "一";
			break;
		case Calendar.TUESDAY:
			weekName = "二";
			break;
		case Calendar.WEDNESDAY:
			weekName = "三";
			break;
		case Calendar.THURSDAY:
			weekName = "四";
			break;
		case Calendar.FRIDAY:
			weekName = "五";
			break;
		case Calendar.SATURDAY:
			weekName = "六";
			break;
		case Calendar.SUNDAY:
			weekName = "日";
			break;
		default:
			break;
		}
		return weekName;
	}

	@SuppressWarnings("deprecation")
	private void dataExportToExcel(Map<Integer, DayEntity> dataMap)
			throws IOException, RowsExceededException, WriteException, BiffException {
		if (dataMap != null && !dataMap.isEmpty()) {
			Workbook in = getExcelTemplate();
			int month = 0;
			Iterator<Entry<Integer, DayEntity>> iter = dataMap.entrySet().iterator();
			if (iter.hasNext()) {
				Entry<Integer, DayEntity> entry = iter.next();
				DayEntity dayEntity = entry.getValue();
				month = dayEntity.getStartDate().getMonth() + 1;
			}
			WritableWorkbook wb = Workbook.createWorkbook(
					new File(EXCEL_PATH + USERNAME + month + "月份统计表" + getCurrentTimestamp() + ".xls"), in);
			WritableSheet sheet = wb.getSheet(0);
			int countDay = 0;
			for (int i = 1; i <= 16; i++) {
				DayEntity dayEntityLeft = dataMap.get(i);
				DayEntity dayEntityRight = dataMap.get(16 + i);
				if (dayEntityLeft != null) {
					String dateTime = getEndTimeLine(dayEntityLeft);
					if (!StringUtils.isBlank(dateTime) && dateTime.length() > 6) {
						WritableCell weekName = sheet.getWritableCell(1, 2 + i);
						WritableCell endTime = sheet.getWritableCell(2, 2 + i);
						WritableCell description = sheet.getWritableCell(3, 2 + i);
						sheet.addCell(new Label(1, i + 2, dayEntityLeft.getWeekName(), weekName.getCellFormat()));
						sheet.addCell(new Label(2, i + 2, dateTime, endTime.getCellFormat()));
						sheet.addCell(new Label(3, i + 2, "√" + description.getContents().substring(1),
								description.getCellFormat()));
						countDay++;
					}
				}
				if (dayEntityRight != null) {
					String dateTime = getEndTimeLine(dayEntityRight);
					if (!StringUtils.isBlank(dateTime) && dateTime.length() > 6) {
						WritableCell weekName = sheet.getWritableCell(6, 2 + i);
						WritableCell endTime = sheet.getWritableCell(7, 2 + i);
						WritableCell description = sheet.getWritableCell(8, 2 + i);
						sheet.addCell(new Label(6, i + 2, dayEntityRight.getWeekName(), weekName.getCellFormat()));
						sheet.addCell(new Label(7, i + 2, dateTime, endTime.getCellFormat()));
						sheet.addCell(new Label(8, i + 2, "√" + description.getContents().substring(1),
								description.getCellFormat()));
						countDay++;
					}
				}
				if (i == 16) {
					WritableCell title = sheet.getWritableCell(0, 1);
					sheet.addCell(new Label(0, 1,
							"姓名：" + USERNAME + "            部门：" + DEPARTMENT + "            记录月份：" + month + "月份",
							title.getCellFormat()));
					WritableCell totalAmount = sheet.getWritableCell(5, 18);
					sheet.addCell(new Label(5, 18, "补贴天数：" + countDay + " 天     金额：" + countDay * AMOUNT + " 元",
							totalAmount.getCellFormat()));
				}
			}
			wb.write();
			wb.close();
		}
	}

	private String getEndTimeLine(DayEntity dayEntity) {
		String timeLine = "";
		String startDate = SDF.format(dayEntity.getStartDate());
		String endDate = dayEntity.getEndDate() == null ? "" : SDF.format(dayEntity.getEndDate());
		String dateTime = "";
		if (!StringUtils.isBlank(endDate) && endDate.length() > 10) {
			timeLine = endDate.substring(0, 10) + " " + TIME_LINE;
			if (endDate.compareTo(timeLine) >= 0) {
				dateTime = SDF_HHMMSS.format(dayEntity.getEndDate());
			}
		} else if (!StringUtils.isBlank(startDate) && startDate.length() > 10) {
			timeLine = startDate.substring(0, 10) + " " + TIME_LINE;
			if (startDate.compareTo(timeLine) >= 0) {
				dateTime = SDF_HHMMSS.format(dayEntity.getStartDate());
			}
		}
		return dateTime;
	}

	private Workbook getExcelTemplate() throws IOException, BiffException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME);
		Workbook workbook = null;
		workbook = Workbook.getWorkbook(is);
		if (is != null) {
			is.close();
		}
		return workbook;
	}

	private String getCurrentTimestamp() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sf.format(new Date());
	}

}
