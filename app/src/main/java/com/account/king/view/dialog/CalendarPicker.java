package com.account.king.view.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.king.R;
import com.account.king.util.DensityUtils;

import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * CalendarPicker
 *
 * @author King
 */
public class CalendarPicker extends LinearLayout {

    private CalendarMonthView monthView;// 月视图
    String weekDayNames[] = {"日", "一", "二", "三", "四", "五", "六"};

    public CalendarPicker(Context context) {
        this(context, null);
    }

    private int model = 0;// 0=选择时间模式   1=日历模式
    private int week_color;
    private int week_bg;
    private float week_height;
    private int week_tv_testsize;

    public CalendarPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarPicker, 0, 0);
        if (null != a) {
            model = a.getInteger(R.styleable.CalendarPicker_show_modle, 0);
            week_color = a.getColor(R.styleable.CalendarPicker_week_color, getResources().getColor(R.color.color_bg));
            week_bg = a.getColor(R.styleable.CalendarPicker_week_bg, -1);
            week_height = a.getDimension(R.styleable.CalendarPicker_week_height, DensityUtils.dp2px(context, 20));
            week_tv_testsize = a.getInteger(R.styleable.CalendarPicker_week_tv_textsize, 14);
            a.recycle();
        }
        // 设置排列方向为竖向
        setOrientation(VERTICAL);
        LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) week_height);
        // 周视图根布局
        LinearLayout llWeek = new LinearLayout(context);
        if (week_bg != -1) {
            llWeek.setBackgroundColor(week_bg);
        }
        llWeek.setOrientation(HORIZONTAL);
        LayoutParams lpWeek = new LayoutParams(WRAP_CONTENT, (int) week_height);
        lpWeek.gravity = Gravity.CENTER_HORIZONTAL;
        lpWeek.weight = 1;
        // --------------------------------------------------------------------------------周视图
        for (int i = 0; i < 7; i++) {
            TextView tvWeek = new TextView(context);
            tvWeek.setText(weekDayNames[i]);
            tvWeek.setGravity(Gravity.CENTER);
            tvWeek.setTextSize(TypedValue.COMPLEX_UNIT_SP, week_tv_testsize);
            tvWeek.setTextColor(week_color);
            llWeek.addView(tvWeek, lpWeek);
        }
        addView(llWeek, llParams);
        // ------------------------------------------------------------------------------------月视图

        monthView = new CalendarMonthView(context, model);

        addView(monthView, llParams);
    }

    public void refreshDate(int year, int month) {
        monthView.clearCache();
        setDate(year, month);
    }

    /**
     * 设置初始化年月日期
     *
     * @param year  ...
     * @param month ...
     */
    public void setDate(int year, int month) {
        monthView.setDate(year, month);
    }

    public void setToday() {
        Calendar calendar = Calendar.getInstance();
        monthView.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        monthView.moveToToday();
    }

    public void setDate(int ymd) {
        monthView.setDate(ymd);
    }

    /**
     * 设置单选监听器
     */
    public void setOnDatePickedListener(OnDatePickedListener onDatePickedListener) {
        monthView.setOnDatePickedListener(onDatePickedListener);
    }

    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        monthView.setPageChangeListener(pageChangeListener);
    }

    /**
     * 日期单选监听器
     */
    public interface OnDatePickedListener {
        void onDatePicked(Calendar calendar);
    }

    /**
     * 年份月份改变监听器
     */
    public interface PageChangeListener {
        void onPageChangeListener(Calendar calendar);
    }
}
