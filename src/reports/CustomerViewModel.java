package reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.omg.CORBA.CustomMarshal;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import sales.CashInvoiceData;
import setup.SetupData;
import domains.CityModel;
import domains.CountryModel;
import domains.CurrencyModel;
import domains.CustomerModel;
import domains.ExhibitionsModel;
import domains.Users;

public class CustomerViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	String viewType;
	ReportsData data=new ReportsData();
	SetupData sdata=new SetupData();
	private List<CustomerModel> lstCustomers;
	private CustomerModel selectedCustomer;
	private ExhibitionsModel objExhibtion;
	private List<CountryModel> lstCountry;
	private CountryModel selectedCountry;
	private List<CityModel> lstCity;
	private CityModel selectedCity;
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		viewType=type==null?"":type;
		
		
		if(viewType.equals("list"))
		{
			lstCustomers=data.getCustomerList(0);
			lstCountry=sdata.getCountryList("اختار");
			selectedCountry=lstCountry.get(0);	
		}
		
		else if(viewType.equals("editcustomer"))
		{
			
			lstCountry=sdata.getCountryList("اختار");
			selectedCountry=lstCountry.get(0);		
			selectedCustomer=(CustomerModel) map.get("selectedCustomer");
			
			if(map.get("objExhibtion")!=null)
			{
				//in case add
				objExhibtion=(ExhibitionsModel) map.get("objExhibtion");
				selectedCustomer.setCountryid(objExhibtion.getCountryid());
				selectedCustomer.setCityid(objExhibtion.getCityid());
				setDDLValues();
			}
			
			else
			{	//in case edit	
				if(selectedCustomer.getCustomerid()>0)
				setDDLValues();
			}
		}
		
		}
		catch (Exception ex) 
		{
			  logger.error("error in CustomerViewModel---init-->" , ex);			
		}
		
	}
	
	private void setDDLValues()
	{
		for (CountryModel item : lstCountry)
		{
			if(item.getCountryid()==selectedCustomer.getCountryid())
			{
				selectedCountry=item;
				break;
			}
		}
		lstCity=sdata.getCityList("اختار", selectedCountry.getCountryid());
		for (CityModel item : lstCity) 
		{
			if(item.getCityid()==selectedCustomer.getCityid())
			{
				selectedCity=item;
				break;
			}
		}		
		
	}
	
	@Command
	public void addNewCustomerCommand()
	{
		try
		{
			CashInvoiceData cashdata=new CashInvoiceData();
			objExhibtion=cashdata.getActiveExhibtions();
			
			selectedCustomer=new CustomerModel();
			selectedCustomer.setExhibitionid(objExhibtion.getExhibitionid());
			Map<String, Object> arg = new HashMap<String, Object>();	
			arg.put("selectedCustomer", selectedCustomer);
			arg.put("objExhibtion", objExhibtion);	
			Window window = (Window)Executions.createComponents("/reports/editcustomer.zul", null, arg);
			window.doModal();
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in CustomerViewModel---addNewCustomerCommand-->" , ex);			
		}
		
	}
	
	@Command
	public void editCustomerCommand(@BindingParam("todo") CustomerModel todo)
	{
		try
		{
		selectedCustomer=todo;
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedCustomer", selectedCustomer);
		//arg.put("objExhibtion", objExhibtion);	
		Window window = (Window)Executions.createComponents("/reports/editcustomer.zul", null, arg);
		window.doModal();
		}
		catch (Exception ex) 
		{
			  logger.error("error in CustomerViewModel---editCustomerCommand-->" , ex);			
		}
	}
	
	@Command
	public void updateCustomerCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			if(selectedCountry.getCountryid()==0)
			{
				Messagebox.show("يرجى اختيار الدولة !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(selectedCity.getCityid()==0)
			{
				Messagebox.show("الرجاء اختيار المدينة !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			selectedCustomer.setCountryid(selectedCountry.getCountryid());
			selectedCustomer.setCityid(selectedCity.getCityid());
			data.updateCustomer(selectedCustomer);
			Map args = new HashMap();
			BindUtils.postGlobalCommand(null, null, "refreshCustomerReportParent", args);							
			//refreshParent();		
			comp.detach();	
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in CustomerViewModel---updateCustomerCommand-->" , ex);			
		}
	}
	
	@GlobalCommand 
	 @NotifyChange({"lstCustomers"})	
	 public void refreshCustomerReportParent()
	 {
		 lstCustomers=data.getCustomerList(0);
		 selectedCustomer=lstCustomers.get(selectedCountry.getCountryid());
	 }
	
	@Command
	@NotifyChange({"lstCustomers"})
	public void deleteCommand(@BindingParam("todo") CustomerModel todo)
	{
		int count=data.checkCustomersInvoices(todo.getCustomerid());
		if(count>0)
		{
			Messagebox.show("You cant delete this customer has " + count + " invoices !! ","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		else
		{
			data.deleteCustomer(todo.getCustomerid());
			 lstCustomers=data.getCustomerList(0);
		}
	}

	public List<CustomerModel> getLstCustomers() {
		return lstCustomers;
	}

	public void setLstCustomers(List<CustomerModel> lstCustomers) {
		this.lstCustomers = lstCustomers;
	}

	public CustomerModel getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(CustomerModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public List<CountryModel> getLstCountry() {
		return lstCountry;
	}

	public void setLstCountry(List<CountryModel> lstCountry) {
		this.lstCountry = lstCountry;
	}

	public CountryModel getSelectedCountry() {
		return selectedCountry;
	}

	@NotifyChange({"lstCity","selectedCity","lstCustomers"})
	public void setSelectedCountry(CountryModel selectedCountry) 
	{
		this.selectedCountry = selectedCountry;
		if(viewType.equals("list"))
		{
			lstCustomers=data.getCustomerList(selectedCountry.getCountryid());
		}
		else
		{
		lstCity=sdata.getCityList("اختار", selectedCountry.getCountryid());		
		selectedCity=lstCity.get(0);
		}
	}

	public List<CityModel> getLstCity() {
		return lstCity;
	}

	public void setLstCity(List<CityModel> lstCity) {
		this.lstCity = lstCity;
	}

	public CityModel getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(CityModel selectedCity) 
	{
		this.selectedCity = selectedCity;
	}
}
