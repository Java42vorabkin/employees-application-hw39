package telran.employees.controller;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

import telran.employees.services.*;
import telran.view.*;
public class EmployeesAppl {
	
	private static String employeesDataFileKey = "employeesDataFile";
;
	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		if (args.length < 1) {
			io.writeObjectLine("Usage - argument should contain configuration file name");
			return;
		}
		//Configuration file contains text like employeesDataFile=employees.data
		//Apply BufferedReader for reading configuration
		String fileName = getFileName(args[0]);
		if(fileName==null) {
			io.writeObjectLine("Employee Data Configuration File isn't defined");
			return;
		}
		io.writeObjectLine("config file: "+fileName);
		EmployeesMethods employeesMethods = new EmployeesMethodsMapsImpl(fileName);
		employeesMethods.restore();
		HashSet<String> departments = new HashSet<>(Arrays.asList("QA", "Development", "HR", "Management"));
		Menu menu = new Menu("Employees Application", EmployeeActions.getActionItems(employeesMethods, departments));
		menu.perform(io);

	}

	private static String getFileName(String configFile) {
		Properties prop = new Properties();	
		try {
			BufferedReader reader = new BufferedReader(new FileReader(configFile));	            
			prop.load(reader);
			String fileName = prop.getProperty(employeesDataFileKey);
			if(fileName==null) {
				System.out.println(employeesDataFileKey + ": the key isn't found");
			}
			reader.close();
			return fileName;
		} catch (FileNotFoundException ex) {
			System.out.println(configFile +": config. file name isn't correct. " + ex.toString());
		} catch(IOException ex) {
			System.out.println("Reading properties failed.  " + ex.toString());
		}
		return null;
	}

}
