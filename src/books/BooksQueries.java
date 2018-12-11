package books;

import java.text.SimpleDateFormat;

import domains.AgencyModel;
import domains.BooksModel;
import domains.ImportbookModel;

public class BooksQueries 
{
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getActiveExhibitionsListQuery()
	{
		query=new StringBuffer();
	    query.append("Select e.* ,  c.currencyname,c.rate  from exhibitions e INNER JOIN currency c ON e.currencyid=c.currencyid ");
	    query.append(" where isactive=1 ");	
	    query.append(" ORDER BY exhibitionname ");
		return query.toString();		
	}
	
	public String getAgencyQuery(int exhibitionid)
	{
		query=new StringBuffer();
		if(exhibitionid>0)
	    query.append("Select * from agency where exhibitionid= " + exhibitionid);
		else
		query.append("Select * from agency ");
	    query.append(" ORDER BY agencyname ");
		return query.toString();		
	}
	
	public String getImportBooksListQuery(int exhibitionid)
	{
		query=new StringBuffer();
	    query.append("SELECT i.importbookid,i.invoicenumber,i.importdate,a.agencyname,a.agencyid ");
	    query.append(" FROM importbook i INNER JOIN agency a ON i.agencyid=a.agencyid");
	    query.append(" where i.exhibitionid= " + exhibitionid);
		return query.toString();		
	}
	
	public String addImportBookQuery(ImportbookModel obj)
	{
		query=new StringBuffer();
		query.append("insert into importbook(exhibitionid, agencyid,invoicenumber,filepath)"
				+ " values('%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(), obj.getExhibitionid(), obj.getAgencyid() , obj.getInvoicenumber(),obj.getFilepath());
						
	}
	
	
	public String addBooksQuery(BooksModel obj)
	{
		query=new StringBuffer();
		query.append("insert into books(importbookid,bookcode, bookname,editor,author,quantity,price,remainquantity,agencyid)"
				+ " values('%s','%s','%s','%s','%s','%s','%s','%s','%s')");
		
		return query.toString().format(query.toString(), obj.getImportbookid(),obj.getBookCode(), obj.getBookName() , obj.getEditor(),obj.getAuthor(),
				obj.getQuantity(),obj.getPrice(),obj.getQuantity(),obj.getAgencyid());
						
	}
	public String updateBooksQuantityQuery(BooksModel obj)
	{
		query=new StringBuffer();
		query.append("update books set quantity=quantity+'%s',remainquantity=remainquantity+'%s' "
				+ " where bookid ='%s' ");				    
		return query.toString().format(query.toString(),obj.getQuantity(),
				obj.getQuantity() , obj.getBookid());
						
	}
	
	public String updateBooksQuery(BooksModel obj)
	{
		query=new StringBuffer();
		query.append("update books set bookcode='%s' , bookname='%s',editor='%s',quantity='%s',price='%s',localprice='%s' ,remainquantity='%s' "
				+ " where bookid ='%s' ");				    
		return query.toString().format(query.toString(),obj.getBookCode(), obj.getBookName() , obj.getEditor(),obj.getQuantity(),
				obj.getBookPrice(),obj.getLocalPrice(),obj.getRemainquantity() , obj.getBookid());
						
	}
	
	public String checkInvoiceNumberQuery(ImportbookModel obj)
	{
		query=new StringBuffer();
	    query.append("Select * from importbook where exhibitionid= " + obj.getExhibitionid() + " and agencyid=" + obj.getAgencyid());
	    query.append(" and invoicenumber='" + obj.getInvoicenumber()+"'");
		return query.toString();		
	}
	
	public String getBooksManagmentListQuery(int exhibitionid,int agencyid,String invoicenumber)
	{
		query=new StringBuffer();
	    query.append("SELECT b.* ");
	    query.append(" FROM importbook i INNER JOIN books b ON i.importbookid=b.importbookid");
	    query.append(" where i.exhibitionid= " + exhibitionid);
	    query.append(" and i.agencyid=" + agencyid);
	    if(!invoicenumber.equals(""))
	    	 query.append(" and i.invoicenumber='" + invoicenumber + "'");
	    
	    query.append("  ORDER BY b.bookname ");
		return query.toString();		
	}
	
	public String updateAgencyQuery(AgencyModel obj)
	{
		query=new StringBuffer();
		query.append("update agency set agencyname ='%s',address='%s',telephone='%s'"
				+ " where agencyid='%s'");				    
		return query.toString().format(query.toString(), obj.getAgencyname() , obj.getAddress(),
				obj.getTelephone(),obj.getAgencyid());		
	}
	
}
