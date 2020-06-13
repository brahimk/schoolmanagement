package com.assalam.school.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.assalam.school.TestUtil;
import org.junit.jupiter.api.Test;


public class FeuillePresenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeuillePresence.class);
        FeuillePresence feuillePresence1 = new FeuillePresence();
        feuillePresence1.id = 1L;
        FeuillePresence feuillePresence2 = new FeuillePresence();
        feuillePresence2.id = feuillePresence1.id;
        assertThat(feuillePresence1).isEqualTo(feuillePresence2);
        feuillePresence2.id = 2L;
        assertThat(feuillePresence1).isNotEqualTo(feuillePresence2);
        feuillePresence1.id = null;
        assertThat(feuillePresence1).isNotEqualTo(feuillePresence2);
    }
}
