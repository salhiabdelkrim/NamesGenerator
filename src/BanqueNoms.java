import java.util.*;

public class BanqueNoms {

    private Map<Lettre, Integer> statistiques ; // liste contenante les diffèrents lettres que contient le texte entré et le nombre d'apparitions de chaque caractère
    private ArrayList<Nom> listeNoms;


    /**méthode qui permet de retourner une liste des mots et la liste des lettres avec leurs occurences à partir d'une chaine de caractère*/
    public BanqueNoms(String texte) {
        String[] mots = texte.split(" ");
        char[][] Mots = new char[mots.length][] ;
        for (int i = 0; i < Mots.length; i++) {
            Mots[i] = mots[i].toCharArray();
        }
        this.listeNoms =new ArrayList<>();
        for (char[] mot : Mots) {
            Nom m = new Nom();
            for (int j = 0; j < mot.length; j++) {
                m.inserer(new Lettre(mot[j]));
            }
            this.listeNoms.add(m);
        }
        this.statistiques=compterOccurrences(texte);
    }

    public Map<Lettre, Integer> getStatistiques() {
        return statistiques;
    }


    /** Méthode qui génére la matrice de transition à partir d'un texte entrée en paramètre */
    public double[][] genererMatriceMarkov(Map<Lettre, Integer> statistiques) {
        int taille = statistiques.size();
        double[][] matrice = new double[taille][taille];

        // Associer chaque caractère à un index (dans l'ordre d'apparition)
        Map<Lettre, Integer> indexMap = new LinkedHashMap<>();
        int index = 0;
        for (Lettre c : statistiques.keySet()) {
            indexMap.put(c, index++);
        }

        // Compter les transitions : (a -> b)
        int[][] transitions = new int[taille][taille];

        for (Nom nom : listeNoms) {
            Lettre premier = nom.getPremier();

            // Ajouter transition depuis ' ' vers la première lettre du mot
            Lettre espace = new Lettre(' ');
            if (premier != null && indexMap.containsKey(espace) && indexMap.containsKey(premier)) {
                int i = indexMap.get(espace);
                int j = indexMap.get(premier);
                transitions[i][j]++;
            }

            // Parcourir les autres transitions normales
            Lettre courant = premier;
            while (courant != null && courant.getSuivant() != null) {
                Lettre a = courant;
                Lettre b = courant.getSuivant();

                if (indexMap.containsKey(a) && indexMap.containsKey(b)) {
                    int i = indexMap.get(a);
                    int j = indexMap.get(b);
                    transitions[i][j]++;
                }

                courant = courant.getSuivant();
            }
        }

        // Remplir la matrice avec les probabilités
        for (int i = 0; i < taille; i++) {
            int total = 0;
            for (int j = 0; j < taille; j++) {
                total += transitions[i][j];
            }
            if (total > 0) {
                for (int j = 0; j < taille; j++) {
                    matrice[i][j] = (double) transitions[i][j] / total;
                }
            }
        }

        return matrice;
    }



    /** méthode pour afficher la matrice sous forme carré à la console */
    public void afficherMatriceMarkov(double[][] matrice, Map<Lettre, Integer> stats) {
        // Récupérer les lettres dans l'ordre de la map
        Lettre[] lettres = stats.keySet().toArray(new Lettre[0]);

        // En-tête de colonne
        System.out.print("     "); // petit espace pour l'alignement
        for (Lettre l : lettres) {
            System.out.printf("%5s", l.getLettre());
        }
        System.out.println();

        // Lignes
        for (int i = 0; i < matrice.length; i++) {
            System.out.printf("%5s", lettres[i].getLettre()); // étiquette de la ligne
            for (int j = 0; j < matrice[i].length; j++) {
                System.out.printf("%5.2f", matrice[i][j]); // format pour 2 chiffres après la virgule
            }
            System.out.println();
        }
    }




    /** Méthode qui retourne une liste des caractères que contient un texte avec leur occurence */
    private static Map<Lettre, Integer> compterOccurrences(String chaine) {
        Map<Lettre, Integer> nonTriee = new LinkedHashMap<>();

        for (char c : chaine.toCharArray()) {
            Lettre l = new Lettre(c);
            nonTriee.put(l, nonTriee.getOrDefault(l, 0) + 1);
        }

        // Si l'espace est présent, on le déplace au début
        if (nonTriee.containsKey(new Lettre(' '))) {
            int espaceCount = nonTriee.remove(new Lettre(' '));
            Map<Lettre, Integer> avecEspaceDevant = new LinkedHashMap<>();
            avecEspaceDevant.put(new Lettre(' '), espaceCount);
            avecEspaceDevant.putAll(nonTriee);
            return avecEspaceDevant;
        }

        return nonTriee;
    }

    /**Méthode qui renvoie une liste des transitions avec leurs occurences*/
    public Map<String, Integer> compterTransitions() {
        Map<String, Integer> transitionsMap = new LinkedHashMap<>();

        for (Nom nom : listeNoms) {
            Lettre premier = nom.getPremier();

            // Transition depuis l'espace vers la première lettre du mot
            if (premier != null) {
                String transitionDebut = " ->" + premier.getLettre();
                transitionsMap.put(transitionDebut, transitionsMap.getOrDefault(transitionDebut, 0) + 1);
            }

            Lettre courant = premier;
            while (courant != null && courant.getSuivant() != null) {
                String transition = courant.getLettre() + "->" + courant.getSuivant().getLettre();
                transitionsMap.put(transition, transitionsMap.getOrDefault(transition, 0) + 1);
                courant = courant.getSuivant();
            }
        }

        return transitionsMap;
    }

}
