public class Nom {

    /** L'objet Nom c'est une liste doublement chainée qui modélise la structure d'un nom */
    private Lettre premier ;
    private Lettre dernier ;

    public Nom(){
        this.premier = null;
        this.dernier = null;
    }


    public void inserer(Lettre noeud)
    {

        if (estVide()) {
            this.premier = noeud;
            this.premier.setPrecedent(new Lettre(' '));
        }
        else {
            this.dernier.setSuivant(noeud);
            noeud.setPrecedent(this.dernier);
        }
        this.dernier = noeud;
        this.dernier.setSuivant(new Lettre(' '));
    }


    public Lettre getPremier(){
        return this.premier;
    }

    private boolean estVide(){
        return (this.premier == null);
    }
}
