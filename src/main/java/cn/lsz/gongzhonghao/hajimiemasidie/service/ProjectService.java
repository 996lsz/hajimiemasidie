package cn.lsz.gongzhonghao.hajimiemasidie.service;

import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    public String sourceCode(String userName, String text){
        return "https://github.com/996lsz/hajimiemasidie";
    }

}
