package com.example.SpringDebtSlayer.Controllers;

import com.example.SpringDebtSlayer.Models.User;
import com.example.SpringDebtSlayer.Models.data.DebtDao;
import com.example.SpringDebtSlayer.Models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class AbstractController {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected DebtDao debtDao;

    public static final String userSessionKey = "user_id";

    protected User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        return userId == null ? null : userDao.findOne(userId);
    }

    protected void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @ModelAttribute("user")
    public User getUserForModel(HttpServletRequest request) {
        return getUserFromSession(request.getSession());
    }

}
