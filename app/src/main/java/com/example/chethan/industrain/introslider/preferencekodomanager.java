package com.example.chethan.industrain.introslider;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chethan.industrain.R;

public class preferencekodomanager
{
    private Context context;
    private SharedPreferences sharedPreferences;

    public preferencekodomanager(Context context)
    {
        this.context=context;
        getSharedPrefence();
    }


    private void getSharedPrefence()
    {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_preference),context.MODE_PRIVATE);

    }

    public void writePreference()
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(context.getString(R.string.my_preference_key),"INIT_OK");
        editor.commit();
    }



    public boolean checkPreference()
    {
        boolean status = false;
        if (sharedPreferences.getString(context.getString(R.string.my_preference_key),"null").equals("null"))
        {
            status = false;
        }
        else
        {
           // status = true;
             status=false;
        }
        return status;
    }

    public void clearPreferences()
    {
        sharedPreferences.edit().clear().commit();

    }
}
