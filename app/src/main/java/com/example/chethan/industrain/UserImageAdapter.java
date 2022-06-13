package com.example.chethan.industrain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chethan.industrain.homeadapters.Upload1;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserImageAdapter extends RecyclerView.Adapter<UserImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload1> mUploads;

    public UserImageAdapter(Context context, List<Upload1> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        Upload1 uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getPhone());
        final String pp = holder.textViewName.getText().toString().trim();
        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(holder.imageView);
        holder.farname.setText(uploadCurrent.getFarmername());
        holder.address.setText(uploadCurrent.getLocality());
        holder.cropname.setText(uploadCurrent.getCropname());
        holder.updatebutton.setVisibility(View.INVISIBLE);
        holder.price.setText(String.valueOf(uploadCurrent.getPrice()));
        holder.cropme.setText((uploadCurrent.getCropname()));

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = pp;
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
        public TextView textViewName;
        public ImageView imageView;
        public TextView farname;
        public TextView cropname;
        public TextView address;
        public  TextView price;
        public Button updatebutton;
        public TextView cropme;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            farname=itemView.findViewById(R.id.raithaname);
            cropname=itemView.findViewById(R.id.raithacrop);
            address=itemView.findViewById(R.id.raithaaddress);
            price=itemView.findViewById(R.id.price);
            updatebutton=itemView.findViewById(R.id.update);
            cropme=itemView.findViewById(R.id.cropmel);

        }
    }
}
