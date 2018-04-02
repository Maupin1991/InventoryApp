package com.example.android.inventoryapp;

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

import com.example.android.inventoryapp.data.ItemContract.ProductEntry;

/**
 * {@link ItemCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
class ItemCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView productPriceTextView = (TextView) view.findViewById(R.id.product_price);
        TextView productQuantityTextView = (TextView) view.findViewById(R.id.product_quantity);

        // Find the columns of product attributes that we're interested in
        final int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        final int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(productNameColumnIndex);
        Float productPrice = cursor.getFloat(productPriceColumnIndex);
        final Integer productQuantity = cursor.getInt(productQuantityColumnIndex);

        // Update the TextViews with the attributes for the current product
        productNameTextView.setText(productName);
        productPriceTextView.setText(String.valueOf(productPrice));
        productQuantityTextView.setText(String.valueOf(productQuantity));

        final Button saleButton = (Button) view.findViewById(R.id.sale_button);
        if (productQuantity == 0) saleButton.setEnabled(false);
        else saleButton.setEnabled(true);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if there is at least one item to sell
                if (cursor.getInt(productQuantityColumnIndex) >= 0) {
                    ContentValues values = new ContentValues();
                    int updatedProductQuantity = productQuantity - 1;
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, updatedProductQuantity);

                    int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
                    long id = cursor.getLong(idColumnIndex);
                    int rowsAffected = context.getContentResolver().update(Uri.withAppendedPath(ProductEntry.CONTENT_URI, String.valueOf(id)), values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(view.getContext(), R.string.item_not_sold, Toast.LENGTH_LONG).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(view.getContext(), R.string.item_sold, Toast.LENGTH_LONG).show();
                    }

                } else {
                    saleButton.setEnabled(false);
                }
            }
        });

    }
}