package com.test.util;


import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.io.File;


public class ParserDOCX implements Parser{
    @Override
    public Set<String> parse(File entry, String regx) {

        Set<String> result = new HashSet<>();
        WordprocessingMLPackage wordMLPackage = null;

        try {
            wordMLPackage = WordprocessingMLPackage
                    .load(entry);
            MainDocumentPart mainDocumentPart = wordMLPackage
                    .getMainDocumentPart();
            String textNodesXPath = "//w:t";
            List<Object> textNodes= mainDocumentPart
                    .getJAXBNodesViaXPath(textNodesXPath, true);
            for (Object obj : textNodes) {
                Text text = (Text) ((JAXBElement) obj).getValue();
                String textValue = text.getValue();

                Pattern pattern = Pattern.compile(regx,Pattern.UNICODE_CASE);
                Matcher matcher = pattern.matcher(textValue);

                while (matcher.find()) {
                    result.add(matcher.group());
                }
            }
        } catch (Docx4JException | JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;

    }
}
