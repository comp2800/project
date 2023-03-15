import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Main implements ActionListener {

    private GameState gameState;
    private boolean running = true;
    private boolean restartLevel = false;

    Main() {
        JFrame f = new JFrame("Flight Simulator");
        f.setSize(1200, 800);

        gameState = new GameState();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar jmb = new JMenuBar();

        JMenu jmFile = new JMenu("File");
        JMenuItem jmiNewGame = new JMenuItem("New Game");
        JMenuItem jmiRestartLevel = new JMenuItem("Restart Level");
        JMenuItem jmiExit = new JMenuItem("Exit");
        jmFile.add(jmiNewGame);
        jmFile.add(jmiRestartLevel);
        jmFile.addSeparator();
        jmFile.add(jmiExit);
        jmb.add(jmFile);

        jmiNewGame.addActionListener((ActionEvent e) -> {
            f.dispose(); // dispose of the current frame
            new Main(); // create a new instance of Main
        });

        jmiRestartLevel.addActionListener((ActionEvent e) -> {
            System.out.println("Restarting level " + gameState.getCurrentLevel());
            restartLevel = true; // set flag to indicate level should be restarted

            while (running) {
                if (restartLevel) {
                    // reload the current level
                    gameState.resetLevel(); // reset game state for current level
                    System.out.println("Reloading level " + gameState.getCurrentLevel());
                    restartLevel = false; // clear the flag
                }
                // update game logic and render graphics
            }
        });

        jmiExit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        f.setJMenuBar(jmb);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String comStr = ae.getActionCommand();
        System.out.println(comStr + " Selected");
    }

    public static void main(String args[]){
        new Main();
    }

    private class GameState {
        private int currentLevel;
        private int score;

        public GameState() {
            currentLevel = 1;
            score = 0;
        }

        public int getCurrentLevel() {
            return currentLevel;
        }

        public void resetLevel() {
            // Reset game state for current level
            score = 0;
            // ...
        }

        // Other methods for updating game state such as updating score, etc.
    }
}