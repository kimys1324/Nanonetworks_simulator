package domain.receivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.DataInterface;
import domain.particles.Particle;

public abstract class ColoredParticleReceiver extends ParticleReceiver {

	HashMap<Integer,Integer> totalCount;
		
	public ColoredParticleReceiver(String name, double x, double y,
			boolean absorb, boolean accumulate) {
		super(name, x, y, absorb, accumulate);
		super.iscolored = 1;
		totalCount = new HashMap<>();
	}
	
	
	public HashMap<Integer,Integer> ccount(ArrayList<Particle> particlesList){
		HashMap<Integer, Integer> count = complexcount(particlesList);
		
		
		if(_accumulate){
			//totalCount += count;
			Iterator<Integer> iteratorKey = count.keySet( ).iterator( );
			int key = -1;
			if(iteratorKey.hasNext()) key = iteratorKey.next();
			int i = 0;
			while(true && key != -1){
				if(count.containsKey(i)){
					if(totalCount.containsKey(i)){
						totalCount.put(i, totalCount.get(i)+count.get(i) );
					}
					else{
						totalCount.put(i,  count.get(i));
					}
					if(iteratorKey.hasNext()){
						key = iteratorKey.next();
					}
					else break;
				}
				
				i++;
			}	
			return totalCount;
		}
		return count;
	}	
	
	abstract HashMap<Integer, Integer> complexcount(ArrayList<Particle> particlesList);
}
