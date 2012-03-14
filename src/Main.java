import java.util.*;

public class Main {
    public static final float COUT_CORRESPONDANCE = 120;
    public static final float COUT_TRAJET_PIED = 240;
    public static final float COUT_TRAMWAY = 40;


    private static Map<Integer, Noeud> stations;
    private static Collection<Arc> arcs;
    private static Map<String, Ligne> lignes;

    private static AlgoCalculPlusCourtChemin algo;

    public Main() {
        //Initialiser stations, arcs, lignes. !!!!
    }

    public static void afficheLigne(String ligne) {
        if (lignes.get(ligne) == null) {
            System.out.println("La ligne " + ligne + " n'existe pas.");
            return;
        }
        System.out.println(lignes.get(ligne));
    }

    public static void afficheCorrespondance(String station) {
        Noeud noeudStation = getStation(station);
        if (noeudStation == null) {
            System.out.println("La station " + station + " n'existe pas.");
            return;
        }
        Set<String> lignes = new HashSet<String>();
        Collection<Noeud> correspondancesATraiter = new LinkedList<Noeud>();
        for (Arc arc : noeudStation.getArcs()) {
            String temp = arc.getLigne();
            if (temp.equals("0"))
                correspondancesATraiter.add(arc.getDestination());
            else
                lignes.add(arc.getLigne());
        }
        for (Noeud n : correspondancesATraiter)
            for (Arc arc : n.getArcs())
                lignes.add(arc.getLigne());

    }

    public static void affichePlusCourtChemin(String stationDepart, String stationArrive) {
        if (algo == null) {
            System.out.println("L'algorithme de plus court chemin n'est pas initialise");
            return;
        }
        Boolean isMal = false;
        String str = "Les parametres fournis sont invalides:\n";
        if (stationDepart == null || getStation(stationDepart) == null) {
            isMal = true;
            str += "Le nom de la station de depart invalide.\n";
        }
        if (stationArrive == null || getStation(stationArrive) == null) {
            isMal = true;
            str += "Le nom de la station d'arrive invalide.";
        }
        if (isMal) {
            System.out.println(str);
            return;
        }
        str = getChemin(stationDepart, stationArrive, algo.plusCourtChemin(stationDepart, stationArrive));
    }

    /**
     * Fonction utiliser pour donner le format adequat a <code>String</code> contenant le plus court chemin.
     *
     * @param chemin <code>Collection</code> d'<code>Arc</code> avec le chemin des arcs en ordre et enchaines.
     * @return <code>String</code> avec le plus court chemin.
     */
    private static String getChemin(String stationDepart, String stationArrive, Collection<Arc> chemin) {
        Arc curArc = null;
        String curLigne = "";
        int curNombreStationLigne = 0;
        float cout = 0;
        String str = "";
        str += "L'itineraire le plus cout entre <" + stationDepart + "> et <" + stationArrive + ">:\n";
        Iterator<Arc> iterator = chemin.iterator();
        while (iterator.hasNext()) {
            curArc = iterator.next();
            if (curArc.getLigne().equals("0")) {
                str += "et <" + curArc.getSource() + "> (" + curNombreStationLigne + " stations)\n";
                str += "=> Correspondance\n";
                cout += COUT_CORRESPONDANCE;
                continue;
            }
            if (!curLigne.equals(curArc.getLigne())) {
                curLigne = curArc.getLigne();
                str += "Ligne " + curLigne + ": de <" + curArc.getSource() + "> ";
                curNombreStationLigne = 1;
            } else {
                curNombreStationLigne++;
                cout += COUT_TRAMWAY;
            }
        }
        str += "et <" + curArc.getSource() + "> (" + curNombreStationLigne + " stations)\n";
        str += "Cout total: " + cout;
        return str;
    }

    /**
     * Recuper le <code>Noeuc</code> correspondant au <code>String</code> station passe en parametre.
     * Pre-requis: le <code>Map<Integer,Noeud</code> doit implementer l'interface <code>Iterable</code>.
     * NullPointerException est levee si le nom de la station n'existe pas.
     *
     * @param station <code>String</code> avec le nom d'une station.
     * @return retour un <code>Noeud</code> s'il existe une station avec ce nom.
     */
    public static Noeud getStation(String station) throws NullPointerException {
        Iterator<Noeud> iterator = ((Iterable<Noeud>) stations).iterator();
        boolean isFound = false;
        Noeud cur = null;
        while (iterator.hasNext() && !isFound) {
            cur = iterator.next();
            if (cur.equals(station))
                isFound = true;
        }
        if (!isFound)
            cur = null;
        return cur;
    }
}
