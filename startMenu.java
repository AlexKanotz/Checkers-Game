import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

// **********************************************************
// *                    startMenu class                     *
// **********************************************************
public class startMenu implements ActionListener {
    JFrame frame;
    JLabel label, label1;
    JButton startButton, howToPlayButton, exitButton;

    // **********************************************************
    // *                 startMenu constructor                  *
    // **********************************************************
    startMenu() {
        // Add label
        label = new JLabel("Checkers");
        label.setBounds(175, 50, 150, 25); // x, y, width, height
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        
        // Add label
        label1 = new JLabel("Created by: Alex Kanotz");
        label1.setBounds(135, 150, 300, 25); // x, y, width, height
        label1.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        // Add start button
        startButton = new JButton("Start");
        startButton.setBounds(180, 250, 120, 30);
        startButton.addActionListener(this);
        startButton.setFocusable(false);
        startButton.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

        // Add how to play button
        howToPlayButton = new JButton("How To Play");
        howToPlayButton.setBounds(180, 285, 120, 30);
        howToPlayButton.addActionListener(this);
        howToPlayButton.setFocusable(false);
        howToPlayButton.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

        // Add exit button
        exitButton = new JButton("Exit");
        exitButton.setBounds(180, 320, 120, 30);
        exitButton.addActionListener(this);
        exitButton.setFocusable(false);
        exitButton.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

        // Display start menu GUI
        frame = new JFrame();
        frame.setTitle("Checkers");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(118, 142, 181));
        frame.setResizable(false);
        frame.add(label);
        frame.add(label1);
        frame.add(startButton);
        frame.add(howToPlayButton);
        frame.add(exitButton);
    } // End startMenu constructor


    // **********************************************************
    // *                 actionPerformed method                 *
    // **********************************************************
    @Override
    public void actionPerformed(ActionEvent e) {
        // Start the game
        if (e.getSource() == startButton) {
            frame.dispose();
            new gameBoard();
        }

        // Open new window to show user how to play
        if (e.getSource() == howToPlayButton)
            new howToPlay();

        // Exit game
        if (e.getSource() == exitButton)
            System.exit(0);
    } // End actionPerformed method
} // End startMenu class
