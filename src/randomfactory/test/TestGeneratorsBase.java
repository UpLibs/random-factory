package randomfactory.test;

import randomfactory.RandomGenerator;


public class TestGeneratorsBase {

	public TestGeneratorsBase() {
	}

	private RandomGenerator randomGenerator ;
	
	public RandomGenerator getRandomGenerator() {
		return randomGenerator;
	}
	
	public void setRandomGenerator(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
}
