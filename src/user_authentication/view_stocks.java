package user_authentication;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class view_stocks {
    private JFrame frame;
    private JButton btnBack, btnSignOut;
    private String username;

    public view_stocks(String username) {
        this.username = username;
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("View Stocks");
            frame.setSize(750, 600);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(null);
            frame.setLocationRelativeTo(null);

            btnBack = new JButton("Back");
            btnBack.setBounds(40, 500, 120, 50);
            btnBack.addActionListener(e -> {
                new signin();
                frame.dispose();
            });
            frame.add(btnBack);

            btnSignOut = new JButton("Sign Out");
            btnSignOut.setBounds(600, 500, 120, 50);
            btnSignOut.addActionListener(e -> frame.dispose());
            frame.add(btnSignOut);

            displayStocks(username);

            frame.setVisible(true);
        });
    }

    private void displayStocks(String username) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "radicals133@")) {

                String query = "SELECT name, quantity, price FROM maindetails WHERE username = ?";
                try (PreparedStatement pst = con.prepareStatement(query)) {
                    pst.setString(1, username);
                    try (ResultSet rs = pst.executeQuery()) {

                        String[] columnNames = {"Name", "Quantity", "Price"};
                        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                        JTable table = new JTable(model);
                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setBounds(40, 40, 700, 450);
                        frame.add(scrollPane);

                        while (rs.next()) {
                            String name = rs.getString("name");
                            int quantity = rs.getInt("quantity");
                            double price = rs.getDouble("price");
                            model.addRow(new Object[]{name, quantity, price});
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching stock details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
