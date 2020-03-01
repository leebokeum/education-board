package com.demo.board.controller;

import com.demo.board.domain.BadRequestException;
import com.demo.board.domain.Content;
import com.demo.board.domain.ContentDto;
import com.demo.board.domain.ErrorResponse;
import com.demo.board.infra.ContentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

//https://github.com/cheese10yun/spring-jpa-best-practices/blob/master/src/main/java/com/cheese/springjpa/Account/api/AccountController.java
@RestController
public class BoardRestController {
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BoardRestController(ContentRepository contentRepository, ModelMapper modelMapper) {
        this.contentRepository = contentRepository;
        this.modelMapper = modelMapper;
    }

    /***
     * @GetMapping("/content")
     * 1차 리팩토링
     ****/

/*    @GetMapping("/content")
    ResponseEntity content(@Param("id") int id){
        Optional<Content> content= contentRepository.findById(id);

        if(!content.isPresent()){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode("잘못된 요청");
            errorResponse.setMessage("데이터가 존재하지 않습니다.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        ContentDto contentDto = modelMapper.map(content.get(), ContentDto.class);
        return new ResponseEntity<>(contentDto, HttpStatus.OK);
    }*/

    /***
     * @GetMapping("/content")
     * 2차 리팩토링
     ****/
/*    @GetMapping("/content")
    ResponseEntity content(@Param("id") int id){
        Optional<Content> content= contentRepository.findById(id);
        if(!content.isPresent()){
            throw new BadRequestException("데이터가 존재하지 않습니다.");
        }

        ContentDto contentDto = modelMapper.map(content.get(), ContentDto.class);
        return new ResponseEntity<>(contentDto, HttpStatus.OK);
    }*/

    /***
     * @GetMapping("/content")
     * 3차 리팩토링
     ****/

    @GetMapping("/content")
    ResponseEntity content(@Param("id") int id){
        Optional<Content> content= contentRepository.findById(id);
        content.orElseThrow(() ->new BadRequestException("데이터가 존재하지 않습니다."));

        ContentDto contentDto = modelMapper.map(content.get(), ContentDto.class);
        return new ResponseEntity<>(contentDto, HttpStatus.OK);
    }

    /***
     * @GetMapping("/content")
     * 1차 리팩토링
     ****/
/*    @GetMapping("/contents")
    ResponseEntity contents(){
        List<ContentDto> contents = contentRepository.findAll().stream().map(conetnt -> modelMapper.map(conetnt, ContentDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }*/
    /***
     * @GetMapping("/content")
     * 2차 리팩토링
     ****/
/*    @GetMapping("/contents")
    ResponseEntity contents(){
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<Content> contents = contentRepository.findAll(pageable);
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }*/

    @GetMapping("/contents")
    ResponseEntity contents(@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 5) Pageable pageable){
        Page<Content> contents = contentRepository.findAll(pageable);
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }

    @PostMapping("/content")
    ResponseEntity createContent(@Valid @RequestBody Content content, BindingResult bindingResult){
        // 예외처리 -> bindingResult.getFieldError().getDefaultMessage() 를 통하여 리턴받을 수 있음
        if(bindingResult.hasErrors()){
            ErrorResponse errorResponse = new ErrorResponse();
            StringBuilder builder = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                builder.append("[");
                builder.append(fieldError.getField());
                builder.append("](은)는 ");
                builder.append(fieldError.getDefaultMessage());
                builder.append(" 입력된 값: [");
                builder.append(fieldError.getRejectedValue());
                builder.append("]");
            }

            errorResponse.setMessage(builder.toString());
            errorResponse.setCode("BindingResultException");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        ContentDto contentDto = modelMapper.map(contentRepository.save(content), ContentDto.class);
        return new ResponseEntity<>(contentDto, HttpStatus.CREATED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(BadRequestException e){
        System.out.println("handleBadRequestException 발생");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode("BadRequestException");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
