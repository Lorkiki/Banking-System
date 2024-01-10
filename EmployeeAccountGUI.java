package Bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeAccountGUI extends JFrame {

        private JTextArea textArea;




        // Constructor for Temple.CustomerAccountGUI
        public EmployeeAccountGUI(int  account_ID) throws SQLException {
            // Create the account window
            setTitle("Employee Profile");
            setSize(500, 500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new FlowLayout()); // Set the layout
            textArea = new JTextArea(25, 30);



            String name ;
            String job ;


            Connection connection = Database.connection;
            String query = "SELECT * FROM Employee_account WHERE account_ID = '" + account_ID + "' ";
            Statement stm = connection.createStatement(); // Create statement
            ResultSet result = stm.executeQuery(query); // Execute the query
            while (result.next() ) {

                name = result.getString("name");
                textArea.append("Name: " + name + "\n");
                job = result.getString("job");
                textArea.append("Job: " + job + "\n");
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


            JButton listButton = new JButton("List Customer total amount");
            listButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int ID;
                    String name;
                    String address;
                    int count;
                    int total;
                    try {
                        Connection connection = Database.connection;
                        String query = "SELECT Bank_account.account_ID, customer_name,address, Count(*) bank_account_ID, SUM(Bank_account.money)" +
                                       "FROM Bank_account INNER JOIN Customer_accounts ON Bank_account.account_ID = Customer_accounts.account_ID GROUP BY Bank_account.account_ID";
                        Statement stm = connection.createStatement(); // Create statement
                        ResultSet result = stm.executeQuery(query); // Execute the query
                            while (result.next()) {
                                ID = result.getInt("account_ID");
                                textArea.append("Account ID: " + ID + "\n");
                                name = result.getString("customer_name");
                                textArea.append("Name: " + name + "\n");
                                address = result.getString("address");
                                textArea.append("Address: " + address + "\n");
                                count = result.getInt("bank_account_ID");
                                textArea.append("Number of bank account: " + count + "\n");
                                total = result.getInt("SUM(Bank_account.money)");
                                textArea.append("Total of money: " + total + "\n");
                                textArea.append("\n");
                            }
                        } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                });
            add(listButton);


            JButton listBankButton = new JButton("List Customer by Bank account");
            listBankButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int ID;
                    String name;
                    String type;
                    int money;

                    try {
                        Connection connection = Database.connection;
                        String query = "SELECT bank_account_ID, money, type, Customer_accounts.customer_name FROM Bank_account INNER JOIN Customer_accounts ON Bank_account.account_ID = Customer_accounts.account_ID ";
                        Statement stm = connection.createStatement(); // Create statement
                        ResultSet result = stm.executeQuery(query); // Execute the query
                        while (result.next()) {
                            ID = result.getInt("bank_account_ID");
                            textArea.append("Bank Account ID: " + ID + "\n");
                            name = result.getString("customer_name");
                            textArea.append("Name: " + name + "\n");
                            type = result.getString("type");
                            textArea.append("Type: " + type + "\n");
                            money = result.getInt("money");
                            textArea.append("Money: " + money + "\n");
                            textArea.append("\n");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            add(listBankButton);





            JButton resetButton = new JButton("Refresh");
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    dispose();
                    try {
                        EmployeeAccountGUI reset = new EmployeeAccountGUI(account_ID);
                        reset.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            add(resetButton);


            JButton deleteButton = new JButton("Delete bank account");
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    String accountNumber = JOptionPane.showInputDialog(
                            EmployeeAccountGUI.this,
                            "Type in bank account number to delete a bank account"
                    );

                        try {
                            Connection connection = Database.connection; // Connect to database
                            String query = "DELETE FROM Bank_account WHERE Bank_account_ID = '"+ accountNumber + "'" ;
                            PreparedStatement stm = connection.prepareStatement(query);
                            stm.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        {JOptionPane.showMessageDialog(EmployeeAccountGUI.this,
                                "Bank account is deleted",
                                "Completed ",
                                JOptionPane.ERROR_MESSAGE );
                        }
                    }});
            add(deleteButton);



            JButton CheckMessageButton = new JButton("Check Message");
            CheckMessageButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    ShowMessageGUI MessageGUI = new ShowMessageGUI();
                    MessageGUI.setVisible(true);
                }
            });
            add(CheckMessageButton);






















        }











    }



