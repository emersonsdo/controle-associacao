package br.com.kuka.controleassociados.model;

import android.provider.BaseColumns;

/**
 * Created by 01748913506 on 23/02/17.
 */

public class GastoFixoContract {

    private GastoFixoContract(){
    }

    public static final class GastoFixo implements BaseColumns {
        public static final String TABLE_NAME = "gastos_fixos";
        public static final String COLUMN_NAME_VALOR = "valor";
        public static final String COLUMN_NAME_DESCRICAO = "descricao";
        public static final String COLUMN_NAME_DATA_CRIACAO = "data_criacao";
    }
}
