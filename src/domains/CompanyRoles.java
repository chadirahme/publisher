package domains;

public class CompanyRoles 
{

	private int companyroleid;
	private int companyid;
	private String rolename;
	private boolean editingStatus;
	
	public int getCompanyroleid() {
		return companyroleid;
	}
	public void setCompanyroleid(int companyroleid) {
		this.companyroleid = companyroleid;
	}
	public int getCompanyid() {
		return companyid;
	}
	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	
	
}
