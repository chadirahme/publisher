package common;

public class CommonValues 
{

	public static enum HbaList {
		CashPayment("نقدي"),
		UnpaidPayment("بالحساب"),
		OnHold("معلقة"),
		ReturnInvoice("مرتجع");
		
		
		private final String value;

		private HbaList(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
