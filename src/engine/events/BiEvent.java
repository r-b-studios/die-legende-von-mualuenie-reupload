// Simo Münc

package engine.events;

import engine.tools.SafeList;

import java.util.function.BiConsumer;

// Ereignis, dass zwei verschiedene Parameter übergibt
public class BiEvent<K, V> {

    // alle Abonnenten des Events
    private final SafeList<BiConsumer<K, V>> subscribers = new SafeList<>();

    // Event abonnieren
    public void subscribe(BiConsumer<K, V> subscriber) {
        subscribers.add(subscriber);
    }

    // Event deabonnieren
    public void unsubscribe(BiConsumer<K, V> subscriber) {
        subscribers.remove(subscriber);
    }

    // Event abfeuern
    public void invoke(K key, V value) {
        subscribers.forEach(sub -> {
            sub.accept(key, value);
        });
    }
}
