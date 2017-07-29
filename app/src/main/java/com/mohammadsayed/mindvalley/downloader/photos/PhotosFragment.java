package com.mohammadsayed.mindvalley.downloader.photos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.bases.BaseFragment;
import com.mohammadsayed.mindvalley.downloader.bases.BaseObserver;
import com.mohammadsayed.mindvalley.downloader.data.Photo;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

/**
 * Created by mohammad on 7/28/17.
 */

public class PhotosFragment extends BaseFragment<PhotosController> implements PhotosAdapter.OnPhotosAdapterListener {

    private PhotosAdapter mAdapter;

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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_photos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new PhotosAdapter(getContext(), new ArrayList<Photo>(), this);
        recyclerView.setAdapter(mAdapter);
        getPhotos();
    }

    private void getPhotos() {
        getController().getPhotos(new BaseObserver<ArrayList<Photo>>() {
            @Override
            public void onNext(@NonNull ArrayList<Photo> photos) {
                mAdapter.setPhotos(photos);
            }
        });
    }

    @Override
    public void onPhotoSelected(Photo photo) {
        Toast.makeText(getContext(), photo.getCreationDate(), Toast.LENGTH_SHORT).show();
    }
}