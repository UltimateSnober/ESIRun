package transport.core;

import java.time.LocalDate;

public class Employe extends Personne {

    private String matricule;
    private TypeFonction fonction;

    public Employe(String nom, String prenom, LocalDate dateNaissance, boolean handicape, String matricule,
            TypeFonction fonction) {
        super(nom, prenom, dateNaissance, handicape);
        this.matricule = matricule;
        this.fonction = fonction;
    }

    public String getMatricule() {
        return matricule;
    }

    public TypeFonction getFonction() {
        return fonction;
    }
}
