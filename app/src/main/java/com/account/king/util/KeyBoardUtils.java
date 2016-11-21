package com.account.king.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

public class KeyBoardUtils {
	/**
	 * 打开软键盘
	 * @param mContext
	 * @param view
	 */
	public static void openKeyboard(Context mContext, View view){
		if(null==view){
			return;
		}
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try{
			imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					InputMethodManager.HIDE_IMPLICIT_ONLY);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 打开软键盘
	 * @param mContext
	 * @param view
	 */
	public static void openKeyboardDelayed(final Context mContext, final View view){
		if(null==view){
			return;
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				openKeyboard(mContext, view);
			}
		}, 500);
	}



	/**
	 * 关闭软键盘
	 * @param mContext
	 * @param view
	 */
	public static void closeKeyboard(Context mContext, View view){
		if(null==view){
			return;
		}
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void autoOpenKeyboard(final Context mContext, final View view){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				KeyBoardUtils.openKeyboard(mContext, view);
			}
		}, 500);
	}

}
