package Simulation;

public class ServiceDesk extends Machine {

    private final double standardDeviation;


    public ServiceDesk(Queue q, ProductAcceptor s, CEventList e, String n) {
        // mean processing time is 4.1/min
        super(q, s, e, n, (4.1/60));

        // standard deviation is 1.1/minute
        this.standardDeviation = (1.1/60) ;
    }

    private void startProduction()
    {
        // generate duration with Normal
        double duration = Machine.drawRandomNormal(meanProcTime, standardDeviation);

        // create new event
        double tme = eventlist.getTime();
        eventlist.add(this,0,tme+duration);

        // set status to busy
        status='b';
    }
}
