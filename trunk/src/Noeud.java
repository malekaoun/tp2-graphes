import java.util.Map;

public class Noeud {
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
     * @return ou si la ligne recherche est "0".
     */
    public boolean isSurLigne(String ligne) {
        if (!ligne.equals("0"))
            return arcs.keySet().contains(ligne);
        return false;
    }
}
