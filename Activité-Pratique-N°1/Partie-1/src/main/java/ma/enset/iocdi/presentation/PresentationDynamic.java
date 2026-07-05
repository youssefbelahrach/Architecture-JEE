package ma.enset.iocdi.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ma.enset.iocdi.dao.IDao;
import ma.enset.iocdi.metier.IMetier;

/**
 * Injection par instanciation dynamique avec réflexion Java.
 */
public final class PresentationDynamic {
    private static final String CONFIG_FILE = "config.txt";

    private PresentationDynamic() {
    }

    public static double run() {
        try {
            List<String> classNames = readConfiguration();
            if (classNames.size() < 2) {
                throw new IllegalStateException(
                        "config.txt doit contenir le nom de la classe DAO puis celui de la classe métier.");
            }

            Class<?> daoClass = Class.forName(classNames.get(0));
            IDao dao = (IDao) newInstance(daoClass);

            Class<?> metierClass = Class.forName(classNames.get(1));
            IMetier metier = (IMetier) newInstance(metierClass);

            Method setter = metierClass.getMethod("setDao", IDao.class);
            setter.invoke(metier, dao);

            return metier.calcul();
        } catch (ReflectiveOperationException | IOException exception) {
            throw new IllegalStateException("Impossible de créer les objets à partir de config.txt.", exception);
        }
    }

    private static List<String> readConfiguration() throws IOException {
        try (InputStream input = PresentationDynamic.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Ressource introuvable : " + CONFIG_FILE);
            }

            return new String(input.readAllBytes(), StandardCharsets.UTF_8)
                    .lines()
                    .map(String::trim)
                    .filter(line -> !line.isBlank() && !line.startsWith("#"))
                    .collect(Collectors.toList());
        }
    }

    private static Object newInstance(Class<?> type) throws ReflectiveOperationException {
        Constructor<?> constructor = Objects.requireNonNull(type.getDeclaredConstructor());
        return constructor.newInstance();
    }
}
