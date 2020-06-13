package com.assalam.school.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.assalam.school.TestUtil;
import org.junit.jupiter.api.Test;


public class DisciplineTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Discipline.class);
        Discipline discipline1 = new Discipline();
        discipline1.id = 1L;
        Discipline discipline2 = new Discipline();
        discipline2.id = discipline1.id;
        assertThat(discipline1).isEqualTo(discipline2);
        discipline2.id = 2L;
        assertThat(discipline1).isNotEqualTo(discipline2);
        discipline1.id = null;
        assertThat(discipline1).isNotEqualTo(discipline2);
    }
}
