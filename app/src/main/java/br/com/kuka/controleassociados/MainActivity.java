package br.com.kuka.controleassociados;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.titulo_main);

        Intent intent = getIntent();
        if(intent != null){
            Long idDeletado = intent.getLongExtra("ID_DELETADO", -100);
            if(idDeletado >= -1){
                deletar(idDeletado);
            }
        }

        atualizarSituacaoAssociados();
    }

    private void atualizarSituacaoAssociados() {
        Date atualDate = new Date();
        Log.i("MAIN", "Data atual: " +  atualDate.getTime());
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
                intentCadastrar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intentCadastrar, CODIGO_REQUEST_CADASTRAR);

                return true;
            case R.id.im_configuracao:
                Intent intentConfigurar = new Intent(this, ConfiguracaoActivity.class);
                intentConfigurar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intentConfigurar.putExtra("QUANTIDADE_ASSOCIADOS", associadosAdapter.getCount());
                startActivity(intentConfigurar);

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
                Date dataNascimento = null;
                Date dataUltimoPagamento = null;
                Date dataAssociacao = null;
                String telefone = data.getStringExtra("TELEFONE");
                try {
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    if(!data.getStringExtra("DATA_NASCIMENTO").isEmpty()){
                        dataNascimento = format.parse(data.getStringExtra("DATA_NASCIMENTO"));
                    }
                    if (!data.getStringExtra("DATA_ULTIMO_PAGAMENTO").isEmpty()){
                        dataUltimoPagamento = format.parse(data.getStringExtra("DATA_ULTIMO_PAGAMENTO"));
                    }
                    if(!data.getStringExtra("DATA_ASSOCIACAO").isEmpty()){
                        dataAssociacao = format.parse(data.getStringExtra("DATA_ASSOCIACAO"));
                    }
                } catch (ParseException e) {
                    Log.i("MAIN", "Erro no parser da data!");
                    e.printStackTrace();
                }

                Toast toast = Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_LONG);
                toast.show();

                associadosAdapter.addAssociado(new Associado(nomeCadastrado, dataNascimento, dataUltimoPagamento, dataAssociacao, telefone));

//                imprimirDados();
//            }else if(requestCode == CODIGO_REQUEST_CONFIGURAR) {
//                Toast toast = Toast.makeText(this, "Valor da mensalidade: " + data.getStringExtra("MENSALIDADE"), Toast.LENGTH_LONG);
//                toast.show();
            }else if(requestCode == CODIGO_REQUEST_EDITAR) {
//                TODO
            }
        }else{
            Log.i("MAIN", "Houve um erro na excecuçao da tarefa");
            if(requestCode == CODIGO_REQUEST_CADASTRAR) {
                Toast toast = Toast.makeText(this, "Erro ao salvar dados", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void editarAssociado(View view, int position){
        Associado associado = associadosAdapter.getItem(position);

        Intent intentEditar = new Intent(this, AssociadoActivity.class);
        intentEditar.putExtra("NOME", associado.nome);
        startActivityForResult(intentEditar, CODIGO_REQUEST_EDITAR);
    }

    public void deletar(Long idDeletado){
        if(idDeletado == -1){
            Toast toast = Toast.makeText(this, "Nada feito", Toast.LENGTH_LONG);
            toast.show();
        }else{
            associadosAdapter.deletarAssociado(idDeletado);
        }
//        imprimirDados();
    }

//    private void imprimirDados() {
//        associadosAdapter.getListaAssociados();
//    }

}