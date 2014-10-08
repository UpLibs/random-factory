package randomfactory.test;

import java.util.HashMap;
import java.util.Map.Entry;

import randomfactory.RandomGenerator;
import randomfactory.RandomTools;
import randomfactory.generator.RandomGeneratorHybrid;
import randomfactory.generator.RandomGeneratorMersenneTwister;
import randomfactory.generator.RandomGeneratorSIMDOrientedFastMersenneTwister;
import randomfactory.generator.RandomGeneratorXorShift;

public class TestGenerators {

	public static void main(String[] args) {
		
		RandomGeneratorSIMDOrientedFastMersenneTwister randGen1 = new RandomGeneratorSIMDOrientedFastMersenneTwister( 
				//-6173673850639005589L 
				) ;
		
		RandomGeneratorMersenneTwister randGen2 = new RandomGeneratorMersenneTwister() ;
		
		RandomGeneratorXorShift randGen3 = new RandomGeneratorXorShift() ;
		
		
		RandomGeneratorHybrid randGenHyb = new RandomGeneratorHybrid( randGen1 , randGen2 , randGen3 ) ;
		
		//randGenHyb.initialize( RandomTools.createSeeds( randGenHyb.getGeneratorsSize() ) );
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		System.out.println("SEED> "+ randGen1.getSeedSet() );
		
		RandomGenerator randGen = randGen1 ;
		
		HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();
		
		int total = 100000000 ;
		
		for (int i = 0; i < total ; i++) {
			
			int n = randGen.nextInt(10) ;
			
			if (n < 0) throw new IllegalStateException("n: "+ n) ;
			
			Integer c = count.get(n) ;
			
			c = c == null ? 1 : c+1 ;
			
			count.put(n,c) ;
			
			//System.out.println(i+"> "+ n +" = "+ c);
			
		}
		
		double distCount = (total*1d)/count.size() ;
		
		for (Entry<Integer, Integer> entry : count.entrySet()) {
			int v = entry.getValue() ;
			
			double ratio = v / distCount ;
			
			double err = 1 - ratio ;
			if (err < 0) err = -err ;
			
			//if ( err < 0.001 ) err = 0 ;
			
			System.out.println(entry +" \t\t> "+ err);
		}
		
	}
	
}
