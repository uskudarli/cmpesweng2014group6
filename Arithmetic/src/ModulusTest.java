import static org.junit.Assert.*;

import org.junit.Test;

/**
 * \brief Modulus test class. 
 * Used to test modulus.
 * 
 * This class was created as a homework for CMPE 352 lecture, Spring 2014.
 * @author Yunus Emre Tekin, 2010400126
 */
public class ModulusTest {
	
	/**
	 * Function for modulus test.
	 * */
	@Test
	public void test() {
		
		Modulus tester = new Modulus();
		assertEquals("17 % 4 must be equal to 1", 1, tester.modulus(17, 4));
	}

}