package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private boolean handicape;
    private static int idCounter = 1;

    public Personne(String nom, String prenom, LocalDate dateNaissance, boolean handicape) {
        this.id = idCounter++;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.handicape = handicape;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public boolean isHandicape() {
        return handicape;
    }

    public void setHandicape(boolean handicape) {
        this.handicape = handicape;
    }

    /**
     * Définit le compteur d'ID pour la génération des prochains identifiants
     *
     * @param value La nouvelle valeur du compteur
     */
    public static void setIdCounter(int value) {
        idCounter = value;
    }

}
