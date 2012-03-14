import java.sql.SQLOutput;
import java.util.*;

public class Main {
    public static final float COUT_CORRESPONDANCE = 120;
    public static final float COUT_TRAJET_PIED = 240;
    public static final float COUT_TRAMWAY = 40;

    public static final String GRENOBLE = "reseauTramGrenoble2010.graph";
    public static final String PARIS = "reseauMetroRERTramParis2009.grapgh";


    private static Map<Integer, Noeud> stations;
    private static Collection<Arc> arcs;
    private static Map<String, Ligne> lignes;

    private static AlgoCalculPlusCourtChemin algo;

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
        Set<String> strings = new HashSet<String>();
        Collection<Noeud> correspondancesATraiter = new LinkedList<Noeud>();
        for (Arc arc : noeudStation.getArcs()) {
            String temp = arc.getLigne();
            if (temp.equals("0"))
                correspondancesATraiter.add(arc.getDestination());
            else{
                strings.add(arc.getLigne());
                break;
            }
        }
        for (Noeud n : correspondancesATraiter)
            for (Arc arc : n.getArcs())
                if (!arc.getLigne().equals("0")){
                    strings.add(arc.getLigne());
                    break;
                }

        String correspondance = "Les correspondance de la station: " + station + " sont: ";
        for(String str : strings)
            correspondance += "\n" + str;
        System.out.println(correspondance);
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
        System.out.println(getChemin(stationDepart, stationArrive, algo.plusCourtChemin(stationDepart, stationArrive)));
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
                cout += curArc.getCout();
                continue;
            }
            if (!curLigne.equals(curArc.getLigne())) {
                curLigne = curArc.getLigne();
                str += "Ligne " + curLigne + ": de <" + curArc.getSource() + "> ";
                curNombreStationLigne = 1;
            } else {
                curNombreStationLigne++;
                cout += curArc.getCout();
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

    public static void menuPrincipale() {
        System.out.println("Tp - Le plus court chemin dans un graphe.");
        System.out.println("1 - Afficher ligne.");
        System.out.println("2 - Afficher corespondance");
        System.out.println("3 - Afficher plus court chemin");
        System.out.println("4 - Charge un graphe à partir d'un fichier.");
        System.out.println("S/s sortir");
        Scanner scanner = new Scanner(System.in);
        String opt = scanner.next();
        if (opt.toLowerCase().charAt(0) != 's') {
            int optInt = Integer.parseInt(opt);
            switch (optInt) {
                case 1:
                    System.out.println("Veuillez introduire la ligne à afficher: ");
                    afficheLigne(scanner.next());
                    break;

                case 2:
                    System.out.println("Veuillez introduire le nom de la station à traiter: ");
                    afficheCorrespondance(scanner.nextLine());
                    break;

                case 3:
                    System.out.println("Veuillez choisir un algo de calcul: ");
                    System.out.println("A - Arborescence");
                    System.out.println("B - Bellman-Ford");
                    System.out.println("D - Dijkstra (default)");
                    switch (scanner.next().toLowerCase().charAt(0)){
                        case 'a' :
                            algo = new ExplorationArborescente(stations.values());
                            break;
                        case 'b' :
                            algo = new BellmanFord();
                            break;
                        default:
                        case 'd' :
                            algo = new Dijkstra();
                            break;
                    }
                    System.out.println("Veuillez saisir le nom de la station de départ: ");
                    String str = scanner.next();
                    System.out.println("Veuillez saisir le nom de la station d'arrivée: ");
                    affichePlusCourtChemin(str, scanner.next());
                    break;

                case 4:
                    String fichier = "";
                    System.out.println("Veuillez saisir la option de fichier à charger: ");
                    System.out.println("G - Grenoble (default)");
                    System.out.println("P - Paris");
                    switch (scanner.next().toLowerCase().charAt(0)){
                        default:
                        case 'g':
                            fichier = GRENOBLE;
                            break;
                        case 'p':
                            fichier = PARIS;
                            break;
                    }
                    stations.clear();
                    arcs.clear();
                    lignes.clear();
                    Lecteur.lecture(stations, arcs, lignes, fichier);
                    break;
                default:
                    System.out.println("Option inexistante.");
            }
            menuPrincipale();
        }
    }

    public static void main(String args[]) {
        stations = new HashMap<Integer, Noeud>();
        arcs = new ArrayList<Arc>();
        lignes = new Hashtable<String, Ligne>();
        Lecteur.lecture(stations, arcs, lignes, GRENOBLE);
        menuPrincipale();
    }
}
