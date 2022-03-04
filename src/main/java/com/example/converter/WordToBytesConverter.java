package com.example.converter;

import com.example.model.Word;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.annotation.Nullable;

public class WordToBytesConverter implements Converter<Word, byte[]> {

    private final Jackson2JsonRedisSerializer<Word> serializer;

    public WordToBytesConverter() {
        this.serializer = new Jackson2JsonRedisSerializer<Word>(Word.class);
        this.serializer.setObjectMapper(new ObjectMapper());
    }


    @Override
    public byte[] convert(@Nullable Word value) {
        return this.serializer.serialize(value);
    }
}
