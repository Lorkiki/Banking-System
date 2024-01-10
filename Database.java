package Bank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
	public static Connection connection;
	
	public static void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/Bank?serverTimezone=EST", "root", "lorki996");
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	public static void main(String [] args) {
		Database.connect();
		// getCustomers();
		// setMoney();
		// getCustomers();
		// listcustomer();
		delete();
	}

	public static void getCustomers() {
		try {
			Connection connection = Database.connection; // Connect to database
			String query = "SELECT * FROM Bank_account WHERE bank_account_ID = 1 " ;
					//WHERE patient_name = '" + " Thomas Tank " + "'  "; // Enter the query
			Statement stm = connection.createStatement(); // Create statement
			ResultSet result = stm.executeQuery(query); // Execute the query

			while (result.next()) {
				System.out.println("Bank ID: " + result.getInt("bank_account_ID"));
				System.out.println("Money: " + result.getInt("money"));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void setMoney() {
		try {
			int accountNumber = 1;
			String amount = "100";
			Connection connection = Database.connection;
			String query = "UPDATE Bank_account SET money =  ? WHERE bank_account_ID = 1";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, 100);
			stm.executeUpdate();

		}catch (SQLException ex) {
		ex.printStackTrace();

		}

	}










	public static void listcustomer() {
		int ID = 0;
		String name = "null";
		try {
			Connection connection = Database.connection; // Connect to database
			String query = "SELECT Bank_account.account_ID,customer_name,address, Count(*) bank_account_ID, SUM(Bank_account.money)" +
				        	"FROM Bank_account INNER JOIN Customer_accounts ON Bank_account.account_ID = Customer_accounts.account_ID GROUP BY Bank_account.account_ID";
			//WHERE patient_name = '" + " Thomas Tank " + "'  "; // Enter the query
			Statement stm = connection.createStatement(); // Create statement
			ResultSet result = stm.executeQuery(query); // Execute the query

			while (result.next()) {
				System.out.println(" ID: " + result.getInt("account_ID"));
				System.out.println("Name: " + result.getString("customer_name"));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	public static void delete(){


			int accountID = 5;
			List<Integer> bankID = new ArrayList<>();

			try{
				Connection connection = Database.connection;
				String query = "SELECT \n" +
						"Bank_account.account_ID,bank_account_ID \n" +
						"FROM Bank_account \n" +
						"INNER JOIN Customer_accounts \n" +
						"ON Bank_account.account_ID = Customer_accounts.account_ID \n" +
						"WHERE Bank_account.account_ID = ' "+ accountID +" '  ";
				Statement stm = connection.createStatement(); // Create statement
				ResultSet result = stm.executeQuery(query); // Execute the query
				while (result.next() ) {
					accountID = result.getInt("account_ID");
					bankID.add(result.getInt("bank_account_ID"));
				}
				System.out.println(accountID);
				for (int i = 0; i < bankID.size(); i++) {
					System.out.println(bankID.get(i));
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
		System.out.println(sql);

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



		}


}






