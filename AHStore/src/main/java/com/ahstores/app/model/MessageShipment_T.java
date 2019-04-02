package com.ahstores.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MSG_SHIPMENT_T")
public class MessageShipment_T {
	@Id
	@NotNull@Column(name = "MESG_SEQ_NO")
	private Long msgSeqNumber;
	@NotNull@Column(name = "SHIPMENT_ID")
	private Long shipmentId;
	@NotNull@Column(name = "ORDER_ID")
	private Long orderId;
	@Column(name = "GLN_BUYER")
	private Long glnBuyer;
	@Column(name = "GLN_SUPPLIER")
	private Long glnSupplier;
	@Column(name = "GLN_CONSIGNOR")
	private Long glnConsignor;
	@Column(name = "GLN_CONSIGNEE")
	private Long glnConsignee;
	@Column(name = "GLN_DELIVERYPOINT")
	private Long glnDeliveryPoint;
	@Column(name = "INTERNALWAREHOUSENO")
	private Long internalWareHouseNo;
	@NotNull@Column(name = "IND_BACKHAUL")
	private String indBackHaul;
	@NotNull@Column(name = "MESG_EVENT_DATETIME")
	private Date msgEventDateTime;
	@NotNull@Column(name = "MESG_ACTION")
	private String msgAction;
	@NotNull@Column(name = "MESG_INSERTED_DATETIME")
	private Date msgInsertedDateTime;
	@NotNull@Column(name = "MESG_IND_PROCESSED")
	private String msgIndProcessed;
	@NotNull@Column(name = "MESG_IND_COMPLETED")
	private String msgIndCompleted;
	
	public Date getMsgInsertedDateTime() {
		return msgInsertedDateTime;
	}
	public void setMsgInsertedDateTime(Date msgInsertedDateTime) {
		this.msgInsertedDateTime = msgInsertedDateTime;
	}
	public Long getMsgSeqNumber() {
		return msgSeqNumber;
	}
	public void setMsgSeqNumber(Long msgSeqNumber) {
		this.msgSeqNumber = msgSeqNumber;
	}
	public Long getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getGlnBuyer() {
		return glnBuyer;
	}
	public void setGlnBuyer(Long glnBuyer) {
		this.glnBuyer = glnBuyer;
	}
	public Long getGlnSupplier() {
		return glnSupplier;
	}
	public void setGlnSupplier(Long glnSupplier) {
		this.glnSupplier = glnSupplier;
	}
	public Long getGlnConsignor() {
		return glnConsignor;
	}
	public void setGlnConsignor(Long glnConsignor) {
		this.glnConsignor = glnConsignor;
	}
	public Long getGlnConsignee() {
		return glnConsignee;
	}
	public void setGlnConsignee(Long glnConsignee) {
		this.glnConsignee = glnConsignee;
	}
	public Long getGlnDeliveryPoint() {
		return glnDeliveryPoint;
	}
	public void setGlnDeliveryPoint(Long glnDeliveryPoint) {
		this.glnDeliveryPoint = glnDeliveryPoint;
	}
	public Long getInternalWareHouseNo() {
		return internalWareHouseNo;
	}
	public void setInternalWareHouseNo(Long internalWareHouseNo) {
		this.internalWareHouseNo = internalWareHouseNo;
	}
	public String getIndBackHaul() {
		return indBackHaul;
	}
	public void setIndBackHaul(String indBackHaul) {
		this.indBackHaul = indBackHaul;
	}
	public Date getMsgEventDateTime() {
		return msgEventDateTime;
	}
	public void setMsgEventDateTime(Date msgEventDateTime) {
		this.msgEventDateTime = msgEventDateTime;
	}
	public String getMsgAction() {
		return msgAction;
	}
	public void setMsgAction(String msgAction) {
		this.msgAction = msgAction;
	}
	public String getMsgIndProcessed() {
		return msgIndProcessed;
	}
	public void setMsgIndProcessed(String msgIndProcessed) {
		this.msgIndProcessed = msgIndProcessed;
	}
	public String getMsgIndCompleted() {
		return msgIndCompleted;
	}
	public void setMsgIndCompleted(String msgIndCompleted) {
		this.msgIndCompleted = msgIndCompleted;
	}
}
