package reports;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.RowRenderer;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.exporter.excel.ExcelExporter.ExportContext;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.xssf.usermodel.XSSFCellStyle;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;

import books.BooksData;
import domains.AgencyModel;
import domains.ExhibitionsModel;
import domains.ReportModel;

public class BooksBuyViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	String viewType;
	ReportsData data=new ReportsData();
	BooksData bookdata=new BooksData();
	private List<ExhibitionsModel> lstExhibitions;
	private ExhibitionsModel selectedExhibitions;
	
	private List<AgencyModel> lstAgency;
	private AgencyModel selectedAgency;
	private String bookName;
	private String bookCode;
	private List<ReportModel> lstBooks;
	private int bookQty;
	private boolean withReturn;
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		viewType=type==null?"":type;
		
		if(viewType.equals("booksbuy"))
		{
			lstExhibitions=bookdata.getActiveExhibitionsList("اختار");
			selectedExhibitions=lstExhibitions.get(0);
			bookName="";
			bookCode="";
			withReturn=false;
		}
		else if(viewType.equals("booksalerts"))
		{
			lstExhibitions=bookdata.getActiveExhibitionsList("اختار");
			selectedExhibitions=lstExhibitions.get(0);
			bookName="";
			bookCode="";
			bookQty=5;
			
		}
		
		
		}
		
		catch (Exception ex) 
		{
			  logger.error("error in BooksBuyViewModel---init-->" , ex);			
		}
	}

	@Command
	@NotifyChange({"lstBooks"})
	public void viewBooksListCommand()
	{
		try
		{			
			lstBooks=data.getBooksBuyList(selectedExhibitions.getExhibitionid(), bookCode, bookName,withReturn,selectedAgency.getAgencyid());
		}
		catch (Exception ex) 
		{
			  logger.error("error in BooksBuyViewModel---viewBooksListCommand-->" , ex);			
		}
	}
	
	@Command
	public void exportToExcel()
	{
		try
		{
		 if(lstBooks==null)
		  {
	  		Messagebox.show("There is no record !!","Books Buy", Messagebox.OK , Messagebox.EXCLAMATION);
	  		return;
		  }
		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 String[] tsHeaders;
		 tsHeaders = new String[]{"تاريخ الفاتورة","رقم الفاتورة", "كود الكتاب", "إسم الكتاب","إسم الدار", "الكمية","السعر بعد الحسم" , "السعر"};
		 final String[] headers=tsHeaders;
		 
		 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
		     
			    @Override
			    public void beforeRendering(XSSFWorkbook target) {
			        ExportContext context = exporter.getExportContext();
			         
			        for (String header : headers) {
			            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
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
		 
			exporter.export(headers.length, lstBooks, new RowRenderer<Row, ReportModel>() {
				@Override
				public void render(Row table, ReportModel item, boolean isOddRow) 
					 {
					 	ExportContext context = exporter.getExportContext();
				        XSSFSheet sheet = context.getSheet();				        
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCreattionDate());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoiceprefix());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookCode());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookName());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAgencyName());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getQuantity());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDiscountPrice());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getPrice());
					 }
					 
			    }, out);
			 
			   	AMedia amedia = new AMedia("BooksReport.xls", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
			 
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in BooksBuyViewModel---exportToExcel-->" , ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstBooks"})
	public void viewBooksRemainsCommand()
	{
		try
		{
			int agencyid=0;
			if(selectedAgency!=null)
				agencyid=selectedAgency.getAgencyid();
				
			lstBooks=data.getBooksRemainsList(selectedExhibitions.getExhibitionid(), bookQty,agencyid);
		}
		catch (Exception ex) 
		{
			  logger.error("error in BooksBuyViewModel---viewBooksRemainsCommand-->" , ex);			
		}
	}
	@Command
	public void exportBooksRemainToExcel()
	{
		try
		{
		 if(lstBooks==null)
		  {
	  		Messagebox.show("There is no record !!","Books Remains", Messagebox.OK , Messagebox.EXCLAMATION);
	  		return;
		  }
		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 String[] tsHeaders;
		 tsHeaders = new String[]{ "كود الكتاب", "إسم الكتاب","إسم الدار","رقم الفاتورة", "الكمية","الكمية الباقية","السعر"};
		 final String[] headers=tsHeaders;
		 
		 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
		     
			    @Override
			    public void beforeRendering(XSSFWorkbook target) {
			        ExportContext context = exporter.getExportContext();
			         
			        for (String header : headers) {
			            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
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
		 
			exporter.export(headers.length, lstBooks, new RowRenderer<Row, ReportModel>() {
				@Override
				public void render(Row table, ReportModel item, boolean isOddRow) 
					 {
					 	ExportContext context = exporter.getExportContext();
				        XSSFSheet sheet = context.getSheet();				        				        
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookCode());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBookName());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAgencyName());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoicenumber());
				        
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getQuantity());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRemainQuantity());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getPrice());				        
					 }
					 
			    }, out);
			 
			   	AMedia amedia = new AMedia("BooksRemainsReport.xls", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
			 
			
		}
		catch (Exception ex) 
		{
			  logger.error("error in BooksBuyViewModel---exportBooksRemainsToExcel-->" , ex);			
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
		lstAgency=bookdata.getAgencyList("اختار", selectedExhibitions.getExhibitionid());
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


	public void setSelectedAgency(AgencyModel selectedAgency) {
		this.selectedAgency = selectedAgency;
	}

	public String getBookName() {
		return bookName;
	}


	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public List<ReportModel> getLstBooks() {
		return lstBooks;
	}

	public void setLstBooks(List<ReportModel> lstBooks) {
		this.lstBooks = lstBooks;
	}

	public String getBookCode() {
		return bookCode;
	}

	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}

	public int getBookQty() {
		return bookQty;
	}

	public void setBookQty(int bookQty) {
		this.bookQty = bookQty;
	}

	public boolean isWithReturn() {
		return withReturn;
	}

	public void setWithReturn(boolean withReturn) {
		this.withReturn = withReturn;
	}
}
