package com.assalam.school.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Discipline.
 */
@Entity
@Table(name = "discipline")
@RegisterForReflection
public class Discipline extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    public Instant date;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
     public String description;

    @Column(name = "comment")
    public String comment;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    @JsonbTransient
    public Eleve eleve;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Discipline)) {
            return false;
        }
        return id != null && id.equals(((Discipline) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Discipline{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", description='" + description + "'" +
            ", comment='" + comment + "'" +
            "}";
    }

    public Discipline update() {
        return update(this);
    }

    public Discipline persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Discipline update(Discipline discipline) {
        if (discipline == null) {
            throw new IllegalArgumentException("discipline can't be null");
        }
        var entity = Discipline.<Discipline>findById(discipline.id);
        if (entity != null) {
            entity.date = discipline.date;
            entity.description = discipline.description;
            entity.comment = discipline.comment;
            entity.eleve = discipline.eleve;
        }
        return entity;
    }

    public static Discipline persistOrUpdate(Discipline discipline) {
        if (discipline == null) {
            throw new IllegalArgumentException("discipline can't be null");
        }
        if (discipline.id == null) {
            persist(discipline);
            return discipline;
        } else {
            return update(discipline);
        }
    }


}
