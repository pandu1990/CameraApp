package com.applicationcourse.mobile.assignment3_1002814653;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applicationcourse.mobile.assignment3_1002814653.camera2basic.R;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ArrayList<ImageItem> mDataset;
    private Activity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView image;
        private TextView title;
        public ViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.info_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(Activity myActivity, ArrayList<ImageItem> myDataset) {
        mActivity = myActivity;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ImageItem item = mDataset.get(position);

        holder.image.setImageBitmap(item.getImage());
        holder.title.setText(item.getTitle());

        holder.image.setOnClickListener(clickListener);
        holder.title.setOnClickListener(clickListener);
        holder.image.setTag(holder);
        holder.title.setTag(holder);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CardAdapter.ViewHolder holder = (CardAdapter.ViewHolder) view.getTag();
            int position = holder.getPosition();

            ImageItem item = mDataset.get(position);
            Intent intent = new Intent(mActivity, DetailsActivity.class);
            intent.putExtra("location", item.getTitle());
            intent.putExtra("path", item.getPath());

            //Start details activity
            mActivity.startActivity(intent);

        }
    };

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (null != mDataset ? mDataset.size() : 0);
    }
}