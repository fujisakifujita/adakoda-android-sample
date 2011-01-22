package com.adakoda.android.paypalsample;

import java.math.BigDecimal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

public class PayPalSampleActivity extends Activity {

	private TextView mResultText;
	private CheckoutButton mCheckoutButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mResultText = (TextView) findViewById(R.id.result_text);

		PayPalInitializer payPalInitializer = new PayPalInitializer(this);
		payPalInitializer.execute();
	}

	private class PayPalInitializer extends AsyncTask<Void, Void, Boolean> {

		private static final String APP_ID = "APP-80W284485P519543T";
		private Context mContext;
		private ProgressDialog mProgressDialog;
		
		public PayPalInitializer(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("ライブラリーを初期化中...");
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean success = false;
			PayPal payPal = PayPal.getInstance();
			if (payPal == null) {
				payPal = PayPal.initWithAppID(mContext, APP_ID, PayPal.ENV_SANDBOX);
				payPal.setLanguage("ja_JP");
				// FEEPAYER_SENDER,
				// FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and
				// FEEPAYER_SECONDARYONLY.
				payPal.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
				// Set to true if the transaction will require shipping.
				payPal.setShippingEnabled(true);
				// Dynamic Amount Calculation allows you to set tax and shipping
				// amounts based on the user's shipping address. Shipping must
				// be
				// enabled for Dynamic Amount Calculation. This also requires
				// you to create a class that implements PaymentAdjuster and
				// Serializable.
				payPal.setDynamicAmountCalculationEnabled(false);
				// --
				if (payPal.isLibraryInitialized()) {
					success = true;
				}
			}
			return success;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			mProgressDialog.hide();
			mProgressDialog = null;
			if (result) {
				setupButton();
			} else {
				mResultText.setText("ライブラリーの初期化に失敗しました");
			}
		}
	};

	
	private void setupButton() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
		PayPal payPal = PayPal.getInstance();
		mCheckoutButton = payPal.getCheckoutButton(this, PayPal.BUTTON_194x37,
				CheckoutButton.TEXT_PAY);
		mCheckoutButton.setOnClickListener(mCheckoutButtonOnClickListener);
		linearLayout.addView(mCheckoutButton);
	}
	private static final int request = 1;
	private View.OnClickListener mCheckoutButtonOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			PayPalPayment payment = new PayPalPayment();
			payment.setCurrencyType("USD");
			payment.setRecipient("example-merchant-1@paypal.com");
			payment.setSubtotal(new BigDecimal("8.25"));
			payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
			PayPalInvoiceData invoice = new PayPalInvoiceData();
	    	// Sets the tax amount.
	    	invoice.setTax(new BigDecimal("1.25"));
	    	// Sets the shipping amount.
	    	invoice.setShipping(new BigDecimal("4.50"));
	    	
	    	// PayPalInvoiceItem has several parameters available to it. None of these parameters is required.
	    	PayPalInvoiceItem item1 = new PayPalInvoiceItem();
	    	// Sets the name of the item.
	    	item1.setName("Pink Stuffed Bunny");
	    	// Sets the ID. This is any ID that you would like to have associated with the item.
	    	item1.setID("87239");
	    	// Sets the total price which should be (quantity * unit price). The total prices of all PayPalInvoiceItem should add up
	    	// to less than or equal the subtotal of the payment.
	    	item1.setTotalPrice(new BigDecimal("6.00"));
	    	// Sets the unit price.
	    	item1.setUnitPrice(new BigDecimal("2.00"));
	    	// Sets the quantity.
	    	item1.setQuantity(3);
	    	// Add the PayPalInvoiceItem to the PayPalInvoiceData. Alternatively, you can create an ArrayList<PayPalInvoiceItem>
	    	// and pass it to the PayPalInvoiceData function setInvoiceItems().
	    	invoice.getInvoiceItems().add(item1);
	    	
	    	// Create and add another PayPalInvoiceItem to add to the PayPalInvoiceData.
	    	PayPalInvoiceItem item2 = new PayPalInvoiceItem();
	    	item2.setName("Well Wishes");
	    	item2.setID("56691");
	    	item2.setTotalPrice(new BigDecimal("2.25"));
	    	item2.setUnitPrice(new BigDecimal("0.25"));
	    	item2.setQuantity(9);
	    	invoice.getInvoiceItems().add(item2);
	    	
	    	// Sets the PayPalPayment invoice data.
	    	payment.setInvoiceData(invoice);
	    	// Sets the merchant name. This is the name of your Application or Company.
	    	payment.setMerchantName("The Gift Store");
	    	// Sets the description of the payment.
	    	payment.setDescription("Quite a simple payment");
	    	// Sets the Custom ID. This is any ID that you would like to have associated with the payment.
	    	payment.setCustomID("8873482296");
	    	// Sets the Instant Payment Notification url. This url will be hit by the PayPal server upon completion of the payment.
	    	payment.setIpnUrl("http://www.exampleapp.com/ipn");
	    	// Sets the memo. This memo will be part of the notification sent by PayPal to the necessary parties.
	    	payment.setMemo("Hi! I'm making a memo for a simple payment.");
	    	
	    	Intent checkoutIntent = PayPal.getInstance().checkout(payment, PayPalSampleActivity.this, new ResultDelegate());
			// Use the android's startActivityForResult() and pass in our Intent. This will start the library.
	    	startActivityForResult(checkoutIntent, request);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case request:
			if (resultCode == Activity.RESULT_OK) {
				
			} else if (resultCode == Activity.RESULT_CANCELED) {
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
		
	}

}