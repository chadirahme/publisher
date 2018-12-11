package sales;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import db.DBHandler;
import domains.BooksModel;
import domains.CompanySettingModel;
import domains.ExhibitionsModel;
import domains.ImportbookModel;
import domains.InvoiceDetailModel;
import domains.InvoiceModel;

public class CashInvoiceData 
{

	private Logger logger = Logger.getLogger(this.getClass());
	CashInvoiceQueries query=new CashInvoiceQueries();
	
	public ExhibitionsModel getActiveExhibtions()
	{
		ExhibitionsModel obj=new ExhibitionsModel();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getActiveExhibtionQuery());
			 while(rs.next())
			 {				
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setExhibitionname(rs.getString("exhibitionname"));
				 obj.setCurrencyid(rs.getInt("currencyid"));
				 obj.setCurrencyrate(rs.getDouble("currencyrate"));
				 obj.setDiscountaudience(rs.getDouble("discountaudience"));
				 obj.setDiscounttotal(rs.getDouble("discounttotal"));
				 obj.setPrefix(rs.getString("prefix"));
				 obj.setSerialnumber(rs.getInt("serialnumber"));
				 obj.setCountryid(rs.getInt("countryid"));
				 obj.setCityid(rs.getInt("cityid"));
				 obj.setFromdate(rs.getDate("fromdate"));
				 obj.setTodate(rs.getDate("todate"));
				 obj.setCurrencysymbol(rs.getString("currencysymbol"));
				 obj.setTax(rs.getDouble("tax"));				 				
			 }
		}
		catch (Exception ex) 
		{
			  logger.error("error in CashInvoiceData---getActiveExhibtions-->" , ex);			
		}
		return obj;
	}
	
	public CompanySettingModel getCompanySetting()
	{
		CompanySettingModel obj=new CompanySettingModel();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery("Select * from companysetting ");
			 while(rs.next())
			 {				 
				 obj.setCompanysettingid(rs.getInt("companysettingid"));
				 obj.setCompanyName(rs.getString("companyName"));
				 obj.setSecondline(rs.getString("secondline"));
				obj.setPrintcount(rs.getInt("printcount"));
				obj.setInvoicefooter(rs.getString("invoicefooter"));
				obj.setLogopath(rs.getString("logopath"));
				obj.setJenahname(rs.getString("jenahname"));				
			 }
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---getCompanySetting-->" , ex);			
		}
		return obj;
	}
	
	public int getMaxInvoiceNumber(int exhibitionid)
	{
		int maxInvNo=0;
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getMaxInvoiceNumberQuery(exhibitionid));
			 while(rs.next())
			 {				
				maxInvNo=rs.getInt("invno");
			 }			
			 maxInvNo=maxInvNo+1;
		}
		catch (Exception ex) 
		{
			  logger.error("error in CashInvoiceData---getMaxInvoiceNumber-->" , ex);			
		}
		return maxInvNo;
	}
	
	
	public List<BooksModel> getExhibtionBooksList(int exhibitionid)
	{
		List<BooksModel> lst=new ArrayList<BooksModel>();
		try
		{			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getExhibtionBooksListQuery(exhibitionid));
			 while(rs.next())
			 {
				 BooksModel obj=new BooksModel();
				 obj.setBookid(rs.getInt("bookid"));
				 obj.setBookCode(rs.getString("bookcode")==null?"":rs.getString("bookcode"));
				 obj.setBookName(rs.getString("bookname")==null?"":rs.getString("bookname"));
				 obj.setEditor(rs.getString("editor")==null?"":rs.getString("editor"));
				 obj.setAuthor(rs.getString("author")==null?"":rs.getString("author"));
				 obj.setPrice(rs.getString("price"));
				 obj.setBookPrice(rs.getDouble("price"));
				 obj.setQuantity(rs.getString("quantity"));
				 obj.setRemainquantity(rs.getInt("remainquantity"));		
				 obj.setLocalPrice(rs.getDouble("localprice"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---getExhibtionBooksList-->" , ex);			
		}
		return lst;
	}
	
	public void addNewInvoice(InvoiceModel obj , List<InvoiceDetailModel> lstInvDetails)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 if(obj.getInvoiceid()==0)
			 {
			 int invoiceid= db.executeUpdateQuery(query.addNewInvoiceQuery(obj));
			 for (InvoiceDetailModel item : lstInvDetails) 
			 {
				 item.setInvoiceid(invoiceid);
				 double totalPrice=item.getQuantity()*item.getDiscountprice();
				 item.setTotalprice(totalPrice);
				 db.executeUpdateQuery(query.addNewInvoiceDetailQuery(item));				 
			 }
			 }
			 else
			 {
				 db.executeUpdateQuery(query.updateInvoiceQuery(obj)); 
				 db.executeUpdateQuery(query.deleteInvoiceDetailsQuery(obj.getInvoiceid()));
				 for (InvoiceDetailModel item : lstInvDetails) 
				 {
					 item.setInvoiceid(obj.getInvoiceid());
					 double totalPrice=item.getQuantity()*item.getDiscountprice();
					 item.setTotalprice(totalPrice);
					 db.executeUpdateQuery(query.addNewInvoiceDetailQuery(item));				 
				 }
			 }
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---addNewInvoice-->" , ex);			
		}
	}
	
	public void updateInvoice(InvoiceModel obj , List<InvoiceDetailModel> lstInvDetails)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 if(obj.getInvoiceid()>0)
			 {
			
				 db.executeUpdateQuery(query.updateInvoiceQuery(obj)); 
				 db.executeUpdateQuery(query.deleteInvoiceDetailsQuery(obj.getInvoiceid()));
				 for (InvoiceDetailModel item : lstInvDetails) 
				 {
					 item.setInvoiceid(obj.getInvoiceid());
					 double totalPrice=item.getQuantity()*item.getDiscountprice();
					 item.setTotalprice(totalPrice);
					 db.executeUpdateQuery(query.addNewInvoiceDetailQuery(item));				 
				 }
			 }			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---updateInvoice-->" , ex);			
		}
	}
	
	public void deleteOnHoldInvoice(InvoiceModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 if(obj.getInvoiceid()>0)
			 {			
				 db.executeUpdateQuery(query.deleteInvoiceDetailsQuery(obj.getInvoiceid()));	
				 db.executeUpdateQuery(query.deleteOnHoldInvoiceQuery(obj.getInvoiceid())); 				 			
			 }			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---deleteOnHoldInvoice-->" , ex);			
		}
	}
	
	public List<InvoiceModel> getInvoicesListByStatus(int exhibitionid,int statusId , Date fromDate,Date toDate)
	{
		List<InvoiceModel> lst=new ArrayList<InvoiceModel>();
		try
		{			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getInvoicesListByStatusQuery(exhibitionid,statusId,fromDate,toDate));
			 while(rs.next())
			 {
				 InvoiceModel obj=new InvoiceModel();
				 obj.setInvoiceid(rs.getInt("invoiceid"));
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setInvoicenumber(rs.getInt("invoicenumber"));
				 obj.setInvoiceprefix(rs.getString("invoiceprefix"));
				 obj.setInvoicedate(rs.getDate("invoicedate"));
				 obj.setCustomerid(rs.getInt("customerid"));
				 obj.setCustomername(rs.getString("customername"));
				 obj.setDiscounttype(rs.getInt("discounttype"));
				 obj.setInvoiceamount(rs.getDouble("invoiceamount"));
				 obj.setInvoicediscount(rs.getDouble("invoicediscount"));
				 obj.setInvoicetotal(rs.getDouble("invoicetotal"));
				 obj.setPaidamount(rs.getDouble("paidamount"));
				 obj.setInvoicestatus(rs.getInt("invoicestatus"));
				 obj.setBalanceamount(rs.getDouble("balanceamount"));
				 obj.setDateofpay(rs.getString("dateofpay"));
				 obj.setNotes(rs.getString("notes"));
				 obj.setCreatedby(rs.getString("createdby"));
				 obj.setCreationdate(rs.getDate("creationdate"));
				 obj.setStatusName(rs.getString("statusname"));
				 obj.setInvoicediscountPerc(rs.getDouble("invoicediscountPerc"));
				 obj.setPaidIndollar(rs.getInt("paidindollar"));
				 obj.setPaidDollarAmount(rs.getDouble("paiddollaramount"));
				 
				 if(obj.getInvoicestatus()==4)//return
				 {
					 obj.setInvoiceamount(rs.getDouble("invoiceamount")*-1);
					 obj.setInvoicetotal(rs.getDouble("invoicetotal")*-1);
					 obj.setPaidamount(rs.getDouble("paidamount")*-1);
					 obj.setBalanceamount(rs.getDouble("balanceamount")*-1);
				 }
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---getInvoicesListByStatus-->" , ex);			
		}
		return lst;
	}
	
	public List<InvoiceDetailModel> getInvoicesDetailsList(int invoiceid)
	{
		List<InvoiceDetailModel> lst=new ArrayList<InvoiceDetailModel>();
		try
		{			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getInvoicesDetailsListQuery(invoiceid));
			 while(rs.next())
			 {
				 InvoiceDetailModel obj=new InvoiceDetailModel();
				 obj.setInvoicedetailid(rs.getInt("invoicedetailid"));
				 obj.setInvoiceid(rs.getInt("invoiceid"));
				 obj.setInvdline(rs.getInt("invdline"));
				 obj.setBookid(rs.getInt("bookid"));
				 obj.setBookcode(rs.getString("bookcode"));
				 obj.setBookname(rs.getString("bookname"));
				 obj.setQuantity(rs.getInt("quantity"));
				 obj.setPrice(rs.getDouble("price"));
				 obj.setDiscountprice(rs.getDouble("discountprice"));
				 obj.setTotalprice(rs.getDouble("totalprice"));
				 obj.setRemainquantity(rs.getInt("remainquantity"));			
				 obj.setMainquantity(rs.getInt("remainquantity"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---getInvoicesDetailsList-->" , ex);			
		}
		return lst;
	}
	
	public InvoiceModel getInvoiceByInvoicePrefix(String invoiceprefix,int exhibitionid)
	{
		InvoiceModel obj=new InvoiceModel();
		try
		{			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getInvoiceByInvoicePrefixQuery(invoiceprefix,exhibitionid));
			 while(rs.next())
			 {				 
				 obj.setInvoiceid(rs.getInt("invoiceid"));
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setInvoicenumber(rs.getInt("invoicenumber"));
				 obj.setInvoiceprefix(rs.getString("invoiceprefix"));
				 obj.setInvoicedate(rs.getDate("invoicedate"));
				 obj.setCustomerid(rs.getInt("customerid"));
				 obj.setCustomername(rs.getString("customername"));
				 obj.setDiscounttype(rs.getInt("discounttype"));
				 obj.setInvoiceamount(rs.getDouble("invoiceamount"));
				 obj.setInvoicediscount(rs.getDouble("invoicediscount"));
				 obj.setInvoicetotal(rs.getDouble("invoicetotal"));
				 obj.setPaidamount(rs.getDouble("paidamount"));
				 obj.setInvoicestatus(rs.getInt("invoicestatus"));
				 obj.setBalanceamount(rs.getDouble("balanceamount"));
				 obj.setDateofpay(rs.getString("dateofpay"));
				 obj.setNotes(rs.getString("notes"));
				 obj.setCreatedby(rs.getString("createdby"));
				 obj.setCreationdate(rs.getDate("creationdate"));
				 obj.setStatusName(rs.getString("statusname"));				
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CashInvoiceData---getInvoiceByInvoicePrefix-->" , ex);			
		}
		return obj;
	}
}
