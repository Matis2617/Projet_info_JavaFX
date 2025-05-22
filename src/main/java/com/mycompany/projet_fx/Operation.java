/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

import java.io.Serializable;

/**
 *
 * @author Matis
 */
public class Operation implements Serializable{
    private int id_operation;
    private float dureeOperation;
    private String refEquipement;

    public int getId_operation() {
        return id_operation;
    }

    public void setId_operation(int id_operation) {
        this.id_operation = id_operation;
    }

    public Operation(int id_operation) {
        this.id_operation = id_operation;
    }

    public float getDureeOperation() {
        return dureeOperation;
    }

    public void setDureeOperation(float dureeOperation) {
        this.dureeOperation = dureeOperation;
    }

    public String getRefEquipement() {
        return refEquipement;
    }

    public void setRefEquipement(String refEquipement) {
        this.refEquipement = refEquipement;
    }
    
    public void affiche(){
    System.out.println("id_operation ="+id_operation);
    System.out.println("Durée de l'opération ="+dureeOperation+"minutes");
    System.out.println("Référence de l'équipement ="+refEquipement);
    }   
    
    public void setDureeOperationInput(){
        System.out.println("Quelle est la durée de cette opération ?");
        this.dureeOperation = Lire.f();
    }
}
