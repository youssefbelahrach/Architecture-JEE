package ma.enset.miniioc.core;

import ma.enset.miniioc.annotations.Autowired;
import ma.enset.miniioc.annotations.Component;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Conteneur IoC avec scan de packages. Il détecte les classes @Component et
 * injecte les dépendances signalées par @Autowired.
 */
public class AnnotationApplicationContext implements MiniApplicationContext {
    private final Map<String, Class<?>> componentTypes = new LinkedHashMap<>();
    private final Map<String, Object> singletons = new LinkedHashMap<>();
    private final Set<String> currentlyCreating = new LinkedHashSet<>();

    public AnnotationApplicationContext(String basePackage) {
        registerComponents(scanPackage(basePackage));
    }

    public AnnotationApplicationContext(Class<?>... componentClasses) {
        registerComponents(List.of(componentClasses));
    }

    private void registerComponents(Iterable<Class<?>> classes) {
        for (Class<?> type : classes) {
            Component component = type.getAnnotation(Component.class);
            if (component == null) {
                continue;
            }
            String id = component.value().isBlank() ? defaultBeanId(type) : component.value();
            if (componentTypes.putIfAbsent(id, type) != null) {
                throw new IllegalArgumentException("Deux composants portent le nom : " + id);
            }
        }
        if (componentTypes.isEmpty()) {
            throw new IllegalArgumentException("Aucun composant @Component n'a été trouvé.");
        }
    }

    private String defaultBeanId(Class<?> type) {
        String simpleName = type.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    @Override
    public Object getBean(String id) {
        if (singletons.containsKey(id)) {
            return singletons.get(id);
        }
        Class<?> type = componentTypes.get(id);
        if (type == null) {
            throw new IllegalArgumentException("Aucun composant nommé '" + id + "'");
        }
        if (!currentlyCreating.add(id)) {
            throw new IllegalStateException("Dépendance circulaire détectée : " + currentlyCreating + " -> " + id);
        }

        try {
            Object bean = createBean(type);
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
            throw new IllegalArgumentException("Le composant '" + id + "' n'est pas de type " + type.getName());
        }
        return type.cast(bean);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        List<String> candidates = findCandidates(type);
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("Aucun composant compatible avec " + type.getName());
        }
        if (candidates.size() > 1) {
            throw new IllegalStateException("Plusieurs composants compatibles avec " + type.getName()
                    + " : " + candidates);
        }
        return getBean(candidates.get(0), type);
    }

    private Object createBean(Class<?> type) {
        try {
            Object instance = instantiate(type);
            injectFields(instance);
            injectSetters(instance);
            return instance;
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Impossible de créer le composant " + type.getName(), exception);
        }
    }

    private Object instantiate(Class<?> type) throws ReflectiveOperationException {
        List<Constructor<?>> annotatedConstructors = new ArrayList<>();
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                annotatedConstructors.add(constructor);
            }
        }
        if (annotatedConstructors.size() > 1) {
            throw new IllegalStateException("Un seul constructeur peut être annoté @Autowired : " + type.getName());
        }

        Constructor<?> constructor;
        if (annotatedConstructors.size() == 1) {
            constructor = annotatedConstructors.get(0);
        } else {
            try {
                constructor = type.getDeclaredConstructor();
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException("Le composant " + type.getName()
                        + " doit avoir un constructeur vide ou un constructeur @Autowired", exception);
            }
        }

        Object[] arguments = resolveArguments(constructor.getParameterTypes());
        constructor.setAccessible(true);
        return constructor.newInstance(arguments);
    }

    private Object[] resolveArguments(Class<?>[] types) {
        Object[] arguments = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            arguments[i] = getBean(types[i]);
        }
        return arguments;
    }

    private void injectFields(Object instance) throws IllegalAccessException {
        for (Class<?> current = instance.getClass(); current != null; current = current.getSuperclass()) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getType()));
                }
            }
        }
    }

    private void injectSetters(Object instance) throws ReflectiveOperationException {
        for (Class<?> current = instance.getClass(); current != null; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                if (method.getParameterCount() != 1) {
                    throw new IllegalStateException("Une méthode @Autowired doit avoir un seul paramètre : " + method);
                }
                method.setAccessible(true);
                method.invoke(instance, getBean(method.getParameterTypes()[0]));
            }
        }
    }

    private List<String> findCandidates(Class<?> dependencyType) {
        List<String> candidates = new ArrayList<>();
        for (Map.Entry<String, Class<?>> entry : componentTypes.entrySet()) {
            if (dependencyType.isAssignableFrom(entry.getValue())) {
                candidates.add(entry.getKey());
            }
        }
        return candidates;
    }

    private List<Class<?>> scanPackage(String basePackage) {
        String path = basePackage.replace('.', '/');
        List<Class<?>> classes = new ArrayList<>();
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if ("file".equals(url.getProtocol())) {
                    classes.addAll(scanDirectory(basePackage, Path.of(url.toURI())));
                } else if ("jar".equals(url.getProtocol())) {
                    classes.addAll(scanJar(basePackage, url));
                }
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de scanner le package " + basePackage, exception);
        }
        return classes;
    }

    private List<Class<?>> scanDirectory(String basePackage, Path directory) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        try (var paths = Files.walk(directory)) {
            paths.filter(path -> path.toString().endsWith(".class"))
                    .filter(path -> !path.getFileName().toString().contains("$"))
                    .forEach(path -> {
                        String relative = directory.relativize(path).toString();
                        String className = basePackage + "." + relative
                                .replace(File.separatorChar, '.')
                                .replaceAll("\\.class$", "");
                        classes.add(loadClass(className));
                    });
        }
        return classes;
    }

    private List<Class<?>> scanJar(String basePackage, URL url) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        String packagePath = basePackage.replace('.', '/') + "/";
        try (JarFile jar = connection.getJarFile()) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith(packagePath) && name.endsWith(".class") && !name.contains("$")) {
                    classes.add(loadClass(name.substring(0, name.length() - 6).replace('/', '.')));
                }
            }
        }
        return classes;
    }

    private Class<?> loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Classe introuvable pendant le scan : " + name, exception);
        }
    }
}
