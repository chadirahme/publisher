package domains;

public class ImportbookModel 
{

	private int importbookid;
	private int exhibitionid;
	private int agencyid;
	private String invoicenumber;
	private String filepath;
	
	private String agencyName;
	private String importDate;
	
	public int getImportbookid() {
		return importbookid;
	}
	public void setImportbookid(int importbookid) {
		this.importbookid = importbookid;
	}
	public int getExhibitionid() {
		return exhibitionid;
	}
	public void setExhibitionid(int exhibitionid) {
		this.exhibitionid = exhibitionid;
	}
	public int getAgencyid() {
		return agencyid;
	}
	public void setAgencyid(int agencyid) {
		this.agencyid = agencyid;
	}
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getImportDate() {
		return importDate;
	}
	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}
	
	
	
}
