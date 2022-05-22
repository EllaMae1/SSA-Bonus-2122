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

    /*
    public void execute(int type, double tme)
    {
        // show arrival
        System.out.println("GPU Job Arrival at time = " + tme);
        // give arrived product to queue
        Product p = new Product(1);

        p.stamp(tme,"Creation",name);
        queue.giveProduct(p);
        // generate duration
        if(meanArrTime>0)
        {
            double duration = Source.drawPoissonDist(meanArrTime);
            // Create a new event in the eventlist
            list.add(this,0,tme+duration); //target,type,time
        }
        else
        {
            interArrCnt++;
            if(interarrivalTimes.length>interArrCnt)
            {
                list.add(this,0,tme+interarrivalTimes[interArrCnt]); //target,type,time
            }
            else
            {
                list.stop();
            }
        }
    }
        */

    public static double drawPoissonDist(double t)
    {
        double lambda = t;
        double time = -Math.log(1 - Math.random()) * lambda;
        return  time;
    }
}
