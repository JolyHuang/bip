package com.sharingif.bips.bip0032.wordlist;

import com.sharingif.cube.core.exception.CubeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractWordList
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 下午5:21
 */
public abstract class AbstractWordList implements WordList {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> wordList = new ArrayList<String>(2048);
    private Map<String, Integer> wordMap = new HashMap<String, Integer>(2048);

    @Override
    public String getSpace() {
        return " ";
    }

    @Override
    public String getWord(int index) {
        if(wordList.isEmpty()) {
            load();
        }

        return wordList.get(index);
    }

    @Override
    public int getIndex(String word) {
        if(wordMap.isEmpty()) {
            load();
        }

        return wordMap.get(word);
    }

    abstract protected String getFileName();

    protected void load() {
        String fileClassPath =  new StringBuilder("config/wordlist/").append(getFileName()).toString();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileClassPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int i = 0;
        try {
            while ((line=br.readLine()) != null) {
                wordList.add(line);
                wordMap.put(line, i++);
            }
        } catch (Exception e) {
            logger.error("read file error", e);
            throw new CubeRuntimeException("read file error");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                logger.error("close BufferedReader error", e);
                throw new CubeRuntimeException("close BufferedReader error");
            }
        }

    }

}
