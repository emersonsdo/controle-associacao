package br.com.kuka.controleassociados.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.kuka.controleassociados.R;
import br.com.kuka.controleassociados.model.Associado;
import br.com.kuka.controleassociados.model.AssociadoContract;
import br.com.kuka.controleassociados.util.DBAssociadoHelper;

/**
 * Created by 01748913506 on 16/02/17.
 */

public class ListAssociadosAdapter extends BaseAdapter {

    ArrayList<Associado> listaAssociados;
    LayoutInflater inflater;
    DBAssociadoHelper dbAssociadoHelper;
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public ListAssociadosAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.dbAssociadoHelper = new DBAssociadoHelper(context);
        this.listaAssociados = getListaAssociados();
    }

    public ArrayList<Associado> getListaAssociados(){
        SQLiteDatabase db = dbAssociadoHelper.getReadableDatabase();

//        String query = "SELECT " + AssociadoContract.Associado._ID +
//                ", " + AssociadoContract.Associado.COLUMN_NAME_NOME +
//                ", " + AssociadoContract.Associado.COLUMN_NAME_ATRASO +
//                ", " + AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO +
//                ", " + AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO +
//                ", " + AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO +
//                " FROM " + AssociadoContract.Associado.TABLE_NAME;
//
//        Cursor cursor = db.rawQuery(query, null);

        String [] dadosAssociado = {
                AssociadoContract.Associado._ID,
                AssociadoContract.Associado.COLUMN_NAME_NOME,
                AssociadoContract.Associado.COLUMN_NAME_DATA_NASCIMENTO,
                AssociadoContract.Associado.COLUMN_NAME_DATA_ULTIMO_PAGAMENTO,
                AssociadoContract.Associado.COLUMN_NAME_DATA_ASSOCIACAO,
                AssociadoContract.Associado.COLUMN_NAME_TELEFONE
        };

        Cursor cursor = db.query(AssociadoContract.Associado.TABLE_NAME,
                dadosAssociado, null, null, null, null, null);

        listaAssociados = new ArrayList<>();

        while (cursor.moveToNext()) {
            Associado associado = new Associado();

            associado._ID = Long.parseLong(cursor.getString(0));
            associado.nome = cursor.getString(1);

            if(cursor.getString(2) != null && !cursor.getString(2).isEmpty()){
                try {
                    Date dataNascimento = format.parse(cursor.getString(2));
                    associado.dataNascimento = dataNascimento;
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ADAPTER", "Erro no parser da data de nascimento!");
                    e.printStackTrace();
                }
            }

            if(cursor.getString(3) != null && !cursor.getString(3).isEmpty()){
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
                    Log.i("ASSOCIADO_ADAPTER", "Erro no parser da data do ultimo pagamento!");
                    e.printStackTrace();
                }
            }

            if(cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
                try {
                    Date dataAssociacao = format.parse(cursor.getString(4));
                    associado.dataAssociacao = dataAssociacao;
                } catch (ParseException e) {
                    Log.i("ASSOCIADO_ADAPTER", "Erro no parser da data de associaçao!");
                    e.printStackTrace();
                }
            }

            associado.telefone = cursor.getString(5);

            listaAssociados.add(associado);
        }

        cursor.close();
        db.close();

        return listaAssociados;
    }

    public void addAssociado(Associado associado){
        listaAssociados.add(associado);
    }

    public void deletarAssociado(Long idDeletado){

        for(Associado associado : listaAssociados){
            if(associado._ID == idDeletado){
                Log.i("ADAPTER", "ID do Associado removido: " + associado._ID);
                listaAssociados.remove(associado);
                break;
            }
        }
    }

    @Override
    public int getCount() {
        SQLiteDatabase db = dbAssociadoHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, AssociadoContract.Associado.TABLE_NAME);
        db.close();

        return (int) count;

//        return listaAssociados.size();
    }

    @Override
    public Associado getItem(int position) {
        return listaAssociados.get(position);
    }

    @Override
    public long getItemId(int i) {
        return listaAssociados.get(i)._ID;
    }

    public Associado getItemById(Long id){
        for(Associado associado : listaAssociados){
            if(associado._ID == id){
                return associado;
            }
        }
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.list_view_associados, null);

        TextView tvNome = (TextView) view.findViewById(R.id.tv_nome);
        CheckedTextView ctvSemAtraso = (CheckedTextView) view.findViewById(R.id.ctv_sem_atraso);
        CheckedTextView ctvComAtraso = (CheckedTextView) view.findViewById(R.id.ctv_com_atraso);
        TextView tvDataNascimento = (TextView) view.findViewById(R.id.tv_data_nascimento);
        TextView tvDataUltimoPagamento = (TextView) view.findViewById(R.id.tv_data_ultimo_pagamento);
        TextView tvDataAssociacao = (TextView) view.findViewById(R.id.tv_data_associacao);
        TextView tvTelefone = (TextView) view.findViewById(R.id.tv_telefone);

        Associado associado = getItem(i);

        if(associado != null) {
            tvNome.setText(associado.nome);

            Calendar dataAtual = new GregorianCalendar();
            Calendar dataApurada = new GregorianCalendar();

            if(associado.emAtraso) {
                ctvSemAtraso.setVisibility(View.INVISIBLE);
                ctvComAtraso.setVisibility(View.VISIBLE);
            }else{
                ctvComAtraso.setVisibility(View.INVISIBLE);
                ctvSemAtraso.setVisibility(View.VISIBLE);
            }

            if(associado.dataUltimoPagamento != null){
                tvDataUltimoPagamento.setText(format.format(associado.dataUltimoPagamento));
            }

            if(associado.dataNascimento != null){
                tvDataNascimento.setText(format.format(associado.dataNascimento));
            }

            if(associado.dataAssociacao != null){
                tvDataAssociacao.setText(format.format(associado.dataAssociacao));
            }

            tvTelefone.setText(associado.telefone);
        }

        return view;
    }
}
