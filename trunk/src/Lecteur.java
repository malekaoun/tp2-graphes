import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Scanner;

/**
 * @author asus
 * @Param remplie les collections de donn�es des arcs et noeuds
 */

public class Lecteur {
public void lecture(Map<Integer, Noeud> stations, Collection<Arc> arcs, Map<String, Ligne> lignes) {
        String pathFile = "C:/Users/asus/Documents/doc moi/travail/Algo prog/TestLecteur";

        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le nom du fichier � chager :");
        String str = sc.nextLine();
        String fichier = pathFile + '/' + str;

        //lecture du fichier texte
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String[] ligne;
            Integer nbNoeud, nbArc;
            ligne = br.readLine().split(" ");
            nbArc = Integer.parseInt(ligne[0]);
            nbNoeud = Integer.parseInt(ligne[1]);

            for (int i = 0; i < nbNoeud; i++) {
                ligne = br.readLine().split(" ",2);
                stations.put(ligne[1],Noeud(Integer.parseInt(ligne[0]), ligne[1]));
            }
            String lastLigne = "";
            for (int i = 0; i < nbArc; i++) {
                ligne = br.readLine().split(" ",2);
                Noeud start = stations.get(Integer.parseInt(ligne[0]));
                Noeud end = stations.get(Integer.parseInt(ligne[1]));
                lastLigne = ligne[3];
                if(ligne[3] != lastLigne && ligne[3] != "0"){// ne gere pas si les arc sont en desordre
                	lignes.put(ligne[3],new(ligne(ligne[3],start)));
                }
                Arc A = Arc(ligne[3], Float.parseFloat(ligne[2]), start, end);
                arcs.add(A);
                start.lierArc(A);

            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
