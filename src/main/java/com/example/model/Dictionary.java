package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@FieldDefaults(makeFinal = true)
@Getter
@RedisHash("dictionary")
@Builder
public class Dictionary {

    @Id
    String id;

    List<Word> wordList;
}
