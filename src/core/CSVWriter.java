package core;

import java.io.*;

public class CSVWriter {
	
	private FileWriter fw;
	
	public CSVWriter(String fileName) {
		//NB: Only works on windows and in standalone mode.
		try {
			fw = new FileWriter(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+fileName +".csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		write("sep=,\n");
	}

	public void write(String row) {
		try {
			fw.write(row);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
