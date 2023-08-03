package com.hhs.codeboard.blog.config.web.property;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hhs.codeboard.blog.enumeration.CodeboardEnum;

import java.io.IOException;
import java.io.Serializable;

/**
 * SimpleSerializer를 base로 분석후 코딩
 */
class CodeboardEnumSerializer {

    private CodeboardEnumSerializer() {}

    static class Serializer extends Serializers.Base implements Serializable {

        private final SerializerProcess serializerProcess = new SerializerProcess();

        @Override
        public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
            // 결국 모든 파라미터가 한번씩 거쳐감
            // 해당 클래스 상속여부만 채크해서 넘긴다.
            if (CodeboardEnum.class.isAssignableFrom(type.getRawClass())) {
                return serializerProcess;
            }

            return null;
        }
    }
    /**
     * 직렬화용
     */
    private static class SerializerProcess extends StdSerializer<CodeboardEnum> {

        protected SerializerProcess() {
            super(CodeboardEnum.class);
        }

        @Override
        public void serialize(CodeboardEnum value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getCode());
        }
    }

}
