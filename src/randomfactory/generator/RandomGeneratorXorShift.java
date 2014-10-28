package randomfactory.generator;

import randomfactory.RandomTools;


final public class RandomGeneratorXorShift extends RandomGeneratorIntMaskBased {
	
	public RandomGeneratorXorShift() {
		this( RandomTools.createSeed() ) ;
	}
	
	public RandomGeneratorXorShift(long seed) {
		initialize(seed) ;
	}
	
	@Override
	public RandomGeneratorXorShift clone() {
		RandomGeneratorXorShift clone = new RandomGeneratorXorShift(1) ;
		clone.seed = this.seed ;
		return clone ;
	}
	
	@Override
	public RandomGeneratorXorShift newInstance() {
		return new RandomGeneratorXorShift() ;
	}
	
	@Override
	public RandomGeneratorXorShift newInstance(long... seed) {
		return new RandomGeneratorXorShift(seed[0]) ;
	}

	private long seed;

	@Override
	public void initialize(long... seed) {
		this.seed = seed[0];
	}

	@Override
	protected int next32() {
		long x = seed;
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		seed = x;
		return (int) x ;
	}
	
}
