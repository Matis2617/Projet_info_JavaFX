package com.mycompany.projet_fx.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Atelier implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nom;
    private ArrayList<Equipement> equipements;     // nom pluriel plus clair
    private ArrayList<Operateur> operateurs;
    private ChefAtelier chefAtelier;
    private ArrayList<Personne> personnes;
    private ArrayList<Poste> postes = new ArrayList<>();
    private ArrayList<Operation> operations = new ArrayList<>();

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

    public ChefAtelier getChefAtelier() { return chefAtelier; }
    public void setChefAtelier(ChefAtelier chefAtelier) { this.chefAtelier = chefAtelier; }

    public ArrayList<Personne> getPersonnes() { return personnes; }
    public void setPersonnes(ArrayList<Personne> personnes) { this.personnes = personnes; }

    public ArrayList<Poste> getPostes() { return postes; }
    public void setPostes(ArrayList<Poste> postes) { this.postes = postes; }

    public ArrayList<Operation> getOperations() { return operations; }
    public void setOperations(ArrayList<Operation> operations) { this.operations = operations; }
}
