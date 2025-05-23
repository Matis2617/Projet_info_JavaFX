package com.mycompany.projet_fx.Model;

import com.mycompany.projet_fx.Model.Machine.ETAT;
import java.io.Serializable;

/**
 * Classe modèle pour un opérateur (MVC friendly)
 */
public class Operateur extends Personne implements Serializable {
    private String competences;
    private int id_op;
    private ETAT etat;

    // ----- CONSTRUCTEUR -----
    public Operateur(String idpersonne, String nom, String prenom, String competences, int id_op, ETAT etat) {
        super(idpersonne, nom, prenom);
        this.competences = competences;
        this.id_op = id_op;
        this.etat = etat;
    }

    // ----- GETTERS / SETTERS -----
    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }

    public int getId_op() { return id_op; }
    public void setId_op(int id_op) { this.id_op = id_op; }

    public ETAT getEtat() { return etat; }
    public void setEtat(ETAT etat) { this.etat = etat; }

    // ----- AFFICHAGE MVC (pour ListView, debug, etc) -----
    @Override
    public String toString() {
        return super.toString()
            + " | Compétences: " + competences
            + " | Id Opérateur: " + id_op
            + " | État: " + etat;
    }
}
