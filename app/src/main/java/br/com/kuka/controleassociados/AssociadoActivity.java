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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.kuka.controleassociados.date.AssociationDatePickerFragment;
import br.com.kuka.controleassociados.date.BirthDatePickerFragment;
import br.com.kuka.controleassociados.date.LastPaymentDatePickerFragment;
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

    public void salvar(View view) {
//        TODO: Verificar atributos obrigatorios
        EditText etNome = (EditText) findViewById(R.id.et_nome);
        String nomeCadastrado = etNome.getText().toString();

        EditText etDataNascimento = (EditText) findViewById(R.id.et_data_nascimento);
        String nascimento = etDataNascimento.getText().toString();

        EditText etUltimoPagamento = (EditText) findViewById(R.id.et_data_ultimo_pagamento);
        String ultimoPagamento = etUltimoPagamento.getText().toString();

        EditText etAssociacao = (EditText) findViewById(R.id.et_data_associacao);
        String associacao = etAssociacao.getText().toString();

        EditText etTelefone = (EditText) findViewById(R.id.et_telefone);
        String telefoneCadastrado = etTelefone.getText().toString();

        ContentValues values = new ContentValues();
        values.put(AssociadoContract.Associado._ID, idAssociadoEditado);
        values.put(AssociadoContract.Associado.COLUMN_NAME_NOME, nomeCadastrado);
        values.put(AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO, nascimento);
        values.put(AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO, ultimoPagamento);
        values.put(AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO, associacao);
        values.put(AssociadoContract.Associado.COLUMN_NAME_TELEFONE, telefoneCadastrado);

        insert(values);
    }

    private void editar(Intent intent) {
        String nomeAssociadoEditado = intent.getStringExtra("NOME");
        Associado associado = obterAssociadoPorNome(nomeAssociadoEditado);

        EditText etNome = (EditText) findViewById(R.id.et_nome);
        etNome.setText(associado.nome);

        CheckBox cbEmAtraso = (CheckBox) findViewById(R.id.cb_em_atraso);
        cbEmAtraso.setChecked(associado.emAtraso);

        if(associado.dataNascimento != null){
            EditText etDataNascimento = (EditText) findViewById(R.id.et_data_nascimento);
            etDataNascimento.setText(format.format(associado.dataNascimento));
        }

        if(associado.dataUltimoPagamento != null){
            EditText etUltimoPagamento = (EditText) findViewById(R.id.et_data_ultimo_pagamento);
            etUltimoPagamento.setText(format.format(associado.dataUltimoPagamento));
        }

        if(associado.dataAssociacao != null){
            EditText etAssociacao = (EditText) findViewById(R.id.et_data_associacao);
            etAssociacao.setText(format.format(associado.dataAssociacao));
        }

        EditText etTelefone = (EditText) findViewById(R.id.et_telefone);
        etTelefone.setText(associado.telefone);
    }

    public void deletar(View view){
        EditText etNome = (EditText) findViewById(R.id.et_nome);
        String nomeCadastrado = etNome.getText().toString();

        Associado associado = obterAssociadoPorNome(nomeCadastrado);

        if(associado != null){
            DBAssociadoHelper dbHelper = new DBAssociadoHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.execSQL("DELETE FROM "+ AssociadoContract.Associado.TABLE_NAME + " WHERE " +
                    AssociadoContract.Associado._ID + " = "+associado._ID+"");
            db.close();

            Intent intentDeletar = new Intent(this, MainActivity.class);
            intentDeletar.putExtra("ID_DELETADO", associado._ID);
            startActivity(intentDeletar);
        }else{
            Intent intentDeletar = new Intent(this, MainActivity.class);
            intentDeletar.putExtra("ID_DELETADO", Long.parseLong("-1"));
            startActivity(intentDeletar);
        }
    }

    private Associado obterAssociadoPorNome(String nomeAssociado){

        DBAssociadoHelper dbHelper = new DBAssociadoHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String [] dadosAssociado = {
                AssociadoContract.Associado._ID,
                AssociadoContract.Associado.COLUMN_NAME_NOME,
                AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO,
                AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO,
                AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO,
                AssociadoContract.Associado.COLUMN_NAME_TELEFONE
        };

        String whereClause = AssociadoContract.Associado.COLUMN_NAME_NOME+"=?";
        String [] whereArgs = {nomeAssociado};

        Cursor cursor = db.query(AssociadoContract.Associado.TABLE_NAME,
                dadosAssociado, whereClause, whereArgs, null, null, null);

        listAssociados = new ArrayList<>();
        Associado associado = null;

        while (cursor.moveToNext()) {
            associado = new Associado();

            associado._ID = Long.parseLong(cursor.getString(0));
            idAssociadoEditado = String.valueOf(associado._ID);
            associado.nome = cursor.getString(1);

            if (cursor.getString(2) != null && !cursor.getString(2).isEmpty()) {
                try {
                    Date dataNascimento = format.parse(cursor.getString(2));
                    associado.dataNascimento = dataNascimento;
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ACTIVITY", "Erro no parser da data de nascimento!");
                    e.printStackTrace();
                }
            }

            if (cursor.getString(3) != null && !cursor.getString(3).isEmpty()) {
                try {
                    Date dataUltimoPagamento = format.parse(cursor.getString(3));
                    associado.dataUltimoPagamento = dataUltimoPagamento;

                    Calendar dataAtual = new GregorianCalendar();
                    Calendar dataApurada = new GregorianCalendar();
                    dataApurada.setTime(associado.dataUltimoPagamento);

                    if(dataAtual.get(Calendar.MONTH) > dataApurada.get(Calendar.MONTH) || dataAtual.get(Calendar.YEAR) > dataApurada.get(Calendar.YEAR)){
                        associado.emAtraso = true;
                    }else{
                        associado.emAtraso = false;
                    }
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ACTIVITY", "Erro no parser da data do ultimo pagamento!");
                    e.printStackTrace();
                }
            }

            if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
                try {
                    Date dataAssociacao = format.parse(cursor.getString(4));
                    associado.dataAssociacao = dataAssociacao;
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ACTIVITY", "Erro no parser da data de associaçao!");
                    e.printStackTrace();
                }
            }

            associado.telefone = cursor.getString(5);
            listAssociados.add(associado);
        }
        cursor.close();
        db.close();

//        TODO: rever while e retorno
        return associado;
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
        intent.putExtra("DATA_NASCIMENTO", values.get(AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO).toString());
        intent.putExtra("DATA_ULTIMO_PAGAMENTO", values.get(AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO).toString());
        intent.putExtra("DATA_ASSOCIACAO", values.get(AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO).toString());
        intent.putExtra("TELEFONE", values.get(AssociadoContract.Associado.COLUMN_NAME_TELEFONE).toString());

        setResult(Activity.RESULT_OK, intent);
    }
}
