package randomfactory.test;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Test;

import randomfactory.RandomGenerator;
import randomfactory.generator.RandomGeneratorAtomic;
import randomfactory.generator.RandomGeneratorHybrid;
import randomfactory.generator.RandomGeneratorMersenneTwister;
import randomfactory.generator.RandomGeneratorSIMDOrientedFastMersenneTwister;
import randomfactory.generator.RandomGeneratorThreadLocal;
import randomfactory.generator.RandomGeneratorXorShift;

public class TestDistribution {
	
	static final public boolean FAST_TEST = true ;
	
	static final public long[] SEEDS = new long[] {
		123456789L,
		1343432L ,
		5764334145235636L ,
		-2456354376L,
		-5745523235587824355L,
		1445675564232645645L,
		8956675785474L,
		-55621545756767L,
		3432112323324223L,
		-67867454564645L,
		7348821245L,
		-5480273489481L,
		
		8587623588918076600L ,
		-8264151040368321171L ,
		304902674233577295L ,
		2648364644169858221L ,
		-1437215012418197157L ,
		7727384737696662500L ,
		8414576676774683917L ,
		7804237138280971753L ,
		-8949511429979919673L ,
		7555374012724165763L,
		
		6199356937955155985L ,
		8184143441079239953L ,
		-2998104621203447855L ,
		8356321431352564431L ,
		5988485044965493617L ,
		4807397097354098380L ,
		1051412706461012729L ,
		-4268466929856599225L ,
		1539125997851728298L ,
		-3615767655901861324L ,
		
		4117936480094606155L ,
		7940236184929877544L ,
		8969991034918273248L ,
		3361885891888708048L ,
		-8951689395691247280L ,
		-7091064770337688970L ,
		4528863778684163951L ,
		2025498945684639569L ,
		12297836323962585L ,
		4207071810546770011L ,

		6616547283354140007L ,
		-2984793094465905630L ,
		-4622494886380876580L ,
		2106763533897434204L ,
		5898870277521249080L ,
		6303561034703892633L ,
		-4209071707710476765L ,
		-8816305565309587582L ,
		-7512827906355200759L ,
		-8688926710020911462L ,

	} ;
	
	///////////////////////////////////////////////////////////////////////
	
	@Test
	public void checkDistribution_MersenneTwister() {
		checkDistribution( RandomGeneratorMersenneTwister.class );
	}
	
	@Test
	public void checkDistribution_SIMDOrientedFastMersenneTwister() {
		checkDistribution( RandomGeneratorSIMDOrientedFastMersenneTwister.class );
	}
	
	@Test
	public void checkDistribution_XorShift() {
		checkDistribution( RandomGeneratorXorShift.class );
	}
	
	@Test
	public void checkDistribution_Atomic_MersenneTwister() {
		checkDistribution( RandomGeneratorAtomic.class , new RandomGeneratorMersenneTwister() );
	}
	
	@Test
	public void checkDistribution_Atomic_Hybrid() {
		checkDistribution( RandomGeneratorAtomic.class , new RandomGeneratorMersenneTwister() , new RandomGeneratorSIMDOrientedFastMersenneTwister() , new RandomGeneratorXorShift() );
	}
	
	@Test
	public void checkDistribution_ThreadLocal_MersenneTwister() {
		checkDistribution( RandomGeneratorThreadLocal.class , new RandomGeneratorMersenneTwister() );
	}
	
	@Test
	public void checkDistribution_ThreadLocal_Hybrid() {
		checkDistribution( RandomGeneratorThreadLocal.class , new RandomGeneratorMersenneTwister() , new RandomGeneratorSIMDOrientedFastMersenneTwister() , new RandomGeneratorXorShift() );
	}

	
	@Test
	public void checkDistribution_Hybrid() {
		checkDistribution( RandomGeneratorHybrid.class , new RandomGeneratorMersenneTwister() , new RandomGeneratorSIMDOrientedFastMersenneTwister() , new RandomGeneratorXorShift() );
	}
	
	private RandomGenerator createRandomGenerator(long seed, Class<? extends RandomGenerator> randGenClass, RandomGenerator... params) {
		
		try {
			Constructor<? extends RandomGenerator> constructor ;
			
			if ( params == null || params.length == 0 ) {
				constructor = null ;
			}
			else {
				constructor = randGenClass.getConstructor( RandomGenerator[].class ) ;
			}
			
			RandomGenerator randGen = constructor != null ? constructor.newInstance( (Object)params ) : randGenClass.newInstance() ;
			
			randGen.initialize(seed);
			
			return randGen ;
		}
		catch (Exception e) {
			throw new IllegalStateException(e) ;
		}
		
	}
	
	private void checkDistribution(Class<? extends RandomGenerator> randGenClass, RandomGenerator... params) {
		RandomGenerator randGenBase = createRandomGenerator(1, randGenClass, params) ;
		
		for (int i = 0; i < SEEDS.length; i++) {
			long seed = SEEDS[i] ;
			checkDistribution( randGenBase.newInstance(seed) );
		}
	}
	
	private void checkDistribution(RandomGenerator randGen) {
		checkDistribution_nextBoolean(randGen);
		checkDistribution_nextInt(randGen);
	}
	
	private void checkDistribution_nextBoolean(RandomGenerator randGen) {
		
		int[] count = new int[2] ;
		
		int total = FAST_TEST ? 1000000 : 10000000 ;
		double totalD = total ;
		
		for (int i = 0; i < total; i++) {
			int n = randGen.nextBoolean() ? 0 : 1 ;
			count[n]++ ;
		}
		
		double distribution = totalD / count.length ;
		
		double tolerance = 0.05 ;
		
		double minDist = distribution * (1-tolerance) ;
		double maxDist = distribution * (1+tolerance) ;
		
		for (int i = 0; i < count.length; i++) {
			int c = count[i] ;
			
			assertTrue( "distribution: "+ minDist +" <= "+ c +" <= "+ maxDist , c >= minDist && c <= maxDist ) ;
		}
		
	}
	
	private void checkDistribution_nextInt(RandomGenerator randGen) {
		
		int[] count = new int[100] ;
		
		int total = FAST_TEST ? 1000000 : 10000000 ;
		double totalD = total ;
		
		for (int i = 0; i < total; i++) {
			int n = randGen.nextInt(100) ;
			count[n]++ ;
		}
		
		double distribution = totalD / count.length ;
		
		double tolerance = 0.05 ;
		
		double minDist = distribution * (1-tolerance) ;
		double maxDist = distribution * (1+tolerance) ;
		
		for (int i = 0; i < count.length; i++) {
			int c = count[i] ;
			
			assertTrue( c >= minDist && c <= maxDist ) ;
			
		}
		
	}

	////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void checkValid_MersenneTwister() {
		checkValid( RandomGeneratorMersenneTwister.class );
	}
	
	@Test
	public void checkValid_SIMDOrientedFastMersenneTwister() {
		checkValid( RandomGeneratorSIMDOrientedFastMersenneTwister.class );
	}
	
	@Test
	public void checkValid_XorShift() {
		checkValid( RandomGeneratorXorShift.class );
	}

	@Test
	public void checkValid_Atomic_MersenneTwister() {
		checkValid( RandomGeneratorAtomic.class , new RandomGeneratorMersenneTwister() );
	}
	
	@Test
	public void checkValid_Atomic_Hybrid() {
		checkValid( RandomGeneratorAtomic.class , new RandomGeneratorMersenneTwister() , new RandomGeneratorSIMDOrientedFastMersenneTwister() , new RandomGeneratorXorShift() );
	}
	
	@Test
	public void checkValid_ThreadLocal_MersenneTwister() {
		checkValid( RandomGeneratorThreadLocal.class , new RandomGeneratorMersenneTwister() );
	}
	
	@Test
	public void checkValid_ThreadLocal_Hybrid() {
		checkValid( RandomGeneratorThreadLocal.class , new RandomGeneratorMersenneTwister() , new RandomGeneratorSIMDOrientedFastMersenneTwister() , new RandomGeneratorXorShift() );
	}
	
	@Test
	public void checkValid_Hybrid() {
		checkValid( RandomGeneratorHybrid.class , new RandomGeneratorMersenneTwister() , new RandomGeneratorSIMDOrientedFastMersenneTwister() , new RandomGeneratorXorShift() );
	}

	private void checkValid(Class<? extends RandomGenerator> randGenClass, RandomGenerator... params) {
		RandomGenerator randGenBase = createRandomGenerator(1, randGenClass, params) ;
		
		for (int i = 0; i < SEEDS.length; i++) {
			long seed = SEEDS[i] ;
			checkValid( randGenBase.newInstance(seed) );
		}
	}
	
	private void checkValid(RandomGenerator randGen) {
		
		int total = FAST_TEST ? 1000000 : 10000000 ;
		
		for (int i = 0; i < total; i++) {
			float f = randGen.nextFloat() ;
			assertTrue(f >= 0 && f < 1) ;
			
			double d = randGen.nextDouble() ;
			assertTrue(d >= 0 && d < 1) ;
		}
		
	}

}
