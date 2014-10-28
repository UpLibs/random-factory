package randomfactory;

import randomfactory.generator.RandomGeneratorThreadLocal;

abstract public class RandomGeneratorConcurrent extends RandomGenerator {
	
	static public RandomGeneratorConcurrent newDefaultGenerator() {
		return new RandomGeneratorThreadLocal() ;
	}
	
	static public RandomGeneratorConcurrent newDefaultGenerator(long seed) {
		return new RandomGeneratorThreadLocal(seed) ;
	}

}
