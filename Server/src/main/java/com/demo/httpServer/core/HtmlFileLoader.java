package com.demo.httpServer.core;

import com.demo.httpServer.config.HttpConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HtmlFileLoader {
    private final String htmlData;

    public HtmlFileLoader(String filePath) {
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException("File path for Config file is Invalid.", e);
        }
        StringBuffer buff = new StringBuffer();

        int i = 0;
        while(true){
            try {
                if ((i = reader.read()) == -1) break;
            } catch (IOException e) {
                throw new HttpConfigurationException("Error while reading Json file : Config", e);
            }
            buff.append((char)i);
        }

        this.htmlData = buff.toString();
    }

    public String getHtmlData() {
        return this.htmlData;
    }
}
