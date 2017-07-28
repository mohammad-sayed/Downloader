package com.mohammadsayed.mindvalley.downloader;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;
import com.mohammadsayed.mindvalley.downloader.downloader.TextDownloader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ivMain = (ImageView) findViewById(R.id.iv_main_photo);
        ImageView ivSecondary = (ImageView) findViewById(R.id.iv_secondary_photo);
        TextView tvJson = (TextView) findViewById(R.id.tv_json_file);
        String url = "https://ak2.picdn.net/shutterstock/videos/4055689/thumb/8.jpg";
        ImageDownloader.with(this)
                .fromUrl(url)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .setOnDownloadCompletedListener(new ImageDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(Bitmap bitmap, long duration) {
                        Toast.makeText(MainActivity.this, "Duration: " + duration + " milliseconds", Toast.LENGTH_SHORT).show();
                    }
                }).into(ivMain);

        String jsonUrl = "http://pastebin.com/raw/wgkJgazE";
        TextDownloader.with(this)
                .fromUrl(jsonUrl)
                .setOnDownloadCompletedListener(new TextDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(String string, long duration) {
                        Toast.makeText(MainActivity.this, "Duration: " + duration + " milliseconds", Toast.LENGTH_SHORT).show();
                    }
                })
                .into(tvJson);

        ImageDownloader.with(this)
                .fromDrawable(R.drawable.img_success)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivSecondary);
    }
}
