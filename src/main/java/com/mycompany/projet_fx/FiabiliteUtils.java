/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class FiabiliteUtils implements Serializable{

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter HEURE_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static void ecrireEtatMachines(List<Fiabilite> machines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("fiabilite.txt"))) {
            for (Fiabilite machine : machines) {
                
                LocalDate date = LocalDate.now();
                LocalTime heure = LocalTime.now();
                String evenement = genererEvenement();
                String operateur = genererOperateur();
                String cause = genererCause();

                String ligne = genererEtatMachine(machine, date, heure, evenement, operateur, cause);
                bw.write(ligne);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur d'écriture : " + e.getMessage());
        }
    }
    
    public static String genererEvenement(){
        System.out.println("Quel est l'évenement (A/D)");
        return Lire.s();
    }
    
    public static String genererOperateur(){
        System.out.println("Quel est l'operateur");
        return Lire.s();
    }
    
    public static String genererCause(){
        System.out.println("Quel est la cause");
        return Lire.s();
    }


    public static String genererEtatMachine(Fiabilite machine, LocalDate date, LocalTime heure, String evenement, String operateur, String cause) {
        return String.format("%s;%s;%s;%s;%s;%s",
                date.format(DATE_FORMAT),
                heure.format(HEURE_FORMAT),
                machine.getNomMachine(),
                evenement,
                operateur,
                cause);
    }
}