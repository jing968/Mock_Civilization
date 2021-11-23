package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import unsw.gloriaromanus.troop.*;

public class Battle implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -1458004897576506042L;
    private Province atk;
    private Province def;
    private ArrayList<Unit> routed;

    /**
     * Takes in two province to place in a battle
     * @param a invader province
     * @param d defender province
     */
    public Battle(Province a, Province d){
        this.atk = a;
        this.def = d;
        routed = new ArrayList<Unit>();
    }

    /**
     * Function to start the battle
     * @return return battle result;
     */
    public String start(){
        Random rnd = new Random();
        ifTower();
        while (atk.getUnitsList().size() != 0 && def.getUnitsList().size() != 0 ){
            Druidic_fervour(atk.getUnitsList(),def.getUnitsList());
            int a = rnd.nextInt(atk.getUnitsList().size());
            int b = rnd.nextInt(def.getUnitsList().size());
            Phalanx(atk.getUnitsList().get(a));
            Phalanx(def.getUnitsList().get(b));
            Skirmish skirmish = new Skirmish(atk.getUnitsList().get(a),def.getUnitsList().get(b),def.haveWall());
            String result = skirmish.start();
            atk.updateArmy();
            def.updateArmy();
            // handle result
            if (result.equals("attack")){
                if(atk.getUnitsList().size() > a && a >= 0){
                    routed.add(atk.getUnitsList().get(a));
                    atk.getUnitsList().remove(a);
                }
            }
            else if (result.equals("defend")){
                if(def.getUnitsList().size() > b && b >= 0){
                    routed.add(atk.getUnitsList().get(b));
                    def.getUnitsList().remove(b);
                }
            }
            else if (result.equals("both")){
                if( 0<= a && a < atk.getUnitsList().size()){
                    routed.add(atk.getUnitsList().get(a)); 
                    atk.getUnitsList().remove(a);                                     
                }
                if(0 <= b && b < def.getUnitsList().size() ){
                    routed.add(def.getUnitsList().get(b));
                    def.getUnitsList().remove(b);                
                }




            }
            // sucess / fail / draw case doesnt need operation on units
        }
        if(atk.getUnitsList().size() == 0) {
            for(Unit u:def.getUnitsList()){
                if (u.getTroop().getType().equals("ballista") || u.getTroop().getType().equals("ballista") ){
                    def.getUnitsList().remove(u);
                }
            }

            // routed army will join the winner of the battle
            for (Unit units : routed){
                for(int i = 0; i<= units.getNumTroops() ; i++){
                    def.addUnit(units.getTroop());
                }
            }
            return "defend";
        }
        else {
            def.getFaction().removeProvince(def);
            def.setFaction(atk.getFaction());
            atk.getFaction().addProvince(def);
            // routed army will join the winner in the new province if the invader won the battle
            for (Unit units : routed){
                for(int i = 0; i<= units.getNumTroops() ; i++){
                    def.addUnit(units.getTroop());
                }
            }
            // move half of the invader army into the new province
            Map<String, Unit> units = atk.getUnits();
            for (String s:units.keySet()){
                Unit u = units.get(s);
                Double count = u.getNumTroops();
                u.updateNum((count/2));
                Troop troop = u.getTroop().clone();
                for(Double i = 0.0; i < count/2; i ++){
                    def.addUnit(troop);
                }

            }
            return "attack";
        }
        
    }

    /**
     * If tower is built in the defending province, it can also be treated as a troop
     */
    public void ifTower(){
        if(def.haveWall()){
            Troop temp_tower = def.getDefendtower();
            def.addUnit(temp_tower);
        }
    }
 
    /**
     * Hoplites or pikemen have double the melee defence, but half of the speed, as they are otherwise configured to have
     */
    public void Phalanx(Unit unit){
        System.out.println(unit.getTroop().getType());
        if(unit.getTroop().getType().equals("hoplite") || unit.getTroop().getType().equals("pikeman")){
            unit.getTroop().setArmour(unit.getTroop().getArmour() * 2);
            unit.getTroop().setSpeed(unit.getTroop().getSpeed() * 0.5);        
        }

    }

    /**
     * If durid is present for any army, allies and enemy morale will be modified 
     * @param atk lists of attacking units
     * @param def lists of defending units
     */
    public void Druidic_fervour(ArrayList<Unit> atk, ArrayList<Unit> def){

        for (Unit u: atk){
            System.out.println(u.getTroop().getType());
            if(u.getTroop().getType().equals("druid")) {
                double stack = u.getNumTroops();
                if (stack > 5) stack = 5;
                for (Unit d: atk){
                    d.getTroop().buff_druidic_fevour(stack);
                }
                for (Unit d: def){
                    d.getTroop().debuff_druidic_fevour(stack);
                }
            } 
        }
        for (Unit u: def){
            System.out.println(u.getTroop().getType());
            if(u.getTroop().getType().equals("druid")) {
                double stack = u.getNumTroops();
                if (stack > 5) stack = 5;
                for (Unit d: def){
                    d.getTroop().buff_druidic_fevour(stack);
                }
                for (Unit d: atk){
                    d.getTroop().debuff_druidic_fevour(stack);
                }

            }
        }

    }


}


