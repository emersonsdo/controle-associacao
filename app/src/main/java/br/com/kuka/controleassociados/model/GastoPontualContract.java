package br.com.kuka.controleassociados.model;

import android.provider.BaseColumns;

/**
 * Created by 01748913506 on 03/03/17.
 */

public class GastoPontualContract {

    private GastoPontualContract(){
    }

    public static final class GastoPontual implements BaseColumns {
        public static final String TABLE_NAME = "gastos_pontuais";
        public static final String COLUMN_NAME_VALOR = "valor";
        public static final String COLUMN_NAME_DESCRICAO = "descricao";
        public static final String COLUMN_NAME_DATA_CRIACAO = "data_criacao";
        public static final String COLUMN_NAME_CONTABILIZADO = "contabilizado";
    }
}
