package randomfactory.test;

import randomfactory.RandomGenerator;
import randomfactory.RandomTools;
import randomfactory.generator.RandomGeneratorSIMDOrientedFastMersenneTwister;

public class TestGenerators {

	public static void main(String[] args) {
		
		RandomGenerator randGen = new RandomGeneratorSIMDOrientedFastMersenneTwister() ;
		
		randGen.initialize( RandomTools.createSeed() );
		
		for (int i = 0; i < 100; i++) {
			
			int n = randGen.nextInt() ;
			
			System.out.println(i+"> "+ n);
			
		}
		
		
	}
	
}
