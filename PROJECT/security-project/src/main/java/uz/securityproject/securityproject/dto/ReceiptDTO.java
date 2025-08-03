package uz.securityproject.securityproject.dto;

import lombok.Data;

@Data
public class ReceiptDTO {

    private String merchantName;
    private String address;
    private String date;
    private String total;
}
