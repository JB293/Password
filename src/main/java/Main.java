import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("GENERATEUR DE MOT DE PASSE SECURISE (CLI)");
        System.out.println("=========================================");

        // Utilisation du try-with-resources pour fermer automatiquement le Scanner
        try (Scanner sc = new Scanner(System.in)) {
            generateurPassword generateur = new generateurPassword();
            ValidateurDocker validateur = new ValidateurDocker();

            int longueur = 0;
            boolean inclureMinuscule = false;
            boolean inclureMajuscule = false;
            boolean inclureChiffres = false;
            boolean inclureSymboles = false;

            // Boucle de configuration des critères
            while (true) {
                longueur = lireEntier(sc, "Entrez la longueur du mot de passe (min 8): ", 1);

                inclureMinuscule = demanderOption(sc, "Inclure des minuscules (abcd)");
                inclureMajuscule = demanderOption(sc, "Inclure des majuscules (ABCD)");
                inclureChiffres = demanderOption(sc, "Inclure des chiffres (0123)");
                inclureSymboles = demanderOption(sc, "Inclure des symboles (%@#)");

                int nombreDeCriteres = 0;
                if (inclureMinuscule) nombreDeCriteres++;
                if (inclureMajuscule) nombreDeCriteres++;
                if (inclureChiffres) nombreDeCriteres++;
                if (inclureSymboles) nombreDeCriteres++;

                if (nombreDeCriteres == 0) {
                    System.out.println("Erreur : Vous devez sélectionner au moins un type de caractère.\n");
                    continue;
                }

                if (longueur < nombreDeCriteres) {
                    System.out.printf("Erreur : Une longueur de %d est trop courte pour les %d types choisis !%n", longueur, nombreDeCriteres);
                    System.out.println("Veuillez recommencer.\n");
                    continue;
                }
                break;
            }

            // Mode rafale
            int quantite = lireEntier(sc, "Combien de mots de passe souhaitez-vous générer ? ", 1);

            System.out.println("\n=========================================");
            System.out.println("RESULTATS DE GENERATION DU MOT DE PASSE");
            System.out.println("=========================================");

            for (int i = 1; i <= quantite; i++) {
                String password = generateur.generate(longueur, inclureMinuscule, inclureMajuscule, inclureChiffres, inclureSymboles);
                String force = validateur.evaluerRobustesse(password);
                System.out.printf("[%d] %s --> [Indicateur: %s]%n", i, password, force);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Une erreur est survenue lors de la génération : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Une erreur imprévue est survenue : " + e.getMessage());
        }
    }

    // Méthode utilitaire pour valider la saisie des entiers de manière robuste
    private static int lireEntier(Scanner sc, String message, int min) {
        while (true) {
            System.out.print(message);
            String saisie = sc.next().trim();
            try {
                int valeur = Integer.parseInt(saisie);
                if (valeur >= min) {
                    return valeur;
                }
                System.out.printf("La valeur doit être supérieure ou égale à %d.%n", min);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
            }
        }
    }

    // Méthode de validation des options oui/non
    private static boolean demanderOption(Scanner sc, String message) {
        while (true) {
            System.out.print(message + " (o/n) : ");
            String reponse = sc.next().trim().toLowerCase();
            if (reponse.equals("o") || reponse.equals("oui")) {
                return true;
            } else if (reponse.equals("n") || reponse.equals("non")) {
                return false;
            }
            System.out.println("Réponse invalide. Tapez 'o' ou 'n'.");
        }
    }
}
