package com.account.king.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.R;
import com.account.king.util.CalendarUtil;

import java.util.Calendar;


/**
 * @author  King
 */
public class CalendarDialog extends Dialog implements View.OnClickListener , CalendarPicker.OnDatePickedListener
                                            , CalendarPicker.PageChangeListener{

    private Context context ;

    private Animation pushInAnim;
    private Animation pushOutAnim;

    private CalendarPicker calendarPicker ;
    private RelativeLayout rootRela ;
    private TextView dateText ;
    private ImageView leftImg ,rightImg ;
    private Calendar calendar = Calendar.getInstance();
    private int date ;

    private OnSelectDateListener onSelectDateListener ;

    public CalendarDialog(Context context) {
        this(context, R.style.dialog_transparent);
    }

    public CalendarDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        initView();
    }

    /**
     *
     * @param date
     */
    public void setDate(long date) {
        this.date = CalendarUtil.getDateYMD(date);
        //calendarPicker.setDate(this.date);
    }

    public void setOnSelectDateListener(OnSelectDateListener onSelectDateListener){
        this.onSelectDateListener = onSelectDateListener;
    }

    private void initView(){

        dateText = (TextView) findViewById(R.id.date);
        leftImg = (ImageView) findViewById(R.id.date_left);
        leftImg.setOnClickListener(this);
        rightImg = (ImageView) findViewById(R.id.date_right);
        rightImg.setOnClickListener(this);
        findViewById(R.id.empty).setOnClickListener(this);

        rootRela = (RelativeLayout) findViewById(R.id.calendar_root);

        calendarPicker = (CalendarPicker) findViewById(R.id.calendar_cp);
        calendarPicker.setOnDatePickedListener(this);
        calendarPicker.setPageChangeListener(this);
        calendarPicker.setDate(this.date);

        pushInAnim = AnimationUtils.loadAnimation(context, R.anim.selector_anim_push_in);
        pushOutAnim = AnimationUtils.loadAnimation(context, R.anim.selector_anim_push_out);

        pushOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        dateText.setText(CalendarUtil.getStringYM(date));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_left:
                addMonth(-1);
                break;
            case R.id.date_right:
                addMonth(1);
                break;
            case R.id.empty:
                dismiss();
                break;
        }
    }

    private void addMonth(int month){
        Calendar calendar = CalendarUtil.getCalendar(date);
        calendar.add(Calendar.MONTH,month);
        date = CalendarUtil.getDate(calendar);
        calendarPicker.setDate(this.date);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeDialog();
        }
        return true;
    }

    public void closeDialog(){
        rootRela.startAnimation(pushOutAnim);
    }

    @Override
    public void show() {
        super.show();
        setDialogFull();
        rootRela.startAnimation(pushInAnim);
    }

    private void setDialogFull() {
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void onDatePicked(Calendar calendar) {
        int date = CalendarUtil.getDate(calendar);
        long ymd_hms = date * 1000000L + CalendarUtil.getNowTime();
        ymd_hms = CalendarUtil.date2TimeMilis(ymd_hms);
        if(onSelectDateListener != null){
            onSelectDateListener.okDate(ymd_hms);
        }
    }

    @Override
    public void onPageChangeListener(Calendar calendar) {
        date = CalendarUtil.getDate(calendar);
        dateText.setText(CalendarUtil.getStringYM(date));
    }

    public interface OnSelectDateListener{
        public void okDate(long date);
    }
}
