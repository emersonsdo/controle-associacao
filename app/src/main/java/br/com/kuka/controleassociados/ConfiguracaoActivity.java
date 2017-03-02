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
import java.util.Date;
import java.util.Locale;

import br.com.kuka.controleassociados.adapters.ListGastosFixosAdapter;
import br.com.kuka.controleassociados.model.GastoFixo;

public class ConfiguracaoActivity extends AppCompatActivity {

    private final static int CODIGO_NOVO_GASTO_FIXO = 1;

    SharedPreferences configuracoes;

    EditText editValorMensalidade;
    EditText editSaldo;

    String valorMensalidade;
    String saldo;

    ListView lvGastosFixos;
    ListGastosFixosAdapter gastosFixosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        configuracoes = getSharedPreferences("preferencies", Context.MODE_PRIVATE);
        valorMensalidade = configuracoes.getString("valor_mensalidade", "Nenhum valor salvo");
        saldo = configuracoes.getString("valor_saldo", "0");

        editValorMensalidade = (EditText) findViewById(R.id.ed_valor_mensalidade);
        editValorMensalidade.setHint(valorMensalidade);

        editSaldo = (EditText) findViewById(R.id.ed_saldo);
        editSaldo.setHint(saldo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.configuration_toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.titulo_configutation);

        gastosFixosAdapter = new ListGastosFixosAdapter(this);
        lvGastosFixos = (ListView) findViewById(R.id.lv_gastos_fixos);
        lvGastosFixos.setAdapter(gastosFixosAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuracao, menu);
        return true;
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
//                Intent intentConfigurar = new Intent(this, ConfiguracaoActivity.class);
//                intentConfigurar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intentConfigurar);

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
