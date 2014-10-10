import static org.junit.Assert.*;

import org.junit.Test;

/**
 * \brief Divide test class. 
 * Used to test division.
 * 
 * This class was created as a homework for CMPE 352 lecture, Spring 2014.
 * @author Kivanc Yazan, 2009400198
 */
public class DivideTest {

	/**
	 * Function for division test.
	 * */
	@Test
	public void test() {
		
		Divide tester = new Divide();
		assertEquals("4 / 1 must be equal 4", 4, tester.divide(4, 1));
		
	}
	

}
