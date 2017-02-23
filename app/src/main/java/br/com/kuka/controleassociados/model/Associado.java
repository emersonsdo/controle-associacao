package br.com.kuka.controleassociados.model;

import android.media.Image;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 01748913506 on 16/02/17.
 */

public class Associado implements Serializable{
    public long _ID;
    public String nome;
    public Boolean emAtraso;
    public Date dataNascimento;
    public Date dataUltimoPagamento;
    public Date dataAssociacao;
    public String telefone;
//    public String valorDevido;
//    public Image foto;

    public Associado (){
    }

    public Associado (String nome/*, Boolean emAtraso*/){
        this.nome = nome;
//        this.emAtraso = emAtraso;
    }

    public Associado (String nome, /*Boolean emAtraso, */Date dataNascimento){
        this.nome = nome;
//        this.emAtraso = emAtraso;
        this.dataNascimento = dataNascimento;
    }

    public Associado (String nome, /*Boolean emAtraso, */Date dataNascimento, Date dataUltimoPagamento){
        this.nome = nome;
//        this.emAtraso = emAtraso;
        this.dataNascimento = dataNascimento;
        this.dataUltimoPagamento = dataUltimoPagamento;
    }

    public Associado (String nome, /*Boolean emAtraso, */Date dataNascimento, Date dataUltimoPagamento, Date dataAssociacao){
        this.nome = nome;
//        this.emAtraso = emAtraso;
        this.dataNascimento = dataNascimento;
        this.dataUltimoPagamento = dataUltimoPagamento;
        this.dataAssociacao = dataAssociacao;
    }

    public Associado (String nome, /*Boolean emAtraso, */Date dataNascimento, Date dataUltimoPagamento, Date dataAssociacao, String telefone){
        this.nome = nome;
//        this.emAtraso = emAtraso;
        this.dataNascimento = dataNascimento;
        this.dataUltimoPagamento = dataUltimoPagamento;
        this.dataAssociacao = dataAssociacao;
        this.telefone = telefone;
    }
}
