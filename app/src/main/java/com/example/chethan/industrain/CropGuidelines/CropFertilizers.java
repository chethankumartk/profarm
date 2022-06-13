package com.example.chethan.industrain.CropGuidelines;


import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.ViewMukyaActivity;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropFertilizers extends Fragment {
    FragmentActivity mContext;
    private YouTubePlayer YPlayer;
    SharedPreferences sharedPref;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    crop cropp;
    TextView Cropname,scname,cpkm,yph,soiltpe,firstharvest,harvettime,adddetails,season;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof FragmentActivity) {
            mContext = (FragmentActivity) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_fertilizers, container, false);

        Cropname=view.findViewById(R.id.cropname);
        scname=view.findViewById(R.id.sciencename);
        cpkm=view.findViewById(R.id.cpkm);
        yph=view.findViewById(R.id.yph);
        soiltpe=view.findViewById(R.id.soiltype);
        firstharvest=view.findViewById(R.id.firstharvest);
        harvettime=view.findViewById(R.id.harvesttime);
        adddetails=view.findViewById(R.id.adddetails);
        season=view.findViewById(R.id.season);




        sharedPref=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);

        String myString=sharedPref.getString("re","none");

         youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        FirebaseFirestore.getInstance().collection("crops").whereEqualTo("re",myString)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    cropp=document.toObject(crop.class);

                                    youTubePlayerFragment.initialize("AIzaSyDPoqKD7KzaZp1IfFvUZiuu4olJkhFPYAA", new YouTubePlayer.OnInitializedListener() {
                                        @Override
                                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                            if (!b) {
                                                YPlayer = youTubePlayer;
                                                YPlayer.setFullscreen(false);

/*
                    YPlayer.loadVideo("2zNSgSzhBfM");
*/
                                                YPlayer.cueVideo(cropp.getVu());
                                                /*YPlayer.play();*/
                                            }
                                        }

                                        @Override
                                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                        }
                                    });

                                    Cropname.setText(cropp.getNa());
                                    scname.setText(cropp.getSn());
                                    yph.setText(cropp.getYph());
                                    soiltpe.setText(cropp.getSt());
                                    firstharvest.setText(cropp.getFy());
                                    harvettime.setText(cropp.getHt());
                                    adddetails.setText(cropp.getAde());
                                    cpkm.setText(String.valueOf(cropp.getCpk()));
                                    season.setText(cropp.getSe());





                                }




                        }
                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();

    }
}