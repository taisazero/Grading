import java.io.FileNotFoundException;
import javafx.stage.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

/**
 * 
 * @author E-Neon
 * @docRoot: http://javacsv.sourceforge.net/com/csvreader/CsvReader.html
 *
 */
public class KahootScorer {
	// test
	private static String KahootPath = "";
	private static String gradePath = "";
	private static String aName = "";

	public static void main(String args[]) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the Kahoot Directory: ");
		KahootPath = sc.nextLine();
		System.out.print("Enter the Gradebook Directory: ");
		gradePath = sc.nextLine();
		System.out.print("Enter the Assignment name: ");
		aName = sc.nextLine();
		System.out.print("Enter the Kahoot file name: ");
		String kahoot = sc.nextLine();
		System.out.print("Enter the gradebook file name: ");
		String gradeBook = sc.nextLine();

		ArrayList<String[]> grades = KahootReader(kahoot);
		gradeUpdate(gradeBook, grades);
		System.out.println("done!");

		sc.close();

	}

	public static void gradeUpdate(String f, ArrayList<String[]> kahoot) {

		try {
			
			CsvReader reader = new CsvReader(gradePath + "\\" + f + ".csv");
			CsvWriter writer = new CsvWriter(new FileWriter(gradePath + "\\" + f.substring(0, f.length() - 3) + "(1)" + ".csv"), ',');
			CsvReader absentReader= new CsvReader (gradePath+"\\"+"absences.csv");
			ArrayList<String[]> names=new ArrayList<String[]>();
			
			while(absentReader.readRecord()){
				names.add(new String[] {absentReader.get(0),absentReader.get(1)});
			}
			absentReader.close();
			CsvWriter absentWriter =new CsvWriter(new FileWriter (gradePath+"\\"+"absences.csv"), ',');
			Scanner input=new Scanner(System.in);
			System.out.println("Y/N update attendace");
			char attendance=input.next().toUpperCase().charAt(0);
			boolean update=attendance=='Y';
			
			
			ArrayList<String> missing=new ArrayList<String>();
			reader.readHeaders();
			String[] headers = reader.getHeaders();
			writer.writeRecord(headers);
			while (reader.readRecord()) {
				int index = 0;
				String[] data = new String[headers.length];
				for (int i = 0; i < headers.length; i++) {
					boolean isPresent = false;
					if (headers[i].equals(aName)) {
						if (data[2].equals("") || data[0].contains("Points")) {
							data[i] = reader.get(headers[i]);
						}
						else{
						for (String[] temp : kahoot) {
							if (data[2].equalsIgnoreCase(temp[0])) {
								index = kahoot.indexOf(temp);
								isPresent = true;
								break;
							}
						}
						if (isPresent) {
							data[i] = kahoot.get(index)[1];
						} else {
							missing.add(data[2]);
							data[i] = "0";
						}

					}
					}

					else {
						data[i] = (reader.get(headers[i]));
					}
				}
				writer.writeRecord(data);
			}
				
			for (String [] record : names){
				boolean flag=false;		
				for (String n : missing){
				if(record[0].equalsIgnoreCase(n)){
					flag=true;
				}
				}
			if(flag&&update){
				Integer temp =new Integer (record[1]);
				temp=temp+1;
				absentWriter.writeRecord(new String[]{record[0],temp.toString()});
			}
			else{
				
				absentWriter.writeRecord(record);
				}
				
			}
			absentWriter.close();
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Writing File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<String[]> KahootReader(String f) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		try {
			CsvReader reader = new CsvReader(KahootPath + "\\" + f + ".csv");

			reader.readHeaders();

			while (reader.readRecord()) {
				data.add(new String[] { reader.get("Players"), reader.get("Correct Answers") });

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Reading File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

}
