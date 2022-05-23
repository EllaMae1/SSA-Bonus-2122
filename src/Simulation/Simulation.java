/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package Simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Simulation {

    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Machine machine;
    public ArrayList<Integer>servicedesk_delays;
    public ArrayList<Integer> regular_delays;
    public static int runs = 1;
    public static int sim_length = 1000;

    public static ArrayList<Machine> all_desks = new ArrayList<>();

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int counter = 0;
        double[][] output = new double[runs][6];
        while(counter < runs) {

            ArrayList<Double> servicedesk_delays = new ArrayList<Double>();
            ArrayList<Double> regular_delays = new ArrayList<Double>();
            ArrayList<Double> all_delays = new ArrayList<Double>();


            // Create an eventlist
            CEventList l = new CEventList();

            // A queue for the machine
            Queue servicedesk_q = new Queue();
            Queue regular_q = new Queue();

            // A source
            ServiceDeskSource sd_s = new ServiceDeskSource(servicedesk_q,l,"Service Desk Source");
            RegularDeskSource regular_s = new RegularDeskSource(regular_q,servicedesk_q,l, "Regular Desk Source");

            // A sink
            Sink service_desk_sink = new Sink("Service Desk sink");
            Sink regular_sink = new Sink("Regular sink");


            // machines
            ServiceDesk service_desk1 = new ServiceDesk (servicedesk_q,regular_q,regular_sink,service_desk_sink,l,"Service Desk 1");
            all_desks.add(service_desk1);
            // Open mandatory registers
            for (int i = 0; i < 5; i++) {
                Queue queue = new Queue();
                RegularDesk rd = new RegularDesk(queue, regular_sink, l, "regular desk " + i);
                if(i < 2)
                    rd.open();
                all_desks.add(rd);
            }

            l.start(sim_length);
            // main policy


            String[] events = regular_sink.getEvents();
            double[] times = regular_sink.getTimes();
            String[] stations = regular_sink.getStations();
            int[] numbers = regular_sink.getNumbers();
           int counter2 = 0;
           for(int i = 0; i< times.length; i++) {

            System.out.println("number = " + numbers[i]);
            System.out.println("event = " + events[i]);
            System.out.println("time = " + times[i]);
            System.out.println("station = " + stations[i]);
            System.out.println();
//
               if(counter2 == 2) {
                   double a = times[i-(counter2-1)] - times[i-counter2];
                   regular_delays.add(a);
                   all_delays.add(a);
                   counter2 = 0;
               }
               else counter2 ++;
           }

//            System.out.println("times array size = " + times.length);

            String[] sd_events = service_desk_sink.getEvents();
            double[] sd_times = service_desk_sink.getTimes();
            String[] sd_stations = service_desk_sink.getStations();
            int[] sd_numbers = service_desk_sink.getNumbers();
            int sd_counter = 0;
            for(int i = 0; i< sd_times.length; i++) {
				System.out.println("number = " + sd_numbers[i]);
				System.out.println("event = " + sd_events[i]);
				System.out.println("time = " + sd_times[i]);
				System.out.println("station = " + sd_stations[i]);
				System.out.println();

                if(sd_counter == 2) {
                    double delay = sd_times[i-(sd_counter-1)] - sd_times[i-sd_counter];
                    servicedesk_delays.add(delay);
                    all_delays.add(delay);
                    sd_counter = 0;
                }
                else sd_counter ++;
            }


            //calculate averages
            double sd_temp = 0;
            double sd_average;
            for(int i=0; i<servicedesk_delays.size(); i++) {
                sd_temp += servicedesk_delays.get(i);
            }
            sd_average = sd_temp/servicedesk_delays.size();

            double regular_temp = 0;
            double regular_average;
            for(int i=0; i<regular_delays.size(); i++) {
                regular_temp += regular_delays.get(i);
            }
            regular_average = regular_temp/regular_delays.size();

            System.out.println("regular delays array size = " + regular_delays.size());


            double all_temp = 0;
            double all_average;
            for(int i=0; i<all_delays.size(); i++) {
                all_temp += all_delays.get(i);
            }
            all_average = all_temp/all_delays.size();

            System.out.println("service desk average delay: "+sd_average);
            System.out.println("regular average delay: "+regular_average);
            System.out.println("all average delay: "+all_average);

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();

            for (int i = 0; i<servicedesk_delays.size(); i++) {
                sb1.append(servicedesk_delays.get(i));
                sb1.append(",");
            }
            for (int i = 0; i<regular_delays.size(); i++) {
                sb2.append(regular_delays.get(i));
                sb2.append(",");
            }
            sb1.append("\n");
            for (int i = 0; i<all_delays.size(); i++) {
                sb3.append(all_delays.get(i));
                sb3.append(",");
            }

            BufferedWriter br = new BufferedWriter(new FileWriter("batch_means_ServiceDesk.csv"));
            br.write(sb1.toString());
            br.close();

            BufferedWriter br2 = new BufferedWriter(new FileWriter("batch_means_regular.csv"));
            br2.write(sb2.toString());
            br2.close();

            BufferedWriter br3 = new BufferedWriter(new FileWriter("batch_means_all.csv"));
            br3.write(sb3.toString());
            br3.close();

            counter++;
        }
    }}