 /* 
  Copyright Software Engineering Research laboratory <serl@cs.wichita.edu>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Library General Public
 License as published by the Free Software Foundation; either
 version 2 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Library General Public License for more details.

 You should have received a copy of the GNU Library General Public
 License along with this program; if not, write to the Free
 Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 
 */
package relevantfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/*
 * @author: Sara Bahrami <mxbahramizanjani@wichita.edu>
 * @input: BugId+BugDescription and Corpus ( created by XmlParser Class)
 * @output: List of 10 relevant file for each bug
 * Calls :machinelearningwithlsi.py a Python file uses Gensim library for text matching and KNN
 */
public class PythonRun {
	public static  void main(String[] args)throws IOException{
	//String Path ="/media/extrav/DeveloperRecommendation/Eclipse/";
	 String Path=args[0];
	//Benchmark : IssueId	BugDescription
	//String	issuesinput="EclipseBenchmark";
	 String	issuesinput=args[1];
	//output file from XmlParser class
	//String	Corpusfile="CorpusEclipse2013-04";
	 String	Corpusfile=args[2];
	//Machine learning part
	String   pythonfile="machinelearningwithlsi.py";
	String   resultfilename;
	//this file generates by machinelearningwithoutlsi.py 
	String   Corpuswithoutidfile="Corpuswithoutid.txt";

		String strLine=null;
		BufferedReader reader1 = new BufferedReader(new FileReader(Path+File.separator+issuesinput));
		while ((strLine = reader1.readLine())!= null) {
	           	String[] strsplit= strLine.split("\t",3);
	           	//String[] strsplit= strLine.split(" ",2);
	           	resultfilename=strsplit[0].replace("\n", "").replace("\r", "");
	           //	System.out.println(resultfilename);
	           	String bugdesc=strsplit[1].replace("\"","");
	            // System.out.println(bugdesc);
		Pythonrun(bugdesc,Path,pythonfile,resultfilename,Corpusfile,Corpuswithoutidfile);

		}	
		}

	/*****************************************************************/
	public static void Pythonrun(String bugdesc,String Path,String pythonfile,String resultfilename,String Corpusfile,String Corpuswithoutidfile)
	{	
     	Process proc = null;
    	try {
    		proc = Runtime.getRuntime().exec("cmd.exe");
    		} catch (IOException e) {
    			e.printStackTrace();
    								}
    	if (proc != null) {
    		BufferedReader in = new BufferedReader(new InputStreamReader(
            proc.getInputStream()));
    		PrintWriter out = new PrintWriter(new BufferedWriter(
            new OutputStreamWriter(proc.getOutputStream())), true);
    		String cmd = "cd" + " " + Path ;
    		out.println(cmd);
    		String cmd1 ="python"+" "+"\""+pythonfile+"\""+" "+"\""+bugdesc+"\""+" "+"\""+resultfilename+"\""+" "+"\""+Corpusfile+"\""+" "+"\""+Corpuswithoutidfile+"\""+" "+"\""+Path+"\"";
    		System.out.println(cmd1);
    		
    		out.println(cmd1);
    		
    		out.println("exit");
    		
    		try {
    		 
    			String line;
    			while ((line = in.readLine()) != null) {
    				System.out.println(line);
    			}
    			proc.waitFor();
    			in.close();
    			out.close();
    			proc.destroy();
    			
    		} 	catch (Exception e) {
    			e.printStackTrace();
    }
}
	}
	
}
