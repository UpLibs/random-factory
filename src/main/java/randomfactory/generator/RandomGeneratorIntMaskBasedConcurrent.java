package randomfactory.generator;

import randomfactory.RandomGeneratorConcurrent;

abstract public class RandomGeneratorIntMaskBasedConcurrent extends RandomGeneratorConcurrent {

	@Override
	abstract public RandomGeneratorIntMaskBasedConcurrent clone() ;
	
	@Override
	abstract public RandomGeneratorIntMaskBasedConcurrent newInstance() ;
	
	@Override
	abstract public RandomGeneratorIntMaskBasedConcurrent newInstance(long... seed) ;

	abstract protected int next32() ;
	
	protected int next(int nbits) {
		return next32() >>> (32 - nbits) ;
	}
	
	@Override
	public int nextInt() {
		return next32() ;
	}

	@Override
	public long nextLong() {
        return ((long)(next32()) << 32) + next32();
    }
	
	@Override
	public boolean nextBoolean() {
		return next(1) != 0;
	}

	@Override
	public float nextFloat() {
		return next(24) / ((float)(1 << 24));
	}

	@Override
	public double nextDouble() {
		return (((long)(next(26)) << 27) + next(27)) / (double)(1L << 53);
	}
	
	@Override
	public int nextInt(int n) {
		
		n++ ;
		
		int val ;
		do {
			val = next(31) % n ;
		}
		while (val <= 0) ;
		
		return val-1 ;
		
    }

}
