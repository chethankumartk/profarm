package com.example.chethan.industrain.CropGuidelines;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropGuide1 extends Fragment {

    SharedPreferences sharedPref;
    FragmentActivity mContext;
    crop cropp;

    TextView tv;

    public CropGuide1() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crop_guide1, container, false);
        tv=view.findViewById(R.id.fertilizerguideline);
        Bundle args = getArguments();
        sharedPref=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);

        String myString=sharedPref.getString("re","none");


        FirebaseFirestore.getInstance().collection("crops").whereEqualTo("re",myString)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                cropp=document.toObject(crop.class);
                                tv.setText(cropp.getFd());

                            }

                        }
                    }
                });













        return view;
    }


}
