package br.com.cwi.cursoandroid.locadoraroots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.cwi.cursoandroid.locadoraroots.adapters.JogoSnesRecyclerAdapter;
import br.com.cwi.cursoandroid.locadoraroots.firebase.JogoSnesRepository;
import br.com.cwi.cursoandroid.locadoraroots.models.JogoSnes;
import br.com.cwi.cursoandroid.locadoraroots.models.ListaJogosSnes;
import br.com.cwi.cursoandroid.locadoraroots.utils.AsyncDataHandler;
import br.com.cwi.cursoandroid.locadoraroots.utils.Constantes;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener, AsyncDataHandler<JogoSnes>
{

    private static final String TAG = "MainActivity";
    // https://developer.android.com/training/material/lists-cards.html
    private RecyclerView rcvJogos;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ListaJogosSnes listaJogos;
    private JogoSnesRepository jogoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initComponents();
    }

    private void initData() {
        this.jogoRepository = new JogoSnesRepository(this);
        listaJogos = new ListaJogosSnes(getSharedPreferences(
                Constantes.ARQUIVO_PREFERENCIAS,
                MODE_PRIVATE
        ));
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Passou pelo onResume!!!");
//        SharedPreferences preferences = getSharedPreferences(
//                Constantes.ARQUIVO_PREFERENCIAS,
//                MODE_PRIVATE);
//        Log.d(TAG, preferences.getString(Constantes.LISTA_JOGOS, "sem jogos no preferences"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "Passou pelo onPause!!!");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Passou pelo onStop!!!");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "Passou pelo onRestart!!!");
        super.onRestart();
    }

    private void initComponents() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBarMain);
        setSupportActionBar(myToolbar);
        this.rcvJogos = (RecyclerView)findViewById(R.id.rcvJogos);
        // rcvJogos.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        //this.adapter = new JogoSnesRecyclerAdapter(this.listaJogos.getAll());
        jogoRepository.enqueueGetAll();
        String tipoDesconto = getIntent().getStringExtra(Constantes.TIPO_DESCONTO_FITA);
        Double descontoFita = getIntent().getDoubleExtra(Constantes.DESCONTO_FITA, 0);
        this.adapter = new JogoSnesRecyclerAdapter(tipoDesconto, descontoFita);
        // obtendo descontos vindos do push
        this.rcvJogos.setLayoutManager(layoutManager);
        this.rcvJogos.setAdapter(this.adapter);
        rcvJogos.setLongClickable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemBusca:
                // https://developer.android.com/reference/android/support/design/widget/Snackbar.html
                Toast.makeText(this, "TODO: pesquisar", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DetalheJogoSnesActivity.class);
        int posicao = this.rcvJogos.getChildLayoutPosition(v);
        JogoSnes jogoTocado = this.listaJogos.get(posicao);
        intent.putExtra(Constantes.USUARIO_DETALHE, jogoTocado);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        int posicao = this.rcvJogos.getChildLayoutPosition(v);
        listaJogos.remover(posicao);
        this.adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onDataAdded(JogoSnes jogo) {
        Log.d(TAG, String.format("Jogo adicionado: %d - %s", jogo.id, jogo.titulo));
        ((JogoSnesRecyclerAdapter)adapter).adicionarJogo(jogo);
        this.listaJogos.adicionar(jogo);
    }

    @Override
    public void onDataChanged(JogoSnes jogo) {
        Log.d(TAG, String.format("Jogo alterado: %d - %s", jogo.id, jogo.titulo));
    }

    @Override
    public void onDataRemoved(JogoSnes jogo) {
        Log.d(TAG, String.format("Jogo removido: %d - %s", jogo.id, jogo.titulo));
    }

    @Override
    public void onDataMoved(JogoSnes jogo) {
        Log.d(TAG, String.format("Jogo movido: %d - %s", jogo.id, jogo.titulo));
    }
}