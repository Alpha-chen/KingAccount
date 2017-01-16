// Copyright 2012 Square, Inc.

package com.account.king.view.dialog;

import java.util.Date;


public class MonthCellDescriptor {
    private Date date;
    private int value;
    private String cCalendarValue;//农历日期
    private boolean isCurrentMonth;
    private boolean isSelected;
    private boolean isToday;
    private boolean isRest;
    private boolean hasData;

    private int hasDataType ;// //0=无数据  1=有支出  2=有收入  3=都有
    private float fout;
    private float fin;
    

    public MonthCellDescriptor() {
    }

    public MonthCellDescriptor(Date date, int value, String cCalendarValue, boolean isCurrentMonth, boolean isSelected, boolean isToday, boolean isRest, boolean hasData) {
        this.date = date;
        this.value = value;
        this.cCalendarValue = cCalendarValue;
        this.isCurrentMonth = isCurrentMonth;
        this.isSelected = isSelected;
        this.isToday = isToday;
        this.isRest = isRest;
        this.hasData = hasData;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getcCalendarValue() {
        return cCalendarValue;
    }

    public void setcCalendarValue(String cCalendarValue) {
        this.cCalendarValue = cCalendarValue;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setIsCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }

    public boolean isRest() {
        return isRest;
    }

    public void setIsRest(boolean isRest) {
        this.isRest = isRest;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public int getHasDataType() {
        return hasDataType;
    }

    public void setHasDataType(int hasDataType) {
        this.hasDataType = hasDataType;
    }

    public float getFin() {
        return fin;
    }

    public void setFin(float fin) {
        this.fin = fin;
    }

    public float getFout() {
        return fout;
    }

    public void setFout(float fout) {
        this.fout = fout;
    }

    @Override
    public String toString() {
        return "MonthCellDescriptor{" +
                "date=" + date +
                ", value=" + value +
                ", cCalendarValue='" + cCalendarValue + '\'' +
                ", isCurrentMonth=" + isCurrentMonth +
                ", isSelected=" + isSelected +
                ", isToday=" + isToday +
                ", isRest=" + isRest +
                ", hasData=" + hasData +
                ", hasDataType=" + hasDataType +
                ", fout=" + fout +
                ", fin=" + fin +
                '}';
    }
}
