package com.zipparser.util;

import java.io.File;
import java.util.Set;

public interface Parser {
    Set<String> parse(File entry, String pattern);
}
