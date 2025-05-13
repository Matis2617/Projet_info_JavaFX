/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet;

/**
 *
 * @author Matis
 */
public class Equipement {
    private int id_equipement;
    private String refEquipement;

    public String getRefEquipement() {
        return refEquipement;
    }

    public void setRefEquipement(String refEquipement) {
        this.refEquipement = refEquipement;
    }
    
    public int getId_equipement() {
        return id_equipement;
    }

    public void setId_equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }

    public Equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }
   
   public void affiche(){
    System.out.print("id_equipement ="+id_equipement);
}
}
