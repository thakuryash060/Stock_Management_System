package user_authentication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class profile {
    private JFrame frame;
    private JButton btnBack, btnSignOut;
    private JLabel lblWelcome, lblDob, lblAccountCreated, lblLastLogin, lblEmail;

    public profile() {
        frame = new JFrame("Profile");
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnBack = new JButton("Back");
        btnBack.setBounds(40, 400, 120, 50);
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new signin();
                frame.dispose();
            }
        });

        btnSignOut = new JButton("Sign Out");
        btnSignOut.setBounds(600, 400, 120, 50);
        btnSignOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        lblWelcome = new JLabel();
        lblWelcome.setHorizontalAlignment(JLabel.CENTER);
        lblWelcome.setBounds(50, 50, 650, 30);

        lblDob = new JLabel();
        lblDob.setBounds(50, 100, 650, 30);

        lblAccountCreated = new JLabel();
        lblAccountCreated.setBounds(50, 150, 650, 30);

        lblLastLogin = new JLabel();
        lblLastLogin.setBounds(50, 200, 650, 30);

        lblEmail = new JLabel();
        lblEmail.setBounds(50, 250, 650, 30);


        frame.add(btnBack);
        frame.add(btnSignOut);
        frame.add(lblWelcome);
        frame.add(lblDob);
        frame.add(lblAccountCreated);
        frame.add(lblLastLogin);
        frame.add(lblEmail);


        fetchAndDisplayUserDetails(user_class.Username);

        frame.setVisible(true);
    }

    private void fetchAndDisplayUserDetails(String username) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "radicals133@")) {
                String query = "SELECT username, dob, signup_time, signin_time, email FROM details WHERE username = ?";
                try (PreparedStatement pst = con.prepareStatement(query)) {
                    pst.setString(1, username);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            String name = rs.getString("username");
                            String dob = rs.getString("dob");
                            LocalDateTime signupTime = rs.getTimestamp("signup_time").toLocalDateTime();
                            LocalDateTime signinTime = rs.getTimestamp("signin_time").toLocalDateTime();
                            String formattedSignupTime = signupTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            String formattedSigninTime = signinTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            String email = rs.getString("email");

                            SwingUtilities.invokeLater(() -> {
                                lblWelcome.setText("Welcome, " + name);
                                lblDob.setText("Date of Birth: " + dob);
                                lblAccountCreated.setText("Account Created: " + formattedSignupTime);
                                lblLastLogin.setText("Last Login: " + formattedSigninTime);
                                lblEmail.setText("Email: " + email);
                            });
                        } else {
                            JOptionPane.showMessageDialog(frame, "No user found with username: " + username, "User Not Found", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
