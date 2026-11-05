package com.dbbl.repository;


import com.dbbl.model.SmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsMessageRepository extends JpaRepository<SmsMessage, Long> {

}
