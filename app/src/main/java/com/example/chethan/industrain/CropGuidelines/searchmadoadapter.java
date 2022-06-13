package com.example.chethan.industrain.CropGuidelines;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chethan.industrain.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class searchmadoadapter extends RecyclerView.Adapter<searchmadoadapter.ImageViewHolder> {
    private Context mContext;
    private List<crop> mUploads;
    SharedPreferences sharedPref;

    public searchmadoadapter(Context context, List<crop> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public searchmadoadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.saerchcrop, parent, false);
        return new searchmadoadapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(searchmadoadapter.ImageViewHolder holder, final int position) {
        final crop uploadCurrent = mUploads.get(position);
        holder.textmain.setText(uploadCurrent.getNa());
        holder.textmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  mContext.startActivity(new Intent(mContext,usermainActivity.class).putExtra("cropsearch",uploadCurrent.getCropname()));
            }
        });
        //holder.subtext.setText(uploadCurrent.getLocality());

        holder.subtext.setText(uploadCurrent.getSe());
        holder.textmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref=mContext.getSharedPreferences("myPref",MODE_PRIVATE);

                sharedPref.edit().putString("re", uploadCurrent.getRe()).commit();
                mContext.startActivity(new Intent(mContext,GuidelineActivity.class)
                        .putExtra("refid",uploadCurrent.getRe())
                        .putExtra("name",uploadCurrent.getNa()));

            }
        });


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

