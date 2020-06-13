package com.assalam.school.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Eleve.
 */
@Entity
@Table(name = "eleve")
@RegisterForReflection
public class Eleve extends PanacheEntityBase implements Serializable {

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

    @NotNull
    @Column(name = "date_naissance", nullable = false)
    public Instant dateNaissance;

    @Column(name = "niveau_scolaire")
    public String niveauScolaire;

    @Column(name = "niveau_arabe")
    public String niveauArabe;

    @Column(name = "comment")
    public String comment;

    @Lob
    @Column(name = "image")
    public byte[] image;

    @Column(name = "image_content_type")
    public String imageContentType;

    @OneToMany(mappedBy = "eleve")
    public Set<FeuillePresence> feuillePresences = new HashSet<>();

    @OneToMany(mappedBy = "eleve")
    public Set<Discipline> disciplines = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "famille_id")
    @JsonbTransient
    public Famille famille;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    @JsonbTransient
    public Classe classe;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eleve)) {
            return false;
        }
        return id != null && id.equals(((Eleve) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Eleve{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", dateNaissance='" + dateNaissance + "'" +
            ", niveauScolaire='" + niveauScolaire + "'" +
            ", niveauArabe='" + niveauArabe + "'" +
            ", comment='" + comment + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            "}";
    }

    public Eleve update() {
        return update(this);
    }

    public Eleve persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Eleve update(Eleve eleve) {
        if (eleve == null) {
            throw new IllegalArgumentException("eleve can't be null");
        }
        var entity = Eleve.<Eleve>findById(eleve.id);
        if (entity != null) {
            entity.nom = eleve.nom;
            entity.prenom = eleve.prenom;
            entity.dateNaissance = eleve.dateNaissance;
            entity.niveauScolaire = eleve.niveauScolaire;
            entity.niveauArabe = eleve.niveauArabe;
            entity.comment = eleve.comment;
            entity.image = eleve.image;
            entity.feuillePresences = eleve.feuillePresences;
            entity.disciplines = eleve.disciplines;
            entity.famille = eleve.famille;
            entity.classe = eleve.classe;
        }
        return entity;
    }

    public static Eleve persistOrUpdate(Eleve eleve) {
        if (eleve == null) {
            throw new IllegalArgumentException("eleve can't be null");
        }
        if (eleve.id == null) {
            persist(eleve);
            return eleve;
        } else {
            return update(eleve);
        }
    }


}
