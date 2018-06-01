package com.waterfairy.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.waterfairy.fileselector.ImageViewTintUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageViewTintUtils.setTint((ImageView) findViewById(R.id.image2), getResources().getColor(R.color.colorPrimary), R.mipmap.ic_audio);
    }
}
