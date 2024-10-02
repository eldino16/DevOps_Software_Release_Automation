package de.lion5.spring.dvd.repository;

import java.util.List;
import java.util.Optional;

import de.lion5.spring.dvd.model.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {

    @Override
    @EntityGraph(value = "Actor.movies")
        // entity graph solution
    Optional<Actor> findById(Long aLong);

    @Override
    @EntityGraph(value = "Actor.actors")
        // entity graph solution
    List<Actor> findAll();

    @Override
    @EntityGraph(value = "Actor.actors")
        // entity graph solution
    Page<Actor> findAll(Pageable pageable);
}

