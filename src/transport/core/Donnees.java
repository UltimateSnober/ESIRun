package transport.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for managing all data in the ESIRun system.
 */
public class Donnees {

    private static List<Personne> usagers = new ArrayList<>();
    private static List<TitreTransport> titres = new ArrayList<>();
    private static Map<Integer, Personne> usagerParId = new HashMap<>();

    /**
     * Adds a user to the system
     *
     * @param usager The user to add
     */
    public static void ajouterUsager(Personne usager) {
        // The ID is already set in the Personne constructor
        usagers.add(usager);
        usagerParId.put(usager.getId(), usager);
        System.out.println("Ajout de l'usager: " + usager + " avec ID: " + usager.getId());
    }

    /**
     * Finds a user by their ID
     *
     * @param id The user ID to search for
     * @return The found person or null if not found
     */
    public static Personne trouverUsagerParId(int id) {
        return usagerParId.get(id);
    }

    /**
     * Returns all users in the system
     *
     * @return A copy of the users list
     */
    public static List<Personne> getUsagers() {
        return new ArrayList<>(usagers);
    }

    /**
     * Adds a transportation title to the system
     *
     * @param titre The title to add
     */
    public static void ajouterTitre(TitreTransport titre) {
        titres.add(titre);
        System.out.println("Ajout du titre: " + titre);
    }

    /**
     * Returns all transportation titles in the system
     *
     * @return A copy of the titles list
     */
    public static List<TitreTransport> getTitres() {
        return new ArrayList<>(titres);
    }

    /**
     * Finds a transportation title by ID
     *
     * @param id The title ID to search for
     * @return The found title or null if not found
     */
    public static TitreTransport trouverTitreParId(int id) {
        for (TitreTransport titre : titres) {
            if (titre.getId() == id) {
                return titre;
            }
        }
        return null;
    }

    /**
     * Gets all titles associated with a specific user
     *
     * @param usagerId The user ID to find titles for
     * @return List of titles owned by the user
     */
    public static List<TitreTransport> getTitresParUsager(int usagerId) {
        List<TitreTransport> titresUsager = new ArrayList<>();

        for (TitreTransport titre : titres) {
            if (titre instanceof CarteNavigation) {
                CarteNavigation carte = (CarteNavigation) titre;
                Personne personne = carte.getPersonne();
                if (personne != null && personne.getId() == usagerId) {
                    titresUsager.add(titre);
                }
            }
        }

        return titresUsager;
    }

    /**
     * Resets all data in the system (for testing purposes) Note: This does not
     * reset the ID counter in Personne class.
     */
    public static void resetData() {
        usagers.clear();
        titres.clear();
        usagerParId.clear();
    }

    /**
     * Returns a string representation of a user to display in UI
     */
    public static String getUsagerDisplayString(int id) {
        Personne p = trouverUsagerParId(id);
        if (p == null) {
            return null;
        }

        return p.getId() + " - " + p.getPrenom() + " " + p.getNom();
    }
}
