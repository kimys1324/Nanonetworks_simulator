package domain.receivers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import domain.particles.Particle;
import domain.particles.ParticleXComparator;

public class ColoredSphericalReceiver extends ColoredParticleReceiver {

	double _radius;
	
	public ColoredSphericalReceiver(String name, double x, double y,
			boolean absorb, boolean accumulate, double radius) {
		super(name, x, y, absorb, accumulate);
			_radius = radius;	
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
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)<=_radius*_radius){
					count++;
					if(_absorb) {
						toDeleteList.add(p);
					}
				}
			}
		}
		for(Particle pa: toDeleteList) particlesList.remove(pa);
		return count;
	}

	public String getDescription() {
		String s = super.getDescription();
		s += "receiver radius (nm): " + _radius + "\n"; 
		return s;
	}

	@Override
	HashMap<Integer, Integer> complexcount(ArrayList<Particle> particlesList) {
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		HashMap<Integer,Integer> count = new HashMap<>();
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
		while(it.hasNext()){
			p = it.next();
			if(p.getX() > _x+_radius) break;
			if(p.getX() >= _x-_radius){
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)<=_radius*_radius){
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

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeMotion() {
		// TODO Auto-generated method stub
		
	}	
}
