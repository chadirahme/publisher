package domains;

public class Users
{
	private Integer userid;
	private String username;
	private String userpassword;
	private String firstname;
	private String lastname;
	private String useremail;
	private String usermobile;
	private Integer roleid;
	private boolean isadmin;
	private boolean isactive;
	private String roleName;
	private String invoiceFooter;
	private String exhibitionname;
	
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpassword() {
		return userpassword;
	}
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public String getUsermobile() {
		return usermobile;
	}
	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
	}
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	public boolean isIsadmin() {
		return isadmin;
	}
	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}
	public boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getInvoiceFooter() {
		return invoiceFooter;
	}
	public void setInvoiceFooter(String invoiceFooter) {
		this.invoiceFooter = invoiceFooter;
	}
	public String getExhibitionname() {
		return exhibitionname;
	}
	public void setExhibitionname(String exhibitionname) {
		this.exhibitionname = exhibitionname;
	}
	
	
}
