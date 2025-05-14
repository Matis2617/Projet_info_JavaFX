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
    private String nom;
    private String prenom;
    private String competences;
    private int id_op;
    private ETAT etat;

    @Override public String getNom() {
        return nom;
    }

    @Override public void setNom(String nom) {
        this.nom = nom;
    }

    @Override public String getPrenom() {
        return prenom;
    }

    @Override public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

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

    public Operateur(String nom, String prenom, String competences, int id_op, ETAT etat,String idpersonne,String Nom, String Prenom) {
        super(idpersonne,Nom,Prenom);
        this.nom = nom;
        this.prenom = prenom;
        this.competences = competences;
        this.id_op = id_op;
        this.etat = etat;
    }
    @Override public String affiche(){
    System.out.print("nom ="+nom);
    System.out.print("pronom ="+prenom);
    System.out.print("competences ="+competences);
    System.out.print("id_op ="+id_op);
    System.out.print("etat ="+etat);
    return "nom ="+nom+",prenom ="+prenom+"competences ="+competences+"id_op ="+id_op+"etat ="+etat;
}
}
