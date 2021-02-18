package com.example.bargo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ListBarFragment extends Fragment {

    private ListView barsListView;
    private BarListInfo barsListInfo = new BarListInfo();
    private int[] barsListImages;
    private String[] barsListNames;
    private String[] barsListAdresses;
    private Boolean[] barsListVisited;
    private TextView mapButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_bar, container, false);

        barsListImages = barsListInfo.getImages();
        barsListNames = barsListInfo.getBars();
        barsListAdresses = barsListInfo.getAdresses();
        barsListVisited = barsListInfo.getVisited();

        barsListView = view.findViewById(R.id.barsList);
        AdaptadorListBar adaptador = new AdaptadorListBar();
        barsListView.setAdapter(adaptador);

        mapButton = view.findViewById(R.id.button1);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = new ImageView(getContext());
                image.setImageResource(R.drawable.map);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).
                                setView(image);
                builder.create().show();
            }
        });
        return  view;
    }



    class AdaptadorListBar extends BaseAdapter {

        @Override
        public int getCount() {
            return barsListImages.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.bar_item_list, null);
            ImageView barImage = view.findViewById(R.id.barImage);
            TextView barName = view.findViewById(R.id.barNameText);
            TextView barAdress = view.findViewById(R.id.adressText);
            CheckBox barVisited = view.findViewById(R.id.visitedCheckBox);
            Button infoButton = view.findViewById(R.id.infoButton);

            barImage.setImageResource(barsListImages[position]);
            barName.setText(barsListNames[position]);
            barAdress.setText(barsListAdresses[position]);
            barVisited.setChecked(barsListVisited[position]);

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) { //solo bar Casa Pepe
                        Intent intent = new Intent(getActivity(), InfoBarActivity.class);
                        startActivity(intent);
                    }

                }
            });

            return view;
        }
    }
}

