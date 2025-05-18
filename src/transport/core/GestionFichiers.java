package transport.core;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Classe responsable de la sauvegarde et du chargement des données
 */
public class GestionFichiers {

    // Chemin des fichiers de données
    private static final String DATA_DIR = "data";
    private static final String USAGERS_FILE = DATA_DIR + "/usagers.dat";
    private static final String TITRES_FILE = DATA_DIR + "/titres.dat";

    /**
     * Assure que le répertoire de données existe
     */
    private static void verifierRepertoires() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du répertoire de données: " + e.getMessage());
        }
    }

    /**
     * Sauvegarde les usagers dans un fichier
     */
    public static boolean sauvegarderUsagers() {
        verifierRepertoires();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USAGERS_FILE))) {
            List<Personne> usagers = Donnees.getUsagers();
            // Sauvegarde la liste des usagers
            oos.writeObject(usagers);

            // Sauvegarde la map des usagers par ID
            Map<Integer, Personne> usagersMap = new HashMap<>();
            for (Personne p : usagers) {
                usagersMap.put(p.getId(), p);
            }
            oos.writeObject(usagersMap);

            System.out.println("Sauvegardé " + usagers.size() + " usagers");
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des usagers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sauvegarde les titres de transport dans un fichier
     */
    public static boolean sauvegarderTitres() {
        verifierRepertoires();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TITRES_FILE))) {
            List<TitreTransport> titres = Donnees.getTitres();
            oos.writeObject(titres);

            System.out.println("Sauvegardé " + titres.size() + " titres de transport");
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des titres: " + e.getMessage());
            return false;
        }
    }

    /**
     * Charge les usagers depuis un fichier
     */
    @SuppressWarnings("unchecked")
    public static boolean chargerUsagers() {
        File file = new File(USAGERS_FILE);
        if (!file.exists()) {
            System.out.println("Aucun fichier d'usagers trouvé. Démarrage avec une liste vide.");
            return true;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Personne> usagers = (List<Personne>) ois.readObject();
            Map<Integer, Personne> usagersMap = (Map<Integer, Personne>) ois.readObject();

            // Réinitialiser les données existantes
            Donnees.resetUsagers();

            // Restaurer les données
            for (Personne p : usagers) {
                Donnees.importUsager(p);
            }

            // Restaurer le compteur d'ID à la valeur maximale + 1
            int maxId = usagers.stream()
                    .mapToInt(Personne::getId)
                    .max()
                    .orElse(0);
            Personne.setIdCounter(maxId + 1);

            System.out.println("Chargé " + usagers.size() + " usagers");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des usagers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Charge les titres de transport depuis un fichier
     */
    @SuppressWarnings("unchecked")
    public static boolean chargerTitres() {
        File file = new File(TITRES_FILE);
        if (!file.exists()) {
            System.out.println("Aucun fichier de titres trouvé. Démarrage avec une liste vide.");
            return true;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<TitreTransport> titres = (List<TitreTransport>) ois.readObject();

            // Réinitialiser les données existantes
            Donnees.resetTitres();

            // Restaurer les données
            for (TitreTransport t : titres) {
                Donnees.importTitre(t);
            }

            // Dans la méthode chargerTitres() de GestionFichiers
            int maxTicketId = 0;
            int maxCarteId = 9999; // Valeur par défaut

            for (TitreTransport t : titres) {
                int id = t.getId();
                if (t instanceof Ticket && id > maxTicketId) {
                    maxTicketId = id;
                } else if (t instanceof CarteNavigation && id > maxCarteId) {
                    maxCarteId = id;
                }
            }

            // Mettre à jour les compteurs
            if (maxTicketId > 0) {
                Ticket.setIdCounter(maxTicketId + 1);
            }
            if (maxCarteId > 9999) {
                CarteNavigation.setIdCounter(maxCarteId + 1);
            }

            System.out.println("Chargé " + titres.size() + " titres de transport");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des titres: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sauvegarde toutes les données de l'application
     */
    public static boolean sauvegarderDonnees() {
        boolean usagersOk = sauvegarderUsagers();
        boolean titresOk = sauvegarderTitres();

        return usagersOk && titresOk;
    }

    /**
     * Charge toutes les données de l'application
     */
    public static boolean chargerDonnees() {
        boolean usagersOk = chargerUsagers();
        boolean titresOk = chargerTitres();

        return usagersOk && titresOk;
    }
}
