package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.Random;
import unsw.gloriaromanus.troop.*;

public class Skirmish implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 3068879969161760927L;
    private Unit attack;
    private Unit defend;
    private double atk_cas;
    private double def_cas;
    private boolean wall;
    private int rounds;

    public Skirmish (Unit atk, Unit def, boolean wall){
        this.attack = atk;
        this.defend = def;
        this.atk_cas = 0;
        this.def_cas = 0;
        this.wall = wall;
        this.rounds = 0;
    }

    /**
     * Determine the type of engagement based on the situation 
     * @return return engagetype as a string "MvMM" for Melee vs Melee Melee engagement; "RvRR" for Ranged vs Ranged Ranged engagement; "MM" for mixed melee; "MR" for mixed ranged
     */
    public String engagetype(){
        
        if(!attack.isRanged() && !defend.isRanged()){
            //System.out.println("melee engagement");
            return "MvMM";
        }else if (attack.isRanged() && defend.isRanged()){
            //System.out.println("missile engagement");
            return "RvRR";
        }
        else{
            if(defend.getTroop().getType().equals("archertower") || defend.getTroop().getType().equals("ballista")){
                return "MR";
            }
            double melee = 0;
            double range = 0;
            if(wall){
                melee = 0.5;
                range = 0.5;
            }else{
                melee = 0.1;
                range = 0.9;
            }

            if(attack.isRanged()){
                melee = melee + (attack.getTroop().getSpeed() - defend.getTroop().getSpeed())*0.1;
            }else{
                melee = melee + (defend.getTroop().getSpeed() - attack.getTroop().getSpeed())*0.1;
            }
            range = 1 - melee;
            if(range > 0.95) range = 0.95;
            if(melee > 0.95) melee = 0.95;
            Random random = new Random();
            double diced = random.nextDouble();
            if(diced <= melee)return "MM";
            return "MR";          
        }

    }

    /**
     * Melee unit vs melee unit in a melee engagement
     */
    public String MvM_melee_engage(){
        double a_dmg = attack.getNumTroops()*0.1 * (attack.getTroop().getAttack()/ (defend.getTroop().getArmour() + defend.getTroop().getShieldDefense()) * positive_n());
        double d_dmg = defend.getNumTroops()*0.1 * (defend.getTroop().getAttack()/ (attack.getTroop().getArmour() + attack.getTroop().getShieldDefense()) * positive_n());
        System.out.println(a_dmg);
        System.out.println(attack.getNumTroops());
        System.out.println(d_dmg);
        System.out.println(defend.getNumTroops());
        a_dmg = checkDmg(a_dmg, defend.getTroop());
        d_dmg = checkDmg(d_dmg, attack.getTroop());
        inflict(a_dmg, defend, "defend");
        inflict(d_dmg, attack, "attack");
        System.out.println("-----------Melee Engagment--------------");
        System.out.println("Defend unit inflicts " + d_dmg + " casualties to attack unit");
        System.out.println("Attack unit inflicts " + a_dmg + " casualties to defend unit");
        if(isBroken(defend.getTroop(), "defend") && a_dmg != 0){
            if(isBroken(attack.getTroop(), "attack")){
                return "both";
            }else{
                if(!isRouting("defend")){
                    defend.getTroop().setAttack(0);
                }else{
                    return "defend";
                }
            }
        }
        if(isBroken(attack.getTroop(), "attack") && d_dmg != 0){
            if(isBroken(defend.getTroop(), "defend")){
                return "both";
            }else{
                if(!isRouting("attack")){
                    attack.getTroop().setAttack(0);
                }else{
                    return "attack";
                }
            }
        }
        return "none";
    }

    /**
     * Range uni vs range unit in a ranged engagement
     */
    public String RvR_range_engage(){
        double a_dmg = attack.getNumTroops()*0.1 * (attack.getTroop().getAttack()/ (defend.getTroop().getArmour() + defend.getTroop().getShieldDefense()) * positive_n());
        double d_dmg = defend.getNumTroops()*0.1 * (defend.getTroop().getAttack()/ (attack.getTroop().getArmour() + attack.getTroop().getShieldDefense()) * positive_n());
        a_dmg = checkDmg(a_dmg, defend.getTroop());
        d_dmg = checkDmg(d_dmg, attack.getTroop());
        inflict(a_dmg, defend, "defend");
        inflict(d_dmg, attack, "attack");
        System.out.println("-----------Range Engagment--------------");
        System.out.println("Defend unit inflicts " + d_dmg + " casualties to attack unit");
        System.out.println("Attack unit inflicts " + a_dmg + " casualties to defend unit");
        if (!(defend.getTroop().getType().equals("archertower") || (defend.getTroop().getType().equals("ballista")))){
            if(isBroken(defend.getTroop(), "defend") && a_dmg != 0 ){
                if(isBroken(attack.getTroop(), "attack")){
                    return "both";
                }else{
                    if(!isRouting("defend")){
                        defend.getTroop().setAttack(0);
                    }else {
                        return "defend";
                    }
                }
            }
            if(isBroken(attack.getTroop(), "attack") && d_dmg != 0 ){
                if(isBroken(defend.getTroop(), "defend")){
                    return "both";
                }else{
                    if(!isRouting("attack")){
                        attack.getTroop().setAttack(0);
                    }else {
                        return "attack";
                    }
                }
            }
        }
        return "none";
    }

    /**
     * Melee unit vs range unit in a melee engagement
     */
    public String MvR_melee_engage(){
        double a_dmg = attack.getNumTroops()*0.1 * (attack.getTroop().getAttack()/ (defend.getTroop().getArmour() + defend.getTroop().getShieldDefense()) * positive_n());
        double d_dmg = defend.getNumTroops()*0.1 * (defend.getTroop().getAttack()/ (attack.getTroop().getArmour() + attack.getTroop().getShieldDefense()) * positive_n());
        a_dmg = checkDmg(a_dmg, defend.getTroop());
        d_dmg = checkDmg(d_dmg, attack.getTroop());
        inflict(a_dmg, defend, "defend");
        inflict(d_dmg, attack, "attack");
        System.out.println("-----------Melee Engagment--------------");
        System.out.println("Defend unit inflicts " + d_dmg + " casualties to attack unit");
        System.out.println("Attack unit inflicts " + a_dmg + " casualties to defend unit");
        if(isBroken(defend.getTroop(), "defend") && a_dmg != 0){
            if(isBroken(attack.getTroop(), "attack")){
                return "both";
            }else{
                if(!isRouting("defend")){
                    defend.getTroop().setAttack(0);
                }else{
                    return "defend";
                }
            }
        }
        if(isBroken(attack.getTroop(), "attack") && d_dmg != 0 ){
            if(isBroken(defend.getTroop(), "defend")){
                return "both";
            }else{
                if(!isRouting("attack")){
                    attack.getTroop().setAttack(0);
                }else{
                    return "attack";
                }
            }
        }
        return "none";
    }

    /**
     * Melee unit vs range unit in a ranged engagement
     */
    public String MvR_range_engage(){
        Unit range;
        Unit melee;
        if(attack.isRanged()){
            range = attack;
            melee = defend;         
        }else{
            melee = attack;
            range = defend;
        }
        double dmg = range.getNumTroops()*0.1 * (range.getTroop().getAttack()/ (melee.getTroop().getArmour() + melee.getTroop().getShieldDefense()) * positive_n());
        dmg = checkDmg(dmg,melee.getTroop());
        System.out.println("-----------Range Engagment--------------");
        System.out.println("Ranged unit inflicts " + dmg + " casualties to melee unit");
        System.out.println("Melee units cannot inflict casualties to ranged unit in a ranged engagement");
        if(attack.isRanged()){
            inflict(dmg, defend, "defend");
            if(!(defend.getTroop().getType().equals("archertower") || (defend.getTroop().getType().equals("ballista")))){
                if(isBroken(defend.getTroop(), "defend")){
                    if(!isRouting("defend")){
                        defend.getTroop().setAttack(0);
                    }else{
                        return "defend";
                    }
                }
            }
        }else{
            inflict(dmg, attack, "attack");
            if (!(defend.getTroop().getType().equals("archertower") || (defend.getTroop().getType().equals("ballista")))){
                if(isBroken(attack.getTroop(), "attack")){
                    if(!isRouting("defend")){
                        attack.getTroop().setAttack(0);
                    }
                    else{
                        return "attack";
                    }
                }
            }
        }

        return "none";
    }

    /** 
     * Check if inflicting dmg exceed enemy max hp or lower than 0
     * @return return max enemy hp if excced, return 0 if lower than enemy hp, return original dmg if in between
     */
    public double checkDmg(double dmg, Troop oppo){
        if(dmg < 0) return 0;
        else return dmg;
    }

    /**
     * Update units when damaged is calculated
     * @param dmg amount of damage being dealt
     * @param unit Unit which are receiving damage
     * @param side string stating if the unit is defender or attacker
     */
    public void inflict (double dmg, Unit unit, String side){
        double hp = unit.getTroop().getHealh();
        double cas = dmg/hp;
        if(side.equals("attack")) {
            if (cas > attack.getNumTroops()) cas = attack.getNumTroops();
            atk_cas = atk_cas + cas;
            attack.updateNum(cas);
        }
        else if(side.equals("defend")) {
            if (cas > defend.getNumTroops()) cas = defend.getNumTroops();
            def_cas = def_cas + cas;
            defend.updateNum(cas);
        }
        System.out.println(cas + " units died on the " + side + "side");
    }

    /**
     * Calculate if a unit is broken according to formula
     * @param troop troop object of the current troop type
     * @param side string stating if the unit is attacker or defender
     * @return return true if borken, false otherwise
     */
    public boolean isBroken(Troop troop, String side){
        // only towers have morale of 11, towers doesn't break
        if(troop.getMorale() == 11) return false;
        double base = 1-(troop.getMorale() * 0.1);
        double break_chance = base;
        if(side.equals("attack")){
            break_chance = base + (atk_cas/attack.getNumTroops())/(def_cas/defend.getNumTroops()) * 0.1;
        }else{
            break_chance = base + (def_cas/defend.getNumTroops()/(atk_cas/attack.getNumTroops())) * 0.1;
        }
        if (break_chance < 0.05) break_chance  = 0.05;
        if (break_chance > 0.95) break_chance  = 0.95;
        Random random = new Random();
        double diced = random.nextDouble();
        System.out.println("chance to break is " + break_chance);
        System.out.println("dice roll out to be " + diced);
        if(diced <= break_chance){
            System.out.println(side + " side is broken");
            return true;
        }
        return false;
    }


    /**
     * Calculate if a unit is routing from a battle
     * @param side string stating if the unit is a ttacker or defender
     */
    public boolean isRouting(String side){
        double chance;
        if(side.equals("attack")){
            chance = 0.5 + 0.1*(attack.getTroop().getSpeed() - defend.getTroop().getSpeed());
        }else{
            chance = 0.5 + 0.1*(defend.getTroop().getSpeed() - attack.getTroop().getSpeed());
        }
        if(chance < 0.1) chance = 0.1;
        if(chance > 1) chance = 1;
        System.out.println(chance);
        Random random = new Random();
        double diced = random.nextDouble();
        System.out.println("chance to rout is " + chance);
        System.out.println("dice roll out to be " + diced);
        if(diced <= chance ) {
            System.out.println(side + " side is attempting to rout");
            return true;
        }
        return false;
    }

    /**
     * Getter function for a positive factor for damage calculation formula
     */
    public double positive_n(){
        Random random = new Random();
        double var = random.nextGaussian()+1;
        while(var <= 0 ){
            var = random.nextGaussian()+1;
        }
        return var;
    }
    /**
     * Once a skirmish is created, start methods starts the skirmish till there is a result
     * @return return DRAW if exceed 200 rounds, return ATTACK if attacker flee, return DEFEND if defender flee, return BOTH if both flee, return FAIL if defender won, return SUCESS if attacker won
     */
    public String start(){
        while(attack.getNumTroops() != 0 && defend.getNumTroops() != 0 ){
            if(rounds >= 200){
                System.out.println("Draw");
                return "draw";
            }
            String result;
            if (engagetype().equals("MR")){
                result = MvR_range_engage();
            }else if(engagetype().equals("MM")){
                result = MvR_melee_engage();
            }else if(engagetype().equals("MvMM")){
                result = MvM_melee_engage();
            }else{
                result = RvR_range_engage();
            }
            // handle result
            if (result.equals("both")) return "both";
            else if (result.equals("attack")) return "attack";
            else if (result.equals("defend")) return "defend";
            rounds = rounds + 1;
            if (attack.getNumTroops() <=0) return "fail";
            if (defend.getNumTroops() <=0) return "sucess";
        }
        if (attack.getNumTroops() <=0) return "fail";
        else  return "sucess";

    }

}
