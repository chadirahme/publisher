package domains;

public class CityModel 
{

	private int cityid;
	private String cityname;
	private int countryid;
	
	public CityModel(int cityid, String cityname, int countryid) {
		super();
		this.cityid = cityid;
		this.cityname = cityname;
		this.countryid = countryid;
	}
	
	public int getCityid() {
		return cityid;
	}
	public void setCityid(int cityid) {
		this.cityid = cityid;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public int getCountryid() {
		return countryid;
	}
	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}
	
	
}
