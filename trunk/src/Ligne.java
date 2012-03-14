import javax.management.BadAttributeValueExpException;
import java.util.Collection;
import java.util.HashSet;

public class Ligne {
    private String ligne;
    private Noeud terminus;
    private Collection<Noeud> stationsParcourues;

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

    @Override
    public String toString() {
        stationsParcourues = new HashSet<Noeud>();
        String chaineLigne = "Ligne " + ligne + ": ";
        chaineLigne += arborescenceParcourue(terminus);
        chaineLigne += "\n(" + stationsParcourues.size() + " stations)";
        return chaineLigne;
    }

    /**
     * Cette fonction construir en parcurant l'arborescence des <code>Noeuds</code> sans passer 2 fois sur le meme.
     * Appels recursif, commencez par le terminus.
     *
     * @param station station appartenant a la ligne
     * @return <code>String</code> avec l'arborescence possible a partir de cet arc.
     */
    private String arborescenceParcourue(Noeud station) {
        String str = station.getStation() + " -";
        stationsParcourues.add(station);
        for (Arc arc : station.getArcs())
            if (arc.getLigne().equals(ligne) && !stationsParcourues.contains(arc.getDestination()))
                str += arborescenceParcourue(arc.getDestination());
        return str;
    }
}
