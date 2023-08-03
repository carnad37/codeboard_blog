package com.hhs.codeboard.blog.config.web.property;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import org.springframework.stereotype.Component;


/**
 * Jackson Module을 이용한 se/deserializer 구현
 * @see <a href="http://www.baeldung.com">https://d2.naver.com/helloworld/0473330</a>
 */
@Component
public class CodeboardEnumModule extends Module {

    @Override
    public String getModuleName() {
        return "codeboardEnum";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addSerializers(new CodeboardEnumSerializer.Serializer());
        context.addDeserializers(new CodeboardEnumDeserializer.Serializer());
    }

}
