package com.account.king.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtils {

	private ScreenUtils()
	{
		/** cannot be instantiated **/
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获得屏幕宽度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 得到屏幕的高度和宽度
	 * @param context
	 * @return
	 */
	public static int[] getScreenWidthHeight(Context context) {
		int[] arr = new int[2];
		arr[0] =getScreenWidth(context); // 屏幕宽度（像素）
		arr[1] = getScreenHeight(context); // 屏幕高度（像素）
		return arr;
	}

	/**
	 * 得到屏幕的高度和宽度
	 * @param context
	 * @return
	 */
	public static String getScreenWidthAndHeight(Context context) {
		StringBuilder sb = new StringBuilder();
		sb.append(getScreenWidth(context)).append("x").append(getScreenHeight(context));
		return sb.toString();
	}
	/**
	 * 获得状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context)
	{
		int statusHeight = -1;
		try
		{
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return statusHeight;
	}
}
