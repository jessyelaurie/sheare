package com.jessyend.sheare.UserSearchView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jessyend.sheare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Track;


/**
 * The Search Adapter for the Search view.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.MyViewHolder> {

    private final List<User> mItems = new ArrayList<>();
    private final String mPlaylistId, mPlaylistName;
    private final Context mContext;
    private final ItemSelectedListener mListener;

    /**
     * Custom view holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView title;
        public final ImageView image;

        /**
         * constructor for each viewHolder
         * @param itemView the individual item
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.entity_name);
            image = (ImageView) itemView.findViewById(R.id.entity_pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(getLayoutPosition());
            mListener.onItemSelected(v, mItems.get(getAdapterPosition()));
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(View itemView, User item);
    }

    public UserSearchAdapter(Context context, String token, String playlistId, String playlistName,
                             ItemSelectedListener listener) {
        mListener = listener;
        mContext = context;
        mPlaylistId = playlistId;
        mPlaylistName = playlistName;
    }

    public void clearData() {
        mItems.clear();
    }

    public void addData(List<User> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collab_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = mItems.get(position);
        holder.title.setText(user.getSpotifyId());
        Picasso.with(mContext).load(user.getPictureUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}