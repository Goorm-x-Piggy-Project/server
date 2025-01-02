package com.piggymetrics.statistics.domain.timeseries;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;

@Getter
public class DataPointId implements Serializable {
	private static final long serialVersionUID = 1L;

	private String account;
	private Date date;

	public DataPointId(String account, Date date) {
		this.account = account;
		this.date = date;
	}

	@Override
	public String toString() {
		return "DataPointId{" +
				"account='" + account + '\'' +
				", date=" + date +
				'}';
	}
}
