package user_authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;
import org.jdesktop.swingx.JXDatePicker;

public class signup {
  private JFrame frame1;
  private JTextField usernameField, emailField;
  private JPasswordField passwordField;
  private JXDatePicker dobPicker;

  public signup() {
    frame1 = new JFrame("Sign Up");
    frame1.setSize(400, 400);
    frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame1.setLayout(null);
    frame1.setLocationRelativeTo(null);


    JLabel titleLabel = new JLabel("SIGN UP", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setBounds(0, 20, 400, 40);
    frame1.add(titleLabel);


    JLabel lblUsername = new JLabel("Username:");
    lblUsername.setBounds(50, 80, 100, 30);
    frame1.add(lblUsername);

    usernameField = new JTextField();
    usernameField.setBounds(150, 80, 200, 30);
    frame1.add(usernameField);


    JLabel lblPassword = new JLabel("Password:");
    lblPassword.setBounds(50, 120, 100, 30);
    frame1.add(lblPassword);

    passwordField = new JPasswordField();
    passwordField.setBounds(150, 120, 200, 30);
    frame1.add(passwordField);


    JLabel lblEmail = new JLabel("Email ID:");
    lblEmail.setBounds(50, 160, 100, 30);
    frame1.add(lblEmail);

    emailField = new JTextField();
    emailField.setBounds(150, 160, 200, 30);
    frame1.add(emailField);

    JLabel lblDOB = new JLabel("Date of Birth:");
    lblDOB.setBounds(50, 200, 100, 30);
    frame1.add(lblDOB);

    dobPicker = new JXDatePicker();
    dobPicker.setDate(Calendar.getInstance().getTime());
    dobPicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));
    dobPicker.setBounds(150, 200, 200, 30);
    frame1.add(dobPicker);

    JButton btnCreateAccount = new JButton("Create Account");
    btnCreateAccount.setBounds(150, 260, 200, 40);
    btnCreateAccount.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && isValidEmail(email)) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          String dobStr = sdf.format(dobPicker.getDate());
          addCredential(username, password, email, dobStr);
        } else {
          JOptionPane.showMessageDialog(frame1, "Please fill in all fields and ensure the email is valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    frame1.add(btnCreateAccount);

    frame1.setVisible(true);
  }

  private boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    return pattern.matcher(email).matches();
  }

  private void addCredential(String username, String password, String email, String dob) {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "radicals133@")) {
        if (emailExists(con, email)) {
          JOptionPane.showMessageDialog(frame1, "An account with this email already exists!", "Duplicate Email", JOptionPane.WARNING_MESSAGE);
        } else {
          PreparedStatement ps = con.prepareStatement("INSERT INTO details (username, password, email, dob) VALUES (?, ?, ?, ?)");
          ps.setString(1, username);
          ps.setString(2, password);
          ps.setString(3, email);
          ps.setString(4, dob);

          int i = ps.executeUpdate();

          if (i > 0) {
            JOptionPane.showMessageDialog(frame1, "Account created successfully!");
            frame1.dispose();
          } else {
            JOptionPane.showMessageDialog(frame1, "Failed to create account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    } catch (ClassNotFoundException | SQLException e) {
      JOptionPane.showMessageDialog(frame1, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private boolean emailExists(Connection con, String email) throws SQLException {
    PreparedStatement ps = con.prepareStatement("SELECT * FROM details WHERE email = ?");
    ps.setString(1, email);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }
}
