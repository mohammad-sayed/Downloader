package com.mohammadsayed.mindvalley.downloader.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mohammadsayed.mindvalley.downloader.Constants;
import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.bases.BaseFragment;
import com.mohammadsayed.mindvalley.downloader.bases.BaseObserver;
import com.mohammadsayed.mindvalley.downloader.data.Photo;
import com.mohammadsayed.mindvalley.downloader.photodetails.PhotoDetailsActivity;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

/**
 * Created by mohammad on 7/28/17.
 */

public class PhotosFragment extends BaseFragment<PhotosController> implements PhotosAdapter.OnPhotosAdapterListener {

    private PhotosAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected PhotosController createController() {
        return new PhotosController(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_photos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new PhotosAdapter(getContext(), recyclerView, new ArrayList<Photo>(), this);
        recyclerView.setAdapter(mAdapter);
        //mProgressBar.setVisibility(View.VISIBLE);
        getPhotos(0);
    }

    private void getPhotos(int currentItemsSize) {
        getController().getPhotos(new BaseObserver<ArrayList<Photo>>() {
            @Override
            public void onNext(@NonNull ArrayList<Photo> photos) {
                mAdapter.addPhotos(photos);
                //mProgressBar.setVisibility(View.GONE);
            }
        }, currentItemsSize);
    }

    @Override
    public void onPhotoSelected(Photo photo) {
        Intent intent = new Intent(getContext(), PhotoDetailsActivity.class);
        intent.putExtra(Constants.Extras.KEY_PHOTO, photo);
        startActivity(intent);
    }

    @Override
    public void onLoadMoreListener(int size) {
        getPhotos(size);
    }
}
