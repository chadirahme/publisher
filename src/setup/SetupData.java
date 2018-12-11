package setup;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import db.DBHandler;
import domains.AgencyModel;
import domains.CityModel;
import domains.CompanyRoles;
import domains.CompanySettingModel;
import domains.CountryModel;
import domains.CurrencyModel;
import domains.ExhibitionContactModel;
import domains.ExhibitionsModel;
import domains.MenuModel;
import domains.Users;

public class SetupData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SetupDataQueries query=new SetupDataQueries();
	
	public List<Users> getUsersList()
	{
		List<Users> lst=new ArrayList<Users>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
		 try
		 {			
		    rs = db.executeNonQuery(query.getUsersQuery());		    
		    while(rs.next())
		    {
		    	Users item=new Users();		    	
		    	item.setUserid(rs.getInt("userid"));
				item.setUsername(rs.getString("username"));
				item.setUserpassword(rs.getString("userpassword"));
				item.setFirstname(rs.getString("firstname"));
				item.setLastname(rs.getString("lastname"));
				item.setUseremail(rs.getString("useremail"));
				item.setUsermobile(rs.getString("usermobile"));		
				item.setIsadmin(rs.getBoolean("isadmin"));			
				item.setRoleid(rs.getInt("roleid"));		
				item.setIsactive(rs.getBoolean("isactive"));
				item.setRoleName(rs.getString("rolename"));
				lst.add(item);			
		    }
			 
		 }
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in SetupData---getUsersList-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return lst;
		 }
	}
	
	public List<CompanyRoles> getCompanyRoles(int companyid,String firstOption)
	{
		List<CompanyRoles> lst=new ArrayList<CompanyRoles>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null; 	
		 try
		 {
			 CompanyRoles obj=new CompanyRoles();
			 if(!firstOption.equals(""))
			 {
				    obj=new CompanyRoles();
			    	obj.setCompanyroleid(0);
			    	obj.setCompanyid(0);
			    	obj.setRolename(firstOption);
			    	lst.add(obj);
			 }
			    rs = db.executeNonQuery(query.getCompanyRolesQuery(companyid));		    
			    while(rs.next())
			    {
			    	obj=new CompanyRoles();
			    	obj.setCompanyroleid(rs.getInt("companyroleid"));
			    	obj.setCompanyid(rs.getInt("companyid"));
			    	obj.setRolename(rs.getString("rolename"));
			    	lst.add(obj);
			    }
		 }
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in SetupData---getCompanyRoles-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return lst;
		 }		 
	}
	
	public void updateUserStatus(int userId,int isactive)
	{
		try
		{
		 DBHandler db=new DBHandler();			
		 String sql="update users set isactive=" + isactive + " where userid=" + userId;
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---updateUserStatus-->" , ex);			
		}
	}
	
	public void deleteUser(int userId)
	{
		try
		{
		 DBHandler db=new DBHandler();			
		 String sql="delete from users where userid=" + userId;
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---deleteUser-->" , ex);			
		}
	}
	
	public void updateUser(Users obj)
	{
		try
		{
		 String sql="";
		 DBHandler db=new DBHandler();	
		 if(obj.getUserid()>0)
		  sql=query.updateUserQuery(obj);
		 else
			 sql=query.addNewUserQuery(obj); 
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---updateUser-->" , ex);			
		}
	}
	
	
	public int updateCompanyRole(CompanyRoles obj)
	{
		int result=0;
		try
		{
		 String sql="";
		 DBHandler db=new DBHandler();	
		 if(obj.getCompanyroleid()>0)
		  sql=query.updateCompanyRoleQuery(obj);
		 else
			 sql=query.addCompanyRoleQuery(obj); 
		 result= db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			result=-1;
			  logger.error("error in SetupData---updateCompanyRole-->" , ex);			
		}
		finally{
			return result;
		}
	}
	
	
	public int checkCompanyRolesIfUsedQuery(int roleid)
	{
		 int count=0;
		try
		{		
		 String sql="";
		 ResultSet rs = null; 	
		 DBHandler db=new DBHandler();			
		 sql=query.checkCompanyRolesIfUsedQuery(roleid);	
		 
		 rs= db.executeNonQuery(sql);
		 while(rs.next())
		 {
			 count++;
		 }
		 
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---updateUser-->" , ex);			
		}
		return count;
	}
	
	
	public void deleteRole(int companyroleid)
	{
		try
		{
		 DBHandler db=new DBHandler();			
		 String sql="delete from companyroles where companyroleid=" + companyroleid;
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---deleteRole-->" , ex);			
		}
	}
	
	public List<MenuModel> getRoleMenuList(int parentId)
	{
		List<MenuModel> lst=new ArrayList<MenuModel>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getSubMenuListQuery(parentId,-1));
			 while(rs.next())
			 {
				 MenuModel obj=new MenuModel();
				 obj.setMenuid(rs.getInt("menuid"));
				 obj.setTitle(rs.getString("title"));				 
				 obj.setArtitle(rs.getString("artitle"));
				 obj.setParentid(rs.getInt("parentid"));
				 obj.setLevel(rs.getInt("level"));
				
				 obj.setChildren(getSubMenuList(obj.getMenuid(),2));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getSubMenuList-->" , ex);			
		}
		return lst;
	}
	
	private List<MenuModel> getSubMenuList(int parentid,int level)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 String sql="";//"SELECT menuid,title,artitle,href,level FROM webmenu where level= " + level + " and  parentid=" + parentid +" order by menuorder";
			 sql=query.getSubMenuListQuery(parentid,level);
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setHref(rs.getString("href"));
		 			obj.setLevel(rs.getInt("level"));		
		 			obj.setArtitle(rs.getString("artitle"));
		 			obj.setCanView(false);
		 			obj.setCanModify(false);
		 			obj.setCanDelete(false);
		 			lstMenu.add(obj);
			 }
			 			 			 
			}
		   catch (Exception ex) 
			{
			 logger.error("error in SetupData---getSubMenuList-->" , ex);
			  
			}
		 return lstMenu;
	}
	
	public List<MenuModel> getRoleCredentials(int companyroleid,int parentId)
	{
		List<MenuModel> lst=new ListModelList<MenuModel>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getRolesCredentialsQuery(companyroleid,parentId));
			 while(rs.next())
			 {
				 MenuModel obj=new MenuModel();
				 obj.setMenuid(rs.getInt("menuid"));
				 obj.setCanView(rs.getBoolean("canview"));
				 obj.setCanModify(rs.getBoolean("canmodify"));
				 obj.setCanDelete(rs.getBoolean("candelete"));
				 obj.setCanAdd(rs.getBoolean("canadd"));
				 obj.setCanExport(rs.getBoolean("canexport"));
				 obj.setCanPrint(rs.getBoolean("canprint"));
				 lst.add(obj);
			 }
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getRoleCredentials-->" , ex);			
		}
		return lst;
	}
	
	public void deleteUserRoleCredential(int companyroleid,int parentId)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.deleteUserRoleCredentialQuery(companyroleid,parentId));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---deleteUserRoleCredential-->" , ex);			
		}
	}
	
	public void addMainMenuToRolesCredentials(int userId,int companyroleid, MenuModel objMenu,int parentId)
	{
		try
		{
		  DBHandler db = new DBHandler();	
		  db.executeUpdateQuery(query.addMainMenuToRolesCredentailsQuery(userId, companyroleid, objMenu,parentId));
		 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---addMainMenuToRolesCredentails-->" , ex);			
		}
	}
	public void addFullUserRolesCredentials(int userId,int companyroleid, List<MenuModel> lstMainUserMenu,int parentId)
	{
		try
		{
		  DBHandler db = new DBHandler();		  
		  for (MenuModel item : lstMainUserMenu) 
		  {
			if(item.isCanView() || item.isCanModify() || item.isCanDelete() || item.isCanAdd() || item.isCanExport() || item.isCanPrint())
			db.executeUpdateQuery(query.addMainMenuToRolesCredentailsQuery(userId, companyroleid, item,parentId));
		  }
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---addFullUserRolesCredentials-->" , ex);			
		}
	}
	
	//companysetting
	public CompanySettingModel getCompanySetting()
	{
		CompanySettingModel obj=new CompanySettingModel();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getCompanySettingQuery());
			 while(rs.next())
			 {				 
				 obj.setCompanysettingid(rs.getInt("companysettingid"));
				 obj.setCompanyName(rs.getString("companyName"));
				 obj.setSecondline(rs.getString("secondline"));
				obj.setPrintcount(rs.getInt("printcount"));
				obj.setInvoicefooter(rs.getString("invoicefooter"));
				obj.setLogopath(rs.getString("logopath"));
				obj.setJenahname(rs.getString("jenahname"));				
			 }
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getCompanySetting-->" , ex);			
		}
		return obj;
	}
	
	public void updateCompanySetting(CompanySettingModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.updateCompanySettingQuery(obj));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---updateCompanySetting-->" , ex);			
		}
	}
	
	//exhibitions
	public List<CountryModel> getCountryList(String firstOption)
	{
		List<CountryModel> lst=new ArrayList<CountryModel>();
		try
		{
			if(!firstOption.equals(""))
			{
				CountryModel obj=new CountryModel(0, firstOption, "");
				lst.add(obj);
			}
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getCountryQuery());
			 while(rs.next())
			 {
				 CountryModel obj=new CountryModel(rs.getInt("countryid"),rs.getString("countryname"),rs.getString("countrycode"));				 
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getCountryList-->" , ex);			
		}
		return lst;
	}
	
	public List<CityModel> getCityList(String firstOption ,int countryid)
	{
		List<CityModel> lst=new ArrayList<CityModel>();
		try
		{
			if(!firstOption.equals(""))
			{
				CityModel obj=new CityModel(0, firstOption, countryid);
				lst.add(obj);
			}
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getCityQuery(countryid));
			 while(rs.next())
			 {
				 CityModel obj=new CityModel(rs.getInt("cityid"),rs.getString("cityname"),rs.getInt("countryid"));				 
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getCityList-->" , ex);			
		}
		return lst;
	}
	
	public List<CurrencyModel> getCurrencyList(String firstOption)
	{
		List<CurrencyModel> lst=new ArrayList<CurrencyModel>();
		try
		{
			if(!firstOption.equals(""))
			{
				CurrencyModel obj=new CurrencyModel(0, firstOption, 0,"");
				lst.add(obj);
			}
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getCurrencyQuery());
			 while(rs.next())
			 {
				 CurrencyModel obj=new CurrencyModel(rs.getInt("currencyid"),rs.getString("currencyname"),rs.getDouble("rate"),rs.getString("currencysymbol"));				 
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getCurrencyList-->" , ex);			
		}
		return lst;
	}
	
	
	public List<ExhibitionsModel> getExhibitionsList(int countryId)
	{
		List<ExhibitionsModel> lst=new ArrayList<ExhibitionsModel>();
		try
		{			
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getExhibitionsListQuery(countryId));
			 while(rs.next())
			 {
				 ExhibitionsModel obj=new ExhibitionsModel();	
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setExhibitionname(rs.getString("exhibitionname"));
				 obj.setFromdate(rs.getDate("fromdate"));
				 obj.setTodate(rs.getDate("todate"));
				 obj.setCountryName(rs.getString("countryname"));
				 obj.setCityName(rs.getString("cityname"));
				 obj.setContactname(rs.getString("contactname"));
				 obj.setContactmobile(rs.getString("contactmobile"));
				 obj.setIsactive(rs.getBoolean("isactive"));
				 obj.setCountryid(rs.getInt("countryid"));
				 obj.setCityid(rs.getInt("cityid"));
				 obj.setContactemail(rs.getString("contactemail"));
				 obj.setTransferdate(rs.getDate("transferdate"));
				 obj.setCurrencyid(rs.getInt("currencyid"));
				 obj.setCurrencyrate(rs.getDouble("currencyrate"));
				 obj.setDiscountaudience(rs.getDouble("discountaudience"));
				 obj.setDiscounttotal(rs.getDouble("discounttotal"));
				 obj.setPrefix(rs.getString("prefix"));
				 obj.setSerialnumber(rs.getInt("serialnumber"));
				 obj.setMeterreserver(rs.getDouble("meterreserver"));
				 obj.setMeterprice(rs.getDouble("meterprice"));
				 
				 obj.setMandoob1(rs.getString("mandoob1"));
				 obj.setMandoob1ticket(rs.getBoolean("mandoob1ticket"));
				 obj.setMandoob1visa(rs.getBoolean("mandoob1visa"));
				 obj.setMandoob2(rs.getString("mandoob2"));
				 obj.setMandoob2ticket(rs.getBoolean("mandoob2ticket"));
				 obj.setMandoob2visa(rs.getBoolean("mandoob2visa"));
				 obj.setNotes(rs.getString("notes"));
				 obj.setTax(rs.getDouble("tax"));				 				
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getExhibitionsList-->" , ex);			
		}
		return lst;
	}
	
	public void addexhibitions(ExhibitionsModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 if(obj.getExhibitionid()==0)
			 {
			 int exbId= db.executeUpdateQuery(query.addexhibitionsQuery(obj));
			 db.executeUpdateQuery(query.updateExhibitionAgencyQuery(exbId));
			 db.executeUpdateQuery(query.updateExhibitionContactQuery(exbId));
			 db.executeUpdateQuery(query.updateCurrencySymbolQuery(obj));
			 }
			 else
			 {
				 db.executeUpdateQuery(query.updatexhibitionsQuery(obj));
				 db.executeUpdateQuery(query.updateCurrencySymbolQuery(obj));
			 }
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---addexhibitions-->" , ex);			
		}
	}
	
	public void updateExhibitionsStatus(int exhibitionid,int isactive)
	{
		try
		{
		 DBHandler db=new DBHandler();			
		 String sql="update exhibitions set isactive=" + isactive + " where exhibitionid=" + exhibitionid;
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---updateExhibitionsStatus-->" , ex);			
		}
	}
	
	
	public List<AgencyModel> getAgencyList(String firstOption , int exhibitionid)
	{
		List<AgencyModel> lst=new ArrayList<AgencyModel>();
		try
		{
			if(!firstOption.equals(""))
			{
				AgencyModel obj=new AgencyModel();
				obj.setAgencyid(0);
				obj.setExhibitionid(exhibitionid);
				lst.add(obj);
			}
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getAgencyQuery(exhibitionid));
			 while(rs.next())
			 {
				 AgencyModel obj=new AgencyModel();
				 obj.setAgencyid(rs.getInt("agencyid"));
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setAgencyname(rs.getString("agencyname"));
				 obj.setInvoicenumber(rs.getString("invoicenumber"));
				 obj.setInvoiceamount(rs.getDouble("invoiceamount"));
				 obj.setBoxquantity(rs.getInt("boxquantity"));
				 obj.setDiscountpercentage(rs.getDouble("discountpercentage"));
				 obj.setDiscountaudience(rs.getDouble("discountaudience"));				 				
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getAgencyList-->" , ex);			
		}
		return lst;
	}
	
	public void addAgency(AgencyModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 if(obj.getAgencyid()==0)
			 db.executeUpdateQuery(query.addAgencyQuery(obj));
			 else
			db.executeUpdateQuery(query.updateAgencyQuery(obj));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---addAgency-->" , ex);			
		}
	}
	
	public void deleteAgency(int agencyid)
	{
		try
		{
		 DBHandler db=new DBHandler();					
		 db.executeUpdateQuery(query.deleteAgencyQuery(agencyid));
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---deleteAgency-->" , ex);			
		}
	}
	public int checkAgencyBooks(int agencyid)
	{
		 int count=0;
		try
		{		
		 String sql="";
		 ResultSet rs = null; 	
		 DBHandler db=new DBHandler();			
		 sql=query.checkAgencyBooksQuery(agencyid);	
		 
		 rs= db.executeNonQuery(sql);
		 while(rs.next())
		 {
			 count++;
		 }
		 
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---checkAgencyBooks-->" , ex);			
		}
		return count;
	}
	
	//Contacts
	public List<ExhibitionContactModel> getExhibitionContactList(String firstOption , int exhibitionid)
	{
		List<ExhibitionContactModel> lst=new ArrayList<ExhibitionContactModel>();
		try
		{
			if(!firstOption.equals(""))
			{
				ExhibitionContactModel obj=new ExhibitionContactModel();
				obj.setContactid(0);
				obj.setExhibitionid(exhibitionid);
				lst.add(obj);
			}
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getExhibitionContactQuery(exhibitionid));
			 while(rs.next())
			 {
				 ExhibitionContactModel obj=new ExhibitionContactModel();
				 obj.setContactid(rs.getInt("contactid"));
				 obj.setExhibitionid(rs.getInt("exhibitionid"));
				 obj.setContactname(rs.getString("contactname"));
				 obj.setContactphone(rs.getString("contactphone"));
				 obj.setEmail(rs.getString("email"));
				 obj.setNote(rs.getString("note"));				
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in SetupData---getExhibitionContactList-->" , ex);			
		}
		return lst;
	}
	
	public void addExhibitionContact(ExhibitionContactModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 if(obj.getContactid()==0)
			 db.executeUpdateQuery(query.addExhibitionContactQuery(obj));
			 else
			 db.executeUpdateQuery(query.updateExhibitionContactQuery(obj));	 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in SetupData---addExhibitionContact-->" , ex);			
		}
	}
	
	public void deleteExhibitionContact(int contactid)
	{
		try
		{
		 DBHandler db=new DBHandler();					
		 db.executeUpdateQuery(query.deleteExhibitionContactQuery(contactid));
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupData---deleteExhibitionContact-->" , ex);			
		}
	}
}
