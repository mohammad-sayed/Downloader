package com.mohammadsayed.mindvalley.downloader.photos;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mohammadsayed.mindvalley.downloader.Constants;
import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.data.Photo;
import com.mohammadsayed.mindvalley.downloader.data.PhotoUrls;
import com.mohammadsayed.mindvalley.downloader.data.User;
import com.mohammadsayed.mindvalley.downloader.downloader.ImageDownloader;
import com.mohammadsayed.mindvalley.downloader.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by mohammad on 7/29/17.
 */

public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context mContext;
    private ArrayList<Photo> mPhotos;
    private OnPhotosAdapterListener mOnPhotosAdapterListener;
    private int visibleThreshold = Constants.PHOTOS_LOAD_MORE_ITEMS_SIZE;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    public PhotosAdapter(Context context, RecyclerView recyclerView, ArrayList<Photo> photos, final OnPhotosAdapterListener onPhotosAdapterListener) {
        this.mContext = context;
        this.mPhotos = photos;
        this.mOnPhotosAdapterListener = onPhotosAdapterListener;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount != 0 && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    isLoading = true;
                    mPhotos.add(null);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemInserted(mPhotos.size() - 1);
                        }
                    });
                    if (onPhotosAdapterListener != null) {
                        onPhotosAdapterListener.onLoadMoreListener(totalItemCount);
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mPhotos.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_photo, parent, false);
                return new PhotoViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_load_more, parent, false);
                return new LoadingViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoViewHolder) {
            PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
            final Photo photo = mPhotos.get(holder.getAdapterPosition());
            PhotoUrls photoUrls = photo.getUrls();
            if (photoUrls != null && !StringUtil.isEmpty(photoUrls.getSmall(), true)) {
                ImageDownloader.with(mContext)
                        .fromUrl(photoUrls.getSmall())
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(photoViewHolder.mIvPhoto);
            } else {
                ImageDownloader.with(mContext)
                        .fromDrawable(R.drawable.image_not_available)
                        .into(photoViewHolder.mIvPhoto);
            }

            photoViewHolder.mTvPhotoDate.setText(StringUtil.getDisplayingDateFormat(photo.getCreationDate()));

            User user = photo.getUser();
            if (user != null) {
                photoViewHolder.mTvUsername.setText(photo.getUser().getName());
            } else {
                photoViewHolder.mTvUsername.setText("");
            }

            photoViewHolder.mTvLikes.setText(String.valueOf(photo.getLikes()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPhotosAdapterListener != null) {
                        mOnPhotosAdapterListener.onPhotoSelected(photo);
                    }
                }
            });
        }
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
        stopLoading();
        if (!photos.isEmpty()) {
            int startAddingIndex = getItemCount();
            mPhotos.addAll(photos);
            notifyItemRangeInserted(startAddingIndex, photos.size());
        }
    }

    private void stopLoading() {
        isLoading = false;
        if (getItemCount() > 0 && mPhotos.get(getItemCount() - 1) == null) {
            mPhotos.remove(getItemCount() - 1);
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pbLoading;

        public LoadingViewHolder(View view) {
            super(view);
            pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading_more);
        }
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

        void onLoadMoreListener(int size);
    }
}
