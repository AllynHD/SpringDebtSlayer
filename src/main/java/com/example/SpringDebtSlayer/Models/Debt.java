package com.example.SpringDebtSlayer.Models;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import java.util.Comparator;

import static java.lang.String.format;


@Entity
public class Debt {

    @Id
    @GeneratedValue
    private int debtId;

    @NotNull
    @Size(min = 1, message = "Please enter a name.")
    private String name;

    @NotNull
    @Min(value = 0, message = "Balance must be at least $0.00")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double initialBalance;


    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Min(value = 1, message = "Please include a monthly payment amount. $0.00 is not acceptable.")
    private double monthlyPayment;

    @NumberFormat(pattern = "#0.00")
    private double interestRate = 0; //Make note on template that blank = 0
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double totalPaid = 0;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double totalInterest = 0;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double interestAmount;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double transfer = 0;



    private double currentBalance;


    @ManyToOne
    private User user;

    public Debt() {
    }


    public int getDebtId() {
        return debtId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double balance) {
        this.initialBalance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getMonthlyPayment() { return monthlyPayment; }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public void setInterestAmount(Debt debt) { //Can add term variability here eventually
        interestAmount = debt.getInterestRate() * debt.getCurrentBalance() / 1200;}

    public double getTransfer() {
        return transfer;
    }

    public void setTransfer(double transfer) {
        this.transfer = transfer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getCurrentBalance() { return currentBalance; }

    public void setCurrentBalance(double balance) { this.currentBalance = balance; }


    protected Debt makeFullPayment(Debt debt){ //could do a "make payment" method, then call each of these where they differ.
        double newBalance;
        setInterestAmount(debt);
        newBalance = currentBalance + interestAmount - monthlyPayment;
        totalInterest += interestAmount;
        totalPaid += monthlyPayment;
        currentBalance = newBalance;
        return debt;
    }

    protected Debt makeFinalPayment(Debt debt){
        setInterestAmount(debt);
        this.currentBalance = this.currentBalance + interestAmount;
        transfer = this.monthlyPayment - this.currentBalance;
        this.monthlyPayment = this.currentBalance;
        totalInterest += interestAmount;
        totalPaid += monthlyPayment;
        this.currentBalance = 0;
        return debt;
    }

    @Override
    public String toString() {

        return name +
                ": id = " + debtId +
                ", balance = $" + initialBalance +
                ", monthly payment = $" + monthlyPayment +
                ", interest rate = " + interestRate +
                "%, total paid = $" + totalPaid +
                ", total principal = $" + (totalPaid - totalInterest) +
                ", total interest = $" + totalInterest;
    }

    // Copy constructor
    public Debt (Debt original) {
        this.name = original.name;
        this.initialBalance = original.initialBalance;
        this.interestRate = original.interestRate;
        this.monthlyPayment = original.monthlyPayment;
        this.totalPaid = 0;
        this.totalInterest = 0;
        this.interestAmount = original.interestAmount;
        this.transfer = 0;

    }


    // For comparing based on current balance
    public static final Comparator<Debt> DebtBalanceComparator = new Comparator<Debt>() {
        @Override
        public int compare(Debt d1, Debt d2) {
            double DebtBalance1 = d1.getCurrentBalance();
            double DebtBalance2 = d2.getCurrentBalance();

            return (int) (DebtBalance1 - DebtBalance2);
        }
    };

    //For comparing based on interest rate

    /*public static final Comparator<Debt> DebtRateComparator = new Comparator<Debt>() {
        @Override
        public int compare(Debt d1, Debt d2) {
            double DebtRate1 = d1.getInterestRate();
            double DebtRate2 = d2.getInterestRate();

            return (int) (DebtRate1 - DebtRate2);
        }
    };  */



}