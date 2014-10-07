package randomfactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RandomTools {
	
	static final private Random random = new Random() ;

	static final private AtomicLong seedCount = new AtomicLong(0L) ;
	
	static public long createSeed() {
		long seed = System.nanoTime() ;
		
		synchronized (random) {
			seed = seed ^ random.nextLong() ;
			seed = seed ^ random.nextLong() ;
		}
		
		seed = seed + seedCount.incrementAndGet() ;
		
		return seed ;
	}
	
	static public long[] createSeeds(int size) {
		long[] seeds = new long[size] ;
		
		for (int i = 0; i < seeds.length; i++) {
			seeds[i] = createSeed() ;
		}
		
		return seeds ;
	}
	
}
