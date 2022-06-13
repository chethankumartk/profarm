package com.example.chethan.industrain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;
    private FirebaseAuth auth;
    private FirebaseUser user;


    public ImageAdapter(Context context, List<Upload1> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Upload1 uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getPhone());
        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imageView);
        holder.farname.setText(uploadCurrent.getFarmername());
        holder.address.setText(uploadCurrent.getLocality());
        holder.pricee.setText(String.valueOf(uploadCurrent.getPrice()));
        holder.cropname.setText(uploadCurrent.getCropname());
        holder.cropme.setText(uploadCurrent.getCropname());

        holder.updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload1 upload = mUploads.get(position);


                    mContext.startActivity(new Intent(mContext,updatemadakke.class)
                            .putExtra("refid",upload.getRefid())
                            .putExtra("eid",upload.getEid())
                            .putExtra("latitude",upload.getLatitude())
                            .putExtra("longitude",upload.getLongitude())
                            .putExtra("dateadded",String.valueOf(upload.getDateadded()))
                            .putExtra("dateupdated",String.valueOf(upload.getDateupdated()))
                            .putExtra("imageurl",upload.getImageurl())
                            .putExtra("farmername",upload.getFarmername())
                            .putExtra("cropname",upload.getCropname())
                            .putExtra("category",upload.getCategory())
                            .putExtra("phone",upload.getPhone())
                            .putExtra("email",upload.getEmail())
                            .putExtra("state",upload.getState())
                            .putExtra("district",upload.getDistrict())
                            .putExtra("locality",upload.getLocality())
                            .putExtra("quantity",upload.getQuantity())
                            .putExtra("price",upload.getPrice())
                            .putExtra("address",upload.getAddress())

                    );
               // ((Activity)mContext).finish();




            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;
        public TextView farname;
        public TextView cropname;
        public TextView address;
        public TextView pricee;
        public Button updatebutton;
        public TextView cropme;

        AssetManager am = mContext.getApplicationContext().getAssets();


        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "font/%s", "Raleway-Regular.ttf"));



        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            farname=itemView.findViewById(R.id.raithaname);
            cropname=itemView.findViewById(R.id.raithacrop);
            address=itemView.findViewById(R.id.raithaaddress);
            pricee=itemView.findViewById(R.id.price);
            updatebutton=itemView.findViewById(R.id.update);
            cropme=itemView.findViewById(R.id.cropmel);


            textViewName.setTypeface(typeface);
            farname.setTypeface(typeface);
            cropme.setTypeface(typeface);
            farname.setTypeface(typeface);
            address.setTypeface(typeface);
            cropname.setTypeface(typeface);
            pricee.setTypeface(typeface);

        }
    }
    public void setFilter(List<Upload1> newlist)
    {
        mUploads =new ArrayList<>();
        mUploads.addAll(newlist);
        notifyDataSetChanged();
    }
}