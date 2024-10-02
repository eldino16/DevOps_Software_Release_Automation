package de.lion5.spring.dvd.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.lion5.spring.dvd.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(name = "Actor.actors", // entity graph solution
        attributeNodes = @NamedAttributeNode(value = "createdBy"))
// entity graph solution
public class Actor {
    @Id
    private Long id;
    private String name;
    private boolean wonOscar;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;
    // default fetch type: LAZY
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "actors", cascade = {MERGE, PERSIST, DETACH})
    @JsonBackReference
    private List<Movie> movies;
    @ManyToOne
    private User createdBy;

    public Actor(long id, String name, boolean wonOscar, LocalDate birthday, User user) {
        this.id = id;
        this.name = name;
        this.wonOscar = wonOscar;
        this.birthday = birthday;
        this.createdBy = user;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", wonOscar=" + this.wonOscar +
                ", birthday=" + this.birthday + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actor)) {
            return false;
        }
        Actor actor = (Actor) o;
        return this.isWonOscar() == actor.isWonOscar() &&
                this.getBirthday() == actor.getBirthday() &&
                this.getName().equals(actor.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getName(), this.isWonOscar(), this.getBirthday());
    }

}
