package com.example.chethan.industrain.newsadapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.ViewMukyaActivity;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ImageViewHolder> {
    private Context mContext;
    private List<news> mUploads;
    SharedPreferences sharedPref;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String eid,strDt;


    public NewsAdapter(Context context, List<news> uploads) {
        mContext = context;

        mUploads = uploads;
    }

    @Override
    public NewsAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.newsfeeditem, parent, false);
        return new NewsAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.ImageViewHolder holder, final int position) {
        final news uploadCurrent = mUploads.get(position);
        Date dt=uploadCurrent.getUploaddate();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yy 'at' HH:mm");

        strDt = simpleDate.format(dt);
        strDt="Date: "+strDt;
        holder.date.setText(strDt);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,NewsViewMukya.class)
                        .putExtra("fulltext",uploadCurrent.getFulltext())
                        .putExtra("imageurl",uploadCurrent.getImageurl())
                        .putExtra("maintext",uploadCurrent.getMainhead())
                        .putExtra("date",strDt)


                );

            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, uploadCurrent.getFulltext());
                sendIntent.setType("text/plain");
                mContext.startActivity(sendIntent);

            }
        });


        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imageView);
        holder.maintext.setText(uploadCurrent.getMainhead());
        holder.subtext.setText(uploadCurrent.getSubhead());


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView maintext;
        public LinearLayout linearLayout;
        public TextView subtext;
        public TextView date;
        public ImageView share;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            maintext=itemView.findViewById(R.id.maintext);
            subtext=itemView.findViewById(R.id.subtext);
            date=itemView.findViewById(R.id.date);
            share=itemView.findViewById(R.id.share);
            linearLayout=itemView.findViewById(R.id.linearlayout);
        }
    }
}

