package com.web.doitcommit.service.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.files.BoardImageRepository;
import com.web.doitcommit.dto.board.BoardRegDto;
import com.web.doitcommit.dto.board.BoardResDto;
import com.web.doitcommit.dto.board.ImageRegDto;
import com.web.doitcommit.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageService imageService;


    /**
     * 게시판 목록 조회
     */
    @Transactional(readOnly = true)
    public List<BoardResDto> getBoardList(int pageNo, int pageSize) {
        List<Board> result = boardRepository.getCustomBoardList(pageNo, pageSize);
        return result.stream().map(board -> new BoardResDto(board)).collect(Collectors.toList());
    }

    /**
     * 게시글 등록
     */
    public void createBoard(BoardRegDto boardRegDto, Long principalId) throws IOException {
        Board boardEntity = boardRegDto.toEntity(principalId);
        boardRepository.save(boardEntity);

        //등록안한 이미지 삭제 시작
        ImageRegDto[] allImageArr = boardRegDto.getAllImageArr();
        ImageRegDto[] imageArr = boardRegDto.getImageArr();

        List<String> imageList = new ArrayList<String>();
        List<String> allImageList = new ArrayList<String>();

        for(int i = 0; i< allImageArr.length; i++) {
            allImageList.add(allImageArr[i].getFilePath()+"/"+allImageArr[i].getFileNm());
        }
        for(int i = 0; i< imageArr.length; i++) {
            imageList.add(imageArr[i].getFilePath()+"/"+imageArr[i].getFileNm());
        }
        System.out.println(allImageList);
        System.out.println(imageList);
        allImageList.removeAll(imageList);
        if(allImageList.size() !=0){
            for(int i = 0; i< allImageList.size(); i++) {
                imageService.imageRemove(allImageList.get(i));
            }
        }

        //등록안한 이미지 삭제 끝

        //db에 이미지 등록
        if(imageArr.length != 0){
            imageService.imageBoardDbRegister(boardEntity, imageArr);
        }


    }

    /**
     * 태그 목록 조회
     */
    @Transactional(readOnly = true)
    public String[] getBoardList() {
        List<String> result = boardRepository.getCustomTagList();
        String[] tagArr = new String[result.size()];
        int size = 0;
        for(String temp : result){
            tagArr[size++] = temp;
        }
        return tagArr;
    }




}
