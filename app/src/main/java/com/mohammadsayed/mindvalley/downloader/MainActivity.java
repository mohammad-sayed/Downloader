package com.mohammadsayed.mindvalley.downloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ivMain = (ImageView) findViewById(R.id.iv_main_photo);
        ImageView ivSecondary = (ImageView) findViewById(R.id.iv_secondary_photo);
        String url = "https://ak2.picdn.net/shutterstock/videos/4055689/thumb/8.jpg";
        ImageDownloader.with(this)
                .from(url)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivMain);

        ImageDownloader.with(this)
                .from(R.drawable.img_success)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivSecondary);
    }
}
