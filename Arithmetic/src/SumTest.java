import static org.junit.Assert.*;

import org.junit.Test;

/**
 * \brief Sum test class. 
 * Used to test sum.
 * 
 * This class was created as a homework for CMPE 352 lecture, Spring 2014.
 * @author Ugur Kalkan, 2010400051
 */
public class SumTest {
	
	
	@Test
	/**
	 * First function for sum test.
	 * */
	public void test() {
		
		Sum tester = new Sum();
		assertEquals("5 + 8 must be equal to 13", 13, tester.sum(5, 8));
		
	}
	
	@Test
	/**
	 * Second function for sum test.
	 * */
	public void test2() {
		
		Sum tester2 = new Sum();
		assertEquals("-5 + 8 must be equal to 3", 3, tester2.sum(-5, 8));
	}
	
	@Test
	/**
	 * Third function for sum test.
	 * */
	public void test3() {
		
		Sum tester3 = new Sum();
		assertEquals("-3 + 3 must be equal to 0", 0, tester3.sum(-3, 3));
	}

}
