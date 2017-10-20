/**
 * Write a description of class renameFile here.
 * 
 * @author Erfan and Hardik
 * @version (a version number or a date)
 */
import java.io.*;
import java.util.*;
public class renameFile
{
    static ArrayList<String> studentName = new ArrayList<>();
    static ArrayList<String> files =new ArrayList<String>();
public static    String mainFolderPath = "C:\\Users\\Owner\\Downloads\\hi";
public static int counter=0;
public static boolean nameFlag=false;
public static boolean comments=true;
public static String name="";
public static String folderName="";
public static String student="";
public static ArrayList<Integer> commentsArray=new ArrayList<Integer>();
public static ArrayList<String> headerComments=new ArrayList<String>();
public static ArrayList<String> students=new ArrayList <String>();
public static boolean packages=false;

    public static void listFilesForFolder(final File folder) {
   
        for (final File fileEntry : folder.listFiles()) {
        	nameFlag=false;
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else if(!fileEntry.getName().equals("GradeLogger.txt")){
            	
                name = fileEntry.getName();
                student=name.substring(0,name.indexOf("_"));
                int index = name.length()-1;
                if(name.contains("-")){
                	 index=name.indexOf("-");
                	 nameFlag=true;
                }
                
                System.out.println(name);
                      folderName = name.substring(0,index);
                     if(fileEntry.canWrite()){
                     ArrayList<String[]>array1 =readFile2(name);
                     
                     
                     String className=name.substring(0,name.length()-5);
                     int index1=searchLine(array1,"class");
                     System.out.println("Class was found at "+index1);
                     int index2=searchCol(array1,"class")+1;
                     array1.get(index1)[index2]=className;
                     try {
           		      

           		      //read a line from the console
           		      

           		      //create an print writer for writing to a file
                    	 File dir = new File(mainFolderPath+"/"+"newFiles");
                    	 
                    		 dir.delete();
                    		 
                    	 
                         dir.mkdir();  
                        
                         PrintWriter out;
                         
                         if(nameFlag){
           		      out = new PrintWriter(new FileWriter(mainFolderPath+"\\newFiles"+"\\"+folderName+".java"));
                         }
                         else{
                        	 out=new PrintWriter(new FileWriter(mainFolderPath+"\\newFiles"+"\\"+name));
                         }
           		      for (int i=0;i<array1.size()-1;i++){
           		    	  for (int z=0;z<array1.get(i).length;z++){
           		      out.print(array1.get(i)[z]+" ");
           		      }
           		    	  out.println();
           		      }
           		      //output to the file a line
           		     
           		      if(nameFlag){
           		     students.add(student);
           		      commentsArray.add(new Integer(array1.get(array1.size()-1)[0]));
           		      headerComments.add(array1.get(array1.size()-1)[1]);
           		      
           		      
           		      
           		      }
           		      
           		      else{
           		    	students.add(student);
           		    	commentsArray.add(new Integer(array1.get(array1.size()-1)[0]));
           		    	headerComments.add(array1.get(array1.size()-1)[1]);
           		      }
           		      //close the file (VERY IMPORTANT!)
           		     out.close();
           	
           		   }
           		      catch(IOException e1) {
           		        System.out.println("Error during reading/writing");
           		   }
                     }
            }
        }//end of file loop
        
        try {
			PrintWriter pt=new PrintWriter(new FileWriter(mainFolderPath+"\\newFiles\\GradeLogger.csv",true));
			int looper=0;
			Integer sum=0;
			for (Integer i :commentsArray){
				 sum+=i;
				 looper++;
			}
			commentsArray.add(sum/looper);
			
			int maxSize=looper;
			looper=0;
			pt.println("Student Name"+","+"Number of Comments"+","+"Header Comments"+","+"Difference from Average");
			for (String s : students){
				pt.println(s+","+commentsArray.get(looper)+","+headerComments.get(looper)+","+new Integer(commentsArray.get(looper)-commentsArray.get(maxSize)));
				looper++;
			}
			pt.println("The Average comments is:,"+commentsArray.get(maxSize));

		pt.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
    }
       
    


        public static void main(String [] args){
        	Scanner sc =new Scanner (System.in);
        	System.out.print("Enter Directory");
         mainFolderPath=sc.nextLine();
         System.out.print("With comments: Y/N? ");
         char c=sc.next().charAt(0);
         System.out.print("Remove Packages: Y/N? ");
         char p=sc.next().charAt(0);
         if(c=='N'){
        	 comments=false;
         }
         if(p=='Y'){
        	 packages=true;
         }
         else{
        	 packages=false;
         }
            File folder = new File(mainFolderPath);
            listFilesForFolder(folder);
         
            
            }


	
        public static ArrayList<String[]> readFile2(String file){
    		
    		String line = "";
            String cvsSplitBy = " ";
            int i=0;
            int numOfComments=0;
            String header="false";
            boolean endHeader=false;
    		ArrayList<String[]>theArray=new ArrayList<String[]>();
    		 try (BufferedReader br = new BufferedReader(new FileReader(mainFolderPath+"\\"+file))) {
    			 
    	            while ((line = br.readLine()) != null) {
    	            	boolean flag=false;
    	            	int index=0;
    	                // use comma as separator
    	                String[]code = line.split(cvsSplitBy);
    	                for(String n:code){
    	                if(n.contains("/*")){
	                		header="true";
	                	}
    	                if(n.contains("*/")){
    	                	endHeader=true;
    	                }
    	                if(n.contains("//")){
	                		numOfComments++;
    	                }
    	                
    	                if(packages){
    	                	if (n.contains("package")){
    	                		flag=true;
    	                		index=-1;
    	                	}
    	                }
    	                if(!comments){
    	                
    	                	
    	                	if(n.contains("//")){
    	                		numOfComments++;
    	                		
    	                		code[index]=code[index].substring(0,code[index].indexOf("//"));
    	                		flag=true;
    	                		
    	                	}
    	                	index++;
    	                	if(flag){
    	                		while(index<code.length){
    	                		
    	                			code[index]="";
    	                			
    	                			index++;
    	                		}
        	                	break;//skips the line when comment is found
        	                }
    	                }
    	                
    	                }
    	                
    	                theArray.add(code);
    	                i++;

    	                
    	                
    	            }
    	            String []last={Integer.toString(numOfComments),header};
    	            theArray.add(last);
    	          
    	           

    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    		 System.out.println(i+" Lines Added");
    	return theArray;
    	}
        public static int searchCol(ArrayList<String[]> array2,String word){
			int index1=0;
			boolean endHeader=true;
			
        	for(String[]theArray:array2){
        		boolean flag=false;
        		
        		int index=0;
        		for(String i  :theArray){
        			if(i.contains("/*")){
  	                	endHeader=false;
  	                	
  	                }
        			if(i.contains("*/")){
  	                	endHeader=true;
  	                
  	                }
        		if(word.equals(i)){
        			 
        			if(i.contains("//")||!endHeader){
        				flag=true;
        			}
        			if (!flag){
        				return index;
        			}
        			
        			
        			
        		}
        		index++;
        		}
        		index1++;
        	}
        	return -1;
        	
        	
        }
public static int searchLine(ArrayList<String[]> array2,String word){
			int index1=0;
			boolean endHeader=true;
			
        	for(String[]theArray:array2){
        		int index=0;
        		boolean flag=false;
        		
        		for(String i  :theArray){
        			if(i.contains("/*")){
  	                	endHeader=false;
  	                	
  	                }
        			  if(i.contains("*/")){
   	                	endHeader=true;
   	                	
   	                }
        		if(word.equals(i)){
        			
 	              
        			if(i.contains("//")||!endHeader){
        				flag=true;
        			}
        			if(!flag){
            			return index1;
        			}
            			
        			
        		}
        		index++;
        		}
        		index1++;
        	}
        	return -1;
        	
        	
        }
}