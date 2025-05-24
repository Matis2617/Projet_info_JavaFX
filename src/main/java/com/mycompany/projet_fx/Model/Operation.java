package com.mycompany.projet_fx.Model;

import java.io.Serializable;

/**
 * Représente une opération dans l'atelier (modèle MVC)
 */
public class Operation implements Serializable {
    private int id_operation;
    private String description;
    private float dureeOperation;

    // ----- CONSTRUCTEURS -----
    public Operation(int id_operation, String description) {
        this.id_operation = id_operation;
        this.description = description;
    }

    // ----- GETTERS & SETTERS -----
    public int getId_operation() { return id_operation; }
    public void setId_operation(int id_operation) { this.id_operation = id_operation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getDureeOperation() { return dureeOperation; }
    public void setDureeOperation(float dureeOperation) { this.dureeOperation = dureeOperation; }

    @Override
    public String toString() {
        return "Opération #" + id_operation + " : " + description;
    }
}
