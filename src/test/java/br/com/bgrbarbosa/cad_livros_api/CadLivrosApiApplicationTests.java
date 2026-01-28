package br.com.bgrbarbosa.cad_livros_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@ActiveProfiles("test")
class CadLivrosApiApplicationTests {

	@Test
	@WithMockUser
	void contextLoads() {
	}


}
