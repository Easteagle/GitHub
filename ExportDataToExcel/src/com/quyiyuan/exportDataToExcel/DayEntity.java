package com.quyiyuan.exportDataToExcel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayEntity {

	private Integer number;

	private String weekName;

	private Date startDate;

	private Date endDate;

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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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
		return "{number:" + number + ",weekName:" + weekName + ",startDate:"
				+ startDateString + ",endDate:" + endDateString + "}";
	}
}
