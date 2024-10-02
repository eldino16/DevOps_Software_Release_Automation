package de.lion5.spring.dvd.controller;

import de.lion5.spring.dvd.model.Actor;
import de.lion5.spring.dvd.model.Movie;
import de.lion5.spring.dvd.properties.ActorProperties;
import de.lion5.spring.dvd.repository.ActorRepository;
import de.lion5.spring.dvd.service.ActorService;
import de.lion5.spring.dvd.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/actors")
public class ActorController {
    private final ActorService actorService;
    private final ActorProperties props;
    private final ActorRepository actorRepository;

    public ActorController(ActorService actorService, ActorProperties props, ActorRepository actorRepository) {
        this.actorService = actorService;
        this.props = props;
        this.actorRepository = actorRepository;
    }

    private void addAttributes(int page, Model model) {
        Page<Actor> pagedActors = this.actorService.findAll(PageRequest.of(page, this.props.getPageSize()));

        model.addAttribute("actors", pagedActors.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("noOfPages", pagedActors.getTotalPages());
    }

    @GetMapping
    public String getActors(Model model, @RequestParam(defaultValue = "0") int page) {

        log.info("Client requested actors");

        List<Actor> listActor = actorRepository.findAll();

        this.addAttributes(page, model);
        model.addAttribute("allActors",listActor);
        model.addAttribute("actor", new Actor());

        return "actors";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String processActor(@Valid Actor actor, Errors errors, Model model, @AuthenticationPrincipal User user) {
        log.info("Client added a new actor: " + actor);
        if (errors.hasErrors()) {
            this.addAttributes(0, model);
            log.info(" . . . but there are errors included: " + actor);
            return "actors";
        }

        actor.setCreatedBy(user);

        this.actorService.saveAndSetId(actor);

        return "redirect:/actors";
    }
}
