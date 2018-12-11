package books;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import db.DBHandler;
import domains.AgencyModel;
import domains.BooksModel;
import domains.ExhibitionsModel;
import domains.ImportbookModel;

public class BooksData 
{

	private Logger logger = Logger.getLogger(this.getClass());
	BooksQueries query=new BooksQueries();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public List<ExhibitionsModel> getActiveExhibitionsList(String firstOption )
	{
		List<ExhibitionsModel> lst=new ArrayList<ExhibitionsModel>();
		try
		{			
			if(!firstOption.equals(""))
			{
				 ExhibitionsModel obj=new ExhibitionsModel();
				 obj.setExhibitionid(0);
				 obj.setExhibitionname(firstOption);
				 lst.add(obj);
			}
			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getActiveExhibitionsListQuery());
			 while(rs.next())
			 {
				 ExhibitionsModel obj=new ExhibitionsModel();	
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setExhibitionname(rs.getString("exhibitionname"));
				 obj.setFromdate(rs.getDate("fromdate"));
				 obj.setTodate(rs.getDate("todate"));				
				 obj.setContactname(rs.getString("contactname"));
				 obj.setContactmobile(rs.getString("contactmobile"));
				 obj.setIsactive(rs.getBoolean("isactive"));
				 obj.setCountryid(rs.getInt("countryid"));
				 obj.setCityid(rs.getInt("cityid"));
				 obj.setContactemail(rs.getString("contactemail"));
				 obj.setTransferdate(rs.getDate("transferdate"));
				 obj.setCurrencyid(rs.getInt("currencyid"));
				// obj.setCurrencyrate(rs.getDouble("currencyrate"));
				 obj.setDiscountaudience(rs.getDouble("discountaudience"));
				 obj.setDiscounttotal(rs.getDouble("discounttotal"));
				 obj.setPrefix(rs.getString("prefix"));
				 obj.setSerialnumber(rs.getInt("serialnumber"));
				 obj.setMeterreserver(rs.getDouble("meterreserver"));
				 obj.setMeterprice(rs.getDouble("meterprice"));
				 
				 obj.setMandoob1(rs.getString("mandoob1"));
				 obj.setMandoob1ticket(rs.getBoolean("mandoob1ticket"));
				 obj.setMandoob1visa(rs.getBoolean("mandoob1visa"));
				 obj.setMandoob2(rs.getString("mandoob2"));
				 obj.setMandoob2ticket(rs.getBoolean("mandoob2ticket"));
				 obj.setMandoob2visa(rs.getBoolean("mandoob2visa"));
				 obj.setNotes(rs.getString("notes"));
				 obj.setCurrencyName(rs.getString("currencyname"));
				 obj.setCurrencyrate(rs.getDouble("currencyrate"));//rate
				
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in BooksData---getActiveExhibitionsList-->" , ex);			
		}
		return lst;
	}
	
	public List<AgencyModel> getAgencyList(String firstOption , int exhibitionid)
	{
		List<AgencyModel> lst=new ArrayList<AgencyModel>();
		try
		{
			if(!firstOption.equals(""))
			{
				AgencyModel obj=new AgencyModel();
				obj.setAgencyid(0);
				obj.setExhibitionid(exhibitionid);
				obj.setAgencyname(firstOption);
				lst.add(obj);
			}			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getAgencyQuery(exhibitionid));
			 while(rs.next())
			 {
				 AgencyModel obj=new AgencyModel();
				 obj.setAgencyid(rs.getInt("agencyid"));
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setAgencyname(rs.getString("agencyname"));
				 obj.setInvoicenumber(rs.getString("invoicenumber"));
				 obj.setInvoiceamount(rs.getDouble("invoiceamount"));
				 obj.setBoxquantity(rs.getInt("boxquantity"));
				 obj.setDiscountpercentage(rs.getDouble("discountpercentage"));
				 obj.setAddress(rs.getString("address"));
				 obj.setTelephone(rs.getString("telephone"));
				 obj.setDiscountaudience(rs.getDouble("discountaudience"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in BooksData---getAgencyList-->" , ex);			
		}
		return lst;
	}
	
	public List<ImportbookModel> getImportBooksList(int exhibitionid)
	{
		List<ImportbookModel> lst=new ArrayList<ImportbookModel>();
		try
		{			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getImportBooksListQuery(exhibitionid));
			 while(rs.next())
			 {
				 ImportbookModel obj=new ImportbookModel();
				 obj.setExhibitionid(exhibitionid);
				 obj.setAgencyid(rs.getInt("agencyid"));
				 obj.setImportbookid(rs.getInt("importbookid"));
				 obj.setAgencyName(rs.getString("agencyname"));
				 obj.setInvoicenumber(rs.getString("invoicenumber"));
				 obj.setImportDate(sdf.format(rs.getDate("importdate")));				 			
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in BooksData---getImportBooksList-->" , ex);			
		}
		return lst;
	}
	
	public void addImportBook(ImportbookModel obj,List<BooksModel> lstNewImport,List<BooksModel> lstExistsImport)
	{
		try
		{
			 Statement st = null;
			 DBHandler db = new DBHandler();
					 
			 int importbookid=db.executeUpdateQuery(query.addImportBookQuery(obj));
			 if(importbookid>0)
			 {
			 logger.info("Begin Batch insert");	 
			 db.connect();
			 Connection conn=db.getConnection();	
			 st = conn.createStatement();	
			 st.addBatch(" START TRANSACTION ");
			 			 			
			 for (BooksModel item : lstNewImport) 
			 {
				 item.setAgencyid(obj.getAgencyid());
				item.setImportbookid(importbookid);				
				String sqlQuery=query.addBooksQuery(item);
				st.addBatch(sqlQuery);
				//db.executeUpdateQuery(query.addBooksQuery(item));
			 }
			 
			 for (BooksModel item : lstExistsImport) 
			 {				
				String sqlQuery=query.updateBooksQuantityQuery(item);
				st.addBatch(sqlQuery);				
			 }
			 
			 st.addBatch(" COMMIT ");
			 st.executeBatch();
			 st.clearBatch();			 
			// dbw.closeStatement(st);
			 //dbw.closeConnection(conn);
			 logger.info("End Batch insert");	 
			 
			 }
			 
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in BooksData---addImportBook-->" , ex);			
		}
	}
	
	public void addImportBookOld(ImportbookModel obj,List<BooksModel> lstBooks)
	{
		try
		{
			 Statement st = null;
			 DBHandler db = new DBHandler();
					 
			 int importbookid=db.executeUpdateQuery(query.addImportBookQuery(obj));
			 if(importbookid>0)
			 {
			 logger.info("Begin Batch insert");	 
			 db.connect();
			 Connection conn=db.getConnection();	
			 st = conn.createStatement();	
			 st.addBatch(" START TRANSACTION ");
			 
			 
			 
			 for (BooksModel item : lstBooks) 
			 {
				item.setImportbookid(importbookid);				
				String sqlQuery=query.addBooksQuery(item);
				st.addBatch(sqlQuery);
				//db.executeUpdateQuery(query.addBooksQuery(item));
			 }
			 
			 st.addBatch(" COMMIT ");
			 st.executeBatch();
			 st.clearBatch();			 
			// dbw.closeStatement(st);
			 //dbw.closeConnection(conn);
			 logger.info("End Batch insert");	 
			 
			 }
			 
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in BooksData---addImportBook-->" , ex);			
		}
	}
	
	public void updateImportBook(List<BooksModel> lstBooks)
	{
		try
		{
			 Statement st = null;
			 DBHandler db = new DBHandler();
					 						
			 logger.info("Begin Batch update");	 
			 db.connect();
			 Connection conn=db.getConnection();	
			 st = conn.createStatement();	
			 st.addBatch(" START TRANSACTION ");
			 
			 for (BooksModel item : lstBooks) 
			 {						
				String sqlQuery=query.updateBooksQuery(item);
				st.addBatch(sqlQuery);			
			 }
			 
			 st.addBatch(" COMMIT ");
			 st.executeBatch();
			 st.clearBatch();			 		
			 logger.info("End Batch update");	 
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in BooksData---updateImportBook-->" , ex);			
		}
	}
	
	public void addBooks(BooksModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.addBooksQuery(obj));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in BooksData---addBooks-->" , ex);			
		}
	}
	
	public void updateBooks(BooksModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.updateBooksQuery(obj));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in BooksData---updateBooks-->" , ex);			
		}
	}
	
	public boolean checkInvoiceNumber(ImportbookModel obj)
	{
		boolean result=false;
		try
		{
			int importbookid=0;
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.checkInvoiceNumberQuery(obj));
			while(rs.next())
			{
			   importbookid=rs.getInt("importbookid");
			}
			result=importbookid>0;
		}
		catch (Exception ex) 
		  {
			  logger.error("error in BooksData---checkInvoiceNumber-->" , ex);			
		}
		return result;
	}
	
	
	public List<BooksModel> getBooksManagmentList(int exhibitionid,int agencyid,String invoicenumber)
	{
		List<BooksModel> lst=new ArrayList<BooksModel>();
		try
		{			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getBooksManagmentListQuery(exhibitionid, agencyid, invoicenumber));
			 while(rs.next())
			 {
				 BooksModel obj=new BooksModel();			
				 obj.setBookid(rs.getInt("bookid"));				
				 obj.setBookCode(rs.getString("bookcode"));
				 obj.setBookName(rs.getString("bookname"));
				obj.setEditor(rs.getString("editor"));
				obj.setQuantity(rs.getString("quantity"));
				obj.setBookPrice(rs.getDouble("price"));
				obj.setRemainquantity(rs.getInt("remainquantity"));
				obj.setLocalPrice(rs.getDouble("localprice"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in BooksData---getBooksManagmentList-->" , ex);			
		}
		return lst;
	}
	
	public void updateAgency(AgencyModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.updateAgencyQuery(obj));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in BooksData---updateAgency-->" , ex);			
		}
	}
}
