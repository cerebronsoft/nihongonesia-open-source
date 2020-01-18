package com.introvesia.nihongonesia.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by asus on 30/06/2017.
 */

public class SQLiteDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "data.db";
    public static final String TAG_NAME = "Nihongonesia";
    private static SQLiteDatabase connection = null;
    private Context context;
    private static String dir;
    private static String path;
    private static String temp_path;

    public SQLiteDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        dir = "/data/user/0/" + context.getApplicationContext().getPackageName();
        path = dir + "/" + DATABASE_NAME;
        temp_path = dir + "/temp.db";
    }

    public static String getDir() {
        return dir;
    }

    public static String getPath() {
        return path;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    public static SQLiteDatabase getConnection(){
        return connection;
    }

    public static void setConnection(SQLiteDatabase connection) {
        SQLiteDb.connection = connection;
    }

    public void connect(boolean is_copy) {
        if (!checkDatabase()) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (Exception ex) {
                Log.e(TAG_NAME, ex.getMessage());
            }
        } else if (is_copy) {
            try {
                copyDatabase();
            } catch (Exception ex) {
                Log.e(TAG_NAME, ex.getMessage());
            }
        }
        if (connection == null)
            connection = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copyDatabase() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        OutputStream myOutput = new FileOutputStream(path);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase loadTemp() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        OutputStream myOutput = new FileOutputStream(temp_path);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

        return SQLiteDatabase.openDatabase(temp_path, null, SQLiteDatabase.OPEN_READONLY);
    }

    private boolean checkDatabase() {
        File file = new File(path);
        if (!file.exists())
            return false;
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public boolean deleteTemp() {
        File file = new File(temp_path);
        return file.delete();
    }

    public boolean setTempAsBase() {
        File file = new File(temp_path);
        File base_file = new File(path);
        if (file.renameTo(base_file)) {
            return base_file.delete();
        }
        return false;
    }
}
