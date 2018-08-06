package com.example.SpringDebtSlayer.Controllers;

import com.example.SpringDebtSlayer.Models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "user")
public class UserController extends AbstractController {

    @RequestMapping(value = "")
    public String index(Model model, HttpServletRequest request) {

        User currentUser = getUserFromSession(request.getSession());

        model.addAttribute("user", currentUser);
        model.addAttribute("title", currentUser + "'s List of Debts");
        model.addAttribute("debts", debtDao.findByUser(currentUser));

        return "user/index";
    }
}

