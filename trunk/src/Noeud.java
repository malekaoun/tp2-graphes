import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Noeud implements Comparable<Noeud> {
    private int id;
    private String station;

    private Collection<Arc> arcs;

    public Noeud(int id, String station) {
        this.id = id;
        this.station = station;
        // Initialiser arcs
    }

    public Collection<Arc> getArcs() {
        return arcs;
    }

    public void lierArc(Arc arc) {
        arcs.add(arc);
    }

    /**
     * Fonction utlise pour savoir si cette station appartient a une certaine ligne.
     *
     * @param ligne <code>String</code> avec le nom de la ligne.
     * @return <code>false</code> dans le cas echeant ou si la ligne recherche est "0".
     */
    public boolean isSurLigne(String ligne) {
        if (!ligne.equals("0"))
            for(Arc arc: arcs)
                if (! arc.getLigne().equals("0"))
                    return arc.getLigne().equals(ligne);
        return false;
    }

    /**
     * Utiliser pour trier les noeuds.
     *
     * @param o
     * @return valeur de retour 0 si les deux objet est le meme, apres on compare le nom, n cas d'egalite on compare les id.
     */
    @Override
    public int compareTo(Noeud o) {
        if (o == this)
            return 0;
        int ret = station.compareTo(o.station);
        if (ret == 0) {
            ret = 1;
            if (id < o.id)
                ret = -1;
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o != this){
            if (o instanceof Noeud)
                return station.equals(((Noeud)o).station);
            if (o instanceof String)
                return station.equals(o);
            return false;
        }
        return true;
    }

    /**
     * On recuperer les stations adjacentes a cette station. Utile pour Djikstra. Pre-requis arcs doit etre iterable.
     *
     * @return <code>TreeSet</code> des station adjacentes via les arcs.
     */
    public HashSet<Noeud> stationsAdjacentes() {
        HashSet<Noeud> stationsAdjacentes = new HashSet<Noeud>();
        Iterator<Arc> iterator = ((Iterable<Arc>) arcs).iterator();
        while (iterator.hasNext())
            stationsAdjacentes.add(iterator.next().getDestination());
        return stationsAdjacentes;
    }
}
