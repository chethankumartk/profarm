package com.example.chethan.industrain.FragmentsAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.usermainActivity;

import java.util.List;

public class searchadapter extends RecyclerView.Adapter<searchadapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;

    public searchadapter(Context context, List<Upload1> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public searchadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.searchlist, parent, false);
        return new searchadapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(searchadapter.ImageViewHolder holder, final int position) {
        final Upload1 uploadCurrent = mUploads.get(position);
        holder.textmain.setText(uploadCurrent.getCropname());
        holder.textmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,usermainActivity.class).putExtra("cropsearch",uploadCurrent.getCropname()));
            }
        });
        holder.subtext.setText(uploadCurrent.getLocality());



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textmain;
        public TextView subtext;
        public ImageViewHolder(View itemView) {
            super(itemView);
            textmain=itemView.findViewById(R.id.maintext);
            subtext=itemView.findViewById(R.id.subtext);
        }
    }
}

