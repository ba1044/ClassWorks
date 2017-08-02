/**
 * Created by Bindu on 2/26/17.
 */



import java.io.*;
import java.util.*;
public class MMQ1Controller {


    public static final int Arrival = 0; // represents arrival
    public static final int Departure = 1; // represents departure


    /* CLASS VARIABLES */
    LinkedList<MMQ1Event> schedule_Queue;     // storing the queue for the system
    double clock;                    // current time
    double endTime;                    // end time
    double nextArrival;                // the time of the next scheduled arrival to the system
    double nextDeparture;            // the time of the next scheduled departure from the system
    double lambda_arrivalRate;           // the rate of arrivals to the system
    double ts;                        // the mean service time
    ArrayList<Double> checkpoints;    // list of monitoring checkpoint times
    int numChecks;                    // number of checkpoints

    double Tq, Tw, Ts;
    int w, q;
    int requests, serviced;
    int index;
    protected  static int event,arr_Event,dept_Event;

    static int pkt,dept_pkt;


    /* INITIALIZE A MMQ1Controller OBJECT */
    public MMQ1Controller(double lambda_arrivalRate, double ts, double endTime) {
        this.lambda_arrivalRate = lambda_arrivalRate;
        this.ts = ts;
        this.endTime = endTime;

        /* Pre-generate a list of monitoring events for ease */
        checkpoints = new ArrayList<Double>();
        double n = 0;
        while (n < 2 * endTime) {
            numChecks++;
            n += exponential(lambda_arrivalRate);
            checkpoints.add(n);
        }


        clock = 0;
        schedule_Queue = new LinkedList<MMQ1Event>();
        nextArrival = exponential(lambda_arrivalRate);
        nextDeparture = Double.POSITIVE_INFINITY;  // no departures initially scheduled

        Tq = 0;
        Tw = 0;     //waiting time
        Ts = 0;
        w = 0;
        q = 0;
        requests = 0;
        serviced = 0;
        int qs;
    }

    /* Determines whether to queue the object up or begin serving it */
    public void ArrivalHandler(double time) {
         pkt++;

      //  System.out.println("Packet "+ pkt+  "   Arrival in Queue");
        event++;
        arr_Event++;
        //System.out.println("Queue Size"+schedule_Queue.size());
        if (schedule_Queue.isEmpty()) { // if the queue is empty, schedule a departure
            scheduleDeparture(time);
        } else { // queue isn't empty, schedule a Arrival
            schedule_Queue.add(new MMQ1Event(time, Arrival));
            System.out.println("Queue Size"+schedule_Queue.size());
        }
        nextArrival += exponential(lambda_arrivalRate); // arrange the next arrival
    }


    /* Removes the packet from the beginning of the queue that has been serviced
     * and determine if there is another object to service next
     */
    public void departureHandler() {
        //schedule_Queue.remove(); // pop the completed request out of the queue
        event++;
        dept_Event++;
        dept_pkt++;
       // System.out.println("Packet departure "+ dept_pkt     +"from queue");
        if (!schedule_Queue.isEmpty()) { // if there is a pending request, modify its contents to be a death event
            MMQ1Event next = schedule_Queue.remove();
            scheduleDeparture(next.getTime());
        } else {
            nextDeparture = Double.POSITIVE_INFINITY; // no pending requests, no pending deaths
            nextArrival += exponential(lambda_arrivalRate);

        }
    }

    /* Convenient function to plan out the departure of an object
     * and also collect some statistics about its time in the
     * system.
     */
    public void scheduleDeparture(double arrivalTime) {
        nextDeparture = clock + exponential(1 / ts);
        serviced++;
        Tq += (nextDeparture - arrivalTime);
        Tw += (clock - arrivalTime);System.out.println("Waiting time:"+Tw);
        Ts += (nextDeparture - clock);System.out.println("Service time:"+Ts);

    }

    /* Observes the queue at exponentially random intervals and
     * notes w and q.
     */
    public void queueHandler(PrintWriter out) {
        int cur_q = schedule_Queue.size();
      // System.out.println("Current Queue size"+cur_q);
        int cur_w = (cur_q > 0) ? (schedule_Queue.size() - 1) : 0;
        w += cur_w;
        q += cur_q;
        checkpoints.remove(0);
        //System.out.println("checkpoint: " + clock);out.println("checkpoint: " + clock);
        //System.out.println("\tnum waiting (w): " + cur_w);out.println("\tnum waiting (w): " + cur_w);

    }

    /* HELPER METHODS */
    public static double exponential(double lambda_arrivalRate) {
        Random r = new Random();
        double x = Math.log(1 - r.nextDouble()) / (-lambda_arrivalRate);
        return x;
    }


    /* MAIN SIMULATION FUNCTION */
    public void run(PrintWriter out) {
        while (clock < 2 * endTime) {

            System.out.println("");
            if (checkpoints.get(0) < nextArrival && checkpoints.get(0) < nextDeparture) {
                clock = checkpoints.get(0);
               // out.println("\tmonitoring at " + clock);
                //System.out.println("\tmonitoring at " + clock);
                queueHandler(out);
            } else if (nextArrival <= nextDeparture) { // an object has arrived to the system
                clock = nextArrival;
               // out.println("\t Packet arrival at " + clock);
              //  System.out.println("\t Packet arrival at " + clock);
                ArrivalHandler(nextArrival);

                requests++;
               System.out.println("\t Packet "+requests+ " arrival at " + clock);
            } else {    // element has been serviced, remove it from queue
                clock = nextDeparture;
              //  out.println("\t Packet departure at " + clock);
                System.out.println("\t Packet departure at " + clock);
                departureHandler();
            }
            index++;
        }
        System.out.println("Total no of event"+event);
        System.out.println("No of Arrival Event"+arr_Event);
        System.out.println("No of Departure Event"+dept_Event);

        printStats(out);
        out.close();
    }

    public void printStats(PrintWriter out) {
    /*    System.out.println("\nSTATISTICS OF RUN");
        System.out.println("w = " + w / numChecks);
        System.out.println("q = " + q / numChecks);
        System.out.println("Tw = " + Tw / requests);
        System.out.println("Tq = " + Tq / serviced);
        System.out.println("Ts = " + Ts / serviced);
*/
        System.out.println("STATISTICS OF RUN");
        System.out.println("requests: " + requests);
        System.out.println("w = " + w / numChecks);
        System.out.println("q = " + q / numChecks);
        System.out.println("Tw = " + Tw / requests);
        System.out.println("Tq = " + Tq / serviced);
        System.out.println("Ts = " + Ts / serviced);
    }
}
