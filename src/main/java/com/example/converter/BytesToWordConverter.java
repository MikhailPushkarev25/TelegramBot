package com.example.converter;

import com.example.model.Word;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.annotation.Nullable;

public class BytesToWordConverter implements Converter<byte[], Word> {

    private final Jackson2JsonRedisSerializer<Word> serializer;

    public BytesToWordConverter() {
        this.serializer = new Jackson2JsonRedisSerializer<Word>(Word.class);
        this.serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public Word convert(@Nullable byte[] value) {
        return this.serializer.deserialize(value);
    }
}
