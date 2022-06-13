package com.example.chethan.industrain.FragmentsAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.Fragments.Wishlist;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.ViewMukyaActivity;
import com.example.chethan.industrain.fragmentclasses.WishlistAdd;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class checkwishadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    // An Activity's Context.
    private final Context mContext;

    // The list of Native ads and menu items.
    private final List<Object> mUploads;

    public checkwishadapter(Context context, List<Object> recyclerViewItems) {
        this.mContext = context;
        this.mUploads = recyclerViewItems;
    }
    /**
     * The {@link ImageViewHolder} class.
     * Provides a reference to each view in the menu item view.
     */

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

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = mUploads.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a Native ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.ad_unified,
                        viewGroup, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case MENU_ITEM_VIEW_TYPE:
                // Fall through.
            default:
                View imageLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.wishlistlist, viewGroup, false);
                return new ImageViewHolder(imageLayoutView);
        }
    }

    /**
     * Replaces the content in the views that make up the menu item view and the
     * Native ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) mUploads.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
                break;
            case MENU_ITEM_VIEW_TYPE:
                // fall through
            default:

                ImageViewHolder menuItemHolder = (ImageViewHolder) holder;


                final WishlistAdd uploadCurrent = (WishlistAdd) mUploads.get(position);

                Picasso.with(mContext)
                        .load(uploadCurrent.getImageurl())
                        .placeholder(R.drawable.gradient5)
                        .fit()
                        .into(menuItemHolder.imagecrop);
                menuItemHolder.cropname.setText(uploadCurrent.getCropname());
                menuItemHolder.price.setText(String.valueOf(uploadCurrent.getPrice()));
                menuItemHolder.location.setText(uploadCurrent.getLocality());
                menuItemHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WishlistAdd uploadCurrent = (WishlistAdd) mUploads.get(position);

                        mContext.startActivity(new Intent(mContext,ViewMukyaActivity.class)
                                .putExtra("refid",uploadCurrent.getId1())
                                .putExtra("price",uploadCurrent.getPrice())
                                .putExtra("eidd",uploadCurrent.getEid())

                        );

                    }
                });
                menuItemHolder.del.setOnClickListener(new View.OnClickListener() {
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
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}
