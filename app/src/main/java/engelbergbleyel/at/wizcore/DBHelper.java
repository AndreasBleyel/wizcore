package engelbergbleyel.at.wizcore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andi on 02.12.16.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Wizcore.db";
    public static final String PLAYERS_TABLE_NAME = "players";
    public static final String PLAYERS_COLUMN_ID = "id";
    public static final String PLAYERS_COLUMN_NAME = "name";
    public static final String PLAYERS_COLUMN_HIGHSCORE = "highscore";
    public static final String PLAYERS_COLUMN_ALLTIMESCORE = "alltimescore";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + PLAYERS_TABLE_NAME +
                        "(" + PLAYERS_COLUMN_ID + " integer primary key, " + PLAYERS_COLUMN_NAME + " text, " + PLAYERS_COLUMN_HIGHSCORE + " int," + PLAYERS_COLUMN_ALLTIMESCORE + " int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+PLAYERS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYERS_COLUMN_NAME, name);
        contentValues.put(PLAYERS_COLUMN_HIGHSCORE, 0);
        contentValues.put(PLAYERS_COLUMN_ALLTIMESCORE, 0);
        db.insert(PLAYERS_TABLE_NAME, null, contentValues);
        return true;
    }


    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PLAYERS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYERS_COLUMN_NAME, name);

        db.update(PLAYERS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean updateAllTimeScore(Integer id, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Integer allTimeScore = getAllTimeScore(id) + score;
        contentValues.put(PLAYERS_COLUMN_ALLTIMESCORE, allTimeScore);

        db.update(PLAYERS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean updateHighScore(Integer id, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYERS_COLUMN_HIGHSCORE, score);

        db.update(PLAYERS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PLAYERS_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+PLAYERS_TABLE_NAME+" where "+PLAYERS_COLUMN_ID+"=" + id, null);
        return res;
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+PLAYERS_TABLE_NAME+ " order by "+PLAYERS_COLUMN_NAME, null);
        return res;
    }

    public Integer getAllTimeScore(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+PLAYERS_TABLE_NAME+ " where "+PLAYERS_COLUMN_ID+" = " +id, null);
        res.moveToFirst();

        return Integer.parseInt(res.getString(res.getColumnIndex(PLAYERS_COLUMN_ALLTIMESCORE)));

    }

    public Cursor getHighscore(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        if(id == -1){
            res = db.rawQuery("select "+PLAYERS_COLUMN_NAME+" , "+PLAYERS_COLUMN_HIGHSCORE+" from "+PLAYERS_TABLE_NAME+ " order by "+PLAYERS_COLUMN_HIGHSCORE+ " desc", null);
        }else{
            res = db.rawQuery("select "+PLAYERS_COLUMN_NAME+" , "+PLAYERS_COLUMN_HIGHSCORE+" from "+PLAYERS_TABLE_NAME+" where "+PLAYERS_COLUMN_ID+" = " + id, null);
        }

        return res;
    }

    public ArrayList<String> getAllContacts() {
        ArrayList<String> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+PLAYERS_TABLE_NAME+ " order by "+PLAYERS_COLUMN_ID+" asc", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            array_list.add(res.getString(res.getColumnIndex(PLAYERS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Integer> getAllIds() {
        ArrayList<Integer> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+PLAYERS_TABLE_NAME+ " order by "+PLAYERS_COLUMN_ID+" asc", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            array_list.add(Integer.parseInt(res.getString(res.getColumnIndex(PLAYERS_COLUMN_ID))));
            res.moveToNext();
        }
        return array_list;
    }
}
