package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Component("metier")
@Service("metier")
public class Metier implements IMetier{
    //@Autowired
    private IDao dao;
    @Override
    public double calcul() {
        double tmp=dao.getData();
        double res=tmp*5;
        return res;
    }
    public Metier(@Qualifier("dao") IDao dao) {
        this.dao = dao;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
