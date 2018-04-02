package com.example.android.inventoryapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp.data.ItemContract.ProductEntry;

public class ItemDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "store.db";
    private static final int DB_VERSION = 1;


    public ItemDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER, " +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, " +
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL DEFAULT 0, " +
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL DEFAULT 0);";
        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
