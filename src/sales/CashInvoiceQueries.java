package sales;

import java.text.SimpleDateFormat;
import java.util.Date;

import domains.InvoiceDetailModel;
import domains.InvoiceModel;

public class CashInvoiceQueries 
{

	StringBuffer query;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public String getActiveExhibtionQuery()
	{
		query=new StringBuffer();
	    query.append("Select * from exhibitions e INNER JOIN currency c ON e.currencyid=c.currencyid where isactive=1");
		return query.toString();		
	}
	
	public String getMaxInvoiceNumberQuery(int exhibitionid)
	{
		query=new StringBuffer();
	    query.append("SELECT MAX(invoicenumber) AS 'invno' FROM invoice where exhibitionid="+exhibitionid);
		return query.toString();		
	}
	
	public String getExhibtionBooksListQuery(int exhibitionid)
	{
		query=new StringBuffer();
	    query.append("SELECT b.* ");
	    query.append(" FROM importbook i INNER JOIN books b ON i.importbookid=b.importbookid");
	    query.append(" where i.exhibitionid= " + exhibitionid);
		return query.toString();		
	}
	
	public String addNewInvoiceQuery(InvoiceModel obj)
	{
		String sql="insert into invoice(exhibitionid,invoicenumber,invoiceprefix,invoicedate,customername,customerid,discounttype,"
				+ "invoiceamount,invoicediscount,invoicetotal,paidamount,invoicestatus,createdby,balanceamount,dateofpay,notes,"
				+ "invoicediscountPerc,paidindollar,paiddollaramount) " +
				  "values('%s','%s','%s','%s','%s','%s','%s',"
				  + " '%s','%s','%s','%s','%s','%s'  ,'%s','%s','%s','%s','%s','%s')";
		sql=sql.format(sql, obj.getExhibitionid(),obj.getInvoicenumber(),obj.getInvoiceprefix(),sdf.format(obj.getInvoicedate()),obj.getCustomername(),
				obj.getCustomerid(),obj.getDiscounttype(),obj.getInvoiceamount(),obj.getInvoicediscount(),obj.getInvoicetotal(),
				obj.getPaidamount(),obj.getInvoicestatus(),obj.getCreatedby() ,obj.getBalanceamount(),obj.getDateofpay(),obj.getNotes(),
				obj.getInvoicediscountPerc(),obj.getPaidIndollar(),obj.getPaidDollarAmount());

		return sql;
	}
	
	public String updateInvoiceQuery(InvoiceModel obj)
	{
		String sql="update invoice set customername='%s',customerid='%s',discounttype='%s',"
				+ "invoiceamount='%s',invoicediscount='%s',invoicetotal='%s',paidamount='%s',invoicestatus='%s',"
				+ "createdby='%s',balanceamount='%s',dateofpay='%s',notes='%s', invoicediscountPerc='%s' , paidindollar='%s',paiddollaramount='%s' " +
				  " where invoiceid='%s'";
				 
		sql=sql.format(sql,obj.getCustomername(),
				obj.getCustomerid(),obj.getDiscounttype(),obj.getInvoiceamount(),obj.getInvoicediscount(),obj.getInvoicetotal(),
				obj.getPaidamount(),obj.getInvoicestatus(),obj.getCreatedby() ,obj.getBalanceamount(),obj.getDateofpay(),obj.getNotes(),
				obj.getInvoicediscountPerc(),obj.getPaidIndollar(),obj.getPaidDollarAmount(),
				obj.getInvoiceid());

		return sql;
	}
	
	public String deleteInvoiceDetailsQuery(int invoiceid)
	{
		query=new StringBuffer();
	    query.append("Delete FROM invoicedetail where invoiceid="+invoiceid);
		return query.toString();
	}
	
	public String deleteOnHoldInvoiceQuery(int invoiceid)
	{
		query=new StringBuffer();
	    query.append("Delete FROM invoice where invoiceid="+invoiceid);
		return query.toString();
	}
	
	public String addNewInvoiceDetailQuery(InvoiceDetailModel obj)
	{
		String sql="insert into invoicedetail(invoiceid,invdline,bookid,bookcode,bookname,quantity,price,"
				+ "discountprice,totalprice) " +
				  "values('%s','%s','%s','%s','%s','%s','%s',"
				  + " '%s','%s')";
		sql=sql.format(sql, obj.getInvoiceid(),obj.getInvdline(),obj.getBookid(),obj.getBookcode(),obj.getBookname(),obj.getQuantity(),
				obj.getPrice(),obj.getDiscountprice(),obj.getTotalprice());

		return sql;
	}
	
	public String getInvoicesListByStatusQuery(int exhibitionid , int statusId,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
	    query.append("SELECT * FROM invoice i");
	    query.append(" inner join invoicestatus s ON i.invoicestatus=s.statusid");
	    query.append(" ");
	    query.append(" where exhibitionid="+exhibitionid);
	    if(fromDate!=null)
	    query.append(" and invoicedate between '" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "' ");
	    
	    if(statusId>0)
	    	query.append(" and invoicestatus=" + statusId);
	    query.append(" order by invoicedate desc");
		return query.toString();		
	}
	
	public String getInvoicesDetailsListQuery(int invoiceid)
	{
		query=new StringBuffer();
	    query.append("SELECT * FROM invoicedetail d INNER JOIN books b");	    
	    query.append(" ON d.bookid=b.bookid");
	    query.append(" where invoiceid="+invoiceid);	
	    query.append(" order by invdline");
		return query.toString();		
	}
	
	public String getInvoiceByInvoicePrefixQuery(String invoiceprefix,int exhibitionid)
	{
		query=new StringBuffer();
		 query.append("SELECT * FROM invoice i");
		    query.append(" inner join invoicestatus s ON i.invoicestatus=s.statusid");   	  
	    query.append(" where invoicestatus=1 and invoiceprefix='"+invoiceprefix+"'");	
	    query.append(" AND i.exhibitionid=" +exhibitionid);
		return query.toString();		
	}
	
}
