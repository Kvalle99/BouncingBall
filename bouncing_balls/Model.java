package bouncing_balls;

import java.lang.Math.*;

/**
 * The physics model.
 * 
 * This class is where you should implement your bouncing balls model.
 * 
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 * 
 * @author Simon Robillard
 *
 */
class Model {

	double areaWidth, areaHeight;

	double gravity = 9.81;
	
	Ball [] balls;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		// Initialize the model with a few balls
		balls = new Ball[2];
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT
		for (Ball b : balls) {
			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {
				b.vx *= -1; // change direction of ball
			}
			if (b.y < b.radius || b.y > areaHeight - b.radius) {
				b.vy *= -1;
			}

			//TODO add logic for collision with another ball
			checkCollison(b);

			// compute new position according to the speed of the ball
			calcVY(b,deltaT);
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
		}
	}
	void calcVY (Ball ball, double deltaT) {
		ball.vy = ball.vy - (deltaT*gravity);
	}

	boolean checkCollison(Ball b) {
		calcBallsPolarCords(b);
		for (Ball ball : balls) {
			calcBallsPolarCords(ball);


			double distance = calcDistancePolarCords(b, ball);
			double totalRadius = b.radius+ball.radius;
			//System.out.println(distance + ":" + totalRadius);

			//if not the same ball and distance between centers are less than their combined radius
			if( b != ball && Math.abs(calcDistancePolarCords(b, ball)) <= b.radius+ball.radius){
				System.out.println("Collision");
				//TODO use physics to calc new speed (momentum calculations)
				return true;
			}
		}
		return false;
	}


	void calcBallsPolarCords (Ball b) {
		b.polarRadius = Math.sqrt((b.x*b.x) + (b.y*b.y));
		b.polarAngle = Math.atan(b.y/b.x); //gives answer in radians
	}

	double calcDistancePolarCords (Ball a, Ball b){
		return Math.sqrt((a.polarRadius*a.polarRadius) + (b.polarRadius*b.polarRadius) - (2*a.polarRadius*b.polarRadius*Math.cos(a.polarAngle-b.polarAngle)));
	}

	/**
	 * Simple inner class describing balls.
	 */
	class Ball {
		
		Ball(double x, double y, double vx, double vy, double r) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius, polarRadius, polarAngle;
	}
}
