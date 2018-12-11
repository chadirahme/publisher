package login;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import common.ArabicToEnglish;

import domains.Users;

public class AuthenticateViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private String message;
	private int x=0;
	private Users dbUser;
	private String arabicText;
	private String englishText;
	
	/*@Init
    public void init()
	{
		try
		{
			//logger.info("AuthenticateViewModel >>> Init");		
			dbUser=new Users();
			//dbUser.setUsername("c");
			//dbUser.setUserpassword("1");
			
			dbUser.setUsername("");
			dbUser.setUserpassword("");
			logger.info("init" + dbUser.getUsername());
			
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in AuthenticateViewModel ----> init", ex);
		}
	}*/
	
	public AuthenticateViewModel()
	{
		try
		{
			//logger.info("AuthenticateViewModel >>> Init");		
			dbUser=new Users();
			//dbUser.setUsername("c");
			//dbUser.setUserpassword("1");
			
			//dbUser.setUsername("");
			//dbUser.setUserpassword("");
			//logger.info("load" + dbUser.getUsername());
			
			arabicText="";
			englishText="";
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in AuthenticateViewModel ----> init", ex);
		}
	}
	
	@Command	
	@NotifyChange({"message","dbUser"})
	public void loginCommand()
	{
		try
		{
			message="";
			LoginData data=new LoginData();
			//dbUser=new Users();
			//dbUser.setUsername("chadi");
			//dbUser.setUserpassword("123");
			Users objUser= data.getUserLogin(dbUser);
			if(objUser!=null)
			{
				
				Sessions.getCurrent().setAttribute("Authentication", objUser);
				//List<Users> temp = DAO.criteria(Users.class).list();
				logger.info("loginCommand >>> GO" + objUser.getUseremail());
				message="done11";
				Executions.sendRedirect("index.zul");
			}
			else
			{
				message="اسم المستخدم أو كلمة المرور خاطئة";
			}
		
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in AuthenticateViewModel ----> loginCommand", ex);
		}
	}

	@Command
	public void logout()
	{
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("Authentication");	
		Executions.sendRedirect("/login.zul");
	}
	
	@Command
	@NotifyChange({"englishText"})
	public void convertCommand()
	{
		ArabicToEnglish conv=new ArabicToEnglish();
		englishText=conv.convertToEnglishText(arabicText).toUpperCase();		
	}
	
	@Command
	@NotifyChange({"englishText","arabicText"})
	public void clearTextCommand()
	{
		arabicText="";
		englishText="";
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Users getDbUser() {
		return dbUser;
	}

	public void setDbUser(Users dbUser) {
		this.dbUser = dbUser;
	}

	public String getArabicText() {
		return arabicText;
	}

	public void setArabicText(String arabicText) {
		this.arabicText = arabicText;
	}

	public String getEnglishText() {
		return englishText;
	}

	public void setEnglishText(String englishText) {
		this.englishText = englishText;
	}
}
