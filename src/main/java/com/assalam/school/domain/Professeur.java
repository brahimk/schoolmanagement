package com.assalam.school.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
// import javax.json.bind.annotation.JsonbTransient;
// import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Professeur.
 */
@Entity
@Table(name = "professeur")
@RegisterForReflection
public class Professeur extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    public String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    public String prenom;

    @Column(name = "comment")
    public String comment;

    @OneToMany(mappedBy = "professeur")
    public Set<Classe> classes = new HashSet<>();

    @OneToMany(mappedBy = "professeur")
    public Set<FeuillePresence> feuillePresences = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professeur)) {
            return false;
        }
        return id != null && id.equals(((Professeur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Professeur{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", comment='" + comment + "'" +
            "}";
    }

    public Professeur update() {
        return update(this);
    }

    public Professeur persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Professeur update(Professeur professeur) {
        if (professeur == null) {
            throw new IllegalArgumentException("professeur can't be null");
        }
        var entity = Professeur.<Professeur>findById(professeur.id);
        if (entity != null) {
            entity.nom = professeur.nom;
            entity.prenom = professeur.prenom;
            entity.comment = professeur.comment;
            entity.classes = professeur.classes;
            entity.feuillePresences = professeur.feuillePresences;
        }
        return entity;
    }

    public static Professeur persistOrUpdate(Professeur professeur) {
        if (professeur == null) {
            throw new IllegalArgumentException("professeur can't be null");
        }
        if (professeur.id == null) {
            persist(professeur);
            return professeur;
        } else {
            return update(professeur);
        }
    }


}
