package cs146Schedule;
import java.util.*;

public class Schedule {
	public ArrayList<Job> jobList;
	private int minTime;
	
	public Schedule() {
		jobList=new ArrayList<Job>();
		minTime=0;
	}
	
	// adds a new job to the schedule, input time is the deadline of that job
	public Job insert(int time) {
		Job j=new Job(time);
		jobList.add(j);
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
			j.parent=null;
			j.finished=false;
		}
	}
	
	// return the topologically sorted list of jobs
	public ArrayList<Job> getSortedList(){
		Queue<Job> q=new LinkedList<Job>();
		ArrayList<Job> l=new ArrayList<Job>();
		for(Job j : jobList) {
			if(j.inDegree==0) q.add(j);
		}
		while(!q.isEmpty()) {
			Job j=q.remove();
			l.add(j);
			for(Job todo : j.next) {
				if(--todo.inDegree==0) q.add(todo);
			}
		}
		return l;
	}
	
	// make a global variable, if there's a bigger startTime, update the global startTime
	// relax the path from a to b
	public void relax(Job a, Job b) {
		if(a.startTime+a.time > b.startTime) {
			b.startTime=a.startTime+a.time;
			b.parent=a;
			// signal flag, check for cycle
			if(b.finished) {
				
			}
			// update the minTime if there's a longer time needed to finish
			if(b.startTime+b.time>minTime) minTime=b.startTime+b.time;
		}
	}
	
//	// ?? what is G and vertList
//	public ArrayList<Job> topOrder(ArrayList<Job> jobs, Job start, ArrayList<Job> vertList) {
//		start.discovered=true;
//		for(Job j : start.next) {
//			if(j.discovered==false) {
//				topOrder(jobs, j, vertList);
//			}
//		}
//		vertList.add(start);
//		return vertList;
//	}
	
	
	// earliest possible completion time for the entire schedule
	// do the dagsssp here
	public int finish() {
		initialize();
		ArrayList<Job> toplist=getSortedList();
		for(int i=toplist.size()-1; i>=0; i--) {
			toplist.get(i).finished=true;
			for(Job j : toplist.get(i).next) {
				relax(toplist.get(i), j);
			}
		}
		return minTime;
	}
	
	
	
	public class Job {
		private int time, inDegree, startTime;
		private Job parent;
		private boolean finished;
		public ArrayList<Job> next;
		
		
		// constructor
		private Job(int time) {
			startTime=0;
			this.time=time;
			parent=null;
			inDegree=0;
			next=new ArrayList<Job>();
		}
		
		
		public void requires(Job j) {
			parent=j;
			j.next.add(this);
			inDegree++;
		}
		
		
		// return the min time required before starting the current job, if no pre, return 0; if there's a cycle, return -1;
		public int start() {
			int sum=0;
			Job temp=this;
			while(temp.parent!=null) {
				sum+=temp.parent.time;
				temp=parent;
				if(temp.parent==this) return -1;
			}
			
			return sum;
		}
		
		
	}

	
}


