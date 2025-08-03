package uz.securityproject.securityproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.securityproject.securityproject.dto.ReceiptDTO;
import uz.securityproject.securityproject.model.ReceiptEntity;
import uz.securityproject.securityproject.repository.ReceiptRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public ReceiptDTO processAndSave(MultipartFile file) {
        try {
            // Rasmni vaqtincha saqlash
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path imagePath = Paths.get("uploads", fileName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, file.getBytes());

//            // Python skript chaqirish
//            ProcessBuilder pb = new ProcessBuilder("python3", "python/ocr.py", imagePath.toString());
//            pb.redirectErrorStream(true);
//            Process process = pb.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String jsonOutput = reader.lines().collect(Collectors.joining());
//
//            // Python chiqishini logga yozish (tekshiruv uchun)
//            System.out.println("Python chiqishi: " + jsonOutput);
//
//            // JSON parse qilish
//            ObjectMapper objectMapper = new ObjectMapper();
//            ReceiptDTO dto = objectMapper.readValue(jsonOutput, ReceiptDTO.class);

            String pythonInterpreter = "python"; // yoki "python3" OSga qarab
            String scriptPath = Paths.get("python", "ocr.py").toAbsolutePath().toString();
            String imageArg = imagePath.toAbsolutePath().toString();

            ProcessBuilder pb = new ProcessBuilder(pythonInterpreter, scriptPath, imageArg);
            pb.redirectErrorStream(true);
            Process process = pb.start();

// Natijani o'qish
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String jsonOutput = reader.lines().collect(Collectors.joining());
            System.out.println("reader chiqishi: " + reader);
            System.out.println("Python-jsonOutput chiqishi: " + jsonOutput);
            System.out.println("Python-scriptPath chiqishi: " + scriptPath);
            System.out.println("Python-imageArg chiqishi: " + imageArg);

// JSON parse
            ObjectMapper objectMapper = new ObjectMapper();
            ReceiptDTO dto = objectMapper.readValue(jsonOutput, ReceiptDTO.class);

            // Saqlash
            ReceiptEntity receipt = new ReceiptEntity();
            receipt.setMerchantName(dto.getMerchantName());
            receipt.setAddress(dto.getAddress());
            receipt.setDate(dto.getDate());
            receipt.setTotal(dto.getTotal());
            receipt.setFileName(fileName);

            receiptRepository.save(receipt);

            return dto;

        } catch (IOException e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }
}

