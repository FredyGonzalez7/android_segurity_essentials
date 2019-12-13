package com.example.segurity_essentials_class;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "security.db";
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UID = "uid";

    private static final String TAG = "Fredy";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+TABLE_NAME+
                        "("+COLUMN_ID +"INTEGER PRIMARY KEY, "+COLUMN_NAME+" TEXT, "+COLUMN_EMAIL+" TEXT, "+COLUMN_UID+" TEXT)"
        );

        /*
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + " TEXT PRIMARY KEY, " + COLUMN_EMAIL +
                        " TEXT," + COLUMN_NAME + " TEXT," + COLUMN_UID + " TEXT)"
        );
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
        onCreate(sqLiteDatabase);
    }

    String insertRow(Context context, String uid, String email, String name) {
        //falta verificar que no exista errores en el insert

        if (getUser(context,email)==null){
            /*ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_NAME, name);
            contentValues.put(COLUMN_UID, uid);

            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_NAME, null, contentValues);
            */
            SQLiteDatabase db = this.getWritableDatabase();
            //String query = "INSERT INTO "+TABLE_NAME+" ("+COLUMN_EMAIL+", "+COLUMN_NAME+", "+COLUMN_UID+") VALUES ("+email+","+name+","+uid+")";
            String query = "INSERT INTO user (email,name,uid) VALUES ('"+email+"','"+name+"','"+uid+"')";
            //String query = "INSERT INTO user (name,email,uid) VALUES ("+name+","+email+","+uid+")";
            Log.d(TAG, "insertRow: query " + query);
            db.execSQL(query);
            db.close();
            return "User Register";
        }
        else return null;
    }

    String getUser(Context context, String email) {
        try {
            DBHelper dbHelper = new DBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT name,uid FROM " + TABLE_NAME + " where email = '"+email+"'", null);
            cursor.moveToFirst();
            String name;
            name= cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            cursor.close();
            return name;
        } catch (Exception e) {
            return null;
        }
    }
}
