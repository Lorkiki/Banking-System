package Bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowMessageGUI extends JFrame  {

    private JTextArea textArea;

    public  ShowMessageGUI() {

            setTitle("Show all the message");
            setSize(500, 500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new FlowLayout()); // Set the layout

            textArea = new JTextArea(25, 30);

        int message_ID;
        String content;
        String customer_name;
        int customer_ID;
        try {
            Connection connection = Database.connection;
            String query = "SELECT * FROM Message_box INNER JOIN Customer_accounts ON Message_box.account_ID = Customer_accounts.account_ID    ";
            Statement stm = connection.createStatement(); // Create statement
            ResultSet result = stm.executeQuery(query); // Execute the query
            while (result.next()) {
                message_ID = result.getInt("message_ID");
                textArea.append("Message ID " + message_ID + "\n");
                content = result.getString("content");
                textArea.append("Content: " + content + "\n");
                customer_name = result.getString("customer_name");
                textArea.append("Customer name: " + customer_name + "\n");
                customer_ID = result.getInt("account_ID");
                textArea.append("Customer ID: " + customer_ID + "\n");
                textArea.append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        JButton approveButton = new JButton("Approve an request");
        approveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String permission = null;
                String account_ID = JOptionPane.showInputDialog(
                        ShowMessageGUI.this,
                        "Type in account number to approve a request."
                );

                try {
                    Connection connection = Database.connection;
                    String query = "UPDATE Customer_accounts SET Permission = ? WHERE account_ID = '" + account_ID + "'";
                    PreparedStatement stm = connection.prepareStatement(query);

                    stm.setString(1, "Yes"); // appointment_time (first question mark)
                    stm.executeUpdate();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                JOptionPane.showMessageDialog(ShowMessageGUI.this,
                        "Approve message send ",
                        "Approve message send",
                        JOptionPane.ERROR_MESSAGE );

            }});
        add(approveButton);


        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }});
        add(exitButton);




            JScrollPane scrollPane = new JScrollPane(textArea);
            add(scrollPane);

    }}































