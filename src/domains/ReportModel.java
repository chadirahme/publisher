package domains;

import java.util.Date;

public class ReportModel 
{
	private int bookid;
	private int importbookid;
	private String bookCode;
	private String bookName;
	private String editor;
	private int quantity;
	private int remainQuantity;
	private double price;
	private double localPrice;
	private double discountPrice;
	private double total;
	private int exhibitionid;
	private String exhibitionname;
	private int invoicenumber;
	private String invoiceprefix;
	private Date invoicedate;
	private String creattionDate;
	private String agencyName;
	private String agencyInvoiceNumber;
	private int invoicestatus;
	private String statusName;
	
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	public int getImportbookid() {
		return importbookid;
	}
	public void setImportbookid(int importbookid) {
		this.importbookid = importbookid;
	}
	public String getBookCode() {
		return bookCode;
	}
	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
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
	public double getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getExhibitionid() {
		return exhibitionid;
	}
	public void setExhibitionid(int exhibitionid) {
		this.exhibitionid = exhibitionid;
	}
	public String getExhibitionname() {
		return exhibitionname;
	}
	public void setExhibitionname(String exhibitionname) {
		this.exhibitionname = exhibitionname;
	}
	public int getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(int invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public String getInvoiceprefix() {
		return invoiceprefix;
	}
	public void setInvoiceprefix(String invoiceprefix) {
		this.invoiceprefix = invoiceprefix;
	}
	public Date getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}
	public String getCreattionDate() {
		return creattionDate;
	}
	public void setCreattionDate(String creattionDate) {
		this.creattionDate = creattionDate;
	}
	public int getRemainQuantity() {
		return remainQuantity;
	}
	public void setRemainQuantity(int remainQuantity) {
		this.remainQuantity = remainQuantity;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	
	public String getAgencyInvoiceNumber() {
		return agencyInvoiceNumber;
	}
	public void setAgencyInvoiceNumber(String agencyInvoiceNumber) {
		this.agencyInvoiceNumber = agencyInvoiceNumber;
	}
	public double getLocalPrice() {
		return localPrice;
	}
	public void setLocalPrice(double localPrice) {
		this.localPrice = localPrice;
	}
	public int getInvoicestatus() {
		return invoicestatus;
	}
	public void setInvoicestatus(int invoicestatus) {
		this.invoicestatus = invoicestatus;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
	
}
