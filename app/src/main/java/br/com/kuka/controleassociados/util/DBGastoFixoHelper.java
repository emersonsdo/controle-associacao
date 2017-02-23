package br.com.kuka.controleassociados.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.kuka.controleassociados.model.GastoFixoContract;

/**
 * Created by 01748913506 on 23/02/17.
 */

public class DBGastoFixoHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GastoFixo.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " NUMERIC";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE =
            "CREATE TABLE " + GastoFixoContract.GastoFixo.TABLE_NAME + " (" +
                    GastoFixoContract.GastoFixo._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    GastoFixoContract.GastoFixo.COLUMN_NAME_VALOR + NUMBER_TYPE + COMMA_SEP +
                    GastoFixoContract.GastoFixo.COLUMN_NAME_DESCRICAO + TEXT_TYPE + COMMA_SEP +
                    GastoFixoContract.GastoFixo.COLUMN_NAME_DATA_CRIACAO + TEXT_TYPE + " )";

    public DBGastoFixoHelper(Context context) {
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
