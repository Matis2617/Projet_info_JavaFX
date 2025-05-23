package com.mycompany.projet_fx.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une gamme de fabrication (liste d'opérations & équipements associés)
 */
public class Gamme implements Serializable {
    private String refGamme;
    private List<Operation> operations;
    private List<Equipement> listeEquipements;

    // --- Constructeurs ---
    public Gamme() {
        this.operations = new ArrayList<>();
        this.listeEquipements = new ArrayList<>();
    }

    public Gamme(List<Operation> operations) {
        this.operations = new ArrayList<>(operations);
        this.listeEquipements = new ArrayList<>();
    }

    // --- Getters/Setters ---
    public String getRefGamme() { return refGamme; }
    public void setRefGamme(String refGamme) { this.refGamme = refGamme; }

    public List<Operation> getOperations() { return operations; }
    public void setOperations(List<Operation> operations) { this.operations = new ArrayList<>(operations); }

    public List<Equipement> getListeEquipements() { return listeEquipements; }
    public void setListeEquipements(List<Equipement> listeEquipements) { this.listeEquipements = new ArrayList<>(listeEquipements); }

    // --- Méthodes métier ---
    /**
     * Génère une référence unique (optionnel, selon la logique souhaitée)
     */
    public void creerGamme() {
        this.refGamme = "GAMME_" + System.currentTimeMillis();
        this.operations = new ArrayList<>();
        this.listeEquipements = new ArrayList<>();
    }

    /**
     * Ajoute une opération et un équipement à la gamme.
     */
    public void ajouter(Operation operation, Equipement equipement) {
        this.operations.add(operation);
        this.listeEquipements.add(equipement);
    }

    /**
     * Vide la gamme (toutes opérations/équipements)
     */
    public void supprimerGamme() {
        this.operations.clear();
        this.listeEquipements.clear();
    }

    /**
     * Calcule le coût total de la gamme.
     * Attention : Cast Equipement → Machine : OK uniquement si tous les équipements sont bien des machines !
     */
    public float coutGamme() {
        float coutTotal = 0;
        for (Operation operation : this.operations) {
            for (Equipement equipement : this.listeEquipements) {
                if (equipement instanceof Machine) {
                    coutTotal += ((Machine) equipement).getC() * operation.getDureeOperation();
                }
            }
        }
        return coutTotal;
    }

    /**
     * Calcule la durée totale de la gamme (somme des durées des opérations).
     */
    public float dureeGamme() {
        float dureeTotale = 0;
        for (Operation operation : operations) {
            dureeTotale += operation.getDureeOperation();
        }
        return dureeTotale;
    }

    @Override
    public String toString() {
        return "Gamme{" +
                "refGamme='" + refGamme + '\'' +
                ", operations=" + operations +
                ", listeEquipements=" + listeEquipements +
                '}';
    }
}
