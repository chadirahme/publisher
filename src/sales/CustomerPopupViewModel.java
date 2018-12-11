package sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import reports.ReportsData;
import domains.CustomerModel;
import domains.DataFilter;
import domains.InvoiceModel;

public class CustomerPopupViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private List<CustomerModel> lstCustomers;
	private List<CustomerModel> lstAllCustomers;
	private CustomerModel selectedCustomer;
	ReportsData data=new ReportsData();
	private DataFilter filter=new DataFilter();
	private InvoiceModel objCashInvoice;
	
	public CustomerPopupViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();			
			Map map = exec.getArg();
			logger.info(map.keySet().toString());
			
			objCashInvoice=(InvoiceModel) map.get("objCashInvoice");
			
			lstCustomers=data.getCustomerList(0);
			lstAllCustomers=lstCustomers;
		}
		catch(Exception ex)
		{
			logger.error("ERROR in CustomerPopupViewModel ----> init", ex);
		}
	}
	
	@Command
    @NotifyChange({"lstCustomers"})
    public void changeFilter() 
    {	      
		lstCustomers=filterData();
	  
    }
	
	private List<CustomerModel> filterData()
	{
		lstCustomers=lstAllCustomers;
		List<CustomerModel>  lst=new ArrayList<CustomerModel>();
		for (Iterator<CustomerModel> i = lstCustomers.iterator(); i.hasNext();)
		{
			CustomerModel tmp=i.next();				
			if(tmp.getCustomername().toLowerCase().contains(filter.getCustomerName().toLowerCase())&&
					tmp.getMobile().toLowerCase().contains(filter.getMobile().toLowerCase())					
					)					
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	
	@Command
	public void selectdCustomerCommand(@ContextParam(ContextType.VIEW) Window comp,@BindingParam("row") CustomerModel row)
	{
		Map args = new HashMap();		
		args.put("selectedCustomer", row);	
		args.put("objCashInvoice", objCashInvoice);
		
		BindUtils.postGlobalCommand(null, null, "refreshCustomerParent", args);	
		comp.detach();
	}
	
	@Command
	public void onOkCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		if(lstCustomers.size()==1)
		{
		Map args = new HashMap();		
		args.put("selectedCustomer", lstCustomers.get(0));
		args.put("objCashInvoice", objCashInvoice);
		BindUtils.postGlobalCommand(null, null, "refreshCustomerParent", args);	
		comp.detach();
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


	public DataFilter getFilter() {
		return filter;
	}


	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
}
