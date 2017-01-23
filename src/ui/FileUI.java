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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUI {

	static String _configFileName;
	static File _configFile;
	static int _numParam;
	static int _currentIndex;
	
	public static void readFromFile(ArrayList<String> names, ArrayList<String> values,
			String[] args) throws FileNotFoundException {
		
		_configFileName = args[0];
		_numParam = 1;
		_configFile = new File(_configFileName);
		if(!_configFile.exists()){
			UI.error("ERROR : File " + _configFileName + " does not exist. Program terminates.");
			System.exit(1);
		}
		
		// read all lines 		
		Scanner scanner = new Scanner(_configFile);
	    String line = "";
		while(scanner.hasNext()){
			line = scanner.nextLine();
			// do not read if blank line or begins with #
			while (line.startsWith("#") || line.equals("")){
		    	line = scanner.nextLine();
			}
		    line.replaceAll("\\s", ""); //TODO this doesn't work !?!?!?!
			processLine(line, names, values, args);
		}
		scanner.close();
	}

	//add name and value to the UI lists
	private static void processLine(String line, ArrayList<String> names, ArrayList<String> values, String[] args){
		
		String name = "";
		String value = "";
		Scanner scanner = new Scanner(line);
	    scanner.useDelimiter("=");
	    name = scanner.next();
	    value = scanner.next();
	    		    scanner.close();
	    //if value is 'param', read the next value from args[]
	    if(value.equalsIgnoreCase("param")){
	    	value = args[_numParam];
	    	_numParam++;
	    }
		names.add(name);
		values.add(value);
	}
}
