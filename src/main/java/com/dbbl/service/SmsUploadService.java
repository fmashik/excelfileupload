package com.dbbl.service;

import com.dbbl.model.SmsMessage;
import com.dbbl.repository.SmsMessageRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SmsUploadService {
    private final SmsMessageRepository smsMessageRepository;

    public SmsUploadService(SmsMessageRepository smsMessageRepository) {
        this.smsMessageRepository = smsMessageRepository;
    }

    // Calculate Primary Channel ID
    private String getPrimaryChannelId(String mobileNumber) {

        if (mobileNumber == null || mobileNumber.trim().length() != 11) {
            return "0";
        }

        mobileNumber = mobileNumber.trim();

        if (mobileNumber.startsWith("017")) {
            return "12";
        }
        else if (mobileNumber.startsWith("019")) {
            return "11";
        }
        else {
            return "10";
        }
    }

    // Upload Excel File
    @Transactional
    public int uploadSmsExcel(MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select an Excel file.");
        }

        List<SmsMessage> smsList = new ArrayList<>();

        DataFormatter formatter = new DataFormatter();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Start from row 1 because row 0 contains headers
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String mobileNumber =
                        formatter.formatCellValue(row.getCell(0)).trim();

                String message =
                        formatter.formatCellValue(row.getCell(1));

                String type =
                        formatter.formatCellValue(row.getCell(2));

                // Skip completely empty rows
                if (mobileNumber.isEmpty()
                        && message.isEmpty()
                        && type.isEmpty()) {
                    continue;
                }

                SmsMessage sms = new SmsMessage();

                sms.setMobileNumber(mobileNumber);

                sms.setPrimaryChannelId(
                        getPrimaryChannelId(mobileNumber)
                );

                sms.setMessage(message);
                sms.setType(type);
                sms.setStatus("N");

                smsList.add(sms);
            }
        }

        if (smsList.isEmpty()) {
            throw new IllegalArgumentException("No SMS records found in Excel file.");
        }

        smsMessageRepository.saveAll(smsList);

        return smsList.size();
    }

}
