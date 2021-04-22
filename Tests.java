
import static org.junit.Assert.*;
import org.junit.Test;

public class Tests {

	@Test
    public void testBasics() {
        Schedule schedule = new Schedule();
        schedule.insert(8);
        Schedule.Job j1 = schedule.insert(3);
        schedule.insert(5);
        assertEquals(8, schedule.finish());
        schedule.get(0).requires(schedule.get(2));
        assertEquals(13, schedule.finish()); //should return 13 (job 0 cannot start until time 5)
        schedule.get(0).requires(j1); //job 1 must precede job 0
        assertEquals(13, schedule.finish()); //should return 13
        assertEquals(5, schedule.get(0).start()); //should return 5
        
        j1.start(); //should return 0
        assertEquals(0, schedule.get(2).start()); //should return 0
        j1.requires(schedule.get(2)); //job 2 must precede job 1
        schedule.get(0);
        assertEquals(16, schedule.finish()); //should return 16
        
        assertEquals(8, schedule.get(0).start()); //should return 8
        assertEquals(5, schedule.get(1).start()); //should return 5
        assertEquals(0, schedule.get(2).start()); //should return 0
        schedule.get(1).requires(schedule.get(0));
         //job 0 must precede job 1 (creates loop)
        assertEquals(-1, schedule.finish()); //should return -1
        assertEquals(-1, schedule.get(0).start()); //should return -1
        assertEquals(-1, schedule.get(1).start()); //should return -1
        assertEquals(0, schedule.get(2).start()); //should return 0 (no loops in prerequisites)    
    }
	
	@Test
	public void noCycle() {
		Schedule s=new Schedule();
		assertEquals(0, s.finish());
		
		s.insert(10);
		assertEquals(10, s.finish());
		
		Schedule.Job j1=s.insert(5);
		assertEquals(10, s.finish());
		
		s.get(0).requires(j1);
		assertEquals(15, s.finish());
		
		assertEquals(5,s.get(0).start());
	}
	
	@Test
	public void withCycle() {
		Schedule s=new Schedule();
		s.insert(10);
		s.insert(5);
		s.insert(1);
		s.get(0).requires(s.get(1));
		s.get(1).requires(s.get(2));
		s.get(2).requires(s.get(0));
		assertEquals(-1, s.finish());
		assertEquals(-1, s.get(0).start());
		
	}

	@Test
	public void relax() {
		Schedule s=new Schedule();
		s.insert(10);
		s.insert(5);
		s.insert(1);
		s.insert(3);
		s.insert(2);
		
		s.get(2).requires(s.get(4));
		assertEquals(10, s.finish());
		s.get(0).requires(s.get(2));
		s.get(0).requires(s.get(3));
		s.get(0).requires(s.get(1));
		assertEquals(15, s.finish());
		
	}
	
}
