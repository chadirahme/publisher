package sales;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.swing.JEditorPane;

import login.LoginData;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.RowRenderer;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.exporter.excel.ExcelExporter.ExportContext;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.xssf.usermodel.XSSFCellStyle;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.lowagie.tools.Executable;

import common.BillPrintable;
import common.NumberToWord;
import common.PrintJobWatcher;
import books.BooksData;
import domains.BooksModel;
import domains.CompanySettingModel;
import domains.CustomerModel;
import domains.DataFilter;
import domains.ExhibitionsModel;
import domains.InvoiceDetailModel;
import domains.InvoiceModel;
import domains.ReportModel;
import domains.Users;

public class CashInvoiceViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	NumberToWord numbToWord=new NumberToWord();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//SimpleDateFormat sdtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat sdtf = new SimpleDateFormat("HH:mm:ss");
	DateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat decimalformatter = new DecimalFormat("#,###.00");
	CashInvoiceData data=new CashInvoiceData();
	String viewType;
	private InvoiceModel objCashInvoice;
	//private List<InvoiceDetailModel> lstInvoiceDetails;
	private Users objUser;
	private Users objChangeUser;
	private ExhibitionsModel objExhibtion;
	private List<BooksModel> lstBooks;
	private List<InvoiceDetailModel> lstInvDetails;
	private InvoiceDetailModel selectedInvDetail;
	private String bookName;
	private List<InvoiceModel> lstOnHoldInvoices;
	private List<InvoiceModel> lstAllOnHoldInvoices;
	private DataFilter filter=new DataFilter();
	private String discountType;
	
	private List<ExhibitionsModel> lstExhibitions;
	private ExhibitionsModel selectedExhibitions;
	private List<InvoiceModel> lstInvoices;
	private List<InvoiceModel> lstAllInvoices;
	private String focusNewLine = "false";
	private boolean bookFocus;
	private boolean quantityFocus;
	//return invoice
	private InvoiceModel objReturnInvoice;
	private String printInvoicePath;
	
	private Date fromDate;
	private Date todate;
	private String invoiceFooter;
	private CompanySettingModel objCompanySetting;
	
	//used in invoicelist
	private double totalPaidAmount;
	private double totalPaidDollarAmount;
	private double totalInvoiceListAmount;
	
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();			
		viewType=type==null?"":type;
		
		Session sess = Sessions.getCurrent();		
		objUser=(Users) sess.getAttribute("Authentication");
		if(objUser!=null)
		{
			invoiceFooter=objUser.getInvoiceFooter();
		}
		
		if(viewType.equals("cashinvoice"))
		{
			objCompanySetting=data.getCompanySetting();
			objExhibtion=data.getActiveExhibtions();
			Calendar c = Calendar.getInstance();
			objCashInvoice=new InvoiceModel();
			objCashInvoice.setExhibitionid(objExhibtion.getExhibitionid());			
			objCashInvoice.setDiscounttype(0);
			discountType="1";
			objCashInvoice.setCreationdate(c.getTime());
			objCashInvoice.setInvoicedate(df.parse(sdf.format(c.getTime())));
			if(objUser!=null)
				objCashInvoice.setCreatedby(objUser.getFirstname());
			objCashInvoice.setInvoicenumber(data.getMaxInvoiceNumber(objExhibtion.getExhibitionid()));		
			objCashInvoice.setInvoiceprefix(objExhibtion.getPrefix() + objCashInvoice.getInvoicenumber());		
			objCashInvoice.setCustomername("Cash");
			objCashInvoice.setDateofpay("");
			objCashInvoice.setNotes("");
			objCashInvoice.setExhibitionTax(objExhibtion.getTax());
					
			bookName="";
			lstBooks=data.getExhibtionBooksList(objExhibtion.getExhibitionid());
			
			lstInvDetails=new ArrayList<InvoiceDetailModel>();
			focusNewLine="true";
			bookFocus=true;
			
		}
		
		else if(viewType.equals("unpaid"))
		{
			objCashInvoice=(InvoiceModel) map.get("objCashInvoice");
			lstInvDetails=(List<InvoiceDetailModel>) map.get("lstInvDetails");
			objExhibtion=(ExhibitionsModel) map.get("objExhibtion");
		}
		else if(viewType.equals("onholdInvoices"))
		{
			objExhibtion=(ExhibitionsModel) map.get("objExhibtion");
			lstOnHoldInvoices=data.getInvoicesListByStatus(objExhibtion.getExhibitionid(), 3,null,null);
			lstAllOnHoldInvoices=lstOnHoldInvoices;
		}
		else if(viewType.equals("user"))
		{
			objCashInvoice=(InvoiceModel) map.get("objCashInvoice");
			objReturnInvoice=(InvoiceModel) map.get("objReturnInvoice");
			objChangeUser=new Users();
		}
		
		else if(viewType.equals("invoicelist"))
		{
			Calendar c = Calendar.getInstance();
			fromDate=df.parse(sdf.format(c.getTime()));
			todate=df.parse(sdf.format(c.getTime()));
			objExhibtion=data.getActiveExhibtions();	
			BooksData bdata=new BooksData();
			lstExhibitions=bdata.getActiveExhibitionsList("اختار");
			if(lstExhibitions.size()==2)
			selectedExhibitions=lstExhibitions.get(1);
			else
			selectedExhibitions=lstExhibitions.get(0);	
		}
		
		else if(viewType.equals("returninvoice"))			
		{
			objExhibtion=data.getActiveExhibtions();
			Calendar c = Calendar.getInstance();
			objReturnInvoice=new InvoiceModel();
			
			objReturnInvoice.setExhibitionid(objExhibtion.getExhibitionid());			
			objReturnInvoice.setDiscounttype(0);
			discountType="1";
			objReturnInvoice.setInvoicedate(df.parse(sdf.format(c.getTime())));
			if(objUser!=null)
				objReturnInvoice.setCreatedby(objUser.getFirstname());
			
			objReturnInvoice.setInvoicenumber(data.getMaxInvoiceNumber(objExhibtion.getExhibitionid()));		
			objReturnInvoice.setInvoiceprefix(objExhibtion.getPrefix() + objReturnInvoice.getInvoicenumber());		
			objReturnInvoice.setCustomername("Cash");
			objReturnInvoice.setDateofpay("");
			objReturnInvoice.setNotes("");
			
			bookName="";
			lstBooks=data.getExhibtionBooksList(objExhibtion.getExhibitionid());
			
			lstInvDetails=new ArrayList<InvoiceDetailModel>();
		}
		
		else if(viewType.equals("viewcashinvoice"))			
		{
			objExhibtion=(ExhibitionsModel) map.get("objExhibtion");
			objCompanySetting=data.getCompanySetting();
			objCashInvoice=(InvoiceModel) map.get("viewInvoice");		
			discountType=objCashInvoice.getDiscounttype()+"";
			lstInvDetails=data.getInvoicesDetailsList(objCashInvoice.getInvoiceid());			
		}
		
		else if (viewType.equals("payInvoice"))
		{
			objExhibtion=(ExhibitionsModel) map.get("objExhibtion");
			objCashInvoice=(InvoiceModel) map.get("objCashInvoice");
			lstInvDetails=(List<InvoiceDetailModel>) map.get("lstInvDetails");	
			discountType=(String) map.get("discountType");
			objCompanySetting=(CompanySettingModel) map.get("objCompanySetting");
		}												
		
	
		}
		
		catch (Exception ex) 
		{
			  logger.error("error in CashInvoiceViewModel---init-->" , ex);			
		}
	}

	@Command
	public void openCustomersCommand()
	{		
		try
		{
		//selectedGridItems=type;
		Map<String,Object> arg = new HashMap<String,Object>();		
		arg.put("objCashInvoice", objCashInvoice);
		Executions.createComponents("/sales/customerpopup.zul", null,arg);		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> openCustomersCommand", ex);
		}
	}
	@GlobalCommand 
	@NotifyChange({"objCashInvoice","objReturnInvoice"})
	public void refreshCustomerParent(@BindingParam("selectedCustomer")CustomerModel selectedCustomer ,
			@BindingParam("objCashInvoice")InvoiceModel PopupobjCashInvoice )
	{
		if(objReturnInvoice==null)
		{
		objCashInvoice=PopupobjCashInvoice;	
		objCashInvoice.setCustomerid(selectedCustomer.getCustomerid());
		objCashInvoice.setCustomername(selectedCustomer.getCustomername());
		objCashInvoice.setCustomerDiscount(selectedCustomer.getSpecial());
		}
		else if(objCashInvoice==null)
		{
			objReturnInvoice.setCustomerid(selectedCustomer.getCustomerid());
			objReturnInvoice.setCustomername(selectedCustomer.getCustomername());
			objReturnInvoice.setCustomerDiscount(selectedCustomer.getSpecial());
		}
	}
	
	
	@Command
	public void openBooksCommand()
	{
		//open items popup
		try
		{
		//selectedGridItems=type;
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("lstBooks",lstBooks);
		arg.put("bookName",bookName);
		arg.put("type","cash");
		arg.put("objExhibtion", objExhibtion);
		//arg.put("lstItems", lstItems);
		//arg.put("type","items");
		//arg.put("compSetup", compSetup);
		//arg.put("lstInvcCustomerGridClass", lstInvcCustomerGridClass);
		Executions.createComponents("/sales/bookspopup.zul", null,arg);		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> openBooksCommand", ex);
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"lstInvDetails","objCashInvoice","bookFocus","quantityFocus"})
	public void refreshBooksParent(@BindingParam("selectedBook")BooksModel selectedBook , @BindingParam("objExhibtion")ExhibitionsModel objExhibtion,
			@BindingParam("lstSelectedBooks") List<BooksModel> lstSelectedBooks)
	{
		try
		{
		//check if same book added
		if(lstSelectedBooks!=null)
		{			
			for (BooksModel book : lstSelectedBooks) 
			{				
				boolean isbookFound=false;
				for (InvoiceDetailModel item : lstInvDetails)
				{					
					if(item.getBookid()==book.getBookid())
					{
						item.setQuantity(item.getQuantity()+1);
						item.setRemainquantity(book.getRemainquantity() -1 );
						book.setRemainquantity(book.getRemainquantity() -1);	
						isbookFound=true;
					}
				}
				if(isbookFound==false)
				{
					addBooksToList(book);
				}			
			}
			getTotalInvoice(null);
			return;
		}
		
		if(selectedBook!=null)
		{
		for (InvoiceDetailModel item : lstInvDetails)
		{
			if(item.getBookid()==selectedBook.getBookid())
			{
				item.setQuantity(item.getQuantity()+1);
				item.setRemainquantity(selectedBook.getRemainquantity() -1 );
				selectedBook.setRemainquantity(selectedBook.getRemainquantity() -1);
				getTotalInvoice(null);
				return;
			}
		}
		addBooksToList(selectedBook);
		
		getTotalInvoice(null);
		bookFocus=false;
		quantityFocus=true;
				
		}
		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> refreshBooksParent", ex);
		}
	}
	private void addBooksToList(BooksModel selectedBook)
	{
		InvoiceDetailModel objInvDetail=new InvoiceDetailModel();
		objInvDetail.setInvdline(lstInvDetails.size()+1);
		objInvDetail.setBookid(selectedBook.getBookid());
		objInvDetail.setBookcode(selectedBook.getBookCode());
		objInvDetail.setBookname(selectedBook.getBookName());
		objInvDetail.setQuantity(1);
		objInvDetail.setPrice(selectedBook.getBookPrice());//selectedBook.getLocalPrice());
		objInvDetail.setDiscountprice(selectedBook.getLocalPrice());//objInvDetail.getPrice());
		objInvDetail.setRemainquantity(selectedBook.getRemainquantity() -1 );
		objInvDetail.setMainquantity(selectedBook.getRemainquantity());
		selectedBook.setRemainquantity(selectedBook.getRemainquantity() -1);
		
		if(objCashInvoice.getCustomerDiscount()>0)
		{
			//sum disc from customer and setup
			double totalDiscount=0;
			totalDiscount=objCashInvoice.getCustomerDiscount();
			if(discountType.equals("1"))
			{
				totalDiscount+=objExhibtion.getDiscountaudience();
			}
			else
			{
				totalDiscount+=objExhibtion.getDiscounttotal();
			}
			double disc=totalDiscount/100;
			double discPrice=objInvDetail.getDiscountprice()- ( objInvDetail.getDiscountprice() * disc);
			objInvDetail.setDiscountprice(discPrice);
		}
		else
		{
		//check discount from setup		
		if(discountType.equals("1"))
		{
			if(objExhibtion.getDiscountaudience()>0)
			{
				double discAud=objExhibtion.getDiscountaudience()/100;
				double discPrice=objInvDetail.getDiscountprice()- ( objInvDetail.getDiscountprice() * discAud);
				objInvDetail.setDiscountprice(discPrice);
			}			
		}
		if(discountType.equals("2"))
		{
			if(objExhibtion.getDiscounttotal()>0)
			{
				double disc=objExhibtion.getDiscounttotal()/100;
				double discPrice=objInvDetail.getDiscountprice()- ( objInvDetail.getDiscountprice() * disc);
				objInvDetail.setDiscountprice(discPrice);
			}			
		}
		
		}
		
		int count=lstInvDetails.size();
		objInvDetail.setTotalprice(objInvDetail.getDiscountprice());
		lstInvDetails.add(0,objInvDetail);
	}
	
	@Command
	public void onBlurCommand()
	{
		logger.info("blur >>> "+bookName);
		
	}
	
	@Command
	@NotifyChange({"objCashInvoice","lstInvDetails"})
	public void deleteBookCommand(@BindingParam("todo") InvoiceDetailModel todo)
	{
		findBookByID(todo.getBookid(),todo.getMainquantity());	
		lstInvDetails.remove(todo);
		getTotalInvoice(null);
	}
	
	@Command
	@NotifyChange({"objCashInvoice"})//"lstInvDetails"
	public void getTotalInvoice(@BindingParam("row") InvoiceDetailModel row)
	{
		double invoiceamount=0;
		double invoicediscount=0;
		double invoicetotal=0;
		double paidamount=0;
		int totalQunatity=0;
		
		for (InvoiceDetailModel item : lstInvDetails) 
		{
			invoiceamount+=item.getDiscountprice() * item.getQuantity(); //item.getTotalprice();
			totalQunatity+=item.getQuantity();
		}
		
		objCashInvoice.setInvoiceamount(invoiceamount);
		objCashInvoice.setTotalQunatity(totalQunatity);
		
		invoicetotal= invoiceamount;
		
		 double invoicediscountPerc= objCashInvoice.getInvoicediscountPerc();
		 if(invoicediscountPerc>0)
		 {
			 invoicetotal=invoiceamount - ( invoiceamount*(invoicediscountPerc/100));
		 }
		//objCashInvoice.setInvoicediscount(0);
		
		invoicetotal= invoicetotal - objCashInvoice.getInvoicediscount();
		if(invoicetotal>0)
		objCashInvoice.setInvoicetotal(invoicetotal);
		else
		objCashInvoice.setInvoicetotal(0);
		
		objCashInvoice.setPaidamount(objCashInvoice.getInvoicetotal());
		objCashInvoice.setInvoicetotalBeforeTax(objCashInvoice.getInvoicetotal());
		
		
		if(row!=null)
		{
			row.setRemainquantity(row.getMainquantity() - row.getQuantity());	
			findBookByID(row.getBookid(),row.getMainquantity() - row.getQuantity());			
		}
	}
	
	private void findBookByID(int bookId ,int quantity)
	{				
		if(lstBooks!=null) //use this when edit invoice
		{
		for (BooksModel item : lstBooks)
		{
			if(item.getBookid()==bookId)
			{
				item.setRemainquantity( quantity);
				break;
			}
		}	
		}
	}
	@Command
	@NotifyChange({"objCashInvoice","lstInvDetails","discountType"})
	public void cashPayCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			logger.info(objCashInvoice.getDiscounttype());
			objCashInvoice.setPaidDollarAmount(objCashInvoice.getInvoicetotal() /objExhibtion.getCurrencyrate());			
			objCashInvoice.setInvoicestatus(1);
			objCashInvoice.setDiscounttype(Integer.parseInt(discountType));
			data.addNewInvoice(objCashInvoice ,lstInvDetails);
			Messagebox.show("تم حفظ الفاتورة بنجاح" ,"فاتورة نقدية", Messagebox.OK , Messagebox.INFORMATION);
			
			//cancelCommand();
			BindUtils.postGlobalCommand(null, null, "cancelCommand", null);
			comp.detach();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> cashPayCommand", ex);
		}
	}
	@Command
	@NotifyChange({"objCashInvoice","lstInvDetails","discountType"})
	public void cashPayInDollarCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			logger.info(objCashInvoice.getDiscounttype());
			objCashInvoice.setPaidDollarAmount(objCashInvoice.getInvoicetotal() /objExhibtion.getCurrencyrate());
			objCashInvoice.setPaidIndollar(1);			
			objCashInvoice.setInvoicestatus(1);
			objCashInvoice.setDiscounttype(Integer.parseInt(discountType));
			data.addNewInvoice(objCashInvoice ,lstInvDetails);
			Messagebox.show("تم حفظ الفاتورة بنجاح" ,"فاتورة نقدية", Messagebox.OK , Messagebox.INFORMATION);
			
			//cancelCommand();
			BindUtils.postGlobalCommand(null, null, "cancelCommand", null);
			comp.detach();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> cashPayCommand", ex);
		}
	}
	
	@Command
	public void unPaidCommand()
	{
		try
		{
			if(objCashInvoice.getCustomerid()==0)
			{
				Messagebox.show("select customer first !!.","Invoice", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			objCashInvoice.setInvoicestatus(2);
			objCashInvoice.setDiscounttype(Integer.parseInt(discountType));
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("objCashInvoice",objCashInvoice);
			arg.put("lstInvDetails",lstInvDetails);
			arg.put("objExhibtion",objExhibtion);
			Executions.createComponents("/sales/unpaidpopup.zul", null,arg);	
						
			//data.addNewInvoice(objCashInvoice ,lstInvDetails);
			//Messagebox.show("Done");
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> cashPayCommand", ex);
		}
	}
	
	@Command
	@NotifyChange({"objCashInvoice"})
	public void getbalanceAmount()
	{
		logger.info(objCashInvoice.getInvoicetotal() - objCashInvoice.getPaidamount());
		
		objCashInvoice.setBalanceamount(objCashInvoice.getInvoicetotal() - objCashInvoice.getPaidamount());
	}
	
	@Command
	@NotifyChange({"objCashInvoice","lstInvDetails"})
	public void saveUnpiadCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			logger.info("saveUnpiadCommand");			
			data.addNewInvoice(objCashInvoice ,lstInvDetails);
						
			BindUtils.postGlobalCommand(null, null, "cancelCommand", null);
			comp.detach();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> saveUnpiadCommand", ex);
		}
	}
	
	@GlobalCommand 
	@Command
	@NotifyChange({"objCashInvoice","lstInvDetails","discountType"})
	public void cancelCommand()
	{
		try
		{
		Calendar c = Calendar.getInstance();
		objCashInvoice=new InvoiceModel();
		objCashInvoice.setExhibitionid(objExhibtion.getExhibitionid());	
		discountType="1";
		objCashInvoice.setDiscounttype(1);
		objCashInvoice.setInvoicedate(df.parse(sdf.format(c.getTime())));
		objCashInvoice.setCreationdate(c.getTime());
		if(objUser!=null)
			objCashInvoice.setCreatedby(objUser.getFirstname());
		objCashInvoice.setInvoicenumber(data.getMaxInvoiceNumber(objExhibtion.getExhibitionid()));		
		objCashInvoice.setInvoiceprefix(objExhibtion.getPrefix() + objCashInvoice.getInvoicenumber());		
		objCashInvoice.setCustomername("Cash");
		objCashInvoice.setDateofpay("");
		objCashInvoice.setNotes("");
		objCashInvoice.setExhibitionTax(objExhibtion.getTax());
		//bookName="";
		lstBooks=data.getExhibtionBooksList(objExhibtion.getExhibitionid());
	
		
		lstInvDetails=new ArrayList<InvoiceDetailModel>();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> cancelCommand", ex);
		}
	}
	
	@Command
	@NotifyChange({"objCashInvoice","lstInvDetails"})
	public void onHoldCommand()
	{
		try
		{
			objCashInvoice.setDiscounttype(Integer.parseInt(discountType));
			objCashInvoice.setInvoicestatus(3);
			data.addNewInvoice(objCashInvoice ,lstInvDetails);
			
		Calendar c = Calendar.getInstance();
		objCashInvoice=new InvoiceModel();
		objCashInvoice.setExhibitionid(objExhibtion.getExhibitionid());			
		objCashInvoice.setDiscounttype(1);
		discountType="1";
		objCashInvoice.setInvoicedate(df.parse(sdf.format(c.getTime())));
		if(objUser!=null)
			objCashInvoice.setCreatedby(objUser.getFirstname());
		objCashInvoice.setInvoicenumber(data.getMaxInvoiceNumber(objExhibtion.getExhibitionid()));		
		objCashInvoice.setInvoiceprefix(objExhibtion.getPrefix() + objCashInvoice.getInvoicenumber());		
		objCashInvoice.setCustomername("Cash");
		objCashInvoice.setDateofpay("");
		objCashInvoice.setNotes("");
		//bookName="";
		//lstBooks=data.getExhibtionBooksList(objExhibtion.getExhibitionid());
		
		lstInvDetails=new ArrayList<InvoiceDetailModel>();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> cancelCommand", ex);
		}
	}
	
	@Command
	public void onholdInvoicepopupCommand()
	{		
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("objExhibtion",objExhibtion);
		Executions.createComponents("/sales/onholdpopup.zul", null,arg);	
	}
	
	@Command
	public void viewInvoicepopupCommand(@BindingParam("row") InvoiceModel row)
	{		
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("objExhibtion",objExhibtion);
		arg.put("viewInvoice", row);
		Executions.createComponents("/sales/viewinvoice.zul", null,arg);	
	}
		
	
	@Command
    @NotifyChange({"lstOnHoldInvoices"})
    public void changeFilter() 
    {	      
		lstOnHoldInvoices=filterData();
	  
    }
	
	private List<InvoiceModel> filterData()
	{
		lstOnHoldInvoices=lstAllOnHoldInvoices;
		List<InvoiceModel>  lst=new ArrayList<InvoiceModel>();
		for (Iterator<InvoiceModel> i = lstOnHoldInvoices.iterator(); i.hasNext();)
		{
			InvoiceModel tmp=i.next();				
			if(tmp.getCustomername().toLowerCase().contains(filter.getCustomerName().toLowerCase())
					&& tmp.getInvoiceprefix().toLowerCase().contains(filter.getInvoiceprefix().toLowerCase())
					&& tmp.getCreatedby().toLowerCase().contains(filter.getCreatedby().toLowerCase())
					&& mysqldf.format(tmp.getInvoicedate()).contains(filter.getInvoicedate().toLowerCase())
					//&& tmp.getInvoicetotal()
					)
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	
	@Command
	public void selectOnHoldCommand(@ContextParam(ContextType.VIEW) Window comp,@BindingParam("row") InvoiceModel row)
	{
		Map args = new HashMap();		
		args.put("selectedInvoice", row);	
		
		BindUtils.postGlobalCommand(null, null, "refreshOnHoldParent", args);	
		comp.detach();
	}
	
	@Command
	public void onHoldOkCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		if(lstOnHoldInvoices.size()==1)
		{
		Map args = new HashMap();				
		args.put("selectedInvoice", lstOnHoldInvoices.get(0));	
		BindUtils.postGlobalCommand(null, null, "refreshOnHoldParent", args);	
		comp.detach();
		}
	}
		
	@GlobalCommand 
	@NotifyChange({"lstInvDetails","objCashInvoice","discountType"})
	public void refreshOnHoldParent(@BindingParam("selectedInvoice")InvoiceModel selectedInvoice)
	{
		try
		{
			int totalQunatity=0;
			objCashInvoice=selectedInvoice;
			discountType=objCashInvoice.getDiscounttype()+"";
			lstInvDetails=data.getInvoicesDetailsList(objCashInvoice.getInvoiceid());		
			for (InvoiceDetailModel item : lstInvDetails) 
			{
				//invoiceamount+=item.getDiscountprice() * item.getQuantity(); //item.getTotalprice();
				totalQunatity+=item.getQuantity();
			}
			
			//objCashInvoice.setInvoiceamount(invoiceamount);
			objCashInvoice.setTotalQunatity(totalQunatity);
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> refreshOnHoldParent", ex);
		}
	}
	
	@Command
	public void deleteOnHoldCommand(@ContextParam(ContextType.VIEW) Window comp,@BindingParam("todo") InvoiceModel todo)
	{
		try
		{
			data.deleteOnHoldInvoice(todo);
			Messagebox.show("Invoice is deleted !!","Invoice List", Messagebox.OK , Messagebox.EXCLAMATION);	
			comp.detach();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> refreshOnHoldParent", ex);
		}
	}
	
	@Command
	public void changeUserCommand()
	{
		//changeuser.zul
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("objCashInvoice",objCashInvoice);
		//arg.put("objReturnInvoice",objReturnInvoice);
		arg.put("type", "cash");
		//arg.put("lstInvDetails",lstInvDetails);
		//arg.put("objExhibtion",objExhibtion);
		
		Executions.createComponents("/sales/changeuser.zul", null,arg);
	}
	
	@Command
	public void applyChangeUserCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
		LoginData data=new LoginData();		
		Users objUser= data.getUserLogin(objChangeUser);
		if(objUser!=null)
		{			
			Sessions.getCurrent().setAttribute("Authentication", objUser);	
			Map args = new HashMap();
			objCashInvoice.setCreatedby(objUser.getFirstname());
			args.put("objCashInvoice", objCashInvoice);					
			BindUtils.postGlobalCommand(null, null, "refreshUserParent", args);
			comp.detach();
			
		}
		else
		{
			Messagebox.show("Wrong..");
		}
		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> applyChangeUserCommand", ex);
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"objCashInvoice"})
	public void refreshUserParent(@BindingParam("objChangeUser")Users objChangeUser)
	{
		try
		{
			logger.info(viewType + " " + objChangeUser.getFirstname());			
			objCashInvoice.setCreatedby(objChangeUser.getFirstname());			
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> refreshUserParent", ex);
		}
	}
	
	@Command
	@NotifyChange({"lstInvoices","totalPaidAmount","totalPaidDollarAmount","totalInvoiceListAmount"})
	public void viewInvoiceListCommand()
	{
		try
		{
			totalPaidAmount=0;
			totalPaidDollarAmount=0;
			totalInvoiceListAmount=0;
			
			lstInvoices=data.getInvoicesListByStatus(selectedExhibitions.getExhibitionid(), 0,fromDate,todate);
			lstAllInvoices=lstInvoices;
			//get total footer
			for (InvoiceModel item : lstAllInvoices) 
			{
				totalInvoiceListAmount+=item.getInvoiceamount(); //item.getInvoicetotal();
				if(item.getPaidIndollar()==0)
				totalPaidAmount+=item.getPaidamount();
				if(item.getPaidIndollar()==1)
				totalPaidDollarAmount+=item.getPaidDollarAmount();
			}
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> viewInvoiceListCommand", ex);
		}
	}
	
	@Command
	public void exportInvoiceListToExcel()
	{
		try
		{
		 if(lstInvoices==null)
		  {
	  		Messagebox.show("There is no record !!","Invoice List", Messagebox.OK , Messagebox.EXCLAMATION);
	  		return;
		  }
		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 String[] tsHeaders;
		 tsHeaders = new String[]{"رقم الفاتورة", "تاريخ الفاتورة", "إسم العميل" ,"المجموع", "حسم خاص","الإجمالي" , "الدفع" , "المستخدم" , "الحالة" };
		 final String[] headers=tsHeaders;
		 
		 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
		     
			    @Override
			    public void beforeRendering(XSSFWorkbook target) {
			        ExportContext context = exporter.getExportContext();
			         
			        for (String header : headers) {
			            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
			            cell.setCellValue(header);
			             				           
			                CellStyle srcStyle = cell.getCellStyle();
			                if (srcStyle.getAlignment() != CellStyle.ALIGN_CENTER) {				                   
								XSSFCellStyle newCellStyle = target.createCellStyle();
			                    newCellStyle.cloneStyleFrom(srcStyle);
			                    newCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			                    cell.setCellStyle(newCellStyle);
			                }
			            
			        }
			    }
			    
			    @Override
			    public void afterRendering(XSSFWorkbook target) {
			    }				    				   
			});
		 
			exporter.export(headers.length, lstInvoices, new RowRenderer<Row, InvoiceModel>() {
				@Override
				public void render(Row table, InvoiceModel item, boolean isOddRow) 
					 {
					 	ExportContext context = exporter.getExportContext();
				        XSSFSheet sheet = context.getSheet();				        
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoiceprefix());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(sdf.format(item.getInvoicedate()));
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCustomername());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoiceamount());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoicediscount());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoicetotal());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getPaidamount());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCreatedby());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getStatusName());
					 }
					 
			    }, out);
			 
			   	AMedia amedia = new AMedia("InvoicesReport.xls", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
			 
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in CashInvoiceViewModel---exportInvoiceListToExcel-->" , ex);			
		}
	}
	@Command
    @NotifyChange({"lstInvoices"})
    public void changeInvoiceFilter() 
    {	      
		lstInvoices=filterInvoiceData();
	  
    }
	
	
	
	private List<InvoiceModel> filterInvoiceData()
	{
		lstInvoices=lstAllInvoices;
		List<InvoiceModel>  lst=new ArrayList<InvoiceModel>();
		for (Iterator<InvoiceModel> i = lstInvoices.iterator(); i.hasNext();)
		{
			InvoiceModel tmp=i.next();				
			if(tmp.getCustomername().toLowerCase().contains(filter.getCustomerName().toLowerCase())
					&& tmp.getInvoiceprefix().toLowerCase().contains(filter.getInvoiceprefix().toLowerCase())
					&& tmp.getCreatedby().toLowerCase().contains(filter.getCreatedby().toLowerCase())
					&& tmp.getStatusName().toLowerCase().contains(filter.getStatusName().toLowerCase())
					&& mysqldf.format(tmp.getInvoicedate()).contains(filter.getInvoicedate().toLowerCase())
					//&& tmp.getInvoicetotal()
					)
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	
	@Command
	public void testPrint()
	{
		try
		{
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
			System.out.println("Printer online: "+printService);
			
			String data = "Teltac invoce for Customer KPN";
			data+="Teltac invoce for Customer KPN 1";
			data+="Teltac invoce for Customer KPN 2";
			data+="Teltac invoce for Customer KPN 3";
			data+="Teltac invoce for Customer KPN 4";
			data+="Teltac invoce for Customer KPN 5";
			String newline = System.getProperty("line.separator");
			String mText = "SHOP MA" + "\n" 
			        + "----------------------------" + "\n" 
			        + "Pannampitiya" +newline
			        + "09-10-2012 harsha  no: 001" + "\n" 
			        + "No  Item  Qty  Price  Amount" + "\n" 
			        + "1 Bread 1 50.00  50.00" + "\n" 
			        + "_________________Test Arabic___________" + "\n";
				mText+="فاتورة نقدية";
			//mText="";
			byte[] b = null;
			/*for (int i = lstInvDetails.size()-1; i >-1 ; i--) 
			{
				InvoiceDetailModel item=lstInvDetails.get(i);
				//table.addCell(new Phrase(""+item.getInvdline(),FontFactory.getFont(FontFactory.HELVETICA, 11)));
				
				//PdfPCell cell=new PdfPCell(new Phrase(item.getBookname(),arfont));
				//cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				//cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				//table.addCell(cell);
			    b= item.getBookname().getBytes("UTF-8");
				//mText+=new String(item.getBookname().getBytes("UTF-8"),"UTF-8") + "\n" ;
				mText+=item.getBookname() + "\n" ;
			//	System.out.println(new String(item.getBookname().getBytes("UTF8")));
				
				//table.addCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
				//table.addCell(new Phrase(""+item.getDiscountprice(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
				//table.addCell(new Phrase(""+(item.getQuantity()*item.getDiscountprice()) , FontFactory.getFont(FontFactory.HELVETICA, 11)));
			}*/
			        //+ "\\x1B" + "\\x69";
			// String openDrawerCommand = ((char) 0x1B) + "";
			// mText+=openDrawerCommand;
			// byte[] bytes = {27, 100, 3};
			 
			 String GS = (char)29+"";
			 String ESC = (char)27+"";

			 String COMMAND = "";
			 COMMAND = ESC + "@";
			 COMMAND += GS + "V" + (char)1;
			// mText+=COMMAND;
			 
			//create a print job
			DocPrintJob job = printService.createPrintJob();
			
			//define the format of print document
			//bytes[] bytes = str.getBytes(Charset.forName("UTF-8"));
			//InputStream in = IOUtils.toInputStream(source, "UTF-8");
			//BufferedReader br = new BufferedReader(new StringReader(mText));
			
			
			//InputStream is = new ByteArrayInputStream(mText.getBytes(Charset.forName("UTF-8")));
			//BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		//	String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"invoices"+File.separator;
		//	String fileName=repository+"11"+File.separator+"test.txt";
		//	logger.info(fileName);
			
			//ByteArrayOutputStream printData = new ByteArrayOutputStream();
			//printData.write("Hello".getBytes("CP437"));
			
			
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			/*FileInputStream textStream;
			textStream = new FileInputStream(fileName);
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			Doc mydoc = new SimpleDoc(textStream, flavor, null);*/
			
			//byte[] encodedBytes = Base64.decode(mText);//.getEncoder().encodeToString("JavaTips.net".getBytes("utf-8"));
			
			byte[] originalBytes=mText.getBytes("UTF-8");
			byte[] newBytes = new String(originalBytes, "UTF8").getBytes("Windows-1256");
			InputStream is = new ByteArrayInputStream(mText.getBytes());
						
			
			//DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
			//DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			
			//DocAttributeSet jobAttributeSet = new HashDocAttributeSet();
			//jobAttributeSet.add(OrientationRequested.LANDSCAPE); 
			//print the data
			Doc doc = new SimpleDoc(is, flavor, null);
			
			PrintJobWatcher watcher = new PrintJobWatcher(job);
			
			//PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		   // pras.add(new Copies(2));
			
			job.print(doc, null);
			 
			 // Wait for the print job to be done
			watcher.waitForDone();
            
			is.close();
			System.out.println("Printing Done!!");
			//cutPaper();
			
		}
		catch(Exception ex)
		{
			logger.error("test print >>> ", ex);
		}
		
	}
	@Command
	public void printTextFile()
	{
		try
		{
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"invoices"+File.separator;
			String fileName=repository+"11"+File.separator+"25.pdf";
			logger.info(fileName);
			
			 // Desktop desktop = Desktop.getDesktop();
			 // desktop.print(new File(fileName)); 
			  
			//FileInputStream is;
			//is = new FileInputStream(fileName);
			InputStream is =new BufferedInputStream(new FileInputStream(fileName));
			
			// BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF8"));
			// String str = in.readLine();
			// logger.info(str);
			 
			
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
			System.out.println("Printer online: "+printService);
			
			DocPrintJob job = printService.createPrintJob();
			PrintJobWatcher watcher = new PrintJobWatcher(job);
			
			//DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			
			//DocFlavor psInFormat = new DocFlavor("text/plain; charset=utf-8", InputStream.class);
			//DocFlavor psInFormat = new DocFlavor("text/plain; charset=us-ascii", "java.io.InputStream");
			//psInFormat = new DocFlavor("text/plain; charset=utf-8", InputStream.class.getName());
			Doc mydoc = new SimpleDoc(is, flavor, null);
			
			//PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
		//	attributes.add(new Destination(new java.net.URI("file:C:/myfile.ps")));
			//job.print(mydoc, attributes);

			
			job.print(mydoc, null);
			 
			 // Wait for the print job to be done
			watcher.waitForDone();
           
			is.close();
			System.out.println("Printing Done!!");
			
			//JEditorPane text = new JEditorPane("file:///"+fileName);
		    
		    //text.print(null, null, false, printService, null, false);
		}
		catch(Exception ex)
		{
			logger.error("test print >>> ", ex);
		}
	}
	private void cutPaper()
	{
		try
		{
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		System.out.println("Printer cutter: "+printService);
		String GS = (char)29+"";
		 String ESC = (char)27+"";

		 String COMMAND = "";
		 COMMAND = ESC + "@";
		 COMMAND += GS + "V" + (char)1;		
		 
		//create a print job
		DocPrintJob job = printService.createPrintJob();
		
		//define the format of print document
		InputStream is = new ByteArrayInputStream(COMMAND.getBytes("UTF8"));
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		
		//print the data
		Doc doc = new SimpleDoc(is, flavor, null);
		
		PrintJobWatcher watcher = new PrintJobWatcher(job);
		
		
		job.print(doc, null);
		 
		 // Wait for the print job to be done
		watcher.waitForDone();
       
		is.close();
		System.out.println("Printing cutter Done!!");
		}
		catch(Exception ex)
		{
			logger.error("test print >>> ", ex);
		}
	}
	
	@Command
	public void payinvoice()
	{
		if(lstInvDetails.size()==0)
		{
			Messagebox.show("No books added !!.","Invoice", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		//payinvoice.zul
		//objCashInvoice.setInvoicetotalBeforeTax(objCashInvoice.getInvoicetotal());		
		if(objExhibtion.getTax()>0)
		{
			objCashInvoice.setTax(objCashInvoice.getInvoicetotalBeforeTax() * objExhibtion.getTax()/100);	
			objCashInvoice.setInvoicetotal(objCashInvoice.getInvoicetotalBeforeTax() + objCashInvoice.getTax());		
			objCashInvoice.setPaidamount(objCashInvoice.getInvoicetotal());
		}
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("objExhibtion",objExhibtion);		
		arg.put("objCashInvoice", objCashInvoice);
		arg.put("lstInvDetails", lstInvDetails);
		arg.put("discountType", discountType);
		arg.put("objCompanySetting", objCompanySetting);
		
		Executions.createComponents("/sales/payinvoice.zul", null,arg);
	}
	
	@Command
	public void updateInvoiceCommand()
	{
		try
		{			
			objCashInvoice.setPaidDollarAmount(objCashInvoice.getInvoicetotal() /objExhibtion.getCurrencyrate());			
			objCashInvoice.setInvoicestatus(1);
			objCashInvoice.setDiscounttype(Integer.parseInt(discountType));
			data.updateInvoice(objCashInvoice ,lstInvDetails);
			Messagebox.show("تم حفظ الفاتورة بنجاح" ,"فاتورة نقدية", Messagebox.OK , Messagebox.INFORMATION);
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceViewModel ----> updateInvoiceCommand", ex);			
		}
	}
	@Command
	public void printInvoiceCommand(@BindingParam("silentPrint") boolean silentPrint)
	{
		try
		{
			  PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
		      DocPrintJob printerJob = defaultPrintService.createPrintJob();
		      
		      //File pdfFile = new File("C:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\uploads\\invoices\\11\\244.pdf");
		    //  SimpleDoc simpleDoc = new SimpleDoc(pdfFile.toURL(), DocFlavor.URL.AUTOSENSE, null);
		      createPDFInvoiceFile();
		      
		      FileInputStream fis = new FileInputStream(printInvoicePath);
		      Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
		      printerJob.print(pdfDoc,new HashPrintRequestAttributeSet());
    		  fis.close();
		      
		      // printerJob.print(simpleDoc, null);
//		      for (PrintService printService : PrinterJob.lookupPrintServices()) {
//		    	  System.out.println("check printer name of service " + printService);
//		    	  if(printService.getName().contains("XPS")){
//		    		  DocPrintJob printerJob =printService.createPrintJob();
//		    		  printerJob.print(pdfDoc,new HashPrintRequestAttributeSet());
//		    		  fis.close();
//		    		  return;
//		    	  }
//		      }		      		   
		      
//			PrinterJob pjob = PrinterJob.getPrinterJob();
//			//String header=objExhibtion.getExhibitionname()+"\n" + objCompanySetting.getCompanyName() + " " + objCompanySetting.getJenahname()+"\n";			
//			pjob.setPrintable(new BillPrintable(objCompanySetting, objExhibtion,objCashInvoice,lstInvDetails),getPageFormat(pjob));
//
//	        for (PrintService printService : PrinterJob.lookupPrintServices()) {
//                System.out.println("check printer name of service " + printService);
//                if(printService.getName().contains("XPS")){
//                	pjob.setPrintService(printService);
//                	pjob.print();
//                	return;
//                }
//            }
			//pjob.print();

		}
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> printInvoiceCommand", ex);
		}
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
		
	private void createPDFInvoiceFile()
	{
		try
		{
			printInvoicePath="";
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"invoices"+File.separator;
			String fontrepository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"invoices"+File.separator;
			String dirPath=repository+objExhibtion.getExhibitionid();
			File dir = new File(dirPath);

			if(!dir.exists())
				dir.mkdirs();
			String invoicePath=dirPath +File.separator+ objCashInvoice.getInvoiceprefix()+".pdf";
			logger.info(invoicePath);
			
			BaseFont bfarabic = BaseFont.createFont(fontrepository +File.separator+"trado.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			//BaseFont bfarabic = BaseFont.createFont(fontrepository +File.separator+"trado.ttf", BaseFont.HELVETICA_BOLD, BaseFont.EMBEDDED);
			Font arfont = new Font(bfarabic, 16,Font.NORMAL);//12
			
			Document document = new Document(PageSize.A6, 0,0, 0, 0);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(invoicePath));
							//"C:/temp/invoicePDFWebApplication.pdf"));
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			document.open();
			document.newPage();

			Paragraph paragraph = new Paragraph();
			
			PdfPTable firsttbl = new PdfPTable(2);
			firsttbl.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			firsttbl.setWidthPercentage(100);
			firsttbl.getDefaultCell().setBorder(0);
			firsttbl.setWidths(new int[] { 150, 150 });			
			//Font f1 = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD,BaseColor.RED);
			//Font f1 = new Font(FontFamily.TIMES_ROMAN, 13.0f, Font.BOLD);
			
			//Chunk c = new Chunk(objExhibtion.getExhibitionname());
			//c.setUnderline(0.1f, -2f);
			//c.setFont(f1);
			//Paragraph p = new Paragraph(c);
			PdfPCell cellh=new PdfPCell(new Phrase(objExhibtion.getExhibitionname()+"\n" + objCompanySetting.getCompanyName() + " " + objCompanySetting.getJenahname()+"\n",arfont));
			cellh.setColspan(2);
			cellh.setBorder(0);
			cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellh.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			firsttbl.addCell(cellh);
			
			cellh=new PdfPCell(new Phrase("فاتورة نقدية"  , arfont));		
			cellh.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cellh.disableBorderSide(Rectangle.BOX);
			//c = new Chunk("Cash Invoice");
			//c.setUnderline(0.1f, -2f);
			//c.setFont(f1);
			//p = new Paragraph(c);
			//no need now
			//firsttbl.addCell(cellh);
			//sss
			//PdfPCell cell1 = new PdfPCell(new Phrase("Date :"+sdf.format(objCashInvoice.getInvoicedate())+"\n\n"+"Invoice Number :"+objCashInvoice.getInvoiceprefix()));
			//PdfPCell cell1 = new PdfPCell(new Phrase(sdtf.format(objCashInvoice.getCreationdate())+"\n\n"+"رقم الفاتورة :"+objCashInvoice.getInvoiceprefix(), arfont));
			//PdfPTable tbl1 = new PdfPTable(2);
			//tbl1.setWidthPercentage(100);
			/*PdfPCell cell1 = new PdfPCell(new Phrase(sdtf.format(objCashInvoice.getCreationdate())));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setColspan(1);
			cell1.disableBorderSide(Rectangle.BOX);
			firsttbl.addCell(cell1);*/
			document.add(firsttbl);
			
			PdfPTable tbl1 = new PdfPTable(2);
			tbl1.setWidthPercentage(100);
			tbl1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			PdfPCell cell1 = new PdfPCell(new Phrase(sdf.format(objCashInvoice.getCreationdate())));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setColspan(1);
			cell1.disableBorderSide(Rectangle.BOX);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase(sdtf.format(objCashInvoice.getCreationdate())));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setColspan(1);
			cell1.disableBorderSide(Rectangle.BOX);
			tbl1.addCell(cell1);
			
			document.add(tbl1);
						
			/*------------------------------------------------------------------------*/
			tbl1 = new PdfPTable(2);
			tbl1.setWidthPercentage(100);
			tbl1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//			cell1 = new PdfPCell(new Phrase("Invoice To ," , arfont));	 
//			//cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
//			cellh.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//			cell1.disableBorderSide(Rectangle.BOX);
//			cell1.setBorder(0);
//			tbl1.addCell(cell1);
			
			String customerName=objCashInvoice.getCustomername().equals("Cash")?"نقدي":objCashInvoice.getCustomername();
			cell1 = new PdfPCell(new Phrase("السادة : "+customerName, arfont));	
			cell1.setColspan(1);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase("رقم الفاتورة : "+objCashInvoice.getInvoiceprefix(), arfont));	
			cell1.setColspan(1);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			

			//add this font for arabic text
			//BaseFont bfarabic = BaseFont.createFont("c://temp//trado.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			//BaseFont bfarabic = BaseFont.createFont(dirPath +File.separator+"trado.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			//Font arfont = new Font(bfarabic, 12);
			
			/*cell1 = new PdfPCell(new Phrase(objCashInvoice.getCustomername(),arfont));
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell1.disableBorderSide(Rectangle.BOX);
			tbl1.addCell(cell1);
			document.add(tbl1);*/
			document.add(tbl1);
			/*---------------------------------------------------------------*/ 
			
			paragraph = new Paragraph();
			paragraph.setSpacingAfter(10);
			document.add(paragraph);
			BaseColor myColor = WebColors.getRGBColor("#FFFFFF");//("#8ECDFA");
			PdfPTable table = new PdfPTable(5);
			table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			
			///table.setWidths(new int[]{60,210,60,100,100});
			table.setWidths(new int[]{100,100,60,210,60});
			table.setSpacingBefore(20);
			table.setWidthPercentage(100);
			table.getDefaultCell().setPadding(5);

			PdfPCell HeadderProduct = new PdfPCell(new Phrase("سطر",arfont));	
			HeadderProduct.setPadding(1);
			HeadderProduct.setColspan(1);
			HeadderProduct.setBorder(Rectangle.NO_BORDER);
			HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
			HeadderProduct.setBackgroundColor(myColor);
			table.addCell(HeadderProduct);

			PdfPCell HeadderDate = new PdfPCell(new Phrase("اسم الكتاب", arfont));	
			HeadderDate.setPadding(1);
			HeadderDate.setColspan(1);
			HeadderDate.setBorder(Rectangle.NO_BORDER);
			//HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
			HeadderDate.setBackgroundColor(myColor);
			table.addCell(HeadderDate);

			PdfPCell HeadderQty = new PdfPCell(new Phrase("العدد", arfont));	
			HeadderQty.setPadding(1);
			HeadderQty.setColspan(1);
			HeadderQty.setBorder(Rectangle.NO_BORDER);
			HeadderQty.setHorizontalAlignment(Element.ALIGN_CENTER);
			HeadderQty.setBackgroundColor(myColor);
			HeadderQty.setBorderWidth(20.0f);
			table.addCell(HeadderQty);

			PdfPCell HeadderRate = new PdfPCell(new Phrase("السعر", arfont));	
			HeadderRate.setPadding(1);
			HeadderRate.setColspan(1);
			HeadderRate.setBorder(Rectangle.NO_BORDER);
			HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
			HeadderRate.setBackgroundColor(myColor);
			//HeadderRate.setBorderWidth(40.0f);
			table.addCell(HeadderRate);

			PdfPCell HeadderAmount = new PdfPCell(new Phrase("المجموع", arfont));	
			HeadderAmount.setPadding(1);
			HeadderAmount.setColspan(1);
			HeadderAmount.setBorder(Rectangle.NO_BORDER);
			HeadderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
			HeadderAmount.setBackgroundColor(myColor);
			//HeadderAmount.setBorderWidth(40.0f);
			table.addCell(HeadderAmount);
			
			Collections.sort(lstInvDetails, InvoiceDetailModel.invLineno);
			
			//for (int i = lstInvDetails.size()-1; i >-1 ; i--) 
			for (int i = 0; i <lstInvDetails.size() ; i++) 
			{
				InvoiceDetailModel item=lstInvDetails.get(i);
				table.addCell(new Phrase(""+item.getInvdline(),FontFactory.getFont(FontFactory.HELVETICA, 11)));
				
				PdfPCell cell=new PdfPCell(new Phrase(item.getBookname(),arfont));
				cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				//cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);
				
				table.addCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
				table.addCell(new Phrase(""+item.getDiscountprice(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
				table.addCell(new Phrase(""+(item.getQuantity()*item.getDiscountprice()) , FontFactory.getFont(FontFactory.HELVETICA, 11)));
			}
			
//			for (InvoiceDetailModel item : lstInvDetails) 
//			{
//				table.addCell(new Phrase(""+item.getInvdline(),FontFactory.getFont(FontFactory.HELVETICA, 11)));
//				
//				PdfPCell cell=new PdfPCell(new Phrase(item.getBookname(),arfont));
//				cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//				//cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table.addCell(cell);
//				
//				table.addCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
//				table.addCell(new Phrase(""+item.getDiscountprice(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
//				table.addCell(new Phrase(""+(item.getQuantity()*item.getDiscountprice()) , FontFactory.getFont(FontFactory.HELVETICA, 11)));
//			}
			
			for(PdfPRow r: table.getRows()) {
				for(PdfPCell c1: r.getCells()) {
					c1.setBorder(Rectangle.NO_BORDER);
				}
			}
			
			document.add(table);
			document.add( Chunk.NEWLINE );
			document.add( Chunk.NEWLINE );
			
			
			PdfPTable totaltbl = new PdfPTable(2);
			totaltbl.setWidthPercentage(100);
			totaltbl.getDefaultCell().setBorder(0);
			totaltbl.setWidths(new int[]{400,350});
			totaltbl.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			
			String amtStr1 = BigDecimal.valueOf(objCashInvoice.getInvoiceamount()).toPlainString();
			double amtDbbl1 = Double.parseDouble(amtStr1);			
			cell1 = new PdfPCell(new Phrase("المجموع :"
					+ decimalformatter.format(amtDbbl1) + objExhibtion.getCurrencysymbol(), arfont));	
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBackgroundColor(myColor);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			totaltbl.addCell(cell1);					
			 
			amtStr1 = BigDecimal.valueOf(objCashInvoice.getInvoicediscount()).toPlainString();
			amtDbbl1 = Double.parseDouble(amtStr1);	
			cell1 = new PdfPCell(new Phrase("الحسم :"
					+ decimalformatter.format(amtDbbl1)+ objExhibtion.getCurrencysymbol(), arfont));	
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBackgroundColor(myColor);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			totaltbl.addCell(cell1);
			
			if(objExhibtion.getTax()>0){
				//objCashInvoice.setTax(objCashInvoice.getInvoicetotalBeforeTax() * objExhibtion.getTax()/100);	
				//objCashInvoice.setInvoicetotal(objCashInvoice.getInvoicetotalBeforeTax() + objCashInvoice.getTax())
				amtStr1 = BigDecimal.valueOf(objCashInvoice.getTax()).toPlainString();
				
				amtDbbl1 = Double.parseDouble(amtStr1);					
				cell1 = new PdfPCell(new Phrase(" الضريبة :"
						+ decimalformatter.format(amtDbbl1)+ objExhibtion.getCurrencysymbol(), arfont));	
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				cell1.setBackgroundColor(myColor);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				totaltbl.addCell(cell1);
			}
			
			amtStr1 = BigDecimal.valueOf(objCashInvoice.getInvoicetotal()).toPlainString();
			amtDbbl1 = Double.parseDouble(amtStr1);	
			//cell1 = new PdfPCell(new Phrase("المجموع بعد الحسم :"
			cell1 = new PdfPCell(new Phrase(" بعد الحسم :"
					+ decimalformatter.format(amtDbbl1)+ objExhibtion.getCurrencysymbol(), arfont));	
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setBackgroundColor(myColor);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			totaltbl.addCell(cell1);
			
			amtStr1 = BigDecimal.valueOf(objCashInvoice.getPaidamount()).toPlainString();
			amtDbbl1 = Double.parseDouble(amtStr1);	
			cell1 = new PdfPCell(new Phrase("الدفع :"
					+ decimalformatter.format(amtDbbl1)+ objExhibtion.getCurrencysymbol(), arfont));	
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setBackgroundColor(myColor);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			if(objExhibtion.getTax()>0)
			cell1.setColspan(2);
			totaltbl.addCell(cell1);
		
			if(objExhibtion.getTax()>0){
				
			}
			
			
			cell1 = new PdfPCell(new Phrase("Amount in word: "
					+ numbToWord.GetFigToWord(objCashInvoice.getPaidamount()), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBackgroundColor(myColor);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setColspan(2);
			totaltbl.addCell(cell1);

			document.add(totaltbl);
			
			/*String amtStr1 = BigDecimal.valueOf(objCashInvoice.getPaidamount())
					.toPlainString();
			double amtDbbl1 = Double.parseDouble(amtStr1);
			cell1 = new PdfPCell(new Phrase("Total :"
					+ decimalformatter.format(amtDbbl1), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setBackgroundColor(myColor);
			totaltbl.addCell(cell1);
			document.add(totaltbl);*/
			
			
			firsttbl = new PdfPTable(1);
			firsttbl.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			firsttbl.setWidthPercentage(100);
			firsttbl.getDefaultCell().setBorder(0);
			//firsttbl.setWidths(new int[] { 150, 150 });	
			
			//cellh=new PdfPCell(new Phrase(invoiceFooter,arfont));
			cellh=new PdfPCell(new Phrase(objCompanySetting.getInvoicefooter(),arfont));
			
			//cellh.setColspan(1);
			cellh.setBorder(0);
			cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellh.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			firsttbl.addCell(cellh);
			document.add(firsttbl);
			
//			paragraph=new Paragraph();
//			//"Product or Service once Sold/Provided can not be Refunded"
//			Chunk chunk = new Chunk(invoiceFooter, arfont);
//			paragraph.add(chunk);
//			paragraph.setAlignment(Element.ALIGN_RIGHT);			
//			document.add(paragraph);
			
			document.close();
			printInvoicePath=invoicePath;
			
			
			//InputStream is =new BufferedInputStream(new FileInputStream(printInvoicePath));
			/*FileInputStream fis = new FileInputStream(printInvoicePath);
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
			DocPrintJob job = printService.createPrintJob();
			Doc doc = new SimpleDoc(fis, flavor, null);
			PrintJobWatcher watcher = new PrintJobWatcher(job);
			job.print(doc, null);
			watcher.waitForDone();
			fis.close();
			System.out.println("Printing Done!!");*/
			
			//printSilentInvoiceCommand(invoicePath);
			
			//stop here
			//if(!silentPrint)
			//previewPdfForprintingInvoice(invoicePath);
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> printInvoiceCommand", ex);
		}
	}
	
	@Command
	public void printPDFInvoiceCommand(@BindingParam("silentPrint") boolean silentPrint)
	{
		try
		{
			createPDFInvoiceFile();
			previewPdfForprintingInvoice(printInvoicePath);
		}
		catch(Exception ex)
		{
			logger.error("ERROR in CashInvoiceViewModel ----> printInvoiceCommand", ex);
		}
		
	}
	public void previewPdfForprintingInvoice(String invoicePath)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("invoicePath", invoicePath);
			Executions.createComponents("/sales/invoicePdfView.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceViewModel ----> previewPdfForprintingInvoice", ex);			
		}
	}
	@Command
	 public void printSilentInvoiceCommand(String filePath)
	  {
		  try
		  {
			
			  printInvoiceCommand(true);
			  Executable ex = new Executable();
			  //ex.openDocument(filePath);
			
			  ex.printDocumentSilent(printInvoicePath);
		  }
		  catch (Exception ex)
			{	
				logger.error("ERROR in CashInvoiceViewModel ----> printPDF", ex);			
			}
	  }
	
	@Command
	public void exportInvoiceItemsToExcel()
	{
		try
		{
		 if(lstInvDetails==null)
		  {
	  		Messagebox.show("There is no record !!","Invoice List", Messagebox.OK , Messagebox.EXCLAMATION);
	  		return;
		  }
		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 String[] tsHeaders;
		 tsHeaders = new String[]{"السطر", "كود الكتاب", "إسم الكتاب" ,"العدد", "السعر","السعر بعد الحسم" , "الإجمالي"};
		 final String[] headers=tsHeaders;
		 
		 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
		     
			    @Override
			    public void beforeRendering(XSSFWorkbook target) {
			        ExportContext context = exporter.getExportContext();
			         
			        for (String header : headers) {
			            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
			            cell.setCellValue(header);
			             				           
			                CellStyle srcStyle = cell.getCellStyle();
			                if (srcStyle.getAlignment() != CellStyle.ALIGN_CENTER) {				                   
								XSSFCellStyle newCellStyle = target.createCellStyle();
			                    newCellStyle.cloneStyleFrom(srcStyle);
			                    newCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			                    cell.setCellStyle(newCellStyle);
			                }
			            
			        }
			    }
			    
			    @Override
			    public void afterRendering(XSSFWorkbook target) {
			    }				    				   
			});
		 
			exporter.export(headers.length, lstInvDetails, new RowRenderer<Row, InvoiceDetailModel>() {
				@Override
				public void render(Row table, InvoiceDetailModel item, boolean isOddRow) 
					 {
					 	ExportContext context = exporter.getExportContext();
				        XSSFSheet sheet = context.getSheet();				        
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvdline());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookcode());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookname());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getQuantity());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getPrice());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDiscountprice());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getQuantity()*item.getDiscountprice());				       
					 }
					 
			    }, out);
			 
			   	AMedia amedia = new AMedia("InvoiceItemList.xls", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
			 
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in CashInvoiceViewModel---exportInvoiceItemsToExcel-->" , ex);			
		}
	}
	
	public InvoiceModel getObjCashInvoice() {
		return objCashInvoice;
	}

	public void setObjCashInvoice(InvoiceModel objCashInvoice) 
	{
		this.objCashInvoice = objCashInvoice;
	}

	
	public Users getObjUser() {
		return objUser;
	}

	public void setObjUser(Users objUser) {
		this.objUser = objUser;
	}

	public ExhibitionsModel getObjExhibtion() {
		return objExhibtion;
	}

	public void setObjExhibtion(ExhibitionsModel objExhibtion) {
		this.objExhibtion = objExhibtion;
	}

	public List<BooksModel> getLstBooks() {
		return lstBooks;
	}

	public void setLstBooks(List<BooksModel> lstBooks) {
		this.lstBooks = lstBooks;
	}


	public List<InvoiceDetailModel> getLstInvDetails() {
		return lstInvDetails;
	}


	public void setLstInvDetails(List<InvoiceDetailModel> lstInvDetails) {
		this.lstInvDetails = lstInvDetails;
	}


	public InvoiceDetailModel getSelectedInvDetail() {
		return selectedInvDetail;
	}


	public void setSelectedInvDetail(InvoiceDetailModel selectedInvDetail) {
		this.selectedInvDetail = selectedInvDetail;
	}


	public String getBookName() 
	{
		return bookName;
	}

	@NotifyChange({"lstInvDetails","objCashInvoice","bookName","bookFocus"})
	public void setBookName(String bookName) 
	{
		logger.info(bookName);
		this.bookName = bookName;
		//findBookBarCode();
		//this.bookName="";
		//focusNewLine="false";
		//bookFocus=false;
	}
	private void findBookBarCode()
	{
		for (Iterator<BooksModel> i = lstBooks.iterator(); i.hasNext();)
		{
			BooksModel tmp=i.next();				
			if(tmp.getBookCode().equals(bookName))					
			{
				refreshBooksParent(tmp,objExhibtion,null);
			}
		}
	}
	
	@Command
	@NotifyChange({"lstInvDetails","objCashInvoice","bookName","bookFocus"})
	public void onOKBookName()
	{
		boolean bookFound=false;
		for (Iterator<BooksModel> i = lstBooks.iterator(); i.hasNext();)
		{
			BooksModel tmp=i.next();				
			if(tmp.getBookCode().equals(bookName))					
			{
				bookFound=true;
				refreshBooksParent(tmp,objExhibtion,null);
				break;
			}
		}
		bookName="";
		bookFocus=true;
		if(bookFound==false)
		{
			Messagebox.show("رمز الكتاب غير موجود" ,"فاتورة نقدية", Messagebox.OK , Messagebox.INFORMATION);
		}
			
		
	}

	public List<InvoiceModel> getLstOnHoldInvoices() {
		return lstOnHoldInvoices;
	}

	public void setLstOnHoldInvoices(List<InvoiceModel> lstOnHoldInvoices) {
		this.lstOnHoldInvoices = lstOnHoldInvoices;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Users getObjChangeUser() {
		return objChangeUser;
	}

	public void setObjChangeUser(Users objChangeUser) {
		this.objChangeUser = objChangeUser;
	}

	public List<InvoiceModel> getLstInvoices() {
		return lstInvoices;
	}

	public void setLstInvoices(List<InvoiceModel> lstInvoices) {
		this.lstInvoices = lstInvoices;
	}

	public List<ExhibitionsModel> getLstExhibitions() {
		return lstExhibitions;
	}

	public void setLstExhibitions(List<ExhibitionsModel> lstExhibitions) {
		this.lstExhibitions = lstExhibitions;
	}

	public ExhibitionsModel getSelectedExhibitions() {
		return selectedExhibitions;
	}

	public void setSelectedExhibitions(ExhibitionsModel selectedExhibitions) {
		this.selectedExhibitions = selectedExhibitions;
	}

	public InvoiceModel getObjReturnInvoice() {
		return objReturnInvoice;
	}

	public void setObjReturnInvoice(InvoiceModel objReturnInvoice) {
		this.objReturnInvoice = objReturnInvoice;
	}

	public String getFocusNewLine() {
		return focusNewLine;
	}

	public void setFocusNewLine(String focusNewLine) {
		this.focusNewLine = focusNewLine;
	}

	public boolean isBookFocus() {
		return bookFocus;
	}

	public void setBookFocus(boolean bookFocus) {
		this.bookFocus = bookFocus;
	}

	public boolean isQuantityFocus() {
		return quantityFocus;
	}

	public void setQuantityFocus(boolean quantityFocus) {
		this.quantityFocus = quantityFocus;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public double getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public void setTotalPaidAmount(double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public double getTotalPaidDollarAmount() {
		return totalPaidDollarAmount;
	}

	public void setTotalPaidDollarAmount(double totalPaidDollarAmount) {
		this.totalPaidDollarAmount = totalPaidDollarAmount;
	}

	public double getTotalInvoiceListAmount() {
		return totalInvoiceListAmount;
	}

	public void setTotalInvoiceListAmount(double totalInvoiceListAmount) {
		this.totalInvoiceListAmount = totalInvoiceListAmount;
	}
	
	
}
