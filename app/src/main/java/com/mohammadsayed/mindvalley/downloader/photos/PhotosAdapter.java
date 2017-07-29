package com.mohammadsayed.mindvalley.downloader.photos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.data.Photo;
import com.mohammadsayed.mindvalley.downloader.data.PhotoUrls;
import com.mohammadsayed.mindvalley.downloader.data.User;
import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohammad on 7/29/17.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Context mContext;
    private ArrayList<Photo> mPhotos;
    private SimpleDateFormat mSimpleDateFormat;
    private SimpleDateFormat mDisplaySimpleDateFormat;
    private OnPhotosAdapterListener mOnPhotosAdapterListener;

    public PhotosAdapter(Context context, ArrayList<Photo> photos, OnPhotosAdapterListener onPhotosAdapterListener) {
        this.mContext = context;
        this.mPhotos = photos;
        this.mOnPhotosAdapterListener = onPhotosAdapterListener;
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        mDisplaySimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        final Photo photo = mPhotos.get(position);

        PhotoUrls photoUrls = photo.getUrls();
        if (photoUrls != null) {
            ImageDownloader.with(mContext)
                    .fromUrl(photoUrls.getSmall())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(holder.mIvPhoto);
        } else {
            ImageDownloader.with(mContext)
                    .fromDrawable(R.drawable.image_not_available)
                    .into(holder.mIvPhoto);
        }

        try {
            Date date = mSimpleDateFormat.parse(photo.getCreationDate());
            holder.mTvPhotoDate.setText(mDisplaySimpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.mTvPhotoDate.setText("");
        }

        User user = photo.getUser();
        if (user != null) {
            holder.mTvUsername.setText(photo.getUser().getName());
        } else {
            holder.mTvUsername.setText("");
        }

        holder.mTvLikes.setText(String.valueOf(photo.getLikes()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPhotosAdapterListener != null) {
                    mOnPhotosAdapterListener.onPhotoSelected(photo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mPhotos == null) {
            return 0;
        }
        return mPhotos.size();
    }

    public void setPhotos(ArrayList<Photo> photos) {
        mPhotos.clear();
        this.mPhotos = photos;
        notifyDataSetChanged();
    }

    public void addPhotos(ArrayList<Photo> photos) {
        mPhotos.addAll(photos);
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvPhoto;
        public TextView mTvPhotoDate;
        public TextView mTvUsername;
        public TextView mTvLikes;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mTvPhotoDate = (TextView) itemView.findViewById(R.id.tv_photo_date);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_user_name);
            mTvLikes = (TextView) itemView.findViewById(R.id.tv_likes_number);
        }
    }

    public interface OnPhotosAdapterListener {
        void onPhotoSelected(Photo photo);
    }
}
