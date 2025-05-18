package transport.core;

import java.time.LocalDateTime;

public class CarteNavigation extends TitreTransport {

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

}
