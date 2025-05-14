/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

import com.mycompany.projet_fx.Machine.ETAT;
//permet d'utilis/*er ETAT dans cette classe

/**
 *
 * @author Matis
 */
public class Operateur extends Personne {
    private String competences;
    private int id_op;
    private ETAT etat;

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

    public Operateur(String idpersonne,String Nom, String Prenom, String competences, int id_op, ETAT etat) {
        super(idpersonne,Nom,Prenom);
        this.competences = competences;
        this.id_op = id_op;
        this.etat = etat;
    }
    @Override public String affiche(){
    return "competences ="+competences+"id_op ="+id_op+"etat ="+etat;
}
}
