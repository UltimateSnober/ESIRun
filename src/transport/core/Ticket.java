package transport.core;

import java.time.LocalDateTime;

public class Ticket extends TitreTransport {

    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;
    private Personne personne;

    public Ticket(Personne personne) {
        super(idCounter++, LocalDateTime.now());
        this.setPrix(50);
        this.personne = personne;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    @Override
    public boolean isValid(LocalDateTime date) {
        LocalDateTime expirationTime = this.getDateAchat().plusDays(1);
        return date.isBefore(expirationTime);
    }

    /**
     * Définit le compteur d'ID pour la génération des prochains tickets
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
