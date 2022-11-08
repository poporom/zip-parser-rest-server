package com.test.util;

import java.io.File;
import java.util.Map;
import java.util.Set;

public interface Parser {
    Set<String> parse(File entry, String pattern);
}
