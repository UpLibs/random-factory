package randomfactory.generator;

import randomfactory.RandomGenerator;

abstract public class RandomGeneratorIntMaskBased extends RandomGenerator {

	abstract protected int next(int nbits) ;
	
	@Override
	public int nextInt() {
		return next(32) ;
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
        if ((n & -n) == n)  // i.e., n is a power of 2
            return (int)((n * (long)next(31)) >> 31);

        int bits, val;
        do {
            bits = next(31);
            val = bits % n;
        }
        while (bits - val + (n-1) < 0);
        
        return val;
    }

	
}
