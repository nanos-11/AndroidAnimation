package com.animation.nan.androidanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.animation.nan.androidanimation.widget.WaveView;

public class RadarActivity extends AppCompatActivity {
    private WaveView radarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

        radarView = (WaveView) findViewById(R.id.radar_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        radarView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        radarView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        radarView.stop();
    }
}
