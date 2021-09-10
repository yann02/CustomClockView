package com.yyw.customclockview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 *
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/9/9 15:19
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class CustomViewImage extends View {


    public CustomViewImage(Context context) {
        super(context);
    }

    public CustomViewImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomViewImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
    }

    private void drawBg(Canvas canvas) {
        Bitmap bitmap = getBitmapForBg();
        Rect rect = new Rect(0, 0, 200, 200);
        canvas.drawBitmap(bitmap,null, rect, new Paint());
    }

    private Bitmap getBitmapForBg() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.home_alarm_bg);
    }
}
