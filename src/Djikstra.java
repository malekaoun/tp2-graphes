import java.util.*;

public class Djikstra implements AlgoCalculPlusCourtChemin {

    // Classe interne pour gerer le marcage necessaire pour faire Djikstra
    private class StationWrapper implements Comparable<StationWrapper> {
        float mark;
        Noeud station;

        StationWrapper(Noeud station, float mark) {
            this.mark = mark;
            this.station = station;
        }

        StationWrapper(Noeud station) {
            this(station, Float.MAX_VALUE);
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

    // Structrure necessaire pour Djikstra
    private PriorityQueue<StationWrapper> fileAttente = new PriorityQueue<StationWrapper>();
    private HashMap<Noeud, Float> stationsMarquees = new HashMap<Noeud, Float>();

    @Override
    public Collection<Arc> plusCourtChemin(String stationDepart, String stationArrive) {

        Noeud noeudDepart = stringToNoeud(stationDepart);
        Noeud nouedArrive = stringToNoeud(stationArrive);
        // Verfication de l'existence des 2 stations
        if (noeudDepart == null || nouedArrive == null) {
            System.out.println("Le chemin le plus court est impossible de calculer entre: " + stationDepart + " et " + stationArrive + ".");
            return null;
        }

        TreeSet<Arc> arcsRetour = new TreeSet<Arc>();

        // Sauvegarder le comparator de base.  pas encore sur de l'interet !!!!!
        Comparator<Arc> objectComparator = Arc.getComparator();

        // Parametrer la comparaison qui nous convient pour comparer les Arcs    pas encore sur de l'interet !!!!!
        Arc.setComparator(new ComparatorCout());


        fileAttente.add(new StationWrapper(noeudDepart, 0));
        boolean isArriveeAtteinte = false;
        float coutCurMin = Float.MAX_VALUE;
        while (! fileAttente.isEmpty()) {
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

            Map<String, Arc> curArcs = cur.station.getArcs();
            Iterator<String> iterator = curArcs.keySet().iterator();
            while (iterator.hasNext()) {
                Arc arcCur = curArcs.get(iterator.next());
                float cout = cur.mark + arcCur.getCout();
                Noeud stationAdjacenteCur = arcCur.getDestination();

                //Optimisation qui permet finir Djikstra avant de traiter tous les noeuds.
                if (isArriveeAtteinte){
                    if (coutCurMin > cout)
                        coutCurMin = cout;
                    else{
                        /*
                        La station courrante est atteinte avec un cout plus eleve que pour atteindre notre station d'arrivee.
                        A cause du fait que cette station est celle qui a le cout le plus petit, on peut dire
                        c'est impossible de reduire le cout du plus court chemin.
                         */
                        fileAttente.clear();
                        break;
                    }
                }
                if (stationAdjacenteCur.equals(nouedArrive))
                    isArriveeAtteinte = true;

                fileAttente.add(new StationWrapper(stationAdjacenteCur, cout));
            }
        }

        // Remettre le comparator de base.     pas encore sur de l'interet !!!!!
        Arc.setComparator(objectComparator);

        // Pas completer !!!!!!!
        return arcsRetour;
    }

    /**
     * Je ne sais pas si cette fonction peut aller ailleur. Elle existe car plusieurs station peuvent avoir le meme nom. !!!!!!
     * L'idee est recuperer tous les station et a posteriori les distingues avec la ligne
     *
     * @param station <code>String</code> avec le nom d'une station, il peut etre un nom qui apparait plusieur fois.
     * @return null si la station ne existe pas ou un <code>Noeud</code> dans le cas echeant.
     */
    private Noeud stringToNoeud(String station) {
        Noeud ret;
        Object tmp;
        try {
            tmp = Main.getStation(station);
        } catch (NullPointerException e) {
            System.out.println("La station " + station + " n'existe pas.");
            return null;
        }
        if (tmp instanceof Noeud)
            ret = (Noeud) tmp;
        else {
            //Ici on doit mettre un code plus pertinent pour choisir exactement la station, en prennant en compte la ligne.!!!!!
            Map map = (Map<String, Noeud>) tmp;
            ret = (Noeud) map.get(map.keySet().iterator().next());
        }
        return ret;
    }
}
