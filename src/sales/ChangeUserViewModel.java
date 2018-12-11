package sales;

import java.util.HashMap;
import java.util.Map;

import login.LoginData;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import domains.InvoiceModel;
import domains.Users;

public class ChangeUserViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	CashInvoiceData data=new CashInvoiceData();
	String viewType;
	private Users objChangeUser;
	private String type;
	
	@Init
    public void init(@BindingParam("type") String formtype)
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();			
			viewType=formtype==null?"":formtype;
			objChangeUser=new Users();
			type=(String) map.get("type");
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in ChangeUserViewModel---init-->" , ex);			
		}
		
	}
	
	@Command
	public void applyChangeUserCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
		LoginData data=new LoginData();		
		Users objUser= data.getUserLogin(objChangeUser);
		if(objUser!=null)
		{			
			Sessions.getCurrent().setAttribute("Authentication", objUser);	
			Map args = new HashMap();			
			args.put("objChangeUser", objUser);		
			if(type.equals("cash"))
			BindUtils.postGlobalCommand(null, null, "refreshUserParent", args);
			else if(type.equals("return"))
				BindUtils.postGlobalCommand(null, null, "refreshReturnUserParent", args);
			
			comp.detach();			
		}
		else
		{
			Messagebox.show("Wrong..");
		}
		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in ChangeUserViewModel ----> applyChangeUserCommand", ex);
		}
	}


	public Users getObjChangeUser() {
		return objChangeUser;
	}


	public void setObjChangeUser(Users objChangeUser) {
		this.objChangeUser = objChangeUser;
	}
}
