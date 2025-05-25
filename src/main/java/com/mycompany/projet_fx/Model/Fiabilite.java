package com.mycompany.projet_fx.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fiabilite implements Serializable {
    private Machine machine;
    private LocalDateTime debutPanne;
    private LocalDateTime finPanne;
    private String cause;

    public Fiabilite(Machine machine, LocalDateTime debutPanne, LocalDateTime finPanne, String cause) {
        this.machine = machine;
        this.debutPanne = debutPanne;
        this.finPanne = finPanne;
        this.cause = cause;
    }

    public Machine getMachine() { return machine; }
    public void setMachine(Machine machine) { this.machine = machine; }

    public LocalDateTime getDebutPanne() { return debutPanne; }
    public void setDebutPanne(LocalDateTime debutPanne) { this.debutPanne = debutPanne; }

    public LocalDateTime getFinPanne() { return finPanne; }
    public void setFinPanne(LocalDateTime finPanne) { this.finPanne = finPanne; }

    public String getCause() { return cause; }
    public void setCause(String cause) { this.cause = cause; }

    public void enregistrerPanne(LocalDateTime debutPanne, String cause) {
        this.debutPanne = debutPanne;
        this.cause = cause;
    }

    public void enregistrerReprise(LocalDateTime finPanne) {
        this.finPanne = finPanne;
    }

    public String toFileString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String debutDate = debutPanne.format(dateFormatter);
        String debutTime = debutPanne.format(timeFormatter);
        String finDate = finPanne.format(dateFormatter);
        String finTime = finPanne.format(timeFormatter);

        return String.format("%s;%s;%s;A;OPXXX;%s\n%s;%s;%s;D;OPXXX;ok",
                debutDate, debutTime, machine.getDmachine(), cause,
                finDate, finTime, machine.getDmachine());
    }

    @Override
    public String toString() {
        return "Fiabilite{" + "machine=" + machine.getDmachine()
            + ", debutPanne=" + debutPanne
            + ", finPanne=" + finPanne
            + ", cause=" + cause + '}';
    }
}