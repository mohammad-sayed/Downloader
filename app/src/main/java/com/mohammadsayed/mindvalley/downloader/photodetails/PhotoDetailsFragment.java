package com.mohammadsayed.mindvalley.downloader.photodetails;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.data.Photo;
import com.mohammadsayed.mindvalley.downloader.data.PhotoUrls;
import com.mohammadsayed.mindvalley.downloader.data.ProfileImage;
import com.mohammadsayed.mindvalley.downloader.data.User;
import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;
import com.mohammadsayed.mindvalley.downloader.utils.StringUtil;

/**
 * Created by mohammad on 7/29/17.
 */

public class PhotoDetailsFragment extends Fragment implements View.OnClickListener {

    private TextView mTvLikesNumber;
    private ImageView mIvUserPhoto;
    private TextView mTvUserName;
    private TextView mTvRawUrl;
    private TextView mTvFullUrl;
    private TextView mTvRegularUrl;
    private TextView mTvSmallUrl;
    private TextView mTvThumbUrl;

    private Photo mPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvLikesNumber = (TextView) view.findViewById(R.id.tv_likes_number);
        mIvUserPhoto = (ImageView) view.findViewById(R.id.iv_user_photo);
        mTvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        mTvRawUrl = (TextView) view.findViewById(R.id.tv_photo_raw_url);
        mTvFullUrl = (TextView) view.findViewById(R.id.tv_photo_full_url);
        mTvRegularUrl = (TextView) view.findViewById(R.id.tv_photo_regular_url);
        mTvSmallUrl = (TextView) view.findViewById(R.id.tv_photo_small_url);
        mTvThumbUrl = (TextView) view.findViewById(R.id.tv_photo_thumb_url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_photo_raw_url:
                openUrl(mPhoto.getUrls().getRaw());
                break;
            case R.id.tv_photo_full_url:
                openUrl(mPhoto.getUrls().getFull());
                break;
            case R.id.tv_photo_regular_url:
                openUrl(mPhoto.getUrls().getRegular());
                break;
            case R.id.tv_photo_small_url:
                openUrl(mPhoto.getUrls().getSmall());
                break;
            case R.id.tv_photo_thumb_url:
                openUrl(mPhoto.getUrls().getThumb());
                break;
        }
    }

    public void setPhoto(Photo photo) {
        this.mPhoto = photo;
        mTvLikesNumber.setText(String.valueOf(photo.getLikes()));

        User user = photo.getUser();
        if (user != null) {
            if (!StringUtil.isEmpty(user.getUserName(), true)) {
                mTvUserName.setText(user.getUserName());
            } else if (!StringUtil.isEmpty(user.getName(), true)) {
                mTvUserName.setText(user.getName());
            } else {
                mTvUserName.setText(R.string.unknown);
            }

            ProfileImage profileImage = user.getProfileImage();
            if (profileImage != null && !StringUtil.isEmpty(profileImage.getSmall(), true)) {
                ImageDownloader.with(getContext())
                        .fromUrl(user.getProfileImage().getSmall())
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(mIvUserPhoto);
            } else {
                ImageDownloader.with(getContext())
                        .fromDrawable(R.drawable.image_not_available)
                        .into(mIvUserPhoto);
            }
        }

        PhotoUrls photoUrls = photo.getUrls();
        if (photoUrls != null) {
            setUrlIfAvailable(mTvRawUrl, photoUrls.getRaw());
            setUrlIfAvailable(mTvFullUrl, photoUrls.getRaw());
            setUrlIfAvailable(mTvRegularUrl, photoUrls.getRaw());
            setUrlIfAvailable(mTvSmallUrl, photoUrls.getRaw());
            setUrlIfAvailable(mTvThumbUrl, photoUrls.getRaw());
        }
    }

    private void setUrlIfAvailable(TextView textView, String url) {
        if (!StringUtil.isEmpty(url, true)) {
            textView.setText(url);
            textView.setOnClickListener(this);
        } else {
            textView.setText(R.string.url_not_available);
            textView.setOnClickListener(null);
        }
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            if (getView() != null) {
                Snackbar.make(getView(), R.string.no_available_browser, Snackbar.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), R.string.no_available_browser, Toast.LENGTH_LONG).show();
            }
        }
    }
}
