package domains;

import java.util.Date;

public class InvoiceModel 
{

	private int invoiceid;
	private int exhibitionid;
	private int invoicenumber;
	private String invoiceprefix;
	private Date invoicedate;
	private String customername;
	private int customerid;
	private double customerDiscount;
	private int discounttype;
	private double invoiceamount;
	private double invoicediscount;
	private double invoicetotal;
	private double invoicetotalBeforeTax;
	private double paidamount;
	private int invoicestatus;
	private double balanceamount;
	private String dateofpay;
	private String notes;
	private String createdby;
	private Date creationdate;	
	
	private String statusName;
	private int totalQunatity;
	private double invoicediscountPerc;
	private double paidDollarAmount;
	private int paidIndollar;
	private double exhibitionTax;
	private double tax;
	
	public int getInvoiceid() {
		return invoiceid;
	}
	public void setInvoiceid(int invoiceid) {
		this.invoiceid = invoiceid;
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
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public int getCustomerid() {
		return customerid;
	}
	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}
	public int getDiscounttype() {
		return discounttype;
	}
	public void setDiscounttype(int discounttype) {
		this.discounttype = discounttype;
	}
	public double getInvoiceamount() {
		return invoiceamount;
	}
	public void setInvoiceamount(double invoiceamount) {
		this.invoiceamount = invoiceamount;
	}
	public double getInvoicediscount() {
		return invoicediscount;
	}
	public void setInvoicediscount(double invoicediscount) {
		this.invoicediscount = invoicediscount;
	}
	public double getInvoicetotal() {
		return invoicetotal;
	}
	public void setInvoicetotal(double invoicetotal) {
		this.invoicetotal = invoicetotal;
	}
	public double getPaidamount() {
		return paidamount;
	}
	public void setPaidamount(double paidamount) {
		this.paidamount = paidamount;
	}
	public int getInvoicestatus() {
		return invoicestatus;
	}
	public void setInvoicestatus(int invoicestatus) {
		this.invoicestatus = invoicestatus;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	public int getExhibitionid() {
		return exhibitionid;
	}
	public void setExhibitionid(int exhibitionid) {
		this.exhibitionid = exhibitionid;
	}
	public double getBalanceamount() {
		return balanceamount;
	}
	public void setBalanceamount(double balanceamount) {
		this.balanceamount = balanceamount;
	}
	public String getDateofpay() {
		return dateofpay;
	}
	public void setDateofpay(String dateofpay) {
		this.dateofpay = dateofpay;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public double getCustomerDiscount() {
		return customerDiscount;
	}
	public void setCustomerDiscount(double customerDiscount) {
		this.customerDiscount = customerDiscount;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public int getTotalQunatity() {
		return totalQunatity;
	}
	public void setTotalQunatity(int totalQunatity) {
		this.totalQunatity = totalQunatity;
	}
	public double getInvoicediscountPerc() {
		return invoicediscountPerc;
	}
	public void setInvoicediscountPerc(double invoicediscountPerc) {
		this.invoicediscountPerc = invoicediscountPerc;
	}
	public double getPaidDollarAmount() {
		return paidDollarAmount;
	}
	public void setPaidDollarAmount(double paidDollarAmount) {
		this.paidDollarAmount = paidDollarAmount;
	}
	public int getPaidIndollar() {
		return paidIndollar;
	}
	public void setPaidIndollar(int paidIndollar) {
		this.paidIndollar = paidIndollar;
	}
	public double getExhibitionTax() {
		return exhibitionTax;
	}
	public void setExhibitionTax(double exhibitionTax) {
		this.exhibitionTax = exhibitionTax;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getInvoicetotalBeforeTax() {
		return invoicetotalBeforeTax;
	}
	public void setInvoicetotalBeforeTax(double invoicetotalBeforeTax) {
		this.invoicetotalBeforeTax = invoicetotalBeforeTax;
	}
	
	
	
}
