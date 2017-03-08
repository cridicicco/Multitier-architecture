package Test.Modelli;
import Modelli.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PacchettoTest {
	Pacchetto p = new Pacchetto(10000);
	String nome = "Maurizio";
	Pacchetto q = new Pacchetto(10000);
	

	@Before
	public void setUp() throws Exception {
		p.setUser(nome.getBytes());
		p.copiaPacchetto(q);
		
	}

	@Test
	public void test() {
		assertEquals(new String(q.getUser()), "Maurizio");
	}

}
