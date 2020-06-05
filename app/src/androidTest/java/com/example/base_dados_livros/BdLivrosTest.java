package com.example.base_dados_livros;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BdLivrosTest {
    @Before
    @After
    public void apagaBaseDados(){
        getTargetContext().deleteDatabase(BdLivrosOpenHelper.NOME_BASE_DADOS);
    }
    @Test
    public void consegueAbrirBaseDados() {
        // Context of the app under test.
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getReadableDatabase();
        assertTrue(bd.isOpen());
        bd.close();
    }

    private Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    private long insereCategoria(BdTableCategorias tabelaCategorias, Categoria categoria) {
        long id = tabelaCategorias.insert(Converte.categoriaToContentValues(categoria));
        assertNotEquals(-1, id);

        return id;
    }

    private long insereCategoria(BdTableCategorias tabelaCategorias, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setDescricao(descricao);

        return insereCategoria(tabelaCategorias, categoria);
    }

    private long insereLivro(SQLiteDatabase bdLivros, String titulo, String descCategoria) {
        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        long idCategoria = insereCategoria(tabelaCategorias, descCategoria);

        BdTableLivros tabelaLivros = new BdTableLivros(bdLivros);

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIdCategoria(idCategoria);
        long idLivro = tabelaLivros.insert(Converte.livroToContentValues(livro));
        assertNotEquals(-1, idLivro);

        return idLivro;
    }

    @Test
    public void consegueInserirCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bd);

        insereCategoria(tabelaCategorias, "Ação");

        bd.close();
    }

    @Test
    public void consegueLerCategorias(){
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tableCategorias = new BdTableCategorias(bd);

        Cursor cursor = tableCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);
        int registos = cursor.getCount();
        cursor.close();

        insereCategoria(tableCategorias, "Sci-fi");

        cursor = tableCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);;
        assertEquals(registos + 1, cursor.getCount());
        cursor.close();

        bd.close();
    }

    @Test
    public void  consegueAtualizarCategorias(){
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tableCategorias = new BdTableCategorias(bd);

        Categoria categoria = new Categoria();
        categoria.setDescricao("Romanc");

        long id = insereCategoria(tableCategorias, categoria);

        categoria.setDescricao("Romance");
        int registosAlterados = tableCategorias.update(Converte.categoriaToContentValues(categoria), BdTableCategorias._ID + "=?", new String[]{String.valueOf(id)});
        assertEquals(1, registosAlterados);

        bd.close();
    }

    @Test
    public void  consegueApagarCategorias(){
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tableCategorias = new BdTableCategorias(bd);

        long id = insereCategoria(tableCategorias, "TESTE");

        int registosApagados = tableCategorias.delete(BdTableCategorias._ID + "=?", new String[]{String.valueOf(id)});
        assertEquals(1, registosApagados);

        bd.close();
    }

    @Test
    public void consegueInserirLivros() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        insereLivro(bd, "O Intruso", "Terror");

        bd.close();
    }

    @Test
    public void consegueLerLivros() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableLivros tabelaLivros = new BdTableLivros(bd);
        Cursor cursor = tabelaLivros.query(BdTableLivros.TODOS_CAMPOS, null, null, null, null, null);
        int registos = cursor.getCount();
        cursor.close();

        insereLivro(bd, "O Intruso II", "Terror/Ação");

        cursor = tabelaLivros.query(BdTableLivros.TODOS_CAMPOS, null, null, null, null, null);
        assertEquals(registos + 1, cursor.getCount());
        cursor.close();

        bd.close();
    }

    @Test
    public void consegueAlterarLivros(){
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        long idLivro = insereLivro(bdLivros, "O silêncio dos inocentes", "Thriller");

        BdTableLivros tabelaLivros = new BdTableLivros(bdLivros);

        Cursor cursor = tabelaLivros.query(BdTableLivros.TODOS_CAMPOS, BdTableLivros.CAMPO_ID_COMPLETO + "=?", new String[]{ String.valueOf(idLivro) }, null, null, null);
        assertEquals(1, cursor.getCount());

        assertTrue(cursor.moveToNext());
        Livro livro = Converte.cursorToLivro(cursor);
        cursor.close();

        assertEquals("O silêncio dos inocentes", livro.getTitulo());

        livro.setTitulo("O mistério do quarto secreto");
        int registosAfetados = tabelaLivros.update(Converte.livroToContentValues(livro), BdTableLivros.CAMPO_ID_COMPLETO + "=?", new String[]{String.valueOf(livro.getId())});
        assertEquals(1, registosAfetados);

        bdLivros.close();
    }

    @Test
    public void consegueEliminarLivros(){
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdlivros = openHelper.getWritableDatabase();

       long id = insereLivro(bdlivros, "O silêncio dos inocentes","Thriller");

       BdTableLivros tableLivros = new BdTableLivros(bdlivros);
       int registosEleminados = tableLivros.delete(BdTableLivros._ID + "=?", new String[]{String.valueOf(id)});

       bdlivros.close();
    }
}
