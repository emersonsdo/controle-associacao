package br.com.kuka.controleassociados;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfiguracaoActivity extends AppCompatActivity {

    SharedPreferences configuracoes;
    EditText editValorMensalidade;
    String valorMensalidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        configuracoes = getSharedPreferences("preferencies", Context.MODE_PRIVATE);
        valorMensalidade = configuracoes.getString("valor_mensalidade", "Nenhum valor salvo");

        editValorMensalidade = (EditText) findViewById(R.id.ed_valor_mensalidade);
        editValorMensalidade.setHint(valorMensalidade);
    }

    public void salvar(View view){
        SharedPreferences.Editor editor = configuracoes.edit();

        String novaMensalidade = editValorMensalidade.getText().toString();

        editor.putString("valor_mensalidade", novaMensalidade);
        editor.commit();

        update();
    }

    private void update(){
        valorMensalidade = configuracoes.getString("valor_mensalidade", "Nenhum valor salvo");
        editValorMensalidade.setHint(valorMensalidade);
    }

    public void sair(View view){
        Intent intent = new Intent();
        intent.putExtra("MENSALIDADE", valorMensalidade);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }
}
