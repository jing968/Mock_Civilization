package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;
import unsw.gloriaromanus.troop.*;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;
import unsw.gloriaromanus.infrastructure.*;

public class UnitTest {
    /**
     * Tests Wealth and tax
     */
    @Test
    public void testWealthAndTax() {
        GameState game = new GameState();

        Faction faction = new Faction("Roman");
        game.addFaction(faction);
        faction.setGameState(game);
        assertEquals(faction.getGameState(), game);
        assertEquals(faction.getFaction(), "Roman");

        faction.setTaxRate("Someplace", "taxRate");

        Province province = new Province("Someplace");
        faction.addProvince(province);

        // Check Wealth and gold generation
        assertEquals(province.getWealth(), 0);
        assertEquals(faction.getGold(), 2000);

        game.iterateTurns();
        assertEquals(province.getWealth(), 10);
        assertEquals(faction.getGold(), 2001);

        game.iterateTurns();
        assertEquals(province.getWealth(), 20);
        assertEquals(faction.getGold(), 2003);

        game.iterateTurns();
        assertEquals(province.getWealth(), 30);
        assertEquals(faction.getGold(), 2006);

        // Check wealth and gold generation after tax is applied
        faction.setTaxRate("Someplace", "Normal tax");
        game.iterateTurns();
        assertEquals(province.getWealth(), 30);
        assertEquals(faction.getGold(), 2010.5);

        faction.setTaxRate("Someplace", "High tax");
        game.iterateTurns();
        assertEquals(province.getWealth(), 20);
        assertEquals(faction.getGold(), 2014.5);

        faction.setTaxRate("Someplace", "Very high tax");
        game.iterateTurns();
        assertEquals(province.getWealth(), -10);
        assertEquals(faction.getGold(), 2012.0);

        // See if morale decreases if tax, Very high tax.
        Archerman troop = new Archerman(false);
        province.addUnit(troop);
        assertEquals(troop.getMorale(), 3);
        assertEquals(province.getUnits().size(), 1);

        faction.setTaxRate("Someplace", "Very high tax");
        game.iterateTurns();
        assertEquals(province.getWealth(), -40);
        assertEquals(faction.getGold(), 2002.0);

        assertEquals(troop.getMorale(), 2);
        assertEquals(province.getUnits().size(), 1);

        faction.setTaxRate("Someplace", "Low tax");
        game.iterateTurns();
        assertEquals(province.getWealth(), -30);
        assertEquals(faction.getGold(), 1999.0);

        faction.setTaxRate("Someplace", "tax tax");
        assertEquals(province.getTaxRate(), "Low tax");
    }


    // Test Queue times for buildings
    @Test
    public void testBuildingQueueTimes() {

        GameState game = new GameState();

        Faction faction = new Faction("Roman");
        game.addFaction(faction);
        faction.setGameState(game);
        faction.setFaction("Roman");

        Province province = new Province("Someplace");
        assertEquals(province.getFaction(), null);
        faction.addProvince(province);
        assertEquals(province.getFaction(), faction);

        assertEquals(province.getTroopProduction(), null);

        Structure s = province.getStructures();
        assertEquals(s.getFarmChain(), 0);
        assertEquals(s.getRoadChain(), 0);
        assertEquals(faction.upgradeInfrastructure("Someplace", "troopProduction"), "TroopProduction doesn't exist");
        assertEquals(faction.upgradeInfrastructure("Someplace", "farm"), "Farm doesn't exist");
        assertEquals(faction.upgradeInfrastructure("Someplace", "archertower"), "Upgrade Unsuccessful");
        assertEquals(faction.upgradeInfrastructure("Someplace", "ballistatower"), "Upgrade Unsuccessful");
        assertEquals(faction.upgradeInfrastructure("Someplace", "road"), "Road doesn't exist");
        assertEquals(s.farmToString(), "null");
        assertEquals(s.troopToString(), "null");
        assertEquals(s.roadToString(), "null");
        assertEquals(s.wallToString(), "null");


        // Build rubbish
        faction.buildInfrastructure("gwa", "troopProduction");
        faction.buildInfrastructure("Someplace", "wa");

        // Build troopProduction
        faction.buildInfrastructure("Someplace", "troopProduction");
        assertEquals(province.getQueue().size(), 1);
        assertEquals(province.getGold(), 1880);
        faction.buildInfrastructure("Someplace", "troopProduction");
        assertEquals(province.getQueue().size(), 1);
        assertEquals(province.getGold(), 1880);

        // Build Road
        faction.buildInfrastructure("Someplace", "road");
        assertEquals(province.getQueue().size(), 2);
        game.iterateTurns();
        assertEquals(province.getQueue().size(), 2);

        faction.buildInfrastructure("Someplace", "farm");
        assertEquals(province.getQueue().size(), 3);

        assertEquals(province.haveWall(), false);
        faction.buildInfrastructure("Someplace", "wall");
        assertEquals(province.getQueue().size(), 4);

        assertEquals(province.getGold(), 1571);
        faction.setGold(-2000);
        assertEquals(province.getGold(), 0);

        // Build something again when there is no gold.
        faction.buildInfrastructure("Someplace", "road");
        assertEquals(province.getQueue().size(), 4);
        assertEquals(s.queueToString(), "TroopProduction: 5.0\nRoad: 3.0\nFarm: 4.0\nWall: 4.0\n");
        game.iterateTurns();
        game.iterateTurns();
        game.iterateTurns();

        assertEquals(province.getQueue().size(), 0);

        // Attempt to upgrade when theere is no gold
        faction.upgradeInfrastructure("Someplace", "road");

        faction.setGold(1521);


        assertEquals(s.getProductionchain(), 1);
        faction.upgradeInfrastructure("Someplace", "troopProduction");
        assertEquals(s.getProductionchain(), 2);

        assertEquals(s.getFarmChain(), 1);
        faction.upgradeInfrastructure("Someplace", "farm");
        assertEquals(s.getFarmChain(), 2);

        faction.upgradeInfrastructure("Someplace", "wall");
        assertEquals(s.wallToString(), "wall"); 

        assertEquals(s.getRoadChain(), 1);
        faction.upgradeInfrastructure("Someplace", "road");
        assertEquals(s.getRoadChain(), 2);

        faction.upgradeInfrastructure("Someplace", "archertower");
        assertEquals(s.wallToString(), "archer"); 


        province.setWall(new Wall());
        faction.upgradeInfrastructure("Someplace", "ballistatower");
        assertEquals(faction.getGold(), 960.0);

        // Max out Road
        faction.upgradeInfrastructure("Someplace", "road");
        assertEquals(s.getRoadChain(), 3);

        faction.upgradeInfrastructure("Someplace", "road");
        assertEquals(s.getRoadChain(), 3);

        faction.upgradeInfrastructure("Someplaca", "building");
        faction.upgradeInfrastructure("Someplace", "building");

        assertEquals(s.troopToString(), "2 MaxSlots: 3, Remaining: 3"); 
        assertEquals(s.farmToString(), "2"); 
        assertEquals(s.wallToString(), "ballista"); 
        assertEquals(s.roadToString(), "3"); 

        faction.buildInfrastructure("Someplace", "troopProduction");

        faction.removeProvince(province);
        faction.setGold(-10000);
        faction.upgradeInfrastructure("Someplace", "farm");

        assertEquals(s.farmToString(), "2");
        assertEquals(s.troopToString(), "2 MaxSlots: 3, Remaining: 3");
        assertEquals(s.roadToString(), "3");
        assertEquals(s.wallToString(), "ballista");


    }

    @Test
    public void testRoad() {
        GameState game = new GameState();

        Faction faction = new Faction("Roman");
        game.addFaction(faction);
        faction.setGameState(game);
        Province province = new Province("Someplace");
        faction.addProvince(province);

        // Check if road exists
        Structure s = province.getStructures();
        assertEquals(s.getRoad(), null);

        // Build the road
        faction.buildInfrastructure("Someplace", "road");

        // Check if it updtes the queue
        assertEquals(province.getQueue().size(), 1);

        // Check movement points for the province
        assertEquals(province.getMovementPoints(), 4);


        game.iterateTurns();
        game.iterateTurns();

        // Check if the road has left the queue
        assertEquals(province.getQueue().size(), 0);

        // Get if movement points have been reduced.
        assertEquals(province.getMovementPoints(), 3);
        faction.upgradeInfrastructure("Someplace", "road");

        assertEquals(province.getMovementPoints(), 2);
        faction.upgradeInfrastructure("Someplace", "road");

        assertEquals(province.getMovementPoints(), 1);
        faction.upgradeInfrastructure("Someplace", "road");

        // Attempt to upgrade again but it doesn't change anything as it has reached max chain.
        assertEquals(province.getMovementPoints(), 1);
        faction.upgradeInfrastructure("Someplace", "road");

        // Test Coverage
        Road r = s.getRoad();
        r.setMovementPoints(1);

        // Test Coverage again
        r.setArmour(r.getArmour());
        r.setMaxChain(r.getMaxChain());
        assertEquals(r.getStructure(), s);
        province.setRoad(new Road());
    }

    @Test
    public void testFarm() {

        GameState game = new GameState();

        Faction faction = new Faction("Roman");
        game.addFaction(faction);
        faction.setGameState(game);

        Province province = new Province("Someplace");
        faction.addProvince(province);
        assertEquals(faction.getProvinces().size(), 1);

        faction.buildInfrastructure("Someplace", "farm");

        Structure s = province.getStructures();

        game.iterateTurns();
        game.iterateTurns();
        assertEquals(s.getFarmChain(), 1);
        faction.setGold(-2000);
        faction.upgradeInfrastructure("Someplace", "Farm");
        assertEquals(s.getFarmChain(), 1);
    }

    
    @Test
    public void testTroopProduction() {
    
    // Skips inserting initial_province_ownership.json GameState game = new
    
    GameState game = new GameState();
    
    Faction faction = new Faction("Roman"); 
    game.addFaction(faction);
    faction.setGameState(game);
    
    Province province = new Province("Someplace"); 
    faction.addProvince(province);

    assertEquals(faction.getProvinces().size(), 1);

    // Attempt to train unit when the troop production does not exist
    faction.trainUnit("Someplace", "archerman");
    Structure s = province.getStructures();
    assertEquals(s.getProductionchain(), 0);
    faction.upgradeInfrastructure("Someplace", "troopProduction");

    
    // Skip build time
    TroopProduction troopProduction = new TroopProduction(); 
    province.setTroopProduction(troopProduction);

    // Build a simple archerman
    faction.trainUnit("Someplace", "archerman");
    assertEquals(troopProduction.getQueue().size(), 1);
    assertEquals(troopProduction.toString(), "1 MaxSlots: 1, Remaining: 0");

    // Train another unit when the queue is full
    faction.trainUnit("Someplace", "archerman");
    assertEquals(troopProduction.getQueue().size(), 1);

    // Iterate and see if units exists within the province
    game.iterateTurns();
    assertEquals(province.getUnits().size(), 1);

    // Check if it has left the queue
    assertEquals(troopProduction.getQueue().size(), 0);

    // Attempt to build a higher unit
    faction.trainUnit("Someplace", "crossbowman");
    assertEquals(troopProduction.getQueue().size(), 0);

    // Upgrade troopProduction 
    faction.upgradeInfrastructure("Someplace", "troopProduction");
    assertEquals(province.getStructures().getProductionchain(), 2);

    // And attempt to build the same higher unit again
    faction.trainUnit("Someplace", "crossbowman");
    assertEquals(troopProduction.getQueue().size(), 1);
    assertEquals(troopProduction.queueToString(), "crossbowman finishes training at turn: 3.0\n");
    game.iterateTurns();

    // Test Coverage
    troopProduction.setNSlots(troopProduction.getNSlots());

    // Upgrade troopProduction
    faction.upgradeInfrastructure("Someplace", "troopProduction");
    assertEquals(province.getStructures().getProductionchain(), 3);

    assertEquals(troopProduction.getMaxSlots(), 1);
    // Set a farm (skip building time)
    Farm farm = new Farm();
    province.setFarm(farm);

    // Upgrade farm twice
    faction.upgradeInfrastructure("Someplace", "farm");
    faction.upgradeInfrastructure("Someplace", "farm");

    // Check if slots changes from 1 to 4.
    assertEquals(troopProduction.getMaxSlots(), 4);

    // Build horse_archer
    faction.trainUnit("Someplace", "horse_archer");
    assertEquals(troopProduction.getQueue().size(), 1);

    // Iterate game
    game.iterateTurns();

    // Check if horse_archer is still in queue
    assertEquals(troopProduction.getQueue().size(), 1);
    game.iterateTurns();
    game.iterateTurns();

    // Get units within province
    assertEquals(province.toStringUnits(), "crossbowman: 1.0\nhorse_archer: 1.0\narcherman: 1.0\n");

    // Train a unit in some unknown province
    faction.trainUnit("geawhe", "archerman");
    
    // Attempt to train a unit that doesn't exist
    faction.trainUnit("Someplace", "eghwah");
    assertEquals(troopProduction.getQueue().size(), 0);

    // Attempt to train when you have no gold.
    faction.setGold(-10000);
    faction.trainUnit("Someplace", "archerman");
    assertEquals(troopProduction.getQueue().size(), 0);

    // Attempt to train troop when it province doesn't exist
    faction.trainUnit("eahwe", "archerman");

    // Test Coverage
    faction.setFaction(faction.getFaction());
    }

    @Test
    public void testAttack() {
        GameState game = new GameState();

        Faction faction = new Faction("Roman");
        game.addFaction(faction);
        faction.setGameState(game);

        Faction enemy = new Faction("Dog");
        game.addFaction(enemy);
        enemy.setGameState(game);

        Province province = new Province("Someplace");
        faction.addProvince(province);
        province.addUnit(new Archerman(false));

        Province province2 = new Province("AnotherPlace");
        enemy.addProvince(province2);

        province.addNeighbour(province2);
        faction.fightProvince("a", "b");
        faction.fightProvince("Someplace", "Place");
        faction.fightProvince("Someplace", "AnotherPlace");
        assertEquals(faction.getProvinces().size(), 2);
        assertEquals(faction.toString(), "Province: Someplace\nProvince: AnotherPlace\n");
        assertEquals(enemy.getProvinces().size(), 0);
        assertEquals(enemy.toString(), "");
    }

    @Test
    public void testDefend() {
        GameState game = new GameState();

        Faction faction = new Faction("Roman");
        game.addFaction(faction);
        faction.setGameState(game);

        Province province = new Province("Someplace");
        faction.addProvince(province);

        Faction enemy = new Faction("Dog");
        game.addFaction(enemy);
        enemy.setGameState(game);

        Province province2 = new Province("AnotherPlace");
        enemy.addProvince(province2);
        province2.addUnit(new Archerman(false));

        province.addNeighbour(province2);
        province2.addNeighbour(province);
        enemy.fightProvince("AnotherPlace", "Someplace");
        assertEquals(faction.getProvinces().size(), 0);
        assertEquals(enemy.getProvinces().size(), 2);
    }


    @Test
    public void testMovingTroop() {
        GameState game = new GameState();

        // Create two factions, rome and gaul
        Faction rome = new Faction("rome");
        Faction gaul = new Faction("gaul");

        // Create 4 provinces, a b c d
        Province a = new Province("Lugdunensis");
        Province b = new Province("Lusitania");
        Province c = new Province("Macedonia");
        Province d = new Province("Mauretania Caesariensis");

        // Add everything inside gamestate
        game.addFaction(rome);
        game.addFaction(gaul);

        game.addProvince(a);
        game.addProvince(b);
        game.addProvince(c);
        game.addProvince(d);

        // Allocate a, b and c provinces to the first faction
        rome.addProvince(a);
        rome.addProvince(b);
        rome.addProvince(c);

        // Allocate d to the second faction
        gaul.addProvince(d);

        // Connect a<->b, b<->c, b<->d.
        a.addNeighbour(b);
        b.addNeighbour(a);

        b.addNeighbour(c);
        c.addNeighbour(b);

        b.addNeighbour(d);
        d.addNeighbour(b);

        // In b train a archerman, and a swordman
        Archerman archerman1 = new Archerman(false);
        Swordman swordman = new Swordman(false);
        b.addUnit(archerman1);
        b.addUnit(swordman);
        assertEquals(b.toStringUnits(), "swordman: 1.0\narcherman: 1.0\n");

        // In a train a archerman
        Archerman archerman2 = new Archerman(false);
        a.addUnit(archerman2);
        assertEquals(a.toStringUnits(), "archerman: 1.0\n");

        // Move archerman and swordman from b to a.
        rome.moveTroop("Lusitania", "Lugdunensis", "archerman, swordman");

        // Check the count on archerman in province a.
        assertEquals(a.toStringUnits(), "swordman: 1.0\narcherman: 2.0\n");

        // let gaul take b.
        gaul.addProvince(b);
        rome.removeProvince(b);

        // attempt to move archerman and swordman from a to c.
        rome.moveTroop("Lugdunensis", "Macedonia", "archerman, swordman");

        // Check if they have moved.
        assertEquals(a.toStringUnits(), "swordman: 1.0\narcherman: 2.0\n");
        assertEquals(c.toStringUnits(), "");

        // let rome take back b.
        rome.addProvince(b);
        gaul.removeProvince(b);

        // Check Movement Points
        Unit sword = a.getUnit("swordman");
        assertEquals(sword.getCurrentMovement(), 6);
        game.iterateTurns();
        assertEquals(sword.getCurrentMovement(), 10);

        // move archerman and swordmanfrom a to c
        rome.moveTroop("Lugdunensis", "Macedonia", "archerman, swordman");
        assertEquals(c.toStringUnits(), "swordman: 1.0\narcherman: 2.0\n");
        assertEquals(a.toStringUnits(), "");

        // Try to move again when they have low movement points left
        rome.moveTroop("Macedonia", "Lugdunensis", "archerman, swordman");
        assertEquals(c.toStringUnits(), "swordman: 1.0\narcherman: 2.0\n");
        assertEquals(a.toStringUnits(), "");

        // Move unit to random src and dest
        rome.moveTroop("Macedonia", "ghewa", "archerman");
        rome.moveTroop("heaw", "ghewa", "archerman");

        // Move unit that doesnt exist
        rome.moveTroop("Macedonia", "Lugdunensis", "gewah");

        assertEquals(rome.toString(), "Province: Lugdunensis\nProvince: Macedonia\nProvince: Lusitania\n");

        game.getFactions();
        game.getProvinces();
        assertEquals(game.getProvince("Macedonia").getProvince(), "Macedonia");
    }


    @Test 
    public void engagetypeTest() throws Exception { 
        Troop archerman = new Archerman(false); 
        Troop slingerman = new Slingerman(false); 
        Troop swordman = new Swordman(false); 
        Troop druid = new Druid(false); 
        Unit archer_unit = new Unit(50,archerman); 
        Unit slinger_unit = new Unit(50,slingerman); 
        Unit sword_unit = new Unit(50,swordman); 
        Unit druid_unit = new Unit(50,druid);
        Unit tower_unit = new Unit(1,new Archertower());
        Skirmish tower_skirmish = new Skirmish(sword_unit, tower_unit, true);
        Skirmish MR = new Skirmish(archer_unit , slinger_unit,false); 
        Skirmish melee = new Skirmish(slinger_unit , sword_unit , false); 
        Skirmish range = new Skirmish(archer_unit, druid_unit, false); 
        assertEquals("MR", tower_skirmish.engagetype());
        assertEquals("MvMM", melee.engagetype()); 
        assertEquals("RvRR", range.engagetype());
        assertEquals("MR", MR.engagetype());
    }

    @Test
    public void enagegTest(){
        Troop archerman = new Archerman(false); 
        Troop slingerman = new Slingerman(false); 
        Troop swordman = new Swordman(false); 
        Troop druid = new Druid(false); 
        Troop horse = new Horse_alone(false);
        Unit archer_unit = new Unit(50,archerman); 
        Unit slinger_unit = new Unit(50,slingerman); 
        Unit sword_unit = new Unit(50,swordman); 
        Unit druid_unit = new Unit(50,druid);
        Unit tower_unit = new Unit(1,new Archertower());
        Unit horse_unit = new Unit(50,horse);
        Skirmish tower_skirmish = new Skirmish(sword_unit, tower_unit, true);
        Skirmish MR = new Skirmish(archer_unit , slinger_unit,false); 
        Skirmish melee = new Skirmish(slinger_unit , sword_unit , false); 
        Skirmish range = new Skirmish(archer_unit, druid_unit, false); 
        Skirmish mixed = new Skirmish(archer_unit,sword_unit,false);
        Skirmish mixed_1 = new Skirmish(druid_unit,sword_unit,false);
        Skirmish mixed_2 = new Skirmish(druid_unit,slinger_unit,false);
        Skirmish mixed_3 = new Skirmish(druid_unit,horse_unit,false);
        Skirmish mixed_4 = new Skirmish(archer_unit,horse_unit,false);
        tower_skirmish.start();
        MR.start();
        melee.start();
        range.start();
        mixed.start();
        mixed_1.start();
        mixed_2.start();
        mixed_3.start();
        mixed_4.start();

    }

    @Test
    public void phalanxTest(){
        Faction atk_f = new Faction("atk");
        Faction def_f = new Faction("def");
        Province atk = new Province("atk");
        Province def = new Province("def");
        atk.setFaction(atk_f);
        def.setFaction(def_f);
        def_f.addProvince(def);
        atk_f.addProvince(atk);
        Archerman a = new Archerman(false);
        Hoplite h = new Hoplite(false);
        Pikeman p = new Pikeman(false);
        for(int i =0; i<20;i++){
            atk.addUnit(a);
            atk.addUnit(h);
            atk.addUnit(p);
        }
        def.addUnit(a);
        Battle btl = new Battle(atk,def);
        btl.start();
        
    }


    @Test
    public void druidbuffTest() {
        Troop druid = new Druid(false);
        double before = druid.getMorale();
        druid.buff_druidic_fevour(1);
        assertEquals(before * 1.1, druid.getMorale());
        druid.setMorale(before);
        druid.buff_druidic_fevour(3);
        assertEquals(before * 1.3, druid.getMorale());
    }

    @Test
    public void druiddebuffTest() {
        Troop druid = new Druid(false);
        double before = druid.getMorale();
        druid.debuff_druidic_fevour(1);
        assertEquals(before * 0.95, druid.getMorale());
        druid.setMorale(before);
        druid.debuff_druidic_fevour(3);
        assertEquals(before * 0.85, druid.getMorale());
    }

    /**
     * Test functions/methods in battle class
     */
    @Test
    public void BattleTest() {
        // setup factions and province for battles
        Faction atk_f = new Faction("atk");
        Faction def_f = new Faction("def");
        Province atk = new Province("atk");
        Province def = new Province("def");
        atk.setFaction(atk_f);
        def.setFaction(def_f);
        def_f.addProvince(def);
        atk_f.addProvince(atk);
        // case if invader wins
        Slingerman troop = new Slingerman(false);
        for (Double i = 0.0; i < 20; i++) {
            atk.addUnit(troop);
        }
        Battle btl = new Battle(atk, def);
        btl.Phalanx(atk.getUnit("slingerman"));
        assertEquals(1.2, troop.getArmour() );
        assertEquals(20, troop.getSpeed());
        String result = btl.start();
        assertEquals("attack", result);
        assertEquals("atk", def.getFaction().getFaction());
        assertEquals("atk", atk.getFaction().getFaction());        
        assertEquals(10.0, def.getUnits().get("slingerman").getNumTroops());
        assertEquals(false, atk.haveWall());
        atk.setWall(new Wall());
        assertEquals(true, atk.haveWall());

        // case if defender wins
        Province new_atk = new Province("new atk");
        Province new_def = new Province("new def");
        new_atk.setFaction(atk_f);
        new_def.setFaction(def_f);
        atk_f.addProvince(new_atk);
        def_f.addProvince(new_def);
        Archerman archers = new Archerman(false);
        for (Double i = 0.0; i < 20; i++) {
            new_def.addUnit(archers);
        }
        Battle new_btl = new Battle(new_atk, new_def);
        result = new_btl.start();
        assertEquals("defend", result);
        assertEquals("atk", new_atk.getFaction().getFaction());
        assertEquals("def", new_def.getFaction().getFaction());      
        
        Druid druid= new Druid(false);
        for (Double i = 0.0; i < 20; i++) {
            new_def.addUnit(druid);
            new_def.addUnit(archers);
        }


        Battle btl_1 = new Battle(new_atk, new_def);
        btl_1.start();


    }

    @Test
    public void testTowerEngage(){
        Archerman archers = new Archerman(false);
        Province atk = new Province("atk");
        Faction def_f = new Faction("def_f");
        Faction atk_f = new Faction("atk_f");
        // test if tower were placed into the army for defending province
        atk.setFaction(atk_f);
        Province province = new Province("towers");
        province.setFaction(def_f);
        // ballistatower
        Ballista b = new Ballista();
        province.setWall(b);

        // Test Coverage
        assertEquals(b.getBallistaAttDmg(), 100.0);
        assertEquals(b.getBallistaRange(), true);
        b.setBallistaAttDmg(b.getBallistaAttDmg());
        b.setBallistaRange(b.getBallistaRange());


        for (Double i = 0.0; i < 20; i++) {
            atk.addUnit(archers);
        }
        Battle btl_with_ballista = new Battle(atk,province);
        String result = btl_with_ballista.start();
        assertEquals("attack", result);
        // archertower
        ArcherTower a = new ArcherTower();
        province.setWall(a);

        // Test Coverage
        assertEquals(a.getArcherTowerAttDmg(), 80.0);
        assertEquals(a.getArcherTowerRange(), true);
        a.setArcherTowerAttDmg(a.getArcherTowerAttDmg());
        a.setArcherTowerRange(a.getArcherTowerRange());

        for (Double i = 0.0; i < 20; i++) {
            atk.addUnit(archers);
        }
        Battle btl_with_archertower = new Battle(atk,province);
        result = btl_with_archertower.start();
        assertEquals("attack", result);
    }


    /**
     * Test functions/methods for troop types
     */
    @Test
    public void TroopMethodTest(){
        Archerman archerman =  new Archerman(false);
        Archerman archerman_clone = archerman.clone();
        assertEquals(archerman.getType(), archerman_clone.getType());
        assertEquals(true, archerman.isRanged());

        Archerman_roman roman_a = new Archerman_roman(false);
        Archerman_roman roman_a_clone = roman_a.clone();
        assertEquals(roman_a.getType(), roman_a_clone.getType());
        assertEquals(true, roman_a.isRanged());

        Archerman_egyptian egy_acher = new Archerman_egyptian(false);
        Archerman_egyptian egy_acher_clone = egy_acher.clone();
        assertEquals(egy_acher.getType(), egy_acher_clone.getType());
        assertEquals(true, egy_acher.isRanged());

        Camel_alone c_alone = new Camel_alone(false);
        Camel_alone c_alone_clone = c_alone.clone();
        assertEquals(c_alone.getType(),c_alone_clone.getType());
        assertEquals(false, c_alone.isRanged());

        Camel_archer c_archer = new Camel_archer(false);
        Camel_archer c_archer_clone = c_archer.clone();
        assertEquals(c_archer.getType(),c_archer_clone.getType());      
        assertEquals(true, c_archer.isRanged());
        
        Camel c = new Camel(false);
        Camel c_clone = c.clone();
        assertEquals(c.getType(), c_clone.getType());
        assertEquals(false, c.isRanged());

        Cannon can = new Cannon();
        Cannon can_clone = can.clone();
        assertEquals(can.getType(), can_clone.getType());
        assertEquals(true, can.isRanged());

        Chariot chariot = new Chariot(false);
        Chariot chariot_mer = new Chariot(true);
        Chariot chariot_clone = chariot.clone();
        assertEquals(chariot.getType(),chariot_clone.getType());
        assertEquals(false, chariot.isRanged());

        Crossbowman crs = new Crossbowman(false);
        Crossbowman crs_clone = crs.clone();
        assertEquals(crs.getType(), crs_clone.getType());
        assertEquals(true, crs.isRanged());

        Druid druid = new Druid(false);
        Druid druid_clone = druid.clone();
        assertEquals(druid.getType(), druid_clone.getType());
        assertEquals(true, druid.isRanged());

        Elephant_alone ele_a =  new Elephant_alone(false);
        Elephant_alone ela_a_mer = new Elephant_alone(true);
        Elephant_alone ele_a_clone = ele_a.clone();
        assertEquals(ele_a.getType(), ele_a_clone.getType());
        assertEquals(false, ele_a.isRanged());

        Elephant_archer ele_archer = new Elephant_archer(false);
        Elephant_archer ele_archer_clone = ele_archer.clone();
        assertEquals(ele_archer.getType(),ele_archer_clone.getType());
        assertEquals(true, ele_archer.isRanged());

        Elephant ele =  new Elephant(false);
        Elephant ele_clone = ele.clone();
        assertEquals(ele.getType(), ele_clone.getType());
        assertEquals(false, ele.isRanged());

        Flagbearer_egyptian flag_egy = new Flagbearer_egyptian(false);
        Flagbearer_egyptian flag_egy_clone = flag_egy.clone();
        assertEquals(flag_egy.getType(), flag_egy_clone.getType());
        assertEquals(false, flag_egy.isRanged());

        Flagbearer_roman flag_rom = new Flagbearer_roman(false);
        Flagbearer_roman flag_rom_clone = flag_rom.clone();
        assertEquals(flag_rom.getType() , flag_rom_clone.getType());
        assertEquals(false, flag_rom.isRanged());

        Flagbearer flag = new Flagbearer(false);
        Flagbearer flag_clone = flag.clone();
        assertEquals(flag.getType(), flag_clone.getType());
        assertEquals(false, flag.isRanged());

        Hoplite hop = new Hoplite(false);
        Hoplite hop_clone = hop.clone();
        assertEquals(hop.getType(), hop_clone.getType());
        assertEquals(false, hop.isRanged());

        Horse hor = new Horse(false);
        Horse hor_clone = hor.clone();
        assertEquals(hor.getType(), hor_clone.getType());
        assertEquals(false, hor.isRanged());

        Horse_alone hor_alone = new Horse_alone(false);
        Horse_alone hor_alone_mer = new Horse_alone(true);
        Horse_alone hor_alone_clone = hor_alone.clone();
        assertEquals(hor_alone.getType(), hor_alone_clone.getType());
        assertEquals(false, hor_alone.isRanged());

        Horse_heavy_cavalry hor_cav = new Horse_heavy_cavalry(false);
        Horse_heavy_cavalry hor_cav_mer = new Horse_heavy_cavalry(true);
        Horse_heavy_cavalry hor_cav_clone = hor_cav.clone();
        assertEquals(hor_cav.getType(), hor_cav_clone.getType());
        assertEquals(false, hor_cav.isRanged());

        Horse_lancer hor_lan = new Horse_lancer(false);
        Horse_lancer hor_lan_1 = new Horse_lancer(true);        
        Horse_lancer hor_lan_clone = hor_lan.clone();
        assertEquals(hor_lan.getType(), hor_lan_clone.getType());
        assertEquals(false, hor_lan.isRanged());
        
        Horse_archer hor_arc = new Horse_archer(false);
        Horse_archer hor_arc_mer = new Horse_archer(true);
        Horse_archer hor_arc_clone = hor_arc.clone();
        assertEquals(hor_arc.getType(), hor_arc_clone.getType());
        assertEquals(true, hor_arc.isRanged());

        Netfighter net = new Netfighter(false);
        Netfighter net_clone = net.clone();
        assertEquals(net.getType(), net_clone.getType());
        assertEquals(true, net.isRanged());

        Pikeman pike = new Pikeman(false);
        Pikeman pike_clone = pike.clone();
        assertEquals(pike.getType(), pike_clone.getType());
        assertEquals(false, pike.isRanged());

        Slingerman sling = new Slingerman(false);
        Slingerman sling_clone = sling.clone();
        assertEquals(sling.getType(), sling_clone.getType());
        assertEquals(false, sling.isRanged());

        Spearman spear = new Spearman(false);
        Spearman spear_clone = spear.clone();
        assertEquals(spear.getType(), spear_clone.getType());
        assertEquals(false, spear.isRanged());

        Swordman sword = new Swordman(false);
        Swordman sword_mer = new Swordman(true);
        Swordman sword_clone = sword.clone();
        assertEquals(sword.getType(), sword_clone.getType());
        assertEquals(false, sword.isRanged());

        Trebuchet tre = new Trebuchet();
        Trebuchet tre_clone = tre.clone();
        assertEquals(tre.getType(), tre_clone.getType());  
        assertEquals(true, tre.isRanged());

        Troop trp = (Troop) archerman;
        trp.setCost(trp.getCost());
        trp.setTraining(trp.getTraining());
        trp.setMovement(trp.getMovement());
        trp.setChain(trp.getChain());
        trp.setSlots(trp.getSlots());
        Troop trp_clone = trp.clone();
        assertEquals(trp.getType(), trp_clone.getType());  

        Cavalry cav = (Cavalry) hor_lan;
        cav.setMercenary(cav.isMercenary());
    
        Infantry inf = (Infantry) sword;
        inf.setMercenary(inf.isMercenary());

    }

    /**
     * Test methods/functions for unit class
     */
    @Test
    public void unitsTest(){
        // setup unit for testing
        Archerman a = new Archerman(false);
        Unit unit = new Unit(50, a);
        assertEquals("Unit [numTroops=50.0, troop=archerman]", unit.toString());
        unit.updateNum(20);
        assertEquals((50-20), unit.getNumTroops());
        unit.increaseTroopCount();
        assertEquals((50-20+1), unit.getNumTroops());
        unit.increaseTroopCount(9);
        assertEquals((50-20+1+9), unit.getNumTroops());
        unit.updateNum(90);
        assertEquals(0, unit.getNumTroops());
        assertEquals(10, unit.getCurrentMovement());
        unit.subtractMovement(5);
        assertEquals(10-5, unit.getCurrentMovement());
        unit.restoreMovement();
        assertEquals(10, unit.getCurrentMovement());
        assertEquals(a.getType(), unit.getTroop().getType());
        assertEquals(a.isRanged(), unit.isRanged());
    }


    @Test
    public void conditonsGeneratorTest(){
        WinCondition conds = new WinCondition();
        conds.generateConds();
        assertEquals(false, conds.getConditions().isEmpty());
    }

    @Test
    public void winConditionsTest(){
        GameState game = new GameState();
        WinCondition conds = new WinCondition();
        Faction player = new Faction("Player");
        Province p1 = new Province("1");
        Province p2 = new Province("2");
        game.addProvince(p1);
        game.addProvince(p2);
        game.addFaction(player);
        player.setGameState(game);

        ArrayList<String> conquest = new ArrayList<String>();
        ArrayList<String> wealth = new ArrayList<String>();
        ArrayList<String> treasure = new ArrayList<String>();
        conquest.add("conquest");
        wealth.add("wealth");
        treasure.add("treasure");

        game.setCond(conds);

        // test conquest goal
        conds.addConds(conquest);
        assertEquals(false, player.reachedConditions());
        player.addProvince(p1);
        assertEquals(false, player.reachedConditions());
        player.addProvince(p2);
        assertEquals(true, player.reachedConditions());
        assertEquals("conquest\n", conds.toString());

        // test wealth goal
        conds.removeConds(conquest);
        conds.addConds(wealth);
        assertEquals(false, player.reachedConditions());
        player.setWealth(999999);
        assertEquals(true, player.reachedConditions());
        assertEquals("wealth\n", conds.toString());

        // test treasure goal
        conds.removeConds(wealth);
        conds.addConds(treasure);
        assertEquals(false, player.reachedConditions());
        player.setGold(999999);
        assertEquals(true, player.reachedConditions());
        assertEquals("treasure\n", conds.toString());

        // test multiple goals : Wealth OR  Treasure case
        conds.addConds(wealth);
        // reset wealth/gold stats for player
        player.setWealth(0);    
        player.setGold(-9999999);
        assertEquals(0, player.getWealth());
        assertEquals(0, player.getGold());
        // player not yet completes the game
        assertEquals(false, player.reachedConditions());
        player.setWealth(999999);
        assertEquals(true, player.reachedConditions());
        player.setWealth(0);
        assertEquals(false, player.reachedConditions());
        player.setWealth(999999);
        assertEquals(true, player.reachedConditions());
        assertEquals("treasure\nwealth\n", conds.toString());

        // test mutiple goals : Wealth And Conquest case
        player.setWealth(0);
        player.removeProvince(p1);
        player.removeProvince(p2);
        conds.removeConds(wealth);
        conds.removeConds(treasure);
        ArrayList<String> multi = new ArrayList<String>();
        multi.add("conquest");
        multi.add("wealth");
        conds.addConds(multi);
        // case player havnt complete both
        assertEquals(false, player.reachedConditions());
        // case player complete ONLY wealth
        player.setWealth(99999999);
        assertEquals(false, player.reachedConditions());
        // case player complete ONLY conquest
        player.setWealth(0);
        player.addProvince(p1);
        player.addProvince(p2);
        assertEquals(false, player.reachedConditions());
        // case player complete BOTH
        player.setWealth(9999999);
        assertEquals(true, player.reachedConditions());
        assertEquals("conquest AND wealth\n", conds.toString());

        //test multiple goals Conquest OR (Wealth AND Treasure)
        multi.remove("conquest");
        multi.add("treasure");
        conds.addConds(conquest);
        // restore wealth treaure and province to default
        player.setWealth(0);
        player.setGold(-9999999);
        player.removeProvince(p1);
        player.removeProvince(p2);
        // case player ONLY completes wealth
        player.setWealth(99999999);
        assertEquals(false, player.reachedConditions());
        // case player ONLY completes teasure
        player.setWealth(0);
        player.setGold(99999999);
        assertEquals(false, player.reachedConditions());
        // case player completes BOTH treasure and wealth
        player.setWealth(9999999);
        assertEquals(true, player.reachedConditions());
        // case player completes ALL treasure wealth conquest
        player.addProvince(p1);
        player.addProvince(p2);
        assertEquals(true, player.reachedConditions());
        // case player completes conquest and wealth
        player.setGold(-999999999);
        assertEquals(true, player.reachedConditions());
        // case player completes ONLY conquest
        player.setWealth(0);
        assertEquals(true, player.reachedConditions());
        assertEquals("wealth AND treasure\nconquest\n", conds.toString());

        for(int i =0; i < 30; i ++){game.buildCondition();}
    }
}

