package domains;

public class CountryModel 
{

	private int countryid;
	private String countryname;
	private String countrycode;
	
	public CountryModel(int countryid, String countryname, String countrycode)
	{
		super();
		this.countryid = countryid;
		this.countryname = countryname;
		this.countrycode = countrycode;
	}
	
	public int getCountryid() {
		return countryid;
	}
	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getCountrycode() {
		return countrycode;
	}
	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}
	
	
}
