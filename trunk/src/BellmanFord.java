import java.util.*;

public class BellmanFord implements AlgoCalculPlusCourtChemin {

    // Classe interne pour gerer le marcage necessaire pour faire Djikstra
    private class StationWrapper implements Comparable<StationWrapper> {
        float mark;
        Noeud station;
        Arc predecArc;
        Noeud predecNoeud;

        StationWrapper(Noeud station, float mark, Arc predecArc, Noeud predecNoeud) {
            this.mark = mark;
            this.station = station;
            this.predecArc = predecArc;
            this.predecNoeud = predecNoeud;
        }

        StationWrapper(Noeud station, float mark) {
            this.mark = mark;
            this.station = station;
            this.predecArc = null;
            this.predecNoeud = null;
        }

        StationWrapper(Noeud station) {
            this(station, Float.MAX_VALUE, null, null);
        }

        @Override
        public int compareTo(StationWrapper o) {
            if (((StationWrapper) o).station == station)
                if (mark == ((StationWrapper) o).mark)
                    return 0;
            if (((StationWrapper) o).mark < mark)
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

    // Structrure necessaire pour Bellman-Ford
    private Deque<StationWrapper> pile = new ArrayDeque<StationWrapper>();
    private Map<Noeud, StationWrapper> chemins = new HashMap<Noeud, StationWrapper>();

    @Override
    public Collection<Arc> plusCourtChemin(String stationDepart, String stationArrive) {
        Deque<Arc> bellman = new LinkedList<Arc>();
        Noeud noeudDepart = Main.getStation(stationDepart);
        Noeud noeudArrive = Main.getStation(stationArrive);
        // Verfication de l'existence des 2 stations
        if (noeudDepart == null || noeudArrive == null) {
            System.out.println("Le chemin le plus court est impossible de calculer entre: " + stationDepart + " et " + stationArrive + ".");
            return null;
        }
        // algorithme de Bellman-Ford //
        pile.push(new StationWrapper(noeudDepart, 0));
        Float distance;
        while (!pile.isEmpty()) {
            StationWrapper warp = pile.pop();
            Iterator<Arc> iterator = warp.station.getArcs().iterator();

            while (iterator.hasNext()) {
                Arc arcCur = iterator.next();
                distance = warp.mark + arcCur.getCout();
                if (!chemins.containsKey(arcCur.getDestination()) && chemins.get(arcCur.getDestination()).mark > distance) {
                    StationWrapper newWarp = new StationWrapper(arcCur.getDestination(), distance, arcCur, arcCur.getSource());
                    chemins.put(arcCur.getDestination(), newWarp);
                    pile.push(newWarp);
                }
            }
        }
        // fin algo //

        // remplissage de la liste d'arcs
        StationWrapper stationWarp = chemins.get(noeudArrive);
        while (stationWarp.station != noeudDepart) {
            bellman.addFirst(stationWarp.predecArc);
            stationWarp = chemins.get(stationWarp.predecNoeud);
        }
        return bellman;


    }
}

