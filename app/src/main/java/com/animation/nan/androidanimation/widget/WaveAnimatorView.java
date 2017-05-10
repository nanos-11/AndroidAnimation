package com.animation.nan.androidanimation.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.animation.nan.androidanimation.R;

import static android.content.ContentValues.TAG;

/**
 * Created by allen on 2016/12/13.
 * <p>
 * 波浪动画
 */

public class WaveAnimatorView extends View {

    /**
     * 上下文对象
     */
    private Context mContext;
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;

    /**
     * 波浪的画笔
     */
    private Paint mWavePaint;
    /**
     * 一个周期波浪的长度
     */
    private int mWaveLength;
    /**
     * 一个周期波浪的默认长度
     */
    private int mdefaultWaveLength = 500;

    /**
     * 屏幕宽度内波浪显示几个周期
     */
    private int mWaveCount;

    /**
     * 波纹的中间轴（基准线）
     */
    private int mCenterY;
    /**
     * 波浪的路径
     */
    Path mWavePath;

    /**
     * 平移偏移量
     */
    private int mOffset;

    /**
     * 渐变色shader
     */
    private Shader mShader;
    /**
     * 渐变色shade配置参数
     */
    private int colors[] = new int[2];

    /**
     * 渐变波开始的颜色
     */
    private int mWaveStartColor;

    /**
     * 渐变波结束的颜色
     */
    private int mWaveEndColor;

    /**
     * 颜色渐变波默认开始颜色
     */
    private int mWaveDefaultStartColor;
    /**
     * 颜色渐变波默认结束颜色
     */
    private int mWaveDefaultEndColor;

    /**
     * 振幅
     */
    private int mWaveAmplitude;

    /**
     * 水波动画延时
     */
    private int mWaveDuration;

    private boolean mWaveFillTop;

    private boolean mWaveFillBottom;


    private static final int SIN = 0;
    private static final int COS = 1;
    private static final int SIN_AND_COS = 2;

    /**
     * 水波类型 sin或者cos
     */
    private int mWaveType;

    public WaveAnimatorView(Context context) {
        this(context, null);
    }

    public WaveAnimatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mWaveDefaultStartColor = getResources().getColor(R.color.wave_start);
        mWaveDefaultEndColor = getResources().getColor(R.color.wave_end);
        getAttr(attrs);
        init();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.WaveAnimatorView);

        mWaveLength = typedArray.getDimensionPixelOffset(R.styleable.WaveAnimatorView_waveLength, dip2px(mContext, mdefaultWaveLength));
        mWaveDuration = typedArray.getInt(R.styleable.WaveAnimatorView_waveDuration, 2000);
        mWaveType = typedArray.getInt(R.styleable.WaveAnimatorView_waveType, SIN);
        mWaveAmplitude = typedArray.getDimensionPixelOffset(R.styleable.WaveAnimatorView_waveAmplitude, dip2px(mContext, 20));
        mWaveStartColor = typedArray.getColor(R.styleable.WaveAnimatorView_waveStartColor, mWaveDefaultStartColor);
        mWaveEndColor = typedArray.getColor(R.styleable.WaveAnimatorView_waveEndColor, mWaveDefaultEndColor);

        mWaveFillTop = typedArray.getBoolean(R.styleable.WaveAnimatorView_waveFillTop, true);
        mWaveFillBottom = typedArray.getBoolean(R.styleable.WaveAnimatorView_waveFillBottom, false);

        typedArray.recycle();
    }

    private void init() {
        initPaint();
        Animation();
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {

        mWavePath = new Path();

        colors[0] = mWaveStartColor;
        colors[1] = mWaveEndColor;

        mWavePaint = new Paint();

        mWavePaint.setStrokeWidth(1);
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWavePaint.setAntiAlias(true);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        mScreenWidth = w;

        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
        Log.d(TAG, "onSizeChanged: =======" + mWaveCount);
        mCenterY = mScreenHeight * 3 / 4;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mShader = new LinearGradient(0, 0, getWidth(), 0, colors, null, Shader.TileMode.CLAMP);
        mWavePaint.setShader(mShader);

        switch (mWaveType) {
            case SIN:
                drawWaveSin(canvas);
                break;
            case COS:
                drawWaveCos(canvas);
                break;
            case SIN_AND_COS:
                drawWaveSin(canvas);
                drawWaveCos(canvas);
        }
    }

    /**
     * 类似cos函数的波形
     *
     * @param canvas 画笔
     */
    private void drawWaveCos(Canvas canvas) {
        mWavePath.reset();
        mWavePath.moveTo(-mWaveLength + mOffset, mCenterY);

        for (int i = 0; i < mWaveCount; i++) {
            mWavePath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + mWaveAmplitude, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mWavePath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - mWaveAmplitude, i * mWaveLength + mOffset, mCenterY);
        }
        if (mWaveFillTop) {
            fillTop();
        }

        canvas.drawPath(mWavePath, mWavePaint);
    }

    /**
     * 类似sin函数的波形
     *
     * @param canvas 画笔
     */
    private void drawWaveSin(Canvas canvas) {
        mWavePath.reset();
        mWavePath.moveTo(-mWaveLength + mOffset, mCenterY);

        for (int i = 0; i < mWaveCount; i++) {
            mWavePath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY - mWaveAmplitude, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mWavePath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY + mWaveAmplitude, i * mWaveLength + mOffset, mCenterY);
        }
        if (mWaveFillTop) {
            fillTop();
        }

        canvas.drawPath(mWavePath, mWavePaint);
    }


    /**
     * 填充波浪上面部分
     */
    private void fillTop() {
        mWavePath.lineTo(mScreenWidth, 0);
        mWavePath.lineTo(0, 0);
        mWavePath.close();

    }

    /**
     * 填充波浪下面部分
     */
    private void fillBottom() {
        //填充矩形
        mWavePath.lineTo(mScreenWidth, mScreenHeight);
        mWavePath.lineTo(0, mScreenHeight);
        mWavePath.close();

    }

    private void Animation() {

        ValueAnimator waveAnimator = ValueAnimator.ofInt(0, mWaveLength);
        waveAnimator.setDuration(mWaveDuration);
        waveAnimator.setInterpolator(new LinearInterpolator());
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        waveAnimator.start();
    }

    /**
     * 单位转换工具类
     *
     * @param context  上下文对象
     * @param dipValue 值
     * @return 返回值
     */
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
