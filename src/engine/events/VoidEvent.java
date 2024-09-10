// Simo Münc

package engine.events;

import engine.tools.SafeList;

// Ereignis
public class VoidEvent {

    // Abonnenten
    private final SafeList<Runnable> subscribers = new SafeList<>();

    // Event abonnieren
    public void subscribe(Runnable subscriber) {
        subscribers.add(subscriber);
    }

    // Event deabonnieren
    public void unsubscribe(Runnable subscriber) {
        subscribers.remove(subscriber);
    }

    // Event abfeuern
    public void invoke() {
        subscribers.forEach(Runnable::run);
    }
}