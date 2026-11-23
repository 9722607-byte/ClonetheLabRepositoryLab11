package model;

import exceptions.InvalidAmountException;

public class CheckingAccount extends BankAccount {

    private double dailyWithdrawalLimit = 300.0;

    public CheckingAccount(String customerName, String accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        if (dailyWithdrawalLimit < 0) {
            throw new InvalidAmountException("Денний ліміт не може бути від'ємним.");
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Сума зняття повинна бути більшою за 0.");
        }
        if (amount > dailyWithdrawalLimit) {
            throw new InvalidAmountException(
                    "Сума зняття перевищує денний ліміт (" + dailyWithdrawalLimit + ")."
            );
        }
        super.withdraw(amount);
    }
}