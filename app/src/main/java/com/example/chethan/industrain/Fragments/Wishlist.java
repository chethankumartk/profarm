package com.example.chethan.industrain.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chethan.industrain.FragmentsAdapter.checkwishadapter;
import com.example.chethan.industrain.FragmentsAdapter.wishlistadapter;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.RecyclerTouchListener;
import com.example.chethan.industrain.ViewMukyaActivity;
import com.example.chethan.industrain.fragmentclasses.WishlistAdd;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.usermai;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wishlist extends Fragment {

    String eid;
    checkwishadapter adapter;
    RecyclerView mRecyclerview;
    List<Object> mUploads=new ArrayList<>();
    ActionBarDrawerToggle toggle;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private AdLoader adLoader;
    public static final int NUMBER_OF_ADS = 1;






    public Wishlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        MobileAds.initialize(getActivity(),
                getString(R.string.admob_app_id));

        eid= FirebaseAuth.getInstance().getUid();
        mRecyclerview=view.findViewById(R.id.wishlistrecycle);


        mRecyclerview.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerview.setLayoutManager(layoutManager);
        toggle = ((usermai) getActivity()).getToggle();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));


        mRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                       // sharedPref.edit().putString("category", uploadCurrent.getCategory()).commit();


                        // showUpdateDeleteDialog(upload.getIdd(), upload.getName(),upload.getImageUrl(),
                        //       upload.getmAddress(),upload.getmCropname(),upload.getPhon());

                    }

                    @Override
                    public void onLongClick(View view, int position) {


                        //showUpdateDeleteDialog((Stringupload.getIdd(),upload.getName(),upload.getImageUrl());
                    }
                }));




        gett();
        loadNativeAds();

        // Inflate the layout for this fragment
        return view;
    }

    public void gett()
    {
        FirebaseFirestore.getInstance().collection("WishList")
                .whereEqualTo("eid",eid)
                .limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Object upload = document.toObject(WishlistAdd.class);
                                mUploads.add(upload);

                                //Toast.makeText(getActivity(), document.get("mCropname").toString(), Toast.LENGTH_LONG).show();

                            }
                            adapter = new checkwishadapter(getActivity(), mUploads);

                            mRecyclerview.setAdapter(adapter);



                        } else {
                            Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                            //  usermai.this.notify();
                        }
                    }
                });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int offset = (mUploads.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad : mNativeAds) {
            mUploads.add(index, ad);
            index = index + offset;
        }
    }

    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(getActivity(), getString(R.string.ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                                               if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }






}
