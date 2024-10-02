package de.lion5.spring.dvd.controller;

import de.lion5.spring.dvd.model.Actor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/authors")
public class AuthorController {
    @GetMapping
    public String getAuthors(Model model) {

        log.info("Client requested actors");
        return "authors";
    }
}
