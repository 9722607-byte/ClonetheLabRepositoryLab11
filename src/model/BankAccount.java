package model;

import exceptions.InvalidAmountException;

import java.util.Objects;

public class BankAccount {

    private String customerName;
    private String accountNumber;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    public BankAccount(String customerName, String accountNumber, Long userId, String userPassword) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.userPassword = userPassword;
        this.accountBalance = 0.0;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        if (accountBalance == null || accountBalance < 0) {
            throw new InvalidAmountException("Баланс рахунку не може бути від'ємним.");
        }
        this.accountBalance = accountBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Сума внеску повинна бути більшою за 0.");
        }
        this.accountBalance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Сума зняття повинна бути більшою за 0.");
        }
        if (amount > this.accountBalance) {
            throw new InvalidAmountException("Недостатньо коштів на рахунку.");
        }
        this.accountBalance -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount that)) return false;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}