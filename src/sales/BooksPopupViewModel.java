package sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import domains.BooksModel;
import domains.DataFilter;
import domains.ExhibitionsModel;

public class BooksPopupViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private List<BooksModel> lstBooks;
	private List<BooksModel> lstAllBooks;
	private DataFilter filter=new DataFilter();
	private String bookName;
	private String type;
	private ExhibitionsModel objExhibtion;
	private Set<BooksModel> selectedBooks;
	
	public BooksPopupViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			logger.info(map.keySet().toString());
			type=(String) map.get("type");
			bookName=(String) map.get("bookName");
			objExhibtion=(ExhibitionsModel) map.get("objExhibtion");
			lstBooks= (List<BooksModel>) map.get("lstBooks");	
			lstAllBooks=lstBooks;
			selectedBooks=new HashSet<BooksModel>();
			if(bookName.equals(""))
			{
			
			}
			else
			{
				filter.setBookCode(bookName);
				changeFilter();
			}
			
		}
		catch(Exception ex)
		{
			logger.error("ERROR in BooksPopupViewModel ----> init", ex);
		}
	}

	@Command
    @NotifyChange({"lstBooks"})
    public void changeFilter() 
    {	      
		lstBooks=filterData();
	  
    }
	
	private List<BooksModel> filterData()
	{
		lstBooks=lstAllBooks;
		List<BooksModel>  lst=new ArrayList<BooksModel>();
		for (Iterator<BooksModel> i = lstBooks.iterator(); i.hasNext();)
		{
			BooksModel tmp=i.next();				
			if(tmp.getBookCode().toLowerCase().contains(filter.getBookCode().toLowerCase())&&
					tmp.getBookName().toLowerCase().contains(filter.getBookName().toLowerCase())&&
					tmp.getAuthor().toLowerCase().contains(filter.getBookAuthor().toLowerCase())&&
					tmp.getEditor().toLowerCase().contains(filter.getBookEditor().toLowerCase())
					)					
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	
	@Command
	public void selectdBookCommand(@ContextParam(ContextType.VIEW) Window comp,@BindingParam("row") BooksModel row)
	{
		Map args = new HashMap();		
		args.put("selectedBook", row);
		args.put("objExhibtion", objExhibtion);
		args.put("lstSelectedBooks", null);
		if(type.equals("cash"))
		BindUtils.postGlobalCommand(null, null, "refreshBooksParent", args);
		else if(type.equals("return"))
		BindUtils.postGlobalCommand(null, null, "refreshReturnBooksParent", args);	
		comp.detach();
	}
	
	@Command
	public void onOkCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		if(lstBooks.size()==1)
		{
		Map args = new HashMap();		
		args.put("selectedBook", lstBooks.get(0));
		args.put("objExhibtion", objExhibtion);
		args.put("lstSelectedBooks", null);
		if(type.equals("cash"))
			BindUtils.postGlobalCommand(null, null, "refreshBooksParent", args);
			else if(type.equals("return"))
			BindUtils.postGlobalCommand(null, null, "refreshReturnBooksParent", args);	
		
		comp.detach();
		}
	}
	
	@Command
	public void selectBooksCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			List<BooksModel> lstSelectedBooks=new ArrayList<BooksModel>();
			if(selectedBooks!=null)
			{
				for (BooksModel item : selectedBooks)
				{
					lstSelectedBooks.add(item);
				}
			}
			
			if(lstSelectedBooks.size()==0)
			{
				Messagebox.show("select book first !!.","Books", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			Map args = new HashMap();		
			args.put("selectedBook", null);
			args.put("objExhibtion", objExhibtion);
			args.put("lstSelectedBooks", lstSelectedBooks);
						
			if(type.equals("cash"))
				BindUtils.postGlobalCommand(null, null, "refreshBooksParent", args);
				else if(type.equals("return"))
				BindUtils.postGlobalCommand(null, null, "refreshReturnBooksParent", args);	
			
			comp.detach();
		}
		
		catch(Exception ex)
		{
			logger.error("ERROR in BooksPopupViewModel ----> selectBooksCommand", ex);
		}
	}
	
	public List<BooksModel> getLstBooks() {
		return lstBooks;
	}

	public void setLstBooks(List<BooksModel> lstBooks) {
		this.lstBooks = lstBooks;
	}


	public DataFilter getFilter() {
		return filter;
	}


	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Set<BooksModel> getSelectedBooks() {
		return selectedBooks;
	}

	public void setSelectedBooks(Set<BooksModel> selectedBooks) {
		this.selectedBooks = selectedBooks;
	}
}
