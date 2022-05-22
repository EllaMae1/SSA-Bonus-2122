package Simulation;

public class RegularDeskSource implements CProcess {

    /** Eventlist that will be requested to construct events */
    private final CEventList list;
    /** Queue that buffers products for the machine */
    private final ProductAcceptor queue;
    /** Name of the source */
    private final String name;
    /** Mean interarrival time */
    private final double meanArrTime;

    public RegularDeskSource(CEventList list, ProductAcceptor queue, String name) {

        this.list = list;
        this.queue = queue;
        this.name = name;

        // regular customers arrive at a rate of 1 per minute
        double ratePerMinute = 1;
        double ratePerSecond = ratePerMinute/60;
        this.meanArrTime = 1/ratePerSecond;

        // put first event in list for initialization
        list.add(this,0,Source.drawPoissonDist(meanArrTime));
    }

    @Override
    public void execute(int type, double tme) {
        Product p = new Product();
        p.stamp(tme,"Creation",name);
        queue.giveProduct(p);

        // generate duration
        double duration = Source.drawPoissonDist(meanArrTime);

        // add event to event list
        list.add(this,0,tme+duration);
    }
}
