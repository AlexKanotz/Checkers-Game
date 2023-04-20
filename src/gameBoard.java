import javax.swing.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.awt.*;
import java.util.ArrayList;


// **********************************************************
// *                    gameBoard class                     *
// **********************************************************
public class gameBoard implements ActionListener {
    ArrayList<JButton> buttonArray; // Store buttons
    ArrayList<JPanel> panelArray;   // Store panels

    ArrayList<Boolean> redCheckerArray;      // Keep track of what space the red checker pieces are on
    ArrayList<Boolean> blueCheckerArray;     // Keep track of what space the blue checker pieces are on
    ArrayList<Boolean> redCheckerKingArray;  // Keep track of what space the red checker king pieces are on
    ArrayList<Boolean> blueCheckerKingArray; // Keep track of what space the blue checker king pieces are on

    JFrame frame = new JFrame(); // Create the main frame
    JLabel label = new JLabel(); // Create a label to display the current player's turn
    JPanel panel;                // Create the panels
    JButton button;              // Create/control the buttons on the game board
    JButton startMenuButton, restartButton;

    // Menu items
    JMenuItem startMenu;
    JMenuItem restart;
    JMenuItem howToPlay;
    JMenuItem quit;

    ImageIcon redCheckerPiece, blueCheckerPiece; // Store red and blue checker images
    ImageIcon redCheckerKing, blueCheckerKing;   // Store red and blue checker king images
    Image image;                                 // Sets image formatting

    boolean selected = false;    // Track if a button is selected
    boolean jumpedPiece = false; // Track if a piece has been jumped
    boolean movedPiece = false;  // Track if a piece has been moved

    char player = 'r'; // Track whose turn it is; r for red, b for blue. Red starts

    int currentSpace = 0;   // Track the current space the checker piece is on
    int currentRow = 0;     // Track the row of the current selected space; 1 - 8 (top - bottom)
    int numRedPieces = 12;  // Track the number of red pieces currently on the board
    int numBluePieces = 12; // Track the number of blue pieces currently on the board


    // **********************************************************
    // *                 gameBoard constructor                  *
    // **********************************************************
    gameBoard() {
        // Set up checker board
        panelArray = new ArrayList<JPanel>();
        buttonArray = new ArrayList<JButton>();
        int x = 90, y = 80, j = 0;

        for (int i = 0; i < 64; i++) {
            button = new JButton();
            button.setFocusable(false);
            button.setBounds(x, y, 100, 100);
            button.addActionListener(this);

            panel = new JPanel();
            panel.setBounds(x, y, 100, 100);

            // Check if space is red or black.
            // If j is even, start with black; if j is odd, start with red.
            if (j % 2 == 0) {
                if (i % 2 == 0) {
                    button.setBackground(new Color(0, 0, 0));
                    buttonArray.add(button); // Add button to array
                } else {
                    panel.setBackground(new Color(165, 42, 42));
                    panelArray.add(panel); // Add panel to array
                }
            }
            else {
                if (i % 2 == 0) {
                    panel.setBackground(new Color(165, 42, 42));
                    panelArray.add(panel); // Add panel to array
                } else {
                    button.setBackground(new Color(0, 0, 0));
                    buttonArray.add(button); // Add button to array
                }
            }
            // Check if there should be a new row
            if (x < 720)
                x += 100;
            else {
                x = 90;
                y += 100;
                j++;
            }
        }

        // Place checker pieces on board
        blueCheckerArray = new ArrayList<Boolean>();
        redCheckerArray = new ArrayList<Boolean>();
        blueCheckerKingArray = new ArrayList<Boolean>();
        redCheckerKingArray = new ArrayList<Boolean>();
        
        blueCheckerPiece = new ImageIcon("Images/blueChecker.png");
        redCheckerPiece = new ImageIcon("Images/redChecker.png");

        for (int i = 0; i < buttonArray.size(); i++) {
            if (i < 12) {
            	URL url = gameBoard.class.getResource("blueChecker.png");
            	image = Toolkit.getDefaultToolkit().getImage(url);
                image = image.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
                blueCheckerPiece = new ImageIcon(image);
                buttonArray.get(i).setIcon(blueCheckerPiece);
                button = buttonArray.get(i);
                blueCheckerArray.add(true);
                redCheckerArray.add(false);
            }
            else if (i > 19) {
            	URL url = gameBoard.class.getResource("redChecker.png");
            	image = Toolkit.getDefaultToolkit().getImage(url);
                image = image.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
                redCheckerPiece = new ImageIcon(image);
                buttonArray.get(i).setIcon(redCheckerPiece);
                button = buttonArray.get(i);
                redCheckerArray.add(true);
                blueCheckerArray.add(false);
            }
            else {
                redCheckerArray.add(false);
                blueCheckerArray.add(false);
            }
            // Set the checker king arrays to false
            redCheckerKingArray.add(false);
            blueCheckerKingArray.add(false);
        }

        // Add menu
        addMenu();

        // Display start menu GUI
        frame.setTitle("Checkers");
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(118, 142, 181));
        frame.setResizable(false);

        for (int i = 0; i < buttonArray.size(); i++) {
            frame.add(buttonArray.get(i));
            frame.add(panelArray.get(i));
        }

        displayPlayer();
    } // End gameBoard constructor


    // **********************************************************
    // *                 actionPerformed method                 *
    // **********************************************************
    // When a button is selected, perform an action.
    // Check if the button has been selected or deselected.
    // If one button is selected, validate no other button is selected.
    // If a second button is selected to move checker piece, validate
    // space is adjacent and empty to space selected.
    // If a second button is selected to jump, validate space is empty
    // and adjacent to the piece being jumped. 
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // If a button isn't already selected, select it
        if (!isButtonSelected()) {
            for (int i = 0; i < buttonArray.size(); i++) {
                if (e.getSource() == buttonArray.get(i)) {
                    selectButton(i);
                    break;
                }
            }
        }
        // Deselect the button or select another button to move piece if a piece hasn't been jumped
        else if (isButtonSelected() && !jumpedPiece) {
            for (int i = 0; i < buttonArray.size(); i++) {
                // If the button is reselected, deselect it
                if (e.getSource() == buttonArray.get(i) && buttonArray.get(i) == button) {
                    deselectButton(i);
                    break;
                }
                // Move checker piece
                else {
                    if (e.getSource() == buttonArray.get(i)) {
                        move(i);
                        break;
                    }
                }
            }
        }
        // If a button is selected and piece has been jumped, determine if another piece can be jumped
        else if (isButtonSelected() && jumpedPiece) {
            for (int i = 0; i < buttonArray.size(); i++) {
                // If the button is reselected, deselect it
                if (e.getSource() == buttonArray.get(i) && buttonArray.get(i) == button) {
                    deselectButton(i);
                    break;
                }
                else if (e.getSource() == buttonArray.get(i)) {
                    move(i);
                    break;
                }
            }
        }

        // Go to start menu from menu
        if (e.getSource() == startMenu) {
            frame.dispose();
            new startMenu();
        }
        // Restart game from menu
        if (e.getSource() == restart) {
            frame.dispose();
            new gameBoard();
        }
        // Display how to play window from menu
        if (e.getSource() == howToPlay)
            new howToPlay();
        // Quit the game from menu
        if (e.getSource() == quit)
            System.exit(0);

        // Go to start menu after winning
        if (e.getSource() == startMenuButton) {
            frame.dispose();
            new startMenu();
        }
        // Restart the game after winning
        if (e.getSource() == restartButton) {
            frame.dispose();
            new gameBoard();
        }
    } // End actionPerformed method


    // **********************************************************
    // *                     addMenu method                     *
    // **********************************************************
    public void addMenu()
    {
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Create menu
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        // Create menu items
        startMenu = new JMenuItem("Start Menu");
        restart = new JMenuItem("Restart");
        howToPlay = new JMenuItem("How To Play");
        quit = new JMenuItem("Quit");

        menu.add(startMenu);
        menu.add(restart);
        menu.add(howToPlay);
        menu.add(quit);

        startMenu.addActionListener(this);
        restart.addActionListener(this);
        howToPlay.addActionListener(this);
        quit.addActionListener(this);
    } // End addMenu method


    // **********************************************************
    // *                  displayPlayer method                  *
    // **********************************************************
    // Display the current player whose 
    public void displayPlayer() {
        label.setBounds(425, 30, 150, 20);
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));

        if (player == 'r')
            label.setText("Red's Turn");
        else if (player == 'b')
            label.setText("Blue's Turn");

        frame.add(label);
    } // End displayPlayer method


    // **********************************************************
    // *                   changePlayer method                  *
    // **********************************************************
    // Change the player's turn.
    public void changePlayer() {
        if (player == 'r')
            player = 'b';
        else if (player == 'b')
            player = 'r';

        jumpedPiece = false; // Reinitialize if a piece has been jumped
        movedPiece = false;  // Reinitialize if a piece has been moved
        selected = false;    // Reinitialize if a piece has been selected
        displayPlayer();     // Display the next player
    } // End changePlayer method


    // **********************************************************
    // *                     getRow method                      *
    // **********************************************************
    // Get the row the piece will move to.
    // This will determine if an opponent's piece will be jumped.
    public int getRow(final int index) {
        int newRow = 0;
        if (index >= 0 && index <= 3)
            newRow = 1; // First row
        else if (index >= 4 && index <= 7)
            newRow = 2; // Second row
        else if (index >= 8 && index <= 11)
            newRow = 3; // Third row
        else if (index >= 12 && index <= 15)
            newRow = 4; // Fourth row
        else if (index >= 16 && index <= 19)
            newRow = 5; // Fifth row
        else if (index >= 20 && index <= 23)
            newRow = 6; // Sixth row
        else if (index >= 24 && index <= 27)
            newRow = 7; // Seventh row
        else if (index >= 28 && index <= 31)
            newRow = 8; // Eighth row

        return newRow;
    } // End getRow method


    // **********************************************************
    // *                  displayWinner method                  *
    // **********************************************************
    // Display the winner at the top of the game board.
    public void displayWinner() {
        buttonArray.get(currentSpace).setBackground(new Color(0, 0, 0));
        label.setText(null);

        // Display winner
        JLabel label1 = new JLabel();
        label1.setText("Winner!");
        label1.setBounds(445, 20, 100, 20);
        label1.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));

        JLabel label2 = new JLabel();
        label2.setText(getWinner());
        label2.setBounds(465, 50, 100, 20);
        label2.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));

        // Display button to return to the start menu
        startMenuButton = new JButton("START MENU");
        startMenuButton.setBounds(365, 890, 120, 40);
        startMenuButton.addActionListener(this);
        startMenuButton.setFocusable(false);

        // Display button to restart the game
        restartButton = new JButton("RESTART");
        restartButton.setBounds(495, 890, 120, 40);
        restartButton.addActionListener(this);
        restartButton.setFocusable(false);

        // Add components to the game board
        frame.add(label);
        frame.add(label1);
        frame.add(label2);
        frame.add(startMenuButton);
        frame.add(restartButton);
    } // End displayWinner method


    // **********************************************************
    // *                    isWinner method                     *
    // **********************************************************
    // Return true if there is a winner, false otherwise,
    public boolean isWinner() {
        if (numBluePieces == 0)
            return true;
        else if (numRedPieces == 0)
            return true;

        return false;
    } // End isWinner method


    // **********************************************************
    // *                    getWinner method                    *
    // **********************************************************
    // Return the winner.
    public String getWinner() {
        if (numBluePieces == 0)
            return "Red";
        else if (numRedPieces == 0)
            return "Blue";
        
        return "";
    } // End getwinner method


    // **********************************************************
    // *                illuminateButton method                 *
    // **********************************************************
    public void illuminateButton(final int index) {
        buttonArray.get(index).setBackground(new Color(182, 208, 226));
        button = buttonArray.get(index);
        selected = true;
    } // End illuminateButton method


    // **********************************************************
    // *                isButtonSelected method                 *
    // **********************************************************
    // Determine if a button is selected. If a button is selected,
    // return true, otherwise return false.
    public boolean isButtonSelected() {
        return selected;
    } // End isButtonSelected method


    // **********************************************************
    // *                  selectButton method                   *
    // **********************************************************
    // If a checker piece isn't already selected and the space isn't
    // empty, select a piece. Determine which player is to be selected.
    public void selectButton(final int index) {
        // Validate that the space isn't empty and a button isn't already selected
        if ((blueCheckerArray.get(index) || redCheckerArray.get(index) || redCheckerKingArray.get(index) || blueCheckerKingArray.get(index)) && !isButtonSelected() && !jumpedPiece) {
            // Select red checker piece
            if (redCheckerArray.get(index) && player == 'r')
                illuminateButton(index);
            // Select red checker piece
            else if (blueCheckerArray.get(index) && player == 'b')
                illuminateButton(index);
            // Select red checker king piece
            else if (redCheckerKingArray.get(index) && player == 'r')
                illuminateButton(index);
            // Select blue checker king piece
            else if (blueCheckerKingArray.get(index) && player == 'b')
                illuminateButton(index);

            frame.repaint();
            currentSpace = index;
            currentRow = getRow(index);
        }
    } // End selectButton method


    // **********************************************************
    // *                 deselectButton method                  *
    // **********************************************************
    // Deselect the button if the same button is selected twice.
    // If a piece was previously jumped and the same button was reselected,
    // deselect the button and change the player.
    public void deselectButton(final int index) {
        if (jumpedPiece) {
            changePlayer();
            buttonArray.get(index).setBackground(new Color(0, 0, 0));
            currentSpace = 0;
            frame.repaint();
        }
        else {
            buttonArray.get(index).setBackground(new Color(0, 0, 0));
            currentSpace = 0;
            selected = false;
            frame.repaint();
        }
    } // End deselectButton method


    // **********************************************************
    // *                      move method                       *
    // **********************************************************
    // Move the checker piece. Determine which player moves.
    // Check if a piece is selected and select another position
    // to move. Validate the position is available and if the player
    // is moving one or two spaces away. Two spaces will indicate
    // a jump against the opponent's piece. Check if the piece
    // selected is a king.
    public void move(final int index) {
        // Validate a button is already selected
        if (isButtonSelected()) {
            // Validate space is empty
            if (!redCheckerArray.get(index) && !blueCheckerArray.get(index) && !blueCheckerKingArray.get(index) && !redCheckerKingArray.get(index)) {
                // Determine if piece is moving one space away
                if ((currentRow - 1 == getRow(index) || currentRow + 1 == getRow(index)) && !jumpedPiece) {

                    // Check if the selected piece is a normal red piece
                    if (player == 'r' && redCheckerArray.get(currentSpace))
                        moveRedOneSpace(index);
                    // Check if the selected piece is a normal blue piece
                    else if (player == 'b' && blueCheckerArray.get(currentSpace))
                        moveBlueOneSpace(index);
                    // Check if the selected piece is a king
                    else if (blueCheckerKingArray.get(currentSpace) || redCheckerKingArray.get(currentSpace))
                        moveKingOneSpace(index);
                }
                // Determine if piece is jumping an opponent's piece
                else if (currentRow - 2 == getRow(index) || currentRow + 2 == getRow(index)) {
                    if (player == 'r')
                        jumpBluePiece(index);
                    else if (player == 'b')
                        jumpRedPiece(index);
                }
            }
        }
    } // End movePiece method


    // **********************************************************
    // *                 moveRedOneSpace method                 *
    // **********************************************************
    // Move the red checker piece one adjacent space one adjacent
    // space. Make the piece a king if is on the opposite end of the board.
    public void moveRedOneSpace(final int index) {
        // Check if the piece can become a king
        if (canBeKing(index)) {
            createRedKing(index);
            selected = false;
        }

        // Check if piece is in the first column
        else if ((currentSpace == 8 || currentSpace == 16 || currentSpace == 24) && currentSpace - index == 4)
            movedPiece = true;

        // Check if piece is in the second column
        else if ((currentSpace == 4 || currentSpace == 12 || currentSpace == 20 || currentSpace == 28) && (currentSpace - index == 3 || currentSpace - index == 4))
            movedPiece = true;

        // Check if piece is in the third and fifth column
        else if ((currentSpace - index == 4 || currentSpace - index == 5) &&
        (currentSpace == 9 || currentSpace == 17 || currentSpace == 25 || // Third column
        currentSpace == 10 || currentSpace == 18 || currentSpace == 26)) // Fifth column
            movedPiece = true;

        // Check if piece is in the fourth and sixth column
        else if ((currentSpace - index == 3 || currentSpace - index == 4) &&
        (currentSpace == 13 || currentSpace == 21 || currentSpace == 29 || // Fourth column
        currentSpace == 14 || currentSpace == 22 || currentSpace == 30))  // Sixth column
            movedPiece = true;

        // Check if piece is in the seventh column
        else if ((currentSpace == 11 || currentSpace == 19 || currentSpace == 27) && (currentSpace - index == 4 || currentSpace - index == 5))
        movedPiece = true;

        // Check if piece is in the eighth column
        else if ((currentSpace == 7 || currentSpace == 15 || currentSpace == 23 || currentSpace == 31) && currentSpace - index == 4)
            movedPiece = true;

        // If a piece has been moved update it
        if (movedPiece) {
            updateRedPiece(index);
            selected = false;
        }
    } // End moveRedOneSpace method


    // **********************************************************
    // *                 moveBlueOneSpace method                *
    // **********************************************************
    // Move the blue checker piece one adjacent space one adjacent
    // space. Make the piece a king if is on the opposite end of the board.
    public void moveBlueOneSpace(final int index) {
        // Check if the piece can become a king
        if (canBeKing(index)) {
            createBlueKing(index);
            selected = false;
        }

        // Check if piece is in the first column
        else if ((currentSpace == 0 || currentSpace == 8 || currentSpace == 16 || currentSpace == 24) && index - currentSpace == 4)
            movedPiece = true;

        // Check if piece is in the second column
        else if ((currentSpace == 4 || currentSpace == 12 || currentSpace == 20) && (index - currentSpace == 4 || index - currentSpace == 5))
            movedPiece = true;

        // Check if piece is in the third and fifth column
        else if ((index - currentSpace == 3 || index - currentSpace == 4) &&
        (currentSpace == 1 || currentSpace == 9 || currentSpace == 17 || currentSpace == 25 || // Third column
        currentSpace == 2 || currentSpace == 10 || currentSpace == 18 || currentSpace == 26))  // Fifth column
            movedPiece = true;
        
        // Check if piece is in the fourth and sixth column
        else if ((index - currentSpace == 4 || index - currentSpace == 5) &&
        (currentSpace == 5 || currentSpace == 13 || currentSpace == 21 || // Fourth column
        currentSpace == 6 || currentSpace == 14 || currentSpace == 22))  // Sixth column
            movedPiece = true;

        // Check if piece is in the seventh column
        else if ((currentSpace == 3 || currentSpace == 11 || currentSpace == 19 || currentSpace == 27) && (index - currentSpace == 3 || index - currentSpace == 4))
            movedPiece = true;

        // Check if piece is in the eighth column
        else if ((currentSpace == 7 || currentSpace == 15 || currentSpace == 23) && index - currentSpace == 4)
            movedPiece = true;

        // If a piece has been moved update it
        if (movedPiece) {
            updateBluePiece(index);
            selected = false;
        }
    } // End moveBlueOneSpace method


    // **********************************************************
    // *                moveKingOneSpace method                 *
    // **********************************************************
    public void moveKingOneSpace(final int index) {
        // Check if piece is in the first column
        if ((currentSpace - index == 4 || index - currentSpace == 4) &&
        (currentSpace == 0 || currentSpace == 8 || currentSpace == 16 || currentSpace == 24))
            movedPiece = true;
            
        // Check if piece is in the second column
        else if ((currentSpace - index == 3 || currentSpace - index == 4 || index - currentSpace == 4 || index - currentSpace == 5) &&
        (currentSpace == 4 || currentSpace == 12 || currentSpace == 20 || currentSpace == 28))
            movedPiece = true;

        // Check if piece is in the third and fifth column
        else if ((currentSpace - index == 4 || currentSpace - index == 5 || index - currentSpace == 3 || index - currentSpace == 4) &&
        (currentSpace == 1 || currentSpace == 9 || currentSpace == 17 || currentSpace == 25 || // Third column
        currentSpace == 2 || currentSpace == 10 || currentSpace == 18 || currentSpace == 26)) // Fifth column
            movedPiece = true;

        // Check if piece is in the fourth and sixth column
        else if ((currentSpace - index == 3 || currentSpace - index == 4 || index - currentSpace == 4 || index - currentSpace == 5) &&
        (currentSpace == 5 || currentSpace == 13 || currentSpace == 21 || currentSpace == 29 || // Fourth column
        currentSpace == 6 || currentSpace == 14 || currentSpace == 22 || currentSpace == 30))  // Sixth column
            movedPiece = true;

        // Check if piece is in the seventh column
        else if ((currentSpace - index == 4 || currentSpace - index == 5 || index - currentSpace == 3 || index - currentSpace == 4) &&
        (currentSpace == 3 || currentSpace == 11 || currentSpace == 19 || currentSpace == 27))
            movedPiece = true;

        // Check if piece is in the eighth column
        else if ((currentSpace - index == 4 || index - currentSpace == 4) &&
        (currentSpace == 7 || currentSpace == 15 || currentSpace == 23 || currentSpace == 31))
            movedPiece = true;


        if (redCheckerKingArray.get(currentSpace) && movedPiece)
            updateRedKingPiece(index);
        else if (blueCheckerKingArray.get(currentSpace) && movedPiece)
            updateBlueKingPiece(index);
    } // End moveKingOneSpace method


    // **********************************************************
    // *                  jumpBluePiece method                  *
    // **********************************************************
    // Move the red checker piece and remove the blue checker piece
    // from the board. Determine if a red piece can become a king.
    public void jumpBluePiece(final int index) {
        // Check if current piece is on the first column
        if ((currentSpace == 8 || currentSpace == 16 || currentSpace == 24) && currentSpace - index == 7) {
            if (blueCheckerArray.get(index + 3)) {
                blueCheckerArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 3)) {
                blueCheckerArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
        }
        // Check if current piece is on the second column
        else if ((currentSpace == 12 || currentSpace == 20 || currentSpace == 28) && currentSpace - index == 7) {
            if (blueCheckerArray.get(index + 4)) {
                blueCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 4)) {
                blueCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updatePiece(index);
            }
        }
        // Check if current piece is on the third or fifth column
        else if ((currentSpace - index == 7 || currentSpace - index == 9) &&
        (currentSpace == 9 || currentSpace == 17 || currentSpace == 25 || // Third column
        currentSpace == 10 || currentSpace == 18 || currentSpace == 26))  // Fifth column
        {
            if (blueCheckerArray.get(index + 4) && currentSpace - index == 9) {
                blueCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
            else if (blueCheckerArray.get(index + 3) && currentSpace - index == 7) {
                blueCheckerArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 3)) {
                blueCheckerKingArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 4)) {
                blueCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
        }
        // Check if current piece is on the fourth or sixth column moving upwards
        else if ((currentSpace - index == 7 || currentSpace - index == 9) &&
        (currentSpace == 13 || currentSpace == 21 || currentSpace == 29 || // Fourth column
        currentSpace == 14 || currentSpace == 22 || currentSpace == 30))   // Sixth column
        {
            if (blueCheckerArray.get(index + 5) && currentSpace - index == 9) {
                blueCheckerArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updatePiece(index);
            }
            else if (blueCheckerArray.get(index + 4) && currentSpace - index == 7) {
                blueCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 5)) {
                blueCheckerKingArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 4)) {
                blueCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updatePiece(index);
            }
        }
        // Check if current piece is on the seventh column moving upwards
        else if ((currentSpace == 11 || currentSpace == 19 || currentSpace == 27) && currentSpace - index == 9) {
            if (blueCheckerArray.get(index + 4)) {
                blueCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 4)) {
                blueCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                if (canBeKing(index))
                    createRedKing(index);
                else
                    updatePiece(index);
            }
        }
        // Check if current piece is on the eighth column moving upwards
        else if ((currentSpace == 15 || currentSpace == 23 || currentSpace == 31) && currentSpace - index == 9) {
            if (blueCheckerArray.get(index + 5)) {
                blueCheckerArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updatePiece(index);
            }
            else if (blueCheckerKingArray.get(index + 5)) {
                blueCheckerKingArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updatePiece(index);
            }
        }
        
        /*** Check if a red checker king piece is selected and a blue piece is able to be jumped. ***/

        // Check if current piece is on the first column moving downwards
        else if ((currentSpace == 0 || currentSpace == 8 || currentSpace == 16) && index - currentSpace == 9 && redCheckerKingArray.get(currentSpace)) {
            if (blueCheckerArray.get(index - 5)) {
                blueCheckerArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 5)) {
                blueCheckerKingArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updateRedKingPiece(index);
            }
        }
        // Check if current piece is on the second column moving downwards
        else if ((currentSpace == 4 || currentSpace == 12 || currentSpace == 20) && index - currentSpace == 9 && redCheckerKingArray.get(currentSpace)) {
            if (blueCheckerArray.get(index - 4)) {
                blueCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 4)) {
                blueCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
        }
        // Check if current piece is on the third or fifth column moving downwards
        else if (((index - currentSpace == 7 || index - currentSpace == 9) && redCheckerKingArray.get(currentSpace)) &&
        (currentSpace == 1 || currentSpace == 9 || currentSpace == 17 || // Third column
        currentSpace == 2 || currentSpace == 10 || currentSpace == 18))  // Fifth column
        {
            if (blueCheckerArray.get(index - 5) && index - currentSpace == 9) {
                blueCheckerArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updateRedKingPiece(index);
            }
            else if (blueCheckerArray.get(index - 4) && index - currentSpace == 7) {
                blueCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 5)) {
                redCheckerKingArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 4)) {
                redCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
        }
        // Check if current piece is on the fourth or sixth column moving downwards
        else if (((index - currentSpace == 7 || index - currentSpace == 9) && redCheckerKingArray.get(currentSpace)) &&
        (currentSpace == 5 || currentSpace == 13 || currentSpace == 21 || // Fourth column
        currentSpace == 6 || currentSpace == 14 || currentSpace == 22))   // Sixth column
        {
            if (blueCheckerArray.get(index - 4) && index - currentSpace == 9) {
                blueCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
            else if (blueCheckerArray.get(index - 3) && index - currentSpace == 7) {
                blueCheckerArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 4)) {
                redCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 3)) {
                redCheckerKingArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                updateRedKingPiece(index);
            }
        }
        // Check if current piece is on the seventh column moving downwards
        else if ((currentSpace == 3 || currentSpace == 11 || currentSpace == 19) && index - currentSpace == 7 && redCheckerKingArray.get(currentSpace)) {
            if (blueCheckerArray.get(index - 4)) {
                blueCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 4)) {
                blueCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updateRedKingPiece(index);
            }
        }
        // Check if current piece is on the eighth column moving downwards
        else if ((currentSpace == 7 || currentSpace == 15 || currentSpace == 23) && index - currentSpace == 7 && redCheckerKingArray.get(currentSpace)) {
            if (blueCheckerArray.get(index - 3)) {
                blueCheckerArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                updateRedKingPiece(index);
            }
            else if (blueCheckerKingArray.get(index - 3)) {
                blueCheckerKingArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                updateRedKingPiece(index);
            }
        }
    } // End jumpBluePiece method


    // **********************************************************
    // *                  jumpRedPiece method                   *
    // **********************************************************
    // Move the blue checker piece and remove the red checker piece
    // from the board. Determine if a blue piece can become a king.
    public void jumpRedPiece(final int index) {
        // Check if current piece is on the first column
        if ((currentSpace == 0 || currentSpace == 8 || currentSpace == 16) && index - currentSpace == 9) {
            if (redCheckerArray.get(index - 5)) {
                redCheckerArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 5)) {
                redCheckerKingArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updatePiece(index);
            }
        }
        // Check if current piece is on the second column moving downwards
        else if ((currentSpace == 4 || currentSpace == 12 || currentSpace == 20) && index - currentSpace == 9) {
            if (redCheckerArray.get(index - 4)) {
                redCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 4)) {
                redCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
        }
        // Check if current piece is on the third or fifth column moving downwards
        else if ((index - currentSpace == 7 || index - currentSpace == 9) &&
        (currentSpace == 1 || currentSpace == 9 || currentSpace == 17 || // Third column
        currentSpace == 2 || currentSpace == 10 || currentSpace == 18)) // Fifth column
        {
            if (redCheckerArray.get(index - 5) && index - currentSpace == 9) {
                redCheckerArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updatePiece(index);
            }
            else if (redCheckerArray.get(index - 4) && index - currentSpace == 7) {
                redCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 5)) {
                redCheckerKingArray.set(index - 5, false);
                removeJumpedPiece(index - 5);
                updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 4)) {
                redCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updatePiece(index);
            }
        }
        // Check if current piece is on the fourth or sixth column moving downwards
        else if ((index - currentSpace == 7 || index - currentSpace == 9) &&
        (currentSpace == 5 || currentSpace == 13 || currentSpace == 21 || // Fourth column
        currentSpace == 6 || currentSpace == 14 || currentSpace == 22))  // Sixth column
        {
            if (redCheckerArray.get(index - 4) && index - currentSpace == 9) {
                redCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
            else if (redCheckerArray.get(index - 3) && index - currentSpace == 7) {
                redCheckerArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 4)) {
                redCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 3)) {
                redCheckerKingArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
        }
        // Check if current piece is on the seventh column moving downwards
        else if ((currentSpace == 3 || currentSpace == 11 || currentSpace == 19) && index - currentSpace == 7) {
            if (redCheckerArray.get(index - 4)) {
                redCheckerArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 4)) {
                redCheckerKingArray.set(index - 4, false);
                removeJumpedPiece(index - 4);
                updatePiece(index);
            }
        }
        // Check if current piece is on the eighth column moving downwards
        else if ((currentSpace == 7 || currentSpace == 15 || currentSpace == 23) && index - currentSpace == 7) {
            if (redCheckerArray.get(index - 3)) {
                redCheckerArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
            else if (redCheckerKingArray.get(index - 3)) {
                redCheckerKingArray.set(index - 3, false);
                removeJumpedPiece(index - 3);
                if (canBeKing(index))
                    createBlueKing(index);
                else
                    updatePiece(index);
            }
        }

        /*** Check if a blue checker king piece is selected and a red piece is able to be jumped. ***/

        // Check if current piece is on the first column
        if ((currentSpace == 8 || currentSpace == 16 || currentSpace == 24) && currentSpace - index == 7 && blueCheckerKingArray.get(currentSpace)) {
            if (redCheckerArray.get(index + 3)) {
                redCheckerArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 3)) {
                redCheckerArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                updateBlueKingPiece(index);
            }
        }
        // Check if current piece is on the second column
        else if ((currentSpace == 12 || currentSpace == 20 || currentSpace == 28) && currentSpace - index == 7 && blueCheckerKingArray.get(currentSpace)) {
            if (redCheckerArray.get(index + 4)) {
                redCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 4)) {
                redCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
        }
        // Check if current piece is on the third or fifth column
        else if (((currentSpace - index == 7 || currentSpace - index == 9) && blueCheckerKingArray.get(currentSpace)) &&
        (currentSpace == 9 || currentSpace == 17 || currentSpace == 25 || // Third column
        currentSpace == 10 || currentSpace == 18 || currentSpace == 26)) // Fifth column
        {
            if (redCheckerArray.get(index + 4) && currentSpace - index == 9) {
                redCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
            else if (redCheckerArray.get(index + 3) && currentSpace - index == 7) {
                redCheckerArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 3)) {
                redCheckerKingArray.set(index + 3, false);
                removeJumpedPiece(index + 3);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 4)) {
                redCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
        }
        // Check if current piece is on the fourth or sixth column moving upwards
        else if (((currentSpace - index == 7 || currentSpace - index == 9) && blueCheckerKingArray.get(currentSpace)) &&
        (currentSpace == 13 || currentSpace == 21 || currentSpace == 29 || // Fourth column
        currentSpace == 14 || currentSpace == 22 || currentSpace == 30))  // Sixth column
        {
            if (redCheckerArray.get(index + 5) && currentSpace - index == 9) {
                redCheckerArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updateBlueKingPiece(index);
            }
            else if (redCheckerArray.get(index + 4) && currentSpace - index == 7) {
                redCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 5)) {
                redCheckerKingArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 4)) {
                redCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
        }
        // Check if current piece is on the seventh column moving upwards
        else if ((currentSpace == 11 || currentSpace == 19 || currentSpace == 27) && currentSpace - index == 9 && blueCheckerKingArray.get(currentSpace)) {
            if (redCheckerArray.get(index + 4)) {
                redCheckerArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 4)) {
                redCheckerKingArray.set(index + 4, false);
                removeJumpedPiece(index + 4);
                updateBlueKingPiece(index);
            }
        }
        // Check if current piece is on the eighth column moving upwards
        else if ((currentSpace == 15 || currentSpace == 23 || currentSpace == 31) && currentSpace - index == 9 && blueCheckerKingArray.get(currentSpace)) {
            if (redCheckerArray.get(index + 5)) {
                redCheckerArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updateBlueKingPiece(index);
            }
            else if (redCheckerKingArray.get(index + 5)) {
                redCheckerKingArray.set(index + 5, false);
                removeJumpedPiece(index + 5);
                updateBlueKingPiece(index);
            }
        }
    } // End jumpRedPiece method
    
    
    // **********************************************************
    // *                  updatePiece method                    *
    // **********************************************************
    public void updatePiece(final int index) {
        if (redCheckerArray.get(currentSpace))
            updateRedPiece(index);
        else if (blueCheckerArray.get(currentSpace))
            updateBluePiece(index);
        else if (redCheckerKingArray.get(currentSpace))
            updateRedKingPiece(index);
        else if (blueCheckerKingArray.get(currentSpace))
            updateBlueKingPiece(index);
    } // End updatePiece method


    // **********************************************************
    // *                 updateRedPiece method                  *
    // **********************************************************
    // Update the red checker piece by removing it from the current
    // space and moving the it to the new space.
    public void updateRedPiece(final int index) {
        buttonArray.get(index).setIcon(redCheckerPiece);
        redCheckerArray.set(index, true);
        redCheckerArray.set(currentSpace, false);
        removeCurrentPiece();

        // Determine if there is a winner
        if (isWinner())
            displayWinner();
        else if (!jumpedPiece)
            changePlayer();
        else {
            illuminateButton(index);
            currentSpace = index;
            currentRow = getRow(index);
        }
    } // End updateRedPiece method


    // **********************************************************
    // *                 updateBluePiece method                 *
    // **********************************************************
    // Update the blue checker piece by removing it from the current
    // space and moving the it to the new space.
    public void updateBluePiece(final int index) {
        buttonArray.get(index).setIcon(blueCheckerPiece);
        blueCheckerArray.set(index, true);
        blueCheckerArray.set(currentSpace, false);
        removeCurrentPiece();

        // Determine if there is a winner
        if (isWinner())
            displayWinner();
        else if (!jumpedPiece)
            changePlayer();
        else {
            illuminateButton(index);
            currentSpace = index;
            currentRow = getRow(index);
        }
    } // End updateBluePiece method


    // **********************************************************
    // *                    canBeKing method                    *
    // **********************************************************
    // Determine if the piece is able to become a king.
    public boolean canBeKing(final int index) {
        if ((index == 0 || index == 1 || index == 2 || index == 3) && (redCheckerArray.get(currentSpace) && player == 'r'))
            return true;
        else if ((index == 28 || index == 29 || index == 30 || index == 31)  && (blueCheckerArray.get(currentSpace) && player == 'b'))
            return true;

        return false;
    } // End canBeKing method


    // **********************************************************
    // *                 createRedKing method                   *
    // **********************************************************
    // Create a red checker piece king and check of a winner.
    public void createRedKing(final int index) {
		redCheckerKing = new ImageIcon("Images/redCheckerKing.png");
        URL url = gameBoard.class.getResource("redCheckerKing.png");
    	image = Toolkit.getDefaultToolkit().getImage(url);
        image = image.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
        redCheckerKing = new ImageIcon(image);
        buttonArray.get(index).setIcon(redCheckerKing);
        redCheckerKingArray.set(index, true);
        redCheckerArray.set(currentSpace, false);
        removeCurrentPiece();

        // Determine if there is a winner
        if (isWinner())
            displayWinner();
        else
            changePlayer();
    } // End createRedKing method


    // **********************************************************
    // *                 createBlueKing method                  *
    // **********************************************************
    // Create a blue checker piece king and check if there is a winner.
    public void createBlueKing(final int index) {
        blueCheckerKing = new ImageIcon("Images/blueCheckerKing.png");
        URL url = gameBoard.class.getResource("blueCheckerKing.png");
    	image = Toolkit.getDefaultToolkit().getImage(url);
        image = image.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
        blueCheckerKing = new ImageIcon(image);
        buttonArray.get(index).setIcon(blueCheckerKing);
        blueCheckerKingArray.set(index, true);
        blueCheckerArray.set(currentSpace, false);
        removeCurrentPiece();

        // Determine if there is a winner
        if (isWinner())
            displayWinner();
        else
            changePlayer();
    } // End createBlueKing method


    // **********************************************************
    // *               updateRedKingPiece method                *
    // **********************************************************
    // Update the red king checker piece by removing it from the
    // current space and moving the it to the new space.
    public void updateRedKingPiece(final int index) {
        buttonArray.get(index).setIcon(redCheckerKing);
        redCheckerKingArray.set(index, true);
        redCheckerKingArray.set(currentSpace, false);
        removeCurrentPiece();

        // Determine if there is a winner
        if (isWinner())
            displayWinner();
        else if (!jumpedPiece)
            changePlayer();
        else {
            illuminateButton(index);
            currentSpace = index;
            currentRow = getRow(index);
        }
    } // End updateKingPiece method


    // **********************************************************
    // *               updateBlueKingPiece method               *
    // **********************************************************
    // Update the blue king checker piece by removing it from the
    // current space and moving the it to the new space.
    public void updateBlueKingPiece(final int index) {
        buttonArray.get(index).setIcon(blueCheckerKing);
        blueCheckerKingArray.set(index, true);
        blueCheckerKingArray.set(currentSpace, false);
        removeCurrentPiece();

        // Determine if there is a winner
        if (isWinner())
            displayWinner();
        else if (!jumpedPiece)
            changePlayer();
        else {
            illuminateButton(index);
            currentSpace = index;
            currentRow = getRow(index);
        }
    } // End updateKingPiece method


    // **********************************************************
    // *               removeCurrentPiece method                *
    // **********************************************************
    // Remove the current checker piece.
    public void removeCurrentPiece() {
        buttonArray.get(currentSpace).setIcon(null);
        buttonArray.get(currentSpace).setBackground(new Color(0, 0, 0));
        frame.repaint();
    } // End removePiece method


    // **********************************************************
    // *                removeJumpedPiece method                *
    // **********************************************************
    // Remove the jumped checker piece from the board.
    public void removeJumpedPiece(final int index) {
        if (player == 'r')
            --numBluePieces;
        else if (player == 'b')
            --numRedPieces;

        jumpedPiece = true;
        buttonArray.get(index).setIcon(null);
        frame.repaint();
    } // End removePiece method
} // End gameBoard class