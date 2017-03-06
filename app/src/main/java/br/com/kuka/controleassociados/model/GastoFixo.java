package br.com.kuka.controleassociados.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 01748913506 on 23/02/17.
 */

public class GastoFixo implements Serializable {

    public long _ID;
    public String descricao;
    public long valor;
    public Date dataCriacao;

    public GastoFixo(){
    }

    public GastoFixo(String descricao, long valor) throws ParseException {
        this.valor = valor;
        this.descricao = descricao;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dataCriacao = new Date();
        dataCriacao = format.parse(dataCriacao.toString());
    }

    public GastoFixo(String descricao, long valor, Date dataCriacao) {
        this.valor = valor;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
    }
}