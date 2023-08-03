//package com.hhs.codeboard.blog.config.common;
//
//import com.hhs.codeboard.blog.enumeration.CodeboardEnum;
//import org.modelmapper.Converter;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.PropertyMap;
//import org.modelmapper.convention.MatchingStrategies;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ModelMapperConfig {
//
//    /**
//     * 혹시모를 잘못된 매칭에 대비해 strict로 타이트하게 매
//     * @return
//     */
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
//        // CodeboardEnum -> String 으로 컨버트시 조건추가
//        Converter<? extends CodeboardEnum, String> codeobardEnumConverter =
//                ctx -> {
//                    return ctx.getSource().getCode();
//                }
//        ;
//        PropertyMap<String, String> test = mapper->mapper
//        modelMapper.addMappings(
//            mapper -> mapper.using(collectionToSize).map(Game::getPlayers, GameDTO::setTotalPlayers)
//        );
//        return modelMapper;
//    }
//
//
//}
