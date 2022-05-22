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
    /** Interarrival times (in case pre-specified) */
    private double[] interarrivalTimes;
    /** Interarrival time iterator */
    private int interArrCnt;

    public ServiceDeskSource(ProductAcceptor q, CEventList l, String n)
    {
        list = l;
        queue = q;
        name = n;

        // regular customers arrive at a rate of 5 per minute
        this.meanArrTime = 5*60;

        // put first event in list for initialization
        list.add(this,0,drawPoissonDist(meanArrTime));
    }

    @Override
    public void execute(int type, double tme) {
        System.out.println("Service Desk Job Arrival at time = " + tme);
        Product p = new Product(0);
        p.stamp(tme,"Creation",name);
        queue.giveProduct(p);

        if(meanArrTime>0)
        {
            double duration = drawPoissonDist(meanArrTime);
            // Create a new event in the eventlist
            list.add(this,0,tme+duration); //target,type,time
        }else
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

        // generate duration
        double duration = drawPoissonDist(meanArrTime);

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