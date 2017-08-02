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
import com.mohammadsayed.mindvalley.downloader.utils.StringUtil;

/**
 * Created by mohammad on 7/28/17.
 */

public class TestTextDownloaderFragment extends Fragment implements View.OnClickListener {

    private TextView tvNotCachedJson;
    private TextView tvNotCachedFileStatus;
    private TextView tvCachedJson;
    private TextView tvCachedFileStatus;
    private TextView mBtnRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_downloader, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNotCachedJson = (TextView) view.findViewById(R.id.tv_not_cached_json_file);
        tvNotCachedFileStatus = (TextView) view.findViewById(R.id.tv_not_cached_json_file_status);
        tvCachedJson = (TextView) view.findViewById(R.id.tv_cached_json_file);
        tvCachedFileStatus = (TextView) view.findViewById(R.id.tv_cached_json_file_status);
        mBtnRefresh = (TextView) view.findViewById(R.id.btn_refresh);
        mBtnRefresh.setOnClickListener(this);

        loadJsons();
    }

    private String getTextStatus(long duration, boolean cache) {
        StringBuilder stringBuilder = new StringBuilder();
        String durationText = StringUtil.getDurationTime(duration);
        String cacheStatus = cache ? getString(R.string.cache_true) : getString(R.string.cache_false);
        stringBuilder.append(getString(R.string.cache, cacheStatus))
                .append("\n")
                .append(getString(R.string.time, durationText));
        return stringBuilder.toString();
    }

    private void loadJsons() {
        String jsonUrl = "http://pastebin.com/raw/wgkJgazE";
        TextDownloader.with(getContext())
                .fromUrl(jsonUrl)
                .setGetCached(false)
                .setCacheEnabled(false)
                .setOnDownloadCompletedListener(new TextDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(String text, long duration, boolean cached) {
                        String textStatus = getTextStatus(duration, cached);
                        tvNotCachedFileStatus.setText(textStatus);
                    }
                })
                .into(tvNotCachedJson);

        TextDownloader.with(getContext())
                .fromUrl(jsonUrl)
                .setOnDownloadCompletedListener(new TextDownloader.OnDownloadCompletedListener() {
                    @Override
                    public void onComplete(String text, long duration, boolean cached) {
                        String textStatus = getTextStatus(duration, cached);
                        tvCachedFileStatus.setText(textStatus);
                        mBtnRefresh.setVisibility(!cached ? View.VISIBLE : View.GONE);
                    }
                })
                .into(tvCachedJson);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                loadJsons();
                break;
        }
    }
}
