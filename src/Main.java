import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Main {
    public static final float COUT_CORRESPONDANCE = 120;
    public static final float COUT_TRAJET_PIED = 240;
    public static final float COUT_TRAMWAY = 40;

    private static Collection<Noeud> stations;
    private static Collection<Arc> arcs;
    private static Map<String, Ligne> lignes;

    private static AlgoCalculPlusCourtChemin algo;

    public Main() {
        //Initialiser stations, arcs, lignes.
    }

    public static void afficheLigne(String ligne) {

    }

    public static void afficheCorrespondance(String station) {

    }

    public static void affichePlusCourtChemin(String stationDepart, String stationArrive) {
        if (algo == null){
            System.out.println("L'algorithme de plus court chemin n'est pas initialise");
            return;
        }
        Boolean isMal = false;
        String str = "Les parametres fournis sont invalides:\n";
        if (stationDepart == null || isStation(stationDepart)) {
            isMal = true;
            str += "Le nom de la station de depart invalide.\n";
        }
        if (stationArrive == null || isStation(stationArrive)) {
            isMal = true;
            str += "Le nom de la station d'arrive invalide.";
        }
        if (isMal){
            System.out.println(str);
            return;
        }
        str = getChemin(stationDepart, stationArrive, algo.plusCourtChemin(stationDepart, stationArrive));
    }

    /**
     * Fonction utilise pour verifier l'existence de la station parmis les stations deja inscrites.
     * @param station <code>String</code> avec le nom de la station recherchee.
     * @return <code>true</code> si la station existe, <code>false</code> dans le cas echeant.
     */
    private static boolean isStation(String station){
        return  true;
    }

    /**
     * Fonction utiliser pour donner le format adequat a <code>String</code> contenant le plus court chemin.
     * @param chemin <code>Collection</code> d'<code>Arc</code> avec le chemin des arcs en ordre et enchaines.
     * @return  <code>String</code> avec le plus court chemin.
     */
    private static String getChemin(String stationDepart,String stationArrive, Collection<Arc> chemin) {
        Arc curArc = null;
        String curLigne = "";
        int curNombreStationLigne = 0;
        float cout = 0;
        String str = "";
        str += "L'itineraire le plus cout entre <" + stationDepart + "> et <" + stationArrive + ">:\n";
        Iterator<Arc> iterator = chemin.iterator();
        while (iterator.hasNext()) {
            curArc = iterator.next();
            if (curArc.getLigne().equals("0")){
                str += "et <" + curArc.getSource() + "> (" + curNombreStationLigne + " stations)\n";
                str += "=> Correspondance\n";
                cout += COUT_CORRESPONDANCE;
                continue;
            }
            if (!curLigne.equals(curArc.getLigne())){
                curLigne = curArc.getLigne();
                str += "Ligne " + curLigne + ": de <" + curArc.getSource() + "> ";
                curNombreStationLigne = 1;
            }
            else{
                curNombreStationLigne++;
                cout += COUT_TRAMWAY;
            }
        }
        str += "et <" + curArc.getSource() + "> (" + curNombreStationLigne + " stations)\n";
        str += "Cout total: " + cout;
        return str;
    }
}
