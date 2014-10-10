import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * \brief Power test class. 
 * Used to test power.
 * 
 * This class was created as a homework for CMPE 352 lecture, Spring 2014.
 * @author H.Esma Ozelbas, 2009400087
 */
public class PowerTest {
	Power tester;
	
	@Before
	/**
	 * Setup function for testing.
	 * */
	public void SetUp(){
		
		tester = new Power();
		
	}
	
	@Test
	/**
	 * Test function.
	 * */
	public void Test() {

		assertEquals("3^2 must be equal to 9.", 9, tester.pow(3,2));
		
	}
	
	@Test
	/**
	 * Function to test powers of zero.
	 * */
	public void PowersOfZero(){
		
		assertEquals("0^3 must be equal to 0.", 0, tester.pow(0, 3));
		assertEquals("0^0 must be equal to 1.", 1, tester.pow(0, 0));
		
	}

}
