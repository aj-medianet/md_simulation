import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
/**
 *
 * @author andrewjoseph
 */
public class Particle {
    private double rx, ry, vx, vy, mass, radius;
    private Color color;
    private int collisionCount = 0;

    public Particle() {

    }

    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.radius = radius;
        this.color = color;

        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
        StdDraw.show();
    }

    public void move(double t) {
        rx += vx * t;
        ry += vy * t;
    }

    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
    }

    public double collidesX() {
        if(vx > 0) {
            return (1 - this.radius - this.rx) / this.vx;
        } else if (vx < 0) {
            return (this.radius - this.rx) / this.vx;
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double collidesY() {
        if(vy > 0) {
            return (1 - this.radius - this.ry) / this.vy;
        } else if(vy < 0) {
            return (this.radius - this.ry) / this.vy;
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double collides(Particle b) {
        if (this == b) {
            return Double.MAX_VALUE;
        }
        double dx = b.rx - this.rx;
        double dy = b.ry - this.ry;
        double dvx = b.vx - this.vx;
        double dvy = b.vy - this.vy;
        double dvdr = (dx * dvx) + (dy * dvy);

        if (dvdr > 0) {
            return Double.MAX_VALUE;
        }
        double dvdv = (dvx * dvx) + (dvy * dvy);
        double drdr = (dx * dx) + (dy * dy);
        double sigma = this.radius + b.radius;
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);

        if (d < 0) {
            return Double.MAX_VALUE;
        }

        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    public void bounceX() {
      System.out.println("bounceX");
        this.vx *= -1;
        this.collisionCount++;
    }

    public void bounceY() {
      System.out.println("bounceY");
        this.vy *= -1;
        this.collisionCount++;
    }

    public void bounce(Particle b) {
        double dx = b.rx - this.rx;
        double dy = b.ry - this.ry;
        double dvx = b.vx - this.vx;
        double dvy = b.vy - this.vy;
        double dvdr = (dx * dvx) + (dy * dvy);
        double dist = this.radius + b.radius;

        //magnitude of normal force
        double magnitude = 2 * this.mass * b.mass * dvdr / ((this.mass + b.mass) * dist);

        //normal force
        double forceX = magnitude * dx / dist;
        double forceY = magnitude * dy / dist;

        //updates velocities according to normal force
        this.vx += forceX / this.mass;
        this.vy += forceY / this.mass;

        b.vx -= forceX / this.mass;
        b.vy -= forceY / this.mass;

        b.collisionCount++;
        this.collisionCount++;
    }

    public int getCollisionCount() {
        return this.collisionCount;
    }
}
