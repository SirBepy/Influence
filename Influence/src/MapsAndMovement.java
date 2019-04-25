/**
Author: Josip Mužić
Description: The main method that plays the game
*/
//All of the imports
import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

public class MapsAndMovement extends JPanel {
   //global variables
   ArrayList<Hexagon> hexList;
   ArrayList<String> mapList = new ArrayList();
   private int whichMap, lives, pointsToSpend;
   public static int ADDER = 0;
   /**
   @param _whichMap
   Determines which map the unites will be acting apon
   */
   public MapsAndMovement(int _whichMap) {
      
      addMaps();
      pointsToSpend = 0;
      whichMap = _whichMap;
      add(drawGrid());
      setBackground(new Color(0x44307F));
      System.setOut(
                new PrintStream(System.out) {
                   public void println(String argument) {
                      //checks if you can add points, if not then checks for what action you  are making
                      if (ADDER == 0) {
                      //takes into command from hexagonListener
                         if (argument.contains("spread")) {
                            hexList.get(getIndex(argument)).spread(lives);
                            reset();
                         } else if (argument.contains("reset")) {
                            reset();
                         } else if (argument.contains("attack")) {
                            hexList.get(getIndex(argument)).beingAttacked(lives);
                            reset();
                         } else {
                            onClick(argument);
                            lives = Integer.parseInt("" + argument.charAt(5));
                         }
                         //adding the points to the tiles
                      } else if (ADDER > 0 && pointsToSpend > 0) {
                         Hexagon hexagon = hexList.get(getIndex(argument));
                         if (hexagon.getPlayerNumber() == Hexagon.TURN) {
                            hexagon.addLife();
                            pointsToSpend--;
                         }
                      } else if (ADDER > 0) {
                         System.out.print("User is trying to add more lives, but he cant.\n");
                      }
                      checkIfEnd();
                      revalidate();
                      repaint();
                   }
                });
   }
   //Creates the maps grid, this is what the maps are based off
   public JPanel drawGrid() {
      hexList = new ArrayList();
   
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0, 1));
   
      panel.add(rowOfPanels(6, 0));
      panel.add(rowOfPanels(7, 1));
      panel.add(rowOfPanels(6, 2));
      panel.add(rowOfPanels(7, 3));
      panel.add(rowOfPanels(6, 4));
      panel.add(rowOfPanels(7, 5));
      panel.add(rowOfPanels(6, 6));
      return panel;
   }
   /**
   @param numberOfHex
   Number of hexagons per row
   @param row
   Row number
   @return panel
   Returns the row panel
   */
   public JPanel rowOfPanels(int numberOfHex, int row) {
      JPanel panel = new JPanel();
      panel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
      panel.setBackground(new Color(0x44307F));
      row++;
      //checks the mapList array for certain charachters that mark which tiles are what
      for (int x = 1; x < (numberOfHex + 1); x++) {
         if (mapList.get(row - 1 + (whichMap * 7)).charAt(x - 1) == 'o') {
            Hexagon hexagon = new Hexagon("" + row + " - " + x, true, 0);
            HexagonListener selector = new HexagonListener(hexagon);
            hexagon.addMouseListener(selector);
            hexList.add(hexagon);
            panel.add(hexagon);
         } else if (mapList.get(row - 1 + (whichMap * 7)).charAt(x - 1) == 'x') {
            Hexagon hexagon = new Hexagon("" + row + " - " + x, false);
            hexList.add(hexagon);
         
            panel.add(hexagon);
         } else if (mapList.get(row - 1 + (whichMap * 7)).charAt(x - 1) == '1') {
            Hexagon hexagon = new Hexagon("" + row + " - " + x, true, 1);
            HexagonListener selector = new HexagonListener(hexagon);
            hexagon.addMouseListener(selector);
            hexList.add(hexagon);
            panel.add(hexagon);
         } else if (mapList.get(row - 1 + (whichMap * 7)).charAt(x - 1) == '2') {
            Hexagon hexagon = new Hexagon("" + row + " - " + x, true, 2);
            HexagonListener selector = new HexagonListener(hexagon);
            hexagon.addMouseListener(selector);
            hexList.add(hexagon);
            panel.add(hexagon);
         }
      }
      return panel;
   }
   /**
   @param name
   HExagon panel name
   */
   public void onClick(String name) {
      int row = Integer.parseInt("" + name.charAt(0));
      int col = Integer.parseInt("" + name.charAt(4));
   
      for (Hexagon hex : hexList) {
         int hexRow = Integer.parseInt("" + hex.getName().charAt(0));
         int hexCol = Integer.parseInt("" + hex.getName().charAt(4));
      
         int rowClose = row - hexRow;
         int colClose = col - hexCol;
         //Checks what the possible choice are for the player to spread to
         if (!hex.checkIfItsHisTurn()) {
            if ((row % 2 == 0)) {
               if ((hexRow % 2 == 0)) {
                  if (((rowClose >= -1) && (rowClose <= 1)) && ((colClose >= -1) && (colClose <= 1))) {
                     hex.possibleChoices();
                  } else {
                     hex.normal();
                  }
               } else {
                  if (((rowClose >= -1) && (rowClose <= 1)) && ((colClose >= 0) && (colClose <= 1))) {
                     hex.possibleChoices();
                  } else {
                     hex.normal();
                  }
               }
            } else {
               if ((hexRow % 2 == 0)) {
                  if (((rowClose >= -1) && (rowClose <= 1)) && ((colClose >= -1) && (colClose <= 0))) {
                     hex.possibleChoices();
                  } else {
                     hex.normal();
                  }
               } else {
                  if (((rowClose >= -1) && (rowClose <= 1)) && ((colClose >= -1) && (colClose <= 1))) {
                     hex.possibleChoices();
                  } else {
                     hex.normal();
                  }
               }
            }
         }
      }
   }
   //resets the map
   public void reset() {
      for (Hexagon hex : hexList) {
         if (hex.checkIfItsEmpty()) {
            hex.normal();
         }
      }
   }
   //This gets the hexagon id index
   /**
   @param argument
   Command input from HexagonListener class
   */
   public int getIndex(String argument) {
      int row = Integer.parseInt("" + argument.charAt(0));
      int col = Integer.parseInt("" + argument.charAt(4));
   
      int answer = 0;
      for (int newRow = 1; newRow < (row + 1); newRow++) {
         if ((newRow % 2) == 0) {
            answer += 7;
         } else {
            answer += 6;
         }
      }
   
      if ((row % 2) == 0) {
         answer += col - 7;
      } else {
         answer += col - 6;
      }
      System.out.print(answer + "\n");
      return --answer;
   }

   public void addMaps() {
      // Map 0
      mapList.add("oooooo ");
      mapList.add("ooo2ooo ");
      mapList.add("oooooo ");
      mapList.add("ooooooo ");
      mapList.add("oooooo ");
      mapList.add("ooo1ooo ");
      mapList.add("oooooo ");
   
      // Map 1
      mapList.add("ooooo2 ");
      mapList.add("oxxoxxo ");
      mapList.add("oxooxo ");
      mapList.add("oxoooxo ");
      mapList.add("oxooxo ");
      mapList.add("oxxoxxo ");
      mapList.add("1ooooo ");
   
      // Map 2
      mapList.add("2ooooo ");
      mapList.add("oxxoxxo ");
      mapList.add("oooooo ");
      mapList.add("oxoooxo ");
      mapList.add("oooooo ");
      mapList.add("oxxoxxo ");
      mapList.add("ooooo1 ");
   
      // Map 3
      mapList.add("xooooo ");
      mapList.add("xxxooo2 ");
      mapList.add("xxooxx ");
      mapList.add("xxoooxx ");
      mapList.add("xxooxx ");
      mapList.add("1oooxxx ");
      mapList.add("ooooox ");
   
      // Map 4
      mapList.add("ooooo2 ");
      mapList.add("ooxxxxx ");
      mapList.add("xooxxx ");
      mapList.add("xxooxxx ");
      mapList.add("xxxoox ");
      mapList.add("xxxxoox ");
      mapList.add("1ooooo ");
   }
   /**
   @param newMap
   The map you want to change to
   */
   public void changeMap(int newMap) {
      whichMap = newMap;
      Hexagon.TURN = 1;
      removeAll();
      revalidate();
      repaint();
      add(drawGrid());
   }
   /**
   @return pointsToSpend
   The amount of lives you can add to your tiles
   */
   public int getPointsToSpend() {
      int newPointsToSpend = 0;
      for (Hexagon hex : hexList) {
         if (hex.getPlayerNumber() == Hexagon.TURN) {
            newPointsToSpend++;
         }
      }
      pointsToSpend = newPointsToSpend;
      return pointsToSpend;
   }
   /**
   @return winner
   The winner of the game
   */
   public String getWinner() {
      String winner;
      int playerOne = 0;
      int playerTwo = 0;
      for (Hexagon hex : hexList) {
         if (hex.getPlayerNumber() == 1) {
            playerOne++;
         } else if (hex.getPlayerNumber() == 2) {
            playerTwo++;
         }
      }
      if (playerOne > playerTwo) {
         winner = "Red Player";
      } else if (playerOne < playerTwo) {
         winner = "Blue Player";
      } else {
         winner = "to no one";
      }
      return winner;
   }
   //This methos enacts the surrender method
   public void surrender() {
      JOptionPane.showMessageDialog(null, "Lol, what kind of loser surrenders. Congratulations " + getWinner() + ", on your victory!");
   }
   //this checks if the player can attack anymore, if not, it ends
   public void checkIfEnd() {
      int amountOfPlayerOne = 0;
      int amountOfPlayerTwo = 0;
   
      boolean endAttacking = false;
   
      for (Hexagon hex : hexList) {
         if (hex.getPlayerNumber() == 1) {
            amountOfPlayerOne++;
         } else if (hex.getPlayerNumber() == 2) {
            amountOfPlayerTwo++;
         }
      
         if (hex.getPlayerNumber() == hex.TURN && !endAttacking) {
            if (hex.getLives() != 1) {
               endAttacking = true;
            }
         }
      }
   
      revalidate();
      repaint();
   
      if (ADDER > 0 && pointsToSpend == 0) {
         endTurn();
      }
      if (!endAttacking) {
         endAttacking();
      } else if (amountOfPlayerOne == 0 || amountOfPlayerTwo == 0) {
         JOptionPane.showMessageDialog(null, getWinner() + " wins!");
         changeMap(whichMap);
      }
   
   }
   //ends the attack phase
   public void endAttacking() {
      JOptionPane.showMessageDialog(null, "You can now add " + getPointsToSpend() + " more troops.");
      ADDER = Hexagon.TURN;
   }
   //ends the enitre players turn
   public void endTurn() {
      if (Hexagon.TURN == 1) {
         Hexagon.TURN = 2;
         JOptionPane.showMessageDialog(null, "It is now Blue player's turn.\nBlue player please play your turn.");
      } else {
         Hexagon.TURN = 1;
         JOptionPane.showMessageDialog(null, "It is now Red player's turn.\nRed player please play your turn.");
      }
      ADDER = 0;
   }

}
