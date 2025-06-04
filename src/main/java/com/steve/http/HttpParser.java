package com.steve.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class HttpParser {
    private final static Logger logger= LoggerFactory.getLogger(HttpParser.class);
/** design choice to not make it static as we can instantiate a new parser for each connection, giving choice to modify if required
 */
    public void parseHttpRequest(InputStream inputStream){

    }
}
