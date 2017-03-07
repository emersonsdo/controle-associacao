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
import br.com.kuka.controleassociados.model.GastoPontual;
import br.com.kuka.controleassociados.model.GastoPontualContract;
import br.com.kuka.controleassociados.util.DBGastoPontualHelper;

/**
 * Created by 01748913506 on 03/03/17.
 */

public class ListGastosPontuaisAdapter extends BaseAdapter {

    ArrayList<GastoPontual> listaGastosPontuais;
    LayoutInflater inflater;
    DBGastoPontualHelper dbGastoPontualHelper;
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public ListGastosPontuaisAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.dbGastoPontualHelper = new DBGastoPontualHelper(context);
        this.listaGastosPontuais = getListaGastosPontuais();
    }

    public ArrayList<GastoPontual> getListaGastosPontuais() {
        SQLiteDatabase db = dbGastoPontualHelper.getReadableDatabase();

        String [] dadosGastoPontual = {
                GastoPontualContract.GastoPontual._ID,
                GastoPontualContract.GastoPontual.COLUMN_NAME_DESCRICAO,
                GastoPontualContract.GastoPontual.COLUMN_NAME_VALOR,
                GastoPontualContract.GastoPontual.COLUMN_NAME_DATA_CRIACAO,
                GastoPontualContract.GastoPontual.COLUMN_NAME_CONTABILIZADO
        };

        Cursor cursor = db.query(GastoPontualContract.GastoPontual.TABLE_NAME,
                dadosGastoPontual, null, null, null, null, null);

        listaGastosPontuais = new ArrayList<>();

        while (cursor.moveToNext()) {
            GastoPontual gastoPontual = new GastoPontual();

            gastoPontual._ID = Long.parseLong(cursor.getString(0));
            gastoPontual.descricao = cursor.getString(1);
            gastoPontual.valor = cursor.getLong(2);

            if(cursor.getString(3) != null && !cursor.getString(3).isEmpty()){
                try {
                    Date dataCriacao = format.parse(cursor.getString(3));
                    gastoPontual.dataCriacao = dataCriacao;
                } catch (ParseException e) {
                    Log.i("GASTO_PONTUAL_ADAPTER", "Erro no parser da data de criaçao!");
                    e.printStackTrace();
                }
            }

            gastoPontual.contabilizado = Boolean.valueOf(cursor.getString(4));
            listaGastosPontuais.add(gastoPontual);
        }

        cursor.close();
        db.close();

        return listaGastosPontuais;
    }

    public ArrayList<GastoPontual> getListaGastosPontuais(int mes, int ano){
        ArrayList<GastoPontual> listaGastosPontuaisMensal = new ArrayList<>();

        Calendar dataCriacao = new GregorianCalendar();

        for(GastoPontual gastoPontual : listaGastosPontuais){
            dataCriacao.setTime(gastoPontual.dataCriacao);
            if(dataCriacao.get(Calendar.MONTH) == mes && dataCriacao.get(Calendar.YEAR) == ano){
                listaGastosPontuaisMensal.add(gastoPontual);
            }
        }
        return listaGastosPontuaisMensal;
    }

    public void addGastoPontual(GastoPontual gastoPontual){
        listaGastosPontuais.add(gastoPontual);
    }

    @Override
    public int getCount() {
        SQLiteDatabase db = dbGastoPontualHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, GastoPontualContract.GastoPontual.TABLE_NAME);
        db.close();

        return (int) count;
    }

    @Override
    public GastoPontual getItem(int i) {
        return listaGastosPontuais.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaGastosPontuais.get(i)._ID;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_view_gastos_pontuais, null);

        TextView tvDescricaoGastoPontual = (TextView) view.findViewById(R.id.tv_descricao_gasto_pontual);
        TextView tvValorGastoPontual = (TextView) view.findViewById(R.id.tv_valor_gasto_pontual);
        TextView tvDataCriacaoGastoPontual = (TextView) view.findViewById(R.id.tv_data_criacao_gasto_pontual);

        GastoPontual gastoPontual = getItem(i);

        if(gastoPontual != null){

            tvDescricaoGastoPontual.setText(gastoPontual.descricao);
            tvValorGastoPontual.setText(String.valueOf(gastoPontual.valor));
            tvDataCriacaoGastoPontual.setText(format.format(gastoPontual.dataCriacao));
        }

        return view;
    }
}
