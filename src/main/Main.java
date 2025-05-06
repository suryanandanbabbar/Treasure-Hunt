package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        // This lets the window properly close when user clicks the close ("x") button.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Cannot resize the window
        window.setResizable(false);
        // Game Title
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        /*
        Causes this Window to be sized to fit the preferred size
        and layouts of its subcomponents.
         */
        window.pack();

        /* Not specify the location of the window.
        = The window will be displayed at the center of the screen */
        window.setLocationRelativeTo(null);
        // To make the window visible
        window.setVisible(true);
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}