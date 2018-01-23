package com.abner.ming.snakeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created by Administrator on 2018/1/22.
 * 贪吃蛇
 */

public class SnakeView extends View{
    private int mWidth,mHeight;

    /**
     * 点击了哪个键左上右下,遵循往左不往右，往下不往上，往上不往下，往右不往左
     * */
    private int clickKey=4;

    public SnakeView(Context context) {
        super(context);
        initView(context);
    }

    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    public SnakeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        setBackgroundColor(Color.parseColor("#222222"));

        initPaint();
    }

    /**
     * 初始化画笔
     * */
    private Paint mPaintRect,mPaintLoding,mPaintText,mPaintRectRandom,mPaintSnake;
    private void initPaint() {
        //初始化边框
        mPaintRect=new Paint();
        mPaintRect.setColor(Color.WHITE);
        mPaintRect.setStyle(Paint.Style.STROKE);
        mPaintRect.setAntiAlias(true);
        mPaintRect.setStrokeWidth(5);

        //加载框
        mPaintLoding=new Paint();
        mPaintLoding.setColor(Color.WHITE);
        mPaintLoding.setStyle(Paint.Style.FILL);
        mPaintLoding.setAntiAlias(true);
        mPaintLoding.setStrokeWidth(5);

        //加载文字
        mPaintText=new Paint();
        mPaintText.setColor(Color.parseColor("#d43c3c"));
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setAntiAlias(true);
        mPaintText.setStrokeWidth(20);
        mPaintText.setTextSize(66);

        //随机格子
        mPaintRectRandom=new Paint();
        mPaintRectRandom.setColor(Color.WHITE);
        mPaintRectRandom.setStyle(Paint.Style.FILL);
        mPaintRectRandom.setAntiAlias(true);
        mPaintRectRandom.setStrokeWidth(5);

        //贪吃蛇
        mPaintSnake=new Paint();
        mPaintSnake.setColor(Color.parseColor("#d43c3c"));
        mPaintSnake.setStyle(Paint.Style.FILL);
        mPaintSnake.setAntiAlias(true);
        mPaintSnake.setStrokeWidth(5);
    }

    private int intX,intY,intXS,intYS;
    private int  randomIntX=new Random().nextInt(50);
    private int randomIntY=new Random().nextInt(50);
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
        intX=intXS=mWidth/50;
        intY=intYS=mHeight/50;

    }

    private Canvas canvas;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas=canvas;

        onDrawRect();
        onDrawRandomRect();
        onDrawSnake();
        onDrawLoding();
    }

    private int xLeft,xRight,xTop,xBottom;//贪吃蛇的位置
    /**
     * 绘制贪吃蛇
     * */
    private int num=0;//吃了几个格子了
    private Rect rectSnake;
    private void onDrawSnake() {
        int left=intXS-intX;
        int right=intXS+intX;
        int top=intYS-intY;
        int bottom=intYS+intY;

        if(clickKey==3){
            left=left-intX*num;
        }else if(clickKey==4){
            right=right+intX*num;
        }else if(clickKey==2){
            bottom=bottom+intY*num;
        }else if(clickKey==1){
            top=top-intY*num;
        }
        Log.i("SnakeView","onDrawSnake");
        if(left<0||right>mWidth||top<0||bottom>mHeight){
            mPaintText.setColor(Color.parseColor("#d43c3c"));
            mPaintLoding.setColor(Color.WHITE);
            stopSnake();
            listener.endSnake();
            isLoding=true;
        }else{
            xLeft=left;
            xRight=right;
            xTop=top;
            xBottom=bottom;
            rectSnake=new Rect(left,top,right,bottom);
            canvas.drawRect(rectSnake,mPaintSnake);
        }
    }

    /**
     * 绘制随机的格子
     * */
    private int snakeLeft,snakeRight,snakeTop,snakeBottom;
    private Rect rectRandom;
    private void onDrawRandomRect() {
        if(randomIntX==0){
            randomIntX=1;
        }
        if(randomIntY==0){
            randomIntY=1;
        }
        int left=intX*randomIntX-intX;
        int right=intX*randomIntX+intX;
        int top=intY*randomIntY-intY;
        int bottom=intY*randomIntY+intY;
        snakeLeft=left;
        snakeRight=right;
        snakeTop=top;
        snakeBottom=bottom;
        rectRandom=new Rect(left,top,right,bottom);
        canvas.drawRect(rectRandom,mPaintRectRandom);
    }

    /**
     * 绘制失败的弹框
     * */
    private String message="呀！撞墙了！";
    private boolean isLoding;
    private void onDrawLoding() {
        if(isLoding){
            Rect rect=new Rect(0,0,mWidth,mHeight);
            canvas.drawRect(rect,mPaintLoding);
            canvas.drawText(message,mWidth/2-mPaintText.measureText(message)/2,mHeight/2,mPaintText);
        }

    }

    private void onDrawRect() {
        Rect rect=new Rect(0,0,mWidth,mHeight);
        canvas.drawRect(rect,mPaintRect);
    }

    /**
     * 开始
     * */
    public void startSnake(){
         if(isLoding){
            intX=intXS=mWidth/50;
            intY=intYS=mHeight/50;
            isLoding=false;
            clickKey=4;
        }
        mHandler.removeMessages(SNAKE);
        mHandler.sendEmptyMessage(SNAKE);
    }

    /**
     * 暂停
     * */
    public void stopSnake(){
        mHandler.removeMessages(SNAKE);
    }


    /**
     * 上
     * */
    public void snakeTop(){
        if(clickKey==2){
            return;
        }
        clickKey=1;
        intYS=intYS-intY;
    }
    /**
     * 下
     * */
    public void snakeBottom(){
        if(clickKey==1){
            return;
        }
        clickKey=2;
        intYS=intYS+intY;
    }
    /**
     * 左
     * */
    public void snakeLeft(){
        if(clickKey==4){
            return;
        }
        clickKey=3;
        intXS=intXS-intX;
    }
    /**
     * 右
     * */
    public void snakeRight(){
        if(clickKey==3){
            return;
        }
        clickKey=4;
        intXS=intXS+intX;
    }

    private final int SNAKE=1000;
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SNAKE:
                      mPaintText.setColor(Color.TRANSPARENT);
                      mPaintLoding.setColor(Color.TRANSPARENT);
                    createSnakeRect();
                    if(clickKey==1){
                        if(rectRandom.top==rectSnake.top&&rectRandom.left==rectSnake.left){
                            num++;
                            listener.success();
                        }
                    }else if(clickKey==2){
                        if(rectRandom.bottom==rectSnake.bottom&&rectRandom.left==rectSnake.left){
                            num++;
                            listener.success();
                        }
                    }else if(clickKey==3){
                        if(rectRandom.left==rectSnake.left&&rectRandom.top==rectSnake.top){
                            num++;
                            listener.success();
                        }
                    }else if(clickKey==4){
                        if(rectRandom.right==rectSnake.right&&rectRandom.top==rectSnake.top){
                            num++;
                            listener.success();
                        }
                    }
                    mHandler.sendEmptyMessageDelayed(SNAKE,500);
                    break;
            }
        }
    };

    /**
     * 回调
     * */
    private onSnakeListener listener;
    public void setOnSnakeListener(onSnakeListener listener){
        this.listener=listener;
    }

    /**
     * 重新绘制随机格子
     * */
    public void resertSnake() {
        randomIntX=new Random().nextInt(50);
        randomIntY=new Random().nextInt(50);
        invalidate();
    }

    public interface onSnakeListener{

        void endSnake();//结束

        void success();//重合了

    }

    /**
     * 左上右下
     *
     * */
    private void createSnakeRect() {
        if(clickKey==1){
            snakeTop();
        }else if(clickKey==2){
            snakeBottom();
        }else if(clickKey==3){
            snakeLeft();
        }else if(clickKey==4){
            snakeRight();
        }
        invalidate();

    }
}
