package com.example.base_dados_livros;

import android.content.ContentValues;
import android.nfc.cardemulation.CardEmulation;

public class Converte {
    public  static ContentValues categoriaToContentValue(Categoria categoria){
        ContentValues valores = new ContentValues();
            valores.put(BdTableCategorias.CAMPO_DESCRICAO,categoria.getDescricao());
        return  valores;
    }

    public static Categoria contentValuesToCategoria(ContentValues valores){
        Categoria categoria = new Categoria();

        categoria.setId(valores.getAsLong(BdTableCategorias._ID));
        categoria.setDescricao(valores.getAsString(BdTableCategorias.CAMPO_DESCRICAO));

        return categoria;
    }
}
