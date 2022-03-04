package com.example.telegram;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WriteReadBot {

    String path;
    String username;
    String token;

    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;
}