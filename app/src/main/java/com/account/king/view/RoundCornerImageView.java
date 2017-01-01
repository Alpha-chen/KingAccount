package com.account.king.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.ffrj.pinkwallet.R;


/**
 * 圆角ImageView
 */
public class RoundCornerImageView extends ImageView {

    public RoundCornerImageView(Context context) {
        this(context, null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView, 0, 0);
        rect_adius = typedArray.getDimension(R.styleable.RoundCornerImageView_corner_radius, 10);
        init();
    }

    /**
     * dp
     */
    private float rect_adius = 20;
    private final RectF roundRect = new RectF();
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();

    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        //
        float density = getResources().getDisplayMetrics().density;
        rect_adius = rect_adius * density;
    }

    /**
     * @param adius dp
     */
    public void setRectAdius(float adius) {
        rect_adius = dip2px(adius);
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        super.draw(c);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        zonePaint.setShader(shader);
        roundRect.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
    }

    public float dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }
}
