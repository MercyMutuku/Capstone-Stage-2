package com.example.android.katsapp.Adapters;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.example.android.katsapp.R;
import com.example.android.katsapp.database.FavoriteBreeds;

import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private static final String LOG_TAG = BreedsAdapter.class.getSimpleName();

    private FavAdapterClickListener favClickListener;

    private Activity mActivity;
    private List<FavoriteBreeds> mBreeds;
    private FavoriteBreeds favoriteBreeds;
    private TextView breedName;
    private ImageView imageViewHolder;

    public FavoritesAdapter(FavAdapterClickListener favClickListener, Activity activity){
        this.favClickListener = favClickListener;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_breeds, parent, false);

        return new FavoritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder viewHolder, int position) {

        favoriteBreeds = mBreeds.get(position);

        String country_code = favoriteBreeds.getCountryCode().toLowerCase(Locale.ENGLISH);
        String country_code_image_url = "https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.2.1/flags/1x1/" + country_code + ".svg";

        String breed_name = favoriteBreeds.getBreedName();

        breedName.setText(breed_name);

        SvgLoader.pluck()
                .with(mActivity)
                .setPlaceHolder(R.mipmap.image_not_available, R.mipmap.image_not_available)
                .load(country_code_image_url, imageViewHolder);
    }

    @Override
    public int getItemCount() {
        if (mBreeds == null) {
            return 0;
        }
        return mBreeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            breedName = itemView.findViewById(R.id.breed_title);
            imageViewHolder = itemView.findViewById(R.id.breed_image);

            imageViewHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();

            String breed_id_to_pass = mBreeds.get(getAdapterPosition()).getBreed_id();
            String breed_name_to_pass = mBreeds.get(getAdapterPosition()).getBreedName();

            Log.i(LOG_TAG, "breed_id_to_pass:=" + breed_id_to_pass);
            Log.i(LOG_TAG, "breed_name_to_pass:=" + breed_name_to_pass);

            favClickListener.onBreedClicked(clickedPosition, breed_id_to_pass, breed_name_to_pass);
        }
    }

    /**
     * When data changes, this method updates the list of favoriteBreeds
     * and notifies the adapter to use the new values on it
     */
    public void setBreeds(List<FavoriteBreeds> favoriteBreeds) {
        mBreeds = favoriteBreeds;
        notifyDataSetChanged();
    }

    public interface FavAdapterClickListener{
        void onBreedClicked(int position, String breedId, String breedName);
    }
}
