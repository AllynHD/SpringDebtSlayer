package com.example.SpringDebtSlayer.Models;

import java.util.*;

public class Snowball {

    // Method for running one "month" of payments
    public static User singleSnowball(User user) {

        // Initialize variables and objects
        List<Debt> debtList = user.getDebts();


        // Loop through individual debts, make single payment, recalibrate payments as needed.
        for (Debt debt : debtList) {
            if (debt.getCurrentBalance() == 0) {                   // For paid-down debts
                if (debt.getMonthlyPayment() == 0) {
                    debt.setTransfer(0);
                    continue;
                } else {                                    // For recently-paid-down debts, to finish up
                    int setup = debtList.indexOf(debt);
                    if (setup < debtList.size() - 1) {         // Guarantees current debt is not the last in the list - Causes problems in Regular method
                        debt.setTransfer(debt.getMonthlyPayment());
                        debt.setMonthlyPayment(0);
                        Debt nextDebt = debtList.get(setup + 1);
                        nextDebt.setMonthlyPayment(nextDebt.getMonthlyPayment() + debt.getTransfer());    // Transfers leftover payment amount to next debt
                    }

                    continue;
                }

            // For active debts
            } else if (debt.getMonthlyPayment() < debt.getCurrentBalance() * (1 + debt.getInterestRate() / 12)) {     // Confirms enough left in debt to require additional payment after this month

                Debt newDebt = debt.makeFullPayment(debt);
            } else {
                Debt newDebt = debt.makeFinalPayment(debt); // Makes final payment
                int setup = debtList.indexOf(debt);
                if (setup < debtList.size() - 1) {
                    Debt nextDebt = debtList.get(setup + 1);
                    nextDebt.setMonthlyPayment(nextDebt.getMonthlyPayment() + debt.getTransfer());        // Transfers leftover payment amount to next debt for current month usage
                }
            }
        }

        return user;
    }


    // For modeling the payment of all debts down to $0
    public static User payAllDebtsInFull(User user) {
        //Integer[] keyArray = {0};                           // Later, will be used to count how many debts are paid in full
        boolean check;
        List<Debt> debtList = user.getDebts();
        for (Debt debt : debtList) {
            debt.setCurrentBalance(debt.getInitialBalance());
        }

        debtList.sort(Debt.DebtBalanceComparator);          // Sorts debtList from lowest balance to highest balance

        int count = 0;                                      // counts number of months required

        int length = debtList.size();                       // Check-value for how many debts are in the list


        do {                                                // Initializes modeling of payment cycles
            Integer key = 0;
            user = singleSnowball(user);                    // Runs debts through a single payment cycle
            for (Debt debt : debtList) {
                if (debt.getCurrentBalance() == 0) {
                    key += 1;
                }
            }
            check = (key < debtList.size());                // Is key less than size of DebtList? If True, keep going
            count += 1;
        }

        while (check);                                      // Check no longer true means list of paid-off finally matches list of total

        user.setMonths(count);
        return user;                                     // Sends info back to Main method
    }
}
