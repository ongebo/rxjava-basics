package com.example.rxjavabasics;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class RandomIntegers {

    /**
     * A switch to stop the stream of random integers. It's declared with the volatile keyword
     * to handle thread synchronization.
     */
    static volatile boolean runStream = true;

    /**
     * Creates a stream of random integers in the range 0 - 10000 with a back-pressure
     * strategy of dropping excess integers that the consumer can't handle.
     *
     * @return a Flowable stream of integers.
     */
    static Flowable<Integer> getIntegerStream() {
        return Flowable.create(emitter -> {
            while (runStream) {
                int randomInteger = (int) (Math.random() * 10000);
                emitter.onNext(randomInteger);
            }
            emitter.onComplete();
        }, BackpressureStrategy.DROP);
    }
}
