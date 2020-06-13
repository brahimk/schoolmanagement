package com.assalam.school.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.assalam.school.TestUtil;
import org.junit.jupiter.api.Test;


public class ClasseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classe.class);
        Classe classe1 = new Classe();
        classe1.id = 1L;
        Classe classe2 = new Classe();
        classe2.id = classe1.id;
        assertThat(classe1).isEqualTo(classe2);
        classe2.id = 2L;
        assertThat(classe1).isNotEqualTo(classe2);
        classe1.id = null;
        assertThat(classe1).isNotEqualTo(classe2);
    }
}
