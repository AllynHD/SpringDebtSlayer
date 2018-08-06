package com.example.SpringDebtSlayer.Controllers;


import com.example.SpringDebtSlayer.Models.Debt;
import com.example.SpringDebtSlayer.Models.ListOfDebts;
import com.example.SpringDebtSlayer.Models.Snowball;
import com.example.SpringDebtSlayer.Models.User;
import com.example.SpringDebtSlayer.Models.data.DebtDao;
import com.example.SpringDebtSlayer.Models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("debts")
public class DebtController extends AbstractController {



    static ArrayList<Debt> listOfDebts = new ArrayList<>();


    // Should take in a user (by id) and display a list of that user's debts, with options to add, edit, remove, or run model
    // Handler for debts/user/{userid}
    @RequestMapping(value = "user/{username}")
    public String index(Model model, @PathVariable String username) {

        User user = userDao.findByUsername(username);

        model.addAttribute("title", username + "'s list of debts");
        model.addAttribute("listOfDebts", user.getDebts());
        return "redirect:/user";
    }


    // After clicking "Add Debt" button, comes here to allow a user to add a debt to their list
    // Handler for debts/add, GET
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddDebtForm(Model model, HttpServletRequest request) {

        User currentUser = getUserFromSession(request.getSession());


        model.addAttribute("title", "Add Debt to List");
        model.addAttribute("user", currentUser);
        model.addAttribute("debt", new Debt());

        return "debts/add";
    }


    // processes Add Debt form
    // Handler for debts/add, POST
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String displayDebts (Model model, @ModelAttribute @Valid Debt debt, Errors errors, HttpServletRequest request) {

        User user = getUserFromSession(request.getSession());
        debt.setUser(user);

        if (errors.hasErrors()) {

            model.addAttribute("title", "Add Debt to List");
            model.addAttribute("debt", debt);
            model.addAttribute("errors", errors);
            model.addAttribute("user", getUserForModel(request));
            return "debts/add";

        } else {
            debtDao.save(debt);
            model.addAttribute("title", debt.getUser().getUsername() + "'s Debt List");
            model.addAttribute("listOfDebts", debt.getUser().getDebts());
            return "redirect:/debts/user/" + debt.getUser().getUsername();
        }
    }


    // Handler for debts/id/{debtId}, GET
    @RequestMapping(value = "id/{debtId}", method = RequestMethod.GET)
    public String editDebts (Model model, @PathVariable int debtId) {

        model.addAttribute("title", "Edit or Remove Debt");
        model.addAttribute("debt", debtDao.findOne(debtId));
        model.addAttribute("users", userDao.findAll());


        return "debts/edit";
    }

    // For editing debts that already exist
    // Handler for debts/id/{debtId}, POST
    @RequestMapping(value = "id/{debtId}", method = RequestMethod.POST)
    public String editDebts (Model model, @PathVariable int debtId, @ModelAttribute @Valid Debt debt, Errors errors) {

        if (errors.hasErrors()) {

            model.addAttribute("title", "Add Debt to List");
            model.addAttribute("users", userDao.findAll());
            model.addAttribute("debt", debt);
            model.addAttribute("errors", errors);
            return "debts/edit";
        } else {

        Debt oldDebt = debtDao.findOne(debtId);
        oldDebt.setInitialBalance(debt.getInitialBalance());
        oldDebt.setMonthlyPayment(debt.getMonthlyPayment());
        oldDebt.setInterestRate(debt.getInterestRate());
        oldDebt.setUser(debt.getUser());
        oldDebt.setName(debt.getName());
        debtDao.save(oldDebt);
        model.addAttribute("listOfDebts", oldDebt.getUser().getDebts());
        model.addAttribute("title", debt.getUser().getUsername() + "'s List of Debts");

        return "redirect:/debts/user/"+debt.getUser().getUsername();
        }
    }

    // Handler for debts/id/{debtId}, POST + "remove" param
    @RequestMapping(value = "id/{debtId}", method = RequestMethod.POST, params = "remove")
    public String removeDebts (Model model, @PathVariable int debtId, @ModelAttribute @Valid Debt debt, @RequestParam ("remove") String remove) {

        if (remove.equals("true")) {
            debtDao.delete(debtDao.findOne(debtId));

            return "redirect:/debts/user/" + debt.getUser().getUsername();
        } else {

            model.addAttribute("title", "Something wrong with your edit. Please don't hack me.");
            return "debts/edit";
        }

    }

        // Displays total amount paid and number of months for all the logged-in user's debts.

    // Handler for debts/paydown
    @RequestMapping(value = "paydown")
    public String displayPaydownInformation(Model model, @RequestParam String paydown, HttpServletRequest request) {

        User currentUser = getUserFromSession(request.getSession());
        ArrayList<Debt> listOfDebts = debtDao.findByUser(currentUser);

        if (paydown.equals("minimum")) {
            currentUser = ListOfDebts.payAllDebtsInFull(currentUser);


        } else if (paydown.equals("snowball")){
            currentUser = Snowball.payAllDebtsInFull(currentUser);

        }

        double totalPaid = 0;
        int months = currentUser.getMonths();

        for (Debt debt : currentUser.getDebts()) {
            totalPaid += debt.getTotalPaid();
        }

        model.addAttribute("totalPaid", totalPaid);
        model.addAttribute("months", months);
        model.addAttribute("title", "Slay the Beast");
        model.addAttribute("user", currentUser);

        return "debts/paydown";
    }
}
