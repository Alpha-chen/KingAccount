package com.account.king.presenter.contract;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.account.king.R;
import com.account.king.adapter.UpgradeVersionGuideAdapter;
import com.account.king.util.AppUtils;
import com.account.king.util.DensityUtils;
import com.account.king.util.SPUtils;


/**
 * @author king
 */
public class WelcomePresenter implements WelcomeContract.IPresenter, ViewPager.OnPageChangeListener {

    private Activity context;
    private WelcomeContract.IView mView;
    //引导页
    private int[] imgResourceId;
    private ViewPager upgradeVersionViewpager;
    private LinearLayout upgradeVersionLay;
    private UpgradeVersionGuideAdapter upgradeVersionGuideAdapter;
    private LinearLayout.LayoutParams searchParams;
    private int flagTag1;
    private int currentIndex;
    private boolean isFlag;

    public WelcomePresenter(Activity context, WelcomeContract.IView logoView) {
        this.context = context;
        this.mView = logoView;
    }


    /**
     * 首次登录   引导页
     */
    @Override
    public void showGuide(Handler handler) {
//        context.setContentView(R.layout.activity_show_guide);
        //保存版本升级图显示之后的标志
        SPUtils.put(context, AppUtils.getVersionName(context), true);
        mView.startMainScreen();
        imgResourceId = new int[]{R.mipmap.upgrade_version_guide1};
        upgradeVersionViewpager = (ViewPager) context.findViewById(R.id.upgrade_version_viewpager);
        upgradeVersionViewpager.setOnPageChangeListener(this);
        upgradeVersionLay = (LinearLayout) context.findViewById(R.id.upgrade_version_lay);
        upgradeVersionGuideAdapter = new UpgradeVersionGuideAdapter(context,
                imgResourceId, handler);
        upgradeVersionViewpager.setAdapter(upgradeVersionGuideAdapter);

        searchParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 得到圆形图片距离右边的距离
        searchParams.rightMargin = DensityUtils.dp2px(context, 15);
        searchParams.bottomMargin = DensityUtils.dp2px(context, 25);
        setCircleImg(0);
    }

    /**
     * 设置显示的圆形图标
     *
     * @param num
     */
    public void setCircleImg(int num) {
        upgradeVersionLay.removeAllViews();
        ImageView mCircleImg;
        if (null == imgResourceId) {
            return;
        }
        for (int i = 0; i < imgResourceId.length; i++) {
            mCircleImg = new ImageView(context);
            if (i == num) {
                mCircleImg.setBackgroundResource(R.drawable.ic_radio_button_unchecked_black_);
            } else {
                mCircleImg.setBackgroundResource(R.drawable.ic_radio_button_checked_black_);
            }
            if (i == imgResourceId.length - 1) {
                upgradeVersionLay.addView(mCircleImg);
            } else {
                upgradeVersionLay.addView(mCircleImg, searchParams);
            }
        }
        if (imgResourceId.length == 1 || num == imgResourceId.length - 1) {
            upgradeVersionLay.removeAllViews();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        flagTag1 = positionOffsetPixels;
    }

    @Override
    public void onPageSelected(int position) {
        isFlag = true;// 用来判断是否还可以跳转完成到选择的位置
        setCircleImg(position);// 设置底部小点选中状态
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 滑动到最后一个页面在滑动直接进入主程序
        // 当滑动状态改变时调用 arg0
        // state==1的时候表示正在滑动，state==2的时候表示滑动完毕了，state==0的时候表示什么都没做，就是停在那
        if (state == 0) {
            if (flagTag1 == 0
                    && currentIndex == (upgradeVersionGuideAdapter.getCount() - 1)) {
                // 变为false，进入主页面，如果向右滑动则会重新调用onPageSelected方法把isFlag变为true
                // 如果直接用（!isFlag）则到最后一页会直接进入主页面
                if (isFlag) {
                    isFlag = !isFlag;
                } else {
                    //保存版本升级图显示之后的标志
                    SPUtils.put(context, AppUtils.getVersionName(context), true);
                    mView.startMainScreen();
                }
            }
        }
    }
}
