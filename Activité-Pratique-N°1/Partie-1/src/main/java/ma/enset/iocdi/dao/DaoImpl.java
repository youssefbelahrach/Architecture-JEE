package ma.enset.iocdi.dao;

import org.springframework.stereotype.Repository;

/**
 * Implémentation concrète du DAO.
 */
@Repository
public class DaoImpl implements IDao {

    @Override
    public double getData() {
        return 10.0;
    }
}
