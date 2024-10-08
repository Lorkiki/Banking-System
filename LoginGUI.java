package Bank;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginGUI extends Component {

    // Main frame
    private JFrame mainFrame;

    // Constructor
    public LoginGUI() {
        mainFrame = new JFrame("Login System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 300);
        mainFrame.setLayout(new GridLayout(3, 1));

        // Buttons to login as customer, employee, or admin
        JButton customerButton = new JButton("Login as Customer");
        JButton employeeButton = new JButton("Login as Employee");
        JButton adminButton = new JButton("Login as Admin");
        JButton createNewAccountButton = new JButton("Create new account");

        // Add action listeners to buttons
        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginWindow("Customer");
            }
        });
        employeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginWindow("Employee");
            }
        });
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginWindow("Admin");
            }
        });

        // Add buttons to main frame
        mainFrame.add(customerButton);
        mainFrame.add(employeeButton);
        mainFrame.add(adminButton);

        createNewAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateAccountGUI newaccount = new CreateAccountGUI("Customer");

            }
        });
        mainFrame.add(createNewAccountButton);



        // Display the main frame
        mainFrame.setVisible(true);
    }

    // Method to show the login window
    private void showLoginWindow(String userType) {
        // Login frame
        JFrame loginFrame = new JFrame(userType + " Login");
        loginFrame.setSize(300, 300);
        loginFrame.setLayout(new FlowLayout());

        // Username and password fields
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton mainMenuButton = new JButton("Main Menu");

        // Action listener for main menu button to return to main frame
        mainMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
                mainFrame.setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String type = userType;

                if (authentication(username, password, type) )
                     loginFrame.dispose();
            }
        });



        // Add components to login frame
        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(usernameField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(mainMenuButton);

        // Display the login frame
        mainFrame.setVisible(false);
        loginFrame.setVisible(true);
    }

    // Main method to run the application
    public static void main(String[] args) {
        Database.connect();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginGUI();
            }
        });
    }


    public void setVisible(boolean b) {
        mainFrame.setVisible(true);
    }




    public boolean authentication(String username, String password, String type) {
        boolean accountFound = false;
        boolean passwordcorrect = false;
        try {
            Connection connection = Database.connection; // Connect to database
            String query = null;

            if (type.equalsIgnoreCase("Customer")) {
                query = "SELECT * FROM Customer_accounts " ;}
            else if (type.equalsIgnoreCase("Employee")) {
                query = "SELECT * FROM Employee_account" ;}
            else if (type.equalsIgnoreCase("Admin")) {
                query = "SELECT * FROM Admin_account" ;
            }
             
            Statement stm = connection.createStatement(); // Create statement
            ResultSet result = stm.executeQuery(query); // Execute the query


            String dataUsername;
            String dataPassword;
            int account_ID;


            while (result.next()) {
                dataUsername = result.getString("username");
                dataPassword = result.getString("password");
                account_ID = result.getInt("account_ID");
                if(dataUsername.equalsIgnoreCase(username))
                { accountFound = true;

                    if (HashFunction.checkPassword(password, dataPassword) && accountFound)
                    {
                        passwordcorrect = true;
                        if (type.equalsIgnoreCase("Customer")) {
                            CustomerAccountGUI loginpage = new CustomerAccountGUI(account_ID);
                        loginpage.setVisible(true);}
                        else if (type.equalsIgnoreCase("Employee")) {
                            EmployeeAccountGUI loginpage = new EmployeeAccountGUI(account_ID);
                        loginpage.setVisible(true);}
                        else {
                            AdminAccountGUI loginpage = new AdminAccountGUI(account_ID);
                            loginpage.setVisible(true);
                        }
                    }else
                    {JOptionPane.showMessageDialog(this,
                            "Password is incorrect",
                            "Login error ",
                            JOptionPane.ERROR_MESSAGE );
                        break;
                    }
                }
            }
            if (!accountFound)
            {JOptionPane.showMessageDialog(this,
                    "No Username found",
                    "Login error ",
                    JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception e) {
            System.out.println(e);

        }
        return passwordcorrect;
    }
}
