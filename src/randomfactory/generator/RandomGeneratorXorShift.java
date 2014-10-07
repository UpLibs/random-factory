package randomfactory.generator;

import randomfactory.RandomTools;


final public class RandomGeneratorXorShift extends RandomGeneratorIntMaskBased {
	
	public RandomGeneratorXorShift() {
		initialize( RandomTools.createSeed() ) ;
	}

	private long seed;

	@Override
	public void initialize(long... seed) {
		this.seed = seed[0];
	}

	@Override
	public int next(int nbits) {
		long x = seed;
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		seed = x;
		x &= ((1L << nbits) - 1);
		return (int) x;
	}
	
}
