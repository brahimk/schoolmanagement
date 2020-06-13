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
 * A Famille.
 */
@Entity
@Table(name = "famille")
@RegisterForReflection
public class Famille extends PanacheEntityBase implements Serializable {

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

    @Column(name = "adresse")
    public String adresse;

    @Column(name = "code_postal")
    public Long codePostal;

    @Column(name = "ville")
    public String ville;

    @NotNull
    @Column(name = "telephone_1", nullable = false)
    public Long telephone1;

    @NotNull
    @Column(name = "telephone_2", nullable = false)
    public Long telephone2;

    @NotNull
    @Column(name = "email_1", nullable = false)
    public String email1;

    @Column(name = "email_2")
    public String email2;

    @Column(name = "comment")
    public String comment;

    @OneToMany(mappedBy = "famille")
    public Set<Eleve> eleves = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Famille)) {
            return false;
        }
        return id != null && id.equals(((Famille) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Famille{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", adresse='" + adresse + "'" +
            ", codePostal=" + codePostal +
            ", ville='" + ville + "'" +
            ", telephone1=" + telephone1 +
            ", telephone2=" + telephone2 +
            ", email1='" + email1 + "'" +
            ", email2='" + email2 + "'" +
            ", comment='" + comment + "'" +
            "}";
    }

    public Famille update() {
        return update(this);
    }

    public Famille persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Famille update(Famille famille) {
        if (famille == null) {
            throw new IllegalArgumentException("famille can't be null");
        }
        var entity = Famille.<Famille>findById(famille.id);
        if (entity != null) {
            entity.nom = famille.nom;
            entity.prenom = famille.prenom;
            entity.adresse = famille.adresse;
            entity.codePostal = famille.codePostal;
            entity.ville = famille.ville;
            entity.telephone1 = famille.telephone1;
            entity.telephone2 = famille.telephone2;
            entity.email1 = famille.email1;
            entity.email2 = famille.email2;
            entity.comment = famille.comment;
            entity.eleves = famille.eleves;
        }
        return entity;
    }

    public static Famille persistOrUpdate(Famille famille) {
        if (famille == null) {
            throw new IllegalArgumentException("famille can't be null");
        }
        if (famille.id == null) {
            persist(famille);
            return famille;
        } else {
            return update(famille);
        }
    }


}
