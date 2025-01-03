package de.lion5.spring.dvd.controller;

import javax.validation.Valid;

import de.lion5.spring.dvd.model.Actor;
import de.lion5.spring.dvd.model.Movie;
import de.lion5.spring.dvd.properties.MovieProperties;
import de.lion5.spring.dvd.repository.ActorRepository;
import de.lion5.spring.dvd.repository.MovieRepository;
import de.lion5.spring.dvd.service.ActorService;
import de.lion5.spring.dvd.service.MovieService;
import de.lion5.spring.dvd.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieProperties props;

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieService movieService, MovieProperties props, ActorRepository actorRepository, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.props = props;
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    private void addAttributes(int page, Model model) {
        Page<Movie> pagedMovies = this.movieService.findAll(PageRequest.of(page, this.props.getPageSize()));

        model.addAttribute("movies", pagedMovies.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("noOfPages", pagedMovies.getTotalPages());
    }

    @GetMapping
    public String getMovies(Model model, @RequestParam(defaultValue = "0") int page) {

        log.info("Client requested movies");

        List<Actor> listActor = actorRepository.findAll();
        List<Movie> listMovie = movieRepository.findAll();

        this.addAttributes(page, model);
        model.addAttribute("listMovies", listMovie);
        model.addAttribute("movie", new Movie());
        model.addAttribute("listActor",listActor);

        return "movies";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String processMovie(@Valid Movie movie, Errors errors, Model model, @AuthenticationPrincipal User user) {
        log.info("Client posted a new movie: " + movie);
        if (errors.hasErrors()) {
            this.addAttributes(0, model);
            log.info(" . . . but there are errors included: " + movie);
            return "movies";
        }

        movie.setCreatedBy(user);
        this.movieService.saveAndSetId(movie);

        return "redirect:/movies";
    }
}
