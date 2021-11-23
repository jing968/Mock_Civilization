package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import unsw.gloriaromanus.troop.*;

public class Movement implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = 6607119111560727763L;

	private final String[] provinces =  {"Lugdunensis", "Lusitania", "Lycia et Pamphylia", "Macedonia", "Mauretania Caesariensis", "Mauretania Tingitana", "Moesia Inferior", "Moesia Superior", "Narbonensis", "Noricum", "Numidia", "Pannonia Inferior", "Pannonia Superior", "Raetia", "Sardinia et Corsica", "Sicilia", "Syria", "Tarraconensis", "Thracia", "V", "VI", "VII", "VIII", "X", "XI", "Achaia", "Aegyptus", "Africa Proconsularis", "Alpes Cottiae", "Alpes Graiae et Poeninae", "Alpes Maritimae", "Aquitania", "Arabia", "Armenia Mesopotamia", "Asia", "Baetica", "Belgica", "Bithynia et Pontus", "Britannia", "Cilicia", "Creta et Cyrene", "Cyprus", "Dacia", "Dalmatia", "Galatia et Cappadocia", "Germania Inferior", "Germania Superior", "I", "II", "III", "IV", "IX", "Iudaea"};

    private Province src;
    private Province dest;
    private Map<String, Unit> units;
    private int[] toVisit;
    private double movement;
    private ArrayList<Province> visited;
    private ArrayList<Province> queue;

    public Movement(Province src, Province dest, Map<String, Unit> units) {
        this.src = src;
        this.dest = dest;
        this.units = new HashMap<String, Unit>();
        this.units.putAll(units);
    }

    /**
     * Move the unit from src to dest using bfs.
     *  Not really the shortest path but it is what it is.
     */
    public String moveUnit() {
        movement = getLowestMovement();

        toVisit = new int[60];
        for (int i = 0; i < provinces.length; i++) {
            toVisit[i] = -1;
        }

        toVisit[getPathIndex(src)] = -2;

        visited = new ArrayList<Province>();
        queue = new ArrayList<Province>();

        queue.add(src);
        while (!queue.isEmpty()) {
            Province node = queue.get(0);
            visited.add(node);
            queue.remove(0);

            if (dest.compareProvinceName(node)) {
                ArrayList<String> path = getPath(toVisit);
                int required_points = addPath(path);
                if (required_points > movement) {
                    return "You cannot move the troops here.";
                } else {
                    transferUnits(required_points);
                    return "Successfully Moved Units!";
                }
            }

            for (Province n : node.getNeighbours()) {
                // Check if we have seen the province before, and if we own the province.
                if (visited.contains(n) || queue.contains(n) || !src.getFaction().compareFactionName(n.getFaction())) continue;
                queue.add(n);
                toVisit[getPathIndex(n)] = getPathIndex(node);
                
            }
        }
        return "Moving Units unsucessful";
    }

    /**
     * Looks through units and determine the troop with the lowest movement points.
     * @return troop with the lowest movement points.
     * 
     */
    public double getLowestMovement() {
        double lowestMovementPoint = 15;
        for (Map.Entry<String, Unit> unit : units.entrySet()) {
            Unit u = unit.getValue();
            if (lowestMovementPoint > u.getCurrentMovement()) {
                lowestMovementPoint = u.getCurrentMovement();
            }
        }
        return lowestMovementPoint;
    }

    /**
     * Searches frough provinces (string[]) to find index of the provinded province.
     * @param province
     * @return  Int Index of the provided province inside provinces.
     */
    public int getPathIndex(Province province) {
        int index = -5;
        for (int i = 0; i < provinces.length; i++) {
            if (province.getProvince().equals(provinces[i])) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Gets the "shortest path" from src to dest by recalling it backwards.
     * @param toVisit
     * @return  ArrayList<String> of the first path that reaches dest.
     */
    public ArrayList<String> getPath(int[] toVisit) {
        ArrayList<String> path = new ArrayList<String>();
        int position = toVisit[getPathIndex(dest)];
        path.add(provinces[position]);

        while (toVisit[position] != -2) {
            position = toVisit[position];
            path.add(provinces[position]);
        }
        return path;
    }
    
    /**
     * Compare visited against the shorestpath against visited.
     *  And add all of the movement points required.
     * @return 
     */

    /**
     * Compare visited against the shorestpath against visited.
     *  And add all of the movement points required.
     * @param visited       Contains list visited path 
     * @param shortestPath  Contains the shortest path.
     * @return  int Movement points required to get from src to dest.
     */
    public int addPath(ArrayList<String> shortestPath) {
        int movement = 0;
        for (int i = 0; i < shortestPath.size(); i++) {
            for (Province p : visited) {
                movement += getMovementPoints(shortestPath.get(i), p);
            }
        }
        return movement;
    }

    /**
     * Compares path against province to determine if they are the same province
     * @param path
     * @param province
     * @return int returns movement point of the province or 0 if province is not the same.
     */
    public int getMovementPoints(String path, Province province) {
        if (path.equals(province.getProvince())) {
            return province.getMovementPoints();
        }
        return 0;
    }

    /**
     * Transfers units inside src to dest.
     * 
     */
    public void transferUnits(double movement) {
        dest.combineUnits(units, movement);
        src.clearUnits(units);
    }
}