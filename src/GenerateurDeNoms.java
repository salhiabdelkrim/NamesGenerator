import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateurDeNoms {
    private final double[][] matriceMarkov;
    private final List<Lettre> lettres;
    private final Random random = new Random();

    public GenerateurDeNoms(double[][] matriceMarkov, Map<Lettre, Integer> statistiques) {
        this.matriceMarkov = matriceMarkov;
        this.lettres = new ArrayList<>(statistiques.keySet()); // ordre correspondant à la matrice
    }

    /**méthode pour générer un nom d'une façon aléatoire depuis la matrice de transition */
    public String genererNom(int longueurMax) {
        StringBuilder mot = new StringBuilder();

        // Étape 1 : choisir la première lettre (depuis l'espace) aléatoirement selon les probabilités
        int indexEspace = lettres.indexOf(new Lettre(' '));
        int index = choisirPremiereLettreAleatoire(indexEspace);
        if (index == -1) return ""; // rien à générer

        Lettre premiereLettre = lettres.get(index);
        mot.append(premiereLettre.getLettre());

        // Étape 2 : choisir les lettres suivantes de façon déterministe (la plus probable à chaque étape)
        for (int i = 1; i < longueurMax; i++) {
            index = choisirSuivantDeterministe(index);
            if (index == -1) break;

            Lettre suivante = lettres.get(index);
            char c = suivante.getLettre();

            if (c == ' ') break; // on arrête si on retombe sur un espace
            mot.append(c);
        }

        return mot.toString();
    }

    // Choix aléatoire pondéré de la 1ère lettre à partir des probabilités de la ligne ' '
    private int choisirPremiereLettreAleatoire(int indexEspace) {
        double[] proba = matriceMarkov[indexEspace];

        // Tirage aléatoire pondéré
        double rand = ThreadLocalRandom.current().nextDouble(); // équivalent à [0.0, 1.0)
        double cumulative = 0.0;

        for (int i = 0; i < proba.length; i++) {
            Lettre candidate = lettres.get(i);
            if (candidate.getLettre() == ' ') continue; // on ignore les espaces en tant que lettre

            cumulative += proba[i];
            if (rand <= cumulative) {
                return i;
            }
        }

        return -1; // aucun choix possible
    }

    // Choix déterministe : lettre ayant la probabilité max depuis l'index courant
    private int choisirSuivantDeterministe(int indexCourant) {
        double maxProba = -1.0;
        int maxIndex = -1;

        for (int i = 0; i < matriceMarkov[indexCourant].length; i++) {
            Lettre candidate = lettres.get(i);
            if (candidate.getLettre() == ' ') continue;

            double proba = matriceMarkov[indexCourant][i];
            if (proba > maxProba) {
                maxProba = proba;
                maxIndex = i;
            }
        }

        return maxIndex;
    }
}
