package com.example.android.katsapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.katsapp.R;
import com.example.android.katsapp.model.Breeds;

public class BreedsAdapter extends RecyclerView.Adapter<BreedsAdapter.BreedsAdapterViewHolder> {

    private static final String LOG_TAG = BreedsAdapter.class.getSimpleName();

    private BreedAdapterClickListener mBreedClickListener;

    private Context mContext;
    private Breeds[] mBreeds;
    private TextView breedName;
    private ImageView imageViewHolder;

    private String breed_name, breed_id;


    public BreedsAdapter(BreedAdapterClickListener mBreedClickListener, Context context){
        this.mBreedClickListener = mBreedClickListener;
        this.mContext = context;
    }

    public void setBreeds(Breeds[] breeds){
        this.mBreeds = breeds;
    }

    @NonNull
    @Override
    public BreedsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_breeds, parent, false);

        return new BreedsAdapter.BreedsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreedsAdapterViewHolder breedsAdapterViewHolder, int position) {

        String country_code = mBreeds[position].getCountryCode();
        String the_country_code = country_code.toLowerCase();
        String country_code_image_url = "https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.2.1/flags/1x1/" + the_country_code + ".svg";

        breed_name = mBreeds[position].getName();
        breed_id = mBreeds[position].getId();

        breedName.setText(breed_name);


        Glide.with(mContext)
                .load("" + country_code_image_url)
                .apply(new RequestOptions().placeholder(R.drawable.abys_1))
                .into(imageViewHolder);

    }

    @Override
    public int getItemCount() {
        return mBreeds.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class BreedsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        BreedsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            breedName = itemView.findViewById(R.id.breed_title);
            imageViewHolder = itemView.findViewById(R.id.breed_image);

            imageViewHolder.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();

            String breed_id_to_pass = mBreeds[clickedPosition].getId();

            mBreedClickListener.onBreedClicked(clickedPosition, breed_id_to_pass);
        }
    }

    public interface BreedAdapterClickListener{
        void onBreedClicked(int position, String breedId);
    }
}
