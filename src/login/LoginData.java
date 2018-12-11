package login;

import java.sql.ResultSet;

import org.apache.log4j.Logger;



import db.DBHandler;
import domains.Users;

public class LoginData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Users getUserLogin(Users objLogin)
	{
		Users dbUser=null;
		
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
	 	try{
	 		
	 		String sql="Select * from users " +
	 				" where isactive=1 and  username ='" + objLogin.getUsername() +"' and userpassword='" + objLogin.getUserpassword() + "'";
	 		rs = db.executeNonQuery(sql);	
	 		
	 		while (rs.next())
	 		{
	 			dbUser=new Users();
	 			dbUser.setUserid(rs.getInt("userid"));
	 			dbUser.setUsername(rs.getString("username"));
	 			dbUser.setFirstname(rs.getString("firstname"));
	 			dbUser.setRoleid(rs.getInt("roleid"));	 
	 			dbUser.setIsadmin(rs.getBoolean("isadmin"));
	 			dbUser.setUseremail(rs.getString("useremail")==null?"":rs.getString("useremail"));	 
	 			dbUser.setInvoiceFooter(getInvoiceFooter());
	 			dbUser.setExhibitionname(getActiveExibtionName());	 			
	 		}
	 		
	 	}
	 	catch (Exception ex) 
	 		{		 	  
	 		 logger.error("error in LoginData---getUserLogin-->" , ex);	
		 	 }
	 	finally{		 	  
		 	   return dbUser;
		 } 	 			
	}
	
	private String getInvoiceFooter()
	{
		String footer="";
		try
		{
			DBHandler db = new DBHandler();
			ResultSet rs = null;
			String sql="Select * from companysetting "; 
	 		rs = db.executeNonQuery(sql);	
	 		while(rs.next())
	 		{
	 			footer=rs.getString("invoicefooter")==null?"":rs.getString("invoicefooter");
	 		}
		}
		catch (Exception ex) 
 		{		 	  
 		 logger.error("error in LoginData---getInvoiceFooter-->" , ex);	
	 	 }
		return footer;
	}
	
	private String getActiveExibtionName()
	{
		String exhibitionname="";
		try
		{
			DBHandler db = new DBHandler();
			ResultSet rs = null;
			String sql="Select * from exhibitions where isactive=1 "; 			
	 		rs = db.executeNonQuery(sql);	
	 		while(rs.next())
	 		{
	 			exhibitionname=rs.getString("exhibitionname");
	 		}
		}
		catch (Exception ex) 
 		{		 	  
 		 logger.error("error in LoginData---getActiveExibtionName-->" , ex);	
	 	 }
		return exhibitionname;
	}
	
}
