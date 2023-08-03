package com.hhs.codeboard.blog.config.web.property;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.hhs.codeboard.blog.enumeration.CodeboardEnum;
import org.springframework.boot.json.JsonParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CodeboardEnumDeserializer {

    private CodeboardEnumDeserializer() {}

    static class Serializer extends Deserializers.Base {

        /**
         * cache를 통해 반복적인 역직렬화 객체생성의 방지.
         * capacity는 만약을 대비해 64로 상향설정.
         * 실행순서는
         * hasDeserializerFor -> findEnumDeserializer 순서로보임
         * <p>
         * simpleDeserializers도 있긴 하지만,
         * 내부에 구현된 cacheMap이 HashMap이라 thread safe하지 않을수 있어보인다.
         */
        final Map<ClassKey, JsonDeserializer<?>> cache = new ConcurrentHashMap<>(64);

        @Override
        public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) {
            // 구현체인지 확인
            if (CodeboardEnum.class.isAssignableFrom(type)) {
                JsonDeserializer<?> enumDeserializer = new SerializerProcess(type);
                addDeserializer(type, enumDeserializer);
                return enumDeserializer;
            }
            // 상관없는 타입은 Null을 리턴하고 다른 역직렬화의 타겟이된다.
            return null;
        }

        @Override
        public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
            return cache.containsKey(new ClassKey(valueType));
        }

        public void addDeserializer(Class<?> forClass, JsonDeserializer<?> deserializer) {
            ClassKey key = new ClassKey(forClass);
            cache.put(key, deserializer);
        }
    }

    /**
     * 역직렬화용
     */
    private static class SerializerProcess extends StdDeserializer<Enum<? extends CodeboardEnum>> implements ContextualDeserializer {

        protected SerializerProcess(Class<?> vc) {
            super(vc);
        }

        /**
         * 캐스팅 수준에서 경고를 무시해야작동.
         *
         * @param jp Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return
         * @throws IOException
         */
        @SuppressWarnings("unchecked")
        @Override
        public Enum<? extends CodeboardEnum> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = jp.getCodec().readTree(jp);
            String text = jsonNode.asText();
            Class<? extends CodeboardEnum> enumType = (Class<? extends CodeboardEnum>)this._valueClass;
            return Arrays.stream(enumType.getEnumConstants())
                    .filter(tEnum->tEnum.getCode().equals(text))
                    .findFirst()
                    .map(target->{
                        Enum<? extends CodeboardEnum> result;
                        try {
                            result = (Enum<? extends CodeboardEnum>) target;
                        } catch (ClassCastException e) {
                            throw new JsonParseException();
                        }
                        return result;
                    })
                    .orElseThrow(JsonParseException::new);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            return new SerializerProcess(property.getType().getRawClass());
        }
    }

}
