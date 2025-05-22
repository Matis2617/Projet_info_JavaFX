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
public class Personne implements Serializable{
    private String idpersonne;
    private String Nom;
    private String Prenom;

    public String getIdpersonne() {
        return idpersonne;
    }

    public void setIdpersonne(String idpersonne) {
        this.idpersonne = idpersonne;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        this.Prenom = prenom;
    }

    public Personne(String idpersonne, String nom, String prenom) {
        this.idpersonne = idpersonne;
        this.Nom = nom;
        this.Prenom = prenom;
    }
    public String affiche(){
    return "identifiant = "+idpersonne+", Nom = "+Nom+", Prenom ="+Prenom;
}
}
