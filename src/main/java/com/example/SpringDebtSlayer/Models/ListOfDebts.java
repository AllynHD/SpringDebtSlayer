package com.example.SpringDebtSlayer.Models;


import java.util.*;

public class ListOfDebts {

    protected static User payAllDebtsOnce(User user) {

        List<Debt> debtList = user.getDebts();


        for (Debt debt : debtList) {
            if (debt.getCurrentBalance() == 0) {
                continue;

            } else if (debt.getMonthlyPayment() < debt.getCurrentBalance() * (1 + debt.getInterestRate() / 1200)) {

                debt = debt.makeFullPayment(debt);
            } else {
                debt = debt.makeFinalPayment(debt);
            }
        }


        return user;
    }

    public static User payAllDebtsInFull(User user) {
        boolean check;
        List<Debt> debtList = user.getDebts();
        for (Debt debt : debtList) {
            debt.setCurrentBalance(debt.getInitialBalance());
        }

        debtList.sort(Debt.DebtBalanceComparator);

        int count = 0;      // counts number of months required

        do {
            Integer key = 0;
            user = payAllDebtsOnce(user);
            for (Debt debt : debtList) {
                if (debt.getCurrentBalance() == 0) {
                    key += 1;
                }

            }
            check = (key < debtList.size());    // Is key less than size of DebtList? If True, keep going
            count += 1;

        }
        while (check);

        user.setMonths(count);
        return user;
    }
}

