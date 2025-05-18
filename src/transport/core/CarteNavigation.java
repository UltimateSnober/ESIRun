package transport.core;

import java.time.LocalDateTime;

public class CarteNavigation extends TitreTransport {

    private static final long serialVersionUID = 1L;
    private static int idCounter = 10000; // Commencer à 10000 pour différencier des tickets

    private TypeCarte typeCarte;
    private Personne personne;

    public CarteNavigation(int id, TypeCarte typeCarte, Personne personne) {
        super(id, LocalDateTime.now());
        this.typeCarte = typeCarte;
        this.personne = personne;

        switch (typeCarte) {
            case JUNIOR ->
                super.setPrix(5000 * 0.7);
            case SENIOR ->
                super.setPrix(5000 * 0.75);
            case SOLIDARITE ->
                super.setPrix(5000 * 0.5);
            case PARTENAIRE ->
                super.setPrix(5000 * 0.6);
            default ->
                super.setPrix(5000);
        }
    }

    /**
     * Constructeur qui utilise un ID auto-généré
     */
    public CarteNavigation(TypeCarte typeCarte, Personne personne) {
        this(idCounter++, typeCarte, personne);
    }

    public TypeCarte getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(TypeCarte typeCarte) {
        this.typeCarte = typeCarte;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    @Override
    public boolean isValid(LocalDateTime date) {
        LocalDateTime expirationTime = this.getDateAchat().plusYears(1);
        return date.isBefore(expirationTime);
    }

    /**
     * Définit le compteur d'ID pour la génération des prochaines cartes
     *
     * @param value La nouvelle valeur du compteur
     */
    public static void setIdCounter(int value) {
        idCounter = value;
    }

    /**
     * Obtient la valeur actuelle du compteur d'ID
     *
     * @return La valeur actuelle du compteur
     */
    public static int getIdCounter() {
        return idCounter;
    }
}
