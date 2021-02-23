package enums;

public enum SerialPortConfigEnum {



    BAUD_RATE("port.baud.rate" , "波特率"),

    SERIAL_NUMBER("port.name" , "串口号"),

    DATA_BITS("port.data.bits" , "数据位"),

    STOP_BITS("port.stop.bits" , "停止位"),

    PARITY_CHECK("port.parity.check" , "奇偶校验");



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
