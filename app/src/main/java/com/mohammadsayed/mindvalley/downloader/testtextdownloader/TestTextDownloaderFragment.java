package com.mohammadsayed.mindvalley.downloader.testtextdownloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.downloader.TextDownloader;

/**
 * Created by mohammad on 7/28/17.
 */

public class TestTextDownloaderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_downloader, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvJson = (TextView) view.findViewById(R.id.tv_json_file);
        String jsonUrl = "http://pastebin.com/raw/wgkJgazE";
        TextDownloader.with(getContext()).fromUrl(jsonUrl).into(tvJson);
    }
}
