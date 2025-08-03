package uz.securityproject.securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.securityproject.securityproject.dto.ReceiptDTO;
import uz.securityproject.securityproject.service.ReceiptService;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/upload")
    public ResponseEntity<ReceiptDTO> uploadReceipt(@RequestParam("file") MultipartFile file) {
        ReceiptDTO dto = receiptService.processAndSave(file);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("loyixa ishladiiiiiiiiiiiiiiii!");
    }
}
