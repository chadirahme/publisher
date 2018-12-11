package layout;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import db.DBHandler;
import domains.MenuModel;

public class MenuData 
{

	private Logger logger = Logger.getLogger(this.getClass());
	public List<MenuModel> getSideBarSubMenuList(int parentid,int level,int companyroleid)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 //String sql="SELECT menuid,title,artitle,href,level FROM webmenu where level= " + level + " and  parentid=" + parentid +" order by menuorder";
			 String sql="SELECT m.menuid,title,artitle,href,level FROM webmenu m INNER JOIN rolescredentials r ON m.menuid = r.menuid" +
			 		" where level= " + level + " and  m.parentid=" + parentid + " AND r.companyroleid = " + companyroleid + " order by menuorder";
			 
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setHref(rs.getString("href"));
		 			obj.setLevel(rs.getInt("level"));
		 			obj.setArtitle(rs.getString("artitle"));
		 			obj.setSclassName("defaultMenu");	
		 			obj.setChildren(getSubMenuList(obj.getMenuid(),2,companyroleid));
		 			lstMenu.add(obj);
			 }
			 			 			 
			}
		  catch (Exception ex) 
			{
			  logger.error("error in MenuData---getSideBarSubMenuList-->" , ex);
			 /// Messagebox.show(ex.getMessage());
			}
		 return lstMenu;
	}
	
	public List<MenuModel> getSubMenuList(int parentid,int level,int companyroleid)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 //String sql="SELECT menuid,title,artitle,href,level FROM webmenu where level= " + level + " and  parentid=" + parentid +" order by menuorder";
			 String sql="SELECT m.menuid,title,artitle,href,level FROM webmenu m INNER JOIN rolescredentials r ON m.menuid = r.menuid" +
				 		" where level= " + level + " and  m.parentid=" + parentid + " AND r.companyroleid = " + companyroleid + " order by menuorder";
				 
			
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setHref(rs.getString("href"));
		 			obj.setLevel(rs.getInt("level"));		
		 			obj.setArtitle(rs.getString("artitle"));
		 			obj.setSclassName("defaultMenu");
		 			lstMenu.add(obj);
			 }
			 			 			 
			}
		  catch (Exception ex) 
			{
			  logger.error("error in MenuData---getSubMenuList-->" , ex);
			 // Messagebox.show(ex.getMessage());
			}
		 return lstMenu;
	}
}
