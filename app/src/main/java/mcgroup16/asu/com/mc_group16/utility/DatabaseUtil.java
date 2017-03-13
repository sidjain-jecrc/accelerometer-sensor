package mcgroup16.asu.com.mc_group16.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mcgroup16.asu.com.mc_group16.model.Sample;

/**
 * Created by mlenka on 2/21/2017.
 */

public class DatabaseUtil extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private String TABLE_NAME = null;

    public DatabaseUtil(Context context, final String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void createTable(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
        String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + " ( "
                + " timestamp INTEGER, "
                + " x_val REAL, "
                + " y_val REAL, "
                + " z_val REAL"
                + " );";
        this.getWritableDatabase().execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    /* Add a sensor sample to DB*/
    public void addSampleToDB(Sample sensorSample, String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp", sensorSample.getTimestamp());
        values.put("x_val", sensorSample.getX());
        values.put("y_val", sensorSample.getY());
        values.put("z_val", sensorSample.getZ());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /*Get k most recent samples from DB*/
    public List<Sample> getSamplesFromDB(String TABLE_NAME, int k) {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY timestamp DESC LIMIT " + k;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Sample> sampleList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Sample sample = new Sample(cursor.getLong(0),
                    cursor.getDouble(1),
                    cursor.getDouble(2),
                    cursor.getDouble(3)
            );
            sampleList.add(sample);
        }
        return sampleList;
    }
}
