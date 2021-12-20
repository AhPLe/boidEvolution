package ea;

// Population class.

public interface Population {
	
	protected Individual[] pop;
	protected Individual[] pop_temp;
	protected int popsize;
	protected int gen;
	protected double maxFit;
	protected double avgFit;
	protected int bestIndiv;
	
	// Constructor takes an array of Individuals
	// as an initial population. Please make all
	// the elements in the Individuals array the
	// same class type; otherwise, the algorithm
	// will attempt to do inter-species breeding
	// and a runtime error will most likely occur.
	public Population(Individual[] population);
	
	// Simulates one generation using the parent
	// selection mechanism specificed by selector.
	public void runGeneration(Selector selector);
	
	public Individual at(int index) { return pop[index]; }
	
	public int size();
	
	public int generation();
	
	public double avgFitness() { return avgFit; }
	
	public Individual getBestIndividual() { return pop[bestIndiv]; }
	
	protected void repopulate(Selector selector);
	
	protected void updateStats();
	
}
