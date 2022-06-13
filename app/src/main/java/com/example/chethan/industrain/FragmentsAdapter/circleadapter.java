package com.example.chethan.industrain.FragmentsAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chethan.industrain.CircleImageView;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.ViewMukyaActivity;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class circleadapter extends RecyclerView.Adapter<circleadapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;
    SharedPreferences sharedPref;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String eid;


    public circleadapter(Context context, List<Upload1> uploads) {
        mContext = context;

        mUploads = uploads;
    }

    @Override
    public circleadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.circlerecycl, parent, false);
        eid= FirebaseAuth.getInstance().getUid();
        db=FirebaseFirestore.getInstance();

        sharedPref=mContext.getSharedPreferences("myPref",MODE_PRIVATE);


        return new circleadapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final circleadapter.ImageViewHolder holder, final int position) {
        final Upload1 uploadCurrent = mUploads.get(position);
        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imageView);
        holder.cropname.setText(uploadCurrent.getCropname());
        holder.price.setText(String.valueOf("₹ "+uploadCurrent.getPrice()));



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload1 uploadCurrent = mUploads.get(position);

                mContext.startActivity(new Intent(mContext,ViewMukyaActivity.class)
                        .putExtra("refid",uploadCurrent.getRefid())
                        .putExtra("price",uploadCurrent.getPrice())

                );
                sharedPref.edit().putString("category", uploadCurrent.getCategory()).commit();

            }
        });


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView cropname;
        public TextView price;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.circlere);
            cropname=itemView.findViewById(R.id.cropcircl);
            price=itemView.findViewById(R.id.cropcostcircl);
        }
    }
}

