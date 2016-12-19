package com.account.king.util;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {

	public static void makeToast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	public static void makeToast(Context context, int content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
//	public static void makeToast(Context context, int content,int gravity) {
//		Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
//		toast.setGravity(gravity, 0, 0);
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.center_toast, null);
//		TextView textViewTop = (TextView) view.findViewById(R.id.menses_habit_toast_bottom);
//		textViewTop.setText(content);
//		toast.setView(view);
//		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.show();
//	}
//
//	/***
//	 * 创建带图片的Toast,居中
//	 *
//	 * @param context
//	 * @param resId
//	 * @param content1
//	 * @param content2
//	 */
//	public static void makeImgToast(Context context, int resId, String content1, String content2) {
//		if (context.getResources().getDrawable(resId) == null) {
//			ToastUtil.makeToast(context, content1 + "\r\n" + content2);
//		}
//		Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.menses_calendar_habit_toast, null);
//		TextView textViewTop = (TextView) view.findViewById(R.id.menses_habit_toast_top);
//		TextView textViewBottom = (TextView) view.findViewById(R.id.menses_habit_toast_bottom);
//		textViewTop.setText(content1);
//		textViewBottom.setText(content2);
//		toast.setView(view);
//		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.show();
//	}
//
//	/***
//	 * 创建带图片的Toast,居中
//	 *
//	 * @param context
//	 * @param resId
//	 * @param content
//	 */
//	public static void makeImgToast(Context context, int resId, String content) {
//		makeImgToast(context, resId, content, null);
//	}

}