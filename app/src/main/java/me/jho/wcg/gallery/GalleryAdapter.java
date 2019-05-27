package me.jho.wcg.gallery;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jho.wcg.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    View galleryAdapterView;

    private ArrayList<GalleryVO> galleryArrayList;
    private ViewHolder galleryHolder;
    private Unbinder unbinder;


    public GalleryAdapter(ArrayList<GalleryVO> galleryArrayList) {
        this.galleryArrayList = galleryArrayList;
    }

    // [START onCreateViewHolder]
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        galleryAdapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_gallery, parent, false);

        galleryHolder = new ViewHolder(galleryAdapterView);

        unbinder = ButterKnife.bind(this, galleryAdapterView);

        return galleryHolder;

    }
    // [END onCreateViewHolder]

    // [START onBindViewHolder]
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.titleTextView.setText(galleryArrayList.get(position).getTitle());
        viewHolder.fontTextView.setText(galleryArrayList.get(position).getFont());
        viewHolder.backgroundColorTextView.setText(galleryArrayList.get(position).getBackgroundColor());
    }
    // [END onBindViewHolder]

    // [START getItemCount]
    @Override
    public int getItemCount() {
        return galleryArrayList.size();
    }
    // [END getItemCount]


    // [START ViewHolder]
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_card_title_content)
        public TextView titleTextView;
        @BindView(R.id.textView_card_font_content)
        public TextView fontTextView;
        @BindView(R.id.textView_card_backgroundColor_content)
        public TextView backgroundColorTextView;


        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);


        }
    }
    // [END ViewHolder]


}
