import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Main {

	static ArrayList<Job[]> population = new ArrayList<Job[]>();
	//static int fitness;
	//static int[] child;
	static int mutationrate = 5;
	static int length = 0;
	static int tour = 5;
	static int replacetour = 5;
	static int popsize = 500;
	//public static Random randomNum = new Random();
	//	public Random mutationChance = new Random();
	public static final Random randomNum = new Random();
	public static int overallFit = 0;
	public static long startTime = System.currentTimeMillis();
	public static int probNo = 5;
	public static int generation = 0;





	public static void main(String[] args) {

		int gen = 100;
		int mut = mutationrate;
		int bestfit = 0;
		int newfit = 0;

		createpop();

		while (gen == 100){
			newfit = Main.select();

			Random mutationChance = new Random();
			if (mutationChance.nextInt(mut) == 1){
				Main.mutation3();
			} 

			//System.out.println("Generation"+ gen);

			//	gen = gen-1;
		}
	}

	public static void createpop(){
		Problem.loadProblem("Problem" + probNo + ".txt");
		Job[] myJobs = Problem.getJobs();


		int j;
		for (j=0; j <popsize;j++){
			Collections.shuffle(Arrays.asList(myJobs));	
			Job[] newJob = Arrays.copyOf(myJobs, myJobs.length);
			//Create new array
			//clone myJobs to new array
			population.add(newJob);
		}
	}

	public static int select(){
		int i, j =0;
		Job[] bestJob = null;
		Job[] secondJob = null;
		int fit, fit2 = 0;
		int bestfit = 0;
		int bestfit2 = 0;

		for (i=0; i <tour -1; i++){

			Job[] picked = population.get(randomNum.nextInt(population.size()));
			fit = fitness(picked);
			if (fit > bestfit){
				bestJob = Arrays.copyOf(picked, picked.length);
				bestfit = fit;
			}
		}
		for (j=0; j <tour -1; j++){
			//	Random random2 = new Random(System.nanoTime());
			Job[] picked2 = population.get(randomNum.nextInt(population.size()));
			fit2 = fitness(picked2);
			if (fit2 > bestfit2){
				secondJob = Arrays.copyOf(picked2, picked2.length);
				bestfit2 = fit2;
			}
		}
		perm2(bestJob, secondJob);
		return bestfit;
	}

	public static void perm(Job[] parent1, Job[] parent2){

		Job[] child;
		Job temp = null;
		int crossPoint = 0;
		int i, j = 0;

		Random counter = new Random();

		crossPoint = counter.nextInt(parent1.length);
		//merge adults here

		//add to child up to crossPoint
		child = new Job[parent1.length];
		for(int k=0; k<crossPoint; k++){
			child[k] = parent1[k];
		}
		//child =  Arrays.copyOfRange(parent1, 0, crossPoint);

		while (Arrays.asList(child).contains(null)){
			for (j=0; j < parent2.length ; j++){
				if (!Arrays.asList(child).contains(parent2[j])){
					temp = parent2[j];
					//child[crossPoint] = parent2[j];
					child[crossPoint] = temp;	
					crossPoint = crossPoint +1;
				}
			}
		}
		replace(child);
	}

	public static void perm2(Job[] parent1, Job[] parent2){
		//do a different permutation operator here
		Job[] child = null;
		Job temp = null;
		int crossPoint = 0;
		int i, j = 0;

		Random counter = new Random();

		crossPoint = counter.nextInt(parent1.length);
		if (crossPoint == 0){
			crossPoint = crossPoint + 1;
		}
		int crossPoint2 = counter.nextInt(crossPoint);
		child = new Job[parent1.length];

		for(int k=0; k<crossPoint && k >crossPoint2; k++){
			child[k] = parent1[k];
		}
		//int crossPoint2 = counter.nextInt(crossPoint);
		//merge adults here

		//add to child up to crossPoint
		child = new Job[parent1.length];
		for(int k=0; k<crossPoint; k++){
			child[k] = parent1[k];
		}
		//child =  Arrays.copyOfRange(parent1, 0, crossPoint);

		while (Arrays.asList(child).contains(null)){
			for (j=0; j < parent2.length ; j++){
				if (!Arrays.asList(child).contains(parent2[j])){
					temp = parent2[j];
					//child[crossPoint] = parent2[j];
					child[crossPoint] = temp;	
					crossPoint = crossPoint +1;
				}
			}
		}
		replace(child);

	}


	public static void output(Job[] myJobs){

		//for(Job j: myJobs){
		//	System.out.print(j.id+",");
		//}
		int childfit = fitness(myJobs);

		if (overallFit < childfit){
			overallFit = childfit;

			long endtime = System.currentTimeMillis();
			long difference = endtime - startTime;
			
			System.out.println ("Generation:" + generation + " New fitness: " + overallFit + " Time: " + difference + "ms");
			generation = generation + 1;
			
			
		}

		//	System.out.println ("Time: " + duration+"s");
	}

	public static int fitness(Job[] k){
		int fit =0;
		int time = 0;
		int location = 0;
		int x = 0;


		for (x=0; x < k.length;x++){

			Job j = k[x];

			time = time + Problem.times[location][j.pickup];

			if (time < j.available)
				time = j.available;


			time = time + Problem.times[j.pickup][j.setdown];

			//Calc payment
			for (int z=0; z < j.payments.length;z++){
				if (time < j.payments[z][0]){
					fit = fit + j.payments[z][1];
					break;
				}
			}
			location = j.setdown;
		}
		return fit;
	}

	public static void replace(Job[] child){

		int index = findWorst();
		int worstFit = fitness(population.get(index));
		int childFit = fitness(child);

		if (childFit > worstFit){
			population.remove(index);
			population.add(child);
			output(child);
			//	System.out.println("Child replaced Job at " + index + ". New Fitness: " + childFit + ". Previous Fitness: " + worstFit);
		}


	}

	public static void mutation(){
		//randomly select Job
		//shuffle contents
		int mutin = 0;
		Job [] j = null;
		//	Random randomNum = new Random(System.nanoTime());
		j = population.get(randomNum.nextInt(population.size()));
		Collections.shuffle(Arrays.asList(j));
		mutin = population.indexOf(j);
		//	population.remove(mutin);
		population.set(mutin, j);
		//population.add(j);
	}

	public static int findWorst(){
		int index = 0;
		//Random indexran = new Random(System.nanoTime());
		Job[] worst = population.get(randomNum.nextInt(population.size()));
		int worstScore = fitness(worst);
		//	int i=0;
		int tempfit = 0;

		for (int i = 0; i <population.size(); i++){

			tempfit = fitness(population.get(i));
			if (tempfit < worstScore){
				worst =population.get(i);
				worstScore = tempfit;
				index = population.indexOf(worst);
			}


		}
		return index;
	}

	public static void mutation2(){
		//swaps 1st and last elements
		int mutin = 0;
		Job [] j = null;
		Job temp, temp2 = null;
		//	Random randomNum = new Random(System.nanoTime());
		j = population.get(randomNum.nextInt(population.size()));
		temp = j[mutin];
		temp2 = j[j.length -1];

		j[mutin] = temp2;
		j[j.length-1] = temp;


	}

	public static void mutation3(){
		//random job swap
		int mutin = 0;
		Job [] j = null;
		Job temp, temp2 = null;
		int i = 0,k = 0;
		//	Random randomNum = new Random(System.nanoTime());
		j = population.get(randomNum.nextInt(population.size()));

		k = randomNum.nextInt(j.length -1);
		i = randomNum.nextInt(j.length-1);
		temp = j[i];
		temp2 = j[k];

		j[i] = temp2;
		j[k] = temp;

	}

	public static int findWorst2(){
		int i = 0;
		int fit = 0;
		int bestfit = 0;
		int worstJob = 0;

		for (i=0; i <replacetour -1; i++){

			Job[] picked = population.get(randomNum.nextInt(population.size()));
			fit = fitness(picked);
			if (fit < bestfit){
				worstJob = population.indexOf(picked);
				//	worstJob = Arrays.copyOf(picked, picked.length);
				bestfit = fit;
			}
		}
		return worstJob;
	}
}

