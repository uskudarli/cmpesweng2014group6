import static org.junit.Assert.*;

import org.junit.Test;

/**
 * \brief Substract test class. 
 * Used to test subtraction.
 * 
 * This class was created as a homework for CMPE 352 lecture, Spring 2014.
 * @author Naqibullah Danishjo, 2007200103
 */
public class SubstractTest {
	
	

	@Test
	/**
	 * Function for subtraction test.
	 * */
	public void test() {
		
		Substract tester = new Substract();
		assertEquals("6 - 2 must be equal to 4", 4, tester.substract(6, 2));
	}

}