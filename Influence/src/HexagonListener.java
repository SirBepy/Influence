/**
Author: Josip Mužić
Description: The main method that plays the game
*/
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class HexagonListener extends MouseAdapter {
    //global variables
    private static Hexagon previousHexagon = null;
    Hexagon hexagon;
    /**
    @param hexagon
    Hexagon ID
    */
    public HexagonListener(Hexagon hexagon) {
        this.hexagon = hexagon;
    }
    /**
    @param e
    Checks what mouse action had happened
    */
    //We made a class that checks if the hexagon panel has been clicked
    //After that we overwrote the system.out.println function to instead send feedback to the MapsAndMovement class
    public void mousePressed(MouseEvent e) {
        if (hexagon.checkIfItsHisTurn() && (hexagon.getLives() > 1 && MapsAndMovement.ADDER == 0)) {
            System.out.println(hexagon.getName() + hexagon.sendLives());
            hexagon.active();
            previousHexagon = hexagon;
            //attack command
        } else if (hexagon.checkIfSpreadPossible() && hexagon.getPlayerNumber() > 0) {
            System.out.println(hexagon.getName() + "attack");
            previousHexagon.sendLivesComplete();
            previousHexagon = null;
            //movement command
        } else if (hexagon.checkIfSpreadPossible()) {
            System.out.println(hexagon.getName() + "spread");
            previousHexagon.sendLivesComplete();
            previousHexagon = null;
        } else if (hexagon.checkIfItsHisTurn() && MapsAndMovement.ADDER > 0) {
            System.out.println(hexagon.getName());
            hexagon.normal();
            //reset command
        } else {
            System.out.println(hexagon.getName() + "reset");
        }
    }
}
