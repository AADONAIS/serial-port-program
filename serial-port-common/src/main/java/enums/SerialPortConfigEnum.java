package enums;

public enum SerialPortConfigEnum {

    DATA_BITS("dataBits" , "数据位"),

    STOP_BITS("stopBits" , "停止位"),

    BAUD_RATE("baudRate" , "波特率"),

    PARITY_CHECK("parityCheck" , "奇偶校验"),

    SERIAL_NUMBER("serialNumber" , "串口号");


    private String code ;

    private String desc ;


    SerialPortConfigEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
