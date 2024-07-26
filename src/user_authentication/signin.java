package user_authentication;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class signin {
    private JFrame frame;
    private JButton btnProfile, btnViewStocks, btnBuyStocks, btnSellStocks, btnSignOut;

    public signin() {
        frame = new JFrame("Sign In Menu");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        btnProfile = new JButton("Profile");
        btnProfile.setBounds(50, 50, 150, 40);
        frame.add(btnProfile);

        btnViewStocks = new JButton("View Stocks");
        btnViewStocks.setBounds(50, 100, 150, 40);
        frame.add(btnViewStocks);

        btnBuyStocks = new JButton("Buy Stocks");
        btnBuyStocks.setBounds(250, 50, 150, 40);
        frame.add(btnBuyStocks);

        btnSellStocks = new JButton("Sell Stocks");
        btnSellStocks.setBounds(250, 100, 150, 40);
        frame.add(btnSellStocks);

        btnSignOut = new JButton("Sign Out");
        btnSignOut.setBounds(500, 500, 130, 40);
        frame.add(btnSignOut);

        btnSignOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        btnProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new profile();
                frame.dispose();
            }
        });

        btnViewStocks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new view_stocks(user_class.Username);
                frame.dispose();
            }
        });

        btnBuyStocks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new add_stocks();
            }
        });

        btnSellStocks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new sell_stock();
            }
        });


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    }

