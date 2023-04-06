package codes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static codes.Game.create_Scene;

public class Main implements ActionListener {

    Main() throws IOException {
        JFrame f = new JFrame("Flight Simulator");
        f.setSize(1920, 1080);


        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar jmb = new JMenuBar();

        JMenu jmFile = new JMenu("File");
        JMenuItem jmiNewGame = new JMenuItem("New Game");
        jmFile.add(jmiNewGame);
        jmFile.addSeparator();
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

        f.setJMenuBar(jmb);
        f.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    public void actionPerformed(ActionEvent ae) {
    }
}