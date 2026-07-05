package pre;

import dao.DaoImpl;
import dao.IDao;
import ext.DaoImplV2;
import metier.IMetier;
import metier.Metier;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class presentation {
    public static void main(String[] args) throws Exception {
        //**** Faire l'injection des dépendances ****
        //- Par instanciation statique:
             /*DaoImpl dao=new DaoImpl();
              //DaoImplV2 dao=new DaoImplV2();
              Metier metier=new Metier();
              metier.setDao(dao);*/
        //- Par instanciation dynamique:
        Scanner scanner=new Scanner(new File("src/main/resources/config.txt"));
        String daoClassName=scanner.nextLine();
        Class cDao=Class.forName(daoClassName);
        IDao dao=(IDao) cDao.newInstance();

        String metierClassName=scanner.nextLine();
        Class cMetier=Class.forName(metierClassName);
        IMetier metier=(IMetier) cMetier.newInstance();

        Method method=cMetier.getMethod("setDao",IDao.class);
        method.invoke(metier,dao);
        System.out.println("Result = "+metier.calcul());
    }
}
