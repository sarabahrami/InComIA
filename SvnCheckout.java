package svnsrcml;

	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.DataInputStream;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.OutputStreamWriter;
	import org.apache.commons.io.FilenameUtils;
	import java.io.PrintWriter;

	import org.apache.commons.io.FileUtils;

	public class SvnCheckout {
		public static File wd = new File("/usr/bin/");
		//public static String sourceCodeDirPath = "/home/sunshine40270/mine/projectdata/mylynsvn/trunk";
		//public static String sourceCodeDirPath1 = "/home/sunshine40270/mine/projectdata/mylynsvn/commitbenchmark";
		//public static String sourceCodeDirPath2 = "/home/sunshine40270/mine/projectdata/mylynsvn";
		
		public static String sourceCodeDirPath = "/data/sara/mylynsvn/trunk";
		public static String sourceCodeDirPath1 = "/data/sara/commitbenchmark";
		public static String sourceCodeDirPath2 = "/data/sara";
		public static String revision=" ";
		//public static String dirPath = "/home/sunshine40270/mine/projectdata/mylynsvn/trunk";

	    public static void main(String[] args) throws IOException{
	    	 File file1 = new File(sourceCodeDirPath2 +"/"+ "commitfile");
			 FileInputStream fstream; 
			 fstream = new FileInputStream(file1);
	         DataInputStream in1 = new DataInputStream(fstream);
	         BufferedReader br = new BufferedReader(new InputStreamReader(in1));
	         String strLine=null;
	            while ((strLine = br.readLine()) != null) {
	            	String[] row= strLine.split("\t");
	            	 revision=row[1];
	            	Process proc = null;
	            	try {
	            		proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
	            		} catch (IOException e) {
	            			e.printStackTrace();
	            								}
	            	if (proc != null) {
	            		BufferedReader in = new BufferedReader(new InputStreamReader(
	                    proc.getInputStream()));
	            		PrintWriter out = new PrintWriter(new BufferedWriter(
	                    new OutputStreamWriter(proc.getOutputStream())), true);
	            		String cmd = "cd" + " " + sourceCodeDirPath ;
	            		out.println(cmd);
	            		int y = Integer.parseInt(revision);
	            		//y=y-1;
	            		String cmd1 ="svn"+" "+"update"+" "+"-r"+y;
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
	            	for(int i=2;i<=row.length-1;i++)
	            	walkSourceDirectory(sourceCodeDirPath,row[i]);
	       
	    }
	}
	 
	    
	    private static void  walkSourceDirectory(String srcDirRoot,String filename) {

	        File root = new File(srcDirRoot);
	        File[] list = root.listFiles();

	        for (File f : list) {
	                if (f.isDirectory()) 
	                    walkSourceDirectory(f.getAbsolutePath(),filename);
	                else {

	                    if (f.getName().equalsIgnoreCase(filename))
	                    {try {
	                    	
	                    	File trgDir = new File(f.getPath());
	                    	String fileNameWithOutExt = FilenameUtils.removeExtension(f.getName());
	                    	System.out.print(fileNameWithOutExt);
	                    	System.out.print("\n");
	                    	File srcDir = new File(sourceCodeDirPath1+"/"+fileNameWithOutExt+"_"+ revision+".java");
	                    	FileUtils.copyFile(trgDir, srcDir);
	                    	System.out.print(f.getPath());
	                    	System.out.print("\n");
	                  }     catch (IOException e) {
	                        e.printStackTrace();
	                   }
	                    }     
	                }
	                    
	                }
	            }
	}


