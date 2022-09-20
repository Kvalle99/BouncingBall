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

	boolean checkedCollission = false;

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
			if (b.x < b.radius) {
				b.vx *= -1; // change direction of ball
				b.x = b.radius; //so that it doesnt go off screen
			}
			if (b.x > areaWidth - b.radius) {
				b.vx *= -1; // change direction of ball
				b.x = areaWidth - b.radius;
			}
			if (b.y < b.radius) {
				b.vy *= -1;
				b.y = b.radius;
				System.out.println("VY: " + b.vy);
			}
			if(b.y > areaHeight - b.radius){
				b.vy *= -1;
				b.y = areaHeight-b.radius;
			}
				// so that a collision isnt accounted for twice)
				if (!checkedCollission)
					checkCollison(b);
				else
					checkedCollission = false;
			// compute new position according to the speed of the ball
			calcVY(b,deltaT);
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
			b.bounceTime++;

		}
	}
	void calcVY (Ball ball, double deltaT) {
		ball.vy = ball.vy - (deltaT*gravity);
	}

	//for both balls
	void calcVXCollision (Ball ball, Ball collideWith) {
		//Uses formula for momentum to calculate new velocity in X-axis for both balls
		double oldVX = ball.vx;
		double kinetic = (ball.mass*ball.vx*ball.vx)/2 + (collideWith.mass*collideWith.vx*collideWith.vx)/2;
		//System.out.println("Kinetic energy X: " + kinetic);
		ball.vx = (((ball.mass-collideWith.mass)/(ball.mass+collideWith.mass))*oldVX) + ((2*collideWith.mass/(ball.mass+collideWith.mass))*collideWith.vx);
		collideWith.vx =(((collideWith.mass-ball.mass)/(ball.mass+collideWith.mass))*collideWith.vx) + ((2*ball.mass/(ball.mass+collideWith.mass))*oldVX);
	}
	void calcVYCollision (Ball ball, Ball collideWith) {
		//Uses formula for momentum to calculate new velocity in Y-axis for both balls
		double oldVY =ball.vy;
		double kinetic = (ball.mass*ball.vy*ball.vy)/2 + (collideWith.mass*collideWith.vy*collideWith.vy)/2;
		//System.out.println("Kinetic energy Y: " + kinetic);
		ball.vy = (((ball.mass-collideWith.mass)/(ball.mass+collideWith.mass))*oldVY) + ((2*collideWith.mass/(ball.mass+collideWith.mass))*collideWith.vy);
		collideWith.vy =(((collideWith.mass-ball.mass)/(ball.mass+collideWith.mass))*collideWith.vy) + ((2*ball.mass/(ball.mass+collideWith.mass))*oldVY);
	}

	void checkCollison(Ball a) {
		checkedCollission = true;
		calcBallsPolarCords(a);
		for(Ball b : balls) {
			calcBallsPolarCords(b);

			double distance = calcDistancePolarCords(a, b);
			double totalRadius = a.radius + b.radius;
			//System.out.println(distance + ":" + totalRadius);

			if (a != b && Math.abs(calcDistancePolarCords(a, b)) <= a.radius + b.radius) {
				System.out.println("Collision");
				calcVXCollision(a, b);
				calcVYCollision(a, b);
			}
		}
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
			this.mass = r*r;
			bounceTime = 20;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius, polarRadius, polarAngle, mass, bounceTime;
	}
}
