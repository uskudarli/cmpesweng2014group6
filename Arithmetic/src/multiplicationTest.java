import static org.junit.Assert.*;
import org.junit.Test;

/**
 * \brief Multiplication test class. 
 * Used to test multiplication.
 * 
 * This class was created as a homework for CMPE 352 lecture, Spring 2014.
 * @author Serkan Bugur, 2009400099
 */
public class multiplicationTest {
	@Test
	/**
	 * First function for multiplication test.
	 * */
	public void test() {
		
		Multiplication tester = new Multiplication();
		assertEquals("3 * 2 must be equal to 6", 6, tester.multiplication(3, 2));
	}
	@Test
	/**
	 * Second function for multiplication test.
	 * */
	public void test2() {
		
		Multiplication tester = new Multiplication();
		assertEquals("3 * -4 must be equal to -12", -12, tester.multiplication(3, -4));
	}
	@Test
	/**
	 * Third function for multiplication test.
	 * */
	public void test3() {
		
		Multiplication tester = new Multiplication();
		assertEquals("0 * 5 must be equal to 0", 0, tester.multiplication(0, 5));
	}
}
