package domains;

public class DataFilter 
{
	private String bookName = "";
	private String bookCode = "";
	private String bookEditor = "";
	private String bookAuthor = "";
	
	private String invoiceprefix = "";
	private String invoicedate = "";
	private String invoicetotal = "";
	
	public String getBookEditor() {
		return bookEditor;
	}
	public void setBookEditor(String bookEditor) {
		this.bookEditor = bookEditor;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	private String createdby = "";
	
	private String customerName = "";
	private String mobile = "";
	
	private String statusName = "";
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookCode() {
		return bookCode;
	}
	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getInvoiceprefix() {
		return invoiceprefix;
	}
	public void setInvoiceprefix(String invoiceprefix) {
		this.invoiceprefix = invoiceprefix;
	}
	public String getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(String invoicedate) {
		this.invoicedate = invoicedate;
	}
	
	public String getInvoicetotal() {
		return invoicetotal;
	}
	public void setInvoicetotal(String invoicetotal) {
		this.invoicetotal = invoicetotal;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
