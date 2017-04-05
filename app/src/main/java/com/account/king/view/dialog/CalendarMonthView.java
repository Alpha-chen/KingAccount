package com.account.king.view.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.account.king.R;
import com.account.king.db.storage.KingAccountStorage;
import com.account.king.util.CalendarUtil;
import com.account.king.util.DensityUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;

/**
 * CalendarMonthView
 * <p/>
 */
public class CalendarMonthView extends View {
    private final Region[][] MONTH_REGIONS = new Region[6][7];
    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG |
            Paint.LINEAR_TEXT_FLAG);
    private Scroller mScroller;
    private CalendarPicker.PageChangeListener pageChangeListener;
    private CalendarPicker.OnDatePickedListener onDatePickedListener;

    private int circleRadius;
    private int indexYear, indexMonth;
    private int centerYear, centerMonth;
    private int leftYear, leftMonth;
    private int rightYear, rightMonth;
    private int width, height;
    private int lastPointX;
    private int lastPointY;
    private int lastMoveX;
    private int criticalWidth;

    private float sizeTextGregorian, sizeTextFestival;
    private float offsetYFestival1, offsetYFestival;
    private boolean isNewEvent;

    private Calendar today = Calendar.getInstance(Locale.getDefault());
    //返回第二天 0时0分0秒
    private Calendar nextDay = CalendarUtil.getNextDayTime();
    //private XxtChineseCalendar xxtChineseCalendar = new XxtChineseCalendar();
    private Map<Integer, SparseArray> monthCellDescriptorMap = new HashMap<>();
    private Map<Integer, SparseArray<CalendarDayNode>> monthDataMap = new HashMap<>();

    private float strokeWidth;
    private int selectedCalendarDay;
    private int mTouchSlop;

    private Context context;
    private KingAccountStorage bookStorage;

    private int model = 0;// 0=选择时间模式   1=日历模式
    public static final int select_model = 0;
    public static final int calendar_model = 1;

    //new
    private Paint dashPaint;
    private int topPading;
    private int moneyTextSize = 9;
    private int moneyPading = 2;

    public CalendarMonthView(Context context, int model) {
        super(context);
        this.context = context;
        this.model = model;
        mScroller = new Scroller(context);
        mPaint.setTextAlign(Paint.Align.CENTER);
        selectedCalendarDay = CalendarUtil.getDate(today);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        circleRadius = DensityUtils.dp2px(context, 30);
        sizeTextGregorian = DensityUtils.sp2px(context, 13);
        bookStorage = new KingAccountStorage(context);
        topPading = DensityUtils.dp2px(context, 5);
        moneyTextSize = DensityUtils.sp2px(context, moneyTextSize);
        moneyPading = DensityUtils.dp2px(context, moneyPading);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            requestLayout();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                isNewEvent = true;
                lastPointX = (int) event.getX();
                lastPointY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = Math.abs(lastPointX - event.getX());
                if (Math.abs(lastPointY - event.getY()) > offsetX) {
                    break;
                }
                if (isNewEvent) {
                    if (offsetX > mTouchSlop) {
                        isNewEvent = false;
                    }
                }
                int totalMoveX = (int) (lastPointX - event.getX()) + lastMoveX;
                smoothScrollTo(totalMoveX, indexYear * height);
                break;
            case MotionEvent.ACTION_UP:
                float offsetX1 = Math.abs(lastPointX - event.getX());
                if (Math.abs(lastPointY - event.getY()) > offsetX1) {
                    //break;
                }
                if (offsetX1 > mTouchSlop) {
                    if (lastPointX > event.getX() &&
                            Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                        indexMonth++;
                        centerMonth = (centerMonth + 1) % 13;
                        if (centerMonth == 0) {
                            centerMonth = 1;
                            centerYear++;
                        }
                    } else if (lastPointX < event.getX() &&
                            Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                        indexMonth--;
                        centerMonth = (centerMonth - 1) % 12;
                        if (centerMonth == 0) {
                            centerMonth = 12;
                            centerYear--;
                        }
                    }
                    computeDate();
                    smoothScrollTo(width * indexMonth, indexYear * height);
                    lastMoveX = width * indexMonth;
                } else {
                    defineRegion((int) event.getX(), (int) event.getY());
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (model == calendar_model) {
            //宽高比 7/6
            setMeasuredDimension(measureWidth, measureWidth * 6 / 7);
        } else {
            setMeasuredDimension(measureWidth, (int) (measureWidth / 1.5));
        }
        //setMeasuredDimension(measureWidth, DensityUtils.dp2px(context,250));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;

        criticalWidth = (int) (1F / 5F * width);
        int cellW = (int) (w / 7F);
        int cellH = (int) (h / 6F);

        mPaint.setTextSize(sizeTextGregorian);

        strokeWidth = width / 200F;
        float heightGregorian = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
        sizeTextFestival = width / 40F;
        mPaint.setTextSize(sizeTextFestival);

        float heightFestival = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
        offsetYFestival1 = (((Math.abs(mPaint.ascent() + mPaint.descent())) / 1.7F) +
                heightFestival / 1.7F + heightGregorian / 1.7F) / 1.7F;
        offsetYFestival = offsetYFestival1 - (((Math.abs(mPaint.ascent() + mPaint.descent())) / 2F) +
                heightFestival / 2F + heightGregorian / 2F) / 2F;
        for (int i = 0; i < MONTH_REGIONS.length; i++) {
            for (int j = 0; j < MONTH_REGIONS[i].length; j++) {
                Region region = new Region();
                region.set((j * cellW), (i * cellH), cellW + (j * cellW),
                        cellH + (i * cellH));
                MONTH_REGIONS[i][j] = region;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        draw(canvas, width * (indexMonth - 1), height * indexYear, leftYear, leftMonth);//上一个月
        draw(canvas, width * indexMonth, indexYear * height, centerYear, centerMonth);//当前月
        draw(canvas, width * (indexMonth + 1), height * indexYear, rightYear, rightMonth);//下一个月
    }

    private void draw(Canvas canvas, int x, int y, int year, int month) {
        canvas.save();
        canvas.translate(x, y);
        List<List<MonthCellDescriptor>> cells = getMonthCells(year, month);
//        if(model == calendar_model){
//            initDashData();
//            drawDashLine(canvas);
//        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (model == calendar_model) {
                    drawCell(canvas, MONTH_REGIONS[i][j].getBounds(), cells.get(i).get(j));
                } else {
                    drawCell_select(canvas, MONTH_REGIONS[i][j].getBounds(), cells.get(i).get(j));
                }
            }
        }
        canvas.restore();
    }

    private void initDashData() {
        dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setColor(getResources().getColor(R.color.my_color));
        dashPaint.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(
                new float[]{DensityUtils.dp2px(context, 3), DensityUtils.dp2px(context, 2)}, 1);
        dashPaint.setPathEffect(effects);
    }

    private void drawDashLine(Canvas canvas) {
        int cellW = (int) (width / 7F);
        int cellH = (int) (height / 6F);
        Path path = new Path();
        for (int i = 0; i < 7; i++) {
            path.reset();
            path.moveTo(0, cellH * i);//起始坐标
            path.lineTo(width, cellH * i);//终点坐标
            canvas.drawPath(path, dashPaint);
        }
        for (int i = 0; i < 7; i++) {
            path.reset();
            path.moveTo(cellW * i, 0);//起始坐标
            path.lineTo(cellW * i, height);//终点坐标
            canvas.drawPath(path, dashPaint);
        }
    }

    /**
     * 日历模式
     *
     * @param canvas
     * @param rect
     * @param monthCellDescriptor
     */
    private void drawCell(Canvas canvas, Rect rect, MonthCellDescriptor monthCellDescriptor) {
        //选中
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        if (monthCellDescriptor.isSelected()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_press_bg);
            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, srcRect, rect, mPaint);
        }
        //是否有数据  画 点
        int dataType = monthCellDescriptor.getHasDataType();
        if (dataType != 0) {
            mPaint.setTextSize(moneyTextSize);
            if (dataType == 1) {
                mPaint.setColor(getResources().getColor(R.color.color5));
                Rect bounds = new Rect();
                String value = "-" + String.format("%.2f", monthCellDescriptor.getFout());
                mPaint.getTextBounds(value, 0, value.length(), bounds);
                canvas.drawText(value, rect.centerX(),
                        rect.bottom - moneyPading - bounds.height() / 2, mPaint);
            } else if (dataType == 2) {
                mPaint.setColor(getResources().getColor(R.color.color6));
                Rect bounds = new Rect();
                String value = "+" + String.format("%.2f", monthCellDescriptor.getFin());
                mPaint.getTextBounds(value, 0, value.length(), bounds);
                canvas.drawText(value, rect.centerX(),
                        rect.bottom - moneyPading - bounds.height() / 2, mPaint);
            } else {
                mPaint.setColor(getResources().getColor(R.color.color5));
                Rect bounds = new Rect();
                String value = "-" + String.format("%.2f", monthCellDescriptor.getFout());
                mPaint.getTextBounds(value, 0, value.length(), bounds);
                canvas.drawText(value, rect.centerX(),
                        rect.bottom - moneyPading - bounds.height() / 2, mPaint);

                mPaint.setColor(getResources().getColor(R.color.color6));
                Rect bounds2 = new Rect();
                String value2 = "+" + String.format("%.2f", monthCellDescriptor.getFin());
                mPaint.getTextBounds(value2, 0, value2.length(), bounds2);
                canvas.drawText(value2, rect.centerX(),
                        rect.bottom - moneyPading - bounds.height() - moneyPading * 3 - bounds2.height() / 2, mPaint);
            }
        }
        int color = getResources().getColor(R.color.color1);
        mPaint.setColor(color);
        //大于当天 或者非当月  替换色值
        if (!monthCellDescriptor.isCurrentMonth() || monthCellDescriptor.getDate().getTime() >= nextDay.getTime().getTime()) {
            mPaint.setColor(getResources().getColor(R.color.color3));
        }
        mPaint.setTextSize(sizeTextGregorian);
        //画今天
        if (monthCellDescriptor.isToday()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_king);
            canvas.drawBitmap(bitmap, rect.centerX() - bitmap.getWidth() / 2, rect.top + topPading, mPaint);
        } else {
            Rect bounds = new Rect();
            String value = monthCellDescriptor.getValue() + "";
            mPaint.getTextBounds(value, 0, value.length(), bounds);
            canvas.drawText(monthCellDescriptor.getValue() + "", rect.centerX(),
                    rect.top + topPading + bounds.height() / 2 + topPading, mPaint);
        }
    }


    private void drawCell_select(Canvas canvas, Rect rect, MonthCellDescriptor monthCellDescriptor) {
        if (monthCellDescriptor.isSelected()) {
            mPaint.setColor(getResources().getColor(R.color.my_color));
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F, mPaint);
        }

        int color = monthCellDescriptor.isCurrentMonth()
                ? getResources().getColor(R.color.color1) : getResources().getColor(R.color.color3);

        //超过当天颜色灰色
        if (monthCellDescriptor.getDate().getTime() >= nextDay.getTime().getTime()) {
            color = getResources().getColor(R.color.color3);
        }

        mPaint.setColor(color);
        if (monthCellDescriptor.isSelected()) {
            mPaint.setColor(getResources().getColor(R.color.white));
        }
        mPaint.setTextSize(sizeTextGregorian);
        canvas.drawText(monthCellDescriptor.getValue() + "", rect.centerX(), rect.centerY() + offsetYFestival, mPaint);
        //画今天
        if (monthCellDescriptor.isToday()) {
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(getResources().getColor(R.color.my_color));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F, mPaint);
        }
        mPaint.setStrokeWidth(0);
    }


    public void setPageChangeListener(CalendarPicker.PageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    public void setOnDatePickedListener(CalendarPicker.OnDatePickedListener onDatePickedListener) {
        this.onDatePickedListener = onDatePickedListener;
    }

    void clearCache() {
        monthDataMap.clear();
    }

    void setDate(int year, int month) {
        centerYear = year;
        centerMonth = month;
        indexYear = 0;
        indexMonth = 0;
        computeDate();
        requestLayout();
        invalidate();
        move();
    }

    public void move() {
        int dx = 0 - mScroller.getFinalX();
        smoothScrollBy(dx, 0);
        lastMoveX = width * indexMonth;
        requestLayout();
        invalidate();
    }

    private void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    /**
     * 回到今天
     */
    public void moveToToday() {
        selectedCalendarDay = CalendarUtil.getDate(today);
        if (null != onDatePickedListener) {
            onDatePickedListener.onDatePicked(CalendarUtil.getCalendar(selectedCalendarDay));
        }
        int dx = 0 - mScroller.getFinalX();
        smoothScrollBy(dx, 0);
        lastMoveX = width * indexMonth;
        requestLayout();
        invalidate();
    }

    void setDate(int ymd) {
        centerYear = CalendarUtil.getYear(ymd);
        centerMonth = CalendarUtil.getMonth(ymd);
        indexYear = 0;
        indexMonth = 0;
        computeDate();
        requestLayout();
        invalidate();
        moveToToday(ymd);
    }

    public void moveToToday(int ymd) {
        selectedCalendarDay = ymd;
        int dx = 0 - mScroller.getFinalX();
        smoothScrollBy(dx, 0);
        lastMoveX = width * indexMonth;
        requestLayout();
        invalidate();
    }

    /**
     * @param dx x轴移动的位移 正直表示向左移动
     * @param dy y轴移动的位移 垂直方向不移动dy=0
     */
    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
        invalidate();
    }

    private void defineRegion(int x, int y) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                Region region = MONTH_REGIONS[i][j];
                if (region.contains(x, y)) {
                    Calendar selectedCalendar = getMonthCellValue(centerYear, centerMonth, i, j);
                    //不能选择大于今天的日期
                    if (selectedCalendar.getTime().getTime() >= nextDay.getTime().getTime()) {
                        return;
                    }
                    selectedCalendarDay = CalendarUtil.getDate(selectedCalendar);
                    invalidate();
                    if (null != onDatePickedListener) {
                        onDatePickedListener.onDatePicked(selectedCalendar);
                    }
                }
            }
        }
    }

    private void computeDate() {
        rightYear = leftYear = centerYear;
        rightMonth = centerMonth + 1;
        leftMonth = centerMonth - 1;

        if (centerMonth == 12) {
            rightYear++;
            rightMonth = 1;
        }
        if (centerMonth == 1) {
            leftYear--;
            leftMonth = 12;
        }
        if (null != pageChangeListener) {
            Calendar cal = Calendar.getInstance();
            cal.set(centerYear, centerMonth, 0);
            pageChangeListener.onPageChangeListener(cal);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    List<List<MonthCellDescriptor>> getMonthCells(int year, int month) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(DAY_OF_MONTH, 1);
        //取得按月保存的数据
        int ym = CalendarUtil.getYearMonth(cal.getTime());
        SparseArray sparseArray = monthCellDescriptorMap.get(ym);
        List<List<MonthCellDescriptor>> cells = new ArrayList<>();
        cal.set(DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);
        int start = CalendarUtil.getDate(cal);
        cal.add(DATE, 41);
        int end = CalendarUtil.getDate(cal);
        SparseArray<CalendarDayNode> sparseIntArray = null;
        if (model == calendar_model) {
            sparseIntArray = monthDataMap.get(ym);
            if (null == sparseIntArray) {
            /*    sparseIntArray = bookStorage.selectByDateCount(CalendarUtil.date2TimeMilis(start * 1000000L),
                        CalendarUtil.date2TimeMilis(end * 1000000L));*/
                monthDataMap.put(ym, sparseIntArray);
            }
        }
        cal.add(DATE, -41);
        while (cells.size() < 6) {// 保证每个月都是现实42天
            List<MonthCellDescriptor> weekCells = new ArrayList<>();
            cells.add(weekCells);
            for (int c = 0; c < 7; c++) {
                Date date = cal.getTime();
                int date_value = CalendarUtil.getDate(date);
                if (null == sparseArray) {
                    sparseArray = new SparseArray();
                }
                MonthCellDescriptor monthCellDescriptor = (MonthCellDescriptor) sparseArray.get(date_value);
                if (null == monthCellDescriptor) {
                    monthCellDescriptor = new MonthCellDescriptor();
                    monthCellDescriptor.setDate(date);
                    monthCellDescriptor.setIsCurrentMonth(cal.get(MONTH) == month - 1);
                    monthCellDescriptor.setIsToday(date_value == CalendarUtil.getDate(today.getTime()));
                    int value = cal.get(DAY_OF_MONTH);
                    monthCellDescriptor.setValue(value);
                    //计算农历
                    //xxtChineseCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), value);
                    // monthCellDescriptor.setcCalendarValue(xxtChineseCalendar.getChinese(XxtChineseCalendar.CHINESE_TERM_OR_DATE));
                    //monthCellDescriptor.setIsRest(XxtChineseCalendar.isHoliday(date_value));
                }
                if (model == calendar_model && null != sparseIntArray) {
                    CalendarDayNode node = sparseIntArray.get(date_value);
                    if (node != null) {
                        monthCellDescriptor.setHasDataType(node.type);
                        monthCellDescriptor.setFin(node.fin);
                        monthCellDescriptor.setFout(node.fout);
                    }
                }
                monthCellDescriptor.setIsSelected(date_value == selectedCalendarDay);
                //保存每天的数据
                sparseArray.put(date_value, monthCellDescriptor);
                weekCells.add(monthCellDescriptor);
                cal.add(DATE, 1);
            }
        }
        //保存每个月的数据
        monthCellDescriptorMap.put(ym, sparseArray);
        return cells;
    }

    Calendar getMonthCellValue(int year, int month, int i, int j) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);
        cal.add(DATE, i * 7 + j);
        return cal;
    }
}
