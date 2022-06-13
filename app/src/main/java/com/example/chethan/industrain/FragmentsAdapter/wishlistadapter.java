package com.example.chethan.industrain.FragmentsAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.ViewMukyaActivity;
import com.example.chethan.industrain.fragmentclasses.WishlistAdd;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class wishlistadapter extends RecyclerView.Adapter<wishlistadapter.ImageViewHolder> {
    private Context mContext;
    private List<WishlistAdd> mUploads;

    public wishlistadapter(Context context, List<WishlistAdd> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public wishlistadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.wishlistlist, parent, false);
        return new wishlistadapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(wishlistadapter.ImageViewHolder holder, final int position) {
        final WishlistAdd uploadCurrent = mUploads.get(position);

        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imagecrop);
       holder.cropname.setText(uploadCurrent.getCropname());
        holder.price.setText(String.valueOf(uploadCurrent.getPrice()));
        holder.location.setText(uploadCurrent.getLocality());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistAdd uploadCurrent = mUploads.get(position);

                mContext.startActivity(new Intent(mContext,ViewMukyaActivity.class)
                        .putExtra("refid",uploadCurrent.getId1())
                        .putExtra("price",uploadCurrent.getPrice())
                        .putExtra("eidd",uploadCurrent.getEid())

                );

            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //String idd=uploadCurrent.getIdd();
                String eid= FirebaseAuth.getInstance().getUid();
                Query query= FirebaseFirestore.getInstance().collection("WishList").
                        whereEqualTo("id1",uploadCurrent.getId1())
                        .whereEqualTo("eid",eid);

                query.get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                        for (DocumentSnapshot document : task.getResult()) {
                                            FirebaseFirestore.getInstance().collection("WishList").document(document.getId()).delete();
                                            Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                                            mUploads.remove(position);
                                            notifyItemRemoved(position);
                                            notifyDataSetChanged();

                                        }




                                } else {


                                }
                            }
                        });

            }
        });


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagecrop;
        public TextView cropname;
        public TextView price;
        public TextView location;
        public ImageView del;
        public LinearLayout linearLayout;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imagecrop=itemView.findViewById(R.id.cropimage);
            cropname=itemView.findViewById(R.id.Cropname);
            price=itemView.findViewById(R.id.price);
            location=itemView.findViewById(R.id.Address);
            del=itemView.findViewById(R.id.deletee);
            linearLayout=itemView.findViewById(R.id.ll2);

        }
    }
}


