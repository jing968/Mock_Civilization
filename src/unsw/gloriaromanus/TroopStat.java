package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.Map;

import unsw.gloriaromanus.troop.*;

public class TroopStat implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -4883096166282367205L;
    Map<String, Troop> u;

    public TroopStat() {
        u = Map.ofEntries(
            Map.entry("archerman_egyptian", new Archerman_egyptian(false)),
            Map.entry("archerman_roman",    new Archerman_roman(false)),
            Map.entry("archerman",          new Archerman(false)),
            Map.entry("camel_alone",        new Camel_alone(false)),
            Map.entry("camel_archer",       new Camel_archer(false)),
            Map.entry("camel",              new Camel(false)),
            Map.entry("cannon",             new Cannon()),
            Map.entry("chariot",            new Chariot(false)),
            Map.entry("crossbowman",        new Crossbowman(false)),
            Map.entry("druid",              new Druid(false)),
            Map.entry("elephant_alone",     new Elephant_alone(false)),
            Map.entry("elephant_archer",    new Elephant_archer(false)),
            Map.entry("elephant",           new Elephant(false)),
            Map.entry("flagbearer_egyptian",new Flagbearer_egyptian(false)),
            Map.entry("flagbearer_roman",   new Flagbearer_roman(false)),
            Map.entry("flagbearer",         new Flagbearer(false)),
            Map.entry("hoplite",            new Hoplite(false)),
            Map.entry("horse_alone",        new Horse_alone(false)),
            Map.entry("horse_heavy_cavalry",new Horse_heavy_cavalry(false)),
            Map.entry("horse_lancer",       new Horse_lancer(false)),
            Map.entry("horse_archer",       new Horse_archer(false)),
            Map.entry("netfighter",         new Netfighter(false)),
            Map.entry("pikeman",            new Pikeman(false)),
            Map.entry("slingerman",         new Slingerman(false)),
            Map.entry("spearman",           new Spearman(false)),
            Map.entry("swordman",           new Swordman(false)),
            Map.entry("trebuchet",          new Trebuchet())
        );
    }
    
    /**
     * Given generated table, and param
     *  Find the unit within the table
     * @param troop
     * @return return the clone (new instance) of the object
     */
    public Troop generateTroop(String troop) {
        Troop t = null;
        if (u.containsKey(troop)) {
            t = u.get(troop).clone();
        }
        return t;
    }
}
