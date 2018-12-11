package setup;

import java.text.SimpleDateFormat;

import domains.AgencyModel;
import domains.CompanySettingModel;
import domains.ExhibitionContactModel;
import domains.ExhibitionsModel;
import domains.MenuModel;
import domains.CompanyRoles;
import domains.Users;

public class SetupDataQueries 
{
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public String getUsersQuery()
	{
		query=new StringBuffer();
	    query.append("Select * from users u LEFT JOIN companyroles r ON u.roleid=r.companyroleid order by isadmin desc");
		return query.toString();		
	}
	
	public String getCompanyRolesQuery(int companyid)
	{
		query=new StringBuffer();
	    query.append("Select * from companyroles where companyid=" +companyid +" order by rolename");
		return query.toString();		
	}
	
	public String checkCompanyRolesIfUsedQuery(int roleid)
	{
		query=new StringBuffer();
	    query.append("Select * from users where roleid=" +roleid);
		return query.toString();		
	}
	
	
	public String updateUserQuery(Users obj)
	{
		String sql="update users set username='%s' , userpassword='%s' ,firstname='%s',lastname='%s',useremail='%s',usermobile='%s' ,roleid=%d ,isadmin=%d  where userid=%d";
		sql=sql.format(sql, obj.getUsername(),obj.getUserpassword(),obj.getFirstname(),obj.getLastname(),obj.getUseremail(),obj.getUsermobile() , obj.getRoleid(),obj.isIsadmin()?1:0 ,obj.getUserid());
		return sql;
	}
	
	public String addNewUserQuery(Users obj)
	{
		String sql="insert into users(username,userpassword,firstname,lastname,useremail,usermobile,roleid,isadmin,isactive) " +
				  "values('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
		sql=sql.format(sql, obj.getUsername(),obj.getUserpassword(),obj.getFirstname(),obj.getLastname(),obj.getUseremail(),obj.getUsermobile(),obj.getRoleid(),obj.isIsadmin()?1:0,"1");

		return sql;
	}
	
	public String updateCompanyRoleQuery(CompanyRoles obj)
	{
		query=new StringBuffer();
		query.append("update companyroles set rolename='" + obj.getRolename() + "' where companyroleid=" + obj.getCompanyroleid());
		return query.toString();	
	}
	
	public String addCompanyRoleQuery(CompanyRoles obj)
	{
		query=new StringBuffer();
		query.append("insert into companyroles(companyid , rolename) values('%s',N'%s') ");
		return query.toString().format(query.toString(),obj.getCompanyid(),obj.getRolename());			
	}
	
	//menu access
	public String getSubMenuListQuery(int parentId,int level)
	{
		query=new StringBuffer();
		if(level==-1)
	    query.append("SELECT menuid, title,artitle,parentid,level FROM webmenu where parentid="+parentId + " order by menuorder");
		else
		query.append("SELECT menuid,title,artitle,href,level FROM webmenu where level= " + level + " and  parentid=" + parentId +" order by menuorder");
		return query.toString();		
	}
	 
		public String getRolesCredentialsQuery(int companyroleid,int parentId)
		{
			query=new StringBuffer();
		    query.append("SELECT * FROM rolescredentials where companyroleid="+companyroleid + " and parentId=" + parentId);
			return query.toString();		
		}
		
		public String deleteUserRoleCredentialQuery(int companyroleid,int parentId)
		{
			query=new StringBuffer();
		    query.append("DELETE FROM rolescredentials WHERE companyroleid="+companyroleid + " and parentId=" + parentId);
			return query.toString();		
		}
		
		public String addMainMenuToRolesCredentailsQuery(int userID,int companyroleid,MenuModel objMenu,int parentId)
		{
			query=new StringBuffer();
			query.append("insert into rolescredentials(createdby, companyroleid,menuid,canview,canmodify,candelete,parentId,canadd,canexport,canprint) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')");				    
			return query.toString().format(query.toString(), userID, companyroleid , objMenu.getMenuid(),objMenu.isCanView()?1:0,objMenu.isCanModify()?1:0
					,objMenu.isCanDelete()?1:0 , parentId , objMenu.isCanAdd()?1:0,objMenu.isCanExport()?1:0,objMenu.isCanPrint()?1:0);		
		}
		
		//company Setting
		public String getCompanySettingQuery()
		{
			query=new StringBuffer();
		    query.append("Select * from companysetting ");
			return query.toString();		
		}
		
		public String updateCompanySettingQuery(CompanySettingModel obj)
		{
			query=new StringBuffer();
			query.append("update companysetting set companyName='" + obj.getCompanyName() + "' , secondline='" + obj.getSecondline() + "' , ");
			query.append(" printcount='" + obj.getPrintcount() +"' , invoicefooter='"+obj.getInvoicefooter()+"' , logopath='" + obj.getLogopath()+"'");
			query.append(" ,jenahname='" + obj.getJenahname() + "'");			
			query.append(" where companysettingid=" + obj.getCompanysettingid());
			return query.toString();	
		}
		
		//exhibitions
		public String getCountryQuery()
		{
			query=new StringBuffer();
		    query.append("Select * from country ");
			return query.toString();		
		}
		public String getCityQuery(int countryId)
		{
			query=new StringBuffer();
		    query.append("Select * from city where countryid=" + countryId);
			return query.toString();		
		}
		
		public String getCurrencyQuery()
		{
			query=new StringBuffer();
		    query.append("Select * from currency ");
			return query.toString();		
		}
		
		public String getExhibitionsListQuery(int countryId)
		{
			query=new StringBuffer();
		    query.append("Select * from exhibitions e");
		    query.append(" LEFT JOIN country c ON e.countryid=c.countryid");
		    query.append(" LEFT JOIN city t ON t.cityid=e.cityid");
		    if(countryId>0)
			    query.append("  where c.countryid=" + countryId);	
		    query.append(" ORDER BY fromdate DESC ");
			return query.toString();		
		}
		
		public String addexhibitionsQuery(ExhibitionsModel obj)
		{		
			query=new StringBuffer();
			query.append("insert into exhibitions(exhibitionname, fromdate,todate,countryid,cityid,contactname,contactmobile,contactemail,"
					+ "transferdate,currencyid,currencyrate,discountaudience,discounttotal,prefix,serialnumber,meterreserver,meterprice,"
					+ "mandoob1,mandoob1visa,mandoob1ticket , mandoob2,mandoob2visa,mandoob2ticket,notes,isactive,tax) ");	
			
			query.append(" values('%s','%s','%s','%s','%s',  '%s' ,'%s','%s','%s','%s',  '%s','%s' ,'%s','%s','%s',  '%s','%s',"
					+ "  '%s','%s','%s','%s','%s',  '%s' ,'%s','%s','%s')");
			return query.toString().format(query.toString(), obj.getExhibitionname(),sdf.format(obj.getFromdate()) ,sdf.format(obj.getTodate()),
					obj.getCountryid(),obj.getCityid(),obj.getContactname(),obj.getContactmobile(),obj.getContactemail(),
					sdf.format(obj.getTransferdate()) , obj.getCurrencyid() , obj.getCurrencyrate(),obj.getDiscountaudience(),obj.getDiscounttotal(),
					obj.getPrefix(),obj.getSerialnumber(),obj.getMeterreserver(),obj.getMeterprice(),
					obj.getMandoob1(),obj.isMandoob1visa()?"1":"0" , obj.isMandoob1ticket()?"1" :0 , obj.getMandoob2(),obj.isMandoob2visa()?"1":"0",
							obj.isMandoob2ticket()?"1":"0" , obj.getNotes() , "1" , obj.getTax());		
		}	
		
		public String updatexhibitionsQuery(ExhibitionsModel obj)
		{		
			query=new StringBuffer();
			query.append("update exhibitions set exhibitionname ='%s' , fromdate= '%s',todate= '%s',countryid= '%s',cityid= '%s',contactname= '%s',contactmobile= '%s',contactemail= '%s',"
					+ "transferdate= '%s',currencyid= '%s',currencyrate= '%s',discountaudience= '%s',discounttotal= '%s',prefix= '%s',serialnumber= '%s',meterreserver= '%s',meterprice= '%s',"
					+ "mandoob1= '%s',mandoob1visa= '%s',mandoob1ticket= '%s' , mandoob2= '%s',mandoob2visa= '%s',mandoob2ticket= '%s',notes= '%s' ,tax='%s' ");	
			
			query.append(" where exhibitionid=" + obj.getExhibitionid());
			return query.toString().format(query.toString(), obj.getExhibitionname(),sdf.format(obj.getFromdate()) ,sdf.format(obj.getTodate()),
					obj.getCountryid(),obj.getCityid(),obj.getContactname(),obj.getContactmobile(),obj.getContactemail(),
					sdf.format(obj.getTransferdate()) , obj.getCurrencyid() , obj.getCurrencyrate(),obj.getDiscountaudience(),obj.getDiscounttotal(),
					obj.getPrefix(),obj.getSerialnumber(),obj.getMeterreserver(),obj.getMeterprice(),
					obj.getMandoob1(),obj.isMandoob1visa()?"1":"0" , obj.isMandoob1ticket()?"1" :0 , obj.getMandoob2(),obj.isMandoob2visa()?"1":"0",
							obj.isMandoob2ticket()?"1":"0" , obj.getNotes() ,obj.getTax());		
		}	
		
		//Agency
		public String getAgencyQuery(int exhibitionid)
		{
			query=new StringBuffer();
		    query.append("Select * from agency where exhibitionid= " + exhibitionid);
			return query.toString();		
		}
		
		
		public String addAgencyQuery(AgencyModel obj)
		{
			query=new StringBuffer();
			query.append("insert into agency(exhibitionid, agencyname,invoicenumber,invoiceamount,boxquantity,discountpercentage,discountaudience)"
					+ " values('%s','%s','%s','%s','%s','%s','%s')");				    
			return query.toString().format(query.toString(), obj.getExhibitionid(), obj.getAgencyname() , obj.getInvoicenumber(),
					obj.getInvoiceamount(),obj.getBoxquantity(),obj.getDiscountpercentage(),obj.getDiscountaudience());		
		}
		public String updateAgencyQuery(AgencyModel obj)
		{
			query=new StringBuffer();
			query.append("update agency set agencyname ='%s',invoicenumber='%s',invoiceamount='%s',boxquantity='%s',discountpercentage='%s',discountaudience='%s'"
					+ " where agencyid='%s'");				    
			return query.toString().format(query.toString(), obj.getAgencyname() , obj.getInvoicenumber(),
					obj.getInvoiceamount(),obj.getBoxquantity(),obj.getDiscountpercentage(),obj.getDiscountaudience(),obj.getAgencyid());		
		}
		
		public String updateExhibitionAgencyQuery(int exhibitionid)
		{
			query=new StringBuffer();
		    query.append("update agency set exhibitionid= " + exhibitionid + " where exhibitionid=0");
			return query.toString();		
		}
		
		
		public String deleteAgencyQuery(int agencyid)
		{
			query=new StringBuffer();
		    query.append("delete from agency where agencyid= " + agencyid);
			return query.toString();		
		}
		
		public String checkAgencyBooksQuery(int agencyid)
		{
			query=new StringBuffer();
		    query.append("Select * from importbook where agencyid= " + agencyid);
			return query.toString();		
		}
		
		//Contacts
		public String getExhibitionContactQuery(int exhibitionid)
		{
			query=new StringBuffer();
		    query.append("Select * from exhibitioncontact where exhibitionid= " + exhibitionid);
			return query.toString();		
		}
		
		public String addExhibitionContactQuery(ExhibitionContactModel obj)
		{
			query=new StringBuffer();
			query.append("insert into exhibitioncontact(exhibitionid, contactname,contactphone,email,note)"
					+ " values('%s','%s','%s','%s','%s')");				    
			return query.toString().format(query.toString(), obj.getExhibitionid(), obj.getContactname() , obj.getContactphone(),
					obj.getEmail(),obj.getNote());		
		}
		
		public String updateExhibitionContactQuery(ExhibitionContactModel obj)
		{
			query=new StringBuffer();
			query.append("update exhibitioncontact set contactname='%s',contactphone='%s',email='%s',note='%s' "
					+ " where contactid='%s'");				    
			return query.toString().format(query.toString(), obj.getContactname() , obj.getContactphone(),
					obj.getEmail(),obj.getNote() ,obj.getContactid());		
		}
		
		public String updateExhibitionContactQuery(int exhibitionid)
		{
			query=new StringBuffer();
		    query.append("update exhibitioncontact set exhibitionid= " + exhibitionid + " where exhibitionid=0");
			return query.toString();		
		}
		
		public String deleteExhibitionContactQuery(int contactid)
		{
			query=new StringBuffer();
		    query.append("delete from exhibitioncontact where contactid= " + contactid);
			return query.toString();		
		}
		
		public String updateCurrencySymbolQuery(ExhibitionsModel obj)
		{
			query=new StringBuffer();
		    query.append("update currency set currencysymbol='" + obj.getCurrencysymbol() + "' , rate='"+obj.getCurrencyrate() + "' where currencyid="+obj.getCurrencyid());
			return query.toString();		
		}
}
