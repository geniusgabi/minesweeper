/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
public class Board
{
    private JButton[][] board;
    private boolean[][] bombs;
    private int[][] numbers;
    private double bombCheck;
    private final int SQUARESIZE = 25;
    private static JFrame frame;
    private static JPanel panel = new JPanel();
    private int xSize;
    private int ySize;
    private int bombCount;
    private int xClick;
    private int yClick;
    private int bombsAround;
    private JButton clickingBombs;
    private boolean bombMode = false;
    private JButton restart;
    private boolean hasWon = false;
    private boolean isFlagged[][];
    private int numCorrect = 0;
    private int numFlagged = 0;
    private boolean hasLost = false;
    private JLabel numBombs = new JLabel(); 
    private JLabel numFlag = new JLabel();
    private int initialBombCount;
    private boolean bombsInit = false;
    private JButton square = new JButton(); 
    private JButton homey;
       
    private Icon squarePic = new ImageIcon("/Users/gabi/Downloads/minesweep/idlesquare.png");
       private Icon bombPic = new ImageIcon("/Users/gabi/Downloads/minesweep/bomb.png");
       private Icon onePic = new ImageIcon("/Users/gabi/Downloads/minesweep/one.png");
       private Icon twoPic = new ImageIcon("/Users/gabi/Downloads/minesweep/two.png");
       private Icon threePic = new ImageIcon("/Users/gabi/Downloads/minesweep/three.png");
       private Icon fourPic = new ImageIcon("/Users/gabi/Downloads/minesweep/four.png");
       private Icon fivePic = new ImageIcon("/Users/gabi/Downloads/minesweep/five.png");
       private Icon sixPic = new ImageIcon("/Users/gabi/Downloads/minesweep/six.png");
       private Icon sevenPic = new ImageIcon("/Users/gabi/Downloads/minesweep/seven.png");
       private Icon eightPic = new ImageIcon("/Users/gabi/Downloads/minesweep/eight.png");
       private Icon emptyPic = new ImageIcon("/Users/gabi/Downloads/minesweep/empty.png");
       private Icon flagPic = new ImageIcon("/Users/gabi/Downloads/minesweep/flag.png");

    /**
     * Constructor for objects of class Board
     */
    public Board(int x, int y, JFrame framePass, JButton reseter, JButton homeBut)
    {
        panel.removeAll();
        board = new JButton[x][y];
        bombs = new boolean[x][y];
        numbers = new int[x][y];
        isFlagged = new boolean[x][y];
        xSize = x;
        ySize = y;
        bombCount = 0;
        frame = framePass;
        restart = reseter;
        restart.setBounds(xSize*25, ySize*15, 75, 25);
        panel.add(reseter);
        homey = homeBut;
        homey.setBounds(xSize*25, ySize*18, 75, 25);
        homey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                JLabel title = new JLabel("Minesweeper");
                title.setBounds(225, 50, 200, 50);
                panel.add(title);
            }
        });
        panel.add(homey);

        guiMaker();
        placeBombs();
        initialBombCount = bombCount;
        placeNumbers();
        checkingBombsButton();

        //debugging stuff to see if number placement works
        // for (int e = 1; e < x-1; e++)
        // {
            // for (int w = 1; w < y-1; w++)
            // {
               // System.out.print(bombs[w][e] + " ");
            // }
            // System.out.println();
        // }
        // for (int e = 0; e < xSize; e++)
        // {
            // for (int w = 0; w < ySize; w++)
            // {
               // System.out.print(numbers[w][e] + " ");
            // }
            // System.out.println();
        // }


    }

    /**
     * creates the GUI
     */
    public void guiMaker()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());

        panel.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(xSize*25 + 200, ySize*25));
        panel.setLayout(null);

        panel.setVisible(true);
        frame.getContentPane().add(panel);
        frame.pack();
    }

    /**
     * creates a generic idle square button that should be added into the board array.
     */
    public JButton makeGenericButton(int xPos, int yPos)
    {
                if (xPos == 0 || xPos == xSize -1 || yPos == 0 || yPos == ySize - 1)
                {
                    square.setBounds((SQUARESIZE)*xPos, (SQUARESIZE)*yPos, SQUARESIZE, SQUARESIZE);
                }
                else
                {
                    square = new JButton(squarePic);
                    square.setBounds((SQUARESIZE)*xPos, (SQUARESIZE)*yPos, SQUARESIZE, SQUARESIZE);
                    square.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (hasWon)
                {
                    for (int r = 0; r< xSize; r++)
                    {
                          for (int c = 0; c < ySize; c++)
                          {
                             getButton(r, c).setEnabled(false); 
                           }
                    }   
                }
                else
                {
                if (bombMode == true && bombsInit == false)
                {
                    if (isFlagged[xPos][yPos] == true)
                    {
                          getButton(xPos, yPos).setIcon(squarePic);
                          isFlagged[xPos][yPos] = false;
                          numFlagged--;
                          bombCount++;
                          refreshFlagCount();
                          //refreshBombCount();
                    }   
                    else
                    {
                        getButton(xPos, yPos).setIcon(flagPic);
                        isFlagged[xPos][yPos] = true;
                        numFlagged++;
                        bombCount--;
                        refreshFlagCount();
                        //refreshBombCount();
                    }
                }
                else if (bombs[xPos][yPos] == true && !isFlagged[xPos][yPos] && bombsInit == false)
                {
                        for (int x = 0; x < xSize; x++)
                        {
                            for (int y = 0; y < ySize; y++)
                            {
                                if (bombs[x][y] == true)
                                {
                                    getButton(x, y).setIcon(bombPic);
                                }
                            }
                        }
                        bombsInit = true;
                        loseMessage2();
                }
                else if (!isFlagged[xPos][yPos] && bombsInit == false)
                {
                    if (getNumbersAround(xPos, yPos) == 1)
                    {
                        getButton(xPos, yPos).setIcon(onePic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 2)
                    {
                        getButton(xPos, yPos).setIcon(twoPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 3)
                    {
                        getButton(xPos, yPos).setIcon(threePic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 4)
                    {
                        getButton(xPos, yPos).setIcon(fourPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 5)
                    {
                        getButton(xPos, yPos).setIcon(fivePic);
                    }   
                    else if (getNumbersAround(xPos, yPos) == 6)
                    {
                        getButton(xPos, yPos).setIcon(sixPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 7)
                    {
                        getButton(xPos, yPos).setIcon(sevenPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 8)
                    {
                        getButton(xPos, yPos).setIcon(eightPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 0 && isBomb(xPos, yPos) == false)
                    {
                        getButton(xPos, yPos).setIcon(emptyPic);
                        for (int a = xPos-1; a <= xPos +1; a++)
                        {
                            for (int b = yPos-1; b <= yPos +1; b++)
                                { 
                                    if (a ==0  && b ==0)
                                    {
                                        setSquare(a+1, b+1);
                                    }
                                    else if (a == xSize-1 && b ==0)
                                    {
                                        setSquare(a-1, b+1);
                                    }
                                    else if (a == xSize -1 && b == ySize-1)
                                    {
                                        setSquare(a-1, b-1);
                                    }
                                    else if (b == ySize-1 && a ==0)
                                    {
                                            setSquare(a +1, b-1);
                                    }
                                    else if (b == ySize-1)
                                    {
                                        setSquare(a, b-1);
                                    }   
                                    else if (a == xSize -1)
                                    {
                                        setSquare(a-1, b);
                                    }
                                    else if (a ==0)
                                    {
                                        setSquare(a +1, b);
                                    }
                                    else if (b == 0)
                                    {
                                        setSquare(a, b+1);
                                    }
                                    else 
                                    {
                                        setSquare(a, b);
                                    }
                                }
                            }
                    }
                }
                if (numFlagged == initialBombCount)
                {
                for (int y = 0; y < xSize; y++)
                {
                    for (int t = 0; t < ySize; t++)
                    {
                        if (bombs[y][t] == true && isFlagged[y][t] == true)
                        {
                            numCorrect++;
                            //System.out.print(bombs[y][t] + " " + isFlagged[y][t]);
                        }
                    }
                }
                if (numCorrect == initialBombCount)
                {
                    //System.out.println(numCorrect + " " + bombCount);
                    winMessage();
                    for (int k = 1; k < xSize-1; k++)
                    {
                        for (int l = 1; l < ySize-1; l++)
                        {
                            setSquare(k,l);
                        }
                    }   
                    hasWon = true;
                }
                else 
                {
                    hasLost = true;
                    for (int k = 1; k < xSize-1; k++)
                    {
                        for (int l = 1; l < ySize-1; l++)
                        {
                            if (bombs[k][l] == true)
                                {
                                    getButton(k, l).setIcon(bombPic);
                                }
                            setSquare(k,l);
                        }
                    }   
                    loseMessage();
                }
             }
            }
        }

        });
    }


        panel.add(square);
        return square;
    }

    
    /**
     * makes the flag bomb button
     */
    public JButton checkingBombsButton()
    {
        clickingBombs = new JButton("FLAG BOMBS");
        clickingBombs.setBounds(xSize*25 + 20, 25, 102, 50);
        clickingBombs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bombMode = !bombMode;
                if (bombMode)
                {
                    clickingBombs.setBackground(Color.RED);
                    clickingBombs.setOpaque(true);
                }
                else
                {
                    clickingBombs.setBackground(Color.WHITE);
                    clickingBombs.setOpaque(true);
                }
              
            }
        });
        panel.add(clickingBombs);
        return clickingBombs;
    }

    /**
     * returns button at certain position
     */
    public JButton getButton(int xPosi, int yPosi)
    {
        return board[xPosi][yPosi];
    }

    /**
     * generates bombs
     */
    public void placeBombs()
    {
        for (int row = 1; row < xSize - 1; row++)
        {
            for (int col = 1; col < ySize - 1; col++)
            {
                board[row][col] = makeGenericButton(row, col);
                bombCheck = (Math.random());
                //System.out.println(bombCheck);
                //System.out.println(3.0/5);
                if (xSize >= 17)
                {
                    if (bombCheck <= 1.0/7)
                    {
                        bombs[row][col] = true;
                        bombCount++;
                        //makeGenericButton(row, col);
                    }
                    else 
                    {
                        bombs[row][col] = false;
                    }
                }
                else 
                {
                 if (bombCheck <= 0.5/5)
                    {
                        bombs[row][col] = true;
                        bombCount++;
                        //makeGenericButton(row, col);
                    }
                    else 
                    {
                        bombs[row][col] = false;
                    }   
                }
                //System.out.println(bombs[row][col]);
            }
        }
        numFlag.setText("Number left to flag: " + bombCount);
        numFlag.setBounds(xSize*25, ySize*10, 200, 50);
        //System.out.println("1");
        panel.add(numFlag);
        //System.out.println("1");
        panel.revalidate();
        panel.repaint();
    }

    
    /**
     * refreshes how many bombs are left
     */
    public void refreshBombCount()
    {
        numBombs.setText("Number of bombs left: " + bombCount);
        panel.revalidate();
        panel.repaint();
        //System.out.println("1");
    }
    
    
/**
 * refreshes how many bombs you have flagged
 */
    public void refreshFlagCount()
    {
        numFlag.setText("Number left to flag: " + bombCount);
        panel.revalidate();
        panel.repaint();
        //System.out.println("1");
    }

    /**
     * generates numbers for squares
     */
    public void placeNumbers()
    {
        for (int a = 1; a < xSize -1; a++)
        {
            for (int b = 1; b < ySize -1; b++)
            {
                if (bombs[a][b] == false)
                {
                    bombsAround = 0;
                    for (int p = a-1;  p <= a+1; p++)
                    {
                        for (int r = b-1; r <= b+1; r++)
                        {
                            if (bombs[p][r] == true)
                            {
                                bombsAround++;
                            }
                        }
                    }
                    numbers[a][b] = bombsAround;
                }
            }
        }
    }
    
    
    /**
     * sets the square to the number/space it should be
     */
    public void setSquare(int xPos, int yPos)
    {
        if (getNumbersAround(xPos, yPos) == 1)
                    {
                        getButton(xPos, yPos).setIcon(onePic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 2)
                    {
                        getButton(xPos, yPos).setIcon(twoPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 3)
                    {
                        getButton(xPos, yPos).setIcon(threePic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 4)
                    {
                        getButton(xPos, yPos).setIcon(fourPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 5)
                    {
                        getButton(xPos, yPos).setIcon(fivePic);
                    }   
                    else if (getNumbersAround(xPos, yPos) == 6)
                    {
                        getButton(xPos, yPos).setIcon(sixPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 7)
                    {
                        getButton(xPos, yPos).setIcon(sevenPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 8)
                    {
                        getButton(xPos, yPos).setIcon(eightPic);
                    }
                    else if (getNumbersAround(xPos, yPos) == 0 && isBomb(xPos, yPos) == false)
                    {
                        getButton(xPos, yPos).setIcon(emptyPic);
                    }
    }

    /**
     * returns the number of a square
     */
    public int getNumbersAround(int p, int o)
    {
        return numbers[p][o];
    }

    /**
     * returns if the square is a bomb or not
     */
    public boolean isBomb(int g, int n)
    {
        return bombs[g][n];
    }

    /**
     * adds a win message
     */
    public void winMessage()
    {
        // panel.removeAll();
        // panel.repaint();
        JLabel winner = new JLabel("You Won!");
        winner.setBounds(xSize*25, 65, 102, 50);
        winner.setBackground(Color.BLUE);
        //panel.revalidate();
        panel.add(winner);
        panel.revalidate();
        panel.repaint();
        //System.out.println("You Win!");
    }

    /**
     * adds a lose message that tells you your flags are wrong
     */
    public void loseMessage()
    {
        JLabel loser = new JLabel("Your flags are wrong.");
        loser.setBounds((xSize*25), ySize*5, 200, 50);
        loser.setBackground(Color.BLUE);
        //panel.revalidate();
        panel.add(loser);
        panel.revalidate();
        panel.repaint();
    }

    
    /**
     * adds a lose message that happens when you hit a bomb
     */
    public void loseMessage2()
    {
        JLabel loser = new JLabel("You lose.");
        loser.setBounds((xSize*25 + 100), 65, 102, 50);
        loser.setBackground(Color.BLUE);
        //panel.revalidate();
        panel.add(loser);
        panel.revalidate();
        panel.repaint();
    }
    
    public JFrame getFrame()
    {
        return frame;
    }
    
    public JPanel getPanel()
    {
        return panel;
    }




}