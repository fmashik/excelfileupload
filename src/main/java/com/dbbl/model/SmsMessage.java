package com.dbbl.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sms_message")
public class SmsMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mobile_number", length = 30)
    private String mobileNumber;

    @Column(name = "primary_channel_id", length = 10)
    private String primaryChannelId;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "status", length = 30, nullable = false)
    private String status = "N";

    @PrePersist
    public void setDefaultStatus() {
        if (status == null || status.isBlank()) {
            status = "N";
        }
    }

}