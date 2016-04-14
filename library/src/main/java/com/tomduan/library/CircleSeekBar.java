package com.tomduan.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tomduan on 16-4-12.
 */
public class CircleSeekBar extends View {

    private static final double RADIAN = 180 / Math.PI;
    private static final float TIME_UNIT = (float) 0.25;

    public static final int NUMBER = 0x0000000;
    public static final int CLOCK = 0x0000001;



    private OnSeekBarChangeListener mChangListener;

    private int mMaxNumber = 100;
    private int mCurrentNumber = 10;

    private float mRestAngle = 360;
    private float mAbsloutaleAngle;
    private float mStartAngle;
    private float mSweepAngle;
    private float mInvaildStartAngle = 0;
    private float mInvaildAngle = 0;
    private float mWheelCurX;
    private float mWheelCurY;

    private Paint mCirclePaint;
    private int circleColor;
    private float circleWidth;

    private Paint mInvaildPaint;
    private int invaildColor;
    private float rangeWidth;

    private Paint mSelectPaint;
    private int selectColor;

    private Paint mPointerPaint;
    private int pointerColor;
    private float mPointerRadius;

    private Paint mEdgePaint;

    private boolean isSetStart = true;
    private boolean isInvaild = false;
    private boolean isBlockEnd = true;
    private boolean isEnd;

    private int style = NUMBER;

    public CircleSeekBar(Context context) {
        this(context, null);


    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        initAttrs(attrs, defStyleAttr);
//        initPadding();
//        initPaints();
        initPaint();
//        initClock();
    }

    private void initPaint() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStrokeWidth(circleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mInvaildPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInvaildPaint.setColor(invaildColor);
        mInvaildPaint.setStrokeWidth(rangeWidth);
        mInvaildPaint.setStyle(Paint.Style.STROKE);

        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(selectColor);
        mSelectPaint.setStrokeWidth(rangeWidth);
        mSelectPaint.setStyle(Paint.Style.STROKE);

        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setColor(pointerColor);
        mPointerPaint.setStyle(Paint.Style.FILL);
        mPointerRadius = rangeWidth;

        mEdgePaint = new Paint(mSelectPaint);
        mEdgePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float left = getPaddingLeft() + circleWidth / 2;
        float top = getPaddingTop() + circleWidth / 2;
        float right = canvas.getWidth() - getPaddingRight() - circleWidth / 2;
        float bottom = canvas.getHeight() - getPaddingBottom() - circleWidth / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;

        float wheelRadius = (canvas.getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - circleWidth / 2;

        canvas.drawCircle(centerX, centerY, wheelRadius, mCirclePaint);

        if (!isSetStart){
            canvas.drawArc(new RectF(left, top, right, bottom),
                    (mStartAngle - 90),
                    mSweepAngle,
                    false,
                    mSelectPaint);
        }

        if (isInvaild){
            canvas.drawArc(new RectF(left, top, right, bottom),
                    mInvaildStartAngle - 90,
                    mInvaildAngle,
                    false,
                    mInvaildPaint);
        }
        if (!isCircle()){
            canvas.drawCircle(mWheelCurX, mWheelCurY, mPointerRadius, mPointerPaint);
        }
    }

    public boolean isCircle() {
        return mInvaildAngle == 360;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

//        mSweepAngle = (float) (mCurrentNumber / mMaxNumber * 360.0);
        double cos = -Math.cos(Math.toRadians(mSweepAngle));
        float radius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - circleWidth) / 2;
        mWheelCurX = calcXLocationInWheel(mSweepAngle > 180 ? 0 : min, (float) cos, radius);
        mWheelCurY = calcYLocationInWheel((float) cos, radius);


    }

    private float calcXLocationInWheel(float x, float cos, float radius) {
        if (x > (getMeasuredWidth() / 2)) {
            return (float) (getWidth() / 2 + Math.sqrt(1 - cos * cos) * radius);
        } else {
            return (float) (getWidth() / 2 - Math.sqrt(1 - cos * cos) * radius);
        }
    }

    private float calcYLocationInWheel(float cos, float radius) {
        return getMeasuredWidth() / 2 + radius * cos;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE || isTouch(x, y)) {
            // 通过当前触摸点搞到cos角度值
            float cos = computeCos(x, y);
            // 通过反三角函数获得角度值
//            Log.i("cos", "s:" + Math.acos(cos) * RADIAN);
            if (isSetStart){
                if (x < getWidth() / 2) {
                // 滑动超过180度,左半圆
                    mStartAngle = (float) (Math.PI * RADIAN + Math.acos(cos) * RADIAN);
                } else {
                // 没有超过180度,右半圆
                    mStartAngle = (float) (Math.PI * RADIAN - Math.acos(cos) * RADIAN);
                }

//                Log.i("start", "s:" + mStartAngle);
//                isSetStart = !isSetStart;
            }else {
                mSweepAngle = calculateSweep(cos, x);
//                Log.i("iii", "" + mSweepAngle);
            }

            mCurrentNumber = getSelectedValue();

            if (isBlockEnd){
                float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - circleWidth) / 2;
                mWheelCurX = calcXLocationInWheel(x, cos, radius);
                mWheelCurY = calcYLocationInWheel(cos, radius);
            }else {
                if (isEnd){
                    cos = (float) -Math.cos(Math.toRadians(mInvaildStartAngle));
                    float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - circleWidth) / 2;
                    mWheelCurX = calcXLocationInWheel(getmAbsloutaleAngle() > 180 ? 0 : x, cos, radius);
//                    mWheelCurX = calcXLocationInWheel(x, cos, radius);
                    mWheelCurY = calcYLocationInWheel(cos, radius);
                    Log.i("true", "" + mWheelCurX + ", " + mWheelCurY);
                }else {
                    cos = (float) -Math.cos(Math.toRadians(mStartAngle));
                    float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - circleWidth) / 2;
                    mWheelCurX = calcXLocationInWheel(getmAbsloutaleAngle() > 180 ? 0 : x, cos, radius);
//                    mWheelCurX = calcXLocationInWheel(x, cos, radius);
                    mWheelCurY = calcYLocationInWheel(cos, radius);
                    Log.i("false", "" + mWheelCurX + ", " + mWheelCurY);
                }
            }
            if (mChangListener != null) {
                mChangListener.onChanged(this, mMaxNumber, mCurrentNumber);
            }
            invalidate();
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    private float calculateSweep(float cos, float x) {
        float calculate;

        calculate = x < (getWidth() /2) ?
                (float) (Math.PI * RADIAN + Math.acos(cos) * RADIAN):
                (float) (Math.PI * RADIAN - Math.acos(cos) * RADIAN);
        calculate -= mStartAngle;
        calculate = calculate > 0 ? calculate : (calculate + 360);

        calculate = calculate > 0 ? calculate : 0;
        isBlockEnd = calculate < 0;

        calculate = calculate > (mRestAngle + mInvaildAngle / 3) ? 0: calculate;
        if (calculate == 0){
            isBlockEnd = false;
            isEnd = false;
            return calculate;
        }
        calculate = calculate > mRestAngle ? mRestAngle: calculate;

        isBlockEnd = calculate < mRestAngle;
        isEnd = true;

        return calculate;
    }

    private float computeCos(float x, float y) {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    private boolean isTouch(float x, float y) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius * radius;
    }

    private float getCircleWidth() {
        return Math.max(circleWidth, Math.max(rangeWidth, mPointerRadius));
    }

    private int getSelectedValue() {
        return Math.round(mMaxNumber * (getmAbsloutaleAngle() / 360));
    }

    private float getmAbsloutaleAngle(){
        mAbsloutaleAngle = mStartAngle + mSweepAngle;
        if (mAbsloutaleAngle > 360){
            mAbsloutaleAngle = mAbsloutaleAngle - 360;
        }
        return this.mAbsloutaleAngle;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public void setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
    }

    public void setInvaildColor(int invaildColor) {
        this.invaildColor = invaildColor;
    }

    public void setRangeWidth(float rangeWidth) {
        this.rangeWidth = rangeWidth;
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public void setPointerColor(int pointerColor) {
        this.pointerColor = pointerColor;
    }

    public void setmMaxNumber(int mMaxNumber) {
        this.mMaxNumber = mMaxNumber;
    }

    public void setmCurrentNumber(int mCurrentNumber) {
        this.mCurrentNumber = mCurrentNumber;
    }

    public void setIsSetStart(boolean is){
        this.isSetStart = is;
    }

    public void initInvaildStartAngle(){
        this.mInvaildStartAngle = mStartAngle;
    }

    public String getCurrent(){
        String current = "";

        switch (style){

            case NUMBER:

                current = String.valueOf(mCurrentNumber);

                break;

            case CLOCK:

                int min = (int) (getmAbsloutaleAngle()/TIME_UNIT);

                current = min / 60 + ":" + (min % 60);

                break;

        }
        return current;
    }

    public void build(){
        initPaint();
//        postInvalidate();
    }

    public void setmChangListener(OnSeekBarChangeListener mChangListener) {
        this.mChangListener = mChangListener;
    }

    public float getmSweepAngle(){
        return this.mSweepAngle;
    }

    public void reSeek(){
        isInvaild = true;
        this.mInvaildAngle += mSweepAngle;
        this.mStartAngle += mSweepAngle;
        this.mRestAngle -= mSweepAngle;
        this.mSweepAngle = 0;
        build();
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public interface OnSeekBarChangeListener {
        void onChanged(CircleSeekBar seekbar, int maxValue, int curValue);
    }
}
