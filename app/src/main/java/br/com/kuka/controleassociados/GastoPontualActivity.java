package br.com.kuka.controleassociados;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import br.com.kuka.controleassociados.model.GastoPontualContract;
import br.com.kuka.controleassociados.util.DBGastoFixoHelper;
import br.com.kuka.controleassociados.util.DBGastoPontualHelper;

public class GastoPontualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_pontual);

        Toolbar toolbar = (Toolbar) findViewById(R.id.gasto_pontual_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gastos Pontuais");
    }

    public void salvar(View view){
        EditText etDescricao = (EditText) findViewById(R.id.et_descricao_gasto_pontual);
        String descricao = etDescricao.getText().toString();

        EditText etValor = (EditText) findViewById(R.id.et_valor_gasto_pontual);
        Long valor = Long.parseLong(etValor.getText().toString());

        String data = obterDataAtual();

        ContentValues values = new ContentValues();
        values.put(GastoPontualContract.GastoPontual.COLUMN_NAME_DESCRICAO, descricao);
        values.put(GastoPontualContract.GastoPontual.COLUMN_NAME_VALOR, valor);
        values.put(GastoPontualContract.GastoPontual.COLUMN_NAME_DATA_CRIACAO, data);

        insert(values);
    }

    private void insert(ContentValues values){
        DBGastoPontualHelper dbHelper = new DBGastoPontualHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Intent intent = new Intent();
        long newRowId = db.insert(GastoPontualContract.GastoPontual.TABLE_NAME, null, values);

        if(newRowId >= 0){
            setResult(Activity.RESULT_OK, intent);
            intent.putExtra("ID",newRowId);
            intent.putExtra("DESCRICAO", values.get(GastoFixoContract.GastoFixo.COLUMN_NAME_DESCRICAO).toString());
            intent.putExtra("VALOR", values.get(GastoFixoContract.GastoFixo.COLUMN_NAME_VALOR).toString());
            intent.putExtra("DATA", values.get(GastoFixoContract.GastoFixo.COLUMN_NAME_DATA_CRIACAO).toString());
        }else{
            setResult(Activity.RESULT_CANCELED, intent);
        }

        finish();
    }

    private String obterDataAtual(){
        Calendar calendar = new GregorianCalendar();
        Date data = calendar.getTime();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        String dataAtual = format.format(data);

        return dataAtual;
    }
}
