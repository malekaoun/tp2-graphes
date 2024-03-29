import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;

/**
 * @author asus
 * @Param remplie les collections de donn�es des arcs et noeuds
 */

public class Lecteur {
    public static void lecture(Map<Integer, Noeud> stations, Collection<Arc> arcs, Map<String, Ligne> lignes, String fileName) {
        String pathFile = "resource";
        String fichier = pathFile + '/' + fileName;

        //lecture du fichier texte
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String[] ligne;
            Integer nbNoeud, nbArc;
            ligne = br.readLine().split(" ");
            nbArc = Integer.parseInt(ligne[1]);
            nbNoeud = Integer.parseInt(ligne[0]);

            for (int i = 0; i < nbNoeud; i++) {
                ligne = br.readLine().split(" ", 2);
                String nomStation = ligne[1].replaceAll(" ","_");
                stations.put(Integer.parseInt(ligne[0]), new Noeud(Integer.parseInt(ligne[0]), nomStation));
            }
            String lastLigne = "";
            for (int i = 0; i < nbArc; i++) {
                ligne = br.readLine().split(" ");
                Noeud start = stations.get(Integer.parseInt(ligne[0]));
                Noeud end = stations.get(Integer.parseInt(ligne[1]));
                Arc arcret = new Arc(ligne[3], Float.parseFloat(ligne[2]), start, end);
                arcs.add(arcret);
                if (!ligne[3].equals(lastLigne) && !ligne[3].equals("0")) {// ne gere pas si les arc sont en desordre
                    lignes.put(ligne[3], new Ligne(ligne[3], start));
                    lastLigne = ligne[3];
                }

            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
