package common;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;

import domains.CompanySettingModel;
import domains.ExhibitionsModel;
import domains.InvoiceDetailModel;
import domains.InvoiceModel;

public class BillPrintable implements Printable {

	//https://gist.github.com/gayangithub/569eb8a162d8b8e47ad5
	private Logger logger = Logger.getLogger(this.getClass());
	
	String text;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//SimpleDateFormat sdtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat sdtf = new SimpleDateFormat("HH:mm:ss");
	InvoiceModel objCashInvoice;
	List<InvoiceDetailModel> lstInvDetails;
	CompanySettingModel objCompanySetting;
	ExhibitionsModel objExhibtion;;
	
	public BillPrintable(String invoice)
		{
		logger.info("print >>>  " + invoice);
		text=invoice;
	}
	public BillPrintable(CompanySettingModel _objCompanySetting,ExhibitionsModel _objExhibtion,InvoiceModel _objCashInvoice, List<InvoiceDetailModel> _lstInvDetails)
	{
		//logger.info("print >>>  " + header);		
		objCompanySetting=_objCompanySetting;
		objExhibtion=_objExhibtion;
		objCashInvoice= _objCashInvoice;
		lstInvDetails=_lstInvDetails;
	}
	
	@Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int result = NO_SUCH_PAGE;
        if (pageIndex == 0) {

            Graphics2D g2d = (Graphics2D) graphics;

            double width = pageFormat.getImageableWidth();
            logger.info("width " + width);
            

            g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY());

            ////////// code by alqama//////////////

            FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
            //    int idLength=metrics.stringWidth("000000");
            //int idLength=metrics.stringWidth("00");
            int idLength=metrics.stringWidth("000");
            int amtLength=metrics.stringWidth("000000");
            int qtyLength=metrics.stringWidth("00000");
            int priceLength=metrics.stringWidth("000000");
            int prodLength=(int)width - idLength - amtLength - qtyLength - priceLength-17;

            //    int idPosition=0;
            //    int productPosition=idPosition + idLength + 2;
            //    int pricePosition=productPosition + prodLength +10;
            //    int qtyPosition=pricePosition + priceLength + 2;
            //    int amtPosition=qtyPosition + qtyLength + 2;

            int productPosition = 0;
            int discountPosition= prodLength+5;
            int pricePosition = discountPosition +idLength+10;
            int qtyPosition=pricePosition + priceLength + 4;
            int amtPosition=qtyPosition + qtyLength;



            try{
            /*Draw Header*/
                int y=20;
                int yShift = 10;
                int headerRectHeight=15;
                int headerRectHeighta=40;

                ///////////////// Product names Get ///////////
                String  pn1a="Item";
                String pn2a="Item2";
                String pn3a="Item3";
                String pn4a="Item4";
                ///////////////// Product names Get ///////////


                ///////////////// Product price Get ///////////
                int pp1a=Integer.valueOf(1);
                int pp2a=Integer.valueOf(2);
                int pp3a=Integer.valueOf(3);
                int pp4a=Integer.valueOf(4);
                int sum=pp1a+pp2a+pp3a+pp4a;
                ///////////////// Product price Get ///////////

                g2d.setFont(new Font("Monospaced",Font.PLAIN,9));//Arial Monospaced
               // g2d.drawString(text, 0, 0);
               // text=objExhibtion.getExhibitionname()+"\n" + objCompanySetting.getCompanyName() + " " + objCompanySetting.getJenahname()+"\n";
               // g2d.drawString("-------------------------------------",12,y);
               // y+=yShift;
                drawCenterString(g2d,width,metrics,objExhibtion.getExhibitionname(),y);
                y+=yShift;
                drawCenterString(g2d,width,metrics,objCompanySetting.getCompanyName(),y);
                y+=yShift;
                drawCenterString(g2d,width,metrics,objCompanySetting.getJenahname(),y);
                y+=yShift;
                
               // g2d.drawString("      "+  objExhibtion.getExhibitionname()   +"        ",12,y);
                //y+=yShift;
                //g2d.drawString("      "+  objCompanySetting.getCompanyName()   +"        ",12,y);
                //y+=yShift;
               // drawCenteredString(g2d,objCompanySetting.getJenahname(),);
                //int x = (int) ((width - metrics.stringWidth(objCompanySetting.getJenahname())) / 2);
                //g2d.drawString(" "+  objCompanySetting.getJenahname()   +" ",x,y);
                //y+=yShift;
                
                String invDate=sdf.format(objCashInvoice.getCreationdate());
                g2d.drawString("      "+  invDate   +"        ",12,y);
                //y+=yShift;
                int dateLength=metrics.stringWidth("dd/MM/yyyy");
                g2d.drawString("      "+  sdtf.format(objCashInvoice.getCreationdate())   +"        ",50+dateLength,y);
                y+=yShift;
                g2d.drawString("-------------------------------------",12,y);
                y+=yShift;
                String customerName=objCashInvoice.getCustomername().equals("Cash")?"نقدي":objCashInvoice.getCustomername();
               
                g2d.drawString(""+  "رقم الفاتورة : "+objCashInvoice.getInvoiceprefix()   +" ",5,y);
                g2d.drawString(" "+  "السادة : "+customerName   +" ",150,y);
                y+=yShift;
                
                /*Draw Colums*/
               // g2d.drawLine(10, y+40, 180, y+40);
                y+=20;                                           
             
                g2d.drawString("المجموع", 10 ,y);
                g2d.drawString("السعر", 50 ,y);
                g2d.drawString("العدد", 80 ,y);
                g2d.drawString("اسم الكتاب", 150 ,y);
                //g2d.drawString("سطر", 220 ,y);
              //  g2d.drawLine(10, y+60, 220, y+60);
                y+=yShift;        
               // g2d.drawString("-------------------------------------",12,y);
               // y+=headerRectHeight;

                //g2d.drawString("-------------------------------------",10,y);y+=yShift;
                //g2d.drawString("  Book                      Price   ",10,y);y+=yShift;
                //g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;
               for(InvoiceDetailModel item : lstInvDetails)
               {
            	   g2d.drawString(String.valueOf(item.getQuantity()*item.getDiscountprice()), 10 ,y);
                   g2d.drawString(String.valueOf(item.getDiscountprice()), 50 ,y);
                   g2d.drawString(String.valueOf(item.getQuantity()), 80 ,y);
                   
                   String[] bookName=item.getBookname().split(" ");
                   if(bookName.length<3)
                   g2d.drawString(item.getBookname(), 150 ,y);
                   else
                   {
                	   g2d.drawString(bookName[0] + " " + bookName[1] + " " + bookName[2], 150 ,y);
                	   y+=yShift;
                	   for(int i=3;i<bookName.length;i++)
                	   g2d.drawString(bookName[i] + " ", 150 ,y);
                   }
                  
                  // g2d.drawString(""+item.getInvdline(), 220 ,y);
                   y+=yShift;  
            	   //g2d.drawString(" "+item.getBookname()+"                  " + item.getQuantity()*item.getDiscountprice() +"  ",10,y);y+=yShift;            	   
            	   //	table.addCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
   				//table.addCell(new Phrase(""+item.getDiscountprice(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
   				//table.addCell(new Phrase(""+(item.getQuantity()*item.getDiscountprice()) , FontFactory.getFont(FontFactory.HELVETICA, 11)));
               }
//                g2d.drawString(" "+pn1a+"                  "+pp1a+"  ",10,y);y+=yShift;
//                g2d.drawString(" "+pn2a+"                  "+pp2a+"  ",10,y);y+=yShift;
//                g2d.drawString(" "+pn3a+"                  "+pp3a+"  ",10,y);y+=yShift;
//                g2d.drawString(" "+pn4a+"                  "+pp4a+"  ",10,y);y+=yShift;
                
                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                g2d.drawString(" Total amount: "+sum+"               ",10,y);y+=yShift;
                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                //g2d.drawString("          Free Home Delivery         ",10,y);y+=yShift;
               // g2d.drawString("             03111111111             ",10,y);y+=yShift;
                g2d.drawString("*************************************",10,y);y+=yShift;
                g2d.drawString("    THANKS TO VISIT US   ",10,y);y+=yShift;
                g2d.drawString("*************************************",10,y);y+=yShift;





//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift;


            }
            catch(Exception r){
                r.printStackTrace();
            }

            result = PAGE_EXISTS;
        }
        return result;
    }
	
	private void drawCenterString(Graphics2D g2d,double width,FontMetrics metrics,String text, int y)
	{
		 int x = (int) ((width - metrics.stringWidth(text)) / 2);
		 g2d.drawString(text, x, y);
		 
	}
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}
}
