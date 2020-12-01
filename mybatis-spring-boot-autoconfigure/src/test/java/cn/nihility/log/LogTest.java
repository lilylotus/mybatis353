package cn.nihility.log;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daffodil
 * @date 2020-12-02 00:04:34
 */
public class LogTest {

    private static final Logger log = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void log() {
        log.info("log test info");
    }

}
