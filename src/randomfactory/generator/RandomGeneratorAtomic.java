package randomfactory.generator;

import java.util.concurrent.atomic.AtomicInteger;

import randomfactory.RandomGenerator;

final public class RandomGeneratorAtomic extends RandomGeneratorIntMaskBased {

	public RandomGeneratorAtomic() {
		this( new RandomGeneratorMersenneTwister() ) ;
	}
	
	public RandomGeneratorAtomic(RandomGenerator... randomGenerators) {
		if ( randomGenerators == null || randomGenerators.length == 0 ) throw new IllegalArgumentException("invalid randomGenerators: "+ randomGenerators) ;
		
		if ( randomGenerators.length == 1 ) {
			this.bufferGen = randomGenerators[0] ;
		}
		else {
			this.bufferGen = new RandomGeneratorHybrid(randomGenerators) ;
		}
		
		fillBuffer();
	}
	
	static final private int BUFFER_SIZE = 1024*16 ;
	
	final private int[] buffer = new int[BUFFER_SIZE] ;
	final private AtomicInteger bufferCursor = new AtomicInteger(0) ;
	final private RandomGenerator bufferGen ;
	
	private int fillBuffer() {
		synchronized (bufferGen) {
			int val = bufferGen.nextInt() ;
			
			for (int i = 0; i < BUFFER_SIZE ; i++) {
				buffer[i] = bufferGen.nextInt() ;
			}
			bufferCursor.set(0);
			
			return val ;
		}
	}
	
	public int nextInt() {
		do {
			int cursorNow = bufferCursor.get() ;
			int cursorNext = cursorNow+1 ;
			
			if ( cursorNext < BUFFER_SIZE && bufferCursor.compareAndSet(cursorNow, cursorNext) ) {
				return buffer[cursorNext] ;
			}
			else {
				synchronized (bufferGen) {
					cursorNow = bufferCursor.get() ;
					cursorNext = cursorNow+1 ;
					
					if ( cursorNext >= BUFFER_SIZE ) {
						return fillBuffer() ;
					}
					else if ( bufferCursor.compareAndSet(cursorNow, cursorNext) ) {
						return buffer[cursorNext] ;
					}
				}
			}
		}
		while (true) ;
	}

	@Override
	protected int next32() {
		return nextInt() ;
	}


	private RandomGeneratorAtomic(RandomGeneratorAtomic other) {
		synchronized (other.bufferGen) {
			this.bufferCursor.set(other.bufferCursor.get());
			this.bufferGen = other.bufferGen.clone() ;
			System.arraycopy(other.buffer, 0, this.buffer, 0, this.buffer.length);
		}
	}

	@Override
	public RandomGeneratorAtomic clone() {
		return new RandomGeneratorAtomic(this) ;
	}
	@Override
	public RandomGeneratorAtomic newInstance() {
		return new RandomGeneratorAtomic( this.bufferGen.newInstance() );
	}

	@Override
	public RandomGeneratorAtomic newInstance(long... seed) {
		return new RandomGeneratorAtomic( this.bufferGen.newInstance(seed) );
	}

	@Override
	public void initialize(long... seed) {
		synchronized (this.bufferGen) {
			this.bufferGen.initialize(seed);
			fillBuffer() ;
		}
	}
	
}
