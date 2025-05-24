package com.mycompany.projet_fx.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Atelier implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nom;
    private ArrayList<Equipement> equipements;
    private ArrayList<Operateur> operateurs;
    private ArrayList<Personne> personnes;
    private ArrayList<Poste> postes = new ArrayList<>();
    private ArrayList<Operation> operations = new ArrayList<>();
    private ArrayList<Gamme> gammes = new ArrayList<>();
    private ArrayList<Produit> produits = new ArrayList<>(); // AJOUT ICI

    // Constructeur
    public Atelier(int id, String nom, ArrayList<Equipement> equipements, ArrayList<Operateur> operateurs, ArrayList<Personne> personnes) {
        this.id = id;
        this.nom = nom;
        this.equipements = equipements;
        this.operateurs = operateurs;
        this.personnes = personnes;
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public ArrayList<Equipement> getEquipements() { return equipements; }
    public void setEquipements(ArrayList<Equipement> equipements) { this.equipements = equipements; }

    public ArrayList<Operateur> getOperateurs() { return operateurs; }
    public void setOperateurs(ArrayList<Operateur> operateurs) { this.operateurs = operateurs; }

    public ArrayList<Personne> getPersonnes() { return personnes; }
    public void setPersonnes(ArrayList<Personne> personnes) { this.personnes = personnes; }

    public ArrayList<Poste> getPostes() { return postes; }
    public void setPostes(ArrayList<Poste> postes) { this.postes = postes; }

    public ArrayList<Operation> getOperations() { return operations; }
    public void setOperations(ArrayList<Operation> operations) { this.operations = operations; }

    public ArrayList<Gamme> getGammes() { return gammes; }
    public void setGammes(ArrayList<Gamme> gammes) { this.gammes = gammes; }

    // ---- PRODUITS ----
    public ArrayList<Produit> getProduits() { return produits; }
    public void setProduits(ArrayList<Produit> produits) { this.produits = produits; }
}
