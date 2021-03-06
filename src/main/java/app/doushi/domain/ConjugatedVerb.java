package app.doushi.domain;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.doushi.domain.enumeration.ConjugationType;

/**
 * A ConjugatedVerb.
 */
@Entity
@Table(name = "conjugated_verb")
public class ConjugatedVerb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conjugation_type", nullable = false)
    private ConjugationType conjugationType;

    @Size(min = 0, max = 100)
    @Column(name = "romanji_text", length = 100, nullable = true)
    private String romanjiText;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "kanji_text", length = 100, nullable = false)
    private String kanjiText;

    @ManyToOne(optional = false)
    @NotNull
    private Verb verb;

    @OneToMany(mappedBy = "conjugatedVerb")
    @JsonIgnore
    private Set<ConjugatedVerbDefinition> definitions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConjugationType getConjugationType() {
        return conjugationType;
    }

    public ConjugatedVerb conjugationType(ConjugationType conjugationType) {
        this.conjugationType = conjugationType;
        return this;
    }

    public void setConjugationType(ConjugationType conjugationType) {
        this.conjugationType = conjugationType;
    }

    public String getRomanjiText() {
        return romanjiText;
    }

    public ConjugatedVerb romanjiText(String romanjiText) {
        this.romanjiText = romanjiText;
        return this;
    }

    public void setRomanjiText(String romanjiText) {
        this.romanjiText = romanjiText;
    }

    public String getKanjiText() {
        return kanjiText;
    }

    public ConjugatedVerb kanjiText(String kanjiText) {
        this.kanjiText = kanjiText;
        return this;
    }

    public void setKanjiText(String kanjiText) {
        this.kanjiText = kanjiText;
    }

    public Verb getVerb() {
        return verb;
    }

    public ConjugatedVerb verb(Verb verb) {
        this.verb = verb;
        return this;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public Set<ConjugatedVerbDefinition> getDefinitions() {
        return definitions;
    }

    public ConjugatedVerb definitions(Set<ConjugatedVerbDefinition> conjugatedVerbDefinitions) {
        this.definitions = conjugatedVerbDefinitions;
        return this;
    }

    public ConjugatedVerb addDefinitions(ConjugatedVerbDefinition conjugatedVerbDefinition) {
        this.definitions.add(conjugatedVerbDefinition);
        conjugatedVerbDefinition.setConjugatedVerb(this);
        return this;
    }

    public ConjugatedVerb removeDefinitions(ConjugatedVerbDefinition conjugatedVerbDefinition) {
        this.definitions.remove(conjugatedVerbDefinition);
        conjugatedVerbDefinition.setConjugatedVerb(null);
        return this;
    }

    public void setDefinitions(Set<ConjugatedVerbDefinition> conjugatedVerbDefinitions) {
        this.definitions = conjugatedVerbDefinitions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConjugatedVerb conjugatedVerb = (ConjugatedVerb) o;
        if (conjugatedVerb.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conjugatedVerb.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConjugatedVerb{" +
            "id=" + getId() +
            ", conjugationType='" + getConjugationType() + "'" +
            ", romanjiText='" + getRomanjiText() + "'" +
            ", kanjiText='" + getKanjiText() + "'" +
            "}";
    }
}
