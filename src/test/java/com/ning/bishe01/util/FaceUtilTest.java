package com.ning.bishe01.util;


import com.baidu.aip.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class FaceUtilTest {



    @Test
    public void registerTest() throws Exception{
        String path="C:\\Users\\21160\\Pictures\\Camera Roll\\宁.jpg";
        byte[] bytes= Files.readAllBytes(Paths.get(path));
        String encode= Base64Util.encode(bytes);
        String result=FaceUtil.faceRegister(encode,"1","beshe01");
        System.err.println(result);
    }
}