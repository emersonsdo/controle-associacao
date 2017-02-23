package br.com.kuka.controleassociados.model;

import android.provider.BaseColumns;

/**
 * Created by 01748913506 on 16/02/17.
 */

public class AssociadoContract {

    private AssociadoContract(){
    }

    public static final class Associado implements BaseColumns {
        public static final String TABLE_NAME = "associado";
        public static final String COLUMN_NAME_NOME = "nome";
//        public static final String COLUMN_NAME_ATRASO = "atraso";
        public static final String COLUMN_NAME_DATA_NASCIMENTO = "data_nascimento";
        public static final String COLUMN_NAME_DATA_ULTIMO_PAGAMENTO = "data_ultimo_pagamento";
        public static final String COLUMN_NAME_DATA_ASSOCIACAO = "data_associacao";
        public static final String COLUMN_NAME_TELEFONE = "telefone";
    }
}
