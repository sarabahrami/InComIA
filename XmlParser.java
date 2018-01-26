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
 GNU library General Public
this is an example of policy
 */
package relevantfile;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
@author: Sara Bahrami <mxbahramizanjani@wichita.edu>

* Purpose: Parsing the XML files(output of SrcML.jar)
* output: Corpus with the textual information for each source code file in separated rows.
* 
*/

public class XmlParser{
	 public static HashMap<String,ArrayList<String>> Corpus=new HashMap<String,ArrayList<String>>();
	//public static String dirPath = "/media/extrav/DeveloperRecommendation/Eclipse/CorpusEclipse2013-04";
	// public static String Corpusfilepath="/media/extrav/Eclipse/Repository/CorpusEclipse2013-04";
	 public static String Corpusfilepath ;
	// public static String xmldirPath ="/media/extrav/Eclipse/Repository/srcml/";
	 public static String xmldirPath ;
	 public static void main(String[] args) {
		 	
		 xmldirPath=args[0];
		 Corpusfilepath=args[1];
		 try{
			File Corpusfile = new File(Corpusfilepath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(Corpusfile));
			File folder = new File(xmldirPath);
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles)
				{
					if (file.length()!=0)	
					{
						String filename=file.getName().replace('-',File.separatorChar );
						System.out.println(filename);
					print(writer,CreatCorpus(file,xmldirPath),filename);
					
					}
					}
			writer.flush();
			writer.close();
		}
		catch(Exception e)
		
		{e.printStackTrace();}

	}
	
	/********************************************************************/
	public static  String CamelCase (String Str)	
	{   
		Str=Str.replaceAll("[\\W|\\_]"," ");	
		Str=Str.replaceAll("((?<=[a-z])(?=[A-Z]))|((?<=[A-Z])(?=[A-Z][a-z]))" , " " );	
		return Str;		
		
	} 

/*********************************************************************************************************************/
	public static ArrayList<String> CreatCorpus(File file,String xmldirPath) throws ParserConfigurationException, SAXException, IOException
	{	
				
				ArrayList<String> textinfo = new ArrayList<String>();
				String name=file.getName();
				File fXmlFile= new File(xmldirPath+File.separatorChar+name);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
				NodeList nList_name = doc.getElementsByTagName("name");
				for (int temp=0; temp<nList_name.getLength();temp++) {
					Node nNode = nList_name.item(temp);
					String value;
					value=nNode.getFirstChild().getTextContent();
					String[] value1=CamelCase(value).split("\\s+");
					for (int j=0;j<value1.length;j++)
					{	
							String infotemp=removeStopWordsAndStem(value1[j]);
							if (infotemp.length()>0) 
							{
								textinfo.add(infotemp);
							}
						
						}
		
				}	
				NodeList nList_comment= doc.getElementsByTagName("comment");
				for (int temp=0; temp<nList_comment.getLength();temp++) 
				{
					Node nNode = nList_comment.item(temp);
					String value1;
					value1=nNode.getFirstChild().getTextContent().trim();
					if(!(value1.contains("Copyright")))
					{
						value1 = value1.replaceAll("[\\r|\\n]", " ");
						value1 = value1.replaceAll("[\\*|\\/]", "");
						value1 = value1.replaceAll("@author", "");
						value1=value1.trim();
						value1=value1.replaceAll(" +", " ");
						//System.out.println(value1);
						String[] str=value1.split("\\s+");
						for(int j=0;j<str.length;j++)	
		
						{
							String infotemp=removeStopWordsAndStem(str[j]);
							if (infotemp.length()>0) 
							{
								textinfo.add(infotemp);
							}
						}
					}
					
					/*if (value1.contains("Copyright"))
					{
						value1 = value1.replaceAll("[\\r|\\n]", " ");
						value1 = value1.replaceAll("[\\*|\\/]", "");
						value1 = value1.replaceAll("@author", "");
						value1=value1.trim();
						value1=value1.replaceAll(" +", " ");
						//System.out.println(value1);
						//if((value1.contains("Description"))&&(value1.contains("Contributors")))
								//{
										
										//Integer first=value1.indexOf("Description");
										//Integer end=value1.indexOf("Contributors");
										//value1=value1.substring(first, end);
										String[] str=value1.split("\\s+");
										//System.out.println(value1);
										for(int j=0;j<str.length;j++)	
			
										{
											
											//if(!textinfo.contains(str[j]))
											//{
											textinfo.add(str[j]);
											//}
										}
								//}

					}*/
					
					
					
		
				
				}
				
				NodeList nlListexp =  doc.getElementsByTagName("expr");
                for (int k = 0; k < nlListexp.getLength(); k++) 
                {
	                Node node =nlListexp.item(k);
	                if (node.hasChildNodes()==true)
	                if(node.getFirstChild().getNodeType()==Node.TEXT_NODE)
	                {
	                	String exprvalue=node.getFirstChild().getTextContent().trim();
	                	exprvalue = exprvalue.replaceAll("[\\r|\\n]", " ");
	                	String[] str=exprvalue.split("\\s+");
						for(int j=0;j<str.length;j++)	
		
						{	
							String infotemp=removeStopWordsAndStem(str[j]);
							if (infotemp.length()>0) 
							{
								textinfo.add(infotemp);
							}

						}
	                }
                }
                
                
				
		return textinfo;}


	
/******************************************************************************************/	
	public static void print(BufferedWriter writer, ArrayList<String> textinfo, String filename) throws Exception
	{
					writer.write(filename);
					writer.write("\t");
		
					for (int i=0;i<textinfo.size();i++)
					{ 
						writer.write(removeStopWordsAndStem(textinfo.get(i)).trim());
						writer.write(" ");
						}
						writer.newLine();
		

						
	}
/********************************************************************************************/
	public static String removeStopWordsAndStem(String input) throws IOException 
	{
		/*String[] stop_word={"abstract","assert","boolean","break","byte","case","catch","char","class","const","continue"
				,"default","do","double","else","enum","extends","final","finally","float","for","goto","if","implements","import","instanceof","int"
				,"interface","long","native","new","package","private","protected","public","return","short","static","strictfp","super",
				"switch","synchronized","this","throw","throws","transient","try","void","volatile","while","false","null","true"};*/
		String[] stop_word={"auto", "break","case", "char","const","continue","default","do","double","else","enum",
				"extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof",
				"static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while","abstract","as" ,"base",
				"bool", "byte", "catch", "checked", "class", "decimal", "delegate", "event", "explicit", "false", "finally",
				"fixed", "foreach", "implicit", "in" , "interface", "internal", "is", "lock", "namespace", "new", "null"
				, "object", "operator", "out", "override", "params", "private", "protected", "public", "readonly", "ref",
				"sbyte", "sealed", "stackalloc", "string", "this", "throw", "true", "try", "typeof", "uint", "ulong", "unchecked"
				, "unsafe", "ushort", "using", "virtual"};
		ArrayList<String> stopWords = new ArrayList<String>();
		for (int k=0;k<stop_word.length;k++)
			stopWords.add(stop_word[k]);
	    TokenStream tokenStream = new StandardTokenizer(
	    		Version.LUCENE_46, new StringReader(input));
	    tokenStream = new StopFilter(Version.LUCENE_46, tokenStream, StandardAnalyzer.STOP_WORDS_SET);
	    tokenStream = new StopFilter(Version.LUCENE_46, tokenStream, StopFilter.makeStopSet(Version.LUCENE_46, stopWords));
	    tokenStream = new PorterStemFilter(tokenStream);
	    StringBuilder sb = new StringBuilder();
	    CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
	    tokenStream.reset();
	    while (tokenStream.incrementToken()) {
	        if (sb.length() > 0) {
	            sb.append(" ");
	        }
	        sb.append(token.toString());
	    }
	    tokenStream.end();
	    tokenStream.close();
	    return sb.toString();
	}

	
}
