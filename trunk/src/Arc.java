import java.util.Comparator;

public class Arc implements Comparable {
    private String ligne;
    private float cout;

    private Noeud source;
    private Noeud destination;

    private static Comparator<Arc> comparator;

    public Arc(String ligne, float cout, Noeud source, Noeud destination) {
        this.ligne = ligne;
        this.source = source;
        this.destination = destination;
        setCout(cout);
        lierNoeud();
    }

    public Arc(String ligne, Noeud source, Noeud destination) {
        this(ligne, Float.MAX_VALUE, source, destination);
    }

    public String getLigne() {
        return ligne;
    }

    public float getCout() {
        return cout;
    }

    public Noeud getSource() {
        return source;
    }

    public Noeud getDestination() {
        return destination;
    }

    /**
     * Fonction utilisee pour verifier la contrainte de cout possitif ou nul.
     *
     * @param cout reel qui correspond au temps necessaire pour traverser cet arc
     */
    public void setCout(float cout) {
        if (0 > cout)
            this.cout = Float.MAX_VALUE;
        else
            this.cout = cout;
    }

    private void lierNoeud() {
        source.lierArc(this);
        destination.lierArc(this);
    }

    /**
     * Setter du <code>comparator</code>, utiliser pour change le parametre de comparaison
     * entre deux arcs en temps d'exection.
     *
     * @param comparator il doit avoir la logique de comparaison entre 2 arcs.
     */
    public static void setComparator(Comparator<Arc> comparator) {
        Arc.comparator = comparator;
    }

    public static Comparator<Arc> getComparator() {
        return comparator;
    }

    /**
     * Utilise pour comparer cet arc avec un autre, en modifiant le <code>comparator</code> on peut faire
     * un autre type de comparaison en temps d'execution.
     *
     * @param o deuxieme objet a comparer, un <code>Arc</code> est attendu.
     * @return la valeur de retour lors d'une erreur <code>Integer.MIN_VALUE</code>.
     */
    @Override
    public int compareTo(Object o) {
        if (this == o)
            // Les deux objets sont les memes.
            return 0;
        if (comparator != null) {
            // La comparaison est deleguee a la classe Comparator
            return comparator.compare(this, (Arc) o);
        }
        return Integer.MIN_VALUE;
    }
}
