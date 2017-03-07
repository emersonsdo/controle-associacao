package br.com.kuka.controleassociados;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.kuka.controleassociados.adapters.ListGastosFixosAdapter;
import br.com.kuka.controleassociados.adapters.ListGastosPontuaisAdapter;
import br.com.kuka.controleassociados.model.GastoFixo;
import br.com.kuka.controleassociados.model.GastoPontual;

public class ConfiguracaoActivity extends AppCompatActivity {

    private final static int CODIGO_NOVO_GASTO_FIXO = 1;
    private final static int CODIGO_NOVO_GASTO_PONTUAL = 2;

    SharedPreferences configuracoes;

    EditText editValorMensalidade;
    EditText editSaldo;
    EditText editReceitasDoMes;
    EditText editDespesasDoMes;

    String valorMensalidade;
    String saldo;
    String mesCalculado;
    //TODO: Calcular receitas recebidas de fato
    String previsaoReceitasDoMes;
    String previsaoDespesasDoMes;

    int quantidadeAssociados;

    ListView lvGastosFixos;
    ListGastosFixosAdapter gastosFixosAdapter;

    ListView lvGastosPontuais;
    ListGastosPontuaisAdapter gastosPontuaisAdapter;

    @Override //TODO: abrir tela de ediçao do item ao clicar neles (como os associados)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        obterPreferencias();
        setTextosIniciais();

        Toolbar toolbar = (Toolbar) findViewById(R.id.configuration_toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.titulo_configutation);

        gastosFixosAdapter = new ListGastosFixosAdapter(this);
        lvGastosFixos = (ListView) findViewById(R.id.lv_gastos_fixos);
        lvGastosFixos.setAdapter(gastosFixosAdapter);

        gastosPontuaisAdapter = new ListGastosPontuaisAdapter(this);
        lvGastosPontuais = (ListView) findViewById(R.id.lv_gastos_pontuais);
        lvGastosPontuais.setAdapter(gastosPontuaisAdapter);

        Intent intent = getIntent();
        quantidadeAssociados = intent.getIntExtra("QUANTIDADE_ASSOCIADOS", 0);

        calcularReceitasDespesas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuracao, menu);
        return true;
    }

    private void obterPreferencias(){
        configuracoes = getSharedPreferences("preferencies", Context.MODE_PRIVATE);
        valorMensalidade = configuracoes.getString("valor_mensalidade", "Nenhum valor salvo");
        saldo = configuracoes.getString("valor_saldo", "0");
        mesCalculado = configuracoes.getString("mes_calculado", "-1");
        previsaoReceitasDoMes = configuracoes.getString("receitas_do_mes", "0");
        previsaoDespesasDoMes = configuracoes.getString("despesas_do_mes", "0");
    }

    private void setTextosIniciais(){
        editValorMensalidade = (EditText) findViewById(R.id.ed_valor_mensalidade);
        editValorMensalidade.setHint(valorMensalidade);

        editSaldo = (EditText) findViewById(R.id.ed_saldo);
        editSaldo.setHint(saldo);

        editReceitasDoMes = (EditText) findViewById(R.id.ed_previsao_receita);
        editReceitasDoMes.setHint(previsaoReceitasDoMes);

        editDespesasDoMes = (EditText) findViewById(R.id.ed_previsao_despesa);
        editDespesasDoMes.setHint(previsaoDespesasDoMes);
    }

    private void calcularReceitasDespesas() {
        Calendar dataAtual = new GregorianCalendar();
        int mesAtual = dataAtual.get(Calendar.MONTH);
        ArrayList<GastoFixo> listaGastosFixos = new ArrayList<GastoFixo>();
        ArrayList<GastoPontual> listaGastosPontuais = new ArrayList<GastoPontual>();

        if(!String.valueOf(mesAtual).equalsIgnoreCase(mesCalculado)){
            mesCalculado = String.valueOf(mesAtual);

            if(!valorMensalidade.equalsIgnoreCase("Nenhum valor salvo")){
                previsaoReceitasDoMes = String.valueOf(quantidadeAssociados*(Long.parseLong(valorMensalidade)));
            }else{
                previsaoReceitasDoMes = "0";
            }
            editReceitasDoMes.setHint(previsaoReceitasDoMes);

            listaGastosFixos = gastosFixosAdapter.getListaGastosFixos();
            listaGastosPontuais = gastosPontuaisAdapter.getListaGastosPontuais(mesAtual, dataAtual.get(Calendar.YEAR));
            long gastosFixos = 0;
            long gastosPontuais = 0;
            if(!listaGastosFixos.isEmpty() && listaGastosFixos != null){
                gastosFixos = calcularPrevisaoDespesasFixas(listaGastosFixos);
            }
            if(!listaGastosPontuais.isEmpty() && listaGastosPontuais != null){
                gastosPontuais = calcularPrevisaoDespesasPontuais(listaGastosPontuais);
            }

            previsaoDespesasDoMes = String.valueOf(gastosFixos+gastosPontuais);
            editDespesasDoMes.setHint(previsaoDespesasDoMes);
        }
    }

    //TODO: usar generics e/ou type para ter apenas um metodo
    private long calcularPrevisaoDespesasPontuais(ArrayList<GastoPontual> listaGastosPontuais) {
        long previsaoDespesasPontuais = 0;
        for(GastoPontual gastoPontual : listaGastosPontuais){
            previsaoDespesasPontuais += gastoPontual.valor;
        }
        return previsaoDespesasPontuais;
    }

    private long calcularPrevisaoDespesasFixas(ArrayList<GastoFixo> listaGastosFixos) {
        long previsaoDespesasFixas = 0;
        for(GastoFixo gastoFixo : listaGastosFixos){
            previsaoDespesasFixas += gastoFixo.valor;
        }
        return previsaoDespesasFixas;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.im_voltar:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;

            case R.id.im_novo_gasto_fixo:
                Intent intentNovoGastoFixo = new Intent(this, GastoFixoActivity.class);
                intentNovoGastoFixo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intentNovoGastoFixo, CODIGO_NOVO_GASTO_FIXO);
                return true;

            case R.id.im_novo_gasto_pontual:
                Intent intentNovoGastoPontual = new Intent(this, GastoPontualActivity.class);
                intentNovoGastoPontual.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intentNovoGastoPontual, CODIGO_NOVO_GASTO_PONTUAL);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CODIGO_NOVO_GASTO_FIXO){
                String descricao = data.getStringExtra("DESCRICAO");
                String valor = data.getStringExtra("VALOR");
                String stringDataCriacao = data.getStringExtra("DATA");
                Date dataCriacao = null;

                try{
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    if(!stringDataCriacao.isEmpty()){
                        dataCriacao = format.parse(stringDataCriacao);
                    }
                } catch (ParseException e) {
                    Log.i("CONFIGURACAO", "Erro no parser da data!");
                    e.printStackTrace();
                }

                gastosFixosAdapter.addGastoFixo(new GastoFixo(descricao, Long.parseLong(valor), dataCriacao));

            } else if (requestCode == CODIGO_NOVO_GASTO_PONTUAL){
                String descricao = data.getStringExtra("DESCRICAO");
                String valor = data.getStringExtra("VALOR");
                String stringDataCriacao = data.getStringExtra("DATA");
                Date dataCriacao = null;

                try{
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    if(!stringDataCriacao.isEmpty()){
                        dataCriacao = format.parse(stringDataCriacao);
                    }
                } catch (ParseException e) {
                    Log.i("CONFIGURACAO", "Erro no parser da data!");
                    e.printStackTrace();
                }

                gastosPontuaisAdapter.addGastoPontual(new GastoPontual(descricao, Long.parseLong(valor), dataCriacao));
            }
        }
    }

    public void salvar(View view){
        SharedPreferences.Editor editor = configuracoes.edit();

        String novaMensalidade = editValorMensalidade.getText().toString();
        if(novaMensalidade.isEmpty()){
            novaMensalidade = editValorMensalidade.getHint().toString();
        }

        String novoSaldo = editSaldo.getText().toString();
        if(novoSaldo.isEmpty()){
            novoSaldo = editSaldo.getHint().toString();
        }

        editor.putString("valor_mensalidade", novaMensalidade);
        editor.putString("valor_saldo", novoSaldo);
        editor.commit();

        update();
    }

    private void update(){
        valorMensalidade = configuracoes.getString("valor_mensalidade", "Nenhum valor salvo");
        editValorMensalidade.setHint(valorMensalidade);

        saldo = configuracoes.getString("valor_saldo", "0");
        editSaldo.setHint(saldo);
    }

    public void limpar(View view){

        editValorMensalidade.setHint("");
        editSaldo.setHint("");

//        Intent intent = new Intent();
//        intent.putExtra("MENSALIDADE", valorMensalidade);
//        intent.putExtra("SALDO", saldo);
//        setResult(Activity.RESULT_OK, intent);
//
//        finish();
    }
}