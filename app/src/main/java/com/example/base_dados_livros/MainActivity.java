package com.example.base_dados_livros;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment framentoActual = null;
    private int menuActual = R.menu.menu_lista_livros;

    public void setMenuActual(int menuActual) {
        if (menuActual != this.menuActual){
            this.menuActual = menuActual;
            invalidateOptionsMenu();
        }
    }

    public void setFramentoActual(Fragment framentoActual) {
        this.framentoActual = framentoActual;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menuActual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (menuActual == R.menu.menu_lista_livros) {
           if(processaOpcoesMenuListaLivros(id)) return true;
        }else if (menuActual  == R.menu.menu_lista_livro){
            if(processaOpcoesMenuInserirLivros(id)) return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean processaOpcoesMenuInserirLivros(int id) {
        ListaLivroFragment adicionaLivroFragment = (ListaLivroFragment) framentoActual;

        if (id == R.id.action_guardar){
            adicionaLivroFragment.guardar();
            return true;
        }else if (id == R.id.action_cancelar) {
            adicionaLivroFragment.cancelar();
            return true;
        }
        return false;
    }

    private boolean processaOpcoesMenuListaLivros(int id) {
        ListaLivroFragment listaLivroFragment = (ListaLivroFragment) framentoActual;

        if (id == R.id.action_inserir_livro){
            listaLivroFragment.novoLivro();
            return true;
        }else if (id == R.id.action_alterar_livro){
            listaLivroFragment.alteraLivro();
            return true;
        }else if (id == R.id.action_eliminar_livro){
            listaLivroFragment.eliminaLivro();
            return true;
        }
        return false;
    }
}
