package com.example.bargo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListProductActivity extends AppCompatActivity {

    private ListView productsListView;
    private int[] productListImage;
    private String[] productListName;
    private String[] productListUnit;
    private int[] productListPoint;
    private int[] loQueQueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        productsListView = findViewById(R.id.productsList);

        productListImage = ProductListInfo.getInstance().getImages();
        productListName = ProductListInfo.getInstance().getProductos();
        productListUnit = ProductListInfo.getInstance().getUnidades();
        productListPoint = ProductListInfo.getInstance().getPoints();
        loQueQueda = ProductListInfo.getInstance().getLoQueQueda();

        //creat an adapter class
        ProductListAdapter productListAdapter = new ProductListAdapter();
        productsListView.setAdapter(productListAdapter);
    }

    public class ProductListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return productListImage.length;
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
        public View getView(final int position, View covertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.layout_list_product_item, null);
            ImageView mImageView = view.findViewById(R.id.productImage);
            TextView mNameView = view.findViewById(R.id.productName);
            TextView mCountView = view.findViewById(R.id.productCount);
            TextView mPointView = view.findViewById(R.id.productPoints);

            mImageView.setImageResource(productListImage[position]);
            mNameView.setText(productListName[position]);
            String aux = Integer.toString(loQueQueda[position]) + productListUnit[position];
            mCountView.setText(aux);
            String points = Integer.toString(productListPoint[position]) + " puntos";
            mPointView.setText(points);

            Button obtenerButton = view.findViewById(R.id.obtenerButton);

            obtenerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( !ProductListInfo.getInstance().checkValue(position)) {
                        Toast.makeText(ListProductActivity.this, R.string.noObtener, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final AlertDialog dialog = new AlertDialog.Builder(ListProductActivity.this)
                                .setTitle("Canjear")
                                .setMessage("Estás seguro de que deseas canjear este producto?")
                                .setPositiveButton("Si", null)
                                .setNegativeButton("No", null)
                                .show();
                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ProductListInfo.getInstance().changeValue(position);
                                dialog.cancel();
                                RetosInfo.getInstance().setContext(getBaseContext());
                                RetosInfo.getInstance().changeChallenges("PROD");
                                Intent mi_product_activity = new Intent(ListProductActivity.this, ListProductActivity.class);
                                startActivity(mi_product_activity);
                                finish();
                            }
                        });
                    }
                }
            });
            return view;
        }
    }
}