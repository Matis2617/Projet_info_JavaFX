package com.mycompany.projet_fx;

import java.io.Serializable;

public class Produit implements Serializable {
    private int code;
    private String id;

    public Produit(int code, String id) {
        this.code = code;
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public String getId() {
        return id;
    }

    // (optionnel) pour une belle liste :
    @Override
    public String toString() {
        return id + " (Code: " + code + ")";
    }
}
