package randomfactory;

import randomfactory.RandomGeneratorConcurrent;
import randomfactory.generator.RandomGeneratorMersenneTwister;
import randomfactory.generator.RandomGeneratorThreadLocal;



public class BenchmarkRandom implements Runnable {
	
	//private static final RandomGeneratorConcurrent rng = RandomGeneratorConcurrent.newDefaultGenerator() ;
	//private static final Random rng = new Random();
	
	private static final RandomGeneratorConcurrent rng = new RandomGeneratorThreadLocal( new RandomGeneratorMersenneTwister() ) ;
	
	//private static final RandomGenerator rng = new RandomGeneratorXorShift() ;
	
	
	private void start() {
		int nThreads = 4;
		
		for (int i=0; i<nThreads; i++) {
			new Thread(this).start();
		}
	}

	public static void main(String[] args) {
		new BenchmarkRandom().start();
	}

	@Override
	public void run() {
		//FastRandomThreadSafe rng = new FastRandomThreadSafe();
		//Random rng = new Random();
		
		int nTests = 50000000;
		
		long time = System.currentTimeMillis();
		
		for (int i=0; i<nTests; i++) {
			//rng.nextDouble();
			//rng.nextInt() ;
			rng.nextFloat() ;
			//rng.nextLong() ;
		}
		
		time = System.currentTimeMillis() - time ;
		double speed = nTests / ( time/1000D) ;
		
		synchronized (System.out) {
			System.out.println("--------------------------------------------");
			System.out.println("Thread finished generating "+nTests);
			System.out.println("Time: "+time+" ms.");
			System.out.println("Speed: "+ speed);	
		}
		
		
	}
}
