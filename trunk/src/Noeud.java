import java.util.Map;

public class Noeud implements Comparable<Noeud>{
    private int id;
    private String station;

    // La clef va etre la ligne. Cette structure sera utile pour afficher la ligne.
    private Map<String, Arc> arcs;

    public Noeud(int id, String station) {
        this.id = id;
        this.station = station;
        // Initialiser arcs
    }

    public void lierArc(Arc arc) {
        arcs.put(arc.getLigne(), arc);
    }

    /**
     * Fonction utlise pour savoir si cette station appartient a une certaine ligne.
     *
     * @param ligne <code>String</code> avec le nom de la ligne.
     * @return <code>true</code> si la station appartient a la ligne,
     * @return <code>false</code> dans le cas echeant ou si la ligne recherche est "0".
     */
    public boolean isSurLigne(String ligne) {
        if (!ligne.equals("0"))
            return arcs.keySet().contains(ligne);
        return false;
    }

    /**
     * Utiliser pour trier les noeuds.
     * @param o
     * @return valeur de retour 0 si les deux objet est le meme, apres on compare le nom, n cas d'egalite on compare les id.
     */
    @Override
    public int compareTo(Noeud o) {
        if (o == this)
            return 0;
        int ret = station.compareTo(o.station);
        if (ret == 0){
            ret = 1;
            if (id < o.id)
                ret = -1;
        }
        return  ret;
    }
}
