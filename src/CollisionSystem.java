import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;

/**
 * @author andrewjoseph
 */
public class CollisionSystem {
    private final static double HZ = 0.5;

    private MinPQ<Event> pq;
    private double t = 0.0;
    private Particle[] particles;

    public CollisionSystem(Particle[] particles) {
        this.particles = particles.clone();
    }

    private void predict(Particle a, double limit) {
        if (a == null)
            return;

        //p to p collision
        Particle min = null;
        for (Particle i: particles) {
            if (a.collides(i) <= limit && a.collides(i) > 0 && a != i) {
                if(min == null || a.collides(i) < a.collides(min)) {
                  min = i;
                }
            }
        }
        if (min != null) {
            pq.insert(new Event(t + a.collides(min), a, min));
        }

        //p to wall collisions
        double dtX = a.collidesX();
        double dtY = a.collidesY();
        if (t + dtX <= limit) {
            pq.insert(new Event(t + dtX, a, null));
        }
        if (t + dtY <= limit) {
            pq.insert(new Event(t + dtY, null, a));
        }
    }

    private void redraw(double limit) {
        StdDraw.clear();
        for (int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(20);

        if (t < limit) {
            pq.insert(new Event(t + 1 / HZ, null, null));
        }
    }

    public void simulate(double limit) {

        pq = new MinPQ<Event>();
        for (int i = 0; i < particles.length; i++) {
            predict(particles[i], limit);
        }
        pq.insert(new Event(0, null, null));  //redraw event

        System.out.println("pq size before loop: " + pq.size());
        //simulation
        while (!pq.isEmpty()) {
            Event e = pq.delMin();
            if (!e.wasSuperveningEvent()) {
                continue;
            }
            Particle a = e.getParticle1();
            Particle b = e.getParticle2();

            for (Particle particle : particles) {
                particle.move(e.getTime() - t);
            }
            t = e.getTime();

            if (a != null && b != null) {
                a.bounce(b);
            } else if (a != null && b == null) {
                a.bounceX();
            } else if (a == null && b!= null) {
                b.bounceY();
            } else if (a == null && b == null) {
                redraw(limit);
            }

            System.out.println("pq size inside loop: " + pq.size());

            predict(a, limit);
            predict(b, limit);
        }
    }
}
