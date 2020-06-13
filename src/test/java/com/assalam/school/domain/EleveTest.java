package com.assalam.school.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.assalam.school.TestUtil;
import org.junit.jupiter.api.Test;


public class EleveTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eleve.class);
        Eleve eleve1 = new Eleve();
        eleve1.id = 1L;
        Eleve eleve2 = new Eleve();
        eleve2.id = eleve1.id;
        assertThat(eleve1).isEqualTo(eleve2);
        eleve2.id = 2L;
        assertThat(eleve1).isNotEqualTo(eleve2);
        eleve1.id = null;
        assertThat(eleve1).isNotEqualTo(eleve2);
    }
}
