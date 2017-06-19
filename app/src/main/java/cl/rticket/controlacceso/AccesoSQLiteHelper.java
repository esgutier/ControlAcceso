package cl.rticket.controlacceso;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by esteban on 15-06-2017.
 */

public class AccesoSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreateNominativas = "CREATE TABLE Nominativas(id INTEGER NOT NULL CONSTRAINT pk_nominativa PRIMARY KEY)";
    String sqlCreateNormales    = "CREATE TABLE Normales(id TEXT NOT NULL CONSTRAINT pk_normal PRIMARY KEY)";

    public AccesoSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateNominativas);
        db.execSQL(sqlCreateNormales);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Nominativas");
        db.execSQL("DROP TABLE IF EXISTS Normales");
        db.execSQL(sqlCreateNominativas);
        db.execSQL(sqlCreateNormales);
    }

    public int getCount(String table)
    {
        String selectQuery = "SELECT id FROM "+table;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        c.moveToFirst();
        int total = c.getCount();
        c.close();

        return total;
    }
}
