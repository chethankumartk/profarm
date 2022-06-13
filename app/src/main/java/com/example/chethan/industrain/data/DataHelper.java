package com.example.chethan.industrain.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Filter;
import android.widget.Toast;

import com.example.chethan.industrain.Upload;
import com.example.chethan.industrain.usermai;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {
    static List<ColorSuggestion> suggestionList = new ArrayList<>();


    // private static final String COLORS_FILE_NAME = "colors.json";

    private static List<ColorWrapper> sColorWrappers = new ArrayList<>();

    private static List<ColorSuggestion> sColorSuggestions =
            new ArrayList<>(Arrays.asList(
                    new ColorSuggestion("seasonal crop guidelines"),
                    new ColorSuggestion("Fertilizers guidelines"),
                    new ColorSuggestion("Rice"),
                    new ColorSuggestion("Ragi"),
                    new ColorSuggestion("Carrot"),
                    new ColorSuggestion("Apple"),
                    new ColorSuggestion("Mango"),
                    new ColorSuggestion("Raddish"),
                    new ColorSuggestion("Green peas"),
                    new ColorSuggestion("Red Chilli"),
                    new ColorSuggestion("Maize"),
                    new ColorSuggestion("Wheat"),
                    new ColorSuggestion("Corriander"),
                    new ColorSuggestion("PineApple"),
                    new ColorSuggestion("Orange"),
                    new ColorSuggestion("Green Chilli"),
                    new ColorSuggestion("Cucumber")));

    public interface OnFindColorsListener {
        void onResults(List<ColorWrapper> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<ColorSuggestion> results);
    }

    public static List<ColorSuggestion> getHistory(Context context, int count) {

        List<ColorSuggestion> suggestionList = new ArrayList<>();
        ColorSuggestion colorSuggestion;
        for (int i = 0; i < sColorSuggestions.size(); i++) {
            colorSuggestion = sColorSuggestions.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (ColorSuggestion colorSuggestion : sColorSuggestions) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(final usermai context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                //  String a=constraint.toString();






                if (!(constraint == null || constraint.length() == 0)) {

                    suggestionList=addsuggestion(constraint,limit,context);








                   /* for (ColorSuggestion suggestion : sColorSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            // suggestionList.add(new ColorSuggestion("chethan"));
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }*/
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<ColorSuggestion>() {
                    @Override
                    public int compare(ColorSuggestion lhs, ColorSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<ColorSuggestion>) results.values);
                }
            }
        }.filter(query);

    }
    public static List<ColorSuggestion> addsuggestion(CharSequence constraint, final int limit, final Context context)
    {
        final List<ColorSuggestion> suggestions=new ArrayList<>();


       /* FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").orderBy("mCropname")
                .startAt(constraint.toString().trim())
                .endAt(constraint.toString()+"\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Upload upload=document.toObject(Upload.class);

                                String b =upload.getmCropname();
                                 if (suggestions.add(new ColorSuggestion(b)))
                                 {
                                     Toast.makeText(context, b, Toast.LENGTH_SHORT).show();
                                 }

                                if (limit != -1 && suggestions.size() == limit) {
                                    break;
                                }



                            }
                        }


                    }
                });*/



        return suggestions;
    }


    public static void findColors(Context context, String query, final OnFindColorsListener listener) {
//       initColorWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<ColorWrapper> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (ColorWrapper color : sColorWrappers) {
                        if (color.getName().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(color);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<ColorWrapper>) results.values);
                }
            }
        }.filter(query);

    }

  /* private static void initColorWrapperList(Context context) {

        if (sColorWrappers.isEmpty()) {
            String jsonString = loadJson(context);
            sColorWrappers = deserializeColors(jsonString);
        }
    }

   /* private static String loadJson(Context context) {

        String jsonString;

        try {
            InputStream is = context.getAssets().open(COLORS_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonString;
    }

   /*private static List<ColorWrapper> deserializeColors(String jsonString) {

        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<ColorWrapper>>() {
        }.getType();
        return gson.fromJson(jsonString, collectionType);
    }*/

}