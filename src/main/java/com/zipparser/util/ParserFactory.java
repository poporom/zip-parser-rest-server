package com.zipparser.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

@Component
public class ParserFactory {

    public Parser getParser(String fileName) {

        Parser parser;
        String ext = FilenameUtils.getExtension(fileName);

        try {
            Class<?> clazz = Class.forName("com.test.util.Parser" +
                    ext.toUpperCase());
            parser = (Parser) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("This file type is not supported");
        }

        return parser;
    }
}
