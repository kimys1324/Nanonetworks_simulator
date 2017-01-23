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

package data;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;

public class DataInterface {

	static String _path;
	static TreeMap<String, DataWriter> _writersMap;
	static String _csvSeparator = ",";
	
	static String _graphFile = "video.ns1";
	static String _simulationFile = "simulationData.txt";
	static String _errorsFile = "error.log";
	static String _infoFile = "info.log";

//S<<<<	static String _speedFile = "velocity.csv";

	public static void init(String path){
		_path = path;
		_writersMap = new TreeMap<String, DataWriter>();
		File _file = new File(_path);
		_file.mkdirs();
		addWriter(_simulationFile);
		addWriter(_errorsFile);
//		addWriter(_infoFile);
//		addWriter(_speedFile);
	}
	
	public static void addWriter(String filename){
		try {
			DataWriter d = new DataWriter(_path+ "/" + filename);
			_writersMap.put(filename, d);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public static void writeLineToFile(String filename, String line){
		DataWriter d = _writersMap.get(filename);
		if(d==null){
			addWriter(filename);
			writeLineToFile(filename, line);
			System.out.println("WARNING : File " + filename + " didn\'t exist. Now created");
		}
		else{
			try {
				d.writeLine(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void close(){
		Set<String> dwset = _writersMap.keySet();
		for (String s : dwset){
			try {
				_writersMap.get(s).close();
			} catch (IOException e) {
				error("ERROR : DATAINTERFACE : Error closing file " + s + ".");
				e.printStackTrace();
			}
		}
	}

	public static String getCsvSeparator() {
		return _csvSeparator;
	}	

	public static String getGraphFile() {
		return _graphFile;
	}
	
	public static String getSimulationFile() {
		return _simulationFile;
	}

	public static String getErrorsFile() {
		return _errorsFile;
	}

	public static String getInfoFile() {
		return _infoFile;
	}

	private static void error(String errorMsg) {
		writeLineToFile(_errorsFile, errorMsg);
		System.out.println(errorMsg);
	}

//	public static String getVelocityFile() {
//		// TODO Auto-generated method stub
//		return _speedFile;
//	}
}
