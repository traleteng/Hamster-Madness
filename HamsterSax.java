import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HamsterSax{


	

	public static void main(String[] args) throws IOException 
	{	
		String TestUrl = "http://xhamster.com/photos/gallery/4404168/i_adore_big_fat_ass_big_booty.html";
		SaveAll(TestUrl);

		//		try
		//		{
		//			SaveallFromTxt("/home/blackdev/Xhamster_Collection/lastresort");
		//		} catch (Exception e) {
		//			
		//			e.printStackTrace();
		//		}
		System.out.println("Done");

	}
	public static void CreateDir(String DirName) 
	{

		File theDir = new File(DirName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + DirName);
			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			} catch(SecurityException se){
				//handle it
			}        
			if(result) {    
				System.out.println("DIR created");  
			}
		}
	}
	public static String[] removeDuplicates(String[] arr) {
		Set<String> alreadyPresent = new HashSet<String>();
		String[] whitelist = new String[0];

		for (String nextElem : arr) {
			if (!alreadyPresent.contains(nextElem)) {
				whitelist = Arrays.copyOf(whitelist, whitelist.length + 1);
				whitelist[whitelist.length - 1] = nextElem;
				alreadyPresent.add(nextElem);
			}
		}

		return whitelist;
	}
	public static void SaveAll(String ImgSrcUrl) throws IOException 
	{

		int len1 = UrlCatcher(ImgSrcUrl).size();
		ArrayList<String> CleanUrlArray = UrlCatcher(ImgSrcUrl);
		String DownloadsDir="/home/blackdev/Xhamster_Collection/";
		CreateDir(DownloadsDir + Jsoup.connect(ImgSrcUrl).get().title());

		for(int h=0;h<len1;h++)
		{	

			Document websrc =  Jsoup.connect(CleanUrlArray.get(h)).get();
			Elements ImgLinks = websrc.select("img");
			int len = ImgLinks.size();

			{for(int i=1; i<len; i++) 
			{
				saveImage(UrlPrepper(ImgLinks.get(i).absUrl("src")), DownloadsDir+Jsoup.connect(ImgSrcUrl).get().title()+"/"+SrcName(UrlPrepper(ImgLinks.get(i).absUrl("src"))));		
			}

			}
		}
	}
	public static void SaveallFromTxt(String path) throws IOException 
	{	

		FileInputStream fstream = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;


		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   
		{
			// Print the content on the console

			System.out.println (strLine);
			try {
				SaveAll(strLine);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		//Close the input stream
		br.close();
	}
	public static void saveImage(String imageUrl, String destinationFile) throws IOException 
	{
		File f = new File(destinationFile);

		if(!f.exists())
		{
			try {
				URL url = new URL(imageUrl);
				InputStream is = url.openStream();
				OutputStream os = new FileOutputStream(destinationFile);
				byte[] b = new byte[2048];
				int length;
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
				is.close();
				os.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}


	}
	public static String SrcName(String Uri) 
	{
		int indexname = Uri.lastIndexOf("/");
		if (indexname == Uri.length()) 
		{
			Uri = Uri.substring(1, indexname);

		}
		indexname = Uri.lastIndexOf("/");

		String name = Uri.substring(indexname+1, Uri.length());

		return name;
	}
	public static ArrayList<String>  UrlCatcher(String Url) throws IOException 
	{	
		if(Url.contains("view")==true)
		{
			Url="http://xhamster.com/photos/gallery"+Url.substring(Url.lastIndexOf("/"), Url.length());
		}
		Document doc = Jsoup.connect(Url).get();
		Elements links = doc.select("div.pager a[href]");
		links.add(doc.select("link").first());

		ArrayList<String> WhiteList = new ArrayList<String>();
		for (Element element : links) 	 
		{
			{
				WhiteList.add(element.absUrl("href"));
			}  	  
		}

		Set<String> hashset = new HashSet<String>(WhiteList);
		ArrayList<String> WhiteList2 = new ArrayList<String>(hashset);

		return WhiteList2; 
	}
	public static String UrlPrepper(String Url) 
	{		
		if (Url.substring(0,Url.length()).endsWith("160.jpg"))
		{
			Url = Url.substring(0,Url.length()-7).concat("1000.jpg");
		}	
		return Url;
	}
	public static void WriteToFileExample(String content) 
	{

		try 
		{

			File input = new File(content);
			Document doc = Jsoup.parse(input, null);
			Elements HrefLinks = doc.select("A");

			File file = new File("$HOME/Desktop/GoogleXhamsterBookmark.txt");


			if (!file.exists()) 
			{
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (Element element : HrefLinks) 


			{

				{	
					if(element.absUrl("href").contains("xhamster.com")==true) 
					{
						bw.write(element.absUrl("href")+"\n");
					}


				}

			}
			bw.close();

			System.out.println("Done");

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
