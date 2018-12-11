package reports;

import java.text.SimpleDateFormat;

import domains.CustomerModel;
import domains.Users;

public class ReportsQueries 
{
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public String getCustomersListQuery(int countryId)
	{
		query=new StringBuffer();
	    query.append("  SELECT *  FROM customers c");
	    query.append("  INNER JOIN country co ON c.countryid=co.countryid ");
	    query.append("  INNER JOIN city ci ON ci.cityid=c.cityid");
	    if(countryId>0)
	    query.append("  where c.countryid=" + countryId);	
	    query.append(" order by customername");
		return query.toString();		
	}
	
	public String addNewCustomerQuery(CustomerModel obj)
	{
		String sql="insert into customers(exhibitionid,customername,mobile,email,countryid,cityid,address,notes,special) " +
				  "values('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
		sql=sql.format(sql, obj.getExhibitionid(),obj.getCustomername(),obj.getMobile(),obj.getEmail(),
				obj.getCountryid(),obj.getCityid(),obj.getAddress(),obj.getNotes(),obj.getSpecial());

		return sql;
	}
	
	public String updateCustomerQuery(CustomerModel obj)
	{
		String sql="update customers set customername='%s' , mobile='%s' ,email='%s',countryid='%s',cityid='%s',address='%s' ,notes='%s' ,special='%s' where customerid=%d";
		sql=sql.format(sql, obj.getCustomername(),obj.getMobile(),obj.getEmail(),obj.getCountryid(),obj.getCityid(),
				obj.getAddress() , obj.getNotes(),obj.getSpecial() ,obj.getCustomerid());
		return sql;
	}
	
	public String checkCustomerInvoicesQuery(int customerid)
	{
		query=new StringBuffer();
	    query.append("Select * from invoice where customerid= " + customerid);
		return query.toString();		
	}
	
	public String deleteCustomerQuery(int customerid)
	{
		query=new StringBuffer();
	    query.append("delete from customers where customerid= " + customerid);
		return query.toString();		
	}
	
	
	public String getBooksBuyListQuery(int exhibitionid,String bookCode,String bookName,boolean withReturn ,int agencyId)
	{
		query=new StringBuffer();
	    query.append("  SELECT b.bookid,b.bookcode,b.bookname,i.creationdate,i.invoiceprefix, ");
	    query.append("  d.quantity,d.price,d.discountprice,d.totalprice,e.exhibitionname,i.invoicestatus,statusname,ag.agencyname, b.price AS 'bookPrice' ");
	    query.append(" FROM books b");
	    query.append(" INNER JOIN invoicedetail d ON b.bookid=d.bookid");
	    query.append(" INNER JOIN invoice i ON i.invoiceid=d.invoiceid");
	    query.append(" INNER JOIN exhibitions e ON e.exhibitionid=i.exhibitionid");
	    query.append(" INNER JOIN invoicestatus s ON i.invoicestatus=s.statusid");
	    query.append(" LEFT JOIN agency ag ON ag.agencyid = b.agencyid");
	    query.append(" WHERE 1=1 ");
	    if(withReturn==false)
	    query.append("  and i.invoicestatus<>4");
	    query.append(" ");
	    query.append(" ");
	    
	    if(exhibitionid>0)
	    query.append("  and e.exhibitionid=" + exhibitionid);	
	    if(agencyId>0)
		    query.append("  and b.agencyid=" + agencyId);	
	    
	    if(!bookCode.equals(""))
	    {
	    	 query.append(" and b.bookcode like '%" + bookCode+"%'");
	    }
	    if(!bookName.equals(""))
	    {
	    	 query.append(" and b.bookName like '%" + bookName+"%'");
	    }
	    
	    query.append(" order by b.bookname");
		return query.toString();		
	}
	
	public String getBooksRemainsQuery(int exhibitionid,int bookQty,int agencyid)
	{
		query=new StringBuffer();
	    query.append("  SELECT b.bookid,b.bookcode,b.bookname,b.quantity,b.remainquantity,a.agencyname,a.invoicenumber ,b.localprice,b.price");	  
	    query.append(" FROM books b");
	    query.append(" INNER JOIN importbook i ON i.importbookid=b.importbookid");
	    query.append(" INNER JOIN exhibitions e ON e.exhibitionid=i.exhibitionid");
	    query.append(" INNER JOIN agency a ON a.agencyid=i.agencyid ");
	    
	    query.append(" WHERE 1=1 ");
	    query.append(" ");
	    query.append(" ");
	    
	    if(exhibitionid>0)
	    query.append("  and e.exhibitionid=" + exhibitionid);	
	    
	    if(agencyid>0)
	    	query.append("  and a.agencyid=" + agencyid);	
	
	   query.append(" and b.remainquantity <= " + bookQty );
	  	    
	    
	    query.append(" order by b.bookname");
		return query.toString();		
	}
}
