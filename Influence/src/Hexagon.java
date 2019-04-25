/**
Author: Josip Mužić
Description: The main method that plays the game
*/
import java.awt.*;
import javax.swing.JPanel;

public class Hexagon extends JPanel {
    //global variables
    String name;
    public static int TURN = 1;
    public static boolean SENT_COMPLETE = true;
    int color, lives, playerNumber;
    int[] playerList = {0x8760FF, 0xFF614B, 0x556BFF};
    boolean notEmpty, possibleSpread;
    /**
    @param name
    Hexagon name
    @param _notEmpty
    Makes sure the hexagon isnt empty
    @param _playerNumber
    The turn players number
    */
    public Hexagon(String name, boolean _notEmpty, int _playerNumber) {
        this(name, true);
        playerNumber = _playerNumber;
        lives = 0;
        if (playerNumber > 0) {
            lives = 3;
        }
    }
    /**
    @param name
    Hexagon name
    @param _notEmpty
    Checks if the hexagon is empty
    */
    public Hexagon(String name, boolean _notEmpty) {
        notEmpty = _notEmpty;
        this.name = name;
        setPreferredSize(new Dimension(80, 80));
        color = 0x8760FF;
    }

    @Override
    /**
    @param g
    The graphics variable
    */
    public void paintComponent(Graphics g) {
        if (notEmpty) {
            drawHex(g, 45, 40, 40);
        }
    }
    /**
    @param g
    The graphics variable
    @param x
    The x-axis index
    @param y
    The y-axis index
    @param r
    Hexagon radius
    */
    private void drawHex(Graphics g, int x, int y, int r) {
        Graphics2D g2d = (Graphics2D) g;

        HexagonPolygon hex = new HexagonPolygon(x, y, r);

        if (playerNumber == 1) {
            hex.draw(g2d, x, y, 0, playerList[1], true);
        } else if (playerNumber == 2) {
            hex.draw(g2d, x, y, 0, playerList[2], true);
        } else {
            hex.draw(g2d, x, y, 0, playerList[0], true);
        }
        hex.draw(g2d, x, y, 4, color, false); // change this for different color of border

        if (playerNumber != 0) {
            g.setColor(new Color(0xFFFFFF));
            g.drawString("" + lives, x - 10 / 2, y + 8 / 2);
        }
    }

    /**
    @return name
    Hexagon name
    */
    public String getName() {
        return name;
    }
    /**
    @return lives
    Number of lives on a tile
    */
    public int getLives() {
        return lives;
    }
    /**
    @return playerNumber
    Turn players id number
    */
    public int getPlayerNumber() {
        return playerNumber;
    }
    /**
    @return notEmpty
    Hexagon isnt empty
    */
    public boolean checkIfItsEmpty() {
        return notEmpty;
    }
    /**
    @return (playerNumber == TURN)
    Checks if it is that players turn
    */
    public boolean checkIfItsHisTurn() {
        return (playerNumber == TURN);
    }
    /**
    @return possibleSpread
    These are the tiles the player can spread to
    */
    public boolean checkIfSpreadPossible() {
        return possibleSpread;
    }
    //This is the active tile chosen
    public void active() {
        color = 0x4D5263;
        possibleSpread = false;
        repaint();
    }
    //These are the tiles the active tile can spread to
    public void possibleChoices() {
        color = 0x63100B;
        possibleSpread = true;
        repaint();
    }
    //These are the generic other tiles
    public void normal() {
        color = 0x8760FF;
        possibleSpread = false;
        repaint();
    }
    /**
    @param _lives
    Number of lives the active tile has
    */
    public void spread(int _lives) {
        playerNumber = TURN;
        lives = _lives;
        repaint();
    }
    /**
    @return sentLives
    Amount of lives transfers
    */
    public int sendLives() {
        int sentLives = lives - 1;
        SENT_COMPLETE = false;
        return sentLives;
    }
    
    public void sendLivesComplete() {
        lives = 1;
        SENT_COMPLETE = true;
    }
    //this adds the lives to a tile
    public void addLife() {
        if (lives < 8) {
            lives += 1;
            repaint();
        }
    }
    /**
    @param enemyLives
    Checks how many lives the enemy tile has
    */
    public void beingAttacked(int enemyLives) {
        if (enemyLives > 4) {
            int randomNumber = (int) (Math.random() * 100);
            if (randomNumber > 60 && randomNumber < 90) {
                enemyLives++;
            } else if (randomNumber >= 90) {
                enemyLives += 2;
            }
        } else {
            int randomNumber = (int) (Math.random() * 10);
            if (randomNumber > 6) {
                enemyLives++;
            }
        }
        if(lives > enemyLives) {
            lives -= enemyLives;
        } else {
            lives = (lives - enemyLives++) * -1;
            if (lives == 0) {
                lives++;
            }
            playerNumber = TURN;
        }
        repaint();
    }

}

class HexagonPolygon extends Polygon {

    private static final long serialVersionUID = 1L;

    public static final int SIDES = 6;

    private Point[] points = new Point[SIDES];
    private Point center = new Point(0, 0);
    private int radius;
    private int rotation = 90;

    /**
     * Main Constructor
     *
     * @param center - Point in the panel
     * @param radius - Integer describing the radius
     */
    public HexagonPolygon(Point center, int radius) {
        npoints = SIDES;
        xpoints = new int[SIDES];
        ypoints = new int[SIDES];

        this.center = center;
        this.radius = radius;

        // Calling updatePoints to draw everything
        updatePoints();
    }

    /**
     * Uses the previous constructor to create by creating a point first
     *
     * @param x - X-axis
     * @param y - Y-axis
     * @param radius - Radius of the hexagon
     */
    public HexagonPolygon(int x, int y, int radius) {
        this(new Point(x, y), radius);
    }

    /**
     * @return radius - Integer
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return rotation - Integer
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * @param radius - Integer describing radius
     */
    public void setRadius(int radius) {
        this.radius = radius;

        updatePoints();
    }

    /**
     * @param rotation - Integer describing how much the drawing method needs to
     * rotate for
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;

        updatePoints();
    }

    /**
     * @param center - Point in panel
     */
    public void setCenter(Point center) {
        this.center = center;

        updatePoints();
    }

    /**
     * Using previous method by first creating a Point object
     *
     * @param x - X-axis
     * @param y - Y-axis
     */
    public void setCenter(int x, int y) {
        setCenter(new Point(x, y));
    }

    /**
     * Making this method just to simplify the code
     *
     * @param fraction
     * @return Result for the angle
     */
    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((rotation + 180) % 360);
    }

    /**
     * Finding the next point of the hexagon to connect all of the points
     *
     * @param angle
     * @return Point
     */
    private Point findPoint(double angle) {
        int x = (int) (center.x + Math.cos(angle) * radius);
        int y = (int) (center.y + Math.sin(angle) * radius);

        return new Point(x, y);
    }

    /**
     * Simple method that draws everything
     */
    private void updatePoints() {
        for (int x = 0; x < SIDES; x++) {
            double angle = findAngle((double) x / SIDES);
            Point point = findPoint(angle);
            xpoints[x] = point.x;
            ypoints[x] = point.y;
            points[x] = point;
        }
    }

    public void draw(Graphics2D graphics, int x, int y, int lineThickness, int colorValue, boolean filled) {
        // Store before changing.
        Stroke tmpS = graphics.getStroke();
        Color tmpC = graphics.getColor();

        graphics.setColor(new Color(colorValue));
        graphics.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

        if (filled) {
            graphics.fillPolygon(xpoints, ypoints, npoints);
        }
        if (!filled) {
            graphics.drawPolygon(xpoints, ypoints, npoints);
        }

        // Set values to previous when done.
        graphics.setColor(tmpC);
        graphics.setStroke(tmpS);
    }
}
