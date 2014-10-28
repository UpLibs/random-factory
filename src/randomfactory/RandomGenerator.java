package randomfactory;

import randomfactory.generator.RandomGeneratorMersenneTwister;
import randomfactory.generator.RandomGeneratorThreadLocal;

abstract public class RandomGenerator implements Cloneable {

	static public RandomGenerator newDefaultGenerator() {
		return new RandomGeneratorMersenneTwister() ;
	}
	
	static public RandomGenerator newDefaultGenerator(long seed) {
		return new RandomGeneratorMersenneTwister(seed) ;
	}
	
	static public RandomGeneratorConcurrent newDefaultGeneratorConcurrent() {
		return new RandomGeneratorThreadLocal() ;
	}
	
	static public RandomGenerator newDefaultGeneratorConcurrent(long seed) {
		return new RandomGeneratorThreadLocal(seed) ;
	}
	
	/////////////////////////////////////////////////////////////////////////
	
	abstract public RandomGenerator clone() ;
	
	abstract public RandomGenerator newInstance() ;
	abstract public RandomGenerator newInstance(long... seed) ;
	
	abstract public void initialize(long... seed) ;
	
	abstract public int nextInt() ;
	
	abstract public int nextInt(int n) ;
	
	abstract public long nextLong() ;
	
	abstract public boolean nextBoolean() ;
	
	abstract public float nextFloat() ;
	
	abstract public double nextDouble() ;
	
	////////////////////////////////
	
	public void fillFloats(float[] a) {
		fillFloats(a, a.length);
	}
	
	public void fillFloats(float[] a, int size) {
		
		for (int i = size-1; i >= 0; i--) {
			a[i] = nextFloat() ;
		}
		
	}
	
	public void fillFloats(float[][] a) {
		for (int i = a.length-1 ; i >= 0; i--) {
			fillFloats( a[i] ) ;
		}
	}
	
	public void fillFloats(float[][] a, int size) {
		for (int i = a.length-1 ; i >= 0; i--) {
			float[] fs = a[i] ;
			
			int lng = size ;
			if (lng > fs.length) lng = fs.length ;
			
			fillFloats(fs, lng) ;
			
			size -= lng ;
			if (size <= 0) break ;
		}
	}
	
	
}
