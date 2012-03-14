import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Dijkstra implements AlgoCalculPlusCourtChemin {

    // Classe interne pour gerer le marcage necessaire pour faire Dijkstra
    private class StationWrapper implements Comparable<StationWrapper> {
        float mark;
        Noeud station;
        StationWrapper prevStation;

        StationWrapper(Noeud station, float mark, StationWrapper prevStation) {
            this.mark = mark;
            this.station = station;
            this.prevStation = prevStation;
        }

        private StationWrapper(Noeud station, float mark) {
            this(station, mark, null);
        }

        StationWrapper(Noeud station) {
            this(station, Float.MAX_VALUE, null);
        }

        @Override
        public int compareTo(StationWrapper o) {
            if (o.station == station)
                if (mark == ((StationWrapper) o).mark)
                    return 0;
            if (o.mark < mark)
                return -1;
            else
                return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Noeud)
                return o == station;
            if (o instanceof StationWrapper)
                return ((StationWrapper) o).station == station;
            return this == o;
        }

        @Override
        public int hashCode() {
            return station.hashCode();
        }
    }

    ;

    // Structrure necessaire pour Dijkstra
    private PriorityQueue<StationWrapper> fileAttente = new PriorityQueue<StationWrapper>();
    private HashMap<Noeud, Float> stationsMarquees = new HashMap<Noeud, Float>();
    private StationWrapper deniereConnexionFaiteAtteignantArrivee;

    @Override
    public Collection<Arc> plusCourtChemin(String stationDepart, String stationArrive) {

        Noeud noeudDepart = Main.getStation(stationDepart);
        Noeud nouedArrive = Main.getStation(stationArrive);
        // Verfication de l'existence des 2 stations
        if (noeudDepart == null || nouedArrive == null) {
            System.out.println("Le chemin le plus court est impossible de calculer entre: " + stationDepart + " et " + stationArrive + ".");
            return null;
        }

        fileAttente.add(new StationWrapper(noeudDepart, 0));
        boolean isArriveeAtteinte = false;
        float coutCurMin = Float.MAX_VALUE;
        while (!fileAttente.isEmpty()) {
            StationWrapper cur = fileAttente.poll();
            if (stationsMarquees.containsKey(cur.station)) {
                if (cur.mark < stationsMarquees.get(cur.station)) {
                    stationsMarquees.remove(cur.station); // Elimination de la station pour permettre re-insertion a posteriori.
                } else
                    break; // Le station currante a traiter a deja une marque plus petit donc il y a aucun interet de la changer.
            }
            /*
            Si la station n'avait pas ete traitee au prealable alors nous l'ajoutons.
            Si elle avait ete deja traitee le if ci-dessus est sense de l'eliminer (mise a jour du marqueur).
             */
            stationsMarquees.put(cur.station, cur.mark);

            Collection<Arc> curArcs = cur.station.getArcs();
            for (Arc arcCur : curArcs) {
                float cout = cur.mark + arcCur.getCout();
                Noeud stationAdjacenteCur = arcCur.getDestination();

                StationWrapper temp = new StationWrapper(stationAdjacenteCur, cout, cur);

                // Optimisation qui permet finir Dijkstra avant de traiter tous les noeuds.
                if (isArriveeAtteinte) {
                    if (coutCurMin > cout) {
                        coutCurMin = cout;
                        deniereConnexionFaiteAtteignantArrivee = temp;
                    } else {
                        /*
                        La station courrante est atteinte avec un cout plus eleve que pour atteindre notre station d'arrivee.
                        A cause du fait que cette station est celle qui a le cout le plus petit, on peut dire
                        c'est impossible de reduire le cout du plus court chemin.
                         */
                        fileAttente.clear();
                        break;
                    }
                }

                if (stationAdjacenteCur.equals(nouedArrive)) {
                    isArriveeAtteinte = true;
                    deniereConnexionFaiteAtteignantArrivee = temp;
                }

                fileAttente.add(temp);
            }
        }

        return noeudsToArcs();
    }

    /**
     * Fonction permettant passer d'une <code>Collectio</code> de <code>Noeud</code> a une de <code>Arc</code> exploitable.
     *
     * @return <code>Collection</code> d'<code>Arc</code> avec les <code>Noeud</code> qui rester dans <code>stationsMarquees</code>
     */
    private LinkedList<Arc> noeudsToArcs() {
        LinkedList<Arc> arcs = new LinkedList<Arc>();
        StationWrapper cur = deniereConnexionFaiteAtteignantArrivee;
        while (cur != null) {
            StationWrapper prev = cur.prevStation;
            arcs.addLast(prev.station.getConnexion(cur.station));
            cur = prev;
        }
        if (arcs.peekFirst() == null)
            arcs.removeFirst();
        return arcs;
    }
}
