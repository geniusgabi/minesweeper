
/**
 * Write a description of class Play here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
public class Play
{
    private static boolean easy1 = false;
    private static boolean medium1 = false;
    private static boolean hard1 = false;
    private static int eSize = 12;
    private static int mSize = 22;
    private static int hSize = 32;
    private static JButton homeButton = new JButton("HOME");
    private static JFrame newframe = new JFrame();
    
    public static void main(String[] args)
    {
        createHome(homeButton);
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //b.getPanel().removeAll();
                createHome(homeButton);
                easy1 = false;
                medium1 = false;
                hard1 = false;
            }
        });
        
        
        
    }
    
    public static void createHome(JButton homeB)
    {
        JPanel home = new JPanel();
        
        newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newframe.setVisible(true);
        newframe.setLayout(new BorderLayout());

        home.setBackground(Color.white);
        home.setPreferredSize(new Dimension(500, 500));
        home.setLayout(null);

        home.setVisible(true);
        newframe.getContentPane().add(home);
        newframe.pack();
        
        JLabel title = new JLabel("Minesweeper");
        title.setBounds(225, 50, 200, 50);
        home.add(title);
        JButton reset = new JButton("RESET");
        
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (easy1 == true)
                {
                    Board b = new Board(eSize, eSize, newframe, reset, homeB);
                    b.getPanel().removeAll();
                    Board c = new Board(eSize, eSize, newframe, reset, homeB);
                }
                else if (medium1)
                {
                    Board b = new Board(mSize, mSize, newframe, reset, homeB);
                    b.getPanel().removeAll();
                    Board c = new Board(mSize, mSize, newframe, reset, homeB);
                }
                else if (hard1)
                {
                    Board b = new Board(hSize, hSize, newframe, reset, homeB);
                    b.getPanel().removeAll();
                    Board c = new Board(hSize, hSize, newframe, reset, homeB);
                }
            }
        });
        
       
        
        JButton easy = new JButton("Easy");
        easy.setBounds(230, 100, 75, 50);
        home.add(easy);
        easy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Board b = new Board(12, 12, newframe, reset, homeB);
                newframe.getContentPane().remove(home);
                easy1 = true;
            }
        });
        
        JButton medium = new JButton("Medium");
        medium.setBounds(215, 175, 100, 50);
        home.add(medium);
        medium.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Board b = new Board(22, 22, newframe,reset, homeB);
                newframe.getContentPane().remove(home);
                medium1 = true;
            }
        });
        
        JButton hard = new JButton("Hard");
        hard.setBounds(230, 240, 75, 50);
        home.add(hard);
        hard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Board b = new Board(32, 32, newframe, reset, homeB);
                newframe.getContentPane().remove(home);
                hard1 = true;
            }
        });
    }
}
