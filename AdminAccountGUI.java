package Bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class AdminAccountGUI extends JFrame {
        private JTextArea textArea;

        public AdminAccountGUI(int  account_ID) throws SQLException {
            // Create the account window
            setTitle("Admin Profile");
            setSize(500, 500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new FlowLayout()); // Set the layout
            textArea = new JTextArea(25, 30);

            String name ;

            Connection connection = Database.connection;
            String query = "SELECT * FROM Admin_account WHERE account_ID = '" + account_ID + "' ";
            Statement stm = connection.createStatement(); // Create statement
            ResultSet result = stm.executeQuery(query); // Execute the query
            while (result.next() ) {
                name = result.getString("Admin_name");
                textArea.append("You login as: " + name + "\n");
                textArea.append("Play for Safety!" + "\n");
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


            JButton resetButton = new JButton("Refresh");
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        AdminAccountGUI reset = new AdminAccountGUI(account_ID);
                        reset.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            add(resetButton);


            JButton deleteButton = new JButton("Delete Customer account");
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    String accountNumber = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "Type in account number to delete a customer account"
                    );

                    int accountID = Integer.parseInt(accountNumber);
                    List<Integer> bankID = new ArrayList<>();

                    try{
                        Connection connection = Database.connection;
                        String query = "SELECT \n" +
                                "Bank_account.account_ID,bank_account_ID \n" +
                                "FROM Bank_account \n" +
                                "INNER JOIN Customer_accounts \n" +
                                "ON Bank_account.account_ID = Customer_accounts.account_ID \n" +
                                "WHERE Bank_account.account_ID = ' "+ accountNumber +" '  ";
                        Statement stm = connection.createStatement(); // Create statement
                        ResultSet result = stm.executeQuery(query); // Execute the query
                        while (result.next() ) {
                            accountID = result.getInt("account_ID");
                            bankID.add(result
                                    .getInt("bank_account_ID"));
                        }
                    }catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    StringBuilder queryBuilder = new StringBuilder("DELETE  FROM Bank_account WHERE bank_account_ID IN (");
                    for (int i = 0; i < bankID.size(); i++) {
                        queryBuilder.append("?");
                        if (i < bankID.size() - 1) {
                            queryBuilder.append(",");
                        }
                    }
                    queryBuilder.append(")");
                    String sql = queryBuilder.toString();
                    try {
                        Connection connection = Database.connection; // Connect to database
                        PreparedStatement stm = connection.prepareStatement(sql);
                        {
                            for (int i = 0; i < bankID.size(); i++) {
                                stm.setInt(i + 1, bankID.get(i));
                            }
                            stm.executeUpdate();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    try {
                        Connection connection = Database.connection; // Connect to database
                        String query = "DELETE FROM Customer_accounts WHERE account_ID = '"+ accountID + "'" ;
                        PreparedStatement stm = connection.prepareStatement(query);
                        stm.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    {JOptionPane.showMessageDialog(AdminAccountGUI.this,
                            "ALL bank account and customer account are delete",
                            "Completed ",
                            JOptionPane.ERROR_MESSAGE );
                    }
                }
                });
            add(deleteButton);



            JButton deleteEMButton = new JButton("Delete Employee account");
            deleteEMButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    String accountNumber = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "Type in account number to delete a Employee account"
                    );

                    int accountID = Integer.parseInt(accountNumber);
                    try {
                        Connection connection = Database.connection; // Connect to database
                        String query = "DELETE FROM Employee_account WHERE account_ID = '"+ accountID + "'" ;
                        PreparedStatement stm = connection.prepareStatement(query);
                        stm.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    {JOptionPane.showMessageDialog(AdminAccountGUI.this,
                            "Employee account is delete",
                            "Completed ",
                            JOptionPane.ERROR_MESSAGE );
                    }
                }
            });
            add(deleteEMButton);

            JButton addEmployeeAccountButton = new JButton("Create Employee Account");
            addEmployeeAccountButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CreateAccountGUI create = new CreateAccountGUI("Employee");
                    create.setVisible(true);
                }
            });
            add(addEmployeeAccountButton);


            JButton addEditInfoEM = new JButton("Edit information of Employee");
            addEditInfoEM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int ID;
                    String name;
                    String username;
                    String password;
                    try {
                        Connection connection = Database.connection;
                        String query = "SELECT account_ID, name, username, password FROM Employee_account ";
                                Statement stm = connection.createStatement(); // Create statement
                                ResultSet result = stm.executeQuery(query); // Execute the query
                                while (result.next()) {
                                    ID = result.getInt("account_ID");
                                    textArea.append("Account ID: " + ID + "\n");
                                    name = result.getString("name");
                                    textArea.append("Name: " + name + "\n");
                                    username = result.getString("username");
                                    textArea.append("Username: " + username + "\n");
                                    password = result.getString("password");
                                    textArea.append("Password: " + password + "\n");
                                    textArea.append("\n");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                    String account_id = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "Which account do you want to edit? Type in account ID"
                    );
                    String new_name = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "What is new name for this account?"
                    );
                    String new_username = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "What is new username for this account?"
                    );
                    String new_password = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "What is new password for this account?"
                    );

                    try {
                        Connection connection = Database.connection;
                        String query = "UPDATE Employee_account SET name = ?, username = ?, password = ? WHERE account_ID = ?";
                        PreparedStatement stm = connection.prepareStatement(query);
                        stm.setString(1, new_name);
                        stm.setString(2, new_username);
                        stm.setString(3, new_password);
                        stm.setInt(4, Integer.parseInt(account_id));
                        stm.executeUpdate();

                        JOptionPane.showMessageDialog(AdminAccountGUI.this,
                                "Edit completed",
                                "Completed ",
                                JOptionPane.ERROR_MESSAGE );
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            add(addEditInfoEM);


            JButton addEditInfoCS = new JButton("Edit information of Customer");
            addEditInfoCS.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int ID;
                    String name;
                    String username;
                    String password;
                    String address;
                    try {
                        Connection connection = Database.connection;
                        String query = "SELECT account_ID, customer_name, username, password, address FROM Customer_accounts ";
                        Statement stm = connection.createStatement(); // Create statement
                        ResultSet result = stm.executeQuery(query); // Execute the query
                        while (result.next()) {
                            ID = result.getInt("account_ID");
                            textArea.append("Account ID: " + ID + "\n");
                            name = result.getString("customer_name");
                            textArea.append("Name: " + name + "\n");
                            username = result.getString("username");
                            textArea.append("Username: " + username + "\n");
                            password = result.getString("password");
                            textArea.append("Password: " + password + "\n");
                            address = result.getString("address");
                            textArea.append("Address: " + address + "\n");
                            textArea.append("\n");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    String account_id = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "Which account do you want to edit? Type in account ID"
                    );
                    String new_name = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "What is new name for this account?"
                    );
                    String new_username = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "What is new username for this account?"
                    );
                    String new_password = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "What is new password for this account?"
                    );
                    String new_address = JOptionPane.showInputDialog(
                            AdminAccountGUI.this,
                            "What is new address for this account?"
                    );


                    try {
                        Connection connection = Database.connection;
                        String query = "UPDATE Customer_accounts SET customer_name = ?, username = ?, password = ?, address = ? WHERE account_ID = ?";
                        PreparedStatement stm = connection.prepareStatement(query);
                        stm.setString(1, new_name);
                        stm.setString(2, new_username);
                        stm.setString(3, new_password);
                        stm.setString(4, new_address);
                        stm.setInt(5, Integer.parseInt(account_id));
                        stm.executeUpdate();

                        JOptionPane.showMessageDialog(AdminAccountGUI.this,
                                "Edit completed",
                                "Completed ",
                                JOptionPane.ERROR_MESSAGE );
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            add(addEditInfoCS);








        }

}









