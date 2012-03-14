import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class ExplorationArborescente implements AlgoCalculPlusCourtChemin {

    private class X {
        float distance;
        Noeud predecesseur;

        private X(float distance) {
            this.distance = distance;
        }

        private X() {
            this(Float.MAX_VALUE);
        }
    }

    ;
    private HashMap<Noeud, X> stations;
    private Noeud source, fin;

    public ExplorationArborescente(Collection<Noeud> noeuds) {
        this.stations = new HashMap<Noeud, X>();
        for (Noeud station : noeuds)
            stations.put(station, new X());
    }

    @Override
    public Collection<Arc> plusCourtChemin(String stationDepart, String stationArrive) {
        source = Main.getStation(stationDepart);
        fin = Main.getStation(stationArrive);
        if (source == null || fin == null) {
            System.out.println("Le chemin le plus court est impossible de calculer entre: " + stationDepart + " et " + stationArrive + ".");
            return null;
        }
        stations.get(source).distance = 0;
        parcourProfondeur(source);

        return noeudsToArcs();
    }

    private void parcourProfondeur(Noeud source) {
        for (Noeud destination : source.getStationsAdjacentes()) {
            float d = stations.get(source).distance + source.getConnexion(destination).getCout();
            X xDestination = stations.get(destination);
            if (d < xDestination.distance) {
                xDestination.distance = d;
                xDestination.predecesseur = source;
                parcourProfondeur(destination);
            }
        }
    }

    private Deque<Arc> noeudsToArcs() {
        Deque<Arc> arcs = new LinkedList<Arc>();
        Noeud cur = fin;
        while (cur != null) {
            Noeud prev = stations.get(cur).predecesseur;
            if (prev == null)
                break;
            arcs.addFirst(prev.getConnexion(prev));
            cur = prev;
        }
        if (arcs.peekFirst() == null)
            arcs.removeFirst();
        return arcs;
    }
}
