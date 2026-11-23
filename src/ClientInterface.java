import exceptions.AccountNotFoundException;
import exceptions.DuplicateUserIdException;
import exceptions.InvalidAmountException;
import exceptions.InvalidPasswordException;
import exceptions.NonZeroBalanceException;
import model.BankAccount;
import model.CheckingAccount;
import java.util.Objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private final List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() throws IOException {
        while (true) {
            printMainMenu();
            String option = scanner.next();

            try {
                int choice = Integer.parseInt(option);
                switch (choice) {
                    case 1:
                        addNewAccount();
                        break;
                    case 2:
                        deleteAccount();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        printInfo();
                        break;
                    case 6:
                        return;
                    default:
                        System.err.println("Error: Please enter a valid choice (1 thru 6).\n");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Please enter a valid number (1 thru 6).\n");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8 || !password.matches(".*\\d.*")) {
            throw new InvalidPasswordException(
                    "Пароль повинен містити щонайменше 8 символів і хоча б одну цифру."
            );
        }
    }

    private Long parseUserId(String rawId) {
        try {
            return Long.parseLong(rawId);
        } catch (NumberFormatException e) {
            throw new AccountNotFoundException("Некоректний формат userId.");
        }
    }

    private double parseAmount(String rawAmount) {
        try {
            return Double.parseDouble(rawAmount.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Некоректний формат суми.");
        }
    }

    private BankAccount findAndAuthenticate(String rawUserId, String password) {
        Long userId = parseUserId(rawUserId);
        for (BankAccount account : accounts) {
            if (userId.equals(account.getUserId())) {
                if (!Objects.equals(account.getUserPassword(), password)) {
                    throw new InvalidPasswordException("Невірний пароль.");
                }
                return account;
            }
        }
        throw new AccountNotFoundException("Рахунок з userId " + userId + " не знайдено.");
    }

    private void addNewAccount() {
        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        System.out.print(">>> Enter customer name: ");
        var name = scanner.next();
        System.out.print(">>> Enter user id: ");
        var rawUserId = scanner.next();
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();
        System.out.print(">>> Confirm password: ");
        String confirmPassword = scanner.next();

        Long userId = parseUserId(rawUserId);
        for (BankAccount account : accounts) {
            if (userId.equals(account.getUserId())) {
                throw new DuplicateUserIdException("Рахунок з userId " + userId + " вже існує.");
            }
        }

        validatePassword(password);
        if (!Objects.equals(password, confirmPassword)) {
            throw new InvalidPasswordException("Пароль та підтвердження паролю не збігаються.");
        }

        String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        CheckingAccount newAccount = new CheckingAccount(name, accountNumber, userId, password);
        accounts.add(newAccount);

        System.out.println("Account successfully created.");
    }

    private void deleteAccount() {
        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        BankAccount account = findAndAuthenticate(userId, password);
        if (account.getAccountBalance() != 0.0) {
            throw new NonZeroBalanceException("Неможливо видалити рахунок з ненульовим балансом.");
        }

        accounts.remove(account);
        System.out.println("Account successfully deleted.");
    }

    private void deposit() {
        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var rawAmount = scanner.next();

        BankAccount account = findAndAuthenticate(userId, password);
        double amount = parseAmount(rawAmount);
        account.deposit(amount);

        System.out.println("Deposit successful. Current balance: " + account.getAccountBalance());
    }

    private void withdraw() {
        System.out.println("\n****************");
        System.out.println("Making a withdrawal: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var rawAmount = scanner.next();

        BankAccount account = findAndAuthenticate(userId, password);
        double amount = parseAmount(rawAmount);
        account.withdraw(amount);

        System.out.println("Withdrawal successful. Current balance: " + account.getAccountBalance());
    }

    private void printInfo() {
        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        BankAccount account = findAndAuthenticate(userId, password);
        double limit = (account instanceof CheckingAccount checking)
                ? checking.getDailyWithdrawalLimit()
                : 0.0;

        System.out.println("\nAccount info: ");
        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - account balance: " + account.getAccountBalance());
        System.out.println(" - account type: Checking");
        System.out.println(" - account daily withdrawal limit: " + limit);

        System.out.print("\nPress Enter to return to main menu...");

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
