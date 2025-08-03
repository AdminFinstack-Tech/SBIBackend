package com.csme.csmeapi.mobile.model;

public class LGInquiryModel {

	String eTradeReferenceNumber;
	String cic;
	String requestId;
	String requestMessage;
	String requestDate;
	String responseMessage;
	String responseDate;
	String sequence;
	String status;
	
	@Override
	public String toString() {
		return "LGInquiryModel [mainref=" + eTradeReferenceNumber + ", cic=" + cic + ", requestMessage=" + requestMessage
				+ ", requestDate=" + requestDate + ", responseMessage=" + responseMessage + ", responseDate="
				+ responseDate + ", sequence=" + sequence + ", status=" + status + "]";
	}
	public final String getRequestId() {
		return requestId;
	}
	
	public final void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public final String getMainref() {
		return eTradeReferenceNumber;
	}
	public final void setMainref(String mainref) {
		this.eTradeReferenceNumber = mainref;
	}
	public final String getCic() {
		return cic;
	}
	public final void setCic(String cic) {
		this.cic = cic;
	}
	public final String getRequestMessage() {
		return requestMessage;
	}
	public final void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	public final String getRequestDate() {
		return requestDate;
	}
	public final void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public final String getResponseMessage() {
		return responseMessage;
	}
	public final void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public final String getResponseDate() {
		return responseDate;
	}
	public final void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
	}
	public final String getSequence() {
		return sequence;
	}
	public final void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public final String getStatus() {
		return status;
	}
	public final void setStatus(String status) {
		this.status = status;
	}
	
}
