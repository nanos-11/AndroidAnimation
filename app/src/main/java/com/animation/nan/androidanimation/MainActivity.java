package com.animation.nan.androidanimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.animation.nan.androidanimation.tagview.TagActivity;
import com.animation.nan.androidanimation.widget.AnimationButton;

/**
 * 画个圆角矩形
 * 让矩形变圆
 * 让圆上移
 * 在圆中绘制对勾
 */
public class MainActivity extends AppCompatActivity {
    private AnimationButton animationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animationButton = (AnimationButton) findViewById(R.id.animation_btn);
        animationButton.setAnimationButtonListener(new AnimationButton.AnimationButtonListener() {
            @Override
            public void onClickListener() {
                animationButton.start();
            }

            @Override
            public void animationFinish() {
                Toast.makeText(MainActivity.this, "动画执行完毕", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, RadarActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.bubbleView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BubbleViewActivity.class));
            }
        });
        findViewById(R.id.WaveByBezier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WaveByBezierActivity.class));
            }
        });
        findViewById(R.id.WaveBySinCos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WaveBySinCosActivity.class));
            }
        });
        findViewById(R.id.TagActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TagActivity.class));
            }
        });
    }
}
