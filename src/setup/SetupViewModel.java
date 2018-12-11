package setup;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.io.Files;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
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

public class SetupViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SetupData data=new SetupData();
	String viewType;
	private List<Users> lstUsers;
	private Users selectedUser;
	private ListModelList<CompanyRoles> lstCompanyRoles;
	private CompanyRoles selectedCompanyRole;
	
	private List<MenuModel> lstRoleMainMenu;
	private MenuGroupAdapter lstSubMenuGroup;
	
	private CompanySettingModel objCompanySetting;
	private List<ExhibitionsModel> lstExhibitions;
	private ExhibitionsModel objExhibitions;
	
	private List<CountryModel> lstCountry;
	private CountryModel selectedCountry;
	private List<CityModel> lstCity;
	private CityModel selectedCity;
	private List<CurrencyModel> lstCurrency;
	private CurrencyModel selectedCurrency;
	private List<AgencyModel> lstAgency;
	private AgencyModel selectedAgency;
	private List<ExhibitionContactModel> lstExhibitionContact;
	private ExhibitionContactModel selectedExhibitionContact;
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
			
		viewType=type==null?"":type;
		
		if(viewType.equals("users"))
		{
		lstUsers=data.getUsersList();
		selectedUser=lstUsers.get(0);
		}
		
		else if(viewType.equals("editusers"))
		{
			lstCompanyRoles=new ListModelList<CompanyRoles>(data.getCompanyRoles(1, ""));
			//Execution exec = Executions.getCurrent();
			//Map map = exec.getArg();
			if(map.get("selectedUser") !=null)
			{
				selectedUser= (Users) map.get("selectedUser");
				if(selectedUser.getRoleid()>0)
				{
				for (CompanyRoles item : lstCompanyRoles) 
				{
					if(item.getCompanyroleid()==selectedUser.getRoleid())
					{
						selectedCompanyRole=item;
					}
				}
			   }
			}
		}
		
		else if(viewType.equals("roles"))
		{
			lstCompanyRoles=new ListModelList<CompanyRoles>(data.getCompanyRoles(1, ""));
			selectedCompanyRole=lstCompanyRoles.get(0);
		}
		
		else if (viewType.equals("menus"))
		{
			lstCompanyRoles=new ListModelList<CompanyRoles>(data.getCompanyRoles(1, "Select"));
			selectedCompanyRole=lstCompanyRoles.get(0);
		}
		
		else if (viewType.equals("company"))
		{
			objCompanySetting=data.getCompanySetting();
			String filePath="";
			String repository = System.getProperty("catalina.home")+File.separator+"uploads"+File.separator;
			filePath=repository+objCompanySetting.getLogopath();
			objCompanySetting.setLogopath(filePath);
		}
		
		else if(viewType.equals("exhibitionslist"))
		{
			lstExhibitions=data.getExhibitionsList(0);
			lstCountry=data.getCountryList("اختار");
			selectedCountry=lstCountry.get(0);		
		}
		
		else if (viewType.equals("exhibitions"))
		{
			
			lstCountry=data.getCountryList("اختار");
			selectedCountry=lstCountry.get(0);
			lstCurrency=data.getCurrencyList("اختار");
			selectedCurrency=lstCurrency.get(0);
			Calendar c = Calendar.getInstance();
			if(map.get("selectedExhibitions")!=null)
			{
				objExhibitions=(ExhibitionsModel) map.get("selectedExhibitions");
				setDDLValues();
				lstAgency=data.getAgencyList("", objExhibitions.getExhibitionid());
				lstExhibitionContact=data.getExhibitionContactList("", objExhibitions.getExhibitionid());
			}
			else
			{
			objExhibitions=new ExhibitionsModel();
			objExhibitions.setExhibitionname("");
			objExhibitions.setContactname("");
			objExhibitions.setContactmobile("");
			objExhibitions.setContactemail("");
			objExhibitions.setPrefix("");
			objExhibitions.setMandoob1("");
			objExhibitions.setMandoob2("");
			objExhibitions.setNotes("");
			objExhibitions.setFromdate(df.parse(sdf.format(c.getTime())));
			objExhibitions.setTodate(df.parse(sdf.format(c.getTime())));
			objExhibitions.setTransferdate(df.parse(sdf.format(c.getTime())));			
			lstAgency=new ArrayList<AgencyModel>();
			lstExhibitionContact=new ArrayList<ExhibitionContactModel>();
			}
		}
		
		else if (viewType.equals("agency"))
		{
			selectedAgency=new AgencyModel();
			selectedAgency.setAgencyname("");
			selectedAgency.setInvoicenumber("");
			selectedAgency.setAddress("");
			selectedAgency.setTelephone("");
			selectedAgency.setInvoiceamount(0);
			selectedAgency.setBoxquantity(0);
			selectedAgency.setDiscountpercentage(0);
			if(map.get("selectedExhibitions")!=null)
			{
				objExhibitions=(ExhibitionsModel) map.get("selectedExhibitions");
				selectedAgency.setExhibitionid(objExhibitions.getExhibitionid());
			}
			if(map.get("selectedAgency")!=null)
			{
				selectedAgency=(AgencyModel) map.get("selectedAgency");
			}
			
		}
		
		else if (viewType.equals("contact"))
		{
			selectedExhibitionContact=new ExhibitionContactModel();			
			if(map.get("selectedExhibitions")!=null)
			{
				objExhibitions=(ExhibitionsModel) map.get("selectedExhibitions");
				selectedExhibitionContact.setExhibitionid(objExhibitions.getExhibitionid());
			}
			if(map.get("selectedContact")!=null)
			{
				selectedExhibitionContact=(ExhibitionContactModel) map.get("selectedContact");
			}
			
		}
		
		
		
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupViewModel---init-->" , ex);			
		}
		
	}
	
	public SetupViewModel()
	{
		
	}

	private void setDDLValues()
	{
		for (CountryModel item : lstCountry)
		{
			if(item.getCountryid()==objExhibitions.getCountryid())
			{
				selectedCountry=item;
				break;
			}
		}
		lstCity=data.getCityList("اختار", selectedCountry.getCountryid());
		for (CityModel item : lstCity) 
		{
			if(item.getCityid()==objExhibitions.getCityid())
			{
				selectedCity=item;
				break;
			}
		}
		for (CurrencyModel item : lstCurrency) 
		{
			if(item.getCurrencyid()==objExhibitions.getCurrencyid())
			{
				selectedCurrency=item;
				break;
			}
		}
		
		
	}
	@Command 
	@NotifyChange({"selectedUser","lstUsers"})
	public void activeCommand(@BindingParam("todo") Users todo)
	{	
		if(todo.isIsadmin())
		{
			Messagebox.show("You can't dis active account admin !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		if(todo.isIsactive())
		{
			data.updateUserStatus(todo.getUserid(), 0);
			todo.setIsactive(false);
		}
		else
		{
			data.updateUserStatus(todo.getUserid(), 1);
			todo.setIsactive(true);
		}					
	}
	
	@Command 
	@NotifyChange({"selectedUser","lstUsers"})
	public void deleteCommand(@BindingParam("todo") final Users todo)
	{	
		if(todo.isIsadmin())
		{
			Messagebox.show("You can't delete account admin !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}		
		
		Messagebox.show("هل أنت متأكد أنك تريد حذف هذا المستخدم?","حذف المسخدم ",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		{
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
	          {
					
					 data.deleteUser(todo.getUserid());					
					 lstUsers.remove(todo);	
					BindUtils.postNotifyChange(null, null, SetupViewModel.this, "lstUsers");

	          }
			 }
		 });							
	}
	
	@Command 
	public void addNewUserCommand()
	{
		try
		{
		selectedUser=new Users();
		selectedUser.setUserid(0);
		selectedUser.setRoleid(1);
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedUser", selectedUser);			
		Window window = (Window)Executions.createComponents("/setup/edituser.zul", null, arg);
		window.doModal();
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupViewModel---addNewUserCommand-->" , ex);			
		}
	}
	
	@Command 
	@NotifyChange({"selectedUser","lstUsers"})
	public void editUserCommand(@BindingParam("todo") Users todo)
	{		
		selectedUser=todo;
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedUser", selectedUser);			
		Window window = (Window)Executions.createComponents("/setup/edituser.zul", null, arg);
		window.doModal();
	}
	
	@Command 
	@NotifyChange({"selectedUser","lstUsers"})
	public void updateUserCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			selectedUser.setRoleid(selectedCompanyRole.getCompanyroleid());			
			data.updateUser(selectedUser);			
			Map args = new HashMap();
			BindUtils.postGlobalCommand(null, null, "refreshParent", args);							
			//refreshParent();		
			comp.detach();	
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupViewModel---updateUserCommand-->" , ex);			
		}
	}

	 @GlobalCommand 
	 @NotifyChange({"lstUsers"})	
	 public void refreshParent()
	 {
		 lstUsers=data.getUsersList();
		 selectedUser=lstUsers.get(0);
	 }
	
	 //roles
	 @Command
		@NotifyChange({"lstCompanyRoles"})
	    public void changeEditableStatus(@BindingParam("row") CompanyRoles item) 
		{
			if(item.getCompanyroleid()==0)
			{
				lstCompanyRoles.remove(item);
			}
			else
			{
			item.setEditingStatus(!item.isEditingStatus());
			}					
	    }
	 
	 @Command
	@NotifyChange({"lstCompanyRoles"})
	 public void saveRoleCommand(@BindingParam("row") CompanyRoles item)
	 {
		 try
		 {
			int result= data.updateCompanyRole(item);
			
			if(item.getCompanyroleid()==0)
			item.setCompanyroleid(result);
			
			item.setEditingStatus(!item.isEditingStatus());
			// changeEditableStatus(item);
		 }		 
		 catch (Exception ex) 
			{
				  logger.error("error in SetupViewModel---saveRoleCommand-->" , ex);			
			}
	 }
	 
	 @Command
		@NotifyChange({"lstCompanyRoles"})
		public void addNewRoleCommand()
		{
		 	for (CompanyRoles item : lstCompanyRoles) 
		 	{
				if(item.getCompanyroleid()==0)
				{
					Messagebox.show("save before","", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		 	
		 	CompanyRoles obj=new CompanyRoles();
		 	obj.setCompanyroleid(0);
		 	obj.setCompanyid(1);
		 	obj.setRolename("");
			obj.setEditingStatus(true);
		 	lstCompanyRoles.add(0,obj);
		}
	 
	 @Command 
     @NotifyChange({"lstCompanyRoles","lstUsers"})
	 public void deleteRoleCommand(@BindingParam("row") final CompanyRoles item)
	 {
		 int count=data.checkCompanyRolesIfUsedQuery(item.getCompanyroleid());
		 if(count>0)
			{
				Messagebox.show("You can't delete this role !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}		
			
			Messagebox.show("هل أنت متأكد أنك تريد حذف هذا الدور?","حذف الدور ",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
			{
				 public void onEvent(Event e)
				 {
					 if (Messagebox.ON_YES.equals(e.getName()))
		          {
						
						 data.deleteRole(item.getCompanyroleid());			
						 lstCompanyRoles.remove(item);	
						 BindUtils.postNotifyChange(null, null, SetupViewModel.this, "lstCompanyRoles");

		          }
				 }
			 });	
	 }
	 
	 
	 
	public List<Users> getLstUsers() {
		return lstUsers;
	}


	public void setLstUsers(List<Users> lstUsers) {
		this.lstUsers = lstUsers;
	}


	public Users getSelectedUser() {
		return selectedUser;
	}


	public void setSelectedUser(Users selectedUser) {
		this.selectedUser = selectedUser;
	}

	public ListModelList<CompanyRoles> getLstCompanyRoles() {
		return lstCompanyRoles;
	}

	public void setLstCompanyRoles(ListModelList<CompanyRoles> lstCompanyRoles) {
		this.lstCompanyRoles = lstCompanyRoles;
	}

	public CompanyRoles getSelectedCompanyRole() {
		return selectedCompanyRole;
	}
	  
	@NotifyChange({"lstSubMenuGroup"})
	public void setSelectedCompanyRole(CompanyRoles selectedCompanyRole) 
	{
		this.selectedCompanyRole = selectedCompanyRole;
		if(viewType.equals("menus"))
		{
			lstRoleMainMenu=data.getRoleMenuList(1);
			if(selectedCompanyRole.getCompanyroleid()>0)
			{
				List<MenuModel> lstRolesCred=data.getRoleCredentials(selectedCompanyRole.getCompanyroleid(),1);
				for (MenuModel mainMenu : lstRoleMainMenu)
				{
					for (MenuModel children : mainMenu.getChildren()) 
					{
						for (MenuModel role : lstRolesCred)
						{
							if(role.getMenuid()==children.getMenuid())
							{
								children.setCanView(role.isCanView());
								children.setCanModify(role.isCanModify());
								children.setCanDelete(role.isCanDelete());
								children.setCanAdd(role.isCanAdd());
								children.setCanExport(role.isCanExport());
								children.setCanPrint(role.isCanPrint());
								break;
							}
						}					
					}			
				}
			}
			lstSubMenuGroup=new MenuGroupAdapter(lstRoleMainMenu, new MenuComparator(), false);
		}
		
	}

	@Command 
	@NotifyChange({"lstSubMenuGroup"})
	public void checkAllAddCommand(@BindingParam("row")  MenuModel row ,@BindingParam("chk") boolean chk,@BindingParam("type") String type)
	{
		//Messagebox.show(item.getTitle());
		for (MenuModel item : lstRoleMainMenu)
		{
			if(item.getArtitle().equals(row.getArtitle()))
			{
			for (MenuModel children : item.getChildren()) 
			{
				if(type.equals("add"))
				children.setCanAdd(chk);
				else if(type.equals("edit"))
					children.setCanModify(chk);
				else if(type.equals("view"))
					children.setCanView(chk);
				else if(type.equals("delete"))
					children.setCanDelete(chk);
				else if(type.equals("export"))
					children.setCanExport(chk);
				else if(type.equals("print"))
					children.setCanPrint(chk);
			}
		  }
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstRoleMainMenu, new MenuComparator(), false);
	}
	
	@Command 
	@NotifyChange("lstMainUserMenu")
	public void saveUserMenuCommand()
	{
		if(selectedCompanyRole.getCompanyroleid()==0)
		{
			Messagebox.show("Please select a role !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		data.deleteUserRoleCredential(selectedCompanyRole.getCompanyroleid(),1);
		boolean isAccess=false;
		for (MenuModel item : lstRoleMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				if(children.isCanView() || children.isCanModify() || children.isCanDelete() || children.isCanAdd() || children.isCanExport() || children.isCanPrint())
					isAccess=true;
			}
			if(isAccess)
			{
				isAccess=false;
				//data.addParentUserMenuAccess(selectedUser.getUserid(), item);
				//data.addFullUserMenuAccess(selectedUser.getUserid(), item.getChildren());
				data.addMainMenuToRolesCredentials(1, selectedCompanyRole.getCompanyroleid(), item,1);
				data.addFullUserRolesCredentials(1, selectedCompanyRole.getCompanyroleid(), item.getChildren(),1);
			}
			 
		}
		
		Clients.showNotification("User access saved !! ");
	}
	
	@Command 
	@NotifyChange("objCompanySetting")
	public void uploadLogoCommand(BindContext ctx)
	{		
		try
		{
			//collectInfo();
			logger.info(Sessions.getCurrent().getWebApp().getServletContext().getContextPath());
			
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
			String filePath="";
			String fileFormat="";
			String repository = System.getProperty("catalina.home")+File.separator+"uploads"+File.separator;
			filePath=repository+event.getMedia().getName();
			
			filePath=Sessions.getCurrent().getWebApp().getServletContext().getContextPath()+"/WebContent/images/logo." + event.getMedia().getFormat();
			Files.copy(new File(filePath), event.getMedia().getStreamData());
			Messagebox.show("File Sucessfully uploaded in the path [ ."
	                 + filePath + " ]");
		
			objCompanySetting.setLogopath(filePath);
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupViewModel---uploadLogoCommand-->" , ex);			
		}
	}
	
	@Command 
	public void saveCompanySettingCommand()
	{
		try
		{
			data.updateCompanySetting(objCompanySetting);
			Clients.showNotification("تم حفظ الأعدادات !! ");
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupViewModel---saveCompanySettingCommand-->" , ex);			
		}
	}
	private void collectInfo() {
        StringBuilder result = new StringBuilder();
 
        try {
            result.append("--------------------------------------------------------------\r");
            result.append("ZK Session\r");
            Session sess = Sessions.getCurrent();
            result.append(".getLocalAddr():\t\t" + sess.getLocalAddr() + "\r");
            result.append(".getLocalName():\t\t" + sess.getLocalName() + "\r");
            result.append(".getRemoteAddr():\t\t" + sess.getRemoteAddr() + "\r");
            result.append(".getRemoteHost():\t\t" + sess.getRemoteHost() + "\r");
            result.append(".getServerName():\t\t" + sess.getServerName() + "\r");
            result.append(".getWebApp().getAppName():\t" + sess.getWebApp().getAppName() + "\r");
 
            HttpSession hses = (HttpSession) sess.getNativeSession();
            result.append("--------------------------------------------------------------------------------------------------\r");
            result.append("HttpSession\r");
            result.append(".getId():\t\t\t" + hses.getId() + "\r");
           // result.append(".getCreationTime():\t\t" + new Date(hses.getCreationTime()).toString() + "\r");
           // result.append(".getLastAccessedTime():\t\t" + new Date(hses.getLastAccessedTime()).toString() + "\r");
 
            result.append("--------------------------------------------------------------------------------------------------\r");
            result.append("ServletContext\r");
            ServletContext sCon = hses.getServletContext();
            result.append(".getServerInfo():\t\t" + sCon.getServerInfo() + "\r");
            result.append(".getContextPath():\t\t" + sCon.getContextPath() + "\r");
            result.append(".getServletContextName():\t" + sCon.getServletContextName() + "\r");
 
            result.append("--------------------------------------------------------------------------------------------------\r");
            result.append("ZK Executions\r");
            result.append(".getHeader('user-agent'):\t" + Executions.getCurrent().getHeader("user-agent") + "\r");
            result.append(".getHeader('accept-language'):\t" + Executions.getCurrent().getHeader("accept-language") + "\r");
            result.append(".getHeader('referer'):\t\t" + Executions.getCurrent().getHeader("referer") + "\r");
            result.append(".getHeader('connection'):\t" + Executions.getCurrent().getHeader("connection") + "\r");
            result.append(".getHeader('zk-sid'):\t\t" + Executions.getCurrent().getHeader("zk-sid") + "\r");
            result.append(".getHeader('origin'):\t\t" + Executions.getCurrent().getHeader("origin") + "\r");
            result.append(".getHeader('host'):\t\t" + Executions.getCurrent().getHeader("host") + "\r");
            result.append(".getHeader('cookie'):\t\t" + Executions.getCurrent().getHeader("cookie") + "\r");
            result.append("--------------------------------------------------------------------------------------------------\r");
 
            logger.info(result.toString());

            // result.append(clientInfo);
           // txtSessionInfo.setValue(result.toString());
 
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

	@Command 
	public void addNewExhibitionsCommand()
	{
		Map<String, Object> arg = new HashMap<String, Object>();	
		//arg.put("selectedExhibitions", objExhibitions);		
		Window window = (Window)Executions.createComponents("/setup/exhibitions.zul", null, arg);
		window.doModal();
	}
	
	@Command 
	public void editExhibitionsCommand(@BindingParam("todo") ExhibitionsModel todo)
	{
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedExhibitions", todo);		
		Window window = (Window)Executions.createComponents("/setup/exhibitions.zul", null, arg);
		window.doModal();
	}
	
	@Command
	public void deleteExhibitionsCommand()
	{
		Messagebox.show("Stopped delete now !! ");
	}
	
	@Command 
	@NotifyChange({"objExhibitions","lstExhibitions"})
	public void activeExhibitionsCommand(@BindingParam("todo") ExhibitionsModel todo)
	{			
		if(todo.isIsactive())
		{
			data.updateExhibitionsStatus(todo.getExhibitionid(), 0);
			todo.setIsactive(false);
		}
		else
		{
			data.updateExhibitionsStatus(todo.getExhibitionid(), 1);
			todo.setIsactive(true);
		}					
	}
	
	@Command 
	public void saveExhibitionsCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			if(objExhibitions.getExhibitionname().equals(""))
			{
				Messagebox.show("!! يرجى إدخال اسم المعرض ","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			objExhibitions.setCountryid(selectedCountry.getCountryid());
			objExhibitions.setCityid(selectedCity.getCityid());
			objExhibitions.setCurrencyid(selectedCurrency.getCurrencyid());	
			objExhibitions.setCurrencysymbol(selectedCurrency.getCurrencysymbol());		
			objExhibitions.setCurrencyrate(selectedCurrency.getRate());
			
			data.addexhibitions(objExhibitions);
			Clients.showNotification("Exhibitions Setting saved !! ");
			BindUtils.postGlobalCommand(null, null, "refreshExhibitionsList", null);	
			comp.detach();
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupViewModel---saveExhibitionsCommand-->" , ex);			
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"lstExhibitions"})	
	 public void refreshExhibitionsList()
	 {
		lstExhibitions=data.getExhibitionsList(selectedCountry.getCountryid());
	 }
	
	@Command
	public void addAgencyCommand()
	{
		
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedExhibitions", objExhibitions);		
		Window window = (Window)Executions.createComponents("/setup/agency.zul", null, arg);
		window.doModal();
	}
	
	@Command
	public void saveAgencyCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try		
		{
			if(selectedAgency.getAgencyname().equals(""))
			{
				Messagebox.show("!! يرجى إدخال اسم الوكالة ","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			data.addAgency(selectedAgency);
			Map args = new HashMap();
			args.put("selectedExhibitions", objExhibitions);
			BindUtils.postGlobalCommand(null, null, "refreshAgency", args);	
			comp.detach();
		}
		catch (Exception ex) 
		{
			  logger.error("error in SetupViewModel---saveAgencyCommand-->" , ex);			
		}
	}
	
	 @GlobalCommand 
	 @NotifyChange({"lstAgency","lstExhibitionContact"})	
	 public void refreshAgency(@BindingParam("selectedExhibitions")ExhibitionsModel objExhibitions)
	 {
		lstAgency=data.getAgencyList("", objExhibitions.getExhibitionid());
		lstExhibitionContact=data.getExhibitionContactList("", objExhibitions.getExhibitionid());
	 }
	 
	 @Command
	 @NotifyChange({"lstAgency"})
	 public void deleteSelectedAgencyCommand(@BindingParam("row") AgencyModel todo)
	 {
		 //check if has importbook records before
		 int count=data.checkAgencyBooks(todo.getAgencyid());
		 if(count>0)
		 {
			 Messagebox.show("You cant delete this agency has " + count + " imported books !! ","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;			 
		 }
		 data.deleteAgency(todo.getAgencyid());
		 lstAgency=data.getAgencyList("", objExhibitions.getExhibitionid());				
	 }
	 @Command
	 @NotifyChange({"lstAgency"})
	 public void editSelectedAgencyCommand(@BindingParam("row") AgencyModel todo)
	 {
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedExhibitions", objExhibitions);	
		arg.put("selectedAgency", todo);
		Window window = (Window)Executions.createComponents("/setup/agency.zul", null, arg);
		window.doModal();
	 }
	 
	//Contacts
	 
	 @Command
	 public void addContactCommand()
	 {
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedExhibitions", objExhibitions);		
		Window window = (Window)Executions.createComponents("/setup/contact.zul", null, arg);
		window.doModal();
	 }
	 @Command
	 public void saveContactCommand(@ContextParam(ContextType.VIEW) Window comp)
	 {
		 try		
			{
				data.addExhibitionContact(selectedExhibitionContact);
				Map args = new HashMap();
				args.put("selectedExhibitions", objExhibitions);
				BindUtils.postGlobalCommand(null, null, "refreshAgency", args);	
				comp.detach();
			}
			catch (Exception ex) 
			{
				  logger.error("error in SetupViewModel---saveContactCommand-->" , ex);			
			}
	 }
	 
	 @Command
	 @NotifyChange({"lstExhibitionContact"})
	 public void deleteSelectedContactCommand(@BindingParam("row") ExhibitionContactModel todo)
	 {
		 data.deleteExhibitionContact(todo.getContactid());
		 lstExhibitionContact=data.getExhibitionContactList("", objExhibitions.getExhibitionid());
	 }
	 
	 @Command
	 @NotifyChange({"lstExhibitionContact"})
	 public void editSelectedContactCommand(@BindingParam("row") ExhibitionContactModel todo)
	 {
		 Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedExhibitions", objExhibitions);	
		arg.put("selectedContact", todo);
		Window window = (Window)Executions.createComponents("/setup/contact.zul", null, arg);
		window.doModal();
	 }
	 
	public List<MenuModel> getLstRoleMainMenu() {
		return lstRoleMainMenu;
	}

	public void setLstRoleMainMenu(List<MenuModel> lstRoleMainMenu) {
		this.lstRoleMainMenu = lstRoleMainMenu;
	}
	
	public MenuGroupAdapter getLstSubMenuGroup() {
		return lstSubMenuGroup;
	}

	public void setLstSubMenuGroup(MenuGroupAdapter lstSubMenuGroup) {
		this.lstSubMenuGroup = lstSubMenuGroup;
	}

	public CompanySettingModel getObjCompanySetting() {
		return objCompanySetting;
	}

	public void setObjCompanySetting(CompanySettingModel objCompanySetting) {
		this.objCompanySetting = objCompanySetting;
	}

	public ExhibitionsModel getObjExhibitions() {
		return objExhibitions;
	}

	public void setObjExhibitions(ExhibitionsModel objExhibitions) {
		this.objExhibitions = objExhibitions;
	}

	public List<CountryModel> getLstCountry() {
		return lstCountry;
	}

	public void setLstCountry(List<CountryModel> lstCountry) {
		this.lstCountry = lstCountry;
	}

	public List<CityModel> getLstCity() {
		return lstCity;
	}

	public void setLstCity(List<CityModel> lstCity) {
		this.lstCity = lstCity;
	}

	public CountryModel getSelectedCountry() {
		return selectedCountry;
	}

	@NotifyChange({"lstCity","selectedCity","lstExhibitions"})
	public void setSelectedCountry(CountryModel selectedCountry) 
	{
		this.selectedCountry = selectedCountry;
		if(viewType.equals("exhibitionslist"))
		{
			lstExhibitions=data.getExhibitionsList(selectedCountry.getCountryid());
		}
		lstCity=data.getCityList("اختار", selectedCountry.getCountryid());		
		selectedCity=lstCity.get(0);
	}

	public CityModel getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(CityModel selectedCity) {
		this.selectedCity = selectedCity;
	}

	public List<CurrencyModel> getLstCurrency() {
		return lstCurrency;
	}

	public void setLstCurrency(List<CurrencyModel> lstCurrency) {
		this.lstCurrency = lstCurrency;
	}

	public CurrencyModel getSelectedCurrency() {
		return selectedCurrency;
	}

	public void setSelectedCurrency(CurrencyModel selectedCurrency) {
		this.selectedCurrency = selectedCurrency;
	}

	public AgencyModel getSelectedAgency() {
		return selectedAgency;
	}

	public void setSelectedAgency(AgencyModel selectedAgency) {
		this.selectedAgency = selectedAgency;
	}

	public List<AgencyModel> getLstAgency() {
		return lstAgency;
	}

	public void setLstAgency(List<AgencyModel> lstAgency) {
		this.lstAgency = lstAgency;
	}

	public List<ExhibitionsModel> getLstExhibitions() {
		return lstExhibitions;
	}

	public void setLstExhibitions(List<ExhibitionsModel> lstExhibitions) {
		this.lstExhibitions = lstExhibitions;
	}

	public List<ExhibitionContactModel> getLstExhibitionContact() {
		return lstExhibitionContact;
	}

	public void setLstExhibitionContact(List<ExhibitionContactModel> lstExhibitionContact) {
		this.lstExhibitionContact = lstExhibitionContact;
	}

	public ExhibitionContactModel getSelectedExhibitionContact() {
		return selectedExhibitionContact;
	}

	public void setSelectedExhibitionContact(ExhibitionContactModel selectedExhibitionContact) {
		this.selectedExhibitionContact = selectedExhibitionContact;
	}
}
