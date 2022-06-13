package com.example.chethan.industrain.homeadapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.ViewMukyaActivity;
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

import static android.content.Context.MODE_PRIVATE;

public class fruitsadapter extends RecyclerView.Adapter<fruitsadapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;
    SharedPreferences sharedPref;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String eid;


    public fruitsadapter(Context context, List<Upload1> uploads) {
        mContext = context;

        mUploads = uploads;
    }

    @Override
    public fruitsadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclfruits, parent, false);
        eid= FirebaseAuth.getInstance().getUid();
        db=FirebaseFirestore.getInstance();

        sharedPref=mContext.getSharedPreferences("myPref",MODE_PRIVATE);


        return new fruitsadapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final fruitsadapter.ImageViewHolder holder, final int position) {
       final Upload1 uploadCurrent = mUploads.get(position);
        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imageView);
        holder.cropname.setText(uploadCurrent.getCropname());
        holder.price.setText(String.valueOf(uploadCurrent.getPrice()));
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

                            }
                            else {

                                holder.wishlist.setImageResource(R.drawable.wishlistfull);

                            }


                        } else {


                        }
                    }
                });


        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show();
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

                                                                        holder.wishlist.setImageResource(R.drawable.wishlistfull);


                                                                    }
                                                                });

                                                            }
                                                            else {

                                                                for (DocumentSnapshot document : task.getResult()) {
                                                                    db.collection("WishList").document(document.getId()).delete();
                                                                    Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();

                                                                    holder.wishlist.setImageResource(R.drawable.wishlistwhite);

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
        public ImageView imageView;
        public TextView cropname;
        public TextView price;
        public ImageView wishlist;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            cropname=itemView.findViewById(R.id.cropname);
            price=itemView.findViewById(R.id.price);
            wishlist=itemView.findViewById(R.id.wishlistt);
        }
    }
}
