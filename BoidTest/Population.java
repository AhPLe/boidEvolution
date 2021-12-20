package ea;

// Population class.

public interface Population {
		
	// Simulates one generation using the parent
	// selection mechanism specificed by selector.
	public void runGeneration(Selector selector);
	
	public Individual at(int index);
	
	public int size();
	
	public int getGeneration();
	
	public double avgFitness();
	
	public Individual getBestIndividual();
	
}
