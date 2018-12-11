package domains;

import java.util.List;

public class MenuModel 
{

	private int menuid;
	private String title;
	private int level;
	private String href;
	private int parentid;
	private int menuorder;
	private String sclassName;
	private String artitle;
	
	private boolean canView;
	private boolean canAdd;
	private boolean canModify;
	private boolean canDelete;
	private boolean canExport;
	private boolean canPrint;
	
	private List<MenuModel> children;
	
	public int getMenuid() {
		return menuid;
	}
	public void setMenuid(int menuid) {
		this.menuid = menuid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	public List<MenuModel> getChildren() {
		return children;
	}
	public void setChildren(List<MenuModel> children) {
		this.children = children;
	}
	public int getMenuorder() {
		return menuorder;
	}
	public void setMenuorder(int menuorder) {
		this.menuorder = menuorder;
	}
	public String getSclassName() {
		return sclassName;
	}
	public void setSclassName(String sclassName) {
		this.sclassName = sclassName;
	}
	public String getArtitle() {
		return artitle;
	}
	public void setArtitle(String artitle) {
		this.artitle = artitle;
	}
	public boolean isCanView() {
		return canView;
	}
	public void setCanView(boolean canView) {
		this.canView = canView;
	}
	public boolean isCanModify() {
		return canModify;
	}
	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}
	public boolean isCanDelete() {
		return canDelete;
	}
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	public boolean isCanAdd() {
		return canAdd;
	}
	public void setCanAdd(boolean canAdd) {
		this.canAdd = canAdd;
	}
	public boolean isCanExport() {
		return canExport;
	}
	public void setCanExport(boolean canExport) {
		this.canExport = canExport;
	}
	public boolean isCanPrint() {
		return canPrint;
	}
	public void setCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}
	
}
