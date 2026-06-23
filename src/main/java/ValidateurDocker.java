import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ValidateurDocker {

    public String evaluerRobustesse(String password) {
        try {
            // Le score est calculé dans Docker pour garder la validation séparée du code Java.
            ProcessBuilder processBuilder = new ProcessBuilder(
                "docker", "run", "--rm", "password-validator", password
            );

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String resultat = reader.readLine();
            int codeRetour = process.waitFor();

            if (codeRetour != 0) {
                String erreur = errorReader.readLine();
                if (erreur != null && !erreur.isBlank()) {
                    System.err.println("Erreur Docker : " + erreur);
                }
                return "Indeterminee";
            }

            if (resultat == null || resultat.isBlank()) {
                return "Indeterminee";
            }

            int score = Integer.parseInt(resultat.trim());
            return convertirScore(score);

        } catch (NumberFormatException e) {
            System.err.println("Reponse Docker invalide : " + e.getMessage());
            return "Indeterminee";
        } catch (IOException | InterruptedException e) {
            System.err.println("Erreur Docker : " + e.getMessage());
            return "Erreur de validation";
        }
    }

    private String convertirScore(int score) {
        return switch (score) {
            case 0 -> "Tres faible";
            case 1 -> "Faible";
            case 2 -> "Moyen";
            case 3 -> "Fort";
            case 4 -> "Tres fort";
            default -> "Inconnu";
        };
    }
}
