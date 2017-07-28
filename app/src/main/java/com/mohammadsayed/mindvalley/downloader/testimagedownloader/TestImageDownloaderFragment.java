package com.mohammadsayed.mindvalley.downloader.testimagedownloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;

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
        ImageView ivSecondary = (ImageView) view.findViewById(R.id.iv_secondary_photo);
        String url = "https://ak2.picdn.net/shutterstock/videos/4055689/thumb/8.jpg";
        ImageDownloader.with(getContext())
                .from(url)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivMain);

        ImageDownloader.with(getContext())
                .from(R.drawable.img_success)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivSecondary);
    }
}
