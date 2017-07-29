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
        ImageView ivNotCachedImage = (ImageView) view.findViewById(R.id.iv_not_cached_image);
        final TextView tvNotCachedImageStatus = (TextView) view.findViewById(R.id.tv_not_cached_image_status);
        ImageView ivCachedImage = (ImageView) view.findViewById(R.id.iv_cached_image);
        final TextView tvCachedImageStatus = (TextView) view.findViewById(R.id.tv_cached_image_status);
        ImageView ivSecondary = (ImageView) view.findViewById(R.id.iv_internal_image);

        String imageUrl = "https://ak2.picdn.net/shutterstock/videos/4055689/thumb/8.jpg";
        ImageDownloader.with(getContext())
                .fromUrl(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .setOnDownloadCompletedListener(new ImageDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(Bitmap bitmap, long duration) {
                        String imageStatus = getImageStatus(duration, false);
                        tvNotCachedImageStatus.setText(imageStatus);
                    }
                })
                .into(ivNotCachedImage);
        ImageDownloader.with(getContext())
                .fromUrl(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .setOnDownloadCompletedListener(new ImageDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(Bitmap bitmap, long duration) {
                        String imageStatus = getImageStatus(duration, false);
                        tvCachedImageStatus.setText(imageStatus);
                    }
                })
                .into(ivCachedImage);

        ImageDownloader.with(getContext())
                .fromDrawable(R.drawable.img_success)
                .into(ivSecondary);
    }

    private String getImageStatus(long duration, boolean cache) {
        StringBuilder stringBuilder = new StringBuilder();
        String durationText = StringUtil.getDurationTime(duration);
        String cacheStatus = cache ? getString(R.string.cache_true) : getString(R.string.cache_false);
        stringBuilder.append(getString(R.string.cache, cacheStatus))
                .append("\n")
                .append(getString(R.string.time, durationText));
        return stringBuilder.toString();
    }
}