/**
Author: Josip Mužić
Description: The main method that plays the game
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EntireGui extends JFrame implements ActionListener {

    private MapsAndMovement maps;
    
    //Adding all of the maps

    private JMenuItem map1 = new JMenuItem("Map 1");
    private JMenuItem map2 = new JMenuItem("Map 2");
    private JMenuItem map3 = new JMenuItem("Map 3");
    private JMenuItem map4 = new JMenuItem("Map 4");
    private JMenuItem map5 = new JMenuItem("Map 5");
    private JMenuItem surrender = new JMenuItem("Surrender");
    private JMenuItem endOrAdd = new JMenuItem("End Attacking");
    //The GUI is split into three parts, the top menu, the bottom menu and the map itself
    public EntireGui() {
        setTitle("Influence");
        setLayout(new BorderLayout());
        //Main map
        maps = new MapsAndMovement(0);
        add(maps, BorderLayout.CENTER);
        setBackground(new Color(0x44307F));
        //Top menu bar
        JMenuBar topBar = new JMenuBar();
        JMenu file = new JMenu("Influence");
        JMenu newGame = new JMenu("New Game");
        topBar.add(file);
        file.add(newGame);
        file.add(surrender);
        newGame.add(map1);
        newGame.add(map2);
        newGame.add(map3);
        newGame.add(map4);
        newGame.add(map5);
        //Bottom menu bar
        JMenuBar bottomBar = new JMenuBar();
        bottomBar.add(endOrAdd);

        map1.addActionListener(this);
        map2.addActionListener(this);
        map3.addActionListener(this);
        map4.addActionListener(this);
        map5.addActionListener(this);
        surrender.addActionListener(this);
        endOrAdd.addActionListener(this);

        add(topBar, BorderLayout.NORTH);
        add(bottomBar, BorderLayout.SOUTH);

        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            //end turn command
        if (e.getActionCommand().equals("End Turn")) {
            maps.endTurn();
            endOrAdd.setText("End Attacking");
            //end of attack
        } else if (e.getActionCommand().equals("End Attacking")) {
            maps.endAttacking();
            endOrAdd.setText("End Turn");
            //choosing a map
        } else if (e.getActionCommand().contains("Map")) {
            int numberOfMap = Integer.parseInt("" + e.getActionCommand().charAt(4));
            maps.changeMap(numberOfMap - 1);
            //surrender option
        } else if (e.getActionCommand().contains("Surrender")) {
            maps.surrender();
        }
    }

}
