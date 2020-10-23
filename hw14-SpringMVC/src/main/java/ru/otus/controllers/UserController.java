package ru.otus.controllers;

import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.SearchForm;
import ru.otus.core.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping({"/", "/user/list"})
    public String userListView(Model model) {
        List<User> users = userDao.findByMask(null, null,null);
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("users", users);
        return "userList.html";
    }

    @PostMapping({"/user/search"})
    public String userSearchView(@ModelAttribute("searchForm") SearchForm searchForm, Model model) {
        List<User> users = userDao.findByMask(
                Objects.toString(searchForm.getId(), null),
                searchForm.getName(),
                searchForm.getLogin());
        model.addAttribute("users", users);
        return "userList.html";
    }

    @GetMapping("/user/edit/{id}")
    public ModelAndView editUserView(@PathVariable Long id) throws NotFoundException {
        Optional<User> optuser = userDao.findById(id);
        if (optuser.isPresent()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("userForm.html");
            User user = optuser.get();
            modelAndView.addObject("user", user);
            return modelAndView;
        } else {
            throw new NotFoundException("Not found user with ID " + id);
        }
    }

    @GetMapping("/user/new")
    public ModelAndView editUserView() throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userForm.html");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    // http://forum.thymeleaf.org/Fields-object-functions-Spring-td3302513.html#a3304174
    @PostMapping(value = "/user/edit", params = "action=submit")
    public ModelAndView editUserAction(@Valid @ModelAttribute("user") User userView, BindingResult errors, Model model) throws NotFoundException {

        ModelAndView modelAndView = new ModelAndView();

        if(errors.hasErrors()) {
                modelAndView.setViewName("userForm.html");
                modelAndView.addObject("user", userView);
                return modelAndView;
        }

        User user;
        if(userView.getId() != 0) {
            Optional<User>  optuser = userDao.findById(userView.getId());
            if (optuser.isPresent()) {
                user = optuser.get();
            } else {
                throw new NotFoundException("Not found user with ID " + userView.getId());
            }
        }
        else {
            user = new User();
        }

        user.setName(userView.getName());
        user.setAge(userView.getAge());
        user.setLogin(userView.getLogin());
        user.setPassword(userView.getPassword());
        userDao.insertOrUpdate(user);
        modelAndView.setViewName("redirect:/user/list");
        return modelAndView;
    }

    @PostMapping(value = "/user/edit", params = "action=cancel")
    public ModelAndView editUserAction() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/user/list");
        return modelAndView;
    }
}
