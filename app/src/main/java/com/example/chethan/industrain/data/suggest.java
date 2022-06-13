package com.example.chethan.industrain.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class suggest implements SearchSuggestion {




    private String suggestion;
    private boolean mIsHistory = false;

    public suggest(String suggestion) {
        this.suggestion = suggestion.toLowerCase();
    }

    public suggest(Parcel source) {
        this.suggestion = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return suggestion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(suggestion);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
    public String getSuggestion(){

        return this.suggestion ;

    }

    public static final Creator<suggest> CREATOR = new Creator<suggest>() {
        @Override
        public suggest createFromParcel(Parcel in) {
            return new suggest(in);
        }

        @Override
        public suggest[] newArray(int size) {
            return new suggest[size];
        }
    };


}