package user_authentication;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.jdesktop.swingx.JXDatePicker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class forget {
    private JFrame frame2;
    private JTextField usernameField;
    private JXDatePicker dobPicker;
    private JButton nextButton;

    public forget() {
        frame2 = new JFrame("Forgot Password");
        frame2.setSize(400, 300);
        frame2.setLayout(null);
        frame2.setLocationRelativeTo(null);
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 30);
        frame2.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 30);
        frame2.add(usernameField);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(50, 100, 100, 30);
        frame2.add(dobLabel);

        dobPicker = new JXDatePicker();
        dobPicker.setDate(Calendar.getInstance().getTime());
        dobPicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));
        dobPicker.setBounds(150, 100, 200, 30);
        frame2.add(dobPicker);


        nextButton = new JButton("Next");
        nextButton.setBounds(150, 160, 100, 30);
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleNextAction();
            }
        });
        frame2.add(nextButton);

        frame2.setVisible(true);
    }

    private void handleNextAction() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame2, "Please enter your username.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dobStr = sdf.format(dobPicker.getDate());
        showPassword(username, dobStr);
    }

    private void showPassword(String username, String dob) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "radicals133@")) {
                String query = "SELECT password FROM details WHERE username = ? AND dob = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, dob);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String password = rs.getString("password");
                    JOptionPane.showMessageDialog(frame2, "Password: " + password);
                    frame2.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame2, "No matching record found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(frame2, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    }

