package com.github.lastsunday.moon.data;

import com.github.lastsunday.service.core.util.DateUtils;

import java.util.Date;
import java.util.List;

public class DateRange {

    public static final int INDEX_DATE_START = 0;
    public static final int INDEX_DATE_END = 1;

    private Date startDate;
    private Date endDate;

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange(List<Date> dateList) {
        if (dateList != null && dateList.size() > 0) {
            if (dateList.size() > 1) {
                startDate = DateUtils.convertToYYYYMMDD(dateList.get(INDEX_DATE_START));
                endDate = DateUtils.convertToYYYYMMDD(dateList.get(INDEX_DATE_END));
            } else {
                startDate = DateUtils.convertToYYYYMMDD(dateList.get(INDEX_DATE_START));
                endDate = DateUtils.convertToYYYYMMDD(dateList.get(INDEX_DATE_START));
            }
            if (startDate.getTime() == endDate.getTime()) {
                endDate = DateUtils.tomorrow(startDate);
            }
        }
    }

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

    public boolean isValid() {
        return startDate != null && endDate != null;
    }
}
