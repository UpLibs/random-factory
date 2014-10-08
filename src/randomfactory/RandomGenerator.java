package randomfactory;

abstract public class RandomGenerator {

	abstract public void initialize(long... seed) ;
	
	abstract public int nextInt() ;
	
	abstract public int nextInt(int n) ;
	
	abstract public long nextLong() ;
	
	abstract public boolean nextBoolean() ;
	
	abstract public float nextFloat() ;
	
	abstract public double nextDouble() ;
	
	
	
}
