package com.example.base_dados_livros;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
    public void apagaBaseDados(){
        getTragetContext().deleteDatabase(BdLivrosOpenHelper.NOME_BASE_DADOS);
    }
    @Test
    public void consegueAbrirBaseDados() {
        // Context of the app under test.
        Context appContext = getTragetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getReadableDatabase();
        assertTrue(bd.isOpen());
        bd.close();
    }

    private Context getTragetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void consegueConsultarDados(){
        Context appContext = getTragetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tableCategorias = new BdTableCategorias(bd);

        Categoria categoria = new Categoria();
        categoria.setDescricao("Ação");

        long id = tableCategorias.insert(Converte.categoriaToContentValue(categoria));
        assertNotEquals(-1, id);

        bd.close();
    }

    @Test
    public void consegueLerCategorias(){
        Context appContext = getTragetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tableCategorias = new BdTableCategorias(bd);

        Cursor cursor = tableCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);
        int registos = cursor.getCount();
        cursor.close();

        Categoria categoria = new Categoria();
        categoria.setDescricao("Sci-fi");

        long id = tableCategorias.insert(Converte.categoriaToContentValue(categoria));
        assertNotEquals(-1, id);

        cursor = tableCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);;
        assertEquals(registos + 1, cursor.getCount());
        cursor.close();

        bd.close();
    }

    @Test
    public void  consegueAtualizarCategorias(){
        Context appContext = getTragetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tableCategorias = new BdTableCategorias(bd);


        Categoria categoria = new Categoria();
        categoria.setDescricao("Romanc");

        long id = tableCategorias.insert(Converte.categoriaToContentValue(categoria));
        assertNotEquals(-1, id);

        categoria.setDescricao("Romance");
        int registosAlterados = tableCategorias.update(Converte.categoriaToContentValue(categoria), BdTableCategorias._ID + "=?", new String[]{String.valueOf(id)});
        assertEquals(1, registosAlterados);

        bd.close();
    }

    @Test
    public void  consegueApagarCategorias(){
        Context appContext = getTragetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        BdTableCategorias tableCategorias = new BdTableCategorias(bd);


        Categoria categoria = new Categoria();
        categoria.setDescricao("Teste");

        long id = tableCategorias.insert(Converte.categoriaToContentValue(categoria));
        assertNotEquals(-1, id);

        int registosApagados = tableCategorias.delete(BdTableCategorias._ID + "=?", new String[]{String.valueOf(id)});
        assertEquals(1, registosApagados);

        bd.close();
    }
}
