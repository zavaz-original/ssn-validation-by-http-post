package com.sb_juhav.siili_pic.app_validation_tests;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sb_juhav.siili_pic.SiiliPicValidationApplication;

@WebAppConfiguration
@ContextConfiguration(classes = SiiliPicValidationApplication.class)
@SpringBootTest
class SiiliPicValidationApplicationTests {

	@Test
	void contextLoads() {

	}

}
