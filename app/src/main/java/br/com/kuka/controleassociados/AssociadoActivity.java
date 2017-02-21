package br.com.kuka.controleassociados;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.kuka.controleassociados.model.Associado;
import br.com.kuka.controleassociados.model.AssociadoContract;
import br.com.kuka.controleassociados.util.DBAssociadoHelper;

public class AssociadoActivity extends AppCompatActivity {

    String idAssociadoEditado = null;
    ArrayList<Associado> listAssociados;
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associado);

        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra("NOME") != null && !intent.getStringExtra("NOME").isEmpty()){
                editar(intent);
            }
        }
    }

    public void cadastrar(View view) {
//        DBAssociadoHelper dbHelper = new DBAssociadoHelper(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();

        EditText etNome = (EditText) findViewById(R.id.et_nome);
        String nomeCadastrado = etNome.getText().toString();

        CheckBox cbEmAtraso = (CheckBox) findViewById(R.id.cb_em_atraso);
        Boolean emAtraso = cbEmAtraso.isChecked();

        EditText etDataNascimento = (EditText) findViewById(R.id.et_data_nascimento);
        String nascimento = etDataNascimento.getText().toString();

        EditText etUltimoPagamento = (EditText) findViewById(R.id.et_data_ultimo_pagamento);
        String ultimoPagamento = etUltimoPagamento.getText().toString();

        EditText etAssociacao = (EditText) findViewById(R.id.et_data_associacao);
        String associacao = etAssociacao.getText().toString();

        ContentValues values = new ContentValues();
        values.put(AssociadoContract.Associado._ID, idAssociadoEditado);
        values.put(AssociadoContract.Associado.COLUMN_NAME_NOME, nomeCadastrado);
        values.put(AssociadoContract.Associado.COLUMN_NAME_ATRASO, emAtraso);
        values.put(AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO, nascimento);
        values.put(AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO, ultimoPagamento);
        values.put(AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO, associacao);

        insert(values);

//        long newRowId = db.insert(
//                AssociadoContract.Associado.TABLE_NAME,
//                null,
//                values);
//
//        Intent intent = new Intent();
//
//        if(newRowId >= 0){
//            intent.putExtra("ID",newRowId);
//            intent.putExtra("NOME", nomeCadastrado);
//            intent.putExtra("EM_ATRASO", emAtraso);
//            intent.putExtra("DATA_NASCIMENTO", nascimento);
//            intent.putExtra("DATA_ULTIMO_PAGAMENTO", ultimoPagamento);
//            intent.putExtra("DATA_ASSOCIACAO", associacao);
//
//            setResult(Activity.RESULT_OK, intent);
//        }else{
//            setResult(Activity.RESULT_CANCELED, intent);
//        }
//
//        finish();
    }

    public void showBirthDatePickerDialog(View v) {
        DialogFragment newFragment = new BirthDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showLastPaymentDatePickerDialog(View v) {
        DialogFragment newFragment = new LastPaymentDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showAssociationDatePickerDialog(View v){
        DialogFragment newFragment = new AssociationDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void editar(Intent intent) {
        String nomeAssociadoEditado = intent.getStringExtra("NOME");
        DBAssociadoHelper dbHelper = new DBAssociadoHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + AssociadoContract.Associado._ID +
                ", " + AssociadoContract.Associado.COLUMN_NAME_NOME +
                ", " + AssociadoContract.Associado.COLUMN_NAME_ATRASO +
                ", " + AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO +
                ", " + AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO +
                ", " + AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO +
                " FROM " + AssociadoContract.Associado.TABLE_NAME +
                " WHERE " + AssociadoContract.Associado.COLUMN_NAME_NOME + " = '" + nomeAssociadoEditado +"'";


        Cursor cursor = db.rawQuery(query, null);

        listAssociados = new ArrayList<>();

        while (cursor.moveToNext()) {

            idAssociadoEditado = cursor.getString(0);
            Log.i("ASSOCIADO_ACTIVITY", "Cursor 1: " + cursor.getString(1));
            Log.i("ASSOCIADO_ACTIVITY", "Cursor 2: " + cursor.getString(2));
            Log.i("ASSOCIADO_ACTIVITY", "Cursor 3: " + cursor.getString(3));
            Log.i("ASSOCIADO_ACTIVITY", "Cursor 4: " + cursor.getString(4));
            Log.i("ASSOCIADO_ACTIVITY", "Cursor 5: " + cursor.getString(5));
            Log.i("ASSOCIADO_ACTIVITY", "Cursor 0: " + cursor.getString(0));

            Associado associado = new Associado();
            associado.nome = cursor.getString(1);

            if (cursor.getString(2).equalsIgnoreCase("1")) {
                associado.emAtraso = true;
            } else {
                associado.emAtraso = false;
            }

            if (cursor.getString(3) != null && !cursor.getString(3).isEmpty()) {
                try {
                    Date dataNascimento = format.parse(cursor.getString(3));
                    associado.dataNascimento = dataNascimento;
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ACTIVITY", "Erro no parser da data de nascimento!");
                    e.printStackTrace();
                }
            }

            if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
                try {
                    Date dataUltimoPagamento = format.parse(cursor.getString(4));
                    associado.dataUltimoPagamento = dataUltimoPagamento;
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ACTIVITY", "Erro no parser da data do ultimo pagamento!");
                    e.printStackTrace();
                }
            }

            if (cursor.getString(5) != null && !cursor.getString(5).isEmpty()) {
                try {
                    Date dataAssociacao = format.parse(cursor.getString(5));
                    associado.dataAssociacao = dataAssociacao;
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ACTIVITY", "Erro no parser da data de associaçao!");
                    e.printStackTrace();
                }
            }

            listAssociados.add(associado);
        }
        cursor.close();
        db.close();

        Associado associado = listAssociados.get(0);

        EditText etNome = (EditText) findViewById(R.id.et_nome);
        etNome.setText(associado.nome);

        CheckBox cbEmAtraso = (CheckBox) findViewById(R.id.cb_em_atraso);
        cbEmAtraso.setChecked(associado.emAtraso);

        EditText etDataNascimento = (EditText) findViewById(R.id.et_data_nascimento);
        etDataNascimento.setText(format.format(associado.dataNascimento));

        EditText etUltimoPagamento = (EditText) findViewById(R.id.et_data_ultimo_pagamento);
        etUltimoPagamento.setText(format.format(associado.dataUltimoPagamento));

        EditText etAssociacao = (EditText) findViewById(R.id.et_data_associacao);
        etAssociacao.setText(format.format(associado.dataAssociacao));
    }

    private void insert(ContentValues values){
        DBAssociadoHelper dbHelper = new DBAssociadoHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int numberOfRowAffected = 0;
        Intent intent = new Intent();

        if(values.get(AssociadoContract.Associado._ID) != null){
            numberOfRowAffected = db.update(AssociadoContract.Associado.TABLE_NAME, values,
                    AssociadoContract.Associado._ID + " = " + values.get(AssociadoContract.Associado._ID), null);

            if(numberOfRowAffected == 0 || numberOfRowAffected > 1){
                setResult(Activity.RESULT_CANCELED, intent);
            }else{
                intent.putExtra("ID",values.get(AssociadoContract.Associado._ID).toString());
                setResultOk(values, intent);
            }
        }else{
            long newRowId = db.insert(AssociadoContract.Associado.TABLE_NAME, null, values);

            if(newRowId >= 0){
                intent.putExtra("ID",newRowId);
                setResultOk(values, intent);
            }else{
                setResult(Activity.RESULT_CANCELED, intent);
            }
        }

        idAssociadoEditado = null;
        finish();
    }

    private void setResultOk(ContentValues values, Intent intent){
        intent.putExtra("NOME", values.get(AssociadoContract.Associado.COLUMN_NAME_NOME).toString());
        intent.putExtra("EM_ATRASO", (Boolean) values.get(AssociadoContract.Associado.COLUMN_NAME_ATRASO));
        intent.putExtra("DATA_NASCIMENTO", values.get(AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO).toString());
        intent.putExtra("DATA_ULTIMO_PAGAMENTO", values.get(AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO).toString());
        intent.putExtra("DATA_ASSOCIACAO", values.get(AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO).toString());

        setResult(Activity.RESULT_OK, intent);
    }
}
