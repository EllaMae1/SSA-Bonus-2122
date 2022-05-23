package Simulation;

public class ServiceDesk extends Machine {

    private  Sink service_sink;
    private  Sink regular_sink;
    private Queue regular_desk_q;



    public ServiceDesk(Queue service_q, Queue regular_desk_q, Sink regular_sink, Sink service_sink, CEventList e, String n) {
        // mean processing time is 4.1/min
        super(service_q, service_sink, e, n, (4.1*60));

        // standard deviation is 1.1/minute
        setStandardDeviation(1.1*60);

        // regular desk queue
        this.regular_desk_q = regular_desk_q;

        // regular queue and sink to which service desk has access
        this.regular_sink = regular_sink;

        // service sink
        this.service_sink = service_sink;

        //open service desk
        this.open();
    }

    public int getQLength(){
        return this.regular_desk_q.getQueueLength() + this.getQueue().getQueueLength();
    }
}
