import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
       System.out.println("Bonjour dans l'application 'Générateur des noms' \n merci d'indiquer le chemin absolu vers le fichier .txt qui contient les données ");

        //code pour lire le fichier texte
        String cheminFichier = scan.nextLine();
        StringBuilder contenu = new StringBuilder();

        contenu = extraireDonnees(cheminFichier);
        /** Remplissage de la matrice avec les mots disponible dans le fichier au début du programme */
        BanqueNoms banque = new BanqueNoms(contenu.toString());
        GenerateurDeNoms generateur = new GenerateurDeNoms(
                banque.genererMatriceMarkov(banque.getStatistiques()),
                banque.getStatistiques()
        );


        int choix = 0;


        while (choix != 5) {
            try{
                System.out.println("Menu - Générateur des Noms :");
                System.out.println("1: Recharger la matrice de transition (réinitialiser le générateur)");
                System.out.println("2: Afficher les relations de transitions détectés");
                System.out.println("3: Générer des noms aléatoires ");
                System.out.println("4: Afficher la matrice de transition  ");
                System.out.println("5: Quitter ");
                System.out.println("Faites votre choix et appuyez sur ENTER");
                choix = scan.nextInt();

                switch (choix) {
                    case 1:
                        String choix1;
                        System.out.println("la réinitialisation doit se faire à partir d'un nouveau fichier de données? (oui/non) ");
                        scan.nextLine();
                        choix1= scan.nextLine();

                        /** Cas où la réinitialisation se fait avec le même fichier */
                        if (choix1.equalsIgnoreCase("non")) {
                            contenu = extraireDonnees(cheminFichier);
                            /**Récupération du contenu du Fichier*/
                            banque = new BanqueNoms(contenu.toString());
                            /** génération de la matrice de Markov */
                            generateur = new GenerateurDeNoms(
                                    banque.genererMatriceMarkov(banque.getStatistiques()),
                                    banque.getStatistiques()
                            );
                        } else if (choix1.equalsIgnoreCase("oui")) {
                            System.out.println("Merci d'indiquer le chemin absolu vers le fichier .txt qui contient les données ");
                            cheminFichier = scan.nextLine();
                            contenu = extraireDonnees(cheminFichier);
                            /**Récupération du contenu du Fichier*/
                            banque = new BanqueNoms(contenu.toString());
                            /** génération de la matrice de Markov */
                            generateur = new GenerateurDeNoms(
                                    banque.genererMatriceMarkov(banque.getStatistiques()),
                                    banque.getStatistiques()
                            );
                        }else {
                            System.out.println("la réponse doit être soit 'oui' ou 'non'");
                        }

                        break;
                    case 2:
                        Map<String, Integer> transitions = banque.compterTransitions();
                        System.out.println("Transitions et leurs occurrences :");
                        for (Map.Entry<String, Integer> entry : transitions.entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }
                        break;
                    case 3:
                        System.out.println("Quel est le nombre de noms que vous voulez générer :");
                        int nombreNoms = scan.nextInt();
                        System.out.println("Quel est le nombre de lettres de chaque nom ? ");
                        int tailleNom = scan.nextInt();
                        System.out.println("Noms générés : \n");
                        for (int i = 0; i < nombreNoms; i++)
                            System.out.println( generateur.genererNom(tailleNom)+"\n");
                        break;
                    case 4:
                        banque.afficherMatriceMarkov(banque.genererMatriceMarkov(banque.getStatistiques()), banque.getStatistiques());
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("choix invalide");
                }
                //gestion du cas où l'utilisateur rentre des valeurs incorrectes ou inattendues
            }catch (InputMismatchException e){
                System.out.println("La valeur rentrer est invalide");
                scan.nextLine();
            }
        }
        scan.close();
    }

    /** méthode qui récupère les données d'un fichier */
    private static StringBuilder extraireDonnees(String cheminFichier) {
        StringBuilder contenu = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                contenu.append(ligne).append(" ");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            return null;
        }
        return contenu;
    }
}

