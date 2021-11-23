package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.Map;

import unsw.gloriaromanus.infrastructure.*;

public class InfrastructureStat implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4918728044465639877L;
    Map<String, Infrastructure> i;

    public InfrastructureStat() {
        i = Map.ofEntries(
            Map.entry("troopProduction", new TroopProduction()),
            Map.entry("wall", new Wall()),
            Map.entry("farm", new Farm()),
            Map.entry("road", new Road())
        );
    }

    /**
     * Within the generated table, attempt to find infrastructure given param
     * @param infrastructure
     * @return infrastructure, otherwise null.
     */
    public Infrastructure generateInfrastructure(String infrastructure) {
        if (i.containsKey(infrastructure)) {
            Infrastructure inf = i.get(infrastructure);
            return inf;
        }
        return null;
    }
}
