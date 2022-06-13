package com.example.chethan.industrain.ViewMadiroList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;

public class Viewmadirolistadapter extends RecyclerView.Adapter<Viewmadirolistadapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String eid;



    public Viewmadirolistadapter(Context context, List<Upload1> uploads) {
        db=FirebaseFirestore.getInstance();
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public Viewmadirolistadapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.viewercropslist, parent, false);
        eid= FirebaseAuth.getInstance().getUid();
        return new Viewmadirolistadapter.ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final Viewmadirolistadapter.ImageViewHolder holder, final int position) {
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
        holder.viewer.setText(" "+String.valueOf(uploadCurrent.getViews())+" Viewers");

        holder.viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,viewers.class).putExtra("refid",uploadCurrent.getRefid()));
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
        public TextView quantity;
        public Button viewer;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            cropname=itemView.findViewById(R.id.cropname);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            viewer=itemView.findViewById(R.id.viewer);

        }
    }


}
