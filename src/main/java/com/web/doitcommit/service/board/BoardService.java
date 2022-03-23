package com.web.doitcommit.service.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.hashtag.BoardHashtag;
import com.web.doitcommit.domain.hashtag.BoardHashtagRepository;
import com.web.doitcommit.domain.hashtag.TagCategory;
import com.web.doitcommit.domain.hashtag.TagCategoryRepository;
import com.web.doitcommit.dto.board.*;
import com.web.doitcommit.handler.exception.CustomException;
import com.web.doitcommit.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardHashtagRepository boardHashtagRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final ImageService imageService;


    /**
     * 게시판 목록 조회
     */
    @Transactional(readOnly = true)
    public List<BoardListResDto> getBoardList(int pageNo, int pageSize) {
        List<Board> result = boardRepository.getCustomBoardList(pageNo, pageSize);
        return result.stream().map(board -> new BoardListResDto(board)).collect(Collectors.toList());
    }

    /**
     * 게시글 등록
     */
    @Transactional
    public void createBoard(BoardRegDto boardRegDto, Long principalId) {
        Board boardEntity = boardRegDto.toEntity(principalId);

        Board board = boardRepository.save(boardEntity);

        if(boardRegDto.getBoardHashtag() !=null){
            for (int i = 0; i < boardRegDto.getBoardHashtag().size(); i++) {
                TagCategory tagCategory = new TagCategory(boardRegDto.getBoardHashtag().get(i));
                BoardHashtag boardHashtag = new BoardHashtag(board,tagCategory);
                boardHashtagRepository.save(boardHashtag);
            }
        }

        ImageRegDto[] allImageArr = new ImageRegDto[0];
        if(boardRegDto.getAllImageArr() !=null){
            allImageArr = boardRegDto.getAllImageArr();
        }

        ImageRegDto[] imageArr = new ImageRegDto[0];
        if(boardRegDto.getImageArr() !=null){
            imageArr = boardRegDto.getImageArr();
        }

        //S3에서 이미지 삭제
        if(allImageArr.length != 0) {
            removeImage(allImageArr, imageArr);
        }
        //DB에 이미지 등록
        if(imageArr.length != 0) {
            registerImage(board, imageArr);
        }
    }

    /**
     * S3에서 이미지 삭제
     */
    @Transactional
    public void removeImage(ImageRegDto[] allImageArr,
                            ImageRegDto[] imageArr) {
        List<String> allImageList = new ArrayList<String>();
        List<String> imageList = new ArrayList<String>();
        //반환한 전체 url list
        for (int i = 0; i < allImageArr.length; i++) {
            allImageList.add(allImageArr[i].getFilePath() + "/" + allImageArr[i].getFileNm());
        }
        //등록한 url list
        for (int i = 0; i < imageArr.length; i++) {
            imageList.add(imageArr[i].getFilePath() + "/" + imageArr[i].getFileNm());
        }
        //S3에서 삭제해야할 url
        allImageList.removeAll(imageList);
        if (allImageList.size() != 0) {
            for (int i = 0; i < allImageList.size(); i++) {
                imageService.imageRemove(allImageList.get(i));
            }
        }
    }

    /**
     * DB에 이미지 등록
     */
    @Transactional
    public void registerImage(Board board, ImageRegDto[] imageArr) {
        if (imageArr.length != 0) {
            imageService.imageBoardDbRegister(board, imageArr);
        }
    }

    /**
     * 게시글 조회
     */
    @Transactional(readOnly = true)
    public BoardResDto GetBoard(Long boardId) {
        Board boardEntity = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));
        boardEntity.changeBoardCnt();
        BoardResDto boardResDto = new BoardResDto(boardEntity);
        List boardHashtags = boardRepository.getCustomTagList(boardId);

        if(boardHashtags.size() != 0){
            boardResDto.setBoardHashtag(boardHashtags);
        }
        return boardResDto;
    }

    /**
     * 태그 목록 조회
     */
    @Transactional(readOnly = true)
    public List<TagCategoryResDto> getBoardTagList() {
        List<TagCategory> result = tagCategoryRepository.findAll();
        return result.stream().map(tag -> new TagCategoryResDto(tag)).collect(Collectors.toList());
    }
}
