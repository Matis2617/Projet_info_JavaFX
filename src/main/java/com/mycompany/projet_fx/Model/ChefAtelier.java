package com.mycompany.projet_fx.Model;

import java.io.Serializable;

public class ChefAtelier extends Personne implements Serializable {

    public ChefAtelier(String idpersonne, String nom, String prenom) {
        super(idpersonne, nom, prenom);
    }
    @Override
    public String affiche() {
        return super.affiche() + ", Role = Chef d'Atelier";
    }
}
