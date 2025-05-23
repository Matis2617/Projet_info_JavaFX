package com.mycompany.projet_fx.Model;

import java.io.Serializable;

/**
 * Représente une opération dans l'atelier (modèle MVC)
 */
public class Operation implements Serializable {
    private int id_operation;
    private float dureeOperation;
    private String refEquipement;

    // ----- CONSTRUCTEURS -----
    public Operation(int id_operation) {
        this.id_operation = id_operation;
    }

    public Operation(int id_operation, float dureeOperation, String refEquipement) {
        this.id_operation = id_operation;
        this.dureeOperation = dureeOperation;
        this.refEquipement = refEquipement;
    }

    // ----- GETTERS & SETTERS -----
    public int getId_operation() { return id_operation; }
    public void setId_operation(int id_operation) { this.id_operation = id_operation; }

    public float getDureeOperation() { return dureeOperation; }
    public void setDureeOperation(float dureeOperation) { this.dureeOperation = dureeOperation; }

    public String getRefEquipement() { return refEquipement; }
    public void setRefEquipement(String refEquipement) { this.refEquipement = refEquipement; }

    // ----- AUTRES MÉTHODES -----
    /**
     * Affiche une description lisible de l'opération (pour debug/log)
     */
    @Override
    public String toString() {
        return "Opération #" + id_operation
            + " | Durée : " + dureeOperation + " min"
            + " | Réf. Équipement : " + refEquipement;
    }
}
