package com.adakoda.android.paypalsample;

import java.io.Serializable;

import com.paypal.android.MEP.PayPalResultDelegate;

public class ResultDelegate implements PayPalResultDelegate, Serializable {

	private static final long serialVersionUID = 10001L;

	/**
	 * Notification that the payment has been completed successfully.
	 * 
	 * @param payKey
	 *            the pay key for the payment
	 * @param paymentStatus
	 *            the status of the transaction
	 */
	public void onPaymentSucceeded(String payKey, String paymentStatus) {
//		MPL_Example.resultTitle = "SUCCESS";
//		MPL_Example.resultInfo = "You have successfully completed your transaction.";
//		MPL_Example.resultExtra = "Key: " + payKey;
	}

	/**
	 * Notification that the payment has failed.
	 * 
	 * @param paymentStatus
	 *            the status of the transaction
	 * @param correlationID
	 *            the correlationID for the transaction failure
	 * @param payKey
	 *            the pay key for the payment
	 * @param errorID
	 *            the ID of the error that occurred
	 * @param errorMessage
	 *            the error message for the error that occurred
	 */
	public void onPaymentFailed(String paymentStatus, String correlationID,
			String payKey, String errorID, String errorMessage) {
//		MPL_Example.resultTitle = "FAILURE";
//		MPL_Example.resultInfo = errorMessage;
//		MPL_Example.resultExtra = "Error ID: " + errorID + "\nCorrelation ID: "
//				+ correlationID + "\nPay Key: " + payKey;
	}

	/**
	 * Notification that the payment was canceled.
	 * 
	 * @param paymentStatus
	 *            the status of the transaction
	 */
	public void onPaymentCanceled(String paymentStatus) {
//		MPL_Example.resultTitle = "CANCELED";
//		MPL_Example.resultInfo = "The transaction has been cancelled.";
//		MPL_Example.resultExtra = "";
	}
}