package com.jessyend.sheare.PlaylistView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jessyend.sheare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Track;


/**
 * The Collaborators Adapter for the collaborators fragment recycler view.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class CollaboratorsAdapter extends RecyclerView.Adapter<CollaboratorsAdapter.MyViewHolder>  {

    private final List<Track> mItems = new ArrayList<>();
    private final String mPlaylistId;
    private final Context mContext;
    List<Map<String, String>> mData;

    /**
     * Custom view holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(View itemView, Track item);
    }

    /**
     * Adapter's constructor
     * @param context the fragment's context
     * @param playlistId the Spotify playlist id
     * @param data data to display on the list
     */
    public CollaboratorsAdapter(Context context, String playlistId,
                         List<Map<String, String>> data)
    {
        mContext = context;
        mPlaylistId = playlistId;
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collab_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(mData.get(position).get("name"));
        Picasso.with(mContext).load(mData.get(position).get("picUrl")).into(holder.image);
    }

    /**
     * @return the recyclerView's size
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Updates the list
     * @param data
     */
    public void swap(List<Map<String, String>> data)
    {
        mData = data;
        notifyDataSetChanged();
    }
}
