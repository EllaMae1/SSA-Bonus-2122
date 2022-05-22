package Simulation;

public class RegularDesk extends Machine {

    public RegularDesk(Queue q, ProductAcceptor s, CEventList e, String n) {
        // mean processing time is 2.6min
        super(q, s, e, n, (2.6*60));

        // standard deviation is 1.1 min
        setStandardDeviation(1.1*60);
    }
}
