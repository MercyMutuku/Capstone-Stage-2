package com.example.android.katsapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.example.android.katsapp.model.Breeds;
import com.example.android.katsapp.model.Images;
import com.example.android.katsapp.provider.BreedsContract.BreedsEntry;
import com.example.android.katsapp.provider.BreedsDbHelper;
import com.example.android.katsapp.utils.JsonUtils;
import com.example.android.katsapp.utils.UrlUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreedDetailsActivity extends AppCompatActivity {

    int clickedBreedPosition;
    String breedsQueryResponse, breedName, breedOrigin, description, temperament, wikipedia_url, breedId;
    String loadingFromFav, breed_id, country_code, country_code_image_url;
    private int affection_level, adaptability, child_friendly, dog_friendly, energy_level, grooming, health_issues;
    private int intelligence, shedding_level, social_needs, stranger_friendly, hypoallergenic;
    Breeds[] breedDetails;
    Images[] breedImageArray;
    GetBreedImageTask getBreedImageTask;

    private static final String LOG_TAG = BreedsFragment.class.getSimpleName();

    Cursor cursor;

    @BindView(R.id.origin)
    TextView origin;

    @BindView(R.id.hypoallergenic)
    TextView tv_hypoallergenic;

    @BindView(R.id.description)
    TextView tv_description;

    @BindView(R.id.temperament)
    TextView tv_temperament;

    @BindView(R.id.affection)
    RatingBar rb_affection;

    @BindView(R.id.adaptability)
    RatingBar rb_adaptability;

    @BindView(R.id.child_friendly)
    RatingBar rb_child_friendly;

    @BindView(R.id.dog_friendly)
    RatingBar rb_dog_friendly;

    @BindView(R.id.energy_level)
    RatingBar rb_energy_level;

    @BindView(R.id.grooming)
    RatingBar rb_grooming;

    @BindView(R.id.health_issues)
    RatingBar rb_health_issues;

    @BindView(R.id.intelligence)
    RatingBar rb_intelligence;

    @BindView(R.id.shedding_level)
    RatingBar rb_shedding_level;

    @BindView(R.id.social_needs)
    RatingBar rb_social_needs;

    @BindView(R.id.stranger_friendly)
    RatingBar rb_stranger_friendly;

    @BindView(R.id.wikipedia)
    TextView tv_wikipedia;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.fab_remove)
    FloatingActionButton fab_remove;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.country_code_image)
    ImageView country_code_image;

    @BindView(R.id.breed_image)
    ImageView iv_breed_image;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.btn_retry)
    Button buttonRetry;

    @BindView(R.id.tv_error)
    TextView tvError;

    @BindView(R.id.affection_label)
    TextView affection_label;

    @BindView(R.id.adaptability_label)
    TextView adaptability_label;

    @BindView(R.id.child_friendly_label)
    TextView child_friendly_label;

    @BindView(R.id.dog_friendly_label)
    TextView dog_friendly_label;

    @BindView(R.id.energy_level_label)
    TextView energy_level_label;

    @BindView(R.id.grooming_label)
    TextView grooming_label;

    @BindView(R.id.health_issues_label)
    TextView health_issues_label;

    @BindView(R.id.intelligence_label)
    TextView intelligence_label;

    @BindView(R.id.shedding_level_label)
    TextView shedding_level_label;

    @BindView(R.id.social_needs_label)
    TextView social_needs_label;

    @BindView(R.id.stranger_friendly_label)
    TextView stranger_friendly_label;

    @BindView(R.id.cv_origin)
    CardView cv_origin;

    @BindView(R.id.cv_hypoallergenic)
    CardView cv_hypoallergenic;

    @BindView(R.id.lly_data_handling)
    LinearLayout lly_data_handling;

    @BindView(R.id.lly2_data_handling)
    LinearLayout lly2_data_handling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retry();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {

            clickedBreedPosition = intent.getIntExtra("clickedBreedPosition", 0);
            breed_id = intent.getStringExtra("breed_id");
            loadingFromFav = intent.getStringExtra("loadingFromFav");
            breedsQueryResponse = intent.getStringExtra("breedsQueryResponse");

            if (loadingFromFav.equals("yeees")) {

                breedId = breed_id;

                breedDetails = getBreedDetails(breed_id);

                breedName = breedDetails[0].getName();

                hypoallergenic = breedDetails[0].getHypoallergenic();
                description = breedDetails[0].getDescription();
                breedName = breedDetails[0].getName();
                temperament = breedDetails[0].getTemperament();
                breedOrigin = breedDetails[0].getOrigin();
                country_code = breedDetails[0].getCountryCode();
                affection_level = breedDetails[0].getAffection_level();
                adaptability = breedDetails[0].getAdaptability();
                child_friendly = breedDetails[0].getChild_friendly();
                dog_friendly = breedDetails[0].getDog_friendly();
                energy_level = breedDetails[0].getEnergy_level();
                grooming = breedDetails[0].getGrooming();
                health_issues = breedDetails[0].getHealth_issues();
                intelligence = breedDetails[0].getIntelligence();
                shedding_level = breedDetails[0].getShedding_level();
                social_needs = breedDetails[0].getSocial_needs();
                stranger_friendly = breedDetails[0].getStranger_friendly();
                wikipedia_url = breedDetails[0].getWikipedia_url();

            } else {

                if (!(CheckNetwork.isInternetAvailable(this))){

                    hideDataViews();
                    networkError();

                    return;
                }

                try {
                    breedDetails = JsonUtils.parseBreedsJson(breedsQueryResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hypoallergenic = breedDetails[clickedBreedPosition].getHypoallergenic();
                description = breedDetails[clickedBreedPosition].getDescription();
                breedName = breedDetails[clickedBreedPosition].getName();
                temperament = breedDetails[clickedBreedPosition].getTemperament();
                breedOrigin = breedDetails[clickedBreedPosition].getOrigin();
                country_code = breedDetails[clickedBreedPosition].getCountryCode();
                affection_level = breedDetails[clickedBreedPosition].getAffection_level();
                adaptability = breedDetails[clickedBreedPosition].getAdaptability();
                child_friendly = breedDetails[clickedBreedPosition].getChild_friendly();
                dog_friendly = breedDetails[clickedBreedPosition].getDog_friendly();
                energy_level = breedDetails[clickedBreedPosition].getEnergy_level();
                grooming = breedDetails[clickedBreedPosition].getGrooming();
                health_issues = breedDetails[clickedBreedPosition].getHealth_issues();
                intelligence = breedDetails[clickedBreedPosition].getIntelligence();
                shedding_level = breedDetails[clickedBreedPosition].getShedding_level();
                social_needs = breedDetails[clickedBreedPosition].getSocial_needs();
                stranger_friendly = breedDetails[clickedBreedPosition].getStranger_friendly();
                wikipedia_url = breedDetails[clickedBreedPosition].getWikipedia_url();

                breedId = breedDetails[clickedBreedPosition].getId();

            }

            String the_country_code = country_code.toLowerCase(Locale.ROOT);
            country_code_image_url = "https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.2.1/flags/1x1/" + the_country_code + ".svg";

            setTitle(breedName);

            String breedImage = "";

            if (CheckNetwork.isInternetAvailable(BreedDetailsActivity.this)) {
                SvgLoader.pluck()
                        .with(this)
                        .load(country_code_image_url, country_code_image);

                getBreedImageTask = new GetBreedImageTask();

                getBreedImageTask.execute();

                try {
                    breedImageArray = getBreedImageTask.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (breedImageArray != null) {
                    breedImage = breedImageArray[0].getImageUrl();
                }
            } else {
                breedImage = "https://cdn2.thecatapi.com/images/27777g.jpg";
            }

            if (breedImage != ""){
                iv_breed_image.setImageResource(R.drawable.abys_2);
            } else {
                Picasso.with(this)
                        .load(breedImage)
                        .placeholder(R.drawable.abys_3)
                        .error(R.drawable.abys_2)
                        .into(iv_breed_image);
            }

            origin.setText(breedOrigin);

            if (hypoallergenic != 0){
                tv_hypoallergenic.setText(R.string.hypoallergenic);
            } else {
                tv_hypoallergenic.setVisibility(View.GONE);
            }

            tv_description.setText(description);
            tv_temperament.setText(temperament);
            rb_affection.setRating(affection_level);
            rb_adaptability.setRating(adaptability);
            rb_child_friendly.setRating(child_friendly);
            rb_dog_friendly.setRating(dog_friendly);
            rb_energy_level.setRating(energy_level);
            rb_grooming.setRating(grooming);
            rb_health_issues.setRating(health_issues);
            rb_intelligence.setRating(intelligence);
            rb_shedding_level.setRating(shedding_level);
            rb_social_needs.setRating(social_needs);
            rb_stranger_friendly.setRating(stranger_friendly);
            tv_wikipedia.setText(wikipedia_url);

        }

        updateUi();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save breed to DB
                saveBreedData();

                Snackbar.make(view, breedName+" marked as favorite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // send a broadcast to update the appwidget
                CatsWidget.sendRefreshBroadcast(BreedDetailsActivity.this);

                updateUi();
            }
        });

        fab_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteBreed(new BreedsDbHelper(BreedDetailsActivity.this)
                        .getReadableDatabase(), BreedsEntry.TABLE_NAME, breedId);

                Snackbar.make(view, breedName+" removed from favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                // send a broadcast to update the appwidget
                CatsWidget.sendRefreshBroadcast(BreedDetailsActivity.this);

                updateUi();
            }
        });

    }

    public void networkError(){
        progressBar.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.VISIBLE);
    }

    private void retry(){
        if (!(CheckNetwork.isInternetAvailable(this))){
            networkError();
            return;
        }

        hideViews();

        // Refresh Activity
        finish();
        startActivity(getIntent());
    }

    public void hideViews(){
        tvError.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("RestrictedApi")
    public void hideDataViews(){

        fab.setVisibility(View.INVISIBLE);
        fab_remove.setVisibility(View.INVISIBLE);

        lly_data_handling.setVisibility(View.GONE);
        lly2_data_handling.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    public void showDataViews(){

        fab.setVisibility(View.VISIBLE);
        fab_remove.setVisibility(View.VISIBLE);

        lly_data_handling.setVisibility(View.VISIBLE);
        lly2_data_handling.setVisibility(View.VISIBLE);

    }

    private Breeds[] getBreedDetails(String breed_id) {

        Uri uri = BreedsEntry.CONTENT_URI;
        Breeds[] theResult = new Breeds[0];

        Cursor mCursor = this.getContentResolver()
                .query(uri, null, "breed_id = ?", new String[]{breed_id}, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            theResult = new Breeds[mCursor.getCount()];

            for (int i=0; i < mCursor.getCount(); i++){
                Breeds breeds = new Breeds();

                breeds.setName(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_BREED_NAME)));
                breeds.setId(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_ID)));

                breeds.setOrigin(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_ORIGIN)));
                breeds.setCountryCode(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_COUNTRY_CODE)));
                breeds.setHypoallergenic(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_HYPO_ALLERGENIC))));
                breeds.setDescription(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_DESCRIPTION)));
                breeds.setTemperament(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_TEMPERAMENT)));
                breeds.setAffection_level(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_AFFECTION))));
                breeds.setAdaptability(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_ADAPTABILITY))));
                breeds.setChild_friendly(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_CHILD_FRIENDLY))));
                breeds.setDog_friendly(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_DOG_FRIENDLY))));
                breeds.setEnergy_level(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_ENERGY_LEVEL))));
                breeds.setGrooming(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_GROOMING))));
                breeds.setHealth_issues(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_HEALTH_ISSUES))));
                breeds.setIntelligence(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_INTELLIGENCE))));
                breeds.setShedding_level(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_SHEDDING_LEVEL))));
                breeds.setSocial_needs(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_SOCIAL_NEEDS))));
                breeds.setStranger_friendly(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_STRANGER_FRIENDLY))));
                breeds.setWikipedia_url(mCursor.getString(mCursor.getColumnIndex(BreedsEntry.COLUMN_WIKIPEDIA)));

                theResult[i] = breeds;

                mCursor.moveToNext();
            }

            mCursor.close();
        }

        return theResult;
    }

    @SuppressLint("RestrictedApi")
    public void updateUi(){

        if (checkBreed(new BreedsDbHelper(this).getReadableDatabase(), BreedsEntry.TABLE_NAME, breedId)){

            fab_remove.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);

            Log.i(LOG_TAG, "FAB FAV HIDDEN. FAB REMOVE SHOWN.");

        } else {

            fab_remove.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.VISIBLE);

            Log.i(LOG_TAG, "FAB FAV SHOWN. FAB REMOVE HIDDEN.");
        }

    }

    // this method checks if a breed exists in the DB
    private boolean checkBreed(SQLiteDatabase db, String tableName, String breedId) {

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE breed_id = ?", new String[]{breedId});

        return cursor.moveToNext();
    }

    // delete breed
    private void deleteBreed(SQLiteDatabase db, String tableName, String breedId) {

        //db.delete(tableName, "breed_id = ?", new String[]{breedId});

        db.execSQL("DELETE FROM " + tableName + " WHERE breed_id = ?", new String[]{breedId});
    }

    private void saveBreedData(){
        BreedsDbHelper breedsDbHelper = new BreedsDbHelper(this);
        SQLiteDatabase db = breedsDbHelper.getReadableDatabase();
        String tableName = BreedsEntry.TABLE_NAME;

        ContentValues contentValues = new ContentValues();

        contentValues.put(BreedsEntry.COLUMN_ID, breedId);
        contentValues.put(BreedsEntry.COLUMN_BREED_NAME, breedName);
        contentValues.put(BreedsEntry.COLUMN_ORIGIN, breedOrigin);
        contentValues.put(BreedsEntry.COLUMN_COUNTRY_CODE, country_code);
        contentValues.put(BreedsEntry.COLUMN_HYPO_ALLERGENIC, hypoallergenic);
        contentValues.put(BreedsEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(BreedsEntry.COLUMN_TEMPERAMENT, temperament);
        contentValues.put(BreedsEntry.COLUMN_AFFECTION, affection_level);
        contentValues.put(BreedsEntry.COLUMN_ADAPTABILITY, adaptability);
        contentValues.put(BreedsEntry.COLUMN_CHILD_FRIENDLY, child_friendly);
        contentValues.put(BreedsEntry.COLUMN_DOG_FRIENDLY, dog_friendly);
        contentValues.put(BreedsEntry.COLUMN_ENERGY_LEVEL, energy_level);
        contentValues.put(BreedsEntry.COLUMN_GROOMING, grooming);
        contentValues.put(BreedsEntry.COLUMN_HEALTH_ISSUES, health_issues);
        contentValues.put(BreedsEntry.COLUMN_INTELLIGENCE, intelligence);

        contentValues.put(BreedsEntry.COLUMN_SHEDDING_LEVEL, shedding_level);
        contentValues.put(BreedsEntry.COLUMN_SOCIAL_NEEDS, social_needs);
        contentValues.put(BreedsEntry.COLUMN_STRANGER_FRIENDLY, stranger_friendly);
        contentValues.put(BreedsEntry.COLUMN_WIKIPEDIA, wikipedia_url);

        getContentResolver().insert(BreedsEntry.CONTENT_URI, contentValues);
    }

    public void refresh(){
        finish();
        startActivity(getIntent());
    }

    public static void logInfo(String str){
        if (str.length() > 4000){
            Log.i(LOG_TAG, str.substring(0, 4000));
            logInfo(str.substring(4000));
        } else {
            Log.i(LOG_TAG, str);
        }
    }

    // Get Breed Images
    @SuppressLint("StaticFieldLeak")
    private class GetBreedImageTask extends AsyncTask<String, Void, Images[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            hideDataViews();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Images[] doInBackground(String... strings) {

            if (!(CheckNetwork.isInternetAvailable(BreedDetailsActivity.this))){
                networkError();
                return null;
            }

            if (UrlUtils.API_KEY.equals("")){
                networkError();
                tvError.setText(R.string.api_error_message);
                return null;
            }

            URL breedImageUrl = UrlUtils.buildBreedImageUrl(breedId);

            String breedImageQueryResponse = null;

            try {
                breedImageQueryResponse = UrlUtils.getResponseFromHttp(breedImageUrl);
                breedImageArray = JsonUtils.parseImagesJson(breedImageQueryResponse);
            } catch (Exception e){
                e.printStackTrace();
            }

            return breedImageArray;
        }


        @Override
        protected void onPostExecute(Images[] images) {
            new GetBreedImageTask().cancel(true);

            if (images != null){
                breedImageArray = images;

                showDataViews();
                hideViews();

                Log.i(LOG_TAG, "breedImageArray,,"+ Arrays.toString(breedImageArray));
            }
        }
    }

}
