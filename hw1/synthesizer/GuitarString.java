package synthesizer;

import java.util.Arrays;

//Make sure this class is public
public class GuitarString {
    /**
     * Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday.
     */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        buffer = new ArrayRingBuffer<>((int) (Math.round(SR / frequency)));
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // Make sure that your random numbers are different from each other.
        Double[] r = new Double[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            double random = Math.random() - 0.5;

            /** @source: doc / csdn
             *      Arrays.asList()
             *      数组转化成List集合的方法。
             *      !!! 特别注意 转化后的size为1
             * */
            //1: contains  0: doesn't contain -> move on
            while (Arrays.asList(r).contains(random)) {
                random = Math.random() - 0.5;
            }
            r[i] = random;
        }

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.dequeue();
            buffer.enqueue(r[i]);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double front = buffer.dequeue();
        double next = buffer.peek();
        double res = DECAY * 0.5 * (front + next);
        buffer.enqueue(res);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
