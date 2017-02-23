package br.com.kuka.controleassociados.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.kuka.controleassociados.model.AssociadoContract;

/**
 * Created by 01748913506 on 16/02/17.
 */

public class DBAssociadoHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Associado.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " NUMERIC";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE =
            "CREATE TABLE " + AssociadoContract.Associado.TABLE_NAME + " (" +
                    AssociadoContract.Associado._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    AssociadoContract.Associado.COLUMN_NAME_NOME + TEXT_TYPE + COMMA_SEP +
//                    AssociadoContract.Associado.COLUMN_NAME_ATRASO + TEXT_TYPE + COMMA_SEP +
                    AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO + TEXT_TYPE + COMMA_SEP +
                    AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO + TEXT_TYPE + COMMA_SEP +
                    AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO + TEXT_TYPE + COMMA_SEP +
                    AssociadoContract.Associado.COLUMN_NAME_TELEFONE + TEXT_TYPE + " )";

    public DBAssociadoHelper(Context context) {
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
