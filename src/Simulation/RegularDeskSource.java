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

        // Solving for shortest queue for regular customers
        int shortest_q = Integer.MAX_VALUE;
        Machine shortest_desk = Simulation.all_desks.get(0);

        for(Machine desk : Simulation.all_desks){
            int q_length = 0;
            if(desk.isOpen()){
                q_length = desk.getQueue().getQueueLength();
                if(desk.getClass() == ServiceDesk.class)
                    q_length = ((ServiceDesk) desk).getQLength();

                if(q_length < shortest_q) {
                    shortest_q = q_length;
                    shortest_desk = desk;
                }
            }

            System.out.println("shortest q length = " + q_length);
        }

        shortest_desk.giveProduct(p);

        // check if we need to open a new cash register
        if(allOpenDesksAreFull() && thereIsAClosedDesk()){
            openAClosedDesk();
        }

        // calculate duration
        double duration = drawPoissonDist(meanArrTime);

        // Create a new event in the event list
        list.add(this,0,tme+duration); //target,type,time

    }
    public static double drawPoissonDist(double t)
    {
        double lambda = t;
        double time = -Math.log(1 - Math.random()) * lambda;
        return  time;
    }

    public static boolean allOpenDesksAreFull(){
        for(Machine desk : Simulation.all_desks) {
            if(desk.isOpen()){
                int q_length = desk.getQueue().getQueueLength();
                if (desk.getClass() == ServiceDesk.class)
                    q_length = ((ServiceDesk) desk).getQLength();
                if(q_length < 4)
                    return false;
            }
        }
        return true;
    }

    public static boolean thereIsAClosedDesk(){
        for(Machine desk : Simulation.all_desks) {
            if(!desk.isOpen()){
                return true;
            }
        }
        return false;
    }

    public static void openAClosedDesk(){
        Machine desk = Simulation.all_desks.get(0);
        int index = 1;
        while(desk.isOpen()){
            desk = Simulation.all_desks.get(index);
            index++;
        }
        desk.open();
    }
}
