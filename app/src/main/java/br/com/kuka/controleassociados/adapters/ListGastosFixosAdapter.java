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
import java.util.Date;
import java.util.Locale;

import br.com.kuka.controleassociados.R;
import br.com.kuka.controleassociados.model.GastoFixo;
import br.com.kuka.controleassociados.model.GastoFixoContract;
import br.com.kuka.controleassociados.util.DBGastoFixoHelper;

/**
 * Created by 01748913506 on 24/02/17.
 */

public class ListGastosFixosAdapter extends BaseAdapter {

    ArrayList<GastoFixo> listaGastosFixos;
    LayoutInflater inflater;
    DBGastoFixoHelper dbGastoFixoHelper;
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public ListGastosFixosAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.dbGastoFixoHelper = new DBGastoFixoHelper(context);
        this.listaGastosFixos = getListaGastosFixos();
    }

    public ArrayList<GastoFixo> getListaGastosFixos() {
        SQLiteDatabase db = dbGastoFixoHelper.getReadableDatabase();

        String [] dadosGastoFixo = {
                GastoFixoContract.GastoFixo._ID,
                GastoFixoContract.GastoFixo.COLUMN_NAME_DESCRICAO,
                GastoFixoContract.GastoFixo.COLUMN_NAME_VALOR,
                GastoFixoContract.GastoFixo.COLUMN_NAME_DATA_CRIACAO
        };

        Cursor cursor = db.query(GastoFixoContract.GastoFixo.TABLE_NAME,
                dadosGastoFixo, null, null, null, null, null);

        listaGastosFixos = new ArrayList<>();

        while (cursor.moveToNext()) {
            GastoFixo gastoFixo = new GastoFixo();

            gastoFixo._ID = Long.parseLong(cursor.getString(0));
            gastoFixo.descricao = cursor.getString(1);
            gastoFixo.valor = cursor.getLong(2);

            if(cursor.getString(3) != null && !cursor.getString(3).isEmpty()){
                try {
                    Date dataCriacao = format.parse(cursor.getString(3));
                    gastoFixo.dataCriacao = dataCriacao;
                } catch (ParseException e) {
                    Log.i("GASTO_FIXO_ADAPTER", "Erro no parser da data de nascimento!");
                    e.printStackTrace();
                }
            }

            listaGastosFixos.add(gastoFixo);
        }

        cursor.close();
        db.close();

        return listaGastosFixos;
    }

    public ArrayList<GastoFixo> getListaGastosFixos(int mes, int ano){
        //TODO
        return null;
    }

    public void addGastoFixo(GastoFixo gastoFixo){
        listaGastosFixos.add(gastoFixo);
    }

    @Override
    public int getCount() {
        SQLiteDatabase db = dbGastoFixoHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, GastoFixoContract.GastoFixo.TABLE_NAME);
        db.close();

        return (int) count;
    }

    @Override
    public GastoFixo getItem(int i) {
        return listaGastosFixos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaGastosFixos.get(i)._ID;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.list_view_gastos_fixos, null);

        TextView tvDescricaoGastoFixo = (TextView) view.findViewById(R.id.tv_descricao_gasto_fixo);
        TextView tvValorGastoFixo = (TextView) view.findViewById(R.id.tv_valor_gasto_fixo);
        TextView tvDataCriacaoGastoFixo = (TextView) view.findViewById(R.id.tv_data_criacao_gasto_fixo);

        GastoFixo gastoFixo = getItem(i);

        if(gastoFixo != null){

            tvDescricaoGastoFixo.setText(gastoFixo.descricao);
            tvValorGastoFixo.setText(String.valueOf(gastoFixo.valor));
            tvDataCriacaoGastoFixo.setText(format.format(gastoFixo.dataCriacao));

        }

        return view;
    }

}
