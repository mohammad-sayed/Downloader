package com.mohammadsayed.mindvalley.downloader.photodetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.mohammadsayed.mindvalley.downloader.Constants;
import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.data.Photo;
import com.mohammadsayed.mindvalley.downloader.data.PhotoUrls;
import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;
import com.mohammadsayed.mindvalley.downloader.utils.StringUtil;

public class PhotoDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.Extras.KEY_PHOTO)) {
                Photo photo = getIntent().getParcelableExtra(Constants.Extras.KEY_PHOTO);
                initializeViews(photo);
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_photo_details);
                if (fragment != null) {
                    ((PhotoDetailsFragment) fragment).setPhoto(photo);
                }
            }
        }
    }

    private void initializeViews(Photo photo) {
        getSupportActionBar().setTitle(StringUtil.getDisplayingDateFormat(photo.getCreationDate()));
        ImageView ivPhotoCover = (ImageView) findViewById(R.id.iv_photo_cover);
        String coverUrl = getCoverPhotoUrl(photo.getUrls());
        if (!StringUtil.isEmpty(coverUrl, true)) {
            ImageDownloader.with(this)
                    .fromUrl(coverUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(ivPhotoCover);
        } else {
            ImageDownloader.with(this)
                    .fromDrawable(R.drawable.image_not_available)
                    .into(ivPhotoCover);
        }
    }

    private String getCoverPhotoUrl(PhotoUrls photoUrls) {
        if (photoUrls == null) {
            return null;
        }
        String url = null;
        if (!StringUtil.isEmpty(photoUrls.getRegular(), true)) {
            url = photoUrls.getRegular();
        } else if (!StringUtil.isEmpty(photoUrls.getSmall(), true)) {
            url = photoUrls.getSmall();
        } else if (!StringUtil.isEmpty(photoUrls.getRaw(), true)) {
            url = photoUrls.getRaw();
        } else if (!StringUtil.isEmpty(photoUrls.getFull(), true)) {
            url = photoUrls.getFull();
        } else if (!StringUtil.isEmpty(photoUrls.getThumb(), true)) {
            url = photoUrls.getThumb();
        }
        return url;
    }
}
