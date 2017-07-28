package com.mohammadsayed.mindvalley.downloader.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.testimagedownloader.TestImageDownloaderActivity;
import com.mohammadsayed.mindvalley.downloader.testtextdownloader.TestTextDownloaderActivity;
import com.mohammadsayed.mindvalley.downloader.userinterface.UserInrefaceActivity;

/**
 * Created by mohammad on 7/28/17.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_app_image_downloader_test).setOnClickListener(this);
        view.findViewById(R.id.btn_app_text_downloader_test).setOnClickListener(this);
        view.findViewById(R.id.btn_app_user_interface).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_app_image_downloader_test:
                goToActivity(TestImageDownloaderActivity.class);
                break;
            case R.id.btn_app_text_downloader_test:
                goToActivity(TestTextDownloaderActivity.class);
                break;
            case R.id.btn_app_user_interface:
                goToActivity(UserInrefaceActivity.class);
                break;
        }
    }

    private void goToActivity(Class activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        startActivity(intent);
    }
}
