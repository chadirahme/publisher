package domains;

public class CurrencyModel 
{

	private int currencyid;
	private String currencyname;
	private double rate;
	private String currencysymbol;
	
	public CurrencyModel(int currencyid, String currencyname, double rate,String currencysymbol) {
		super();
		this.currencyid = currencyid;
		this.currencyname = currencyname;
		this.rate = rate;
		this.currencysymbol=currencysymbol;
	}
	public int getCurrencyid() {
		return currencyid;
	}
	public void setCurrencyid(int currencyid) {
		this.currencyid = currencyid;
	}
	public String getCurrencyname() {
		return currencyname;
	}
	public void setCurrencyname(String currencyname) {
		this.currencyname = currencyname;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public String getCurrencysymbol() {
		return currencysymbol;
	}
	public void setCurrencysymbol(String currencysymbol) {
		this.currencysymbol = currencysymbol;
	}
	
	
	
}
