package domains;

public class ExhibitionContactModel 
{
	private int contactid;	
	private int exhibitionid;
	private String contactname;	
	private String contactphone;
	private String email;
	private String note;
	public int getContactid() {
		return contactid;
	}
	public void setContactid(int contactid) {
		this.contactid = contactid;
	}
	public int getExhibitionid() {
		return exhibitionid;
	}
	public void setExhibitionid(int exhibitionid) {
		this.exhibitionid = exhibitionid;
	}
	public String getContactname() {
		return contactname;
	}
	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
	public String getContactphone() {
		return contactphone;
	}
	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
