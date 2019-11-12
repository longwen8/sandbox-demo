package com.longwen.msg;

public class Msg {

    private String msg;   // 短信内容
    private String phone; // 手机号码
    private String msgType;


    private long id;     //id
    private long fileId; //文件编号


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }


    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Msg [msg=" + msg + ", phone=" + phone + ", fileId=" + fileId
                + "]";
    }
}
