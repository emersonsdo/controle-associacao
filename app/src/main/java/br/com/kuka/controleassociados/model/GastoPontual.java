package br.com.kuka.controleassociados.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 01748913506 on 03/03/17.
 */

public class GastoPontual implements Serializable {

    public long _ID;
    public String descricao;
    public long valor;
    public Date dataCriacao;
    public Boolean contabilizado;

    public GastoPontual(){
    }

    public GastoPontual(String descricao, long valor) throws ParseException {
        this.valor = valor;
        this.descricao = descricao;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.dataCriacao = new Date();
        this.dataCriacao = format.parse(dataCriacao.toString());
        this.contabilizado = false;
    }

    public GastoPontual(String descricao, long valor, Date dataCriacao) {
        this.valor = valor;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.contabilizado = false;
    }

    public GastoPontual(String descricao, long valor, Date dataCriacao, Boolean contabilizado) {
        this.valor = valor;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.contabilizado = contabilizado;
    }
}
