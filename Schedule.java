
import java.util.*;

public class Schedule {
	public ArrayList<Job> jobList;
	private boolean hasCycle;
	private int minTime;
	
	public Schedule() {
		jobList=new ArrayList<Job>();
		minTime=0;
		hasCycle=false;
	}
	
	// adds a new job to the schedule, input time is the deadline of that job
	public Job insert(int time) {
		Job j=new Job(time);
		jobList.add(j);
		if(minTime<time) minTime=time;
		return j;
	}
	
	// return the job by its number
	public Job get(int index) {
		return jobList.get(index);
	}
	
	// SSSPInitialize
	public void initialize() {
		for(Job j : jobList) {
			j.startTime=0;
		}
	}
	
	// return the topologically sorted list of jobs
	public ArrayList<Job> getSortedList(){
		Queue<Job> q=new LinkedList<Job>();
		ArrayList<Job> l=new ArrayList<Job>();
		for(Job j : jobList) {
			j.inDegCopy=j.inDegree;
			if(j.inDegCopy==0) q.add(j);
		}
		while(!q.isEmpty()) {
			Job j=q.remove();
			l.add(j);
			for(Job todo : j.next) {
				if(--todo.inDegCopy==0) q.add(todo);
			}
		}
		hasCycle=(l.size()!=jobList.size());
		return l;
	}
	
	// make a global variable, if there's a bigger startTime, update the global startTime
	// relax the path from a to b
	public void relax(Job a, Job b) {
		if(a.startTime+a.time > b.startTime) {
			b.startTime=(a.startTime+a.time);
			// signal flag, check for cycle
			// update the minTime if there's a longer time needed to finish
			if(b.startTime+b.time>minTime) minTime=b.startTime+b.time;
		}
	}
	
	// earliest possible completion time for the entire schedule
	// do the dagsssp here
	public int finish() {
		initialize();
		ArrayList<Job> topList=getSortedList();
		
		// will cause me problem later
		for(Job v : topList) {
			for(Job j : v.next) {
				relax(v, j);
			}
		}
		if(hasCycle) return -1;
		return minTime;
	}
	
	
	public class Job {
		private int time, inDegree, startTime, inDegCopy;
		public ArrayList<Job> next;
		
		
		// constructor
		private Job(int time) {
			startTime=0;
			this.time=time;
			inDegree=0;
			inDegCopy=inDegree;
			next=new ArrayList<Job>();
		}
		
		
		public void requires(Job j) {
			j.next.add(this);
			inDegree++;
		}
		
		
		// return the min time required before starting the current job, if no pre, return 0; if the job is in a cycle, return -1
		public int start() {
			if(inDegree==0) return 0;
			finish();
			if(inDegCopy!=0) return -1;
			return startTime;
		}
		
//		public void print() {
//			System.out.println("Time: " + time);
//			System.out.println("startTime: " + startTime);
//			System.out.println("inDegree: " + inDegree);
//			for(Job j : next) {
//				System.out.println("next time: " + j.time);
//			}
//			if(parent!=null) System.out.println("parent: " + parent.time);
//		}
		
		
	}

	
}


