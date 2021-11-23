package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class WinCondition implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 5790946674064608585L;
    private ArrayList<ArrayList<String>> conditions;

    public WinCondition() {
        this.conditions = new ArrayList<ArrayList<String>>();
    }


    /**
     * Generate win conditions for players
     */
    public void generateConds() {
        ArrayList<String> goals = new ArrayList<String>();
        ArrayList<String> relations = new ArrayList<String>();
        ArrayList<Boolean> ref = new ArrayList<Boolean>();
        for (int i = 0; i < 3; i++) {
            ref.add(false);
        }
        Random rng = new Random();
        int num_goal = rng.nextInt(3);
        for (int i = 0; i <= num_goal; i++) {
            int goal_ref = rng.nextInt(3);
            while (ref.get(goal_ref).equals(true)) {
                goal_ref = rng.nextInt(3);
            }
            ref.set(goal_ref, true);
            switch (goal_ref) {
                case 0:
                    goals.add("conquest");
                    break;
                case 1:
                    goals.add("wealth");
                    break;
                case 2:
                    goals.add("treasure");
                    break;
            }
        }
        int num_relations = num_goal - 1;
        for (int i = 0; i <= num_relations; i++) {
            boolean and = rng.nextBoolean();
            if (and) {
                relations.add("and");
            } else {
                relations.add("or");
            }
        }
        //System.out.println(goals);
        //System.out.println(relations);

        ArrayList<String> cond1 = new ArrayList<String>();
        cond1.add(goals.get(0));
        conditions.add(cond1);
        for (int i = 0; i < relations.size(); i++) {
            if (relations.get(i).equals("and")) {
                cond1.add(goals.get(i + 1));
            } else {
                ArrayList<String> new_cond = new ArrayList<String>();
                new_cond.add(goals.get(i + 1));
                conditions.add(new_cond);
            }
        }
        //System.out.println(conditions);
    }

    /**
     * Add condition to list of conditions
     * @param cond Array of string
     */
    public void addConds(ArrayList<String> cond) {
        conditions.add(cond);
    }

    /**
     * Remove condition from the list of conditions
     * @param cond Array of string 
     */
    public void removeConds(ArrayList<String> cond) {
        conditions.remove(cond);
    }
    
    /**
     * Getter method for conditons
     */
    public ArrayList<ArrayList<String>> getConditions() {
        return conditions;
    }

    @Override
    public String toString() {
        String s = "";
        for (ArrayList<String> list : conditions) {
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    s += list.get(i) + " AND ";
                } else {
                    s += list.get(i) + "\n";
                }
            }
        }
        return s;
    }
}
