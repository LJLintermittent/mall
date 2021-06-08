package com.learn.mall.member;


import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootTest
public class MallMemberApplicationTests {

    @Test
    public void contextLoads() {
        //抗修改性： 彩虹表 暴力破解
//        String s = DigestUtils.md5Hex("123456");
//        System.out.println(s);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //$2a$10$m0f/e703q5ceiAqGEOATZO10IMedUZHita5J2XNcvfuAYDDaHy/7e
        //$2a$10$0OprUkpaBftCp4u2T3QO/efr4d4ym.uh3xQFF.f7.mnR.IeXlVboO
//        String s = encoder.encode("123456");
        System.out.println(encoder.matches("123456"
                , "$2a$10$j5DpZfZkkcMwkDbvFLX9lOOTuGUuIpmhB4g5vpCupPYM/Zy7b49XG"));
        System.out.println(encoder.matches("123456"
                , "$2a$10$WIe4JoWgi8tuvfZn2snUDeHhL82MbO13hAoFACBVcgcGjwn7Dr.5e"));
    }

}
