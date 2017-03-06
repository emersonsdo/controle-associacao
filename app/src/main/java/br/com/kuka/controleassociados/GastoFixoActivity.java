package br.com.kuka.controleassociados;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.kuka.controleassociados.model.GastoFixoContract;
import br.com.kuka.controleassociados.util.DBGastoFixoHelper;

public class GastoFixoActivity extends AppCompatActivity {

    private String idGastoFixoEditado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_fixo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.gasto_fixo_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gastos Fixos");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void salvar(View view){
        EditText etDescricao = (EditText) findViewById(R.id.et_descricao_gasto_fixo);
        String descricao = etDescricao.getText().toString();

        EditText etValor = (EditText) findViewById(R.id.et_valor_gasto_fixo);
        Long valor = Long.parseLong(etValor.getText().toString());

        String data = obterDataAtual();

        ContentValues values = new ContentValues();
        values.put(GastoFixoContract.GastoFixo._ID, idGastoFixoEditado);
        values.put(GastoFixoContract.GastoFixo.COLUMN_NAME_DESCRICAO, descricao);
        values.put(GastoFixoContract.GastoFixo.COLUMN_NAME_VALOR, valor);
        values.put(GastoFixoContract.GastoFixo.COLUMN_NAME_DATA_CRIACAO, data);

        insert(values);
    }

    private void insert(ContentValues values){
        DBGastoFixoHelper dbHelper = new DBGastoFixoHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int numberOfRowAffected = 0;
        Intent intent = new Intent();

        if(values.get(GastoFixoContract.GastoFixo._ID) != null){
            numberOfRowAffected = db.update(GastoFixoContract.GastoFixo.TABLE_NAME, values,
                    GastoFixoContract.GastoFixo._ID + " = " + values.get(GastoFixoContract.GastoFixo._ID), null);

            if(numberOfRowAffected == 0 || numberOfRowAffected > 1){
                setResult(Activity.RESULT_CANCELED, intent);
            }else{
                intent.putExtra("ID",values.get(GastoFixoContract.GastoFixo._ID).toString());
                setResultOk(values, intent);
            }
        }else{
            long newRowId = db.insert(GastoFixoContract.GastoFixo.TABLE_NAME, null, values);

            if(newRowId >= 0){
                intent.putExtra("ID",newRowId);
                setResultOk(values, intent);
            }else{
                setResult(Activity.RESULT_CANCELED, intent);
            }
        }

        idGastoFixoEditado = null;
        finish();
    }

    private void setResultOk(ContentValues values, Intent intent){
        intent.putExtra("DESCRICAO", values.get(GastoFixoContract.GastoFixo.COLUMN_NAME_DESCRICAO).toString());
        intent.putExtra("VALOR", values.get(GastoFixoContract.GastoFixo.COLUMN_NAME_VALOR).toString());
        intent.putExtra("DATA", values.get(GastoFixoContract.GastoFixo.COLUMN_NAME_DATA_CRIACAO).toString());

        setResult(Activity.RESULT_OK, intent);
    }

    private String obterDataAtual(){
        Calendar calendar = new GregorianCalendar();
        Date data = calendar.getTime();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        String dataAtual = format.format(data);

        return dataAtual;
    }
}