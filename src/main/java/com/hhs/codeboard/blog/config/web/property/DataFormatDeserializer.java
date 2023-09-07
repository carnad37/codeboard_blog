package com.hhs.codeboard.blog.config.web.property;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.hhs.codeboard.blog.data.entity.common.dto.DataFormatDto;
import com.hhs.codeboard.blog.enumeration.CodeboardEnum;
import org.springframework.boot.json.JsonParseException;

import java.io.IOException;
import java.util.Arrays;

class DataFormatDeserializer {

    private DataFormatDeserializer() {}

    static class Serializer extends Deserializers.Base {

//        @Override
//        public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
//            if (CodeboardEnum.class.isAssignableFrom(type)) {
//                JsonDeserializer<?> enumDeserializer = new CodeboardEnumDeserializer.DeserializerProcess(type);
//                addDeserializer(type, enumDeserializer);
//                return enumDeserializer;
//            }
//            return null;
//        }


        @Override
        public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {
            if (DataFormatDto.class.isAssignableFrom(refType.getRawClass())) {
                return new DataFormatDeserializer.DeserializerProcess(refType.getRawClass());
            }
            return null;
        }

        @Override
        public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
            return true;
        }
//
//        public void addDeserializer(Class<?> forClass, JsonDeserializer<?> deserializer) {
//            ClassKey key = new ClassKey(forClass);
//            cache.put(key, deserializer);
//        }
    }

//    /**
//     * 역직렬화용
//     */
    private static class DeserializerProcess extends StdDeserializer<DataFormatModule> implements ContextualDeserializer {

        protected DeserializerProcess(Class<?> vc) {
            super(vc);
        }

        @Override
        public DataFormatModule deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return null;
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

//        @SuppressWarnings("unchecked")
//        @Override
//        public DataFormatModule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//            JsonNode jsonNode = jp.getCodec().readTree(jp);
//            String text = jsonNode.asText();
//            Class<? extends CodeboardEnum> enumType = (Class<? extends CodeboardEnum>)this._valueClass;
//            return Arrays.stream(enumType.getEnumConstants())
//                    .filter(tEnum->tEnum.getCode().equals(text))
//                    .findFirst()
//                    .map(target->{
//                        Enum<? extends CodeboardEnum> result;
//                        try {
//                            result = (Enum<? extends CodeboardEnum>) target;
//                        } catch (ClassCastException e) {
//                            throw new JsonParseException();
//                        }
//                        return result;
//                    })
//                    .orElseThrow(JsonParseException::new);
//        }



        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            return new DeserializerProcess(property.getType().getRawClass());
        }
    }

}
