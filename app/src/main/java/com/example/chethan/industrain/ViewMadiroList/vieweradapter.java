package com.example.chethan.industrain.ViewMadiroList;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class vieweradapter extends RecyclerView.Adapter<vieweradapter.ImageViewHolder> {
    private Context mContext;
    private List<viewcontract> mUploads;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String eid;



    public vieweradapter(Context context, List<viewcontract> uploads) {
        db=FirebaseFirestore.getInstance();
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public vieweradapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.viewerlist, parent, false);
        eid= FirebaseAuth.getInstance().getUid();
        return new vieweradapter.ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final vieweradapter.ImageViewHolder holder, final int position) {

        final viewcontract uploadCurrent = mUploads.get(position);

        holder.name.setText(uploadCurrent.getName().toString());
        holder.phone.setText(uploadCurrent.getPhone().toString());
        holder.Email.setText(uploadCurrent.getEmail().toString());
        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String phone = uploadCurrent.getPhone().toString().trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    mContext.startActivity(intent);

               /* String uri = "tel:" + pp.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PH
                ONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }*/
                    //mContext.startActivity(intent);



            }
        });




    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView Email;
        public  TextView phone;
        public TextView name;
        public FloatingActionButton fab;

        public ImageViewHolder(View itemView) {
            super(itemView);

            Email = itemView.findViewById(R.id.email);
            phone=itemView.findViewById(R.id.phone);
            name=itemView.findViewById(R.id.name);
            fab=itemView.findViewById(R.id.fab);

        }
    }


}
