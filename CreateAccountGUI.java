package Bank;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static Bank.Database.connection;


public class CreateAccountGUI extends Component {

    private JFrame mainFrame;
    boolean sameUsername = true;

    public  CreateAccountGUI(String accountType) {

        mainFrame = new JFrame("Create " + accountType + " account ");
        mainFrame.setSize(300, 300);
        mainFrame.setLayout(new FlowLayout());

        // Username and password fields
        JTextField nameField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        mainFrame.add(new JLabel("Name: "));
        mainFrame.add(nameField);
        mainFrame.add(new JLabel("Username:"));
        mainFrame.add(usernameField);


        JButton checkUsernameButton = new JButton("Check Username");
        checkUsernameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                try {
                    String query = null;
                    if (accountType.equalsIgnoreCase("Customer")) {
                        query = "SELECT * FROM Customer_accounts ";
                    } else if (accountType.equalsIgnoreCase("Employee")) {
                        query = "SELECT * FROM Employee_account";
                    }
                    if (accountType.equalsIgnoreCase("Admin")) {
                        query = "SELECT * FROM Admin_account";
                    }
                    Statement stm = connection.createStatement(); // Create statement
                    ResultSet result = stm.executeQuery(query); // Execute the query
                    String dataUsername;
                    while (result.next()) {
                        dataUsername = result.getString("username");
                        if (dataUsername.equalsIgnoreCase(username)) {
                            sameUsername = true;
                            JOptionPane.showMessageDialog(CreateAccountGUI.this, "This usernmae is been used, try a new one", "Same username ", JOptionPane.ERROR_MESSAGE);

                        }else sameUsername = false;

                        if (sameUsername) {
                            break;
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                if (!sameUsername)
                    JOptionPane.showMessageDialog(CreateAccountGUI.this, "Good usernmae", "Good Username ", JOptionPane.ERROR_MESSAGE);

            }
        });
        mainFrame.add(checkUsernameButton);

        mainFrame.add(new JLabel("\nPassword:"));
        mainFrame.add(passwordField);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (!sameUsername){



                    try {
                        Connection connection = Database.connection;
                        String query = null;
                        if (accountType.equalsIgnoreCase("Customer")) {
                            query = "INSERT INTO Customer_accounts (customer_name, username, password) VALUES ( ?, ?, ?) ";
                        } else if (accountType.equalsIgnoreCase("Employee")) {
                            query = "INSERT INTO Employee_account (name, username, password) VALUES ( ?, ?, ?) ";
                        }
                        if (accountType.equalsIgnoreCase("Admin")) {
                            query = "INSERT INTO Admin_account (Admin_name, Username, Password) VALUES ( ?, ?, ?)";
                        }
                        PreparedStatement stm = connection.prepareStatement(query);
                        stm.setString(1, name);
                        stm.setString(2, username);
                        stm.setString(3, password);
                        stm.executeUpdate();
                        JOptionPane.showMessageDialog(CreateAccountGUI.this, "Account created", "Account created ", JOptionPane.ERROR_MESSAGE);
                        mainFrame.dispose();

                    } catch (Exception xe) {
                        System.out.println(e);
                    }
                }else
                    JOptionPane.showMessageDialog(CreateAccountGUI.this, "This usernmae is been used, try a new one", "Same username ", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainFrame.add(createButton);

        mainFrame.setVisible(true);

    }



}






















