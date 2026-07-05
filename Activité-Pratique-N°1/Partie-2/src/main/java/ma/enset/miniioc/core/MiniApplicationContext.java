package ma.enset.miniioc.core;

/** API minimale commune aux deux conteneurs IoC. */
public interface MiniApplicationContext {
    Object getBean(String id);

    <T> T getBean(String id, Class<T> type);

    <T> T getBean(Class<T> type);
}
