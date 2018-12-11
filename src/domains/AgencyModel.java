package domains;

public class AgencyModel 
{

	private int agencyid;
	private int exhibitionid;
	private String agencyname;
	private String invoicenumber;
	private double invoiceamount;
	private int boxquantity;
	private double discountpercentage;
	private String address;
	private String telephone;
	private boolean editingStatus;
	private double discountaudience;
	
	public int getAgencyid() {
		return agencyid;
	}
	public void setAgencyid(int agencyid) {
		this.agencyid = agencyid;
	}
	public int getExhibitionid() {
		return exhibitionid;
	}
	public void setExhibitionid(int exhibitionid) {
		this.exhibitionid = exhibitionid;
	}
	public String getAgencyname() {
		return agencyname;
	}
	public void setAgencyname(String agencyname) {
		this.agencyname = agencyname;
	}
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public double getInvoiceamount() {
		return invoiceamount;
	}
	public void setInvoiceamount(double invoiceamount) {
		this.invoiceamount = invoiceamount;
	}
	public int getBoxquantity() {
		return boxquantity;
	}
	public void setBoxquantity(int boxquantity) {
		this.boxquantity = boxquantity;
	}
	public double getDiscountpercentage() {
		return discountpercentage;
	}
	public void setDiscountpercentage(double discountpercentage) {
		this.discountpercentage = discountpercentage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	public double getDiscountaudience() {
		return discountaudience;
	}
	public void setDiscountaudience(double discountaudience) {
		this.discountaudience = discountaudience;
	}
	
	
}
