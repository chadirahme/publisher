package reports;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import db.DBHandler;
import domains.CustomerModel;
import domains.ReportModel;
import domains.Users;

public class ReportsData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	ReportsQueries query=new ReportsQueries();
	
	public List<CustomerModel> getCustomerList(int countryId)
	{
		List<CustomerModel> lst=new ArrayList<CustomerModel>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
		 try
		 {			
		    rs = db.executeNonQuery(query.getCustomersListQuery(countryId));		    
		    while(rs.next())
		    {
		    	CustomerModel item=new CustomerModel();		    	
		    	item.setCustomerid(rs.getInt("customerid"));
		    	item.setExhibitionid(rs.getInt("exhibitionid"));
		    	item.setCustomername(rs.getString("customername"));
		    	item.setMobile(rs.getString("mobile"));
		    	item.setEmail(rs.getString("email"));
		    	item.setCountryid(rs.getInt("countryid"));
		    	item.setCityid(rs.getInt("cityid"));
		    	item.setAddress(rs.getString("address"));
		    	item.setNotes(rs.getString("notes"));
		    	item.setSpecial(rs.getDouble("special"));
		    	item.setCountryName(rs.getString("countryname"));
		    	item.setCityName(rs.getString("cityname"));
				lst.add(item);			
		    }
			 
		 }
		  catch (Exception ex) 
	 		{		 	  
			 logger.error("error in ReportsData---getCustomerList-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return lst;
		 }
	}
	
	public void updateCustomer(CustomerModel obj)
	{
		try
		{
		 String sql="";
		 DBHandler db=new DBHandler();	
		 if(obj.getCustomerid()>0)
		  sql=query.updateCustomerQuery(obj);
		 else
			 sql=query.addNewCustomerQuery(obj); 
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in ReportsData---updateCustomer-->" , ex);						
		}
	}
	
	public int checkCustomersInvoices(int customerid)
	{
		 int count=0;
		try
		{		
		 String sql="";
		 ResultSet rs = null; 	
		 DBHandler db=new DBHandler();			
		 sql=query.checkCustomerInvoicesQuery(customerid);	
		 
		 rs= db.executeNonQuery(sql);
		 while(rs.next())
		 {
			 count++;
		 }
		 
		}
		catch (Exception ex) 
		{
			  logger.error("error in ReportsData---checkCustomersInvoices-->" , ex);			
		}
		return count;
	}
	
	
	public void deleteCustomer(int customerid)
	{
		try
		{
		 DBHandler db=new DBHandler();			
		 String sql=query.deleteCustomerQuery(customerid);
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in ReportsData---deleteCustomer-->" , ex);			
		}
	}
	
	
	public List<ReportModel> getBooksBuyList(int exhibitionid,String bookCode,String bookName,boolean withReturn ,int agencyId)
	{
		List<ReportModel> lst=new ArrayList<ReportModel>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
		 try
		 {			
		    rs = db.executeNonQuery(query.getBooksBuyListQuery(exhibitionid, bookCode, bookName,withReturn,agencyId));		    
		    while(rs.next())
		    {
		    	ReportModel item=new ReportModel();		    	
		    	item.setBookid(rs.getInt("bookid"));		    	
		    	item.setBookName(rs.getString("bookname"));
		    	item.setBookCode(rs.getString("bookcode"));
		    	item.setCreattionDate(rs.getString("creationdate"));		    
		    	item.setInvoiceprefix(rs.getString("invoiceprefix"));
		    	item.setQuantity(rs.getInt("quantity"));
		    	item.setPrice(rs.getDouble("price"));
		    	item.setLocalPrice(rs.getDouble("bookPrice"));
		    	item.setDiscountPrice(rs.getDouble("discountprice"));
		    	item.setExhibitionname(rs.getString("exhibitionname"));
		    	item.setInvoicestatus(rs.getInt("invoicestatus"));
		    	item.setStatusName(rs.getString("statusname"));
		    	item.setAgencyName(rs.getString("agencyname")==null?"":rs.getString("agencyname"));		    	
				lst.add(item);			
		    }
			 
		 }
		  catch (Exception ex) 
	 		{		 	  
			 logger.error("error in ReportsData---getBooksBuyList-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return lst;
		 }
	}
	
	public List<ReportModel> getBooksRemainsList(int exhibitionid,int bookQty,int agencyid)
	{
		List<ReportModel> lst=new ArrayList<ReportModel>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
		 try
		 {			
		    rs = db.executeNonQuery(query.getBooksRemainsQuery(exhibitionid, bookQty,agencyid));		    
		    while(rs.next())
		    {
		    	ReportModel item=new ReportModel();		    	
		    	item.setBookid(rs.getInt("bookid"));		    	
		    	item.setBookName(rs.getString("bookname"));
		    	item.setBookCode(rs.getString("bookcode"));		    	
		    	item.setQuantity(rs.getInt("quantity"));
		    	item.setRemainQuantity(rs.getInt("remainquantity"));
		    	item.setAgencyName(rs.getString("agencyname"));
		    	item.setAgencyInvoiceNumber(rs.getString("invoicenumber"));
		    	item.setPrice(rs.getDouble("price"));
		    	item.setLocalPrice(rs.getDouble("localprice"));
				lst.add(item);			
		    }
			 
		 }
		  catch (Exception ex) 
	 		{		 	  
			 logger.error("error in ReportsData---getBooksRemainsList-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return lst;
		 }
	}
}
