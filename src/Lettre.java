public class Lettre {

    /** l'objet lettre désigne le noeud dans une liste doublement chainée, ce noeud pointe sur la lettre avant et la lettre après dans un nom */
    private char lettre;
    private Lettre precedent;
    private Lettre suivant;

    public Lettre(char lettre) {
        this.lettre = lettre;
        precedent = null;
        suivant = null;
    }

    public char getLettre() {
        return lettre;
    }
    public void setLettre(char lettre) {
        this.lettre = lettre;
    }
    public Lettre getPrecedent() {
        return precedent;
    }
    public void setPrecedent(Lettre precedant) {
        this.precedent = precedant;
    }
    public void setSuivant(Lettre suivant) {this.suivant = suivant;}
    public Lettre getSuivant() {return this.suivant;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Lettre lettre1 = (Lettre) obj;
        return lettre == lettre1.lettre;
    }

    @Override
    public int hashCode() {
        return Character.hashCode(lettre);
    }
}
