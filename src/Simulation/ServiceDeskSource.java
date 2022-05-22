package Simulation;

public class ServiceDeskSource implements CProcess {

    /** Eventlist that will be requested to construct events */
    private final CEventList list;
    /** Queue that buffers products for the machine */
    private final ProductAcceptor queue;
    /** Name of the source */
    private final String name;
    /** Mean interarrival time */
    private final double meanArrTime;

    public ServiceDeskSource(ProductAcceptor q, CEventList l, String n)
    {
        list = l;
        queue = q;
        name = n;

        // service desk customers arrive with mean arrival time 5 per minute
        double meanArrTimePerMinute = 5;
        meanArrTime= meanArrTimePerMinute/60;

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
