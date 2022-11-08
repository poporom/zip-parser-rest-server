package com.zipparser.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserTXT implements Parser {

    @Override
    public Set<String> parse(File entry, String regx) {
        Set<String> result = new HashSet<>();

        Pattern pattern = Pattern.compile(regx, Pattern.UNICODE_CASE);
        String str = null;
        try {
            str = FileUtils.readFileToString(entry, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert str != null;

        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }
}
