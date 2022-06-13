package com.example.chethan.industrain.notificationskalsakke;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.CircleImageView;
import com.example.chethan.industrain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{

    private List<Notifications> mNotifList;

    private FirebaseFirestore firebaseFirestore;
    private Context context;

    public NotificationsAdapter(Context context, List<Notifications> mNotifList) {

        this.mNotifList = mNotifList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationrecycl, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        String from_id = mNotifList.get(position).getFrom();

        holder.mNotifMessage.setText(mNotifList.get(position).getMessage());

        firebaseFirestore.collection("profile").document(from_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getString("username");
                String image = documentSnapshot.getString("iu");

                holder.mNotifName.setText(name+" is Interested to buy your product");

                Picasso.with(context)
                        .load(image)
                        .placeholder(R.drawable.account)
                        .fit()
                        .into(holder.mNotifImage);



            }
        });


    }

    @Override
    public int getItemCount() {
        return mNotifList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public CircleImageView mNotifImage;
        public TextView mNotifName;
        public TextView mNotifMessage;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mNotifImage =  mView.findViewById(R.id.notif_image);
            mNotifMessage =  mView.findViewById(R.id.notif_message);
            mNotifName =  mView.findViewById(R.id.notif_name);


        }
    }
    public void removeItem(int position) {
        mNotifList.remove(position);
        notifyItemRemoved(position);
    }
    public List<Notifications> getData() {
        return mNotifList;
    }





}
