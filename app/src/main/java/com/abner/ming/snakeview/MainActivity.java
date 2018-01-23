package com.abner.ming.snakeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 贪吃蛇
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private  SnakeView mSnakeView;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSnakeView=(SnakeView) findViewById(R.id.snakeview);
        mBtnStart=(Button)findViewById(R.id.btn_start);
        findViewById(R.id.btn_top).setOnClickListener(this);
        findViewById(R.id.btn_bottom).setOnClickListener(this);
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mSnakeView.setOnSnakeListener(new SnakeView.onSnakeListener() {
            @Override
            public void endSnake() {
                isStart=true;
                mBtnStart.setText("重新开始");
            }

            @Override
            public void success() {
                mSnakeView.resertSnake();
            }
        });
    }

    private boolean isStart=true;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start://开始，暂停
                if(isStart){
                    mBtnStart.setText("暂停");
                    mSnakeView.startSnake();
                    isStart=false;
                }else{
                    mBtnStart.setText("开始");
                    mSnakeView.stopSnake();
                    isStart=true;
                }
                break;
            case R.id.btn_top://上
                if(isStart){
                    return;
                }
                mSnakeView.snakeTop();
                break;
            case R.id.btn_bottom://下
                if(isStart){
                    return;
                }
                mSnakeView.snakeBottom();
                break;
            case R.id.btn_left://左
                if(isStart){
                    return;
                }
                mSnakeView.snakeLeft();
                break;
            case R.id.btn_right://右
                if(isStart){
                    return;
                }
                mSnakeView.snakeRight();
                break;
        }
    }

}
