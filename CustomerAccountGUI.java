package Bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerAccountGUI extends JFrame {

    private JTextArea textArea;




    // Constructor for Temple.CustomerAccountGUI
    public CustomerAccountGUI(int customer_id) throws SQLException {
        // Create the account window
        setTitle("Client Profile");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout()); // Set the layout

        textArea = new JTextArea(25, 30);


        int number;
        String name ;
        int money = 0;
        String address ;
        String type ;


        Connection connection = Database.connection;
        String query = "SELECT * FROM Customer_accounts INNER JOIN Bank_account ON Customer_accounts.account_ID = Bank_account.account_ID WHERE Customer_accounts.account_ID = '" + customer_id + "' ";
        Statement stm = connection.createStatement(); // Create statement
        ResultSet result = stm.executeQuery(query); // Execute the query
        while (result.next() ) {

            number = result.getInt("bank_account_ID");
            textArea.append("Bank Account number: " + number + "\n");
            name = result.getString("customer_name");
            textArea.append("Name: " + name + "\n");
            address = result.getString("address");
            textArea.append("Address: " + address + "\n");
            money = result.getInt("money");
            textArea.append("Money: " + money + "\n");
            type = result.getString("type");
            textArea.append("type: " + type + "\n");
            textArea.append("\n");

        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);


        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginGUI set = new LoginGUI();
                set.setVisible(true);
            }
        });
        add(logoutButton);


        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNumber = JOptionPane.showInputDialog(
                        CustomerAccountGUI.this,
                        "Which bank account do you want to deposit?"
                );
                String amount = JOptionPane.showInputDialog(
                        CustomerAccountGUI.this,
                        "How much do you want to deposit?"
                );
                try{
                Connection connection = Database.connection;
                String query = "UPDATE Bank_account SET money = money + ? WHERE bank_account_ID = '" + accountNumber +"' ";
                PreparedStatement stm = connection.prepareStatement(query);
                stm.setInt(1, Integer.parseInt(amount));
                stm.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                {JOptionPane.showMessageDialog(CustomerAccountGUI.this,
                        "Money Deposit successfully, Please click refresh button",
                        "Completed ",
                        JOptionPane.ERROR_MESSAGE );
                }

            }
        });
        add(depositButton);


        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNumber = JOptionPane.showInputDialog(
                        CustomerAccountGUI.this,
                        "Which bank account do you want to withdraw?"
                );
                String amount = JOptionPane.showInputDialog(
                        CustomerAccountGUI.this,
                        "How much do you want to withdraw?"
                );


                try{
                    Connection connection = Database.connection;
                    String query = "UPDATE Bank_account SET money = money - ? WHERE bank_account_ID = '" + accountNumber +"' ";
                    PreparedStatement stm = connection.prepareStatement(query);
                    stm.setInt(1, Integer.parseInt(amount));
                    stm.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                {JOptionPane.showMessageDialog(CustomerAccountGUI.this,
                        "Money withdraw successfully, Please click refresh button",
                        "Completed ",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
        });
        add(withdrawButton);




        JButton addEdit = new JButton("Edit information ");
        addEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String new_name = JOptionPane.showInputDialog(
                        CustomerAccountGUI.this,
                        "What is new name for this account?"
                );
                String new_address = JOptionPane.showInputDialog(
                        CustomerAccountGUI.this,
                        "What is new address for this account?"
                );
                try {
                    Connection connection = Database.connection;
                    String query = "UPDATE Customer_accounts SET customer_name = ?, address = ? WHERE account_ID =  '"+ customer_id  +" '";
                    PreparedStatement stm = connection.prepareStatement(query);
                    stm.setString(1, new_name);
                    stm.setString(2, new_address);
                    stm.executeUpdate();

                    JOptionPane.showMessageDialog(CustomerAccountGUI.this,
                            "Edit completed",
                            "Completed ",
                            JOptionPane.ERROR_MESSAGE );
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(addEdit);

        JButton requestButton = new JButton("Request open Ban account");
        requestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String permission = null;
                try{
                    Connection connection = Database.connection;
                    String query = "SELECT Permission FROM Customer_accounts WHERE account_ID = '"+ customer_id +"' ";
                    Statement stm = connection.createStatement(); // Create statement
                    ResultSet result = stm.executeQuery(query); // Execute the query
                    while (result.next()) {
                        permission = result.getString("Permission");
                    }
                } catch (Exception ex) {
                    System.out.println(e);
                }
                if(permission.equalsIgnoreCase("No"))
                {JOptionPane.showMessageDialog(CustomerAccountGUI.this,
                        "You don't have permission to open an account. Talk to Employee",
                        "Error ",
                        JOptionPane.ERROR_MESSAGE );

                    int choice = JOptionPane.showConfirmDialog(null,
                            "Do you want to send request to Employee?",
                            "Choose an option",
                            JOptionPane.YES_NO_OPTION);

                    // Check the user's choice and perform actions based on it
                    if (choice == JOptionPane.YES_OPTION) {
                        try {
                            Connection connection = Database.connection;
                            String query = "INSERT INTO Message_box (content, account_ID, Employee_ID) VALUES (?, ?, ?)";
                            PreparedStatement stm = connection.prepareStatement(query);
                            stm.setString(1, "Customer want to open a bank account");
                            stm.setInt(2, customer_id);
                            stm.setInt(3, 1);
                            stm.executeUpdate();
                            JOptionPane.showMessageDialog(
                                    CustomerAccountGUI.this, "Request Send", "Request Send", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            System.out.println(e);
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(CustomerAccountGUI.this,
                            "Employee approved your request!",
                            "Congratulation! ",
                            JOptionPane.ERROR_MESSAGE );
                    String type = JOptionPane.showInputDialog(
                            CustomerAccountGUI.this,
                            "What type of bank account do you want to open?"
                    );
                    String money = JOptionPane.showInputDialog(
                            CustomerAccountGUI.this,
                            "How much do you want to deposit at your first time?"
                    );
                    try {
                        Connection connection = Database.connection;
                        String query = "INSERT INTO bank_account (money, type, account_ID) VALUES ( ?, ?, ?) ";
                        PreparedStatement stm = connection.prepareStatement(query);
                        stm.setInt(1, Integer.parseInt(money));
                        stm.setString(2, type);
                        stm.setInt(3, customer_id);
                        stm.executeUpdate();
                        JOptionPane.showMessageDialog(
                                CustomerAccountGUI.this, "Bank Account Created", "Bank Account Created ", JOptionPane.ERROR_MESSAGE);

                    } catch (Exception ex) {
                        System.out.println(e);
                    }

                }
            }
        });
        add(requestButton);





        JButton resetButton = new JButton("Refresh");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    CustomerAccountGUI reset = new CustomerAccountGUI(customer_id);
                    reset.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(resetButton);
























    }











}

