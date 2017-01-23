package domain.receivers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import domain.particles.Particle;
import domain.particles.ParticleXComparator;

public class MessageSphericalReceiver3D extends ColoredSphericalReceiver3D {

	private double nextSymboltime;
	private double thisSymboltime;
	private boolean timeSwitch;
	private boolean start=false;
	private double symbolTime;
	private boolean messageArrived = false;
	private String message;
	private int threshold;
	private double timeStep;


	public MessageSphericalReceiver3D(String name, double x, double y, double z,
			boolean absorb, boolean accumulate, double radius, double symboltime, double timeStep) {
		super(name, x, y, z, absorb, accumulate, radius);
		
		this.symbolTime = symboltime;
		this.nextSymboltime = symboltime;
		this.timeSwitch = false;
		this.thisSymboltime = -1;
		this.message = "message : ";
		this.threshold = 40;
		this.timeStep = timeStep;

		// TODO Auto-generated constructor stub
	}
	
	HashMap<Integer, Integer> complexcount(ArrayList<Particle> particlesList) {
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		HashMap<Integer,Integer> count = new HashMap<>();
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
		int sum = 0;
		while(it.hasNext()){
			p = it.next();

			//System.out.println("particle x location"+p.getX());
			//System.out.println("particle z location"+p.getZ());
			if(p.getX() > _x+_radius) break;
			if(p.getX() >= _x-_radius){
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)+(p.getZ()-_z)*(p.getZ()-_z)<=_radius*_radius){
					
					//System.out.println("particle z location"+p.getZ());
					if(count.containsKey(new Integer(p.getType()))){
						count.put(new Integer(p.getType()),new Integer(count.get(p.getType())+1));
					}
					else{
						count.put(new Integer(p.getType()), 1);
					}
					if(_absorb) {
						toDeleteList.add(p);
					}
				}
			}
		}
		for(Particle pa: toDeleteList) particlesList.remove(pa);

		if(count.containsKey(1))  sum = new Integer(count.get(new Integer(1)));
		
		//System.out.println("particle Num: " + sum);
		
		if(this.messageArrived == false && sum>0 )
		{
			if(sum>threshold) this.message+="1";
			this.thisSymboltime = 0;	
			this.nextSymboltime = this.symbolTime;
			this.messageArrived = true;
		}
		
		if(thisSymboltime >= nextSymboltime)
		{
			if(this.timeSwitch == false && this.start == true){ this.message+="0"; }
			System.out.println(this.message);
			this.thisSymboltime = nextSymboltime;
			this.nextSymboltime = this.thisSymboltime+symbolTime;
			this.timeSwitch = false;
		}
		
		
		
		if(this.timeSwitch==false && sum>threshold)
		{
			message+="1";
			this.timeSwitch = true;
			this.start = true;
		}
		
		this.thisSymboltime = this.thisSymboltime + timeStep;
		//System.out.println("thisSymboltime: "+ this.thisSymboltime);

		
		return count;
	}
	

}
