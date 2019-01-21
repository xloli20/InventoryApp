package com.example.alaa.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText sname;
    private EditText semail;
    private EditText sphonenumber;

    private boolean mProductHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setTitle(getString(R.string.new_product));
        invalidateOptionsMenu();

        productName = findViewById(R.id.e1);
        price = findViewById(R.id.e2);
        quantity = findViewById(R.id.e3);
        sname = findViewById(R.id.e4);
        semail = findViewById(R.id.e5);
        sphonenumber = findViewById(R.id.e6);

        productName.setOnTouchListener(mTouchListener);
        price.setOnTouchListener(mTouchListener);
        quantity.setOnTouchListener(mTouchListener);
        sname.setOnTouchListener(mTouchListener);
        semail.setOnTouchListener(mTouchListener);
        sphonenumber.setOnTouchListener(mTouchListener);

        Button m = findViewById(R.id.minus);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity.getText().toString().isEmpty()) {
                    quantity.setText("0");
                }
                int q = Integer.parseInt(quantity.getText().toString());
                if (q > 0) {
                    q--;
                    quantity.setText(String.valueOf(q));
                }
            }
        });
        Button p = findViewById(R.id.plus);
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity.getText().toString().isEmpty()) {
                    quantity.setText("0");
                }
                int q = Integer.parseInt(quantity.getText().toString());
                if (q >= 0) {
                    q++;
                    quantity.setText(String.valueOf(q));
                }
            }
        });
    }
    private void saveProduct() {
        String productString = productName.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        String quantityString = quantity.getText().toString().trim();
        String snameString = sname.getText().toString().trim();
        String semailString = semail.getText().toString().trim();
        String sphoneString = sphonenumber.getText().toString().trim();
        if (productString.length() == 0) {
            productName.requestFocus();
            productName.setError("FIELD CANNOT BE EMPTY");
            Toast.makeText(this, getString(R.string.required_msg), Toast.LENGTH_SHORT).show();
        }
        if (priceString.length() == 0) {
            price.requestFocus();
            price.setError("FIELD CANNOT BE EMPTY");
            Toast.makeText(this, getString(R.string.required_msg), Toast.LENGTH_SHORT).show();
        }
        if (quantityString.length() == 0) {
            quantity.requestFocus();
            quantity.setError("FIELD CANNOT BE EMPTY");
            Toast.makeText(this, getString(R.string.required_msg), Toast.LENGTH_SHORT).show();
        }
        if (snameString.length() == 0) {
            sname.requestFocus();
            sname.setError("FIELD CANNOT BE EMPTY");
            Toast.makeText(this, getString(R.string.required_msg), Toast.LENGTH_SHORT).show();
        }
        if (semailString.length() == 0) {
            semail.requestFocus();
            semail.setError("FIELD CANNOT BE EMPTY");
            Toast.makeText(this, getString(R.string.required_msg), Toast.LENGTH_SHORT).show();
        }
        if (sphoneString.length() == 0) {
            sphonenumber.requestFocus();
            sphonenumber.setError("FIELD CANNOT BE EMPTY");
            Toast.makeText(this, getString(R.string.required_msg), Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productString);
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, snameString);
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIES_EMAIL, semailString);
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, sphoneString);

            int PRICE = 0;
            if (!TextUtils.isEmpty(priceString)) {
                PRICE = Integer.parseInt(priceString);
            }
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, PRICE);
            int QUANTITY = 0;
            if (!TextUtils.isEmpty(quantityString)) {
                QUANTITY = Integer.parseInt(quantityString);
            }
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, QUANTITY);

            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_delete);
        menuItem.setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
