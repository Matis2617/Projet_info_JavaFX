package com.mycompany.projet_fx.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FiabiliteUtils {

    // Structure de données pour stocker les périodes de panne de chaque machine
    public static class PeriodePanne {
        public LocalDateTime debut, fin;
        public String cause;

        public PeriodePanne(LocalDateTime debut, LocalDateTime fin, String cause) {
            this.debut = debut;
            this.fin = fin;
            this.cause = cause;
        }
    }

    // 1°) Lire et extraire les événements (pannes) par machine
    public static Map<String, List<PeriodePanne>> chargerPannesMachines(String fichier) {
        Map<String, List<PeriodePanne>> map = new HashMap<>();
        Map<String, LocalDateTime> lastA = new HashMap<>();
        Map<String, String> lastCause = new HashMap<>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null && !ligne.equalsIgnoreCase("End Of File")) {
                // Ignore les lignes vides ou tirets
                if (ligne.trim().isEmpty() || ligne.startsWith("-")) continue;

                String[] parts = ligne.split("\\s+");
                if (parts.length < 6) continue;

                String dateStr = parts[0];
                String heureStr = parts[1].replace(":", ""); // Certains fichiers peuvent contenir "06 :42"
                heureStr = heureStr.replace(" ", "");
                if (heureStr.length() < 4) heureStr = "0" + heureStr; // Ex: 6:42 → 06:42

                // Correction pour que 642 devienne 06:42
                if (heureStr.length() == 3) heureStr = "0" + heureStr;
                heureStr = heureStr.substring(0,2) + ":" + heureStr.substring(2);

                String machine = parts[2];
                String evenement = parts[3];
                String operateur = parts[4];
                String cause = parts[5];

                LocalDateTime dateHeure = LocalDateTime.parse(dateStr + " " + heureStr, dateFormat);

                if (evenement.equals("A")) {
                    lastA.put(machine, dateHeure);
                    lastCause.put(machine, cause);
                } else if (evenement.equals("D")) {
                    // Si on a eu une panne démarrée précédemment
                    if (lastA.containsKey(machine)) {
                        LocalDateTime debut = lastA.get(machine);
                        String c = lastCause.get(machine);
                        map.putIfAbsent(machine, new ArrayList<>());
                        map.get(machine).add(new PeriodePanne(debut, dateHeure, c));
                        lastA.remove(machine);
                        lastCause.remove(machine);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur de lecture: " + e.getMessage());
        }
        return map;
    }

    // 2°) Calcul de la fiabilité par machine (formule simple : 1 - (temps en panne / temps total observation))
    public static Map<String, Double> calculerFiabilites(Map<String, List<PeriodePanne>> pannesMachines,
                                                         LocalDateTime heureDebut, LocalDateTime heureFin) {
        Map<String, Double> fiabilites = new HashMap<>();
        long tempsTotal = java.time.Duration.between(heureDebut, heureFin).toMinutes();

        for (String machine : pannesMachines.keySet()) {
            long totalPannes = 0;
            for (PeriodePanne periode : pannesMachines.get(machine)) {
                long duree = java.time.Duration.between(periode.debut, periode.fin).toMinutes();
                if (duree > 0) totalPannes += duree;
            }
            double fiab = 1.0 - (double)totalPannes / (double)tempsTotal;
            fiabilites.put(machine, Math.max(0, fiab)); // pour éviter valeurs négatives
        }
        return fiabilites;
    }

    // 3°) Trie les machines par fiabilité décroissante
    public static List<Map.Entry<String, Double>> trierMachinesParFiabilite(Map<String, Double> fiabilites) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(fiabilites.entrySet());
        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return list;
    }
}
