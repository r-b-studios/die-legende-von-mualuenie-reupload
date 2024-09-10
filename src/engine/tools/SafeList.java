package engine.tools;

import java.util.ArrayList;
import java.util.function.Consumer;

// Liste zur Vermeidung von ConcurrrentModificationException
public class SafeList<T> extends ArrayList<T> {


    @Override
    public void forEach(Consumer<? super T> action) {
        ArrayList<Runnable> actions = new ArrayList<>();

        // speichern der Aktionen
        super.forEach(obj -> {
            actions.add(() -> {
                action.accept(obj);
            });
        });

        // gemeinsames Ausführen aller Aktionen
        actions.forEach(Runnable::run);
    }
}