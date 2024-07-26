package user_authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class user_class {
    static String Username;
    private JFrame f;
    private JLabel l, b, m;
    private JTextField u;
    protected JPasswordField p;
    private JButton b1, b2, b3;

    user_class() {
        f = new JFrame();
        ImageIcon ic = new ImageIcon("C:\\Users\\thaku\\Downloads\\inventory.png");
        int frameWidth = 800;
        int frameHeight = 600;
        int fieldWidth = 120;
        int fieldHeight = 40;
        int labelWidth = 100;
        int labelHeight = 40;
        int centerX = (frameWidth - fieldWidth) / 2;

        u = new JTextField();
        u.setBounds(centerX, 100, fieldWidth, fieldHeight);

        l = new JLabel("Username ");
        l.setBounds(centerX - labelWidth - 10, 100, labelWidth, labelHeight);

        p = new JPasswordField();
        p.setBounds(centerX, 150, fieldWidth, fieldHeight);

        m = new JLabel("Password ");
        m.setBounds(centerX - labelWidth - 10, 150, labelWidth, labelHeight);

        b = new JLabel("SIGN IN");
        b.setHorizontalAlignment(b.CENTER);
        b.setBounds(0, 0, frameWidth, 100);

        b1 = new JButton("NEXT");
        b1.setBounds(340, 450, 100, 35);

        b2 = new JButton("Forget Password");
        b2.setBounds(50, 450, 140, 30);
        b3 = new JButton("SIGN UP");
        b3.setBounds(610, 450, 90, 30);

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new signup();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new forget();
            }
        });

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Username = u.getText();
                String Password = new String(p.getPassword());
                if (Username.isEmpty() || Password.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Enter details.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "radicals133@");

                        String query = "SELECT * FROM details WHERE Username = ? AND Password = ?";
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.setString(1, Username);
                        pst.setString(2, Password);

                        ResultSet rs = pst.executeQuery();
                        if (rs.next()) {
                            new signin();
                            updateSignInTime(con, Username);
                            u.setText("");
                            p.setText("");
                        } else {
                            JOptionPane.showMessageDialog(f, "No matching record found.", "Error", JOptionPane.ERROR_MESSAGE);
                            u.setText("");
                            p.setText("");
                        }

                        con.close();
                    } catch (ClassNotFoundException | SQLException g) {
                        JOptionPane.showMessageDialog(f, "Error: " + g.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        f.setIconImage(ic.getImage());
        f.add(u);
        f.add(l);
        f.add(p);
        f.add(m);
        f.add(b);
        f.add(b1);
        f.add(b2);
        f.add(b3);
        f.setSize(frameWidth, frameHeight);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new user_class();
    }

    private static void updateSignInTime(Connection con, String Username) throws SQLException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        String updateQuery = "UPDATE details SET signin_time = ? WHERE Username = ?";
        PreparedStatement updatePst = con.prepareStatement(updateQuery);
        updatePst.setString(1, formattedDateTime);
        updatePst.setString(2, Username);
        updatePst.executeUpdate();
    }
}
