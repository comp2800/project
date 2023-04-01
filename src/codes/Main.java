package codes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static codes.Game.create_Scene;

public class Main implements ActionListener {

    private GameState gameState;
    private boolean running = true;
    private boolean restartLevel = false;

    Main() throws IOException {
        JFrame f = new JFrame("Flight Simulator");
        f.setSize(1920, 1080);

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

        f.getContentPane().add(new Game(create_Scene()));

        jmiNewGame.addActionListener((ActionEvent e) -> {
            f.dispose(); // dispose of the current frame
            try {
                new Main(); // create a new instance of Main
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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

    public static void main(String args[]) throws IOException {
        new Main();
    }

    public void actionPerformed(ActionEvent ae) {
        String comStr = ae.getActionCommand();
        System.out.println(comStr + " Selected");
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