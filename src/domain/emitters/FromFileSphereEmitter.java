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

package domain.emitters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FromFileSphereEmitter extends SphereEmitter{
	
	File _file;
	String _filename;
	double[] _numParticles;
	int _current;
		
	public FromFileSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV, boolean punctual, boolean concentrationEmitter, String filename, int size, String color) {
		super(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV, punctual, concentrationEmitter, color);
		_filename = filename;
		_file = new File(_filename);
		_numParticles = new double[size];
		System.out.println("size : " + size);
		readFile();
		_current = 0;
	}

	public String getDescription(){
		String s = super.getDescription();
		s += "scale factor: " + _amplitude + "\n";
		s += "wave file" + _filename + "\n";
		return s;
	}
	
	//TODO change this method. Do not read the whole file and keep it in memory,
	// read 1 line each timestep. Use a thread?
	private int[] readFile(){
		int i = 0;
		try {
	        BufferedReader input =  new BufferedReader(new FileReader(_file));
	        try {
	        	String line = null;
	        	while (( line = input.readLine()) != null && i < _numParticles.length){
	        		if(!line.equals("")) _numParticles[i] = Double.parseDouble(line);
	        		System.out.println("particles : " + i + " : " +line);
	        		i++;
	        	}
	        	while (i < _numParticles.length){
	        		_numParticles[i] = 0;
	        		i++;
	        	}
	        }
	        finally {
	        	input.close();
	        }
	     }
	     catch (IOException ex){
	    	 ex.printStackTrace();
	     }
	     catch (NumberFormatException ex){
	    	 ex.printStackTrace();
		 }
		return null;
	}
	
	double getNumParticles(double time) {
		double numParticles = 0;
		if(time >= _startTime && time < _endTime){
			numParticles = (_numParticles[_current])*_amplitude;
			_current++;
		}
		System.out.println("time: "+ time + "numParticles: "+numParticles);
		return numParticles;
	}
}
