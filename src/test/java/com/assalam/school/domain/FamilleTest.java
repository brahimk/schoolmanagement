package com.assalam.school.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.assalam.school.TestUtil;
import org.junit.jupiter.api.Test;


public class FamilleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Famille.class);
        Famille famille1 = new Famille();
        famille1.id = 1L;
        Famille famille2 = new Famille();
        famille2.id = famille1.id;
        assertThat(famille1).isEqualTo(famille2);
        famille2.id = 2L;
        assertThat(famille1).isNotEqualTo(famille2);
        famille1.id = null;
        assertThat(famille1).isNotEqualTo(famille2);
    }
}
