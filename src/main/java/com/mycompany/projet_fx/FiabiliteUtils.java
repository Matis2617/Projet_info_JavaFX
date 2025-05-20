/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FiabiliteUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter HEURE_FORMAT = DateTimeFormatter.ofPattern("HHmm");

    // Structure d’un événement dans le fichier
    private static class Evenement {
        LocalDateTime dateTime;
        String type;

        public Evenement(LocalDate date, LocalTime heure, String type) {
            this.dateTime = LocalDateTime.of(date, heure);
            this.type = type;
        }
    }

    public static List<Fiabilite> analyserFichier(String cheminFichier) {
        Map<String, List<Evenement>> mapEvenements = new HashMap<>();
        List<Fiabilite> listeFiabilites = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;

            while ((ligne = br.readLine()) != null) {
                if (ligne.trim().isEmpty() || ligne.contains("End Of File")) continue;

                String[] parts = ligne.trim().split("\\s+");
                if (parts.length < 4) continue;

                LocalDate date = LocalDate.parse(parts[0], DATE_FORMAT);
                LocalTime heure = LocalTime.parse(parts[1].replace(":", ""), HEURE_FORMAT);
                String machine = parts[2];
                String type = parts[3]; // A ou D

                mapEvenements
                        .computeIfAbsent(machine, k -> new ArrayList<>())
                        .add(new Evenement(date, heure, type));
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
            return listeFiabilites;
        }

        // Traitement par machine
        for (String nomMachine : mapEvenements.keySet()) {
            List<Evenement> evenements = mapEvenements.get(nomMachine);
            evenements.sort(Comparator.comparing(evt -> evt.dateTime));

            Fiabilite fiabilite = new Fiabilite(nomMachine);

            LocalDateTime aDebut = null;
            LocalDateTime aFin = null;

            for (Evenement evt : evenements) {
                if (evt.type.equals("A")) {
                    aDebut = evt.dateTime;
                } else if (evt.type.equals("D") && aDebut != null) {
                    aFin = evt.dateTime;

                    long minutesPanne = Duration.between(aDebut, aFin).toMinutes();
                    fiabilite.ajouterTempsDePanne((int) minutesPanne);
                    aDebut = null;
                }
            }

            // Calcul du temps total (observation) = de 06:00 à 20:00 par jour d'activité
            Set<LocalDate> jours = new HashSet<>();
            for (Evenement evt : evenements) {
                jours.add(evt.dateTime.toLocalDate());
            }

            int minutesParJour = 14 * 60;
            int totalTempsObservation = jours.size() * minutesParJour;

            int tempsDeMarche = totalTempsObservation - fiabilite.getTotalTempsDePanne();
            fiabilite.setTotalTempsDeMarche(tempsDeMarche);

            listeFiabilites.add(fiabilite);
        }

        // Tri décroissant de fiabilité
        listeFiabilites.sort(Comparator.comparingDouble(Fiabilite::calculerFiabilite).reversed());
        return listeFiabilites;
    }
}