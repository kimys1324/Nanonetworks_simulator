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

package domain.receivers;

import java.util.ArrayList;
import java.util.HashMap;
import domain.particles.Particle;

public abstract class Receiver {
	int iscolored = 0;

	public abstract int count(ArrayList<Particle> particlesList);
	
	public HashMap<Integer,Integer> ccount(ArrayList<Particle> particlesList){
		return null;
	}
	public abstract String getDescription();

	public abstract String getFileName();
	
	public abstract void updatePosition();
	
	public abstract void makeMotion();
	
	public int isColored(){
		return iscolored;
	}
	
}
