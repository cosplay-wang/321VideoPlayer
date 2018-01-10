package com.demo.duke.videoplayer.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.demo.duke.videoplayer.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * Created by zhiwei.wang on 2018/1/9.
 * wechat 760560322
 * 作用：
 */

public class MusicPlayerView extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final float DISC_ROTATION_INCREASE = 0.5f;
    private static final long TIME_UPDATE = 50L;
    Drawable topLine, coverBorder;
    Bitmap coverDisBitmap, needleBitmap ,coverBitmap;
    int topLineHeight, coverBorderWidth;
    Matrix coverDisMatrix = new Matrix();
    Matrix coverMatrix = new Matrix();
    Matrix needleMatrix = new Matrix();
    private Point mDiscPoint = new Point();
    private Point coverPoint = new Point();
    private Point needlePoint = new Point();
    private float discRotation = 0f;
    private float needleRotationPlay = 0f;
    private float needleRotationPause = -25f;
    private PointF coverDisCenterPoint = new PointF();
    private PointF needleCenterPoint = new PointF();
    ValueAnimator playAnimator,pauseAnimator;
    private float needleRotation = 0f;

    public MusicPlayerView(Context context) {
        this(context, null);
    }

    public MusicPlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MusicPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    void initView() {
        needleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_needle);
        needleBitmap = ImageUtils.resizeImage(needleBitmap, (int) (getScreenWidth() * 0.25),
                (int) (getScreenWidth() * 0.375));
        topLine = getResources().getDrawable(R.drawable.music_top_line);
        topLineHeight = dp2px(1);
        coverDisBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_disc);
        coverDisBitmap = ImageUtils.resizeImage(coverDisBitmap, (int) (getScreenWidth() * 0.75),
                (int) (getScreenWidth() * 0.75));
        coverBorder = getResources().getDrawable(R.drawable.music_cover_border);
        coverBorderWidth = dp2px(1);
        coverBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.play_page_default_cover);
        coverBitmap = ImageUtils.resizeImage(coverBitmap, getScreenWidth() / 2, getScreenWidth()/ 2);
        coverBitmap = ImageUtils.createCircleImage(coverBitmap);
        playAnimator = ValueAnimator.ofFloat(needleRotationPause,needleRotationPlay);

        pauseAnimator = ValueAnimator.ofFloat(needleRotationPlay,needleRotationPause);
        playAnimator.addUpdateListener(this);
        playAnimator.setDuration(300);
        pauseAnimator.addUpdateListener(this);
        pauseAnimator.setDuration(300);
    }
    public void playMusic(){
        playAnimator.start();
        isPlaying = true;
        handler.post(rotationRunnable);
    }
    public void pauseMusic(){
        pauseAnimator.start();
        isPlaying = false;
        handler.removeCallbacks(rotationRunnable);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int discOffsetY = needleBitmap.getHeight() / 2;
        mDiscPoint.x = (getWidth() - coverDisBitmap.getWidth()) / 2;
        mDiscPoint.y = discOffsetY;
        //得到黑胶唱片的中点，用于旋转
        coverDisCenterPoint.x = getWidth() / 2;
        coverDisCenterPoint.y = coverDisBitmap.getHeight() / 2 + discOffsetY;

        coverPoint.x = (getWidth()- coverBitmap.getWidth())/2;
        coverPoint.y =  discOffsetY + (coverDisBitmap.getHeight() - coverBitmap.getHeight())/2;

        needlePoint.x = getWidth()/2  - needleBitmap.getWidth()/6;
      //  needlePoint.x = getWidth()/2;
        needlePoint.y =  - needleBitmap.getWidth()/6;

        needleCenterPoint.x =  coverDisCenterPoint.x;
        needleCenterPoint.y = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1.画top的半透明的线
        topLine.setBounds(0, 0, getWidth(), topLineHeight);
        topLine.draw(canvas);
        // 2.画黑胶唱片的透明边
        coverBorder.setBounds(mDiscPoint.x - coverBorderWidth, mDiscPoint.y - coverBorderWidth,
                mDiscPoint.x + coverDisBitmap.getWidth() + coverBorderWidth, mDiscPoint.y + coverDisBitmap.getHeight() + coverBorderWidth);
        coverBorder.draw(canvas);
        //3. 画黑胶唱片
        // 设置旋转中心和旋转角度，setRotate和preTranslate顺序很重要
        coverDisMatrix.setRotate(discRotation, coverDisCenterPoint.x, coverDisCenterPoint.y);
        // 设置图片起始坐标
        coverDisMatrix.preTranslate(mDiscPoint.x, mDiscPoint.y);
        canvas.drawBitmap(coverDisBitmap, coverDisMatrix, null);
        //4 .画封面
        coverMatrix.setRotate(discRotation, coverDisCenterPoint.x, coverDisCenterPoint.y);
        // 设置图片起始坐标
        coverMatrix.preTranslate(coverPoint.x, coverPoint.y);
        canvas.drawBitmap(coverBitmap, coverMatrix, null);


        // 5.绘制指针
        needleMatrix.setRotate(needleRotation, needleCenterPoint.x, needleCenterPoint.y);
        needleMatrix.preTranslate(needlePoint.x, needlePoint.y);
        canvas.drawBitmap(needleBitmap, needleMatrix, null);


    }
    public void setCover(String path){
        if(path != null &&  !path.equals("")){
            Picasso.with(getContext())
                    .load(path)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            coverBitmap = ImageUtils.resizeImage(bitmap, getScreenWidth() / 2, getScreenWidth()/ 2);
                            coverBitmap = ImageUtils.createCircleImage(coverBitmap);
                            postInvalidate();
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

        }else{

        }
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        needleRotation = (float) animation.getAnimatedValue();
        Log.e("needleRotation",needleRotation+"---");
        invalidate();
    }

    private Handler handler = new Handler();
    private boolean isPlaying = true;
    public boolean isPlaying(){
        return isPlaying;
    }
    private Runnable rotationRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                discRotation += DISC_ROTATION_INCREASE;
                if (discRotation >= 360) {
                    discRotation = 0;
                }
                invalidate();
            }
            handler.postDelayed(this, TIME_UPDATE);
        }
    };
}
