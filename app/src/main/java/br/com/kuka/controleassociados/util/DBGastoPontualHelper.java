package br.com.kuka.controleassociados.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.kuka.controleassociados.model.GastoPontualContract;

/**
 * Created by 01748913506 on 03/03/17.
 */

public class DBGastoPontualHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GastoPontual.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " NUMERIC";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE =
            "CREATE TABLE " + GastoPontualContract.GastoPontual.TABLE_NAME + " (" +
                    GastoPontualContract.GastoPontual._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    GastoPontualContract.GastoPontual.COLUMN_NAME_VALOR + NUMBER_TYPE + COMMA_SEP +
                    GastoPontualContract.GastoPontual.COLUMN_NAME_DESCRICAO + TEXT_TYPE + COMMA_SEP +
                    GastoPontualContract.GastoPontual.COLUMN_NAME_DATA_CRIACAO + TEXT_TYPE + COMMA_SEP +
                    GastoPontualContract.GastoPontual.COLUMN_NAME_CONTABILIZADO + TEXT_TYPE + " )";

    public DBGastoPontualHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO
    }

}
