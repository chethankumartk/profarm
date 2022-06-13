package com.example.chethan.industrain.CropGuidelines;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ImageViewHolder> {
    private Context mContext;
    private List<crop> mUploads;
    SharedPreferences sharedPref;


    public GuideAdapter(Context context, List<crop> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public GuideAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.guidelinerecycl, parent, false);
        return new GuideAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GuideAdapter.ImageViewHolder holder, final int position) {
        final crop uploadCurrent = mUploads.get(position);

        Picasso.with(mContext)
                .load(uploadCurrent.getIu())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imagecrop);

        holder.cropname.setText(uploadCurrent.getNa());
        if(uploadCurrent.getSa() == 1)
        {
            holder.star.setVisibility(View.VISIBLE);

        }
       /* holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop uploadCurrent = mUploads.get(position);
                sharedPref=mContext.getSharedPreferences("myPref",MODE_PRIVATE);

                sharedPref.edit().putString("re", uploadCurrent.getRe()).commit();
                mContext.startActivity(new Intent(mContext,GuidelineActivity.class)
                .putExtra("refid",uploadCurrent.getRe())
                .putExtra("name",uploadCurrent.getNa()));

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagecrop;
        public TextView cropname;
        public FrameLayout star;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imagecrop=itemView.findViewById(R.id.cropimage);
            cropname=itemView.findViewById(R.id.Cropname);
            star=itemView.findViewById(R.id.star);

        }
    }
}


