package com.example.chethan.industrain.CropGuidelines;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropNutrition extends Fragment {


    TextView howcal,whycal;
    TextView howfat,whyfat;
    TextView whyprot,howprot;
    TextView howcarbo,whycarbo;
    TextView howfibre,whyfibre;
    SharedPreferences sharedPref;


    ImageView imageView;
    String a,b;
    TextView description,wh,gotit;
    CircleProgress fatcircle,protcircle,carbcircle,fibrecircle;

    TextView calcium,potassium,iron,cholestrol,vitamina,vitaminc,sodium;

    ArcProgress arcProgress;
    crop cropp;
    public CropNutrition() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crop_nutrition, container, false);
        sharedPref=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);

        String myString=sharedPref.getString("re","none");


        howcal=view.findViewById(R.id.howcar);
        whycal=view.findViewById(R.id.whycar);
        fatcircle=view.findViewById(R.id.fatcircle);
        howfat=view.findViewById(R.id.howfat);
        howprot=view.findViewById(R.id.howprot);
        howcarbo=view.findViewById(R.id.howcarbs);
        howfibre=view.findViewById(R.id.howfibre);

        whyfat=view.findViewById(R.id.whyfat);
        whyprot=view.findViewById(R.id.whyprot);
        whycarbo=view.findViewById(R.id.whycarbs);
        whyfibre=view.findViewById(R.id.whyfibre);


        calcium=view.findViewById(R.id.calcium);
        potassium=view.findViewById(R.id.potassium);
        iron=view.findViewById(R.id.iron);
        cholestrol=view.findViewById(R.id.cholestrol);
        vitamina=view.findViewById(R.id.vitamina);
        vitaminc=view.findViewById(R.id.vitaminc);
        sodium=view.findViewById(R.id.sodium);



        protcircle=view.findViewById(R.id.protcircle);
        carbcircle=view.findViewById(R.id.carbcircle);
        fibrecircle=view.findViewById(R.id.fibrecircle);


        whycal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="Why Calories?";
                b="The number of calories food contains tells us how much potential energy they posses.";
                whydialog();
            }
        });
        howcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="How much Calories?";
                b="An Average women needs 2000 and men needs 2500 calories to maintain, " +
                        "anything over that will have to be cut down toreduce weight" +
                        ".";
                whydialog();
            }
        });


        whyfat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="Why Fat?";
                b="Dietary fats are essential to give your body energy and to support cell growth.";
                whydialog();
            }
        });
        howfat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="How much Fat?";
                b="The dietary reference intake (DRI) " +
                        "for fat in adults is 20% to 35% of total calories from fat. " +
                        "That is about 44 grams to 77 grams of fat per day if you eat 2,000 calories a day." +
                        " It is recommended to eat more of some types of fats because they provide health benefits.";
                whydialog();
            }
        });




        whyprot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="Why Protein?";
                b="Protein is an important building block of bones, muscles, cartilage, skin, and blood. Along with fat and " +
                        "carbohydrates, protein is a \"macronutrient,\" meaning that the body needs relatively large amounts of it.";
                whydialog();
            }
        });
        howprot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="How much Protein?";
                b="The DRI (Dietary Reference Intake) is 0.8 grams of protein per kilogram of body weight, or 0.36 grams per pound. " +
                        "This amounts to: 56 grams per day for the average sedentary man." +
                        " 46 grams per day for the average sedentary woman.";
                whydialog();
            }
        });


        whycarbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="Why Carbohydrates?";
                b="Carbohydrates are the body's main source of energy." +
                        " In their absence, your body will use protein and fat for energy.";
                whydialog();
            }
        });
        howcarbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="How much Carbohydrates?";
                b="The dietary guidelines recommend that carbs provide 45 to 65" +
                        " percent of your daily calorie intake. So if you eat a 2000-calorie diet, you should aim for about" +
                        " 225 to 325 grams of carbs per day." +
                        " But if you need to lose weight, you will get much faster results eating around 50 to 150 grams of carbs.";
                whydialog();
            }
        });



        whyfibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="Why Fibre?";
                b="Dietary fibre is important for our digestive health and regular bowel movements.";
                whydialog();
            }
        });
        howfibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="How much Fibre?";
                b="The national fiber recommendations are 30 to 38 grams a day for men and 25 grams a day " +
                        "for women between 18 and 50 years old, and 21 grams a day if a woman is 51 and older." +
                        " Another general guideline is to get 14 grams of fiber for every 1,000 calories in your diet.";
                whydialog();
            }
        });




        arcProgress=view.findViewById(R.id.arc);
        arcProgress.setBottomText("Calories");
        arcProgress.setSuffixText(" Kcal");
        arcProgress.setFinishedStrokeColor(getActivity().getColor(R.color.colorPrimary));
        arcProgress.setUnfinishedStrokeColor(getActivity().getColor(R.color.arc));
        arcProgress.setTextColor(getActivity().getColor(R.color.colorPrimary));


        FirebaseFirestore.getInstance().collection("crops").whereEqualTo("re",myString)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                cropp=document.toObject(crop.class);
                                carbcircle.setMax(100);
                                protcircle.setMax(100);
                                fibrecircle.setMax(100);
                                fatcircle.setMax(100);
                                arcProgress.setMax(1500);



                                carbcircle.setProgress(cropp.getCar());
                                protcircle.setProgress(cropp.getPr());
                                fibrecircle.setProgress(cropp.getFi());
                                fatcircle.setProgress(cropp.getF());
                                arcProgress.setProgress(cropp.getC());
                                calcium.setText(String.valueOf(cropp.getCa())+" mg");
                                potassium.setText(String.valueOf(cropp.getPo())+" mg");
                                iron.setText(String.valueOf(cropp.getI())+" mg");
                                cholestrol.setText(String.valueOf(cropp.getCh())+" mg");
                                vitamina.setText(String.valueOf(cropp.getVa())+" IU");
                                vitaminc.setText(String.valueOf(cropp.getVc())+" mg");
                                sodium.setText(String.valueOf(cropp.getS())+" mg");


                                protcircle.setSuffixText("g");
                                carbcircle.setSuffixText("g");
                                fibrecircle.setSuffixText("g");

                                fatcircle.setSuffixText("g");




                            }




                        }
                    }
                });






        return view;
    }
    public void whydialog()
    {

        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("Personal Info");
        dialog.setContentView(R.layout.dialognutrition);
        imageView=dialog.findViewById(R.id.imageview);
        description=dialog.findViewById(R.id.description);
        gotit=dialog.findViewById(R.id.gotit);
        wh=dialog.findViewById(R.id.why);

        wh.setText(a);
        description.setText(b);
        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Picasso.with(getContext())
                .load(R.drawable.nav_header)
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(imageView);




        dialog.show();







    }


}
