import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class Problem {
	
	private static ArrayList<Job> jobs = new ArrayList<Job>();
	public static int[][] times;
	
	public static int getTime(int c1, int c2){
		return times[c1][c2];
	}
	
	public static Job[] getJobs(){
		Job[] res = new Job[jobs.size()];
		jobs.toArray(res);
		return res;
	}
	
	public static void loadProblem(String filename){
		try{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] buffer = line.split(",");
			
			if (buffer[0].equals("times")){
				int size = Integer.parseInt(buffer[1].trim());
				times = new int[size][];
				for(int i=0; i < size;i++){
					line = br.readLine();
					times[i] = new int[size];
					buffer = line.split(",");
					for (int z=0; z < size;z++){
						times[i][z] = Integer.parseInt(buffer[z].trim());
					}
				}
			}
			
			if (buffer[0].equals("jobs")){
				int size = Integer.parseInt(buffer[1].trim());
				for (int x=0; x < size; x++){
					line = br.readLine();
					Job j = new Job();
					buffer = line.split(",");
					j.id = Integer.parseInt(buffer[0]);
					j.pickup = Integer.parseInt(buffer[1]);
					j.setdown = Integer.parseInt(buffer[2]);
					j.available = Integer.parseInt(buffer[3]);
					j.payments[0][0] = Integer.parseInt(buffer[4]);
					j.payments[0][1] = Integer.parseInt(buffer[5]);
					j.payments[1][0] = Integer.parseInt(buffer[6]);
					j.payments[1][1] = Integer.parseInt(buffer[7]);
					j.payments[2][0] = Integer.parseInt(buffer[8]);
					j.payments[2][1] = Integer.parseInt(buffer[9]);
					j.payments[3][0] = Integer.parseInt(buffer[10]);
					j.payments[3][1] = Integer.parseInt(buffer[11]);
					jobs.add(j);
				}
			}
		}
	 
		br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public static int score(Job[] plan){
		int reward=0;
		int location =0;
		int time = 0;
		
		if (plan.length < jobs.size()){
			System.out.println("Error: Jobs missing");
			return -1;
		}

		if (plan.length > jobs.size()){
			System.out.println("Error: Extra jobs");
			return -1;
		}

		
		
		for (int x=0; x < plan.length;x++){
			Job j = plan[x];
			
			time = time + times[location][j.pickup];
			
			if (time < j.available)
				time = j.available;
			
			
			time = time + times[j.pickup][j.setdown];
			
			//Calc payment
			for (int z=0; z < j.payments.length;z++){
				if (time < j.payments[z][0]){
					reward = reward + j.payments[z][1];
					break;
				}
			}
			location = j.setdown;
		}
		
		return reward;
	}
}
