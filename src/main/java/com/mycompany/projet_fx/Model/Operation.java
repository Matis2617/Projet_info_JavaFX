package com.mycompany.projet_fx.Model;

import java.io.Serializable;

public class Operation implements Serializable {
    private int id_operation;
    private String description;
    private float duree; // durée en heures (ou minutes si tu préfères)

    public Operation(int id_operation, String description, float duree) {
        this.id_operation = id_operation;
        this.description = description;
        this.duree = duree;
    }

    public int getId_operation() { return id_operation; }
    public void setId_operation(int id_operation) { this.id_operation = id_operation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getDuree() { return duree; }
    public void setDuree(float duree) { this.duree = duree; }

    @Override
    public String toString() {
        return id_operation + " - " + description + " (" + duree + "h)";
    }
}
