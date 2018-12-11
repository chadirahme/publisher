package domains;

import java.util.Comparator;

public class InvoiceDetailModel 
{
	private int invoicedetailid;
	private int invoiceid;
	private int invdline;
	private int bookid;
	private String bookcode;
	private String bookname;
	private int quantity;
	private double price;
	private double discountprice;
	private double totalprice;
	private int remainquantity;
	private int mainquantity;
	
	public int getInvoicedetailid() {
		return invoicedetailid;
	}
	public void setInvoicedetailid(int invoicedetailid) {
		this.invoicedetailid = invoicedetailid;
	}
	public int getInvoiceid() {
		return invoiceid;
	}
	public void setInvoiceid(int invoiceid) {
		this.invoiceid = invoiceid;
	}
	public int getInvdline() {
		return invdline;
	}
	public void setInvdline(int invdline) {
		this.invdline = invdline;
	}
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	public String getBookcode() {
		return bookcode;
	}
	public void setBookcode(String bookcode) {
		this.bookcode = bookcode;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getDiscountprice() {
		return discountprice;
	}
	public void setDiscountprice(double discountprice) {
		this.discountprice = discountprice;
	}
	public double getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}
	public int getRemainquantity() {
		return remainquantity;
	}
	public void setRemainquantity(int remainquantity) {
		this.remainquantity = remainquantity;
	}
	public int getMainquantity() {
		return mainquantity;
	}
	public void setMainquantity(int mainquantity) {
		this.mainquantity = mainquantity;
	}
	
	 public static Comparator<InvoiceDetailModel> invLineno = new Comparator<InvoiceDetailModel>() {

			public int compare(InvoiceDetailModel s1, InvoiceDetailModel s2) {

			   int invLineno1 = s1.getInvdline();
			   int invLineno2 = s2.getInvdline();

			   /*For ascending order*/
			   return invLineno1-invLineno2;

			   /*For descending order*/
			   //rollno2-rollno1;
		   }};
	
}
