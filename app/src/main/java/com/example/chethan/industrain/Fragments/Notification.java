package com.example.chethan.industrain.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.notificationskalsakke.Notifications;
import com.example.chethan.industrain.notificationskalsakke.NotificationsAdapter;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.notificationskalsakke.SwipeDelete;
import com.example.chethan.industrain.usermai;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends android.support.v4.app.Fragment {

    private ActionBarDrawerToggle toggle;

    private RecyclerView mNotificationList;
    private NotificationsAdapter notificationsAdapter;

    private List<Notifications> mNotifList;
    private FirebaseFirestore mFirestore;
    private FirebaseFunctions mFunctions;
    TextView newnoti;
    private TextView clearnoti;



    public Notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notifications, container, false);
        toggle = ((usermai) getActivity()).getToggle();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));

        mFunctions = FirebaseFunctions.getInstance();

        clearnoti=v.findViewById(R.id.clearnoti);
        newnoti=v.findViewById(R.id.newnoti);


        mNotifList = new ArrayList<>();

        mNotificationList =  v.findViewById(R.id.notification_list);
        notificationsAdapter = new NotificationsAdapter(getContext(), mNotifList);



        mNotificationList.setHasFixedSize(true);
        mNotificationList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mNotificationList.setAdapter(notificationsAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        gett();
        enableSwipeToDeleteAndUndo();
        clearnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                recursiveDelete("/profile/"+FirebaseAuth.getInstance().getUid().toString().trim()+"/Notifications")
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        Object details = ffe.getDetails();
                                        Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                                else {
                                    notificationsAdapter.notifyDataSetChanged();

                                }

                                // ...
                            }
                        });






            }
        });




        return v;
    }

    public void gett()
    {
        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();




        mFirestore.collection("profile")
                .document(current_user_id).collection("Notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String a=String.valueOf(task.getResult().size());
                            newnoti.setText("( "+a+" ) New Notifications");


                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Notifications notifications = document.toObject(Notifications.class);
                                mNotifList.add(notifications);

                                notificationsAdapter.notifyDataSetChanged();


                            }



                        } else {
                            Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                });





       /* mFirestore.collection("profile").document(current_user_id).collection("Notifications").
                addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        for(DocumentChange doc: documentSnapshots.getDocumentChanges()) {

                            Notifications notifications = doc.getDocument().toObject(Notifications.class);
                            mNotifList.add(notifications);

                            notificationsAdapter.notifyDataSetChanged();

                        }

                    }
                });*/

    }
    private void enableSwipeToDeleteAndUndo() {
       final String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SwipeDelete swipeToDeleteCallback = new SwipeDelete(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Notifications item = notificationsAdapter.getData().get(position);
                notificationsAdapter.removeItem(position);

                FirebaseFirestore.getInstance().collection("profile").document(current_user)
                        .collection("Notifications").document(item.getRid())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Not deleted", Toast.LENGTH_SHORT).show();

                        }

                    }
                });






            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mNotificationList);
    }











    private Task<String> recursiveDelete(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("path", text);

        return mFunctions
                .getHttpsCallable("recursiveDelete")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }









               /*) FirebaseFirestore.getInstance().collection("profile").document(curid)
                        .collection("Notifications").document(notifications.getRid())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            Toast.makeText(context, "Not deleted", Toast.LENGTH_SHORT).show();

                        }

                    }
                });*/



}
