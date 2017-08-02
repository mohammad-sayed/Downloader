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

public class TestImageDownloaderFragment extends Fragment implements View.OnClickListener {

    private TextView mBtnRefresh;
    private ImageView ivNotCachedImage;
    private TextView tvNotCachedImageStatus;
    private ImageView ivCachedImage;
    private TextView tvCachedImageStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_downloader, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView ivCancelImage1 = (ImageView) view.findViewById(R.id.iv_cancel_image_1);
        ImageView ivCancelImage2 = (ImageView) view.findViewById(R.id.iv_cancel_image_2);
        ImageView ivCancelImage3 = (ImageView) view.findViewById(R.id.iv_cancel_image_3);
        ivNotCachedImage = (ImageView) view.findViewById(R.id.iv_not_cached_image);
        tvNotCachedImageStatus = (TextView) view.findViewById(R.id.tv_not_cached_image_status);
        ivCachedImage = (ImageView) view.findViewById(R.id.iv_cached_image);
        tvCachedImageStatus = (TextView) view.findViewById(R.id.tv_cached_image_status);
        ImageView ivSecondary = (ImageView) view.findViewById(R.id.iv_internal_image);
        mBtnRefresh = (TextView) view.findViewById(R.id.btn_refresh);
        mBtnRefresh.setOnClickListener(this);

        String imageUrl = "http://www.laguiaclub.com/res/archivos/14734338611880495740.jpg";
        ImageDownloader imageDownloader = ImageDownloader.with(getContext());
        imageDownloader.fromUrl(imageUrl)
                .setCacheEnabled(false)
                .setGetCached(false)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivCancelImage1);
        setOnCancellationListener(ivCancelImage1, imageDownloader);

        imageDownloader = ImageDownloader.with(getContext());
        imageDownloader.fromUrl(imageUrl)
                .setCacheEnabled(false)
                .setGetCached(false)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivCancelImage2);
        setOnCancellationListener(ivCancelImage2, imageDownloader);

        imageDownloader = ImageDownloader.with(getContext());
        imageDownloader.fromUrl(imageUrl)
                .setCacheEnabled(false)
                .setGetCached(false)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivCancelImage3);
        setOnCancellationListener(ivCancelImage3, imageDownloader);

        loadImages();

        ImageDownloader.with(getContext())
                .fromDrawable(R.drawable.img_success)
                .into(ivSecondary);
    }

    private void setOnCancellationListener(final ImageView imageView, final ImageDownloader imageDownloader) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDownloader.cancel();
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                loadImages();
                break;
        }
    }

    private void loadImages() {
        String imageUrl = "https://ak2.picdn.net/shutterstock/videos/4055689/thumb/8.jpg";
        ImageDownloader.with(getContext())
                .fromUrl(imageUrl)
                .setCacheEnabled(false)
                .setGetCached(false)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .setOnDownloadCompletedListener(new ImageDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(Bitmap bitmap, long duration, boolean cached) {
                        String imageStatus = getImageStatus(duration, cached);
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
                    public void onComplete(Bitmap bitmap, long duration, boolean cached) {
                        String imageStatus = getImageStatus(duration, cached);
                        tvCachedImageStatus.setText(imageStatus);
                        mBtnRefresh.setVisibility(!cached ? View.VISIBLE : View.GONE);
                    }
                })
                .into(ivCachedImage);

    }
}
