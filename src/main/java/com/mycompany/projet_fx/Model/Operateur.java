package com.mycompany.projet_fx.Model;

import com.mycompany.projet_fx.Model.Machine.ETAT;

/**
 * Classe modèle pour un opérateur (MVC friendly)
 */
public class Operateur{
    private String competences;
    private int id_op;
    private ETAT etat;
    private String nom;
    private String prenom;

    // ----- CONSTRUCTEUR -----
    public Operateur(String nom, String prenom, String competences, int id_op, ETAT etat) {
        this.nom = nom;
        this.prenom = prenom;
        this.competences = competences;
        this.id_op = id_op;
        this.etat = etat;
    }

    // ----- GETTERS / SETTERS -----

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public int getId_op() {
        return id_op;
    }

    public void setId_op(int id_op) {
        this.id_op = id_op;
    }

    public ETAT getEtat() {
        return etat;
    }

    public void setEtat(ETAT etat) {
        this.etat = etat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    // ----- AFFICHAGE MVC (pour ListView, debug, etc) -----

    @Override
    public String toString() {
        return "Operateur{" + "competences=" + competences + ", id_op=" + id_op + ", etat=" + etat + ", nom=" + nom + ", prenom=" + prenom + '}';
    }
    
}
