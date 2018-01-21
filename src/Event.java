/**
 * @author andrewjoseph
 */
public class Event implements Comparable<Event> {

    private final double time;
    private final int countA, countB;
    private final Particle a, b;

    public Event(double t, Particle a, Particle b) {
        this.a = a;
        this.b = b;
        this.time = t;

        if (a != null) {
            countA = a.getCollisionCount();
            //System.out.println("countA " + countA);
        } else {
            countA = -1;
        }
        if (b != null) {
            countB = b.getCollisionCount();
            //System.out.println("countB " + countB);
        } else {
            countB = -1;
        }
    }

    public double getTime() {
        return time;
    }

    public Particle getParticle1() {
        return a;
    }

    public Particle getParticle2() {
        return b;
    }

    @Override
    public int compareTo(Event x) {
      int ev =  Double.compare(this.time, x.time);
      //System.out.println("compare " + ev);
      return ev;
    }

    public boolean wasSuperveningEvent() {
        if (a != null && a.getCollisionCount() != countA) {
            return false;
        }
        if (b != null && b.getCollisionCount() != countB) {
            return false;
        }
        return true;
    }
}
