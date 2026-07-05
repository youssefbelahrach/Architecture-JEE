package ext;

import dao.IDao;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//@Component("daoV2")
@Repository("dao2")
public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version WebServices");
        double temp=40;
        return temp;
    }
}
