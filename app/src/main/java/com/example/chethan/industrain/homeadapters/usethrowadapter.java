package com.example.chethan.industrain.homeadapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class usethrowadapter extends RecyclerView.Adapter<usethrowadapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;

    public usethrowadapter(Context context, List<Upload1> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public usethrowadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclesmall, parent, false);
        return new usethrowadapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(usethrowadapter.ImageViewHolder holder, final int position) {
        Upload1 uploadCurrent = mUploads.get(position);
        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imageView);
        holder.cropname.setText(uploadCurrent.getCropname());
        holder.price.setText(String.valueOf(uploadCurrent.getPrice()));


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView cropname;
        public TextView price;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            cropname=itemView.findViewById(R.id.cropname);
            price=itemView.findViewById(R.id.price);
        }
    }
}
