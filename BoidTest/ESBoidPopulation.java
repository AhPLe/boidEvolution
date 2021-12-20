package implementations;

import java.util.Random;
import ea.Individual;
import ea.Selector;
//import ea.Population;
import java.util.Arrays;
import java.util.Comparator;


public class ESBoidPopulation implements Population{//extends Population{
	
	//current two populations in case mulambda is used instead of mu+lambda
	protected ESBoidIndividual[] pop;
	protected ESBoidIndividual[] offspring;
	protected boolean popSorted = false;
	protected boolean childSorted = false;
	
	protected ESBoidIndividual[] pop_temp;
	protected int popsize;
	protected int gen;
	
	//currently scripted to using max fitness, expected use of -fitness would provide a best fit for a min fitness
	protected double minFit;
	protected double maxFit;
	protected double maxOffFit;
	protected double maxParFit;
	protected double avgFit;
	protected double avgOffFit;
	//protected int bestIndiv;
	protected int bestParent;
	protected int bestOffspring;
	
	protected static final int numoffspring = 5;
	protected static final boolean muplus = false;
	
	// the comparator allows a sorting function during repopulation
	class sortByFitness implements Comparator<ESBoidIndividual>{
		public int compare(ESBoidIndividual a, ESBoidIndividual b){
			//return Double.compare(a.fitness(), b.fitness());
      return -Double.compare(a.totalFitness, b.totalFitness);
		}
	}
	
	// Constructor takes an array of Individuals
	// as an initial population. Please make all
	// the elements in the Individuals array the
	// same class type; otherwise, the algorithm
	// will attempt to do inter-species breeding
	// and a runtime error will most likely occur.
	//@Override
	public ESBoidPopulation(ESBoidIndividual[] offspringPop) {
	
	#this constructor sets up the offspring instead of the population
	#there should be a selector to populate the offspring population.
	
	#TODO: merge this in with constructor (Individual[], selector) so the two use very similar code
	#possible extra option: use default ESSelector to call constructor (Individual[], selector) instead
	
	#have it implement the Population class
	#from there, have it send the parent class to the runner file
	#after that, repopulate using the child class
	#the trouble may be to make all this easily accessible and work together well
	
    minFit = 0;
    maxFit = 0;
    maxOffFit = 0;
    maxParFit = 0;
    avgFit = 0;
    avgOffFit = 0;
    //super(offspringPop);
		
	//do a small test to see if correct pop is used
	if (offspringPop.length%numOffspring != 0){
		System.out.println("Warning: normal constructor called with strange length, use ESBoidPopulation(ESBoidIndividual[], Selector) method instead");
	}
	
	popsize = offspringPop.length/numOffspring;
	
	gen = 0;
	pop = new ESBoidIndividual[popsize];
	offspring = new ESBoidIndividual[popsize*numoffspring];
	pop_temp = new ESBoidIndividual[popsize];//*numoffspring
	
	for (int i = 0; i < popsize; i++) {
		if (i < popsize){pop[i] = offspringPop[i];}
		offspring[i] = offspringPop[i];
		}
	
		selector.update(this);
		repopulate(selector);
		updateStats();
	}
	
	public ESBoidPopulation(ESBoidIndividual[] population, Selector selector) {
	
	#have it implement the Population class
	#from there, have it send the parent class to the runner file
	#after that, repopulate using the child class
	#the trouble may be to make all this easily accessible and work together well
    minFit = 0;
    maxFit = 0;
    maxOffFit = 0;
    maxParFit = 0;
    avgFit = 0;
    avgOffFit = 0;
    //super(population);
		
	//probably unnecessary 
	popsize = population.length;
	
	gen = 0;
	pop = new ESBoidIndividual[popsize];
	for (int i = 0; i < popsize; i++) pop[i] = population[i];//new ESBoidIndividual(population[i]);
	
	//end probably unnecessary
	
	offspring = new ESBoidIndividual[popsize*numoffspring];
	pop_temp = new ESBoidIndividual[popsize];//*numoffspring

	selector.update(this);
    repopulate(selector);
    updateStats();
	}
	
	// getter for individual
    public ESBoidIndividual getIndividual(int index) {
        return (ESBoidIndividual) pop[index];
    }
    
    public ESBoidIndividual getOffspring(int index) {
        return (ESBoidIndividual) offspring[index];
    }
	
	// shuffle the population
    // Implementing Fisher–Yates shuffle
    public void shuffle() {
        int index;
        ESBoidIndividual temp;
        Random r = new Random();
        for (int i = pop.length - 1; i > 0; i--) {
            index = r.nextInt(i + 1);
            //System.out.println("previous offspring temp 1: "+ Double.toString(pop_temp[0].getTrial(0)));
            temp = pop[index];
            pop[index] = pop[i];
            pop[i] = temp;
        }
		popSorted = false;
    }
	
	// // Simulates one generation using the parent
	// // selection mechanism specificed by selector.
	public void runGeneration(Selector selector) {
		
	selector.update(this);
    updateStats();
	repopulate(selector);
	gen++;
		
	}
	
	// public ESBoidIndividual at(int index) { return pop[index]; }
	
	// public int size() { return popsize; }
	
	// public int generation() { return gen; }
	
	// public double minFitness() { return minFit; }
	
	public double maxFitness() { return maxFit; }

	public double maxOffFitness() { return maxOffFit; }

	public double maxParFitness() { return maxParFit; }

	public double avgFitness() { return avgFit; }

	public double avgOffFitness() { return avgOffFit; }

	public int size() { return pop.length; }
	
	//public int offSize() { return offspring.length; }
  
	public int generation() { return gen; }
	
	public static int getNumOffspring() {return numoffspring; }
	
	public ESBoidIndividual at(int pos) {return pop[pos];}

	public void setIndTrial(int indPos, int trial, int fitness){
		pop[indPos].setTrial(trial, fitness);
	}

	public void setOffTrial(int indPos, int trial, int fitness){
		offspring[indPos].setTrial(trial, fitness);
	}

	public double getOffTrial(int indPos, int trial){
		return offspring[indPos].getTrial(trial);
	}

	//best individual is not implemented, but combines a sorted parent and child
	
	//TODO - how is the best individual chosen continuously? the list isn't sorted
	//implementation seems to work TODO - possibly improve
	public ESBoidIndividual getBestIndividual() { 
		if (offspring[bestOffspring].fitness() > pop[bestParent].fitness()) {return offspring[bestOffspring];}
		else {return pop[bestParent];}
	}
	
	public ESBoidIndividual getBestParent() { return pop[bestParent]; }
	public ESBoidIndividual getBestChild() { return offspring[bestOffspring]; }
	
	// public ESBoidIndividual getBestIndividual() { 
		// if (!muplus){return offspring[bestOffspring];}
		// else{
			// double childFit = pop[bestParent].fitness();
			// double parentFit = offspring[bestOffspring].fitness();
			// if (childFit > parentFit){return offspring[bestOffspring];}
			// else {return pop[bestParent];}
		// }
	// }
	
	//@Override - this should be inherited from population, but it is not
	protected void repopulate(Selector selector) {
		if (offspring[0] != null){
			childSorted = false;
			updateChildStats();
			//repopulate based on muplus or lambda selection
      
			//System.out.println("Pre sort 1: "+ Double.toString(offspring[0].getTrial(0)));
			//System.out.println("Pre sort 1: "+ offspring[0].ESString());
			//System.out.println("Pre sort best: "+ offspring[bestOffspring].ESString());
      
			if (!popSorted){
				Arrays.sort(pop, new sortByFitness());
				popSorted = true;
			}
			if (!childSorted){
				Arrays.sort(offspring, new sortByFitness());
				childSorted = true;
			}
      
			//System.out.println("Post sort best: "+ offspring[0].ESString());
			//System.out.println("Post sort worst: "+ offspring[offspring.length - 1].ESString());
		  
		  
			double childFit;
			double parentFit;
			// ESBoidIndividual bestOffspring;
			// ESBoidIndividual bestParent;
		  
			int parPos = 0;
			int childPos = 0;
			if (!muplus){
				for (int i=0; i < popsize; i++){
					pop_temp[i] = offspring[i];
				}
			}
			else{
				for (int i=0; i < popsize; i++){
				  //check if one has finished the array
					if (parPos < pop.length){
						parentFit = pop[parPos].fitness();
					}
					else{
						pop_temp[i] = offspring[childPos];
						childPos++;
						continue;
					}
					if (childPos < offspring.length){
						childFit = offspring[childPos].fitness();
					}
					else{
						pop_temp[i] = pop[parPos];
						parPos++;
						continue;
					}
			  
					if (childFit < parentFit){
						pop_temp[i] = pop[parPos];
						parPos++;
					}
					else{
						pop_temp[i] = offspring[childPos];
						childPos++;
					}
          
				}

			}
		//System.out.println("previous offspring temp 1: "+ Double.toString(offspring[0].getFitness(0)));
		//System.out.println("previous offspring temp 1: "+ Double.toString(offspring[0].getTrial(0)));
		//System.out.println("previous pop temp 1: "+ Double.toString(pop_temp[0].getFitness()));
		//System.out.println("previous offspring temp 1: "+ Double.toString(pop_temp[0].getTrial(0)));

		pop = pop_temp;
		//System.out.println("post pop temp 1: "+ Double.toString(pop[0].getFitness()));
		//System.out.println("post pop 1: "+ Double.toString(offspring[0].getTrial(0)));
		//System.out.println("post pop 1: "+ Double.toString(pop[0].getTrial(0)));
		//System.out.println("post pop temp 1: "+ Double.toString(pop_temp[0].getTrial(0)));
        
		}
		//crossover always exists, but may frequently return the main parent in ES
		for (int i = 0; i < popsize; i++){
			for (int j = 0; j < numoffspring; ++j){
				//just use i as parent 1
				int parent2_idx = selector.select();

				//Arrays.copyOf(genome, genome.length);.clone()
				ESBoidIndividual cGenome = pop[i].crossover((ESBoidIndividual)(pop[parent2_idx]));
        
				offspring[i*numoffspring + j] = new ESBoidIndividual(cGenome.getGenome(), cGenome.getSigma());//cGenome.length);
				
				offspring[i*numoffspring + j].mutate();
				//center variables does not seem to work yet
				//offspring[i*numoffspring + j].center_vars();
			}
		}
		
		
		//updateStats();
	}
	
	protected void updateChildStats() {
    
		double minChildFit = offspring[0].fitness();
		double maxChildFit = offspring[0].fitness();
		maxOffFit = 0;
		avgOffFit = 0;
		
		bestOffspring = 0;
		for (int i = 1; i < popsize*numoffspring; i++) {
			
			if (offspring[i].fitness() < minChildFit) {
				
				minChildFit = offspring[i].fitness();
				//bestOffspring = i;
				
			}
			if (offspring[i].fitness() > maxChildFit) {
				
				maxChildFit = offspring[i].fitness();
				bestOffspring = i;
				
			}
			avgOffFit += offspring[i].fitness();
      
			//if (offspring[i].fitness() > 0){
			//  System.out.println("something went right");
			//}
				
		}
		
		if (minChildFit < minFit){ minFit = minChildFit; }
		if (maxChildFit > maxFit){ maxFit = maxChildFit; }
		if (maxChildFit > maxOffFit){ maxOffFit = maxChildFit; }
		avgOffFit /= offspring.length;
	}
	
	protected void updatePopStats() {
		
		double minParFit = pop[0].fitness();
		maxParFit = pop[0].fitness();
		
		avgFit = 0.0;
		bestParent = 0;
  
		for (int i = 1; i < popsize; i++) {
			if (pop[i].fitness() < minParFit) {
				
				minParFit = pop[i].fitness();
				//bestParent = i;
				
			}
			else if (pop[i].fitness() > maxParFit) {
				
				maxParFit = pop[i].fitness();
				bestParent = i;
				
			}
			
			avgFit += pop[i].fitness();
		//System.out.println("average fit increase: " + Double.toString(avgFit));
			
		}
		
		//child fit comes after parent, so parent should set, with child possibly replacing
		minFit = minParFit;
		maxFit = maxParFit;
		
		// if (minParFit < minFit){ minFit = minParFit;}
		// if (maxParFit > maxFit){ maxFit = maxParFit;}
		
		avgFit /= (double)popsize;
	}
	
	//@Override
	protected void updateStats() {
    //System.out.println("update occurs" + Double.toString(avgOffFit));
    
		if (pop != null){updatePopStats();}
		if (offspring != null && offspring[0] != null) {updateChildStats();}
		
	}
	
}
