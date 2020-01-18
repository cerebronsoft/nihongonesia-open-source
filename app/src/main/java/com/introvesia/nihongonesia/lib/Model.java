package com.introvesia.nihongonesia.lib;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

/**
 * Created by asus on 30/06/2017.
 */

public abstract class Model {
    protected abstract String tableName();

    protected long insert(ContentValues contentValues){
        return SQLiteDb.getConnection().insert(tableName(), null, contentValues);
    }

    protected int update(String columnName, String value, ContentValues contentValues){
        return SQLiteDb.getConnection().update(tableName(), contentValues, columnName + " = ? ", new String[]{value});
    }

    protected String getStringValue(Cursor res, String column_name) {
        if (!res.isNull(res.getColumnIndex(column_name))) {
            return res.getString(res.getColumnIndex(column_name));
        }
        return "";
    }

    protected int getIntegerValue(Cursor res, String column_name) {
        if (!res.isNull(res.getColumnIndex(column_name))) {
            return res.getInt(res.getColumnIndex(column_name));
        }
        return 0;
    }

    protected int numberOfRows() {
        return (int) DatabaseUtils.queryNumEntries(SQLiteDb.getConnection(), tableName());
    }

    protected int getCountOfRowsJoin(String select, String alias, String join, String criteria) {
        String sql = "SELECT " + select + " FROM " + tableName() + " AS " + alias + " " + join + " WHERE " + criteria;
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        return res.getCount();
    }

    protected int getCountOfRows(String criteria) {
        String sql = "SELECT * FROM " + tableName() + " WHERE " + criteria;
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        return res.getCount();
    }

    protected Cursor getData(String columnName, String value){
        String sql = "SELECT * FROM " + tableName() + " " +
                "WHERE " + columnName + " = '" + value + "'";
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        if (res.getCount() == 0)
            return null;
        return res;
    }

    protected Cursor getDataJoin(String select, String alias, String join, String criteria){
        String sql = "SELECT " + select + " FROM " + tableName() + " AS " + alias + " " + join + " WHERE " + criteria;
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        if (res.getCount() == 0)
            return null;
        return res;
    }

    protected boolean isExists(String columnName, String value){
        String sql = "SELECT * FROM " + tableName() + " " +
                "WHERE " + columnName + " = '" + value + "'";
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        return res.getCount() > 0;
    }

    protected Cursor getData(String criteria){
        String sql = "SELECT * FROM " + tableName() + " WHERE " + criteria;
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        if (res.getCount() == 0)
            return null;
        return res;
    }

    protected Cursor getDataBySql(String sql){
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        if (res.getCount() == 0)
            return null;
        return res;
    }

    protected int sumJoin(String columnName, String alias, String join, String criteria){
        String sql = "SELECT SUM(" + columnName + ") AS sum FROM " + tableName() + " AS " + alias + " " + join + " WHERE " + criteria;
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        return getIntegerValue(res, "sum");
    }

    protected Cursor getAll(String criteria){
        String sql = "SELECT * FROM " + tableName() + " WHERE " + criteria;
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        return res;
    }

    protected Cursor getAllJoin(String select, String alias, String join, String criteria){
        String sql = "SELECT " + select + " FROM " + tableName() + " AS " + alias + " " + join + " WHERE " + criteria;
        Cursor res = SQLiteDb.getConnection().rawQuery(sql, null);
        res.moveToFirst();
        return res;
    }

    protected Integer delete(String columnName, String value) {
        return SQLiteDb.getConnection().delete(tableName(),
                columnName + " = ? ",
                new String[]{value});
    }

    public int delete() {
        return SQLiteDb.getConnection().delete(tableName(), "1", new String[]{});
    }
}
