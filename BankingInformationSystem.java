import java.util.*;

class User {
    private String name;
    private final String address;
    private String contactInfo;
    private final String accountNumber;
    private final String password;
    private double balance;
    private final List<String> transactionHistory;

    // Constructor
    public User(String name, String address, String contactInfo, String password, double initialDeposit) {
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.password = password;
        this.accountNumber = UUID.randomUUID().toString(); // Generate a unique account number
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.add("Initial Deposit: " + initialDeposit);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    // Methods for banking operations
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposit: " + amount + ", New Balance: " + balance);
            System.out.println("Deposit of " + amount + " was successful. New Balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrawal: " + amount + ", New Balance: " + balance);
            System.out.println("Withdrawal of " + amount + " was successful. New Balance: " + balance);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    public void transferFunds(User recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            recipient.balance += amount;
            this.transactionHistory.add("Transfer to " + recipient.getAccountNumber() + ": " + amount + ", New Balance: " + this.balance);
            recipient.transactionHistory.add("Transfer from " + this.accountNumber + ": " + amount + ", New Balance: " + recipient.balance);
            System.out.println("Transfer of " + amount + " to " + recipient.getAccountNumber() + " was successful.");
        } else {
            System.out.println("Invalid transfer amount or insufficient funds.");
        }
    }

    public void viewAccountStatement() {
        System.out.println("Account Statement for " + accountNumber + ":");
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Contact Information: " + contactInfo);
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}

class BankSystem {
    private final Map<String, User> users = new HashMap<>();

    // Register a new user
    public void registerUser(String name, String address, String contactInfo, String password, double initialDeposit) {
        User newUser = new User(name, address, contactInfo, password, initialDeposit);
        users.put(newUser.getAccountNumber(), newUser);
        System.out.println("User registered successfully. Your Account Number is: " + newUser.getAccountNumber());
    }

    // Login a user
    public User loginUser(String accountNumber, String password) {
        User user = users.get(accountNumber);
        if (user != null && user.authenticate(password)) {
            System.out.println("Login successful.");
            return user;
        } else {
            System.out.println("Login failed. Invalid account number or password.");
            return null;
        }
    }

    // Find a user by account number
    public User findUserByAccountNumber(String accountNumber) {
        return users.get(accountNumber);
    }
}

public class BankingInformationSystem {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            BankSystem bankSystem = new BankSystem();
            User loggedInUser = null;

            while (true) {
                System.out.println("\n--- Banking Information System ---");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Deposit");
                System.out.println("4. Withdraw");
                System.out.println("5. Transfer Funds");
                System.out.println("6. View Account Statement");
                System.out.println("7. Logout");
                System.out.println("8. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1 -> {
                        // User Registration
                        System.out.println("Enter your name:");
                        String name = scanner.nextLine();
                        System.out.println("Enter your address:");
                        String address = scanner.nextLine();
                        System.out.println("Enter your contact information:");
                        String contactInfo = scanner.nextLine();
                        System.out.println("Enter a password:");
                        String password = scanner.nextLine();
                        System.out.println("Enter initial deposit amount:");
                        double initialDeposit = scanner.nextDouble();
                        bankSystem.registerUser(name, address, contactInfo, password, initialDeposit);
                    }
                    case 2 -> {
                        // User Login
                        System.out.println("Enter your account number:");
                        String accountNumber = scanner.nextLine();
                        System.out.println("Enter your password:");
                        String pwd = scanner.nextLine();
                        loggedInUser = bankSystem.loginUser(accountNumber, pwd);
                    }
                    case 3 -> {
                        // Deposit
                        if (loggedInUser != null) {
                            System.out.println("Enter amount to deposit:");
                            double depositAmount = scanner.nextDouble();
                            loggedInUser.deposit(depositAmount);
                        } else {
                            System.out.println("Please login first.");
                        }
                    }
                    case 4 -> {
                        // Withdraw
                        if (loggedInUser != null) {
                            System.out.println("Enter amount to withdraw:");
                            double withdrawalAmount = scanner.nextDouble();
                            loggedInUser.withdraw(withdrawalAmount);
                        } else {
                            System.out.println("Please login first.");
                        }
                    }
                    case 5 -> {
                        // Transfer Funds
                        if (loggedInUser != null) {
                            System.out.println("Enter recipient's account number:");
                            String recipientAccountNumber = scanner.nextLine();
                            System.out.println("Enter amount to transfer:");
                            double transferAmount = scanner.nextDouble();
                            User recipient = bankSystem.findUserByAccountNumber(recipientAccountNumber);
                            if (recipient != null) {
                                loggedInUser.transferFunds(recipient, transferAmount);
                            } else {
                                System.out.println("Recipient not found.");
                            }
                        } else {
                            System.out.println("Please login first.");
                        }
                    }
                    case 6 -> {
                        // View Account Statement
                        if (loggedInUser != null) {
                            loggedInUser.viewAccountStatement();
                        } else {
                            System.out.println("Please login first.");
                        }
                    }
                    case 7 -> {
                        // Logout
                        loggedInUser = null;
                        System.out.println("Logged out successfully.");
                    }
                    case 8 -> {
                        // Exit
                        System.out.println("Thank you for using the Banking Information System. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
}
