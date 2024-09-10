package engine.main;

// stellt eine Komponente eines GameObject dar, die dem Objekt Funktionen verleiht
public abstract class Component {

    // das GameObject dem die Component gehört
    GameObject owner;

    // wird jeden Frame aufgerufen
    abstract void update();

    public GameObject getOwner()
    {
        return owner;
    }
}