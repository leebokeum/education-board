package com.demo;

import com.demo.board.domain.Content;
import com.demo.board.infra.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IldongCommonBoardApplication implements ApplicationRunner {

    @Autowired
    private ContentRepository contentRepository;

    public static void main(String[] args) {
        SpringApplication.run(IldongCommonBoardApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("##########Spring Boot 초기화#############");
        for(int i=0; i <10; i++){
            contentRepository.save(Content.builder()
                    .title("제목입니다." + i)
                    .content("내용입니다." + i)
                    .password("1234")
                    .writer("leebokeum")
                    .email("leebokeum@hanmil.net").build());
        }

    }
}
