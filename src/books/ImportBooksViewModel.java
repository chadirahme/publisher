package books;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.RowRenderer;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.exporter.excel.ExcelExporter.ExportContext;
import org.zkoss.io.Files;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.xssf.usermodel.XSSFCellStyle;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import sales.CashInvoiceData;
import domains.AgencyModel;
import domains.BooksModel;
import domains.CompanyRoles;
import domains.ExhibitionContactModel;
import domains.ExhibitionsModel;
import domains.ImportbookModel;
import domains.InvoiceDetailModel;


public class ImportBooksViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	BooksData data=new BooksData();
	String viewType;
	
	private List<ExhibitionsModel> lstExhibitions;
	private ExhibitionsModel selectedExhibitions;
	
	private List<AgencyModel> lstAgency;
	private AgencyModel selectedAgency;
	
	private List<ImportbookModel> lstImportBook;
	
	private String uploadedFilePath;
	private String fileName;
	private List<BooksModel> lstBooks;
	
	private ImportbookModel objImportBooks;
	private boolean canUploadFile;
	private boolean canViewData;
	private boolean canSaveData;
	private String totalBooks;
	
	private List<BooksModel> lstManageBooks;
	private double oldPrice;
	private double newPrice;
	private String invoiceNumber;
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();				
			viewType=type==null?"":type;
			invoiceNumber="";
			
			logger.info(viewType);
			
			if(viewType.equals("importlist"))
			{
				lstExhibitions=data.getActiveExhibitionsList("اختار");
				selectedExhibitions=lstExhibitions.get(0);						
			}
			
			else if(viewType.equals("import"))
			{							
				lstExhibitions=data.getActiveExhibitionsList("اختار");
				selectedExhibitions=lstExhibitions.get(0);
				objImportBooks=new ImportbookModel();
				objImportBooks.setInvoicenumber("");	
				
				if(map.get("selectedImportBook") !=null)
				{					
					objImportBooks=(ImportbookModel) map.get("selectedImportBook");
					logger.info(objImportBooks.getAgencyid());
					for (ExhibitionsModel item : lstExhibitions)
					{
						if(item.getExhibitionid()==objImportBooks.getExhibitionid())
						{
							selectedExhibitions=item;
							break;
						}
					}
					
					lstAgency=data.getAgencyList("اختار", selectedExhibitions.getExhibitionid());
					for (AgencyModel item : lstAgency)			
					{
						if(item.getAgencyid()==objImportBooks.getAgencyid())
						{
							selectedAgency=item;
							break;
						}
					}					
				}
			}
			
			else if(viewType.equals("managment"))
			{
				lstExhibitions=data.getActiveExhibitionsList("اختار");
				selectedExhibitions=lstExhibitions.get(0);	
			}
			else if(viewType.equals("agencylist"))
			{
				lstAgency=data.getAgencyList("", 0);
			}
			
		}
		
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---init-->" , ex);			
		}	
	}
	
	@Command 
	@NotifyChange({"lstImportBook"})
	public void viewImportListCommand()
	{
		try
		{
			if(selectedExhibitions.getExhibitionid()==0)
			{
				Messagebox.show("الرجاء تحديد معرض !! ","Import Book", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			lstImportBook=data.getImportBooksList(selectedExhibitions.getExhibitionid());
			
		}		
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---viewImportListCommand-->" , ex);			
		}
	}

	@Command 
	public void openImportBookCommand()
	{
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedExhibitions", selectedExhibitions);		
		Window window = (Window)Executions.createComponents("/books/importbooks.zul", null, arg);//importbooks import.zul
		window.doModal();
	}
	
	@Command 
	public void editImportBookCommand(@BindingParam("row") ImportbookModel todo)
	{
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedImportBook", todo);		
		Window window = (Window)Executions.createComponents("/books/import.zul", null, arg);
		window.doModal();
	}
	
	@Command
	public void deleteImportBookCommand(@BindingParam("row") ImportbookModel todo)
	{
		
	}
	
	
	@Command 
	@NotifyChange({"objCompanySetting","canViewData"})
	public void uploadFileCommand(BindContext ctx)
	{		
		try
		{
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();
			String fileFormat=event.getMedia().getFormat();
			logger.info("format >> "+fileFormat);
			logger.info(event.getMedia().getContentType());
			logger.info(event.getMedia().getName());
			logger.info("size>> " + event.getMedia().getByteData().length);
			fileFormat=fileFormat.contains("excel") ? "xls" : fileFormat;
			
			if(selectedAgency==null || selectedAgency.getAgencyid()==0)
			{
				Messagebox.show("Please select Agency !! ","Import Book", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(!fileFormat.equals("xls"))
			{
				Messagebox.show("Please select Excel in 2003 format (xls) !! ","Import Book", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			String filePath="";
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"books"+File.separator;					
			String dirPath=repository+selectedAgency.getAgencyid();
			File dir = new File(dirPath);

			if(!dir.exists())
				dir.mkdirs();
				
			filePath = dirPath +File.separator +event.getMedia().getName(); //+ "." +event.getMedia().getFormat();	
			createFile(event.getMedia().getStreamData(), filePath);
			logger.info("filePath>> " + filePath);
			
			logger.info("File Uploaded");
			uploadedFilePath=filePath;	
			fileName=event.getMedia().getName();
			canViewData=true;
			//attFile=event.getMedia().getName() + " is uploaded.";
			//canPreview=true;						
		}
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---uploadFileCommand-->" , ex);			
		}
	}
	
	private int createFile( InputStream is, String filePath)
	{
		int res=0;
		try
	    {
		  File file = new File(filePath);  
		  DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		  int c;
		  while((c = is.read()) != -1)
		  {
			  out.writeByte(c);
		  }
		  is.close();
		  out.close();
	    }
		catch(Exception ex)
		{
			res=1;
			Messagebox.show(ex.getMessage(),"Import Employee", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		return res;
	}
	
	@Command 	
	@NotifyChange({"lstBooks","canSaveData","totalBooks"})
	public void previewDataFile()
	{
		try
		{
			if(uploadedFilePath.equals(""))
			{
				Messagebox.show("Please select Excel File !!","Import Books", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			else
			{
				  FileInputStream fis = null;
			      fis = new FileInputStream(uploadedFilePath);
			      HSSFWorkbook workbook = new HSSFWorkbook(fis);
			      HSSFSheet sheet = workbook.getSheetAt(0);
			      Iterator rows = sheet.rowIterator();
			      
			      lstBooks=new ArrayList<BooksModel>();
			      while (rows.hasNext()) 
			      {
		           HSSFRow row = (HSSFRow) rows.next();
		           Iterator cells = row.cellIterator();
		           
		        
		           if (row.getRowNum() >= 1)
		           {
		        	  BooksModel obj=new BooksModel();
			          while (cells.hasNext())
			          {
			        	  HSSFCell cell1 = (HSSFCell) cells.next();
			        	  cell1.setCellType(Cell.CELL_TYPE_STRING);
			        	  
			        	  if(cell1.getColumnIndex() == 0)
				            {
			        		  logger.info("code>> "+cell1.getStringCellValue());
			        		  obj.setBookCode(cell1.getStringCellValue());
				            }
			        	  
			        	  else if(cell1.getColumnIndex() == 1)
				            {
			        		  obj.setBookName(cell1.getStringCellValue());
			        		  
				            }
			        	  else if(cell1.getColumnIndex() == 2)
				            {
			        		  obj.setEditor(cell1.getStringCellValue());
				            }
			        	  else if(cell1.getColumnIndex() == 3)
				            {
			        		  obj.setAuthor(cell1.getStringCellValue());
				            }
			        	  
			        	  else if(cell1.getColumnIndex() == 4)
				            {
			        		  obj.setPrice(cell1.getStringCellValue());
				            }
			        	  else if(cell1.getColumnIndex() == 5)
				            {
			        		  obj.setQuantity(cell1.getStringCellValue());
				            }			        	 			       
			          }
			          logger.info("book Name >> " + obj.getBookName());
			          
			          if( obj.getBookName()!=null && !obj.getBookName().equals("NULL"))
			          {
			        	  obj.setBookName(obj.getBookName()==null?"":replaceSpecialChar(obj.getBookName()));
			        	  obj.setBookCode(obj.getBookCode()==null?"":replaceSpecialChar(obj.getBookCode()));
			        	  obj.setAuthor(obj.getAuthor()==null?"":replaceSpecialChar(obj.getAuthor()));		
			        	  obj.setEditor(obj.getEditor()==null?"":replaceSpecialChar(obj.getEditor()));		
			        	  if(checkIfBookCodeExists(obj)==false)
			        		  lstBooks.add(obj);
			          }
		           }
		           		           
			      }
			      canSaveData=lstBooks.size()>0;
			      totalBooks="Total Books will be added :" + lstBooks.size();
			}
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---previewDataFile-->" , ex);			
		}
	}
	private String replaceSpecialChar(String oldValue)
	{		
		return oldValue.replace("'", ""); //oldValue.replace("'", "`");
		
	}
	
	private boolean checkIfBookCodeExists(BooksModel obj)
	{
		boolean codeFound=false;
		
		if(obj.getBookCode().equals(""))
			return false;
		
		for (BooksModel item : lstBooks)
		{
			if(item.getBookCode().equals(obj.getBookCode()))
			{
					codeFound=true;
					int totalQuantity=Integer.parseInt(item.getQuantity()) + Integer.parseInt(obj.getQuantity());
					item.setQuantity(totalQuantity+"");
					break;
			}
		}
		
		return codeFound;
	}
	
	@Command 
	public void saveImportCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			if(lstBooks==null || lstBooks.size()==0)
			{
				Messagebox.show("Please view data first !!","Import Books", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(objImportBooks.getInvoicenumber().equals(""))
			{
				Messagebox.show("Please enter invoice number !!","Import Books", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			objImportBooks.setExhibitionid(selectedExhibitions.getExhibitionid());
			objImportBooks.setAgencyid(selectedAgency.getAgencyid());
			boolean checkInv=data.checkInvoiceNumber(objImportBooks);
			if(checkInv)
			{
				Messagebox.show("Invoice number exists !!","Import Books", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			//check the old books
			CashInvoiceData cdata=new CashInvoiceData();
			List<BooksModel> lstOldBooks=cdata.getExhibtionBooksList(selectedExhibitions.getExhibitionid());
			List<BooksModel> lstNewImport=new ArrayList<BooksModel>();
			List<BooksModel> lstExistsImport=new ArrayList<BooksModel>();
			boolean isBookFound=false;
			for (BooksModel item : lstBooks)
			{
				isBookFound=false;
				for (BooksModel Olditem : lstOldBooks)
				{
					if(item.getBookCode().equals(Olditem.getBookCode()))
					{	
						isBookFound=true;
						item.setBookid(Olditem.getBookid());
						lstExistsImport.add(item);							
						break;
					}
				}
				
				if(isBookFound==false)
				{
					lstNewImport.add(item);
				}
			}
			
			logger.info("lstNewImport >> " + lstNewImport.size() + " >> lstExistsImport >> " + lstExistsImport.size());
			
										
			objImportBooks.setFilepath(fileName);
			data.addImportBook(objImportBooks,lstNewImport,lstExistsImport);
			
			Map args = new HashMap();
			BindUtils.postGlobalCommand(null, null, "refreshImportParent", args);	
			comp.detach();	
			
		}		
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---saveImportCommand-->" , ex);			
		}
	}
	
	@GlobalCommand 
	 @NotifyChange({"lstImportBook"})	
	 public void refreshImportParent()
	 {
		lstImportBook=data.getImportBooksList(selectedExhibitions.getExhibitionid());
	 }
	
	@Command
	@NotifyChange({"lstManageBooks"})
	public void viewBooksImportListCommand()
	{
		try
		{
			
			if(selectedExhibitions.getExhibitionid()==0)
			{
				Messagebox.show("الرجاء تحديد معرض !! ","Import Book", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			if(selectedAgency.getAgencyid()==0)
			{
				Messagebox.show("يرجى اختيار وكالة !! ","Import Book", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			lstManageBooks=data.getBooksManagmentList(selectedExhibitions.getExhibitionid(), selectedAgency.getAgencyid(), invoiceNumber);
		}
		
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---viewBooksImportListCommand-->" , ex);			
		}
		
	}
	
	@Command
	@NotifyChange({"lstManageBooks"})
	public void getLocalPriceCommand()
	{
		try
		{
			for (BooksModel item : lstManageBooks)
			{
				double disc=selectedAgency.getDiscountaudience()/100;
				double discPrice=item.getBookPrice()- ( item.getBookPrice() * disc);				
				//item.setLocalPrice(Math.floor(discPrice * selectedExhibitions.getCurrencyrate()));
				item.setLocalPrice(discPrice * selectedExhibitions.getCurrencyrate());
				//item.setLocalPrice(Math.floor(item.getBookPrice() * selectedExhibitions.getCurrencyrate()));				
			}
		}
		
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---viewBooksImportListCommand-->" , ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstManageBooks"})
	public void replacePriceCommand()
	{
		try
		{
			for (BooksModel item : lstManageBooks)
			{
				if(item.getLocalPrice()==oldPrice)
					item.setLocalPrice(newPrice);
			}
		}
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---replacePriceCommand-->" , ex);			
		}	
	}
	
	@Command
	@NotifyChange({"lstManageBooks"})
	public void saveBooksChanges()
	{
		try
		{
			data.updateImportBook(lstManageBooks);
			Messagebox.show("Books saved !!","Books Managment", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		
		catch (Exception ex) 
		{
			  logger.error("error in ImportBooksViewModel---viewBooksImportListCommand-->" , ex);			
		}
	}
	
	 @Command
	  @NotifyChange({"lstAgency"})
	 public void changeEditableStatus(@BindingParam("row") AgencyModel item) 
		{
						
		 item.setEditingStatus(!item.isEditingStatus());
							
	    }
	 
	 @Command
	  @NotifyChange({"lstAgency"})
	 public void saveAgencyCommand(@BindingParam("row") AgencyModel item)
	 {
		 try
		 {
			data.updateAgency(item);								
			item.setEditingStatus(!item.isEditingStatus());		
		 }		 
		 catch (Exception ex) 
		{
				  logger.error("error in ImportBooksViewModel---saveAgencyCommand-->" , ex);			
		}
	 }
	 
	 @Command
	 public void exportBooksItemsToExcel()
	 {
		 try
		 {
			 if(lstManageBooks==null)
			  {
		  		Messagebox.show("لا يوجد سجل!!","Books List", Messagebox.OK , Messagebox.EXCLAMATION);
		  		return;
			  }
			 
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 final ExcelExporter exporter = new ExcelExporter();
			 String[] tsHeaders;
			 tsHeaders = new String[]{ "كود الكتاب", "إسم الكتاب" , "اسم المحرر" ,"الكمية", "السعر الإفرادي","السعر المحلي" , "العدد الباقي"};
			 final String[] headers=tsHeaders;
			 
			 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
			     
				    @Override
				    public void beforeRendering(XSSFWorkbook target) {
				        ExportContext context = exporter.getExportContext();
				         
				        for (String header : headers) {
				        	org.zkoss.poi.ss.usermodel.Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());				           
				            cell.setCellValue(header);
				             				           
				                CellStyle srcStyle = cell.getCellStyle();
				                if (srcStyle.getAlignment() != CellStyle.ALIGN_CENTER) {				                   
									XSSFCellStyle newCellStyle = target.createCellStyle();
				                    newCellStyle.cloneStyleFrom(srcStyle);
				                    newCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
				                    cell.setCellStyle(newCellStyle);
				                }
				            
				        }
				    }
				    
				    @Override
				    public void afterRendering(XSSFWorkbook target) {
				    }				    				   
				});
			 
				exporter.export(headers.length, lstManageBooks, new RowRenderer<Row, BooksModel>() {
					@Override
					public void render(Row table, BooksModel item, boolean isOddRow) 
						 {
						 	ExportContext context = exporter.getExportContext();
					        XSSFSheet sheet = context.getSheet();				        
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookCode());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookName());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEditor());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getQuantity());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookPrice());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getLocalPrice());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRemainquantity());				       
						 }
						 
				    }, out);
				 
				   	AMedia amedia = new AMedia("BooksPriceList.xls", "xls", "application/file", out.toByteArray());
					Filedownload.save(amedia);
					out.close();
		 }		 
		 catch (Exception ex) 
		{
			 logger.error("error in ImportBooksViewModel---exportBooksItemsToExcel-->" , ex);			
		}
	 }
	
	
	public List<ExhibitionsModel> getLstExhibitions() {
		return lstExhibitions;
	}


	public void setLstExhibitions(List<ExhibitionsModel> lstExhibitions) {
		this.lstExhibitions = lstExhibitions;
	}


	public ExhibitionsModel getSelectedExhibitions() {
		return selectedExhibitions;
	}

	@NotifyChange({"lstAgency","selectedAgency","selectedExhibitions"})
	public void setSelectedExhibitions(ExhibitionsModel selectedExhibitions) 
	{
		this.selectedExhibitions = selectedExhibitions;
		lstAgency=data.getAgencyList("اختار", selectedExhibitions.getExhibitionid());
		selectedAgency=lstAgency.get(0);
	}


	public List<AgencyModel> getLstAgency() {
		return lstAgency;
	}


	public void setLstAgency(List<AgencyModel> lstAgency) {
		this.lstAgency = lstAgency;
	}


	public AgencyModel getSelectedAgency() {
		return selectedAgency;
	}

	@NotifyChange({"canUploadFile","selectedAgency"})
	public void setSelectedAgency(AgencyModel selectedAgency) 
	{
		this.selectedAgency = selectedAgency;
		if(selectedAgency.getAgencyid()>0)
		{
			canUploadFile=true;
		}
	}


	public List<BooksModel> getLstBooks() {
		return lstBooks;
	}


	public void setLstBooks(List<BooksModel> lstBooks) {
		this.lstBooks = lstBooks;
	}


	public ImportbookModel getObjImportBooks() {
		return objImportBooks;
	}


	public void setObjImportBooks(ImportbookModel objImportBooks) {
		this.objImportBooks = objImportBooks;
	}


	public boolean isCanUploadFile() {
		return canUploadFile;
	}


	public void setCanUploadFile(boolean canUploadFile) {
		this.canUploadFile = canUploadFile;
	}


	public boolean isCanViewData() {
		return canViewData;
	}


	public void setCanViewData(boolean canViewData) {
		this.canViewData = canViewData;
	}


	public boolean isCanSaveData() {
		return canSaveData;
	}


	public void setCanSaveData(boolean canSaveData) {
		this.canSaveData = canSaveData;
	}

	public List<ImportbookModel> getLstImportBook() {
		return lstImportBook;
	}

	public void setLstImportBook(List<ImportbookModel> lstImportBook) {
		this.lstImportBook = lstImportBook;
	}

	public String getTotalBooks() {
		return totalBooks;
	}

	public void setTotalBooks(String totalBooks) {
		this.totalBooks = totalBooks;
	}

	public List<BooksModel> getLstManageBooks() {
		return lstManageBooks;
	}

	public void setLstManageBooks(List<BooksModel> lstManageBooks) {
		this.lstManageBooks = lstManageBooks;
	}

	public double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public double getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(double newPrice) {
		this.newPrice = newPrice;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
}
