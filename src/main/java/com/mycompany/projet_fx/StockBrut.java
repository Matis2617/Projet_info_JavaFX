/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

/**
 *
 * @author Matis
 */
import java.io.Serializable;
import java.util.ArrayList;

public class StockBrut implements Serializable{
   private ArrayList <Produit> produits;
   
   public StockBrut(){
       this.produits = new ArrayList<>();
   }
   public void ajouterProduit (Produit produit){
       produits.add(produit);
   }
   public void supprimerProduit (Produit produit){
       produits.remove(produit);
   }
   public int getNombreProduits(){
       return produits.size();
   }
   public void affiche() {
    System.out.println("Produit : id = " + id + ", code = " + code);
   }

   public void afficherStock(){
       for (Produit produit : produits){
           produit.affiche();
       }
   }
}
