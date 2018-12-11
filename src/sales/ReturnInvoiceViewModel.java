package sales;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import domains.BooksModel;
import domains.CustomerModel;
import domains.ExhibitionsModel;
import domains.InvoiceDetailModel;
import domains.InvoiceModel;
import domains.Users;

public class ReturnInvoiceViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat decimalformatter = new DecimalFormat("#,###.00");
	String viewType;
	private Users objUser;
	private Users objChangeUser;
	private InvoiceModel objReturnInvoice;
	private ExhibitionsModel objExhibtion;
	CashInvoiceData data=new CashInvoiceData();
	private String discountType;
	private List<InvoiceDetailModel> lstReturnInvDetails;
	private InvoiceDetailModel selectedInvDetail;
	private String bookName;
	private List<BooksModel> lstBooks;
	private String invoicePrefix;
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{		
		Session sess = Sessions.getCurrent();		
		objUser=(Users) sess.getAttribute("Authentication");
			
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();			
		viewType=type==null?"":type;
		
		if(viewType.equals("returninvoice"))			
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
			invoicePrefix=objReturnInvoice.getInvoiceprefix();
			bookName="";
			lstBooks=data.getExhibtionBooksList(objExhibtion.getExhibitionid());
			
			lstReturnInvDetails=new ArrayList<InvoiceDetailModel>();
		}				
		
		}
		
		catch (Exception ex) 
		{
			  logger.error("error in ReturnInvoiceViewModel---init-->" , ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstReturnInvDetails","objReturnInvoice"})
	public void findInvoiceCommand()
	{
		try
		{
			InvoiceModel obj=data.getInvoiceByInvoicePrefix(invoicePrefix,objExhibtion.getExhibitionid());
			if(obj.getInvoiceid()==0)				
			{
				Messagebox.show("Invalid invoice number !!.","Return Invoice", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			else
			{
				objReturnInvoice=obj;
				lstReturnInvDetails=data.getInvoicesDetailsList(objReturnInvoice.getInvoiceid());
			}
			
		}
		catch(Exception ex)
		{
			logger.error("ERROR in ReturnInvoiceViewModel ----> findInvoiceCommand", ex);
		}
	}
	
	@Command
	public void openCustomersCommand()
	{		
		try
		{		
		Map<String,Object> arg = new HashMap<String,Object>();		
		Executions.createComponents("/sales/customerpopup.zul", null,arg);		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in ReturnInvoiceViewModel ----> openCustomersCommand", ex);
		}
	}
	@GlobalCommand 
	@NotifyChange({"objReturnInvoice"})
	public void refreshCustomerParent(@BindingParam("selectedCustomer")CustomerModel selectedCustomer)
	{
		objReturnInvoice.setCustomerid(selectedCustomer.getCustomerid());
		objReturnInvoice.setCustomername(selectedCustomer.getCustomername());
		objReturnInvoice.setCustomerDiscount(selectedCustomer.getSpecial());		
	}
	
	@Command
	public void openBooksCommand()
	{		
		try
		{		
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("lstBooks",lstBooks);
		arg.put("bookName",bookName);	
		arg.put("type","return");	
		arg.put("objExhibtion", objExhibtion);
		Executions.createComponents("/sales/bookspopup.zul", null,arg);		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in ReturnInvoiceViewModel ----> openBooksCommand", ex);
		}
	}
	@GlobalCommand 
	@NotifyChange({"lstReturnInvDetails","objReturnInvoice"})
	public void refreshReturnBooksParent(@BindingParam("selectedBook")BooksModel selectedBook,@BindingParam("objExhibtion")ExhibitionsModel objExhibtion,
			@BindingParam("lstSelectedBooks") List<BooksModel> lstSelectedBooks)
	{
		//check if same book added
		if(lstSelectedBooks!=null)
		{			
			for (BooksModel book : lstSelectedBooks) 
			{				
				boolean isbookFound=false;
				for (InvoiceDetailModel item : lstReturnInvDetails)
				{					
					if(item.getBookid()==book.getBookid())
					{
						item.setQuantity(item.getQuantity()+1);
						item.setRemainquantity(book.getRemainquantity() +1 );
						book.setRemainquantity(book.getRemainquantity() +1);	
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
		for (InvoiceDetailModel item : lstReturnInvDetails)
		{
			if(item.getBookid()==selectedBook.getBookid())
			{
				item.setQuantity(item.getQuantity()+1);
				item.setRemainquantity(selectedBook.getRemainquantity() +1 );
				selectedBook.setRemainquantity(selectedBook.getRemainquantity() +1);
				getTotalInvoice(null);
				return;
			}
		}
		addBooksToList(selectedBook);
		
		getTotalInvoice(null);
		}
	}
	
	private void addBooksToList(BooksModel selectedBook)
	{
		InvoiceDetailModel objInvDetail=new InvoiceDetailModel();
		objInvDetail.setInvdline(lstReturnInvDetails.size()+1);
		objInvDetail.setBookid(selectedBook.getBookid());
		objInvDetail.setBookcode(selectedBook.getBookCode());
		objInvDetail.setBookname(selectedBook.getBookName());
		objInvDetail.setQuantity(1);
		objInvDetail.setPrice(selectedBook.getBookPrice());//selectedBook.getLocalPrice());
		objInvDetail.setDiscountprice(selectedBook.getLocalPrice());//objInvDetail.getPrice());
		
		
		//objInvDetail.setPrice(selectedBook.getLocalPrice());
		//objInvDetail.setDiscountprice(objInvDetail.getPrice());
		objInvDetail.setRemainquantity(selectedBook.getRemainquantity() +1 );
		objInvDetail.setMainquantity(selectedBook.getRemainquantity());
		selectedBook.setRemainquantity(selectedBook.getRemainquantity() +1);
		
		if(objReturnInvoice.getCustomerDiscount()>0)
		{
			double disc=objReturnInvoice.getCustomerDiscount()/100;
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
		
		objInvDetail.setTotalprice(objInvDetail.getDiscountprice());
		lstReturnInvDetails.add(objInvDetail);
	}
	
	@Command
	@NotifyChange({"objReturnInvoice","lstReturnInvDetails"})
	public void deleteBookCommand(@BindingParam("todo") InvoiceDetailModel todo)
	{
		findBookByID(todo.getBookid(),todo.getMainquantity());	
		lstReturnInvDetails.remove(todo);
		getTotalInvoice(null);
	}
	
	@Command
	@NotifyChange({"objReturnInvoice","lstReturnInvDetails"})
	public void getTotalInvoice(@BindingParam("row") InvoiceDetailModel row)
	{
		double invoiceamount=0;
		double invoicediscount=0;
		double invoicetotal=0;
		double paidamount=0;
		
		for (InvoiceDetailModel item : lstReturnInvDetails) 
		{
			invoiceamount+=item.getDiscountprice() * item.getQuantity(); //item.getTotalprice();
		}
		
		objReturnInvoice.setInvoiceamount(invoiceamount);
		//objCashInvoice.setInvoicediscount(0);
		
		invoicetotal= invoiceamount - objReturnInvoice.getInvoicediscount();
		if(invoicetotal>0)
			objReturnInvoice.setInvoicetotal(invoicetotal);
		else
			objReturnInvoice.setInvoicetotal(0);
		
		objReturnInvoice.setPaidamount(objReturnInvoice.getInvoicetotal());
		
		if(row!=null)
		{
			row.setRemainquantity(row.getMainquantity() + row.getQuantity());	
			findBookByID(row.getBookid(),row.getMainquantity() + row.getQuantity());			
		}
	}
	
	private void findBookByID(int bookId ,int quantity)
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
	
	@Command
	@NotifyChange({"objReturnInvoice","lstReturnInvDetails"})
	public void cashPayCommand()
	{
		try
		{
			logger.info(objReturnInvoice.getDiscounttype());
			//to not make duplicate when return so we make new one in all cases
			objReturnInvoice.setInvoiceid(0);
			objReturnInvoice.setInvoicenumber(data.getMaxInvoiceNumber(objExhibtion.getExhibitionid()));		
			objReturnInvoice.setInvoiceprefix(objExhibtion.getPrefix() + objReturnInvoice.getInvoicenumber());	
			
			//objReturnInvoice.setInvoiceprefix(invoicePrefix);
			objReturnInvoice.setInvoicestatus(4);
			objReturnInvoice.setDiscounttype(Integer.parseInt(discountType));
			data.addNewInvoice(objReturnInvoice ,lstReturnInvDetails);
			Messagebox.show("Done");
			
			cancelCommand();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in ReturnInvoiceViewModel ----> cashPayCommand", ex);
		}
	}
	
	@GlobalCommand 
	@Command
	@NotifyChange({"objReturnInvoice","lstReturnInvDetails","invoicePrefix"})
	public void cancelCommand()
	{
		try
		{
		Calendar c = Calendar.getInstance();
		objReturnInvoice=new InvoiceModel();
		objReturnInvoice.setExhibitionid(objExhibtion.getExhibitionid());	
		discountType="1";
		objReturnInvoice.setDiscounttype(1);
		objReturnInvoice.setInvoicedate(df.parse(sdf.format(c.getTime())));
		if(objUser!=null)
			objReturnInvoice.setCreatedby(objUser.getFirstname());
		objReturnInvoice.setInvoicenumber(data.getMaxInvoiceNumber(objExhibtion.getExhibitionid()));		
		objReturnInvoice.setInvoiceprefix(objExhibtion.getPrefix() + objReturnInvoice.getInvoicenumber());		
		objReturnInvoice.setCustomername("Cash");
		objReturnInvoice.setDateofpay("");
		objReturnInvoice.setNotes("");
		invoicePrefix=objReturnInvoice.getInvoiceprefix();
		//bookName="";
		//lstBooks=data.getExhibtionBooksList(objExhibtion.getExhibitionid());
		
		lstReturnInvDetails=new ArrayList<InvoiceDetailModel>();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in ReturnInvoiceViewModel ----> cancelCommand", ex);
		}
	}
	
	@Command
	public void changeUserCommand()
	{
		//changeuser.zul
		Map<String,Object> arg = new HashMap<String,Object>();	
		arg.put("objReturnInvoice",objReturnInvoice);
		arg.put("type", "return");
		//arg.put("lstInvDetails",lstInvDetails);
		//arg.put("objExhibtion",objExhibtion);		
		Executions.createComponents("/sales/changeuser.zul", null,arg);
	}
			
	@GlobalCommand 
	@NotifyChange({"objReturnInvoice"})
	public void refreshReturnUserParent(@BindingParam("objChangeUser")Users objChangeUser)
	{
		try
		{
			logger.info(viewType + " " + objChangeUser.getFirstname());			
			objReturnInvoice.setCreatedby(objChangeUser.getFirstname());			
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in ReturnInvoiceViewModel ----> refreshUserParent", ex);
		}
	}
	
	@Command
	//@NotifyChange({"lstInvDetails","objCashInvoice","bookName","bookFocus"})
	@NotifyChange({"lstReturnInvDetails","objReturnInvoice","bookName"})
	public void onOKBookName()
	{		
		for (Iterator<BooksModel> i = lstBooks.iterator(); i.hasNext();)
		{
			BooksModel tmp=i.next();				
			if(tmp.getBookCode().equals(bookName))					
			{
				refreshReturnBooksParent(tmp,objExhibtion,null);				
				break;
			}
		}
		bookName="";		
		
	}
	

	public InvoiceModel getObjReturnInvoice() {
		return objReturnInvoice;
	}

	public void setObjReturnInvoice(InvoiceModel objReturnInvoice) {
		this.objReturnInvoice = objReturnInvoice;
	}

	public ExhibitionsModel getObjExhibtion() {
		return objExhibtion;
	}

	public void setObjExhibtion(ExhibitionsModel objExhibtion) {
		this.objExhibtion = objExhibtion;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public List<InvoiceDetailModel> getLstReturnInvDetails() {
		return lstReturnInvDetails;
	}

	public void setLstReturnInvDetails(List<InvoiceDetailModel> lstInvDetails) {
		this.lstReturnInvDetails = lstInvDetails;
	}

	public InvoiceDetailModel getSelectedInvDetail() {
		return selectedInvDetail;
	}

	public void setSelectedInvDetail(InvoiceDetailModel selectedInvDetail) {
		this.selectedInvDetail = selectedInvDetail;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public List<BooksModel> getLstBooks() {
		return lstBooks;
	}

	public void setLstBooks(List<BooksModel> lstBooks) {
		this.lstBooks = lstBooks;
	}

	public Users getObjChangeUser() {
		return objChangeUser;
	}

	public void setObjChangeUser(Users objChangeUser) {
		this.objChangeUser = objChangeUser;
	}

	public String getInvoicePrefix() {
		return invoicePrefix;
	}

	public void setInvoicePrefix(String invoicePrefix) {
		this.invoicePrefix = invoicePrefix;
	}
}
