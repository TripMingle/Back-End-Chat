package org.example.backendchat.chat;

import org.example.backendchat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMongoRepository extends MongoRepository<ChatMessage, String> {
}
