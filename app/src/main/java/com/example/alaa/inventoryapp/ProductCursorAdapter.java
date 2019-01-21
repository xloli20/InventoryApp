package com.example.alaa.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alaa on 2/3/2018.
 */
public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.items, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView product = view.findViewById(R.id.p);
        TextView price = view.findViewById(R.id.pr);
        TextView quantity = view.findViewById(R.id.q);

        int productColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry._ID));

        String productName = cursor.getString(productColumnIndex);
        int priceP = cursor.getInt(priceColumnIndex);
        final int quantityP = cursor.getInt(quantityColumnIndex);
        final Uri uri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);

        product.setText(productName);
        price.setText(String.valueOf(priceP));
        quantity.setText(String.valueOf(quantityP));

        Button sale = view.findViewById(R.id.b);
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityP > 0) {
                    int updatedQuantity = quantityP - 1;
                    ContentValues values = new ContentValues();
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, updatedQuantity);
                    context.getContentResolver().update(uri, values, null, null);
                } else
                    Toast.makeText(context, "Sorry, nothing to sell!", Toast.LENGTH_LONG).show();
            }
        });
    }
}

