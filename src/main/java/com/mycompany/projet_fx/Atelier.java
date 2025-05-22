package com.mycompany.projet_fx;

import java.io.Serializable;
import java.util.ArrayList;

public class Atelier implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nom;
    private ArrayList<Equipement> equipement;
    private ArrayList<Operateur> operateur;
    private ChefAtelier ChefAtelier;
    private ArrayList<Personne> personne;
    private ArrayList<Poste> postes = new ArrayList<>(); // Ajouté pour la persistance des postes

    // Constructeur
    public Atelier(int id, String nom, ArrayList<Equipement> equipement, ArrayList<Operateur> operateur, ArrayList<Personne> personne) {
        this.id = id;
        this.nom = nom;
        this.equipement = equipement;
        this.operateur = operateur;
        this.personne = personne;
    }

    // Getters/Setters (ajoute ceux pour postes)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public ArrayList<Equipement> getEquipements() { return equipement; }
    public void setEquipements(ArrayList<Equipement> equipement) { this.equipement = equipement; }

    public ArrayList<Operateur> getOperateurs() { return operateur; }
    public void setOperateurs(ArrayList<Operateur> operateur) { this.operateur = operateur; }

    public ChefAtelier getChefAtelier() { return ChefAtelier; }
    public void setChefAtelier(ChefAtelier chefAtelier) { this.ChefAtelier = chefAtelier; }

    public ArrayList<Personne> getPersonnes() { return personne; }
    public void setPersonnes(ArrayList<Personne> personne) { this.personne = personne; }

    public ArrayList<Poste> getPostes() { return postes; }
    public void setPostes(ArrayList<Poste> postes) { this.postes = postes; }
}
