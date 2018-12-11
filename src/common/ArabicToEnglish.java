package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ArabicToEnglish 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HashMap<String, String> hmap = new HashMap<String, String>();
	List<String> lstNumbers;
	
	public ArabicToEnglish()
	{
		mapAlphabets();
		lstNumbers=new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			lstNumbers.add(""+i);
		}		
	}
	
	private void mapAlphabets()
	{
		hmap.put("أ", "a");
		hmap.put("ا", "a");		
		hmap.put("ب", "b");
		hmap.put("ت", "t");
		hmap.put("ذ", "th");
		hmap.put("ض", "d");
		hmap.put("ص", "s");
		hmap.put("ث", "th");
		hmap.put("ق", "q");
		hmap.put("لإ", "la");
		hmap.put("ف", "f");
		hmap.put("إ", "e");
		hmap.put("غ", "gh");
		hmap.put("ع", "a");
		hmap.put("ه", "h");
		hmap.put("خ", "kh");
		hmap.put("ح", "h");
		hmap.put("ج", "g");
		hmap.put("د", "d");
		hmap.put("ش", "sh");
		hmap.put("س", "s");
		hmap.put("ي", "y");
		hmap.put("لأ", "la");
		hmap.put("ل", "l");		
		hmap.put("ن", "n");
		hmap.put("م", "m");
		hmap.put("ك", "k");
		hmap.put("ط", "t");
		hmap.put("ئ", "");
		hmap.put("ء", "");
		hmap.put("ؤ", "o");
		hmap.put("ر", "r");
		hmap.put("لآ", "la");
		hmap.put("لا", "la");
		hmap.put("آ", "aa");
		hmap.put("ى", "a");
		hmap.put("ة", "t");
		hmap.put("و", "w");
		hmap.put("ز", "z");
		hmap.put("ظ", "z");
		
	}
	
	public String convertToEnglishText(String arabicText)
	{
		String result="";
		try
		{
			//String[] arr=arabicText.split("\\|");
			logger.info("arabicText >> "  + arabicText);
			if(arabicText.contains("\n"))
			{
				logger.info("text contains enter");
			}
			
			String[] lines= arabicText.split("\n");
			if(lines.length>0)
			{
				for (String line : lines) 
				{
					String[] splitArabicText= getWords(line);
					for (String word : splitArabicText) 
					{
						for(int i=0;i<word.length();i++)
						{
							char letter=word.charAt(i);
							if(lstNumbers.contains(String.valueOf(letter)))
							{
								result+=String.valueOf(letter);
							}
							
							else if(hmap.containsKey(String.valueOf(letter)))
							{
								result+= hmap.get(String.valueOf(letter));
							}
							else
							{
								result+=String.valueOf(letter);
							}
						}
						
						result+=" ";
					}
					result+="\n";
				}
			}
			
			/*String[] splitArabicText= getWords(arabicText);
			
			for (String word : splitArabicText) 
			{
				for(int i=0;i<word.length();i++)
				{
					char letter=word.charAt(i);
					if(lstNumbers.contains(String.valueOf(letter)))
					{
						result+=String.valueOf(letter);
					}
					
					else if(hmap.containsKey(String.valueOf(letter)))
					{
						result+= hmap.get(String.valueOf(letter));
					}
					else
					{
						result+=String.valueOf(letter);
					}
				}
				
				result+=" ";
			}*/
			
			/*for(int i=0;i<arabicText.length();i++)
			{
				char letter=arabicText.charAt(i);
				if(hmap.containsKey(String.valueOf(letter)))
					result+= hmap.get(String.valueOf(letter));				
			}*/
			
			logger.info("eng result >> "  + result);
			/*for (String letter : arr) 
			{
				if(hmap.containsKey(letter))
					result+= hmap.get(letter);
			}		*/	
		}
		catch (Exception ex) 
		{
			  logger.error("error in ArabicToEnglish---convertToEnglishText-->" , ex);			
		}
		
		return result;
	}
	
	private static String[] getWords(String sentence){
		if (sentence != null) {
			return sentence.split("\\s");
		} else {
			return new String[0];
		}
	}
	
	
}
