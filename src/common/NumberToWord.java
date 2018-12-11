package common;

import java.text.DecimalFormat;


public class NumberToWord {
	
	//http://asmaqureshi.blogspot.com/2013/05/how-to-convert-numbers-to-arabic-word.html
	 
	String[] SingleTable=new String[11];
	String[] TeenTable=new String[10];
	String[] TensTable=new String[10];
	String[] OtherTable=new String[5];

	
	String[] MVAL=new String[9];
	
	String[] MTWO=new String[10];
	
	private boolean iArabic=false;

    private void StoreWordsInArray()
    {
        //To SingleTable Array Starts
        if(iArabic == false)
        {
            SingleTable[0] = "";
            SingleTable[1] = "One";
            SingleTable[2] = "Two";
            SingleTable[3] = "Three";
            SingleTable[4] = "Four";
            SingleTable[5] = "Five";
            SingleTable[6] = "Six";
            SingleTable[7] = "Seven";
            SingleTable[8] = "Eight";
            SingleTable[9] = "Nine";
            SingleTable[10] = "Ten";
            //To SingleTable Array Ends

            //To TeenTable Array Starts

            TeenTable[1] = "Eleven";
            TeenTable[2] = "Twelve";
            TeenTable[3] = "Thirteen";
            TeenTable[4] = "Fourteen";
            TeenTable[5] = "Fifteen";
            TeenTable[6] = "Sixteen";
            TeenTable[7] = "Seventeen";
            TeenTable[8] = "Eighteen";
            TeenTable[9] = "Nineteen";
            //To TeenTable Array Starts

            //To TensTable Array
            TensTable[1] = "Ten";
            TensTable[2] = "Twenty";
            TensTable[3] = "Thirty";
            TensTable[4] = "Forty";
            TensTable[5] = "Fifty";
            TensTable[6] = "Sixty";
            TensTable[7] = "Seventy";
            TensTable[8] = "Eighty";
            TensTable[9] = "Ninety";
            //To TensTable Array

            //To Othertable Array
            OtherTable[1] = "Hundred Thousand";
            OtherTable[2] = "Million";
            OtherTable[3] = "Billion";
            OtherTable[4] = "Trillion";
        }
        else
        {
            SingleTable[0] = "";
            SingleTable[1] = "One";
            SingleTable[2] = "Two";
            SingleTable[3] = "Three";
            SingleTable[4] = "Four";
            SingleTable[5] = "Five";
            SingleTable[6] = "Six";
            SingleTable[7] = "Seven";
            SingleTable[8] = "Eight";
            SingleTable[9] = "Nine";
            SingleTable[10] = "Ten";
            //'To SingleTable Array Ends

           // 'To TeenTable Array Starts

            TeenTable[1] = "Eleven";
            TeenTable[2] = "Twelve";
            TeenTable[3] = "Thirteen";
            TeenTable[4] = "Fourteen";
            TeenTable[5] = "Fifteen";
            TeenTable[6] = "Sixteen";
            TeenTable[7] = "Seventeen";
            TeenTable[8] = "Eighteen";
            TeenTable[9] = "Nineteen";
            //'To TeenTable Array Starts

            //'To TensTable Array
            TensTable[1] = "Ten";
            TensTable[2] = "Twenty";
            TensTable[3] = "Thirty";
            TensTable[4] = "Fourty";
            TensTable[5] = "Fifty";
            TensTable[6] = "Sixty";
            TensTable[7] = "Seventy";
            TensTable[8] = "Eighty";
            TensTable[9] = "Ninety";
           // 'To TensTable Array

            //'To Othertable Array
            OtherTable[1] = "Hundred Thousand";
            OtherTable[2] = "Million";
            OtherTable[3] = "Billion";
            OtherTable[4] = "Trillion";

        }
    }
    
    

    public String  GetFigToWord(double Value)
    {
        StoreWordsInArray();
      
        StringBuffer  stringwordBuff =new StringBuffer("");
        if(iArabic == false)
        {
        	String Decimalstring =""; 
        	long NumberArray[];
    		//MEMORY ALLOCATION FOR LONG ARRAY
        	NumberArray = new long[0];
            NumberArray=new long[2];
            String tmpstringValue =""; 
            String[] TotalWord=new String[4];
            DecimalFormat df = new DecimalFormat("0.00");
            String formatted = df.format(Value); 
            tmpstringValue = formatted;
            Value = Double.parseDouble(tmpstringValue);
            Long IntegerPart; 
            Long DecimalPart; 
            IntegerPart = (long) Value;
            Decimalstring = tmpstringValue.substring(tmpstringValue.indexOf(".") + 1, tmpstringValue.length());
            DecimalPart =	Long.valueOf(Decimalstring).longValue(); 
            NumberArray[0] = IntegerPart;
            NumberArray[1] = DecimalPart;
            Integer tmpX,r,s,t ; 
            String Stringword  = "";
            

            for(tmpX=0 ; tmpX<=1;tmpX++)
            {
               // 'Billion
                if(IntegerPart > 9999999999l)
                {
                    r = (int)(IntegerPart / 1000000000l);
                    if(r > 10 && r < 20)
                    {
                        r = r % 10;
                        Stringword = Stringword + TeenTable[r] +" Billion ";
                    }
                    else
                    {
                        s = (int)(r / 10);
                        t = r % 10;
                        Stringword = Stringword + " " + TensTable[s] + " " + SingleTable[t] + " Billion ";
                    }
                    IntegerPart = IntegerPart % 1000000000;
                }
                if( IntegerPart > 999999999) 
                {
                    r = (int)(IntegerPart / 1000000000);
                    Stringword = Stringword + SingleTable[r] + " Billion ";
                    IntegerPart = IntegerPart % 1000000000;
                }
                //'Million
                if( IntegerPart > 99999999 )
                {
                    r = (int)(IntegerPart / 10000000);
                    if (r > 10 && r < 20)
                    {
                        r = (int)(r / 10);
                        Stringword = Stringword + " " + SingleTable[r] + " Hundred ";
                        IntegerPart = IntegerPart % 100000000;

                    }else if (r < 100 && r > 20)
                    {
                        r = (int)(r / 10);
                       Stringword = Stringword + SingleTable[r] + " Hundred ";
                      //  s = (int)(r / 10);
                      //  t = r % 10;
                     //   Stringword = Stringword + " " + TensTable[s] + SingleTable[t] + " Hundred";
                        IntegerPart = IntegerPart % 100000000;
                        if(IntegerPart <= 999999 )
                        {
                            Stringword = Stringword + " Million ";
                        }
                    }
                    else
                    {
                        r = (int)(r / 10);
                        if(r > 10 && r < 20)
                        {
                            r = r % 10;
                            Stringword = Stringword + " " + TeenTable[r] + " Hundred ";
                        }
                        else
                        {
                            s = (int)(r / 10);
                            t = r % 10;
                            Stringword = Stringword + " " + TensTable[s] + SingleTable[t] + " Hundred ";
                        }
                        IntegerPart = IntegerPart % 10000000;
                        if( IntegerPart <= 999999)
                        {
                            Stringword = Stringword + " Million ";
                        }
                        
                    }
                  //  r = (int)(IntegerPart / 10000);
                  //  Stringword = Stringword + SingleTable[r] + " Hundred ";
                  //  IntegerPart = IntegerPart % 100000;
                }
                if(IntegerPart > 9999999)
                {
                    r = (int)(IntegerPart / 1000000);
                    if( r > 10 && r < 20)
                    {
                        r = r % 10;
                        Stringword = Stringword + TeenTable[r] + " Million ";
                    }
                    else
                    {
                        s = (int)(r / 10);
                        t = r % 10;
                        Stringword = Stringword + " " + TensTable[s] + " " + SingleTable[t] + " Million ";
                    }
                        IntegerPart = IntegerPart % 1000000;
                }
                if( IntegerPart > 999999 )
                {
                    r = (int)(IntegerPart / 1000000);
                    Stringword = Stringword + SingleTable[r] + " Million ";
                    IntegerPart = IntegerPart % 1000000;
                }
               // 'Thousand
                if (IntegerPart > 99999 )
                {
                    r = (int)(IntegerPart / 10000);
                    if(r > 10 && r < 20 )
                    {
                        r = (int)(r / 10);
                        Stringword = Stringword + " " + SingleTable[r] + " Hundred ";
                        IntegerPart = IntegerPart % 100000;
                    }

                    else if( r < 100 && r > 20 )
                    {
                        r = (int)(r / 10);
                        Stringword = Stringword + SingleTable[r] + " Hundred ";
                      //  s = (int)(r / 10);
                      //  t = r % 10;
                      //  Stringword = Stringword + " " + TensTable[s] + SingleTable[t] + " Hundred";
                        IntegerPart = IntegerPart % 100000;
                        if( IntegerPart < 1000 )
                        {
                            Stringword = Stringword + " Thousand ";
                        }
                        
                    }
                    else
                    {
                        r = (int)(r / 10);
                        if(r > 10 && r < 20 )
                        {
                            r = r % 10;
                            Stringword = Stringword + " " + TeenTable[r] + " Hundred ";
                        }
                        else
                        {
                            s = (int)(r / 10);
                            t = r % 10;
                            Stringword = Stringword + " " + TensTable[s] + SingleTable[t] + " Hundred ";
                        }
                        IntegerPart = IntegerPart % 100000;
                        if( IntegerPart < 1000 )
                        {
                            Stringword = Stringword + " Thousand ";
                        }
                    }
                   // 'r = Int(IntegerPart / 10000)
                   // 'Stringword = Stringword & SingleTable(r) & " Hundred "
                   // 'IntegerPart = IntegerPart Mod 100000
                }
                if( IntegerPart > 9999)
                {
                    r = (int)(IntegerPart / 1000);
                    if( r > 10 && r < 20 )
                    {
                        r = r % 10;
                        Stringword = Stringword + " " + TeenTable[r] + " Thousand ";
                    }
                    else
                    {
                    	s = (int)(r / 10);
                        t = r % 10;
                        Stringword = Stringword + " " + TensTable[s] + " " + SingleTable[t] + " Thousand ";

                    }
                    IntegerPart = IntegerPart % 1000;
                }
                if(IntegerPart >= 1000)
                {
                    r = (int)(IntegerPart / 1000);
                    Stringword = Stringword + " " + SingleTable[r] + " Thousand ";
                    IntegerPart = IntegerPart % 1000;
                }
                //'Hundred
                if( IntegerPart >= 100 )
                {
                    r = (int)(IntegerPart / 100);
                    Stringword = Stringword + " " + SingleTable[r] + " Hundred ";
                    IntegerPart = IntegerPart % 100;
                }
                //'Ten
                if(IntegerPart > 19 && IntegerPart < 100)  
                {
                    r = (int)(IntegerPart / 10);
                    Stringword = Stringword + " " + TensTable[r];
                    IntegerPart = IntegerPart % 10;
                }
                //'Teen
                if( IntegerPart > 10 && IntegerPart < 20 )
                {
                    r = (int) (IntegerPart % 10);
                    Stringword = Stringword + " " + TeenTable[r];
                }
                //'One
                if(IntegerPart > 0 && IntegerPart <= 10)
                {
                    Stringword = Stringword + " " + SingleTable[IntegerPart.intValue()];
                }
                IntegerPart = NumberArray[1];
                TotalWord[tmpX] = Stringword;
                Stringword = "";
            }
            if (TotalWord[0].length() > 0 || TotalWord[1].length() > 0 )
            {
               
                if(TotalWord[0].length() > 0) 
                	stringwordBuff.append("L.L" + " " + TotalWord[0]+" "); //need to get currency format from table 
                	else
                		stringwordBuff.append("");
                	if(TotalWord[0].length() > 0 && TotalWord[1].length() > 0) 
                		stringwordBuff.append("And "); 
                	else
                		stringwordBuff.append("");
                	
                	if(TotalWord[1].length() > 0)
                		stringwordBuff.append(TotalWord[1] + "" + " Fills " + ""); //need to get currency format from table 
                	else
                		stringwordBuff.append("");
                	stringwordBuff.append("Only");
            }   		
        }
		return stringwordBuff.toString();
       
    }
}
       
