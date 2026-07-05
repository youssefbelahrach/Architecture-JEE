package ma.enset.miniioc.core;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import ma.enset.miniioc.core.xml.XmlBean;
import ma.enset.miniioc.core.xml.XmlBeans;
import ma.enset.miniioc.core.xml.XmlConstructorArg;
import ma.enset.miniioc.core.xml.XmlField;
import ma.enset.miniioc.core.xml.XmlProperty;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Conteneur IoC configuré par XML. Le XML est transformé en objets Java avec JAXB,
 * puis les dépendances sont reliées à l'aide de la réflexion.
 */
public class XmlApplicationContext implements MiniApplicationContext {
    private final Map<String, XmlBean> definitions = new LinkedHashMap<>();
    private final Map<String, Object> singletons = new LinkedHashMap<>();
    private final Set<String> currentlyCreating = new LinkedHashSet<>();

    public XmlApplicationContext(String classpathResource) {
        loadDefinitions(classpathResource);
    }

    private void loadDefinitions(String classpathResource) {
        try (InputStream input = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(classpathResource)) {
            if (input == null) {
                throw new IllegalArgumentException("Fichier XML introuvable dans le classpath : " + classpathResource);
            }
            JAXBContext context = JAXBContext.newInstance(XmlBeans.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XmlBeans beans = (XmlBeans) unmarshaller.unmarshal(input);

            for (XmlBean bean : beans.getBeans()) {
                if (definitions.putIfAbsent(bean.getId(), bean) != null) {
                    throw new IllegalArgumentException("Bean XML dupliqué : " + bean.getId());
                }
            }
        } catch (JAXBException exception) {
            throw new IllegalStateException("Impossible de lire la configuration XML", exception);
        } catch (Exception exception) {
            if (exception instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("Erreur lors de la fermeture du fichier XML", exception);
        }
    }

    @Override
    public Object getBean(String id) {
        if (singletons.containsKey(id)) {
            return singletons.get(id);
        }

        XmlBean definition = definitions.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Aucun bean XML nommé '" + id + "'");
        }
        if (!currentlyCreating.add(id)) {
            throw new IllegalStateException("Dépendance circulaire détectée : " + currentlyCreating + " -> " + id);
        }

        try {
            Object bean = createBean(definition);
            singletons.put(id, bean);
            return bean;
        } finally {
            currentlyCreating.remove(id);
        }
    }

    @Override
    public <T> T getBean(String id, Class<T> type) {
        Object bean = getBean(id);
        if (!type.isInstance(bean)) {
            throw new IllegalArgumentException("Le bean '" + id + "' n'est pas de type " + type.getName());
        }
        return type.cast(bean);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        List<String> candidates = new ArrayList<>();
        for (Map.Entry<String, XmlBean> entry : definitions.entrySet()) {
            try {
                Class<?> beanType = Class.forName(entry.getValue().getClassName());
                if (type.isAssignableFrom(beanType)) {
                    candidates.add(entry.getKey());
                }
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Classe introuvable : " + entry.getValue().getClassName(), exception);
            }
        }
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("Aucun bean compatible avec " + type.getName());
        }
        if (candidates.size() > 1) {
            throw new IllegalStateException("Plusieurs beans compatibles avec " + type.getName() + " : " + candidates);
        }
        return getBean(candidates.get(0), type);
    }

    private Object createBean(XmlBean definition) {
        try {
            Class<?> beanClass = Class.forName(definition.getClassName());
            Object instance = instantiateWithConstructor(beanClass, definition.getConstructorArgs());
            injectWithSetters(instance, definition.getProperties());
            injectIntoFields(instance, definition.getFields());
            return instance;
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Impossible de créer le bean '" + definition.getId() + "'", exception);
        }
    }

    private Object instantiateWithConstructor(Class<?> beanClass, List<XmlConstructorArg> args)
            throws ReflectiveOperationException {
        Object[] values = new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            values[i] = getBean(args.get(i).getRef());
        }

        for (Constructor<?> constructor : beanClass.getDeclaredConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != values.length) {
                continue;
            }
            boolean compatible = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                if (!parameterTypes[i].isAssignableFrom(values[i].getClass())) {
                    compatible = false;
                    break;
                }
            }
            if (compatible) {
                constructor.setAccessible(true);
                return constructor.newInstance(values);
            }
        }
        throw new IllegalStateException("Aucun constructeur compatible trouvé dans " + beanClass.getName());
    }

    private void injectWithSetters(Object instance, List<XmlProperty> properties)
            throws ReflectiveOperationException {
        for (XmlProperty property : properties) {
            Object dependency = getBean(property.getRef());
            String setterName = "set" + Character.toUpperCase(property.getName().charAt(0))
                    + property.getName().substring(1);
            Method setter = findCompatibleSetter(instance.getClass(), setterName, dependency.getClass());
            if (setter == null) {
                throw new NoSuchMethodException("Setter introuvable : " + setterName
                        + " dans " + instance.getClass().getName());
            }
            setter.setAccessible(true);
            setter.invoke(instance, dependency);
        }
    }

    private Method findCompatibleSetter(Class<?> type, String name, Class<?> dependencyType) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (method.getName().equals(name)
                        && method.getParameterCount() == 1
                        && method.getParameterTypes()[0].isAssignableFrom(dependencyType)) {
                    return method;
                }
            }
        }
        return null;
    }

    private void injectIntoFields(Object instance, List<XmlField> fields)
            throws ReflectiveOperationException {
        for (XmlField xmlField : fields) {
            Object dependency = getBean(xmlField.getRef());
            Field field = findField(instance.getClass(), xmlField.getName());
            if (field == null) {
                throw new NoSuchFieldException("Attribut introuvable : " + xmlField.getName()
                        + " dans " + instance.getClass().getName());
            }
            if (!field.getType().isAssignableFrom(dependency.getClass())) {
                throw new IllegalArgumentException("Le bean '" + xmlField.getRef() + "' est incompatible avec le champ "
                        + xmlField.getName());
            }
            field.setAccessible(true);
            field.set(instance, dependency);
        }
    }

    private Field findField(Class<?> type, String name) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                // On cherche aussi dans la superclasse.
            }
        }
        return null;
    }
}
