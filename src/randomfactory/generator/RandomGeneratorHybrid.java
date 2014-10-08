package randomfactory.generator;

import randomfactory.RandomGenerator;

final public class RandomGeneratorHybrid extends RandomGeneratorIntMaskBased {

	abstract static private class SubGeneratorCaller {
		abstract public int next(int nbits) ;
		abstract public RandomGenerator getGenerator() ;
	}
	
	final static private class SubGeneratorCallerGeneric extends SubGeneratorCaller {
		
		final private RandomGenerator generator ;
		
		public SubGeneratorCallerGeneric(RandomGenerator generator) {
			this.generator = generator;
		}
		
		@Override
		public RandomGenerator getGenerator() {
			return generator ;
		}

		@Override
		public int next(int nbits) {
			return generator.nextInt() ;
		}
	}
	
	final static private class SubGeneratorCallerIntMaskBased extends SubGeneratorCaller {

		final private RandomGeneratorIntMaskBased generator ;
		
		public SubGeneratorCallerIntMaskBased(RandomGeneratorIntMaskBased generator) {
			this.generator = generator;
		}
		
		@Override
		public RandomGeneratorIntMaskBased getGenerator() {
			return generator ;
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
