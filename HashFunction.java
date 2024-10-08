package Bank;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashFunction {




        // Method to hash a password using SHA-256
        public static String hashPassword(String password) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }

        // Method to check if the input password matches the hashed password
        public static boolean checkPassword(String inputPassword, String storedHash) throws NoSuchAlgorithmException {
            String inputHash = hashPassword(inputPassword);
            return storedHash.equals(inputHash);
        }

        public static void main(String[] args) {
            try {
                // Example usage
                String password = "system123";
                String hashedPassword = hashPassword(password);
                System.out.println("Hashed Password: " + hashedPassword);

                // Check if input matches the hashed password
                boolean isMatch = checkPassword("system123", hashedPassword);
                System.out.println("Password match: " + isMatch);

                boolean isWrongMatch = checkPassword("wrongPassword", hashedPassword);
                System.out.println("Password match with wrong password: " + isWrongMatch);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }


