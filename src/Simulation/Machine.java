package Simulation;



/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Machine implements CProcess,ProductAcceptor
{
	/** Product that is being handled  */
	public Product product;
	/** Eventlist that will manage events */
	public final CEventList eventlist;
	/** Queue from which the machine has to take products */
	public Queue queue;
	/** Sink to dump products */
	public ProductAcceptor sink;
	/** Status of the machine (b=busy, i=idle) */
	public char status;
	/** Machine name */
	public final String name;
	/** Mean processing time */
	public double meanProcTime;
	/** Processing times (in case pre-specified) */
	public double[] processingTimes;
	/** Processing time iterator */
	public int procCnt;
	/** Standard deviation	 */
	private double standardDeviation;

	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		meanProcTime=30;
		queue.askProduct(this);
	}

	public Machine(Queue q, ProductAcceptor s, CEventList e, String n, double meanProcTime)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		this.meanProcTime = meanProcTime;
		queue.askProduct(this);
	}


	/**
	*	Method to have this object execute an event
	*	@param type	The type of the event that has to be executed
	*	@param tme	The current time
	*/
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Product finished at time = " + tme);
		// Remove product from system
		product.stamp(tme,"Production complete",name);
		sink.giveProduct(product);
		product=null;
		// set machine status to idle
		status='i';
		// Ask the queue for products
		queue.askProduct(this);
	}
	
	/**
	*	Let the machine accept a product and let it start handling it
	*	@param p	The product that is offered
	*	@return	true if the product is accepted and started, false in all other cases
	*/
        @Override
	public boolean giveProduct(Product p)
	{
		// Only accept something if the machine is idle
		if(status=='i')
		{
			// accept the product
			product=p;
			// mark starting time
			product.stamp(eventlist.getTime(),"Production started",name);
			// start production
			startProduction();
			// Flag that the product has arrived
			return true;
		}
		// Flag that the product has been rejected
		else return false;
	}
	
	/**
	*	Starting routine for the production
	*	Start the handling of the current product with an exponentionally distributed processingtime with average 30
	*	This time is placed in the eventlist
	*/
	private void startProduction()
	{
		// generate duration
		if(meanProcTime>0)
		{
			double duration = drawRandomNormal(meanProcTime,standardDeviation);
			//double duration = drawRandomExponential(meanProcTime);
			// Create a new event in the eventlist
			double tme = eventlist.getTime();
			eventlist.add(this,0,tme+duration); //target,type,time
			// set status to busy
			status='b';
		}
		else
		{
			if(processingTimes.length>procCnt)
			{
				eventlist.add(this,0,eventlist.getTime()+processingTimes[procCnt]); //target,type,time
				// set status to busy
				status='b';
				procCnt++;
			}
			else
			{
				eventlist.stop();
			}
		}
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double std){
		this.standardDeviation = std;
	}

	public Queue getServiceQueue() {
		return this.queue;
	}

	public static double drawRandomNormal(double mean, double std)
	{
		double r, x, y;

		// find a uniform random point (x, y) inside unit circle
		do {
			x = 2.0 * Math.random() - 1.0;
			y = 2.0 * Math.random() - 1.0;
			r = x*x + y*y;
		}
		while (r > 1 || r == 0);

		// apply the Box-Muller formula to get standard Gaussian z
		double z = x * Math.sqrt(-2.0 * Math.log(r) / r);

		double norm = z*std + mean;
		if (norm <1){
			norm = 1;
		}
		// print it to standard output
		return norm;
	}
}