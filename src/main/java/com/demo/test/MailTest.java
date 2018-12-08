package com.demo.test;

import com.demo.constant.Constant;
import com.demo.util.CommonUtil;
import com.demo.util.MailUtil;

import javax.mail.MessagingException;

public class MailTest {



    public static void main(String [] args){
        try {
            MailUtil.sendMail(Constant.USER_NAME,Constant.PASSWORD,"1792695966@qq.com","测试！",CommonUtil.buildRandom(6)+"");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
