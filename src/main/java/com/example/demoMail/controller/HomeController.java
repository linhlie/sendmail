package com.example.demoMail.controller;


import com.example.demoMail.dao.EmailDao;
import com.example.demoMail.model.Email;
import com.example.demoMail.model.PagerModel;
import com.example.demoMail.service.EmailService;
import com.example.demoMail.service.FetchMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class HomeController {
    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10};

    @Autowired
    private EmailService emailService;

    @Autowired
    FetchMail fecthMail;

    @Autowired
    EmailDao emailDao;
//
    @GetMapping("/list")
    public ModelAndView homepage(@RequestParam("pageSize") Optional<Integer> pageSize,
                                 @RequestParam("page") Optional<Integer> page) {
        if (emailDao.count() != 0) {
            ;//pass
        } else {
            emailDao.findAll();
        }
        fecthMail.fetch();

        ModelAndView modelAndView = new ModelAndView("list");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        // print repo
        System.out.println("here is client repo " + emailDao.findAll());
        Page<Email> list = (Page<Email>) emailDao.findAll(new PageRequest(evalPage, evalPageSize));
        System.out.println("client list get total pages" + list.getTotalPages() + "client list get number " + list.getNumber());
        PagerModel pager = new PagerModel(list.getTotalPages(), list.getNumber(), BUTTONS_TO_SHOW);
        // add clientmodel
        modelAndView.addObject("list", list);
        // evaluate page size
        modelAndView.addObject("selectedPageSize", evalPageSize);
        // add page sizes
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        // add pager
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }


    @GetMapping("/email/{id}/delete")
    public String delete(@PathVariable int id, RedirectAttributes redirect) {
        emailService.delete(id);
        redirect.addFlashAttribute("success", "Deleted employee successfully!");
        return "redirect:/list";
    }
    @GetMapping("/demo/{id}")
    public String thismail(@PathVariable int id, Model model) {
        model.addAttribute("list", emailService.getById(id));
        return "demo";
    }

    @GetMapping("/texteditor")
    public String thismail() {
        return "texteditor";
    }

}
