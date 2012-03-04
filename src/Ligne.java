import javax.management.BadAttributeValueExpException;

public class Ligne {
    private String ligne;
    private Noeud terminus;

    public Ligne(String ligne, Noeud terminus) throws BadAttributeValueExpException {
        this.terminus = terminus;
        setLigne(ligne);
    }

    public String getLigne() {
        return ligne;
    }

    /**
     * Setter de ligne utile pour respecter l'invariant de class: le terminus doit etre sur la ligne <code>ligne</code>.
     * Elle leve une exception pour eviter creer un objet non-stable.
     *
     * @param ligne
     */
    public void setLigne(String ligne) throws BadAttributeValueExpException {
        if (terminus.isSurLigne(ligne))
            this.ligne = ligne;
        else
            // BadAttributeValueExpException est utilisee puisque le nom de classe est plus parlant, JMX n'intervient pas.
            throw new BadAttributeValueExpException("Le terminus n'appartient pas a la ligne: " + ligne);
    }
}
