package br.com.kuka.controleassociados;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.kuka.controleassociados.adapters.ListAssociadosAdapter;
import br.com.kuka.controleassociados.model.Associado;
import br.com.kuka.controleassociados.util.DBAssociadoHelper;

public class MainActivity extends AppCompatActivity {

    private final static int CODIGO_REQUEST_CADASTRAR = 1;
    private final static int CODIGO_REQUEST_CONFIGURAR = 2;
    private final static int CODIGO_REQUEST_EDITAR = 3;

    ListView lvAssociados;
    ListAssociadosAdapter associadosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        associadosAdapter = new ListAssociadosAdapter(this);
        lvAssociados = (ListView) findViewById(R.id.lv_associados);
        lvAssociados.setAdapter(associadosAdapter);

        lvAssociados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editarAssociado(view, position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.im_cadastrar:
                Intent intentCadastrar = new Intent(this, AssociadoActivity.class);
                startActivityForResult(intentCadastrar, CODIGO_REQUEST_CADASTRAR);

                return true;
            case R.id.im_configuracao:
                Intent intentConfigurar = new Intent(this, ConfiguracaoActivity.class);
                startActivityForResult(intentConfigurar, CODIGO_REQUEST_CONFIGURAR);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CODIGO_REQUEST_CADASTRAR || requestCode == CODIGO_REQUEST_EDITAR) {
                String nomeCadastrado = data.getStringExtra("NOME");
                Boolean emAtraso = data.getBooleanExtra("EM_ATRASO", false);
                Date dataNascimento = null;
                Date dataUltimoPagamento = null;
                Date dataAssociacao = null;
                try {
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    dataNascimento = format.parse(data.getStringExtra("DATA_NASCIMENTO"));
                    dataUltimoPagamento = format.parse(data.getStringExtra("DATA_ULTIMO_PAGAMENTO"));
                    dataAssociacao = format.parse(data.getStringExtra("DATA_ASSOCIACAO"));
                } catch (ParseException e) {
                    Log.i("MAIN", "Erro no parser da data!");
                    e.printStackTrace();
                }

                associadosAdapter.addAssociado(new Associado(nomeCadastrado, emAtraso, dataNascimento, dataUltimoPagamento, dataAssociacao));

                Toast toast = Toast.makeText(this, "Associado Cadastrado", Toast.LENGTH_LONG);
                toast.show();

                imprimirDados();
            }else if(requestCode == CODIGO_REQUEST_CONFIGURAR) {
                Toast toast = Toast.makeText(this, "Valor da mensalidade: " + data.getStringExtra("MENSALIDADE"), Toast.LENGTH_LONG);
                toast.show();
            }else if(requestCode == CODIGO_REQUEST_EDITAR) {
//                TODO
            }
        }else{
            Log.i("MAIN", "Houve um erro na excecuçao da tarefa");
            if(requestCode == CODIGO_REQUEST_CADASTRAR) {
                Toast toast = Toast.makeText(this, "Erro no cadastro do associado!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void imprimirDados() {
        associadosAdapter.getListaAssociados();
    }

    public void editarAssociado(View view, int position){
        Associado associado = associadosAdapter.getItem(position);

        Intent intentEditar = new Intent(this, AssociadoActivity.class);
        intentEditar.putExtra("NOME", associado.nome);
        startActivityForResult(intentEditar, CODIGO_REQUEST_EDITAR);
    }

}
