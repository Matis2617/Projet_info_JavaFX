/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

/**
 *
 * @author Matis
 */
import java.util.ArrayList;
public class Atelier {
    private int id;
    private String nom;
    ArrayList<Equipement> equipement;
    ArrayList<Operateur> operateur;
    private ChefAtelier ChefAtelier;
    ArrayList<Personne> personnes;

    public ArrayList<Operateur> getOperateur() {
        return operateur;
    }

    public void setOperateurs(ArrayList<Operateur> operateur) {
        this.operateur = operateur;
    }

        
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Equipement> getEquipement() {
        return equipement;
    }

    public void setEquipement(ArrayList<Equipement> equipement) {
        this.equipement = equipement;
    }

     public Atelier(int id, String nom, ArrayList<Equipement> equipement,ArrayList<Operateur> operateur) {
        this.id = id;
        this.nom = nom;
        this.equipement = equipement;
        this.operateur = operateur;
    }
     public ChefAtelier getChefAtelier() {
        return ChefAtelier;
    }

    public void setChefAtelier(ChefAtelier chefAtelier) {
        this.ChefAtelier = chefAtelier;
    }

    public ArrayList<Personne> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(ArrayList<Personne> personnes) {
        this.personnes = personnes;
    }
    
}
