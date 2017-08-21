package cn.devit.demo.cucumber.biz;

import static org.junit.Assert.*;


import org.junit.Test;

import cn.devit.demo.cucumber.biz.Frequency;

public class FrequencyTest {

    @Test
    public void 排序() throws Exception {
        Frequency f = new Frequency("1234567");
        assertEquals("1234567", f.toString());

        f = new Frequency("71");
        
        assertEquals("17", f.toString());
        assertTrue(f.contains(6));
    }
}
