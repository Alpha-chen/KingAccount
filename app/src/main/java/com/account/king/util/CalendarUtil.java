package com.account.king.util;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class CalendarUtil {
    private static String TAG = "CalendarUtil";

    public static int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     *
     * @return
     */
    public static int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前月份
     *
     * @return
     */
    public static int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 数据库存储格式  时间戳 s/单位
     *
     * @return
     */
    public static long getNowTimeMillis() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 当前日期与预算结算日比较  >= 不变  < -1
     *
     * @return
     */
    public static int getBudgetMonth(Context context) {
        int month = getMonth();
//        int budget= SPUtils.getInt(context,SPUtils.BUDGET_DAY);
//        if(budget == 0){
//            budget = 1;
//        }
        int budget = 1;
        int currentDay = CalendarUtil.getDay();
        if (currentDay < budget) {
            month--;
        }
        //1月 -- = 12月
        if (month <= 0) {
            month = 12;
        }
        return month;
    }


    /**
     * 获取一个指定月份的时间戳
     *
     * @param month 201606
     * @return 20160601的时间戳
     */
    public static long getBudgetDate(int month) {
        if ((month + "").length() > 6) {
            return -1;
        }
        month = month * 100 + 1;
        return getCalendar(month).getTimeInMillis() / 1000;
    }

    /**
     * 时间戳转date
     *
     * @param timeMilis 秒单位
     * @return 20160610
     */
    public static int timeMilis2Date(long timeMilis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilis * 1000);
        return getDate(calendar);
    }

    /**
     * 时间戳转time
     *
     * @param timeMilis 秒单位
     * @return HH:MM
     */
    public static String timeMilis2Time(long timeMilis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilis * 1000);
        return getTime(calendar);
    }

    /**
     * @param date 20160601000000
     * @return 时间戳/s
     */
    public static long date2TimeMilis(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date2 = format.parse(date + "");
            return date2.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date().getTime() / 1000;
    }

    /**
     * 获取ymd的下个月时间的前一天
     *
     * @param ymd 20160705  ->20160804
     * @return
     */
    public static int getNextMonth(int ymd) {
        Date date = getDate(ymd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getDate(calendar);
    }

    /**
     * 获取距离ymd时间差为difDay的时间点
     *
     * @param ymd    20160505
     * @param difDay -1
     * @return 20160504
     */
    public static int getDiffDay(int ymd, int difDay) {
        Date date = getDate(ymd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, difDay);
        return getDate(calendar);
    }

    /**
     * 获取距离当前时间差为difDay的时间点
     *
     * @param difDay -1
     * @return 20160504
     */
    public static int getDiffDay(int difDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, difDay);
        return getDate(calendar);
    }

    /**
     * 获取距离ymd月份时间差为difDay的时间点
     *
     * @param ymd      20160505
     * @param difMonth -1
     * @return 20160405
     */
    public static int getDiffMonth(int ymd, int difMonth) {
        Date date = getDate(ymd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, difMonth);
        return getDate(calendar);
    }

    /**
     * 获取距离ymd月份时间差为difDay的时间点的月份的最后一天
     *
     * @param ymd      20160505
     * @param difMonth -1
     * @return 20160405
     */
    public static int getDiffMonthLastDay(int ymd, int difMonth) {
        Date date = getDate(ymd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, difMonth);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getDate(calendar);
    }

    /**
     * 获取距离ymd月份时间差为difDay的时间点
     *
     * @param time     时间戳
     * @param difMonth
     * @return 时间戳
     */
    public static long getDiffMonth(long time, int difMonth) {
        Date date = getDate(timeMilis2Date(time));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, difMonth);
        return date2TimeMilis(getDate(calendar));
    }


    /**
     * 获取当月第一天和最后一天
     *
     * @return
     */
    public static long[] getCurrentMonth() {
        long[] dates = new long[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dates[0] = getDate(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        dates[1] = getDate(calendar);
        return dates;
    }

    /**
     * 获取当年第一天和最后一天
     *
     * @return
     */
    public static long[] getCurrentYear() {
        long[] dates = new long[2];
        Calendar calendar = Calendar.getInstance();
        dates[1] = getDate(calendar);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        dates[0] = getDate(calendar);
        return dates;
    }

    /**
     * 获取当周第一天和最后一天 //周日初始
     *
     * @return
     */
    public static long[] getCurrentWeek() {
        long[] dates = new long[2];
        Calendar calendar = Calendar.getInstance();
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.add(Calendar.DAY_OF_YEAR, 1);//+1天
        dates[0] = getDate(calendar);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        dates[1] = getDate(calendar);
        return dates;
    }

    /**
     * 时间戳的day
     *
     * @param timeMilis
     * @return
     */
    public static String getTimeMilisDay(long timeMilis) {
        int date = timeMilis2Date(timeMilis);
        int day = getDay(date);
        return day < 10 ? "0" + day : "" + day;
    }

    /**
     * 时间戳的201605
     *
     * @param timeMilis
     * @return
     */
    public static int getYearMonth(long timeMilis) {
        int date = timeMilis2Date(timeMilis);
        int year = getYear(date);
        int month = getMonth(date);
        return year * 100 + month;
    }


    /**
     * 得到当前时间 HHmmss 数据库存储格式
     *
     * @return
     */
    public static int getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        return Integer.parseInt(format.format(new Date()));
    }

    /**
     * 根据整形的时间得到年
     *
     * @param date 20160610
     * @return
     */
    public static int getYear(long date) {
        LogUtil.d(TAG, "getYear");
        int year = -1;
        String dataStr = date + "";
        try {
            String monthStr = dataStr.substring(0, 4);
            year = Integer.parseInt(monthStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return year;
    }

    /**
     * 格式为20120401
     *
     * @param date
     * @return -1解析失败
     */
    public static int getMonth(long date) {
        LogUtil.d(TAG, "getMonth");
        int month = -1;
        String dataStr = date + "";

        try {
            String monthStr = dataStr.substring(4, 6);
            month = Integer.parseInt(monthStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return month;
    }


    public static String TimeStamp2Date(long timestampString) {
        String formats = "MM月dd日";
//        Long timestamp = timestampString * 1000;
        LogUtil.d(TAG, "TimeStamp2Date=" + timestampString);
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestampString));
        LogUtil.d(TAG, "date->=" + date);
        return date;
    }

    /**
     * 格式为20120401
     *
     * @param date
     * @return -1解析失败
     */
    public static int getDay(long date) {
        int month = -1;
        String dataStr = date + "";
        try {
            String monthStr = dataStr.substring(6, 8);
            month = Integer.parseInt(monthStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return month;
    }

    public static int getDateYMD(long date) {
        int date1 = -1;
        String dataStr = date + "";
        try {
            String monthStr = dataStr.substring(0, 8);
            date1 = Integer.parseInt(monthStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date1;
    }

    public static String getStringYMD(long date) {
        int year = getYear(date);
        int month = getMonth(date);
        int day = getDay(date);
        return year + "年" + month + "月" + day + "日";
    }

    public static String getStringMD(long date) {
        int month = getMonth(date);
        int day = getDay(date);
        return month + "月" + day + "日";
    }

    public static String getStringYM(long date) {
        int year = getYear(date);
        int month = getMonth(date);
        return year + "年" + (month < 10 ? "0" + month : month) + "月";
    }

    public static String getUnix2Date(long date) {

        int month = getMonth(date);
        int day = getDay(date);
        return month + "月" + day + "日";
    }


    /**
     * yyyyMMdd
     *
     * @return
     */
    public static int getCurrentDate() {
        return getDate(Calendar.getInstance());
    }

    /**
     * yyyyMMdd
     *
     * @return
     */
    public static int getCurrentYM() {
        return getYearMonth(Calendar.getInstance().getTime());
    }

    /**
     * yyyyMMdd
     *
     * @return
     */
    public static int getCurrentYM(int ymd) {
        return getYearMonth(getDate(ymd));
    }


    /**
     * yyyyMMdd
     *
     * @param calendar
     * @return
     */
    public static int getDate(Calendar calendar) {
        if (null == calendar) {
            return 0;
        }
        return getDate(calendar.getTime());
    }

    /**
     * yyyyMMdd
     *
     * @param calendar
     * @return
     */
    public static String getTime(Calendar calendar) {
        if (null == calendar) {
            return "";
        }
        return getTime(calendar.getTime());
    }

    public static int getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(sdf.format(date));
    }

    public static String getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
        return sdf.format(date);
    }

    /**
     * 得到年份和月份
     *
     * @return
     */
    public static int getYearMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return Integer.parseInt(sdf.format(date));
    }

    /**
     * 为小于10的数字前面补零后返回字符串
     */
    public static String PadZero(int num) {
        if (num < 10) {
            return "0" + String.valueOf(num);
        } else {
            return String.valueOf(num);
        }
    }


    /**
     * 获取当月第一天
     *
     * @return
     */
    public static Calendar getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    /**
     * 记账设置 返回明天的时间 hour 00:00:00
     *
     * @return
     */
    public static Calendar getNextDayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        return calendar;
    }

    //2016-06-18 距离当前时间天数
    public static int getDiffDay(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(time);
            Date date1 = Calendar.getInstance().getTime();
            long difM = date1.getTime() - date.getTime();
            long days = difM / (1000 * 60 * 60 * 24);
            return new Long(days + 1).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * @param date 20160601
     * @return
     */
    public static Calendar getCalendar(int date) {
        Date d = getDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    public static Date getDate(int date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date1 = null;
        try {
            date1 = format.parse(date + "");
        } catch (ParseException e) {
            e.printStackTrace();
            date1 = new Date();
        }
        return date1;
    }

}
