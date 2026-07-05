package ma.enset.iocdi.metier;

import ma.enset.iocdi.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * La classe métier ne dépend que de l'abstraction IDao : couplage faible.
 */
@Service
public class MetierImpl implements IMetier {

    private IDao dao;

    /**
     * Constructeur sans argument utilisé par l'injection statique, dynamique
     * et la version XML de Spring (via le setter).
     */
    public MetierImpl() {
    }

    /**
     * Injection par constructeur utilisée par Spring en version annotations.
     */
    @Autowired
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    /**
     * Point d'injection par setter : utile avec l'instanciation statique,
     * la réflexion Java et la configuration Spring XML.
     */
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        if (dao == null) {
            throw new IllegalStateException("La dépendance IDao n'a pas été injectée.");
        }
        return dao.getData() * 5;
    }
}
