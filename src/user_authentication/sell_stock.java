package user_authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class sell_stock {
    private JFrame f;
    private JButton b1, b2, b3;
    private JTextArea textArea;

    public sell_stock() {
        f = new JFrame("Sell Stocks");

        b1 = new JButton("Back");
        b1.setBounds(20, 400, 120, 50);

        b2 = new JButton("SIGN OUT");
        b2.setBounds(600, 400, 120, 50);

        b3 = new JButton("SELL");
        b3.setBounds(340, 400, 120, 50);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new signin();
                f.dispose();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sellStock(user_class.Username);
            }
        });

        textArea = new JTextArea();
        textArea.setBounds(20, 40, 700, 300);
        textArea.setEditable(false);

        f.add(b1);
        f.add(b2);
        f.add(b3);
        f.add(textArea);

        f.setSize(750, 500);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        displayStock();
    }

    private void displayStock() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "radicals133@");

            String query = "SELECT name, quantity FROM maindetails WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, user_class.Username);

            ResultSet rs = pst.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                sb.append("Name: ").append(name).append(", Quantity: ").append(quantity).append("\n");
            }
            textArea.setText(sb.toString());

            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(f, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sellStock(String username) {
        JTextField nameField = new JTextField(10);
        JTextField quantityField = new JTextField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Stock Name:"));
        panel.add(nameField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(f, panel, "Sell Stock", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String quantityStr = quantityField.getText().trim();

            if (name.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(f, "Name and quantity cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(f, "Quantity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "radicals133@");

                String checkQuery = "SELECT quantity, price FROM maindetails WHERE username = ? AND name = ?";
                PreparedStatement checkPst = con.prepareStatement(checkQuery);
                checkPst.setString(1, username);
                checkPst.setString(2, name);
                ResultSet rs = checkPst.executeQuery();

                if (rs.next()) {
                    int existingQuantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    if (quantity > existingQuantity) {
                        JOptionPane.showMessageDialog(f, "Insufficient quantity to sell", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int newQuantity = existingQuantity - quantity;
                        double totalPrice = price * newQuantity;

                        if (newQuantity > 0) {
                            String updateQuery = "UPDATE maindetails SET quantity = ?, totalprice = ? WHERE username = ? AND name = ?";
                            PreparedStatement updatePst = con.prepareStatement(updateQuery);
                            updatePst.setInt(1, newQuantity);
                            updatePst.setDouble(2, totalPrice);
                            updatePst.setString(3, username);
                            updatePst.setString(4, name);
                            updatePst.executeUpdate();
                            JOptionPane.showMessageDialog(f, "Stock sold successfully");
                        } else {
                            String deleteQuery = "DELETE FROM maindetails WHERE username = ? AND name = ?";
                            PreparedStatement deletePst = con.prepareStatement(deleteQuery);
                            deletePst.setString(1, username);
                            deletePst.setString(2, name);
                            deletePst.executeUpdate();
                            JOptionPane.showMessageDialog(f, "Stock sold successfully and removed from your portfolio");
                        }

                        int addMore = JOptionPane.showConfirmDialog(f, "Do you want to sell more stock?", "Sell More", JOptionPane.YES_NO_OPTION);
                        if (addMore == JOptionPane.YES_OPTION) {
                            sellStock(username);
                        } else {
                            displayStock();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(f, "Stock name does not exist in your stock details", "Error", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (ClassNotFoundException | SQLException e) {
                JOptionPane.showMessageDialog(f, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
