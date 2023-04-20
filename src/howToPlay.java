import javax.swing.*;
import java.awt.*;


// **********************************************************
// *                    howToPlay class                     *
// **********************************************************
public class howToPlay {

    // **********************************************************
    // *                  howToPlay constructor                 *
    // **********************************************************
    howToPlay() {
        // Add rules label
        JLabel label1 = new JLabel();
        label1.setText("Rules");
        label1.setBounds(175, 20, 100, 20);
        label1.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));

        // Add game instructions label
        JLabel label2 = new JLabel();
        label2.setText("<html>OBJECTIVE:<br>The objective in Checkers is to remove all of your opponent's checker pieces from the board. The player with the red checker pieces will begin first.<br><br>" + 
                        "MOVEMENT:<br>To move, first select the piece, then select another space diagonally to move the selected piece. To deselect the piece, select it again.<br>" +
                        "If one player gets to the other end of the board, that piece will be awarded as a King, and will be able to move forwards or backwards.<br><br>" +
                        "MULTIPLE JUMPS:<br>The player will be given the option to make multiple jumps if one is made and another is possible. If the player chooses not to do so, reselect the piece.</html>");
        label2.setBounds(20, 0, 400, 500);
        label2.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));

        // Display how to play window
        JFrame frame = new JFrame();
        frame.setTitle("How To Play");
        frame.setSize(450, 500);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(200, 200, 200));
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(label1);
        frame.add(label2);
    } // End howToPlay constructor
} // End howToPlay class
