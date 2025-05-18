package transport.core;

import java.time.LocalDate;

public class Usager extends Personne {

    private static final long serialVersionUID = 1L;

    public Usager(String nom, String prenom, LocalDate dateNaissance, boolean handicape) {
        super(nom, prenom, dateNaissance, handicape);
    }

    @Override
    public int getId() {
        return super.getId();
    }
}
