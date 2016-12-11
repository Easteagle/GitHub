package com.quyiyuan.exportDataToExcel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayEntity {

	private Integer dayNumber;

	private String weekName;

	private Date startDate;

	private Date endDate;

	private Integer monthNumber;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(Integer dayNumber) {
		this.dayNumber = dayNumber;
	}

	public Integer getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(Integer monthNumber) {
		this.monthNumber = monthNumber;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	@Override
	public String toString() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateString = null;
		String endDateString = null;
		if (startDate != null) {
			startDateString = sf.format(startDate);
		}
		if (endDate != null) {
			endDateString = sf.format(endDate);
		}
		return "{monthNumber:" + monthNumber + ",dayNumber:" + dayNumber + ",weekName:" + weekName + ",startDate:"
				+ startDateString + ",endDate:" + endDateString + "}";
	}
}
