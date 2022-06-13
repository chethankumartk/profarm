package com.example.chethan.industrain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chethan.industrain.ViewMadiroList.viewers;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class mostpopularadapter extends RecyclerView.Adapter<mostpopularadapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;
    FirebaseFirestore db;
    String eid;



    public mostpopularadapter(Context context, List<Upload1> uploads) {
        db=FirebaseFirestore.getInstance();
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public mostpopularadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.mostpopularrecycl, parent, false);
        eid= FirebaseAuth.getInstance().getUid();
        return new mostpopularadapter.ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final mostpopularadapter.ImageViewHolder holder, final int position) {
        final Upload1 uploadCurrent = mUploads.get(position);


        String a=uploadCurrent.getCropname();
        //if (a.equalsIgnoreCase("mixed fruit"))
        // {

        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imageView);

        holder.cropname.setText(uploadCurrent.getCropname());
        holder.price.setText("Rs./ "+String.valueOf(uploadCurrent.getPrice()));
        holder.quantity.setText(String.valueOf(uploadCurrent.getQuantity())+" Kg");

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView cropname;
        public  TextView price;
        public TextView quantity;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            cropname=itemView.findViewById(R.id.cropname);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);

        }
    }


}
