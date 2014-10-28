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
	
	final private ThreadLocal<RandomGenerator> threadLocalGenerator = new ThreadLocal<RandomGenerator>() ;
	
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
		RandomGenerator generator = threadLocalGenerator.get() ;
		if (generator != null) return generator ;
		
		generator = mainGenerator.newInstance( createSeed() ) ;
		
		threadLocalGenerator.set(generator);
		
		return generator ;
	}
	
	@Override
	protected int next32() {
		return getRandomGenerator().nextInt() ;
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
