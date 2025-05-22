/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class FiabiliteUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter HEURE_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static void ecrireEtatMachines(String cheminFichier, List<Fiabilite> machines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(cheminFichier))) {
            for (Fiabilite machine : machines) {
                String ligne = genererEtatMachine(machine);
                bw.write(ligne);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur d'écriture : " + e.getMessage());
        }
    }

    private static String genererEtatMachine(Fiabilite machine) {
        Random random = new Random();
        LocalDate date = LocalDate.now(); // Date actuelle pour l'exemple
        LocalTime heure = LocalTime.of(random.nextInt(14) + 6, random.nextInt(60)); // Heure entre 06:00 et 20:00
        String evenement = random.nextBoolean() ? "A" : "D"; // A pour Arrêt, D pour Démarrage
        String operateur = "OP" + (random.nextInt(300) + 1); // Opérateur aléatoire
        String cause = evenement.equals("A") ? getCauseAleatoire() : "ok"; // Cause aléatoire pour les arrêts

        return String.format("%s;%s;%s;%s;%s;%s",
                date.format(DATE_FORMAT),
                heure.format(HEURE_FORMAT),
                machine.getNomMachine(),
                evenement,
                operateur,
                cause);
    }

    private static String getCauseAleatoire() {
        String[] causes = {"panne", "accident", "maintenance"};
        Random random = new Random();
        return causes[random.nextInt(causes.length)];
    }
}
