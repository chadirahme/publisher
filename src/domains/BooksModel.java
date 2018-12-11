package domains;

public class BooksModel 
{

	private int bookid;
	private int importbookid;
	private String bookCode;
	private String bookName;
	private String editor;
	private String author;
	private String quantity;
	private String price;
	private String total;
	
	
	private double localPrice;
	
	private double bookPrice;
	private int remainquantity;
	private int agencyid;
	
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
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
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
	public double getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}
	public int getRemainquantity() {
		return remainquantity;
	}
	public void setRemainquantity(int remainquantity) {
		this.remainquantity = remainquantity;
	}
	public double getLocalPrice() {
		return localPrice;
	}
	public void setLocalPrice(double localPrice) {
		this.localPrice = localPrice;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getAgencyid() {
		return agencyid;
	}
	public void setAgencyid(int agencyid) {
		this.agencyid = agencyid;
	}
	
	
	
}
