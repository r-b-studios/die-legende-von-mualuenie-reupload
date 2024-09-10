// Simo Münc

package engine.events;

import engine.tools.SafeList;

import java.util.function.Consumer;

// Ereignis, dass einen Parameter übergibt
public class Event<T> {

    // Abonnenten
    private final SafeList<Consumer<T>> subscribers = new SafeList<>();

    // Event abonnieren
    public void subscribe(Consumer<T> subscriber) {
        subscribers.add(subscriber);
    }

    // Event dabonnieren
    public void unsubscribe(Consumer<T> subscriber) {
        subscribers.remove(subscriber);
    }

    // Event abfeuern
    public void invoke(T value) {
        subscribers.forEach(sub -> {
            sub.accept(value);
        });
    }
}