package com.mycompany.projet_fx.Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe utilitaire pour la gestion de la fiabilité des machines (sauvegarde dans fichier).
 * Toutes les informations doivent être fournies par la vue/contrôleur.
 */
public class FiabiliteUtils implements Serializable {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter HEURE_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Écrit les états des machines dans un fichier.
     * 
     * @param machines La liste des objets Fiabilite (modèle)
     * @param evenements Liste des événements (ex: "A" ou "D") correspondant aux machines
     * @param operateurs Liste des opérateurs correspondant aux machines
     * @param causes Liste des causes correspondant aux machines
     * (=> tous ces paramètres doivent être de même taille)
     */
    public static void ecrireEtatMachines(List<Fiabilite> machines, List<String> evenements, List<String> operateurs, List<String> causes) {
        if (machines.size() != evenements.size() || machines.size() != operateurs.size() || machines.size() != causes.size()) {
            throw new IllegalArgumentException("Toutes les listes doivent avoir la même taille !");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("fiabilite.txt"))) {
            for (int i = 0; i < machines.size(); i++) {
                Fiabilite machine = machines.get(i);
                LocalDate date = LocalDate.now();
                LocalTime heure = LocalTime.now();
                String evenement = evenements.get(i);
                String operateur = operateurs.get(i);
                String cause = causes.get(i);

                String ligne = genererEtatMachine(machine, date, heure, evenement, operateur, cause);
                bw.write(ligne);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur d'écriture : " + e.getMessage());
        }
    }

    /**
     * Génère la ligne à écrire pour une machine.
     */
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
