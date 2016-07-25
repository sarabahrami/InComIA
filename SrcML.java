package relevantfile;

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
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
* @author: Sara Bahrami <mxbahramizanjani@wichita.edu>
* class for converting source code to XML file. Running srcml
*/
public class SrcML {
	public static  String sourceCodeDirPath;
	public static  String outputDirPath;
	public static String regex;
  public static void main(String[] args) 
  {
	  sourceCodeDirPath=args[0];
	  outputDirPath=args[1];
	  regex=args[2];
	  File folder = new File(sourceCodeDirPath);
     // RemoveTestFiles(folder);
      listFilesForFolder(folder);
   }
  
  
  /*
   * walking trough source code repository
   */
  public static void listFilesForFolder(final File folder) {

  	//Pattern pattern=Pattern.compile("[a-zA-Z_0-9]*(Test)[a-zA-Z_0-9]*");
	  Pattern pattern=Pattern.compile(regex);
  	for (final File fileEntry : folder.listFiles()) 
  	{
        if (fileEntry.isDirectory()) 
        {
          listFilesForFolder(fileEntry);
        } else 
        {
          if (fileEntry.isFile())
          {	
          	Matcher matcher = pattern.matcher(fileEntry.getName());
          	 if ((FileType(fileEntry.getName())==true)&&(!(matcher.find())))
          	converttoSrcXml(fileEntry.getAbsolutePath());
          	 }
      }
  }
  }
   /*
    * Finding  c and cs files 
    */
      public static boolean FileType(final String fileName) {

          Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(cs))$|[^\\s]+(\\.(?i)(c))$|[^\\s]+(\\.(?i)(h))$)");
          Matcher matcher = pattern.matcher(fileName); 
              return matcher.matches();

          }
   /*
    * running src2srcml command for each file and save the XML out put in directory    
    */
   private static void converttoSrcXml(String srcPath) {
	   String srcPathtemp=srcPath;
	   srcPath="\""+srcPath+"\"";
	  
	  Process proc = null;
      String outPutXmlFilePath = "";
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
          File srcFile = new File(srcPathtemp);
          String name=Pathcreator(srcFile.getAbsolutePath());
          outPutXmlFilePath = "\""+outputDirPath+File.separator +name+"\"";
          String cmd = "srcml" + " " + srcPath + " " + "-o " + outPutXmlFilePath;
         // System.out.println(cmd);
          out.println(cmd);
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
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

     
  }
  
   
   private static String Pathcreator(String path)
   {
	   if(path.contains(sourceCodeDirPath))
 	{	
     	int len=sourceCodeDirPath.length();
     	path=path.substring(len+1);
 	
 	}
		path=path.replace(File.separatorChar, '-');
		return path;
		
 }

}

