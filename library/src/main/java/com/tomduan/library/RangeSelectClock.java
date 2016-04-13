package com.tomduan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tomduan on 16-4-11.
 */
public class RangeSelectClock extends View {

    private static final int TEXT_SIZE_DEFAULT_VALUE = 25;
    private static final int END_WHEEL_DEFAULT_VALUE = 360;
    public static final int COLOR_WHEEL_STROKE_WIDTH_DEF_VALUE = 16;
    public static final float POINTER_RADIUS_DEF_VALUE = 20;
    public static final int MAX_POINT_DEF_VALUE = 100;
    public static final int START_ANGLE_DEF_VALUE = 0;

    private OnCircleSeekBarChangeListener mOnCircleSeekBarChangeListener;

    private Context mContext;
    private int maxNumber;
    private String text;
    private boolean show_text;
    private int text_size;
    private int text_color;

    private Rect bounds = new Rect();
    //Arc
    private Paint mPaintArc;
    //PaintClock
    private Paint mPaintClock;
    private int unActiveColor;
    private int activeColor;
    private int mClockStrokeWidth;
    //PaintPoint
    private Paint mPaintPointer;
    private float mPointerRadius;
    private int mPointerColor;
    private float mClockRadius;
    //HaloPoint
    private Paint mPointerHaloPaint;
    private int colorHalo;
    private RectF mCenterHaloRectangle = new RectF();
    //init
    private int initPosition;

    //PaintText
    private Paint textPaint;

    //doing
    private int mArcRadians = 360;
    private int endRadians;
    private float mAngle;
    private int startRadians = 0;
    private int lastRadians = 0;
    private float mTranslationOffset;

    private float[] pointerPosition;

    private RectF mClockRectangle = new RectF();

    private float lastX;


    //TouchEvent
    private boolean block_end = false;
    private boolean block_start = false;
    private boolean mUserIsMovingPointer = false;

    public RangeSelectClock(Context context) {
        this(context, null);
    }

    public RangeSelectClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeSelectClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.RangeSelectClock, defStyleAttr, 0);

        initAttributes(typedArray);

        typedArray.recycle();
        // mAngle = (float) (-Math.PI / 2);

        mPaintClock = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaintClock.setShader(s);
        mPaintClock.setColor(unActiveColor);
        mPaintClock.setStyle(Paint.Style.STROKE);
        mPaintClock.setStrokeWidth(mClockStrokeWidth);

        Paint mColorCenterHalo = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorCenterHalo.setColor(Color.CYAN);
        mColorCenterHalo.setAlpha(0xCC);
        // mColorCenterHalo.setStyle(Paint.Style.STROKE);
        // mColorCenterHalo.setStrokeWidth(mColorCenterHaloRectangle.width() /
        // 2);

        mPointerHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerHaloPaint.setColor(colorHalo);
        mPointerHaloPaint.setStrokeWidth(mPointerRadius + 10);
        // mPointerHaloPaint.setAlpha(150);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
//        textPaint.setColor(text_color);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        // canvas.drawPaint(textPaint);
//        textPaint.setTextSize(text_size);

        mPaintPointer = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPointer.setStrokeWidth(mPointerRadius);

        // mPointerColor.setColor(calculateColor(mAngle));
        mPaintPointer.setColor(mPointerColor);

        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArc.setColor(activeColor);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeWidth(mClockStrokeWidth);

        Paint mCircleTextColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleTextColor.setColor(Color.WHITE);
        mCircleTextColor.setStyle(Paint.Style.FILL);

        mArcRadians = (int) calculateAngleFromText(initPosition) - 90;

        mAngle = calculateAngleFromRadians(mArcRadians > endRadians ? endRadians
                : mArcRadians);
//        setText(String.valueOf(calculateTextFromAngle(mArcRadians)));

        invalidate();
    }

    private void initAttributes(TypedArray typedArray) {
        mClockStrokeWidth = typedArray.getInteger(
                R.styleable.RangeSelectClock_wheel_size, COLOR_WHEEL_STROKE_WIDTH_DEF_VALUE);
        mPointerRadius = typedArray.getDimension(
                R.styleable.RangeSelectClock_pointer_size, POINTER_RADIUS_DEF_VALUE);
        maxNumber = typedArray.getInteger(R.styleable.RangeSelectClock_max, MAX_POINT_DEF_VALUE);

        String wheel_color_attr = typedArray
                .getString(R.styleable.RangeSelectClock_wheel_active_color);
        String wheel_unactive_color_attr = typedArray
                .getString(R.styleable.RangeSelectClock_wheel_unactive_color);
        String pointer_color_attr = typedArray
                .getString(R.styleable.RangeSelectClock_pointer_color);
        String pointer_halo_color_attr = typedArray
                .getString(R.styleable.RangeSelectClock_pointer_halo_color);

        String text_color_attr = typedArray.getString(R.styleable.RangeSelectClock_text_color);

        text_size = typedArray.getDimensionPixelSize(R.styleable.RangeSelectClock_text_size, TEXT_SIZE_DEFAULT_VALUE);

        initPosition = typedArray.getInteger(R.styleable.RangeSelectClock_init_position, 0);

        startRadians = typedArray.getInteger(R.styleable.RangeSelectClock_start_angle, START_ANGLE_DEF_VALUE);
        endRadians = typedArray.getInteger(R.styleable.RangeSelectClock_end_angle, END_WHEEL_DEFAULT_VALUE);

        show_text = typedArray.getBoolean(R.styleable.RangeSelectClock_show_text, true);

        lastRadians = endRadians;

        if (initPosition < startRadians)
            initPosition = calculateTextFromStartAngle(startRadians);

        // mAngle = (float) calculateAngleFromText(init_position);

        if (wheel_color_attr != null) {
            try {
                activeColor = Color.parseColor(wheel_color_attr);
            } catch (IllegalArgumentException e) {
                activeColor = Color.BLUE;
            }

        } else {
            activeColor = Color.BLUE;
        }
        if (wheel_unactive_color_attr != null) {
            try {
                unActiveColor = Color
                        .parseColor(wheel_unactive_color_attr);
            } catch (IllegalArgumentException e) {
                unActiveColor = Color.BLACK;
            }

        } else {
            unActiveColor = Color.BLACK;
        }

        if (pointer_color_attr != null) {
            try {
                mPointerColor = Color.parseColor(pointer_color_attr);
            } catch (IllegalArgumentException e) {
                mPointerColor = Color.CYAN;
            }

        } else {
            mPointerColor = Color.CYAN;
        }

        if (pointer_halo_color_attr != null) {
            try {
                colorHalo = Color.parseColor(pointer_halo_color_attr);
            } catch (IllegalArgumentException e) {
                colorHalo = Color.CYAN;
            }

        } else {
            colorHalo = Color.DKGRAY;
        }

        if (text_color_attr != null) {
            try {
                text_color = Color.parseColor(text_color_attr);
            } catch (IllegalArgumentException e) {
                text_color = Color.CYAN;
            }
        } else {
            text_color = Color.CYAN;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // All of our positions are using our internal coordinate system.
        // Instead of translating
        // them we let Canvas do the work for us.

        canvas.translate(mTranslationOffset, mTranslationOffset);

        // Draw the color wheel.
        canvas.drawArc(mClockRectangle, startRadians + 270, endRadians
                - (startRadians), false, mPaintClock);

        canvas.drawArc(mClockRectangle, startRadians + 270,
                (mArcRadians) > (endRadians) ? endRadians - (startRadians)
                        : mArcRadians - startRadians, false, mPaintArc);

        // Draw the pointer's "halo"
        canvas.drawCircle(pointerPosition[0], pointerPosition[1],
                mPointerRadius, mPaintPointer);

        // Draw the pointer (the currently selected color) slightly smaller on
        // top.
        canvas.drawCircle(pointerPosition[0], pointerPosition[1],
                (float) (mPointerRadius / 1.2), mPaintPointer);
//        textPaint.getTextBounds(text, 0, text.length(), bounds);
        // canvas.drawCircle(mColorWheelRectangle.centerX(),
        // mColorWheelRectangle.centerY(), (bounds.width() / 2) + 5,
        // mCircleTextColor);
//        if (show_text)
//            canvas.drawText(
//                    text,
//                    (mClockRectangle.centerX())
//                            - (textPaint.measureText(text) / 2),
//                    mClockRectangle.centerY() + bounds.height() / 2,
//                    textPaint);

        // last_radians = calculateRadiansFromAngle(mAngle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        mTranslationOffset = min * 0.5f;
        mClockRadius = mTranslationOffset - mPointerRadius;

        mClockRectangle.set(-mClockRadius, -mClockRadius,
                mClockRadius, mClockRadius);

        mCenterHaloRectangle.set(-mClockRadius / 2,
                -mClockRadius / 2, mClockRadius / 2,
                mClockRadius / 2);

        updatePointerPosition();

    }

    private double calculateAngleFromText(int position) {
        if (position == 0 || position >= maxNumber)
            return (float) 90;

        double f = (double) maxNumber / (double) position;

        double f_r = 360 / f;

        return f_r + 90;
    }

    private float calculateAngleFromRadians(int radians) {
        return (float) (((radians + 270) * (2 * Math.PI)) / 360);
    }



    private int calculateTextFromAngle(float angle) {
        float m = angle - startRadians;

        float f = (endRadians - startRadians) / m;

        return (int) (maxNumber / f);
    }

    private int calculateTextFromStartAngle(float angle) {
        float m = angle;

        float f = (float) ((endRadians - startRadians) / m);

        return (int) (maxNumber / f);
    }

    private void updatePointerPosition() {
        pointerPosition = calculatePointerPosition(mAngle);
    }

    private float[] calculatePointerPosition(float angle) {
        // if (calculateRadiansFromAngle(angle) > end_wheel)
        // angle = calculateAngleFromRadians(end_wheel);
        float x = (float) (mClockRadius * Math.cos(angle));
        float y = (float) (mClockRadius * Math.sin(angle));

        return new float[] { x, y };
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Convert coordinates to our internal coordinate system
        float x = event.getX() - mTranslationOffset;
        float y = event.getY() - mTranslationOffset;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Check whether the user pressed on (or near) the pointer
                mAngle = (float) Math.atan2(y, x);

                block_end = false;
                block_start = false;
                mUserIsMovingPointer = true;

                mArcRadians = calculateRadiansFromAngle(mAngle);

                if (mArcRadians > endRadians) {
                    mArcRadians = endRadians;
                    block_end = true;
                }

                if (!block_end) {
//                    setText(String.valueOf(calculateTextFromAngle(mArcRadians)));
                    updatePointerPosition();
                    invalidate();
                }
                if (mOnCircleSeekBarChangeListener != null) {
                    mOnCircleSeekBarChangeListener.onStartTrackingTouch(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mUserIsMovingPointer) {
                    mAngle = (float) Math.atan2(y, x);

                    int radians = calculateRadiansFromAngle(mAngle);

                    if (lastRadians > radians && radians < (360 / 6) && x > lastX
                            && lastRadians > (360 / 6)) {

                        if (!block_end && !block_start)
                            block_end = true;
                        // if (block_start)
                        // block_start = false;
                    } else if (lastRadians >= startRadians
                            && lastRadians <= (360 / 4) && radians <= (360 - 1)
                            && radians >= ((360 / 4) * 3) && x < lastX) {
                        if (!block_start && !block_end)
                            block_start = true;
                        // if (block_end)
                        // block_end = false;

                    } else if (radians >= endRadians && !block_start
                            && lastRadians < radians) {
                        block_end = true;
                    } else if (radians < endRadians && block_end
                            && lastRadians > endRadians) {
                        block_end = false;
                    } else if (radians < startRadians && lastRadians > radians
                            && !block_end) {
                        block_start = true;
                    } else if (block_start && lastRadians < radians
                            && radians > startRadians && radians < endRadians) {
                        block_start = false;
                    }

                    if (block_end) {
                        mArcRadians = endRadians - 1;
//                        setText(String.valueOf(maxNumber));
                        mAngle = calculateAngleFromRadians(mArcRadians);
                        updatePointerPosition();
                    } else if (block_start) {
                        mArcRadians = startRadians;
                        mAngle = calculateAngleFromRadians(mArcRadians);
//                        setText(String.valueOf(0));
                        updatePointerPosition();
                    } else {
                        mArcRadians = calculateRadiansFromAngle(mAngle);
//                        setText(String.valueOf(calculateTextFromAngle(maxNumber)));
                        updatePointerPosition();
                    }
                    invalidate();
                    if (mOnCircleSeekBarChangeListener != null)
                        mOnCircleSeekBarChangeListener.onProgressChanged(this,
                                Integer.parseInt(text), true);

                    lastRadians = radians;

                }
                break;
            case MotionEvent.ACTION_UP:
                mUserIsMovingPointer = false;
                if (mOnCircleSeekBarChangeListener != null) {
                    mOnCircleSeekBarChangeListener.onStopTrackingTouch(this);
                }
                break;
        }
        // Fix scrolling
        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        lastX = x;

        return true;
    }

    private int calculateRadiansFromAngle(float angle) {
        float unit = (float) (angle / (2 * Math.PI));
        if (unit < 0) {
            unit += 1;
        }
        int radians = (int) ((unit * 360) - ((360 / 4) * 3));
        if (radians < 0)
            radians += 360;
        return radians;
    }

    public interface OnCircleSeekBarChangeListener {

        void onProgressChanged(RangeSelectClock clock, int progress, boolean fromUser);

        void onStartTrackingTouch(RangeSelectClock clock);

        void onStopTrackingTouch(RangeSelectClock clock);

    }
}
