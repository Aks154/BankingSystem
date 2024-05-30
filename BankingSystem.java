import java.util.*;

class Account {
    private String accountNumber;
    private double balance;
    private LinkedList<String> transactionHistory;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactionHistory = new LinkedList<>();
        this.transactionHistory.add("Account created with balance: $" + initialBalance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: $" + amount + " | New Balance: $" + balance);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdrew: $" + amount + " | New Balance: $" + balance);
            return true;
        } else {
            transactionHistory.add("Failed Withdrawal: $" + amount + " | Insufficient funds");
            return false;
        }
    }

    public LinkedList<String> getTransactionHistory() {
        return transactionHistory;
    }
}

class User {
    private String username;
    private Account[] accounts;
    private int accountCount;

    public User(String username, int maxAccounts) {
        this.username = username;
        this.accounts = new Account[maxAccounts];
        this.accountCount = 0;
    }

    public String getUsername() {
        return username;
    }

    public void addAccount(Account account) {
        if (accountCount < accounts.length) {
            accounts[accountCount++] = account;
        } else {
            System.out.println("Maximum number of accounts reached.");
        }
    }

    public Account getAccount(String accountNumber) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equals(accountNumber)) {
                return accounts[i];
            }
        }
        return null;
    }

    public Account[] getAccounts() {
        return Arrays.copyOf(accounts, accountCount);
    }
}

public class BankingSystem {
    private ArrayList<User> users;
    private Queue<String> transactions;
    private Scanner scanner;

    public BankingSystem() {
        users = new ArrayList<>();
        transactions = new LinkedList<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("Welcome to the Online Banking System");
            System.out.println("1. Create Account");
            System.out.println("2. View Account Details");
            System.out.println("3. Transfer Funds");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    viewAccountDetails();
                    break;
                case 3:
                    transferFunds();
                    break;
                case 4:
                    viewTransactionHistory();
                    break;
                case 5:
                    System.out.println("Thank you for using the Online Banking System.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine(); 

        User user = findOrCreateUser(username);
        user.addAccount(new Account(accountNumber, initialBalance));
        System.out.println("Account created successfully.");
    }

    private void viewAccountDetails() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        User user = findUser(username);
        if (user != null) {
            Account account = user.getAccount(accountNumber);
            if (account != null) {
                System.out.println("Account Number: " + account.getAccountNumber());
                System.out.println("Balance: $" + account.getBalance());
            } else {
                System.out.println("Account not found.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void transferFunds() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter source account number: ");
        String sourceAccountNumber = scanner.nextLine();
        System.out.print("Enter destination account number: ");
        String destinationAccountNumber = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); 

        User user = findUser(username);
        if (user != null) {
            Account sourceAccount = user.getAccount(sourceAccountNumber);
            Account destinationAccount = user.getAccount(destinationAccountNumber);

            if (sourceAccount != null && destinationAccount != null) {
                if (sourceAccount.withdraw(amount)) {
                    destinationAccount.deposit(amount);
                    transactions.add("Transferred $" + amount + " from " + sourceAccountNumber + " to " + destinationAccountNumber);
                    System.out.println("Transfer successful.");
                } else {
                    System.out.println("Insufficient funds in source account.");
                }
            } else {
                System.out.println("Invalid account details.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void viewTransactionHistory() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        User user = findUser(username);
        if (user != null) {
            Account account = user.getAccount(accountNumber);
            if (account != null) {
                System.out.println("Transaction History for Account Number: " + account.getAccountNumber());
                for (String transaction : account.getTransactionHistory()) {
                    System.out.println(transaction);
                }
            } else {
                System.out.println("Account not found.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private User findOrCreateUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        User newUser = new User(username, 10); 
        users.add(newUser);
        return newUser;
    }

    private User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        bankingSystem.start();
    }
}