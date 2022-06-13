package com.example.chethan.industrain;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.CropGuidelines.GuideLinesListActivity;
import com.example.chethan.industrain.CropGuidelines.GuidelineActivity;
import com.example.chethan.industrain.googlemaps.MapsActivity;
import com.example.chethan.industrain.newsadapter.NewsRecycler;
import com.example.chethan.industrain.youtube.YoutubeLoadAgakke;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class MyAdapter extends PagerAdapter {

    List<Integer> lstImages;
    Context context;
    LayoutInflater layoutInflater;

    public MyAdapter(List<Integer> lstImages,Context context) {
        this.lstImages = lstImages;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lstImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view=layoutInflater.from(container.getContext()).inflate(R.layout.carditem,container,false);
        ImageView mageView=view.findViewById(R.id.imageView);
        TextView textView=view.findViewById(R.id.summerwinter);
        AssetManager am = context.getApplicationContext().getAssets();


        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "font/%s", "Raleway-Regular.ttf"));


        Picasso.with(context)
                .load(lstImages.get(position))
                .fit()
                // .centerInside()
                .into(mageView);
        // mageView.setImageResource(lstImages.get(position));
        container.addView(view);
       /* view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context,check.class));
            }
        });*/
       textView.setTypeface(typeface);
       if(position==0)
       {
           textView.setText("Seasonal crop guidelines");
       }
        if(position==1)
        {
            textView.setText("Crops to grow in Summer");
        }
        if(position==2)
        {
            textView.setText("News Feed");
        }
        if(position==3)
        {
            textView.setText("Give Us Feedback");
        }
        if(position==4)
        {
            textView.setText("Crops to grow in Summer");
        }
        if(position==5)
        {
            textView.setText("Crops to grow in Winter");
        }
        mageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0)
                {
                    Intent i = new Intent(context, GuideLinesListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                    // Intent i = new Intent(context, YoutubeLoadAgakke.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //context.startActivity(i);

                    //Toast.makeText(context, "work"+position, Toast.LENGTH_SHORT).show();

                }
                if(position ==4)
                {
                    Intent i = new Intent(context, GuideLinesListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }
                if(position==2)
                {
                    Intent i=new Intent(context, NewsRecycler.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }

                if(position==1)
                {
                    Intent i=new Intent(context, GuideLinesListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                if(position == 3)
                {

                    sendEmail();
                }
                else {
                   // Toast.makeText(context, "work"+position, Toast.LENGTH_SHORT).show();

                    //Intent i = new Intent(context, usermai.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //context.startActivity(i);


                }
                //context.startActivity(new Intent(view.getContext(),check.class));


            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
    protected void sendEmail() {

        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "subject text" + "&body=" + "body text " + "&to=" + "chethanchethan052@mail.com");
        mailIntent.setData(data);
        context.startActivity(Intent.createChooser(mailIntent, "Send mail.."));
    }

}
