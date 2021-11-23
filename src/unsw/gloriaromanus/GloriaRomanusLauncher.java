package unsw.gloriaromanus;


public class GloriaRomanusLauncher {

    public static void main(String[] args) {
        //GloriaRomanusApplication.main(args);
        
        // Load Game
        GameState game = new GameState();
        try {
            game.readOwnership("src/unsw/gloriaromanus/initial_province_ownership.json");
            game.readConnected("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json");
            } catch (Exception e) {
                System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println(game.getCond().toString());
        // Play the game.
        while (game.getTurns() == 1) {
            for (Faction f : game.getFactions()) {
                System.out.println(game.getFactions().size());
                System.out.println(f.getFaction() + "'s turn: Type in Command");
                System.out.println(f.toString());
                break;
            }
            game.iterateTurns();
        }
        
    
    }
    

}