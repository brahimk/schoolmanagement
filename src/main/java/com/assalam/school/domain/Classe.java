package com.assalam.school.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.assalam.school.domain.enumeration.Salle;

import com.assalam.school.domain.enumeration.Creneau;

/**
 * A Classe.
 */
@Entity
@Table(name = "classe")
@RegisterForReflection
public class Classe extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    public String nom;

    @NotNull
    @Column(name = "niveau", nullable = false)
    public String niveau;

    @Enumerated(EnumType.STRING)
    @Column(name = "salle")
    public Salle salle;

    @Enumerated(EnumType.STRING)
    @Column(name = "creneau")
    public Creneau creneau;

    @Column(name = "comment")
    public String comment;

    @OneToMany(mappedBy = "classe")
    public Set<Eleve> eleves = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    @JsonbTransient
    public Professeur professeur;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classe)) {
            return false;
        }
        return id != null && id.equals(((Classe) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Classe{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", niveau='" + niveau + "'" +
            ", salle='" + salle + "'" +
            ", creneau='" + creneau + "'" +
            ", comment='" + comment + "'" +
            "}";
    }

    public Classe update() {
        return update(this);
    }

    public Classe persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Classe update(Classe classe) {
        if (classe == null) {
            throw new IllegalArgumentException("classe can't be null");
        }
        var entity = Classe.<Classe>findById(classe.id);
        if (entity != null) {
            entity.nom = classe.nom;
            entity.niveau = classe.niveau;
            entity.salle = classe.salle;
            entity.creneau = classe.creneau;
            entity.comment = classe.comment;
            entity.eleves = classe.eleves;
            entity.professeur = classe.professeur;
        }
        return entity;
    }

    public static Classe persistOrUpdate(Classe classe) {
        if (classe == null) {
            throw new IllegalArgumentException("classe can't be null");
        }
        if (classe.id == null) {
            persist(classe);
            return classe;
        } else {
            return update(classe);
        }
    }


}
