package com.mycompany.projet_fx.Model;

import java.io.Serializable;

public class Personne implements Serializable {
    private String idpersonne;
    private String nom;
    private String prenom;

    // ---- CONSTRUCTEUR ----
    public Personne(String idpersonne, String nom, String prenom) {
        this.idpersonne = idpersonne;
        this.nom = nom;
        this.prenom = prenom;
    }

    // ---- GETTERS / SETTERS ----
    public String getIdpersonne() { return idpersonne; }
    public void setIdpersonne(String idpersonne) { this.idpersonne = idpersonne; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    // ---- AFFICHAGE POUR MVC ----
    @Override
    public String toString() {
        return "ID: " + idpersonne + " | Nom: " + nom + " | Prénom: " + prenom;
    }
}
