package transport.core;

import java.time.LocalDateTime;

public class Ticket extends TitreTransport {

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

}
