package reports;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.print.PrintService;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import common.BillPrintable;

public class PrintViewModel {

	private Logger logger = Logger.getLogger(this.getClass());
	private String invoice;
	private String message;
	private String printerName;
	private String selectPrint;
	public static final String DEST = "C:\\Khaled\\pos\\text2pdf.pdf";
	
	public PrintViewModel()
	{
		try
		{
			invoice=formatText();
		}
		catch(Exception ex)
		{
			message=ex.getMessage();
			logger.error("ERROR in PrintViewModel ----> init", ex);
		}
	}
	
	@Command	
	@NotifyChange({"printerName"})
	public void printerNameCommand()
	{
		try
		{			
			 StringBuilder sb=new StringBuilder();		       
	        for (PrintService printService : PrinterJob.lookupPrintServices()) {
	        	sb.append("-------------------------------------");		        
	        	sb.append('\n');
	        	sb.append(printService);
		        sb.append('\n');
                //System.out.println("check printer name of service " + printService);                
            }
	        
	        printerName=sb.toString();
		}
		catch(Exception ex)
		{
			message=ex.getMessage();
			logger.error("ERROR in PrintViewModel ----> printerNameCommand", ex);
		}

	}
	
	@Command	
	@NotifyChange({"message"})
	public void print1Command()
	{
		try
		{
			PrinterJob pjob = PrinterJob.getPrinterJob();
			pjob.setPrintable(new BillPrintable(formatText()),getPageFormat(pjob));

			//default
			//pjob.print();
			
			
			
//	       // pj.setPrintable(new BillPrintable(),getPageFormat(pj));
//	        for (PrintService printService : PrinterJob.lookupPrintServices()) {
//                System.out.println("check printer name of service " + printService);
//                if(printService.getName().contains("XPS")){
//                	pjob.setPrintService(printService);
//                	pjob.print();
//                	return;
//                }
//            }
	        
	        
		}
		catch(Exception ex)
		{
			message=ex.getMessage();
			logger.error("ERROR in PrintViewModel ----> print1Command", ex);
		}

	}
	
	@Command	
	@NotifyChange({"message"})
	public void print2Command()
	{
		try
		{
			PrinterJob pjob = PrinterJob.getPrinterJob();
			pjob.setPrintable(new BillPrintable(formatText()),getPageFormat(pjob));

	       // pj.setPrintable(new BillPrintable(),getPageFormat(pj));
	        for (PrintService printService : PrinterJob.lookupPrintServices()) {
                System.out.println("check printer name of service " + printService);
                if(printService.getName().contains(selectPrint)){
                	pjob.setPrintService(printService);
                	pjob.print();
                	return;
                }
            }
		}
		catch(Exception ex)
		{
			message=ex.getMessage();
			logger.error("ERROR in PrintViewModel ----> print2Command", ex);
		}

	}
	
	@Command	
	@NotifyChange({"message"})
	public void print3Command()
	{
		try
		{
			 File file = new File(DEST);
		     file.getParentFile().mkdirs();
		     createPdf(DEST);
		}
		catch(Exception ex)
		{
			message=ex.getMessage();
			logger.error("ERROR in PrintViewModel ----> print2Command", ex);
		}

	}
	
	private void createPdf(String dest)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        String aString =invoice; //"I am Alex";
        Reader inputString = new StringReader(aString);
        BufferedReader br = new BufferedReader(inputString);

        //BufferedReader br = new BufferedReader(new FileReader(TEXT));
        String line;
        Paragraph p;
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        boolean title = true;
        while ((line = br.readLine()) != null) {
            p = new Paragraph(line, title ? bold : normal);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            title = line.isEmpty();
            document.add(p);
        }
        document.close();
    }

	 private String formatText()
	    {
	        String result="";

	        int y=20;
	        int yShift = 10;
	        int headerRectHeight=15;
	        int headerRectHeighta=40;

	        ///////////////// Product names Get ///////////
	        String  pn1a="Item1";
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


	        StringBuilder sb=new StringBuilder();
	        sb.append("-------------------------------------");
	        sb.append('\n');
	        sb.append("      Restaurant Bill Receipt        ");
	        sb.append('\n');
	        sb.append("-------------------------------------");
	        sb.append('\n');
	        sb.append("-------------------------------------");
	        sb.append('\n');
	        sb.append(" Food Name                 T.Price   ");
	        sb.append('\n');
	        sb.append("-------------------------------------");
	        sb.append('\n');
	        sb.append(" "+pn1a+"                  "+pp1a + " " );
	        sb.append('\n');
	        sb.append(" "+pn2a+"                  "+pp2a+"  ");
	        sb.append('\n');
	        sb.append(" "+pn3a+"                  "+pp3a+"  ");
	        sb.append('\n');
	        sb.append(" "+pn4a+"                  "+pp4a+"  ");
	        sb.append('\n');
	        sb.append("-------------------------------------");
	        sb.append('\n');
	        sb.append(" Total amount: "+sum+"               ");
	        sb.append('\n');
	        sb.append("-------------------------------------");
	        sb.append('\n');
	        sb.append("          Free Home Delivery         ");
	        sb.append('\n');
	        sb.append("             03111111111             ");
	        sb.append('\n');
	        sb.append("*************************************");
	        sb.append('\n');
	        sb.append("    THANKS TO VISIT OUR RESTUARANT   ");
	        sb.append('\n');
	        sb.append("*************************************");
	        sb.append('\n');

	        result =sb.toString();

	        return result;
	    }
	 
	 public PageFormat getPageFormat(PrinterJob pj)
	    {

	        PageFormat pf = pj.defaultPage();
	        Paper paper = pf.getPaper();

	        double middleHeight =8.0;
	        double headerHeight = 2.0;
	        double footerHeight = 2.0;
	        double width = convert_CM_To_PPI(8);      //printer know only point per inch.default value is 72ppi
	        double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight);
	        paper.setSize(width, height);
	        paper.setImageableArea(
	                0,
	                10,
	                width,
	                height - convert_CM_To_PPI(1)
	        );   //define boarder size    after that print area width is about 180 points

	        pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
	        pf.setPaper(paper);

	        return pf;
	    }

	    protected static double convert_CM_To_PPI(double cm) {
	        return toPPI(cm * 0.393600787);
	    }

	    protected static double toPPI(double inch) {
	        return inch * 72d;
	    }
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public String getSelectPrint() {
		return selectPrint;
	}

	public void setSelectPrint(String selectPrint) {
		this.selectPrint = selectPrint;
	}
	
}
