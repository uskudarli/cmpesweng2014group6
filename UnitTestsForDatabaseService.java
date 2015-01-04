import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class UnitTestsForDatabaseService {
	DatabaseService db = new DatabaseService();
	@Test
	public void test() {
		
		ArrayList <String> tagList = new ArrayList<String>();
		
		tagList = db.findTagIds("campus");
		
		assertEquals("41",tagList.get(0) );
		assertEquals("42",tagList.get(1) );	
	}
	
	@Test
	public void testPathByPicId(){
		assertEquals("smeagolkare.jpg",db.pathByPicId(41));
	}
	@Test
	public void testisFollowingPlace(){
		assertEquals("true", Boolean.toString(db.isFollowingPlace(122, 57)));
	}
	@Test
	public void testdontRemember(){
		assertEquals("true", Boolean.toString(db.dontRemember(112, 101)));
	}
	@Test
	public void testRemember(){
		assertEquals("true", Boolean.toString(db.remember(112, 101)));
	}
	@Test
	public void testisRemembered(){
		assertEquals("false", Boolean.toString(db.isRemembered(112, 101)));
	}
	@Test
	public void testgetPicturePathsOfaPlace(){
		ArrayList <String> picturePaths = new ArrayList<String>();
		
		picturePaths = db.getPicturePathsOfaPlace(57);
		
		assertEquals("69.jpg",picturePaths.get(0));
		assertEquals("71.jpg",picturePaths.get(1));
	}
	@Test
	public void testgetStoryPicturePath(){
		ArrayList <String> storyPicPath = new ArrayList<String>();
		
		storyPicPath = db.getStoryPicturePath(200);
		
		assertEquals("http://titan.cmpe.boun.edu.tr:8085/image/56.jpg",storyPicPath.get(0));
		
	}
	
	
	
	
	

}
