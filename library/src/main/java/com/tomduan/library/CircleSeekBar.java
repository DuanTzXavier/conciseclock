package com.tomduan.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomduan on 16-4-12.
 */
public class CircleSeekBar extends View {

    private static final double RADIAN = 180 / Math.PI;
    private static final float TIME_UNIT = (float) 0.25;
    private static final float FIFTEEN_TIME_UNIT = (float) 3.75;

    //Instance
//    private static final String CLICK_LSNR = "click listener";
//    private static final String CHANGE_LSNR = "change listener";
//    private static final String ARCS = "arc list";
//    private static final String COLORS = "color list";
//    private static final String INVAILD_ANG = "invaild angle";
//    private static final String INVAILD_START_ANG = "invaild start angle";
//    private static final String START_ANG = "start angle";
//    private static final String SWEEP_ANG = "sweep angle";
//    private static final String STYLE = "style";
//    private static final String WHEELX_ANG = "wheel x angle";
//    private static final String WHEELY_ANG = "wheel y angle";

    public static final int NUMBER = 0x0000000;
    public static final int CLOCK = 0x0000001;



    private OnSeekBarChangeListener mChangListener;
    private ClickListener clickListener;

    private int mMaxNumber = 100;
    private int mCurrentNumber = 10;

    private float mRestAngle = 360;
    private float mAbsloutaleAngle;
    private float mStartAngle;
    private float mSweepAngle = 0;
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

    private Paint mTextPaint;
    private String text = "";
    private int textColor;
    private int textSize;
    private boolean showText = true;
    private Rect bounds = new Rect();

    private Paint mScalePaint;
    private int scaleSize;
    private int scaleColor;

    private boolean isSetStart = true;
    private boolean isInvaild = false;
    private boolean isBlockEnd = true;
    private boolean isEnd;

    private int style = NUMBER;

    private List<Arc> arcs = new ArrayList<>();

    private List<Integer> colors = new ArrayList<>();

    private float tempCos = 0;
    private float tempX = 0;

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

        arcs.clear();
    }

    private void initPaint() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColor);

        mCirclePaint.setStrokeWidth(getDpValue(circleWidth));
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mInvaildPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInvaildPaint.setColor(invaildColor);
        mInvaildPaint.setStrokeWidth(getDpValue(rangeWidth));
        mInvaildPaint.setStyle(Paint.Style.STROKE);

        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(selectColor);
        mSelectPaint.setStrokeWidth(getDpValue(rangeWidth));
        mSelectPaint.setStyle(Paint.Style.STROKE);

        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setColor(pointerColor);
        mPointerPaint.setStyle(Paint.Style.FILL);
        mPointerRadius = getDpValue(rangeWidth);

        mEdgePaint = new Paint(mSelectPaint);
        mEdgePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(textSize);

        mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mScalePaint.setColor(textColor);
        mScalePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mScalePaint.setTextAlign(Paint.Align.CENTER);
        mScalePaint.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float left = getPaddingLeft() + getDpValue(circleWidth) / 2;
        float top = getPaddingTop() + getDpValue(circleWidth) / 2;
        float right = canvas.getWidth() - getPaddingRight() - getDpValue(circleWidth) / 2;
        float bottom = canvas.getHeight() - getPaddingBottom() - getDpValue(circleWidth) / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;

        float wheelRadius = (canvas.getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - getDpValue(circleWidth) / 2;

        canvas.drawCircle(centerX, centerY, wheelRadius, mCirclePaint);

        if (!isSetStart){
            canvas.drawArc(new RectF(left, top, right, bottom),
                    (mStartAngle - 90),
                    mSweepAngle,
                    false,
                    mSelectPaint);
        }

        drawInvaildArc(canvas, new RectF(left, top, right, bottom));

//        if (isInvaild){
//            canvas.drawArc(new RectF(left, top, right, bottom),
//                    mInvaildStartAngle - 90,
//                    mInvaildAngle,
//                    false,
//                    mInvaildPaint);
//        }

        mTextPaint.getTextBounds(text, 0, text.length(), bounds);

        if (showText){
            canvas.drawText(
                    text,
                    getWidth()/2,
                    getHeight()/2,
                    mTextPaint);
        }
        String time;

        //绘制文字刻度
        for (int i = 0; i < 8; i++) {
            canvas.save();// 保存当前画布
            canvas.rotate(360 / 8 * i, getWidth() / 2, getHeight() / 2);

            time = 24 / 8 * i + ":00";

            canvas.drawText(time, getWidth()/2, getHeight()/2 - wheelRadius + getDpValue(circleWidth) / 2 + getDpValue(9) + 40, mScalePaint);
            canvas.restore();//
        }

        Paint paint = new Paint();
//        paint.setColor(roundBackgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getDpValue(6));
        paint.setAntiAlias(true);

        float start = -90f;
        float p = ((float) mMaxNumber / (float) 100);
        float singlPoint = 3.75f;
//        p = (int) (progress * p);
        for (int i = 0; i < 96; i++) {
            paint.setColor(Color.BLACK);
            // 绘制间隔快
            canvas.drawArc(new RectF(
                            getPaddingLeft() + getDpValue(circleWidth) + getDpValue(6)/2,
                            getPaddingTop() + getDpValue(circleWidth) + getDpValue(6)/2,
                            canvas.getWidth() - getPaddingRight() - getDpValue(circleWidth) - getDpValue(6)/2,
                            canvas.getHeight() - getPaddingBottom() - getDpValue(circleWidth) - getDpValue(6)/2),
                    start + singlPoint - 0.3f,
                    0.3f,
                    false,
                    paint);
            start = (start + singlPoint);
        }

        paint.setStrokeWidth(getDpValue(10));
        singlPoint = 45f;
        for (int i = 0; i < 8; i++) {
            paint.setColor(Color.BLACK);
            // 绘制间隔快
            canvas.drawArc(new RectF(
                            getPaddingLeft() + getDpValue(circleWidth) + getDpValue(10)/2,
                            getPaddingTop() + getDpValue(circleWidth) + getDpValue(10)/2,
                            canvas.getWidth() - getPaddingRight() - getDpValue(circleWidth) - getDpValue(10)/2,
                            canvas.getHeight() - getPaddingBottom() - getDpValue(circleWidth) - getDpValue(10)/2),
                    start + singlPoint - 0.6f,
                    0.6f,
                    false,
                    paint);
            start = (start + singlPoint);
        }

        if (!isCircle()){
            canvas.drawCircle(mWheelCurX, mWheelCurY, mPointerRadius, mPointerPaint);
        }
    }

    private void drawInvaildArc(Canvas canvas, RectF rectF) {

        if (colors.size() != arcs.size()){
            Log.i("error", "color.size != arcs.size");
            return;
        }

        for (int i = 0;i < arcs.size();i++){

            mInvaildPaint.setColor(colors.get(i));

            if (arcs.get(i).istouched()){
                mInvaildPaint.setStrokeWidth(getDpValue((float) (rangeWidth * 1.5)));
            }else {
                mInvaildPaint.setStrokeWidth(getDpValue(rangeWidth));
            }


            canvas.drawArc(rectF,
                    arcs.get(i).getStartArc() - 90,
                    arcs.get(i).getSweepArc(),
                    false,
                    mInvaildPaint);
        }
    }

    private boolean checkIsOnTouchArc(Arc arc){
        float start = arc.getStartArc();
        float sweep = arc.getSweepArc();
        float ontouch = getTouchedAbsAngle();

        start = start > 360 ? start - 360 : start;

        Log.i("sss", ""+ start + "  " + sweep);

        if (start + sweep > 360){
            if (ontouch > start || ontouch < start + sweep - 360){
                return true;
            }else{
                return false;
            }

        }else {
            if (ontouch > start && ontouch < start + sweep){
                return true;
            }else {
                return false;
            }
        }
    }

    public boolean isCircle() {

        if (mInvaildAngle > 359.50){
            text = "complete";
        }
        invalidate();
        return mInvaildAngle > 359.50;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        double cos = -Math.cos(Math.toRadians(mSweepAngle));
        float radius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - getDpValue(circleWidth)) / 2;
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
        if (event.getAction() == MotionEvent.ACTION_MOVE || isTouch(x, y)) {// 通过当前触摸点搞到cos角度值
            float cos = computeCos(x, y);// 通过反三角函数获得角度值

            tempCos = cos;
            tempX = x;

            for (int i = 0;i < arcs.size();i++){
                if (checkIsOnTouchArc(arcs.get(i))){
                    arcs.get(i).setIstouched(true);
                    if (clickListener != null){
                        clickListener.clicked(i);
                    }
                }else {
                    arcs.get(i).setIstouched(false);
                }
            }

            if (isSetStart){
                if (x < getWidth() / 2) {// 滑动超过180度,左半圆
                    mStartAngle = (float) (Math.PI * RADIAN + Math.acos(cos) * RADIAN);
                } else {// 没有超过180度,右半圆
                    mStartAngle = (float) (Math.PI * RADIAN - Math.acos(cos) * RADIAN);
                }
            }else {
                mSweepAngle = calculateSweep(cos, x);
            }

            mCurrentNumber = getSelectedValue();
            text = isCircle() ? "complete" : getCurrent();

            if (isBlockEnd){
                float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - getDpValue(circleWidth)) / 2;
                mWheelCurX = calcXLocationInWheel(x, cos, radius);
                mWheelCurY = calcYLocationInWheel(cos, radius);
            }else {
                cos = (float) (isEnd ? -Math.cos(Math.toRadians(mInvaildStartAngle)) : -Math.cos(Math.toRadians(mStartAngle)));
                float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - getDpValue(circleWidth)) / 2;
                mWheelCurX = calcXLocationInWheel(getmAbsloutaleAngle() > 180 ? 0 : x, cos, radius);//考虑不完全
                mWheelCurY = calcYLocationInWheel(cos, radius);
                Log.i("true", "" + mWheelCurX + ", " + mWheelCurY + ", " + getMeasuredWidth());
                //bug :右半球 mX变成左半球的mX
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getParent() != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
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

    private float getTouchedAbsAngle(){
        float absAngle ;

        if (tempX < getWidth() / 2) {// 滑动超过180度,左半圆
            absAngle = (float) (Math.PI * RADIAN + Math.acos(tempCos) * RADIAN);
        } else {// 没有超过180度,右半圆
            absAngle = (float) (Math.PI * RADIAN - Math.acos(tempCos) * RADIAN);
        }
//        Log.i("aaaa", absAngle + "");
        return absAngle;
    }

    //Save&RestoreInstance
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(INATANCE_STATE, super.onSaveInstanceState());
//        bundle.putInt(INSTANCE_MAX_PROCESS, mMaxProcess);
//        bundle.putInt(INSTANCE_CUR_PROCESS, mCurProcess);
//        bundle.putInt(INSTANCE_REACHED_COLOR, mReachedColor);
//        bundle.putFloat(INSTANCE_REACHED_WIDTH, mReachedWidth);
//        bundle.putBoolean(INSTANCE_REACHED_CORNER_ROUND, isHasReachedCornerRound);
//        bundle.putInt(INSTANCE_UNREACHED_COLOR, mUnreachedColor);
//        bundle.putFloat(INSTANCE_UNREACHED_WIDTH, mUnreachedWidth);
//        bundle.putInt(INSTANCE_POINTER_COLOR, mPointerColor);
//        bundle.putFloat(INSTANCE_POINTER_RADIUS, mPointerRadius);
//        bundle.putBoolean(INSTANCE_POINTER_SHADOW, isHasPointerShadow);
//        bundle.putFloat(INSTANCE_POINTER_SHADOW_RADIUS, mPointerShadowRadius);
//        bundle.putBoolean(INSTANCE_WHEEL_SHADOW, isHasWheelShadow);
//        bundle.putFloat(INSTANCE_WHEEL_SHADOW_RADIUS, mPointerShadowRadius);
//        bundle.putBoolean(INSTANCE_WHEEL_HAS_CACHE, isHasCache);
//        bundle.putBoolean(INSTANCE_WHEEL_CAN_TOUCH, isCanTouch);
//        bundle.putByte();
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if (state instanceof Bundle) {
//            Bundle bundle = (Bundle) state;
//            super.onRestoreInstanceState(bundle.getParcelable(INATANCE_STATE));
//            mMaxProcess = bundle.getInt(INSTANCE_MAX_PROCESS);
//            mCurProcess = bundle.getInt(INSTANCE_CUR_PROCESS);
//            mReachedColor = bundle.getInt(INSTANCE_REACHED_COLOR);
//            mReachedWidth = bundle.getFloat(INSTANCE_REACHED_WIDTH);
//            isHasReachedCornerRound = bundle.getBoolean(INSTANCE_REACHED_CORNER_ROUND);
//            mUnreachedColor = bundle.getInt(INSTANCE_UNREACHED_COLOR);
//            mUnreachedWidth = bundle.getFloat(INSTANCE_UNREACHED_WIDTH);
//            mPointerColor = bundle.getInt(INSTANCE_POINTER_COLOR);
//            mPointerRadius = bundle.getFloat(INSTANCE_POINTER_RADIUS);
//            isHasPointerShadow = bundle.getBoolean(INSTANCE_POINTER_SHADOW);
//            mPointerShadowRadius = bundle.getFloat(INSTANCE_POINTER_SHADOW_RADIUS);
//            isHasWheelShadow = bundle.getBoolean(INSTANCE_WHEEL_SHADOW);
//            mPointerShadowRadius = bundle.getFloat(INSTANCE_WHEEL_SHADOW_RADIUS);
//            isHasCache = bundle.getBoolean(INSTANCE_WHEEL_HAS_CACHE);
//            isCanTouch = bundle.getBoolean(INSTANCE_WHEEL_CAN_TOUCH);
//            initPaints();
//        } else {
//            super.onRestoreInstanceState(state);
//        }
//
//        if (mChangListener != null) {
//            mChangListener.onChanged(this, mMaxProcess, mCurProcess);
//        }
//    }



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
        switch (style){
            case NUMBER:
                text = String.valueOf(mCurrentNumber);
                break;
            case CLOCK:
                int min = (int) (getmAbsloutaleAngle()/FIFTEEN_TIME_UNIT);
//                String mins = String.valueOf((min % 60) < 10 ? "0" + min % 60 : (min % 60));
                String mins = String.valueOf(min % 4 < 1 ? "00" : (min % 4) * 15);
                text = min / 4 + ":" + mins;
                break;
        }
        return text;
    }

    public void build(){
        initPaint();
    }

    public void setmChangListener(OnSeekBarChangeListener mChangListener) {
        this.mChangListener = mChangListener;
    }

    public float getmSweepAngle(){
        return this.mSweepAngle;
    }

    public void reSeek() {
        if (!isSetStart) {
            isInvaild = true;

            if (mSweepAngle > 0){
                arcs.add(new Arc(mStartAngle, mSweepAngle));
                invaildColor = invaildColor - 101010;
                colors.add(invaildColor);
            }
        }

        this.mInvaildAngle += mSweepAngle;
        this.mStartAngle += mSweepAngle;
        this.mRestAngle -= mSweepAngle;
        this.mSweepAngle = 0;
        build();
        invalidate();
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
        switch (style) {
            case NUMBER:
                text = "0";
                break;
            case CLOCK:
                text = "0:00";
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public interface OnSeekBarChangeListener {
        void onChanged(CircleSeekBar seekbar, int maxValue, int curValue);
    }

    public interface ClickListener{
        void clicked(int position);
    }

    public void setClickListener(ClickListener listener){
        this.clickListener = listener;
    }

    private float getDpValue(float w) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, getContext().getResources().getDisplayMetrics());
    }
}
