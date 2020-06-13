package com.assalam.school.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.assalam.school.TestUtil;
import org.junit.jupiter.api.Test;


public class ProfesseurTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professeur.class);
        Professeur professeur1 = new Professeur();
        professeur1.id = 1L;
        Professeur professeur2 = new Professeur();
        professeur2.id = professeur1.id;
        assertThat(professeur1).isEqualTo(professeur2);
        professeur2.id = 2L;
        assertThat(professeur1).isNotEqualTo(professeur2);
        professeur1.id = null;
        assertThat(professeur1).isNotEqualTo(professeur2);
    }
}
