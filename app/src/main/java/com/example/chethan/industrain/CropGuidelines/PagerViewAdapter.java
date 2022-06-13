package com.example.chethan.industrain.CropGuidelines;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class  PagerViewAdapter extends FragmentPagerAdapter {
    private Bundle bundle;

    public PagerViewAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);

        this.bundle=bundle;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:CropFertilizers usersFragment = new CropFertilizers();
                   return usersFragment;



            case 1: CropGuide1 profileFragment = new CropGuide1();
                    return profileFragment;




            case 2:CropNutrition m = new CropNutrition();
                   return m;


            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }

}
