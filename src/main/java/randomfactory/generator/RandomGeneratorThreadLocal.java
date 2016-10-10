package randomfactory.generator;

import java.util.concurrent.atomic.AtomicLong;

import randomfactory.RandomGenerator;

final public class RandomGeneratorThreadLocal extends RandomGeneratorIntMaskBasedConcurrent {

	public RandomGeneratorThreadLocal() {
		this( new RandomGeneratorMersenneTwister() ) ;
	}
	
	public RandomGeneratorThreadLocal(long seed) {
		this( new RandomGeneratorMersenneTwister(seed) ) ;
	}
	
	public RandomGeneratorThreadLocal(RandomGenerator... randomGenerators) {
		if ( randomGenerators == null || randomGenerators.length == 0 ) throw new IllegalArgumentException("invalid randomGenerators: "+ randomGenerators) ;
		
		if ( randomGenerators.length == 1 ) {
			this.mainGenerator = randomGenerators[0] ;
		}
		else {
			this.mainGenerator = new RandomGeneratorHybrid(randomGenerators) ;
		}
	}
	
	final private RandomGenerator mainGenerator ;
	
	final private ThreadLocal<RandomGenerator> threadLocalGenerator = new ThreadLocal<RandomGenerator>() {
		protected RandomGenerator initialValue() {
			return mainGenerator.newInstance( createSeed() ) ;	
		};
	};
	
	final private AtomicLong seedCount = new AtomicLong(0L) ;
	
	public long createSeed() {
		long seed = System.nanoTime() ;
		
		synchronized (mainGenerator) {
			seed = seed ^ mainGenerator.nextLong() ;
			seed = seed ^ mainGenerator.nextLong() ;
		}
		
		seed = seed + seedCount.incrementAndGet() ;
		
		return seed ;
	}
	
	public RandomGenerator getRandomGenerator() {
		return threadLocalGenerator.get() ;
	}
	
	@Override
	protected int next32() {
		return threadLocalGenerator.get().nextInt() ;
	}
	
	public void fillFloats(float[] a) {
		fillFloats(a, a.length);
	}
	
	public void fillFloats(float[] a, int size) {
		RandomGenerator randomGenerator = threadLocalGenerator.get() ;
		
		for (int i = size-1; i >= 0; i--) {
			a[i] = ( randomGenerator.nextInt() >>> (32 - 24) ) / ((float)(1 << 24));
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

	@Override
	public RandomGeneratorThreadLocal clone() {
		return new RandomGeneratorThreadLocal( getRandomGenerator() ) ;
	}
	@Override
	public RandomGeneratorThreadLocal newInstance() {
		return new RandomGeneratorThreadLocal( this.mainGenerator.newInstance() );
	}

	@Override
	public RandomGeneratorThreadLocal newInstance(long... seed) {
		return new RandomGeneratorThreadLocal( this.mainGenerator.newInstance(seed) );
	}

	@Override
	public void initialize(long... seed) {
		getRandomGenerator().initialize(seed);
	}
	
}
