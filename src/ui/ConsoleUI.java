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

package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConsoleUI {

	public static void readFromStdIn(ArrayList<String> names, ArrayList<String> values) {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	
		// Simulation
		
		System.out.println("Please enter simulation information ");
		System.out.println("");
		System.out.println("SIMULATION DATA : ");
		System.out.println("***************** ");
		
		names.add("outPath");
		values.add(questionString(stdin, "Folder to save results to : ", 0));
		
		names.add("graphics");
		values.add(questionString(stdin, "Create graphics file? (yes/no) : ", 0));
		
		names.add("infoFile");
		values.add(questionString(stdin, "Create information file? (yes/no) : ", 0));
		
		names.add("activeCollision");
		values.add(questionString(stdin, "Activate particle collisions? (yes/no) : ", 0));
		
		names.add("BMFactor");
		values.add(questionString(stdin, "Brownian Motion factor? : ", 0));
		
		names.add("inertiaFactor");
		values.add(questionString(stdin, "Inertia motion factor? : ", 0));
				
		names.add("time");
		values.add(questionString(stdin, "Total simulation time (ns) : ", 0));
		
		names.add("timeStep");
		values.add(questionString(stdin, "Random walk timestep (ns) : ", 0));
	
		// Space
		
		System.out.println("");
		System.out.println("SPACE DATA : ");
		System.out.println("************ ");


		names.add("boundedSpace");
		String boundedSpace = questionString(stdin, "Bounded space? (yes/no) : ", 0);
		values.add(boundedSpace);
		
		if(boundedSpace.equalsIgnoreCase("y") || boundedSpace.equalsIgnoreCase("yes") || boundedSpace.equalsIgnoreCase("true")){
			names.add("constantBGConcentration");
			values.add(questionString(stdin, "Simulate infinite space? (yes/no) : ", 0));
			
			names.add("constantBGConcentrationWidth");
			values.add(questionString(stdin, "Boundarie width to simulate infinite space (nm)? : ", 0));
						
			System.out.println("Space dimensions (nm): ");
			
			names.add("xSize");
			values.add(questionString(stdin, "width (x) (nm) : ", 0));
				
			names.add("ySize");
			values.add(questionString(stdin, "height (y) (nm) : ", 0));
		}
		else{
			names.add("constantBGConcentration");
			values.add("false");
			names.add("constantBGConcentrationWidth");
			values.add("0");
			names.add("xSize");
			values.add("0");
			names.add("ySize");
			values.add("0");
		}
		names.add("D");
		values.add(questionString(stdin, "diffusion coeficient (nm2/ns) : ", 0));
		
		names.add("bgConcentration");
		values.add(questionString(stdin, "initial background concentration (particles per 10000nm2) : ", 0));
			
		names.add("sphereRadius");
		values.add(questionString(stdin, "particles radius (nm) : ", 0));
	
		//emitters
		System.out.println("");
		System.out.println("EMITTERS DATA : ");
		System.out.println("*************** ");
		
		
		String emitters = questionString(stdin, "Number of emitters : ", 0);
		names.add("emitters");
		values.add(emitters);
		
		int numEmitters = Integer.parseInt(emitters);
		
		for (int i=0; i< numEmitters; i++){
			System.out.println("Please enter emitter "+ (i+1) + " data : ");
			
			names.add("emitterRadius");
			values.add(questionString(stdin, "sphere influence radius (nm): ", 0));
			
			names.add("x");
			values.add(questionString(stdin, "x location (nm): ", 0));
			
			names.add("y");
			values.add(questionString(stdin, "y location (nm): ", 0));
			
			names.add("startTime");
			values.add(questionString(stdin, "starting time (ns): ", 0));
			
			names.add("endTime");
			values.add(questionString(stdin, "ending time (ns): ", 0));
			
			names.add("color");
			values.add(questionString(stdin, "color (String): ", 0));
			
			String emitterType = questionString(stdin, "Type of emitter :\n\t1 Pulse\n\t2 Rectangular\n\t3 Noise\n\t4 Wave From File\n\t5 Punctual Wave From File\n\t6 Concentration From File\nType : ", 0);
			names.add("emitterType");
			values.add(emitterType);
			
			switch(Integer.parseInt(emitterType)){
				case(1):
					names.add("amplitude");
					values.add(questionString(stdin, "amplitude (particles/100ns) : ", 0));
					break;		
				case(2):
					names.add("amplitude");
					values.add(questionString(stdin, "amplitude (particles/100ns) : ", 0));
					names.add("period");
					values.add(questionString(stdin, "period (ns) : ", 0));
					names.add("timeOn");
					values.add(questionString(stdin, "time ON (ns) : ", 0));
					break;
				case(3):
					names.add("amplitude");
					values.add(questionString(stdin, "amplitude (particles/100ns) : ", 0));
					break;
					//TODO join 4, 5 and 6
				case(4):
					names.add("fileName");
					values.add(questionString(stdin, "file name : ", 0));
					names.add("scaleFactor");
					values.add(questionString(stdin, "scale factor : ", 0));
					break;
				case(5):
					names.add("fileName");
					values.add(questionString(stdin, "file name : ", 0));
					names.add("scaleFactor");
					values.add(questionString(stdin, "scale factor : ", 0));
					break;
				case(6):
					names.add("fileName");
					values.add(questionString(stdin, "file name : ", 0));
					names.add("scaleFactor");
					values.add(questionString(stdin, "scale factor : ", 0));
					break;
				case(7):
					names.add("z");
					values.add(questionString(stdin, "z location (nm): ", 0));
					names.add("fileName");
					values.add(questionString(stdin, "file name : ", 0));
					names.add("scaleFactor");
					values.add(questionString(stdin, "scale factor : ", 0));
					break;
				default: System.out.println("ERROR : CONSOLE UI : Wrong type of emitter. Emitter not created, try again"); i--;
			}
		}
		
		//create receivers
		System.out.println("");
		System.out.println("RECEIVERS DATA : ");
		System.out.println("**************** ");
		
		String receivers = questionString(stdin, "Number of receivers : ", 0);
		names.add("receivers");
		values.add(receivers);
		
		int numReceivers = Integer.parseInt(receivers);
		
		for (int i=0; i< numReceivers; i++){
			System.out.println("Please enter receiver "+ (i+1) + " data : ");
			
			names.add("name");
			values.add(questionString(stdin, "receiver name (any string): ", 0));
			
			names.add("x");
			values.add(questionString(stdin, "x location (nm): ", 0));
			
			names.add("y");
			values.add(questionString(stdin, "y location (nm): ", 0));
			
			names.add("absorb");
			values.add(questionString(stdin, "absorb particles? (yes/no) : ", 0));
			
			names.add("accumulate");
			values.add(questionString(stdin, " accumulative particle counting? (yes/no) : ", 0));
	
			String receiverType = questionString(stdin, "Type of receiver :\n\t1 Square\n\t2 Sphere\nType : ", 0);
			names.add("receiverType");
			values.add(receiverType);
			
			switch(Integer.parseInt(receiverType)){
				case(1):
					names.add("side");
					values.add(questionString(stdin, "square side(nm) : ", 0));
					break;		
				case(2):
					names.add("rradius");
					values.add(questionString(stdin, "radius(nm) : ", 0));
					break;
				default: System.out.println("ERROR : CONSOLE UI : Wrong type of receiver. Receiver not created, try again"); i--;
			}
		}
	}

	
	private static String questionString(BufferedReader stdin, String question, int errors){
		if(errors > 4){
			System.out.println("ERROR : CONSOLE UI : error reading value. Program terminates.");
			System.exit(1);}
		System.out.print(question);
		String s = "";
		try {
			s = stdin.readLine();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("ERROR : CONSOLE UI : error reading value. Please try again.");
			s = questionString(stdin, question, errors+1);
		}
		return s;
	}

}
