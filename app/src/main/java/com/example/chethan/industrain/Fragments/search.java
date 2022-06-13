package com.example.chethan.industrain.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chethan.industrain.FragmentsAdapter.searchadapter;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.RecyclerTouchListener;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.usermai;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class search extends Fragment {

    EditText searchmadakkeedittext;
    FirebaseFirestore db;
    Query query;
    searchadapter adapter;
    RecyclerView mRecyclerview;
    ActionBarDrawerToggle toggle;



    public search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        db=FirebaseFirestore.getInstance();
        searchmadakkeedittext=((usermai)getActivity()).getSearchmadakkeedittext();
        mRecyclerview=view.findViewById(R.id.searchrecycle);


        mRecyclerview.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        toggle = ((usermai) getActivity()).getToggle();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));





        searchmadakkeedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() == 0) {

                } // This is used as if user erases the characters in the search field.
                else {

                    query = db.collection("users").orderBy("cropname").startAt(charSequence.toString().toLowerCase().
                            trim()).endAt(charSequence.toString().toLowerCase().trim() + "\uf8ff");
                    adapterhaku(query);
                }
               // adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    public void adapterhaku( Query query)
    {

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Upload1> mUploads=new ArrayList<>();


                    for(QueryDocumentSnapshot document : task.getResult()) {

                        Upload1 upload = document.toObject(Upload1.class);
                        mUploads.add(upload);
                    }
                    adapter = new searchadapter(getActivity(), mUploads);
                    mRecyclerview.setAdapter(adapter);
                }
            }
        });
    }


}
