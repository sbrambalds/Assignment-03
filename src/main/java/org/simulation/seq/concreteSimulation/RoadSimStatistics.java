package org.simulation.seq.concreteSimulation;

import java.util.List;

import org.simulation.seq.car.AbstractAgent;
import org.simulation.seq.car.CarAgent;
import org.simulation.seq.environment.AbstractEnvironment;
import org.simulation.seq.environment.SimulationListener;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements SimulationListener {

	
	private double averageSpeed;
	private double minSpeed;
	private double maxSpeed;
	
	public RoadSimStatistics() {
	}
	
	@Override
	public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		// TODO Auto-generated method stub
		// log("reset: " + t);
		averageSpeed = 0;
	}

	@Override
	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		double avSpeed = 0;
		
		maxSpeed = -1;
		minSpeed = Double.MAX_VALUE;
		for (var agent: agents) {
			CarAgent car = (CarAgent) agent;
			double currSpeed = car.getCurrentSpeed();
			avSpeed += currSpeed;			
			if (currSpeed > maxSpeed) {
				maxSpeed = currSpeed;
			} else if (currSpeed < minSpeed) {
				minSpeed = currSpeed;
			}
		}
		
		if (agents.size() > 0) {
			avSpeed /= agents.size();
		}
		log("average speed: " + avSpeed);
	}
	
	public double getAverageSpeed() {
		return this.averageSpeed;
	}

	public double getMinSpeed() {
		return this.minSpeed;
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	
	private void log(String msg) {
		System.out.println("[STAT] " + msg);
	}

}