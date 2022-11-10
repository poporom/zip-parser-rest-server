package com.zipparser.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class ParserFactory {

    public Parser getParser(String fileName) throws Exception {

        Parser parser;
        String ext = FilenameUtils.getExtension(fileName);

        Class<?> clazz = Class.forName("com.zipparser.util.Parser" +
                ext.toUpperCase());
        parser = (Parser) clazz.getDeclaredConstructor().newInstance();

        return parser;
    }
}
