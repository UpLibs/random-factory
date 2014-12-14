package randomfactory.generator;

import randomfactory.RandomGenerator;

final public class RandomGeneratorHybrid extends RandomGeneratorIntMaskBased {

	abstract static private class SubGeneratorCaller implements Cloneable {
		abstract public SubGeneratorCaller clone() ;
		abstract public int next32() ;
		abstract public int next(int nbits) ;
		abstract public RandomGenerator getGenerator() ;
	}
	
	final static private class SubGeneratorCallerGeneric extends SubGeneratorCaller {
		
		final private RandomGenerator generator ;
		
		public SubGeneratorCallerGeneric(RandomGenerator generator) {
			this.generator = generator;
		}
		
		@Override
		public SubGeneratorCallerGeneric clone() {
			return new SubGeneratorCallerGeneric(this.generator.clone()) ;
		}
		
		@Override
		public RandomGenerator getGenerator() {
			return generator ;
		}

		@Override
		public int next32() {
			return generator.nextInt() ;
		}
		
		@Override
		public int next(int nbits) {
			return generator.nextInt() & ((1 << nbits) - 1) ;
		}
	}
	
	final static private class SubGeneratorCallerIntMaskBased extends SubGeneratorCaller {

		final private RandomGeneratorIntMaskBased generator ;
		
		public SubGeneratorCallerIntMaskBased(RandomGeneratorIntMaskBased generator) {
			this.generator = generator;
		}
		
		@Override
		public SubGeneratorCallerIntMaskBased clone() {
			return new SubGeneratorCallerIntMaskBased(this.generator.clone()) ;
		}
		
		@Override
		public RandomGeneratorIntMaskBased getGenerator() {
			return generator ;
		}
		
		@Override
		public int next32() {
			return generator.next32() ;
		}

		@Override
		public int next(int nbits) {
			return generator.next(nbits) ;
		}
	}
	
	//////////////////////////////////////////
	
	private final SubGeneratorCaller[] generatorsCallers ;
	
	public RandomGeneratorHybrid( RandomGenerator... generators ) {
		if (generators == null || generators.length < 2) throw new IllegalArgumentException("Generators size should be > 2") ;
		
		this.generatorsCallers = new SubGeneratorCaller[generators.length] ;
		
		for (int i = 0; i < this.generatorsCallers.length; i++) {
			RandomGenerator generator = generators[i] ;
			
			SubGeneratorCaller caller ;
			
			if ( generator instanceof RandomGeneratorIntMaskBased ) {
				caller = new SubGeneratorCallerIntMaskBased( (RandomGeneratorIntMaskBased)generator ) ;
			}
			else {
				caller = new SubGeneratorCallerGeneric(generator) ;
			}
			
			this.generatorsCallers[i] = caller ;
		}
	}
	
	private RandomGeneratorHybrid(SubGeneratorCaller[] generatorsCallers,int nextCount) {
		this.generatorsCallers = generatorsCallers;
		this.nextCount = nextCount;
	}

	@Override
	public RandomGeneratorHybrid clone() {
		SubGeneratorCaller[] callers = new SubGeneratorCaller[this.generatorsCallers.length] ;
		
		for (int i = 0; i < callers.length; i++) {
			callers[i] = this.generatorsCallers[i].clone() ;
		}
		
		return new RandomGeneratorHybrid(callers, this.nextCount);
	}
	
	@Override
	public RandomGeneratorHybrid newInstance() {
		RandomGenerator[] gens = new RandomGenerator[this.generatorsCallers.length] ;
		
		for (int i = 0; i < gens.length; i++) {
			gens[i] = this.generatorsCallers[i].getGenerator().newInstance() ;
		}
		
		return new RandomGeneratorHybrid(gens) ;
	}
	
	@Override
	public RandomGeneratorHybrid newInstance(long... seed) {
		RandomGenerator[] gens = new RandomGenerator[this.generatorsCallers.length] ;
		
		for (int i = 0; i < gens.length; i++) {
			gens[i] = this.generatorsCallers[i].getGenerator().newInstance( seed[i % seed.length] ) ;
		}
		
		return new RandomGeneratorHybrid(gens) ;
	}
	
	public int getGeneratorsSize() {
		return this.generatorsCallers.length ;
	}
	
	public RandomGenerator[] getGenerators() {
		RandomGenerator[] generatos = new RandomGenerator[ this.generatorsCallers.length ] ;
		
		for (int i = 0; i < generatos.length; i++) {
			generatos[i] = this.generatorsCallers[i].getGenerator() ;
		}
		
		return generatos ;
	}
	
	private int nextCount = 0 ;
	
	@Override
	protected int next32() {
		SubGeneratorCaller caller = generatorsCallers[ nextCount++ % generatorsCallers.length ] ;
		return caller.next32() ;
	}
	
	@Override
	protected int next(int nbits) {
		SubGeneratorCaller caller = generatorsCallers[ nextCount++ % generatorsCallers.length ] ;
		return caller.next(nbits) ;
	}

	@Override
	public void initialize(long... seed) {
		
		for (int i = 0; i < this.generatorsCallers.length; i++) {
			RandomGenerator generator = this.generatorsCallers[i].getGenerator() ;
			
			long s = seed[ i % seed.length ] ;
			
			if ( i > seed.length ) {
				s += i ;
			}
			
			generator.initialize(s);
		}
		
	}

}
