package Simulation;

public class RegularDeskSource implements CProcess {

    /** Eventlist that will be requested to construct events */
    private final CEventList list;
    /** Queue that buffers products for the machine */
    private final Queue queue;
    /** Name of the source */
    private final String name;
    /** Mean interarrival time */
    private final double meanArrTime;
    /** Interarrival times (in case pre-specified) */
    private double[] interarrivalTimes;
    /** Interarrival time iterator */
    private int interArrCnt;

    private final Queue service_desk_q;


    public RegularDeskSource(Queue regular_q, Queue service_desk_q, CEventList l, String name) {
        this.list = l;
        this.queue = regular_q;
        this.name = name;

        //
        this.service_desk_q = service_desk_q;

        // regular customers arrive at a rate of 1 per minute
        this.meanArrTime = 60;

        // put first event in list for initialization
        list.add(this,0,Source.drawPoissonDist(meanArrTime));
    }

    @Override
    public void execute(int type, double tme)
    {
        // show arrival
        System.out.println("Regular Job Arrival at time = " + tme);
        // give arrived product to queue
        Product p = new Product(0);
        p.stamp(tme,"Creation",name);

        //policy for determining to which queue the job will go to
        if(queue.getQueueLength() >= service_desk_q.getQueueLength())
        {
            service_desk_q.giveProduct(p);
        }
        else
        {
            queue.giveProduct(p);
        }

        double duration = drawPoissonDist( meanArrTime);
        // Create a new event in the eventlist
        list.add(this,0,tme+duration); //target,type,time

        // generate duration
//        if(meanArrTime>1)
//        {
//            double duration = drawPoissonDist( meanArrTime);
//            // Create a new event in the eventlist
//            list.add(this,0,tme+duration); //target,type,time
//        }
//        else
//        {
//            interArrCnt++;
//            if(interarrivalTimes.length>interArrCnt)
//            {
//                list.add(this,0,tme+interarrivalTimes[interArrCnt]); //target,type,time
//            }
//            else
//            {
//                list.stop();
//            }
//        }
    }
    public static double drawPoissonDist(double t)
    {
        double lambda = t;
        double time = -Math.log(1 - Math.random()) * lambda;
        return  time;
    }


}
