package com.example.alaa.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Uri mCurrentProductUri;
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText sname;
    private EditText semail;
    private EditText sphonenumber;
    private Button call;
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
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        setTitle(getString(R.string.edit_product));
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

        productName = findViewById(R.id.e1);
        price = findViewById(R.id.e2);
        quantity = findViewById(R.id.e3);
        sname = findViewById(R.id.e4);
        semail = findViewById(R.id.e5);
        sphonenumber = findViewById(R.id.e6);
        call = findViewById(R.id.call);

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
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber = sphonenumber.getText().toString();
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phonenumber));
                startActivity(i);
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
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_q);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIES_EMAIL,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER,
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                columns,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        if (data.moveToFirst()) {
            int ColumnNameIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int ColumnPriceIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int ColumnQuantityIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int ColumnSupplierNameIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int ColumnSupplierEmailIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIES_EMAIL);
            int ColumnSupplierPhoneNumberIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            String ProductName = data.getString(ColumnNameIndex);
            int price = data.getInt(ColumnPriceIndex);
            int quantity = data.getInt(ColumnQuantityIndex);
            String supplierName = data.getString(ColumnSupplierNameIndex);
            String supplierEmail = data.getString(ColumnSupplierEmailIndex);
            String supplierPhoneNumber = data.getString(ColumnSupplierPhoneNumberIndex);

            productName.setText(ProductName);
            this.price.setText(Integer.toString(price));
            this.quantity.setText(Integer.toString(quantity));
            sname.setText(supplierName);
            semail.setText(supplierEmail);
            sphonenumber.setText(supplierPhoneNumber);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productName.setText("");
        price.setText("");
        quantity.setText("");
        sname.setText("");
        semail.setText("");
        sphonenumber.setText("");
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
