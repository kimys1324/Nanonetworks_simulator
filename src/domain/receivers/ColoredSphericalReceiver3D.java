package domain.receivers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import domain.particles.Particle;
import domain.particles.ParticleXComparator;

public class ColoredSphericalReceiver3D extends ColoredSphericalReceiver{

	double _z;
	
	
	public ColoredSphericalReceiver3D(String name, double x, double y, double z,
			boolean absorb, boolean accumulate, double radius) {
		super(name, x, y, absorb, accumulate, radius);
		this._z=z;
		// TODO Auto-generated constructor stub
	}
	
	public int simpleCount(ArrayList<Particle> particlesList) {
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		int count = 0;
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
		while(it.hasNext()){

			p = it.next();
			if(p.getX() > _x+_radius) break;
			if(p.getX() >= _x-_radius){
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)+(p.getZ())*(p.getZ())<=_radius*_radius){
					count++;

					System.out.println("particle z location"+p.getZ());
					if(_absorb) {
						toDeleteList.add(p);
					}
				}
			}
		}
		for(Particle pa: toDeleteList) particlesList.remove(pa);
		return count;
	}
	
	public String getDescription()
	{
		String s = super.getDescription();
		s += "z location (nm): "+_z+"\n";
		
		return s;
	}
	
	HashMap<Integer, Integer> complexcount(ArrayList<Particle> particlesList) {
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		HashMap<Integer,Integer> count = new HashMap<>();
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
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
		return count;
	}
	
}
