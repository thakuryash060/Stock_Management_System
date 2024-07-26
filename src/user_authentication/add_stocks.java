package user_authentication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class add_stocks {
    private JFrame f;
    private JButton b1, b2, b3;
    private JTextArea textArea;

    public add_stocks() {
        f = new JFrame("Stock Details");


        b1 = new JButton("Back");
        b1.setBounds(40, 400, 120, 50);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new signin();
                f.dispose();
            }
        });

        b2 = new JButton("SIGN OUT");
        b2.setBounds(600, 400, 120, 50);
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });

        b3 = new JButton("ADD");
        b3.setBounds(340, 400, 120, 50);
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStock(user_class.Username);
            }
        });

        textArea = new JTextArea();
        textArea.setBounds(40, 40, 640, 300);
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

            String query = "SELECT * FROM stockdetails";
            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                sb.append("Name: ").append(name).append(", Price: ").append(price).append("\n");
            }
            textArea.setText(sb.toString());

            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(f, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStock(String username) {

        JTextField nameField = new JTextField(10);
        JTextField quantityField = new JTextField(10);


        JPanel panel = new JPanel();
        panel.add(new JLabel("Stock Name:"));
        panel.add(nameField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);


        int result = JOptionPane.showConfirmDialog(f, panel, "Add Stock", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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

                String checkQuery = "SELECT price FROM stockdetails WHERE name = ?";
                PreparedStatement checkPst = con.prepareStatement(checkQuery);
                checkPst.setString(1, name);
                ResultSet rs = checkPst.executeQuery();

                if (rs.next()) {
                    double price = rs.getDouble("price");
                    double totalPrice = price * quantity;


                    String checkUserStockQuery = "SELECT quantity FROM maindetails WHERE username = ? AND name = ?";
                    PreparedStatement checkUserStockPst = con.prepareStatement(checkUserStockQuery);
                    checkUserStockPst.setString(1, username);
                    checkUserStockPst.setString(2, name);
                    ResultSet userStockRs = checkUserStockPst.executeQuery();

                    if (userStockRs.next()) {
                        int existingQuantity = userStockRs.getInt("quantity");
                        int newQuantity = existingQuantity + quantity;
                        double newTotalPrice = price * newQuantity;

                        String updateQuery = "UPDATE maindetails SET quantity = ?, totalprice = ? WHERE username = ? AND name = ?";
                        PreparedStatement updatePst = con.prepareStatement(updateQuery);
                        updatePst.setInt(1, newQuantity);
                        updatePst.setDouble(2, newTotalPrice);
                        updatePst.setString(3, username);
                        updatePst.setString(4, name);
                        updatePst.executeUpdate();

                        JOptionPane.showMessageDialog(f, "Stock updated successfully");
                    } else {

                        String insertQuery = "INSERT INTO maindetails (username, name, price, quantity, totalprice) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement insertPst = con.prepareStatement(insertQuery);
                        insertPst.setString(1, username);
                        insertPst.setString(2, name);
                        insertPst.setDouble(3, price);
                        insertPst.setInt(4, quantity);
                        insertPst.setDouble(5, totalPrice);
                        insertPst.executeUpdate();

                        JOptionPane.showMessageDialog(f, "Stock added successfully");
                    }

                    int addMore = JOptionPane.showConfirmDialog(f, "Do you want to add more stock?", "Add More", JOptionPane.YES_NO_OPTION);
                    if (addMore == JOptionPane.YES_OPTION) {
                        addStock(username);
                    } else {
                        displayStock();
                    }
                } else {
                    JOptionPane.showMessageDialog(f, "Stock name does not exist in the stock details", "Error", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (ClassNotFoundException | SQLException e) {
                JOptionPane.showMessageDialog(f, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}
