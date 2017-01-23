/*
	N3Sim: A Simulation Framework for Diffusion-based Molecular Communication
    Copyright (C) 2011 Iñaki Pascual - N3CAT (UPC)
    N3Cat (NaNoNetworking Center in Catalunya)
	Universitat Politècnica de Catalunya (UPC)
	Jordi Girona, 1-3, Mòdul D6 (Campus Nord)
	08034 Barcelona, Catalunya, Spain
	acabello@ac.upc.edu

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package domain.collision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.PriorityQueue;

import data.DataInterface;
import domain.boundaries.HorizontalBoundary;
import domain.boundaries.VerticalBoundary;
import domain.particles.Particle;
import domain.particles.ParticleMinXComparator;
import domain.particles.Sphere;
import domain.space.Space;

public class Collisions {

	private static PriorityQueue<SphereCollision> _collisionsQueue;
	private static ArrayList<ArrayList<SphereCollision>> _collisionsList;
	
	private static int _initialAllCollisions;
	private static int _realCollisions;
	private static int _totalCollisions;
	
	// _particleList of space is supossed to be sorted with Xmin comparator
	public static void modifySpace(Space space){
		_realCollisions = 0;
		_initialAllCollisions = 0;
		long lastTime = System.currentTimeMillis();
		// build collisions data structure to store them according time
		createCollisionsQueue(space);
		if(space.getInfoFile()) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "create collisions queue time:"+ (System.currentTimeMillis()-lastTime) );
		lastTime = System.currentTimeMillis();
		// here we really solve the collisions
		solveCollisions(space);
		if(space.getInfoFile()) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "solving collisions time:"+ (System.currentTimeMillis()-lastTime) );
	}

	private static void createCollisionsQueue(Space space) {
		
		
		// iniatialize collisions data structures
		if(_collisionsQueue == null)
			_collisionsQueue = new PriorityQueue<SphereCollision>(100, new CollisionComparator());
		else _collisionsQueue.clear();
		
		if(_collisionsList == null)
			_collisionsList = new ArrayList<ArrayList<SphereCollision>>();
		else _collisionsList.clear();
		
		ArrayList<Particle> particleList = space.getParticleList();
		Collections.sort(particleList, new ParticleMinXComparator());
		
		// for each particle find its collisions as a time-sorted list,
		// save first one in priority queue and rest of list in _collisionsList 
		int size = particleList.size();
		for (int i=0; i<size; i++){
			ArrayList<SphereCollision> aux = findParticleCollisions(i, particleList, -1, space);
			if(aux!=null && aux.size()>0){
				_initialAllCollisions += aux.size();
				_collisionsQueue.add(aux.remove(0));
			}
			_collisionsList.add(aux);
		}
		_totalCollisions = _initialAllCollisions;
		if(space.getInfoFile()) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "initial number of first collisions : " + _collisionsQueue.size());
		if(space.getInfoFile()) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "initial number of all collisions : " + _initialAllCollisions);
	}
	
	// returns a time-sorted list of collisions among particle p and boundaries and other particles
	// i is the index of p in particle list and collisions must happen after value of parameter time
	// particleList is supossed to be sorted with Xmin comparator
	private static ArrayList<SphereCollision> findParticleCollisions(int i, ArrayList<Particle> particleList, double time, Space space){
		
		//check particle collisions against boundaries and other particles with higher list position
		// (particleList must be sorted with XMinComparator)
		ArrayList<SphereCollision> collisions = new ArrayList<SphereCollision>();
		//System.out.println("list size : " + particleList.size() + " ; index : " + i);
		Particle particle = particleList.get(i);
		double pMaxX = particle.getMaxX();
		double pMaxY = particle.getMaxY();
		double pMinX = particle.getMinX();
		double pMinY = particle.getMinY();
		
		int index = i+1;
		int numParticles = particleList.size();
		boolean end = false;
		double t = time;
		
		// sphere sphere collisions
		if(space.getActiveCollision()){
			while (index < numParticles && !end){
				Particle p2 = particleList.get(index);
				if(p2.getMinX() > pMaxX) end = true;
				else{
					if(particle.getMinY() < p2.getMinY()){
						if(p2.getMinY() <= particle.getMaxY()){
							SphereCollision c = new SphereSphereCollision((Sphere)particle, (Sphere)p2);
							t = c.getTime();
							if(t > time + 0.0000000000001){
								collisions.add(c);
							}
						}
					}
					else{
						if(particle.getMinY() <= p2.getMaxY()){
							SphereCollision c = new SphereSphereCollision((Sphere)particle, (Sphere)p2);
							t = c.getTime();
							if(t > time + 0.0000000000001){
								collisions.add(c);
							}
						}
					}
				}
				index++;
			}
		}
		
		// sphere-boundary collisions
		if(space.getBoundedSpace()){	
			double bMinX;
			double bMaxX;
			double bMinY;
			double bMaxY;
			ArrayList<HorizontalBoundary> hbl = space.getHorizontalBoundaries();	
			for (HorizontalBoundary hb : hbl){
				bMinY = hb.getYMin();
				bMinX = hb.getXMin();
				bMaxX = hb.getXMax();
				if((pMaxX >= bMinX)&&(pMinX <= bMaxX)&&(pMinY <= bMinY)&&(pMaxY >= bMinY)){
					SphereCollision c = new SphereHorizontalBoundaryCollision((Sphere)particle, hb);
//					c.checkCollision();
					t = c.getTime();
					if(t > time + 0.0000000000001){
						collisions.add(c);
					}
				}
			}
			
			for (VerticalBoundary vb : space.getVerticalBoundariesList()){
				bMinY = vb.getYMin();
				bMaxY = vb.getYMax();
				bMinX = vb.getXMin();
				
				
				if((pMaxY >= bMinY)&& (pMinY <= bMaxY)&& (pMinX <= bMinX) && (pMaxX >= bMinX)){
					SphereCollision c = new SphereVerticalBoundaryCollision((Sphere)particle, vb);
//					c.checkCollision();
					t = c.getTime();
					if(t> time + 0.0000000000001){
						collisions.add(c);
					}
				}
			}
		}

		Collections.sort(collisions, new CollisionComparator());
		return collisions;
	}
	
	//polls collisions from _collsions queue and solves them until it is empty
	private static void solveCollisions(Space space) {
		ArrayList<Particle> particleList = space.getParticleList();
		SphereCollision c = null;
		ArrayList<Sphere> spheresList = null;
		ArrayList<SphereCollision> auxCollisionList = new ArrayList<SphereCollision>();
		// System.out.println("xivato prewhile noempty");
		while (!_collisionsQueue.isEmpty()){
			// System.out.println("xivato while noempty, coll queue size = " + _collisionsQueue.size());
			// store in auxCollisionList all collisions with same time 
			auxCollisionList.clear();
			c =_collisionsQueue.poll();
			_totalCollisions++;
			// discard old collisions
			while(!c.checkTag() && !_collisionsQueue.isEmpty()){
				// System.out.println("xivato nochecktag");
				//add the next collision of sphere
				if(_collisionsList.get(particleList.indexOf(c.getSphere())).size()>0)
					_collisionsQueue.add(_collisionsList.get(particleList.indexOf(c.getSphere())).remove(0));
				c =_collisionsQueue.poll();
				_totalCollisions++;
			}
			// get the first valid one
			if(c.checkTag())
				auxCollisionList.add(c);
			// get collisions with same time
			while(!_collisionsQueue.isEmpty() && _collisionsQueue.peek().getTime() == c.getTime()){
				SphereCollision col = _collisionsQueue.poll(); _totalCollisions++;
				if(col.checkTag()) auxCollisionList.add(col);
				else {
					if(_collisionsList.get(particleList.indexOf(c.getSphere())).size()>0)
						_collisionsQueue.add(_collisionsList.get(particleList.indexOf(c.getSphere())).remove(0));
				}
			}
		    // solve each collision 
			for(SphereCollision sc : auxCollisionList){
				spheresList = sc.getSpheresList();
				Collections.sort(spheresList, new ParticleMinXComparator());
				// System.out.println("xivato while presolvecollision");
				sc.solveCollision();
				// System.out.println("xivato while postsolvecollision");
				_realCollisions++;
				for(Sphere s : spheresList)	s.incrementTag();
				// we need to sort particleList as s has changed its minim x
				for(Sphere s : spheresList)	sortSphere(s, space.getParticleList());
				for(Sphere s : spheresList)	sortSphere(s, space.getParticleList());
				for(Sphere s : spheresList)	updateParticleCollisions(s, space.getParticleList().indexOf(s), sc.getTime(), space);
			}
		}
		if(space.getInfoFile()) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "final number of real collisions : " + _realCollisions);
		if(space.getInfoFile()) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "final number of false collisions : " + _totalCollisions);
	}
	
	//this method sorts the _particleList when only one particle, passed as parameter, is unsorted
	// it is based on swaping the sphere to the correct position
	private static void sortSphere(Sphere s, ArrayList<Particle> particleList){
		//System.out.println("PARTICLE LIST SIZE 1 : " + particleList.size());
//		for(int i=0; i<_particlesList.size(); i++) // System.out.println("Index "+ i + " MinX " + strPre(_particlesList.get(i).getMinX()));
		// get the current position and remove the particle from de list
		int index = particleList.indexOf(s);
		ArrayList<SphereCollision> aux = _collisionsList.remove(index);
		if(!particleList.remove(s)) DataInterface.writeLineToFile(DataInterface.getErrorsFile(), "ERROR cannot remove sphere from list while sorting");
		
		// get an iterator at s position (index) and a counter
		ListIterator<Particle> it = particleList.listIterator(index);
		int countPositions=0;
		
		ParticleMinXComparator compare = new ParticleMinXComparator();
		// iterate forward and backward on the list to find the correct position
		//// System.out.println();
		while (it.hasNext() && compare.compareBoolean(s, it.next())) countPositions++;
		if(it.hasNext())it.previous();
		//// System.out.println("POSITIONS " +countPositions);
		while (it.hasPrevious() && !compare.compareBoolean(s, it.previous())) countPositions--;
		//System.out.println("POSITIONS " +countPositions);
		//swap spheres	
		if(index+countPositions < 0){
			particleList.add(0, s);
			_collisionsList.add(0,aux);
		}
		else if (index+countPositions > particleList.size()){
			particleList.add(particleList.size(), s);
			_collisionsList.add(particleList.size(),aux);
		}
		else{
			particleList.add(index+countPositions, s);
			_collisionsList.add(index+countPositions,aux);
		}
		// System.out.println("index " + index + " count " +countPositions);
		
		//System.out.println("PARTICLE LIST SIZE 2 : " + particleList.size());
	}
	
	// this method finds the new collisions of a particle as a time sorted list,
	// 
	private static void updateParticleCollisions(Sphere s, int index, double time, Space space){
		ArrayList<SphereCollision> aux = findParticleCollisions(index, space.getParticleList(), time, space);
		if(aux!=null && aux.size()>0){
			_totalCollisions += aux.size();
			_collisionsQueue.add(aux.remove(0));
		}
//		else{
//			if (s.getNextX() < 0 || s.getNextX() > space.getXSize() || s.getNextY() < 0 || s.getNextY() > space.getYSize()){
//				aux = findParticleCollisions(index, space.getParticleList(), time, space);
//			}
//		}
		_collisionsList.remove(index);
		_collisionsList.add(index, aux);
	}
}
