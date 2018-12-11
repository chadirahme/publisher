package layout;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import domains.MenuModel;
import domains.Users;

public class Pagecomposer extends SelectorComposer 
{
	private Logger logger = Logger.getLogger(this.getClass());
	MenuData mData=new MenuData();
	private List<MenuModel> lstListsMenu;
	private String language;
	Users dbUser=null;
	int companyroleid;
	Label LastlblItem;
	
	@Wire
	Tabbox mainContentTabbox;
	
	@Wire
	Tabs contentTabs;
	
	@Wire
	Tabpanels contentTabpanels;
	
	public void doAfterCompose(Component comp) throws Exception 
	 {		    
		   super.doAfterCompose(comp);	
	 }
	
	@Init
    public void init()
	{
		language="ar";
		Session sess = Sessions.getCurrent();	
		dbUser=(Users)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			companyroleid=dbUser.getRoleid();
		}
		else
		{
			Executions.sendRedirect("login.zul");
		}
	}
	
	public Pagecomposer()
	{
		
	}
	
	@Command	
	public void menuHRClicked(@BindingParam("pagename")  MenuModel item,@BindingParam("label") Label lblItem)
	{
		if(LastlblItem!=null)
		{
			LastlblItem.setSclass("defaultMenu");
		}
		if(lblItem!=null)
		{
		lblItem.setSclass("selectedMenu");
		LastlblItem=lblItem;
		}
		
		Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
		Center center = bl.getCenter();
		
		Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				
		Tabs contentTabs=(Tabs)tabbox.getFellow("contentTabs");
		Tabpanels contentTabpanels=(Tabpanels)tabbox.getFellow("contentTabpanels");
		
		for (Component oldTab : contentTabs.getChildren()) 
		{
			if(oldTab instanceof Tab)
			{
				if(language.equals("en"))
				{
				if( ((Tab)oldTab).getLabel().equals(item.getTitle()))
				{
					((Tab) oldTab).setSelected(true);
					return;
				}
					
				}
				else
				{
				if( ((Tab)oldTab).getLabel().equals(item.getArtitle()))
				{
					((Tab) oldTab).setSelected(true);
					return;
				}
					
				}
				
			}
		}
		Tab newTab = new Tab();
		if(language.equals("en"))
		newTab.setLabel(item.getTitle());
		else
		newTab.setLabel(item.getArtitle());	
		
		newTab.setClosable(true);
		Tabpanel newTabpanel = new Tabpanel();
		Include incContentPage = new Include();
		incContentPage.setSrc(item.getHref());
		incContentPage.setParent(newTabpanel);
		newTabpanel.setParent(contentTabpanels);
		newTab.setParent(contentTabs);
		newTab.setSelected(true);
		newTab.setVflex("1");
			
		/*
		center.getChildren().clear();	
		if(!item.getHref().equals(""))
		Executions.createComponents(item.getHref(), center, null);
		//Executions.getCurrent().sendRedirect(pagename);
		//Messagebox.show(pagename);   		  	
		 */
	}
	
	public List<MenuModel> getLstListsMenu() 
	{
		lstListsMenu=mData.getSideBarSubMenuList(1,1,companyroleid);		
		return lstListsMenu;
	}
	public void setLstListsMenu(List<MenuModel> lstListsMenu) {
		this.lstListsMenu = lstListsMenu;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
