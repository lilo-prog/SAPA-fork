package com.example.SAPA.Repositories;

import com.example.SAPA.Models.Chat.AttachmentEntity;
import com.example.SAPA.Models.Chat.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {

    List<AttachmentEntity> findByMessage(MessageEntity message);
}
