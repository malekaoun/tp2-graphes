public class Arc {
    private String ligne;
    private float cout;

    private Noeud source;
    private Noeud destination;

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
}
