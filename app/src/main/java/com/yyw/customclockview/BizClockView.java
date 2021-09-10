package com.yyw.customclockview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * 时钟组件
 */
public class BizClockView extends View {
    private final static int DEFAULT_POINTER_COLOR = Color.parseColor("#b8dbff");
    private final static int DEFAULT_INNER_CIRCLE_COLOR = Color.parseColor("#000393");
    private final static int DEFAULT_SECOND_POINTER_COLOR = Color.parseColor("#e0402b");

    //The ARC constant
    private static final double ARC_LENGTH = Math.PI * 2;
    private static final double SCALE_ARC_LENGTH_OFFSET = ARC_LENGTH / 12d;

    private static final double CLOCK_HOUR_HAND_ARC_LENGTH = SCALE_ARC_LENGTH_OFFSET;
    private static final double CLOCK_MINUTE_HAND_ARC_LENGTH = ARC_LENGTH / 60d;
    private static final double CLOCK_SECOND_HAND_ARC_LENGTH = CLOCK_MINUTE_HAND_ARC_LENGTH;

    private static final double CLOCK_START_ARC = 90 * Math.PI / 180d;

    private int mCenterPointColor = DEFAULT_POINTER_COLOR;
    private int mInnerCenterPointColor = DEFAULT_INNER_CIRCLE_COLOR;
    private int mHourHandColor = DEFAULT_POINTER_COLOR;
    private int mMinuteHandColor = DEFAULT_POINTER_COLOR;
    private int mSecondHandColor = DEFAULT_SECOND_POINTER_COLOR;

    private Paint mCenterCirclePaint, mInnerCenterCirclePaint, mHourHandPaint, mMinHandPaint, mSecondHandPaint;

    private int mWidth, mHeight;
    private float mCenterX, mCenterY, mClockStrokeWidth, mClockRadius, mCenterCircleRadius;

    private float mStopHourHandLength;
    private float mStopMinHandLength;
    private float mStopSecondHandLength;

    private int mHour, mMinute, mSecond;

    private int mDrawableResource;

    public BizClockView(Context context) {
        this(context, null);
    }

    public BizClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BizClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BizClockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttrs(attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //没有指定宽高,使用了wrap_content,则手动指定宽高为MATCH_PARENT
        // (No width or height is specified, wrap_content is used, and the width and height are manually specified as MATCH_PARENT)
        if (modeWidth == AT_MOST && modeHeight == AT_MOST) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            setLayoutParams(layoutParams);
        }
    }

    private void parseAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClockView);
        mCenterPointColor = typedArray.getColor(R.styleable.ClockView_center_point_color, DEFAULT_POINTER_COLOR);
        mHourHandColor = typedArray.getColor(R.styleable.ClockView_hour_hand_color, DEFAULT_POINTER_COLOR);
        mMinuteHandColor = typedArray.getColor(R.styleable.ClockView_minute_hand_color, DEFAULT_POINTER_COLOR);
        mSecondHandColor = typedArray.getColor(R.styleable.ClockView_second_hand_color, DEFAULT_SECOND_POINTER_COLOR);
        mDrawableResource = typedArray.getResourceId(R.styleable.ClockView_src, R.mipmap.home_alarm_bg);
        typedArray.recycle();
    }

    private void init() {
        mCenterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterCirclePaint.setStyle(Paint.Style.FILL);
        mCenterCirclePaint.setColor(mCenterPointColor);

        mInnerCenterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCenterCirclePaint.setStyle(Paint.Style.FILL);
        mInnerCenterCirclePaint.setColor(mInnerCenterPointColor);

        mHourHandPaint = new Paint();
        mHourHandPaint.setStrokeCap(Paint.Cap.ROUND);
        mHourHandPaint.setStrokeJoin(Paint.Join.ROUND);
        mHourHandPaint.setAntiAlias(true);
        mHourHandPaint.setColor(mHourHandColor);

        mMinHandPaint = new Paint();
        mMinHandPaint.setStrokeCap(Paint.Cap.ROUND);
        mMinHandPaint.setStrokeJoin(Paint.Join.ROUND);
        mMinHandPaint.setAntiAlias(true);
        mMinHandPaint.setColor(mMinuteHandColor);

        mSecondHandPaint = new Paint();
        mSecondHandPaint.setStrokeCap(Paint.Cap.ROUND);
        mSecondHandPaint.setStrokeJoin(Paint.Join.ROUND);
        mSecondHandPaint.setAntiAlias(true);
        mSecondHandPaint.setColor(mSecondHandColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mCenterX = w / 2;
        mCenterY = h / 2;

        int minSize = Math.min(w, h);

        mClockRadius = minSize / 3f;
        mClockStrokeWidth = mClockRadius / 7f;

        mHourHandPaint.setStrokeWidth(mClockStrokeWidth / 2f);
        mMinHandPaint.setStrokeWidth(mHourHandPaint.getStrokeWidth());
        mSecondHandPaint.setStrokeWidth(mHourHandPaint.getStrokeWidth() / 2f);

        mCenterCircleRadius = mClockStrokeWidth / 2f;

        mStopHourHandLength = mClockRadius * 3.2f / 7f;
        mStopMinHandLength = mClockRadius * 4.2f / 7f;
        mStopSecondHandLength = mClockRadius * 4.2f / 7f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawClock(canvas);
    }

    private void drawClock(Canvas canvas) {

        drawHour(canvas);
        drawMinute(canvas);
        drawSecond(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mCenterCircleRadius, mCenterCirclePaint);
        canvas.drawCircle(mCenterX, mCenterY, mCenterCircleRadius - 8, mInnerCenterCirclePaint);
        drawBg(canvas);
    }

    private void drawBg(Canvas canvas) {
        Bitmap bitmap = getBitmapForBg();
        RectF rectF = new RectF(0, 0, 100, 100);
//        canvas.drawBitmap(bitmap, mWidth, mHeight, new Paint());
        canvas.drawBitmap(bitmap, null, rectF, new Paint());
    }

    private Bitmap getBitmapForBg() {
        return BitmapFactory.decodeResource(getResources(), mDrawableResource);
    }

    private void drawHour(Canvas canvas) {
        float angle = (float) ((mHour % 12 + mMinute / 60f) * CLOCK_HOUR_HAND_ARC_LENGTH - CLOCK_START_ARC);

        float hourEndX = (float) (mStopHourHandLength * Math.cos(angle) + mCenterX);
        float hourEndY = (float) (mStopHourHandLength * Math.sin(angle) + mCenterY);

        canvas.drawLine(mCenterX, mCenterY, hourEndX, hourEndY, mHourHandPaint);
    }

    private void drawMinute(Canvas canvas) {
        float angle = (float) (mMinute * CLOCK_MINUTE_HAND_ARC_LENGTH - CLOCK_START_ARC);

        float minEndX = (float) (mStopMinHandLength * Math.cos(angle) + mCenterX);
        float minEndY = (float) (mStopMinHandLength * Math.sin(angle) + mCenterY);

        canvas.drawLine(mCenterX, mCenterY, minEndX, minEndY, mMinHandPaint);
    }

    private void drawSecond(Canvas canvas) {
        float angle = (float) (mSecond * CLOCK_SECOND_HAND_ARC_LENGTH - CLOCK_START_ARC);

        float secondEndX = (float) (mStopSecondHandLength * Math.cos(angle) + mCenterX);
        float secondEndY = (float) (mStopSecondHandLength * Math.sin(angle) + mCenterY);

        canvas.drawLine(mCenterX, mCenterY, secondEndX, secondEndY, mSecondHandPaint);
    }

    /**
     * To check if the input time is correct.
     */
    private void checkTime() {
        if (mHour < 0) {
            mHour = 0;
        }

        if (mHour > 24) {
            mHour = 24;
        }

        if (mMinute < 0) {
            mMinute = 0;
        }

        if (mMinute > 60) {
            mMinute = 60;
        }

        if (mSecond < 0) {
            mSecond = 0;
        }

        if (mSecond > 60) {
            mSecond = 60;
        }
    }

    /**
     * Bind the clock to real time.
     *
     * @param hour   The hour, it should be [0-24].
     * @param minute The minute, it should be [0-60].
     * @param second The second, it should be [0-60].
     */
    public void setTime(int hour, int minute, int second) {
        this.mHour = hour;
        this.mMinute = minute;
        this.mSecond = second;

        checkTime();

        postInvalidate();
    }
}
