package com.mehmetakiftutuncu.mykentkart.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.activities.KentKartDetailsActivity;
import com.mehmetakiftutuncu.mykentkart.activities.KentKartInformationActivity;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

import java.util.ArrayList;

public class KentKartAdapter extends RecyclerView.Adapter<KentKartAdapter.ViewHolder> {
    private ArrayList<KentKart> kentKarts;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_kentkart, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KentKartInformationActivity.class);
                intent.putExtra(Constants.KENT_KART_NAME, viewHolder.name);
                intent.putExtra(Constants.KENT_KART_NUMBER, viewHolder.number);
                v.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final KentKart kentKart = kentKarts != null ? kentKarts.get(position) : null;

        if (kentKart != null) {
            viewHolder.name = kentKart.name;
            viewHolder.number = kentKart.number;

            viewHolder.nameTextView.setText(kentKart.name);
            viewHolder.numberTextView.setText(kentKart.getFormattedNumber());
            viewHolder.nfcImageView.setVisibility(StringUtils.isEmpty(kentKart.nfcId) ? View.GONE : View.VISIBLE);

            viewHolder.editImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), KentKartDetailsActivity.class);
                    intent.putExtra(Constants.EDIT_MODE, true);
                    intent.putExtra(Constants.KENT_KART_NAME, kentKart.name);
                    intent.putExtra(Constants.KENT_KART_NUMBER, kentKart.number);
                    intent.putExtra(Constants.KENT_KART_NFC_ID, kentKart.nfcId);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return kentKarts != null ? kentKarts.size() : 0;
    }

    public ArrayList<KentKart> getKentKarts() {
        return kentKarts;
    }

    public void setKentKarts(ArrayList<KentKart> kentKarts) {
        this.kentKarts = kentKarts;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String name;
        public String number;
        public TextView nameTextView;
        public TextView numberTextView;
        public ImageView nfcImageView;
        public ImageButton editImageButton;

        public ViewHolder(View view) {
            super(view);

            this.nameTextView = (TextView) view.findViewById(R.id.textView_kentKart_name);
            this.numberTextView = (TextView) view.findViewById(R.id.textView_kentKart_number);
            this.nfcImageView = (ImageView) view.findViewById(R.id.imageView_kentKart_nfcState);
            this.editImageButton = (ImageButton) view.findViewById(R.id.imageButton_kentKart_edit);
        }
    }
}
