/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

import java.io.Serializable;

/**
 *
 * @author Matis
 */
public class ChefAtelier extends Personne implements Serializable{

    public ChefAtelier(String idpersonne, String nom, String prenom) {
        super(idpersonne, nom, prenom);
    }
    @Override
    public String affiche() {
        return super.affiche() + ", Role = Chef d'Atelier";
    }
}
