package BarGo.Back;

import BarGo.Back.Model.Usuari;
import BarGo.Back.Repository.UsuariInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BarGoApplicationTests {

	@Autowired
	private UsuariInterface usuariInterface;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Test
	public void crearUsuariTest() {
		Usuari usuari = new Usuari("anna", encoder.encode("123"));
		Usuari usuariGuardat = usuariInterface.save(usuari);

		assertTrue(usuariGuardat.getContrasenya().equals(usuari.getContrasenya()));

	}

}
