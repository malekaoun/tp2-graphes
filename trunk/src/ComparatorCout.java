import java.util.Comparator;

public class ComparatorCout implements Comparator<Arc> {

    /**
     * Fontion a redefinir pour pouvoir utiliser un comparaison particuliaire entre 2 arcs en temps d'execution.
     *
     * @param o1 Premier arc a comparer
     * @param o2 Deuxieme arc a comparer
     * @return la valeur de retour est 0 si les 2 arcs sont le meme objet, sinon on fait un comparaison de cout.
     */
    @Override
    public int compare(Arc o1, Arc o2) {
        if (o1 == o2)
            return 0;
        int ret = 1;
        if (o1.getCout() < o2.getCout())
            ret = -1;
        return ret;
    }
}
