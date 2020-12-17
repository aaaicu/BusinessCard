package com.jaehyun.businesscard.data.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendBusinessCardModel {

    @SerializedName("seq")
    @Expose
    private Integer seq;
    @SerializedName("sender")
    @Expose
    private Integer sender;
    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("sendType")
    @Expose
    private String sendType;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("fileSeq")
    @Expose
    private Integer fileSeq;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFileSeq() {
        return fileSeq;
    }

    public void setFileSeq(Integer fileSeq) {
        this.fileSeq = fileSeq;
    }

}