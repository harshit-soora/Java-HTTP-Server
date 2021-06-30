package com.demo.httpServer.httpParser;

import com.demo.httpServer.config.Configuration;
import com.demo.httpServer.config.ConfigurationManager;
import com.demo.httpServer.config.HttpConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/*
 * This is a singleton class ie. instantiated once
 * It contains possible set of header in a HTTP Request
 */

public class HttpHeader {
    private HashSet<String> headerList;
    private static HttpHeader httpHeader;

    private void HttpHeader(){};

    public static HttpHeader getInstance(){
        if(httpHeader==null){
            httpHeader = new HttpHeader();
        }

        return httpHeader;
    }

    public void loadHeaderList(String filePath){
        this.headerList = new HashSet<String>();

        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File path for Header-list file is Invalid.", e);
        }
        StringBuffer buff = new StringBuffer();

        int i = 0;
        while(true){
            try {
                if ((i = reader.read()) == -1) break;
            } catch (IOException e) {
                throw new HttpConfigurationException("Error while reading Header-list file : Config", e);
            }
            buff.append((char)i);
        }

        String data = buff.toString();
        String[] validHeader = data.split("\\r?\\n");

        i=0;
        while(i < validHeader.length){
            this.headerList.add(validHeader[i]);
            i++;
        }
    }

    public boolean validateHeader(String key){
        if(key == null)
            throw new RuntimeException("Key is null, before checking with valid header -> set some non-empty key");
        if(this.headerList.contains(key))
            return true;
        return false;
    }
}
