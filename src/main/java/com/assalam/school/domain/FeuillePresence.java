package com.assalam.school.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
// import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A FeuillePresence.
 */
@Entity
@Table(name = "feuille_presence")
@RegisterForReflection
public class FeuillePresence extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    public Instant date;

    @Column(name = "present")
    public Boolean present;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
     public String description;

    @Column(name = "comment")
    public String comment;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    @JsonbTransient
    public Professeur professeur;

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
        if (!(o instanceof FeuillePresence)) {
            return false;
        }
        return id != null && id.equals(((FeuillePresence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FeuillePresence{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", present='" + present + "'" +
            ", description='" + description + "'" +
            ", comment='" + comment + "'" +
            "}";
    }

    public FeuillePresence update() {
        return update(this);
    }

    public FeuillePresence persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static FeuillePresence update(FeuillePresence feuillePresence) {
        if (feuillePresence == null) {
            throw new IllegalArgumentException("feuillePresence can't be null");
        }
        var entity = FeuillePresence.<FeuillePresence>findById(feuillePresence.id);
        if (entity != null) {
            entity.date = feuillePresence.date;
            entity.present = feuillePresence.present;
            entity.description = feuillePresence.description;
            entity.comment = feuillePresence.comment;
            entity.professeur = feuillePresence.professeur;
            entity.eleve = feuillePresence.eleve;
        }
        return entity;
    }

    public static FeuillePresence persistOrUpdate(FeuillePresence feuillePresence) {
        if (feuillePresence == null) {
            throw new IllegalArgumentException("feuillePresence can't be null");
        }
        if (feuillePresence.id == null) {
            persist(feuillePresence);
            return feuillePresence;
        } else {
            return update(feuillePresence);
        }
    }


}
