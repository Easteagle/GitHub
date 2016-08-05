package com.quyiyuan.exportDataToExcel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang3.StringUtils;

public class ExportDataToExcel {

	public static final String FILE_PATH = "C:\\Users\\fengqianning\\Desktop\\1.txt";

	public static final String EXCEL_PATH = "C:\\Users\\fengqianning\\Desktop\\";

	public static final String TIME_LINE = "19:30";

	public static final String TEMPLATE_NAME = "formwork.xls";
	
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat SDF_HHMMSS = new SimpleDateFormat("HH:mm:ss");

	public static void main(String[] args) throws Exception {
		ExportDataToExcel exportDataToExcel = new ExportDataToExcel();
		BufferedReader br = exportDataToExcel
				.getBufferedReaderByFile(FILE_PATH);
		if (br == null) {
			System.out.println("没有发现文件");
		} else {
			Map<Integer, DayEntity> dataMap = exportDataToExcel.readFile(br);
			System.out.println(dataMap);
			exportDataToExcel.dataExportToExcel(dataMap);
		}
	}

	private BufferedReader getBufferedReaderByFile(String filePath) {
		BufferedReader br = null;
		try {
			if (StringUtils.isBlank(filePath)) {
				return null;
			} else {
				File file = new File(filePath);
				if (file.exists()) {
					br = new BufferedReader(new InputStreamReader(
							new FileInputStream(filePath)));
				}
			}
		} catch (FileNotFoundException e) {
		}
		return br;
	}

	private Map<Integer, DayEntity> readFile(BufferedReader br) {
		Map<Integer, DayEntity> map = new TreeMap<Integer, DayEntity>(
				new Comparator<Integer>() {
					@Override
					public int compare(Integer key1, Integer key2) {
						return key1.compareTo(key2);
					}
				});
		try {
			String temp = "";
			while ((temp = br.readLine()) != null) {
				DayEntity dayEntity = getDayEntityByString(temp);
				if (dayEntity != null) {
					map.put(dayEntity.getNumber(), dayEntity);
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
			}
		}
		return map;
	}

	private DayEntity getDayEntityByString(String data) throws Exception {
		DayEntity dayEntity = null;
		data = data.replace("无记录", "").trim();
		if (data.indexOf("-") > 0 && data.indexOf(":") > 10) {
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			ParsePosition position = new ParsePosition(0);
			Date parseDate = date.parse(data, position);
			data = data.substring(position.getIndex()).trim();
			if (data.indexOf(":") > 0 && data.length() > 4) {
				position = new ParsePosition(0);
				Date parseStartTime = time.parse(data, position);
				data = data.substring(position.getIndex()).trim();
				Date parseEndTime = null;
				if (data.indexOf(":") > 0 && data.length() > 4) {
					position = new ParsePosition(0);
					parseEndTime = time.parse(data, position);
				}
				dayEntity = new DayEntity();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(parseDate);
				dayEntity.setNumber(calendar.get(Calendar.DAY_OF_MONTH));
				dayEntity.setWeekName(getWeekNameByDayOfWeek(calendar
						.get(Calendar.DAY_OF_WEEK)));
				String startDate = date.format(parseDate) + " "
						+ time.format(parseStartTime);
				dayEntity.setStartDate(SDF.parse(startDate));
				if (parseEndTime != null) {
					String endDate = date.format(parseDate) + " "
							+ time.format(parseEndTime);
					dayEntity.setEndDate(SDF.parse(endDate));
				}
			}
		}
		return dayEntity;
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

	private void dataExportToExcel(Map<Integer, DayEntity> dataMap) {
		try {
			Workbook in = getExcelTemplate();
			WritableWorkbook wb = Workbook.createWorkbook(new File(EXCEL_PATH
					+ getCurrentTimestamp() + ".xls"), in);
			if (dataMap != null && !dataMap.isEmpty()) {
				WritableSheet sheet = wb.getSheet(0);
				for (int i = 1; i <= 16; i++) {
					DayEntity dayEntityLeft = dataMap.get(i);
					DayEntity dayEntityRight = dataMap.get(16 + i);
					if (dayEntityLeft != null) {
						String dateTime = getEndTimeLine(dayEntityLeft);
						if(!StringUtils.isBlank(dateTime)
								&& dateTime.length() > 6){
							WritableCell weekName = sheet.getWritableCell(1,2 + i);
							WritableCell endTime = sheet.getWritableCell(2,2 + i);
							WritableCell description = sheet.getWritableCell(3,2 + i);
							sheet.addCell(new Label(1, i + 2, dayEntityLeft
									.getWeekName(), weekName.getCellFormat()));
							sheet.addCell(new Label(2, i + 2, dateTime, endTime.getCellFormat()));
							sheet.addCell(new Label(3, i + 2, "√" + description.getContents().substring(1),description.getCellFormat()));
						}
					}
					if (dayEntityRight != null) {
						String dateTime = getEndTimeLine(dayEntityRight);
						if(!StringUtils.isBlank(dateTime)
								&& dateTime.length() > 6){
							WritableCell weekName = sheet.getWritableCell(6,2 + i);
							WritableCell endTime = sheet.getWritableCell(7,2 + i);
							WritableCell description = sheet.getWritableCell(8,2 + i);
							sheet.addCell(new Label(6, i + 2, dayEntityRight
									.getWeekName(), weekName.getCellFormat()));
							sheet.addCell(new Label(7, i + 2, dateTime, endTime.getCellFormat()));
							sheet.addCell(new Label(8, i + 2, "√" + description.getContents().substring(1),description.getCellFormat()));
						}
					}
				}
			}
			wb.write();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private String getEndTimeLine(DayEntity dayEntity) {
		String timeLine = "";
		String startDate = SDF.format(dayEntity.getStartDate());
		String endDate = dayEntity.getEndDate()==null?"":SDF.format(dayEntity.getEndDate());
		String dateTime = "";
		if (!StringUtils.isBlank(endDate) && endDate.length() > 10) {
			timeLine = endDate.substring(0, 10) +" "+ TIME_LINE;
			if (endDate.compareTo(timeLine) >= 0) {
				dateTime = SDF_HHMMSS.format(dayEntity.getEndDate());
			}
		} else if (!StringUtils.isBlank(startDate) && startDate.length() > 10) {
			timeLine = startDate.substring(0, 10) +" "+ TIME_LINE;
			if (startDate.compareTo(timeLine) >= 0) {
				dateTime = SDF_HHMMSS.format(dayEntity.getStartDate());
			}
		}
		return dateTime;
	}

	private Workbook getExcelTemplate() {
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(TEMPLATE_NAME);
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(is);
		} catch (Exception e) {
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}
		return workbook;
	}

	private String getCurrentTimestamp() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sf.format(new Date());
	}

}
