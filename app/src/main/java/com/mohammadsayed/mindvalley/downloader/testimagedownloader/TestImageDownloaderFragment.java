package com.mohammadsayed.mindvalley.downloader.testimagedownloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;
import com.mohammadsayed.mindvalley.downloader.utils.StringUtil;

/**
 * Created by mohammad on 7/28/17.
 */

public class TestImageDownloaderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_downloader, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView ivMain = (ImageView) view.findViewById(R.id.iv_main_photo);
        final TextView tvMainTime = (TextView) view.findViewById(R.id.tv_main_photo_download_time);
        ImageView ivSecondary = (ImageView) view.findViewById(R.id.iv_secondary_photo);
        String url = "https://ak2.picdn.net/shutterstock/videos/4055689/thumb/8.jpg";
        ImageDownloader.with(getContext())
                .fromUrl(url)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .setOnDownloadCompletedListener(new ImageDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(Bitmap bitmap, long duration) {
                        String durationText = StringUtil.getDurationTime(duration);
                        tvMainTime.setText(getString(R.string.time, durationText));
                    }
                })
                .into(ivMain);

        ImageDownloader.with(getContext())
                .fromDrawable(R.drawable.img_success)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivSecondary);
    }
}
