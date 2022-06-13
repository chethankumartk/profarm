package com.example.chethan.industrain;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.fragmentclasses.WishlistAdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class sumnecheckadapter extends RecyclerView.Adapter<sumnecheckadapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String eid;



    public sumnecheckadapter(Context context, List<Upload1> uploads) {
        db=FirebaseFirestore.getInstance();
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.userimageitem, parent, false);
        eid= FirebaseAuth.getInstance().getUid();
        return new ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        final Upload1 uploadCurrent = mUploads.get(position);


        String a=uploadCurrent.getCropname();
        //if (a.equalsIgnoreCase("mixed fruit"))
       // {

                Picasso.with(mContext)
                        .load(uploadCurrent.getImageurl())
                        .placeholder(R.drawable.gradient5)
                        .fit()
                        .into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.startActivity(new Intent(mContext,ViewMukyaActivity.class)
                                .putExtra("refid",uploadCurrent.getRefid())
                                .putExtra("price",uploadCurrent.getPrice())

                        );
                        Activity thisActivity=(Activity)mContext;


                        thisActivity.overridePendingTransition(R.anim.slidedown,R.anim.slideup);


                    }
                });
        Query query=db.collection("WishList").whereEqualTo("id1",uploadCurrent.getRefid())
                .whereEqualTo("eid",eid);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int a=task.getResult().size();
                            if(a==0)
                            {
                                holder.wishheart.setImageResource(R.drawable.wishlist);

                            }
                            else {
                                holder.wishheart.setImageResource(R.drawable.wishlistfull);
                            }


                        } else {


                        }
                    }
                });

        holder.cropname.setText(uploadCurrent.getCropname());
                holder.price.setText(String.valueOf(uploadCurrent.getPrice()));
                holder.category.setText(uploadCurrent.getCategory());
                holder.location.setText(uploadCurrent.getLocality());
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Query query1=db.collection("WishList")
                                .whereEqualTo("eid",eid);
                        query1.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            int a=task.getResult().size();
                                            if(a<15)
                                            {
                                                Query query=db.collection("WishList").whereEqualTo("id1",uploadCurrent.getRefid())
                                                        .whereEqualTo("eid",eid);

                                                query.get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    int a=task.getResult().size();
                                                                    if(a==0)
                                                                    {
                                                                        documentReference=db.collection("WishList").document();
                                                                        WishlistAdd wishlistAdd=new WishlistAdd(uploadCurrent.getRefid(),eid,uploadCurrent.getImageurl(),
                                                                                uploadCurrent.getCropname(),uploadCurrent.getLocality(),uploadCurrent.getPrice());
                                                                        documentReference.set(wishlistAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                //  searchitem search=new searchitem(mEditTextFileName.getText().toString().trim());
                                                                                //mDatabaseRef.child(uploadId).setValue(search);
                                                                                Toast.makeText(mContext, "added", Toast.LENGTH_SHORT).show();

                                                                                holder.wishheart.setImageResource(R.drawable.wishlistfull);


                                                                            }
                                                                        });

                                                                    }
                                                                    else {

                                                                        for (DocumentSnapshot document : task.getResult()) {
                                                                            db.collection("WishList").document(document.getId()).delete();
                                                                            Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();

                                                                            holder.wishheart.setImageResource(R.drawable.wishlist);

                                                                        }

                                                                    }


                                                                } else {


                                                                }
                                                            }
                                                        });

                                            }
                                            else {

                                                Toast.makeText(mContext, "Length exceeded You can add only 15 items to your wishlist:(", Toast.LENGTH_SHORT).show();
                                            }


                                        } else {

                                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();

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
        public ImageView imageView;
        public TextView cropname;
        public  TextView price;
        public TextView category;
        public TextView location;
        public LinearLayout linearLayout;
        public CardView cardView;
        public ImageView wishheart;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            cropname=itemView.findViewById(R.id.cropname);
            price=itemView.findViewById(R.id.price);
            wishheart=itemView.findViewById(R.id.wishlistheart);
            category=itemView.findViewById(R.id.category);
            location=itemView.findViewById(R.id.location);
            linearLayout=itemView.findViewById(R.id.wishlist);
            cardView=itemView.findViewById(R.id.card);

        }
    }


}
