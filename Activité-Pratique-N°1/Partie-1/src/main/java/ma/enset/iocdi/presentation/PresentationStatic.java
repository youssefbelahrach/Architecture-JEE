package ma.enset.iocdi.presentation;

import ma.enset.iocdi.dao.DaoImpl;
import ma.enset.iocdi.dao.IDao;
import ma.enset.iocdi.metier.IMetier;
import ma.enset.iocdi.metier.MetierImpl;

/**
 * Injection des dépendances par instanciation statique.
 */
public final class PresentationStatic {
    private PresentationStatic() {
    }

    public static double run() {
        IDao dao = new DaoImpl();
        MetierImpl metierImpl = new MetierImpl();
        metierImpl.setDao(dao);

        IMetier metier = metierImpl;
        return metier.calcul();
    }
}
