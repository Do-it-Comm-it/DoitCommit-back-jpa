package com.web.doitcommit.service.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.boardHistory.BoardHistory;
import com.web.doitcommit.domain.boardHistory.BoardHistoryRepository;
import com.web.doitcommit.domain.files.BoardImage;
import com.web.doitcommit.domain.files.MemberImage;
import com.web.doitcommit.domain.hashtag.BoardHashtag;
import com.web.doitcommit.domain.hashtag.BoardHashtagRepository;
import com.web.doitcommit.domain.hashtag.TagCategory;
import com.web.doitcommit.domain.hashtag.TagCategoryRepository;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.dto.board.*;
import com.web.doitcommit.dto.page.PageRequestDto;
import com.web.doitcommit.dto.page.ScrollResultDto;
import com.web.doitcommit.handler.exception.CustomException;
import com.web.doitcommit.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardHashtagRepository boardHashtagRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final BoardHistoryRepository boardHistoryRepository;


    /**
     * 게시글 목록 조회
     */
    @Transactional(readOnly = true)
    public ScrollResultDto<BoardListResDto, Object[]> getBoardList(PageRequestDto requestDto, Long principalId) {

        Pageable pageable = requestDto.getPageable(Sort.by(Sort.Direction.DESC, requestDto.getSortType()));

        Page<Object[]> results = boardRepository.getBoardListBySearch(
                requestDto.getKeyword(), requestDto.getTagCategoryId(),
                requestDto.getBoardCategoryId(), pageable);

        Function<Object[], BoardListResDto> fn = (arr -> {

            //게시글 첫번째 이미지
            Board board = (Board) arr[0];

            String thumbnailUrl = null;
            if (board.getBoardImageList() != null && !board.getBoardImageList().isEmpty()) {
                BoardImage boardImage = board.getBoardImageList().get(0);
                thumbnailUrl = imageService.getImage(boardImage.getFilePath(), boardImage.getFileNm());
            }

            //작성자 프로필 이미지
            MemberImage memberImage = (MemberImage) arr[1];

            String writerImageUrl = null;
            if (memberImage != null) {
                //소셜 이미지일 경우
                if(memberImage.isSocialImg()){
                    writerImageUrl = memberImage.getImageUrl();
                }
                //s3 이미지일 경우
                else{
                    writerImageUrl = imageService.getImage(memberImage.getFilePath(), memberImage.getFileNm());
                }
            }

            return new BoardListResDto((Board) arr[0], writerImageUrl, thumbnailUrl, (int) arr[2], (int) arr[3], principalId);
        });

        return new ScrollResultDto<>(results, fn);
    }

    /**
     * 게시글 사용자 개수 지정 조회
     */
    @Transactional(readOnly = true)
    public List<MainViewBoardListResDto> getCustomLimitBoardList(int limit, String order, Long principalId) {

        List<Object[]> results = boardRepository.getCustomLimitBoardList(limit, order);

        return results.stream().map(arr -> {

            //게시글 첫번째 이미지
            Board board = (Board) arr[0];

            String thumbnailUrl = null;
            if (board.getBoardImageList() != null && !board.getBoardImageList().isEmpty()) {
                BoardImage boardImage = board.getBoardImageList().get(0);
                thumbnailUrl = imageService.getImage(boardImage.getFilePath(), boardImage.getFileNm());
            }

            //작성자 프로필 이미지
            MemberImage memberImage = (MemberImage) arr[1];

            String writerImageUrl = null;
            if (memberImage != null) {
                //소셜 이미지일 경우
                if(memberImage.isSocialImg()){
                    writerImageUrl = memberImage.getImageUrl();
                }
                //s3 이미지일 경우
                else{
                    writerImageUrl = imageService.getImage(memberImage.getFilePath(), memberImage.getFileNm());
                }
            }

            return new MainViewBoardListResDto((Board) arr[0], writerImageUrl, thumbnailUrl, (int) arr[2], principalId);
        }).collect(Collectors.toList());
    }

    /**
     * 북마크 게시글 리스트 조회
     */
    @Transactional(readOnly = true)
    public ScrollResultDto<BoardListResDto, Object[]> getBookmarkList(PageRequestDto requestDto, Long principalId) {

        Pageable pageable = requestDto.getPageable(Sort.by("boardId").ascending());

        Page<Object[]> results = boardRepository.getBoardListByBookmark(
                requestDto.getKeyword(), requestDto.getTagCategoryId()
                ,requestDto.getBoardCategoryId(), principalId, pageable);

        Function<Object[], BoardListResDto> fn = (arr -> {

            //게시글 첫번째 이미지
            Board board = (Board) arr[0];

            String thumbnailUrl = null;
            if (board.getBoardImageList() != null && !board.getBoardImageList().isEmpty()) {
                BoardImage boardImage = board.getBoardImageList().get(0);
                thumbnailUrl = imageService.getImage(boardImage.getFilePath(), boardImage.getFileNm());
            }

            //작성자 프로필 이미지
            MemberImage memberImage = (MemberImage) arr[1];

            String writerImageUrl = null;
            if (memberImage != null) {
                //소셜 이미지일 경우
                if(memberImage.isSocialImg()){
                    writerImageUrl = memberImage.getImageUrl();
                }
                //s3 이미지일 경우
                else{
                    writerImageUrl = imageService.getImage(memberImage.getFilePath(), memberImage.getFileNm());
                }
            }

            return new BoardListResDto((Board) arr[0], writerImageUrl, thumbnailUrl, (int) arr[2], (int) arr[3], principalId);
        });

        return new ScrollResultDto<>(results, fn);
    }

    /**
     * 조회한 게시글 히스토리 내역 조회
     */
    @Transactional(readOnly = true)
    public ScrollResultDto<BoardListResDto, Object[]> getBoardHistoryList(PageRequestDto requestDto, Long principalId){

        Pageable pageable = requestDto.getPageable();

        Page<Object[]> results = boardRepository.getBoardListByBoardHistory(requestDto.getKeyword(),
                requestDto.getTagCategoryId(), requestDto.getBoardCategoryId(), principalId, pageable);

        Function<Object[], BoardListResDto> fn = (arr -> {

            //게시글 첫번째 이미지
            Board board = (Board) arr[0];

            String thumbnailUrl = null;
            if (board.getBoardImageList() != null && !board.getBoardImageList().isEmpty()) {
                BoardImage boardImage = board.getBoardImageList().get(0);
                thumbnailUrl = imageService.getImage(boardImage.getFilePath(), boardImage.getFileNm());
            }

            //작성자 프로필 이미지
            MemberImage memberImage = (MemberImage) arr[1];

            String writerImageUrl = null;
            if (memberImage != null) {
                //소셜 이미지일 경우
                if(memberImage.isSocialImg()){
                    writerImageUrl = memberImage.getImageUrl();
                }
                //s3 이미지일 경우
                else{
                    writerImageUrl = imageService.getImage(memberImage.getFilePath(), memberImage.getFileNm());
                }
            }

            return new BoardListResDto((Board) arr[0], writerImageUrl, thumbnailUrl, (int) arr[2], (int) arr[3], principalId);
        });

        return new ScrollResultDto<>(results, fn);
    }




    /**
     * 회원별 - 작성한 게시글 리스트 사용자 개수 지정 조회
     */
    @Transactional(readOnly = true)
    public BoardListOfMemberResDto getCustomLimitBoardListOfMember(int limit, Long memberId){

        Object[] arr = memberRepository.findWithImageByMemberId(memberId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        Member member = (Member) arr[0];

        //회원 프로필 이미지
        MemberImage memberImage = (MemberImage) arr[1];

        String memberImageUrl = null;
        if (memberImage != null) {
            //소셜 이미지일 경우
            if(memberImage.isSocialImg()){
                memberImageUrl = memberImage.getImageUrl();
            }
            //s3 이미지일 경우
            else{
                memberImageUrl = imageService.getImage(memberImage.getFilePath(), memberImage.getFileNm());
            }
        }

        //작성자의 다른 게시글 총 개수 조회
        long totalCnt = boardRepository.countByMemberId(memberId);

        //작성자의 다른 게시글 리스트 조회
        List<Board> boardList = boardRepository.getCustomLimitBoardListOfMember(limit, memberId);

        List<BoardOfMemberResDto> boardOfMemberResDtoList = boardList.stream().map(board -> {

            //게시글 첫번째 이미지
            String thumbnailUrl = null;
            if (board.getBoardImageList() != null && !board.getBoardImageList().isEmpty()) {
                BoardImage boardImage = board.getBoardImageList().get(0);
                thumbnailUrl = imageService.getImage(boardImage.getFilePath(), boardImage.getFileNm());
            }
            return new BoardOfMemberResDto(board, thumbnailUrl);
        }).collect(Collectors.toList());

        return new BoardListOfMemberResDto(member, memberImageUrl, totalCnt, boardOfMemberResDtoList);
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
    @Transactional
    public BoardResDto GetBoard(Long boardId, Long principalId) {
        Board boardEntity = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        //조회수 증가
        boardEntity.changeBoardCnt();

        //게시글 히스토리 추가
        addBoardHistory(boardEntity, principalId);

        BoardResDto boardResDto = new BoardResDto(boardEntity);
        Map<Long, String> savedImageIdsAndUrl = new HashMap<>();

        //이미지id와 이미지url map
        if (boardEntity.getBoardImageList().size() != 0) {
            for (int i = 0; i < boardEntity.getBoardImageList().size(); i++) {
                savedImageIdsAndUrl.put(boardEntity.getBoardImageList().get(i).getImageId(),
                        imageService.getImage(boardEntity.getBoardImageList().get(i).getFilePath(), boardEntity.getBoardImageList().get(i).getFileNm()));
            }
            boardResDto.changeSavedImageIdsAndUrl(savedImageIdsAndUrl);
        }

        //로그인한 사용자의 북마크 글인지 유무
        for (int i = 0; i < boardEntity.getBookmarkList().size(); i++) {
            Long memberId = boardEntity.getBookmarkList().get(i).getMember().getMemberId();
            if (memberId.equals(principalId)) {
                boardResDto.changeMyBookmark(true);
                break;
            }
        }

        //로그인한 사용자가 좋아요한 글인지 유무
        for (int i = 0; i < boardEntity.getHeartList().size(); i++) {
            Long memberId = boardEntity.getHeartList().get(i).getMember().getMemberId();
            if (memberId.equals(principalId)) {
                boardResDto.changeMyHeart(true);
                break;
            }
        }

        //해시태그 목록
        List boardHashtags = boardRepository.getCustomTagList(boardId);

        if (boardHashtags.size() != 0) {
            boardResDto.setBoardHashtag(boardHashtags);
        }
        return boardResDto;
    }

    private void addBoardHistory(Board boardEntity, Long principalId) {

        //로그인 상태일 경우
        if(principalId != null){

            Optional<BoardHistory> result =
                    boardHistoryRepository.findByBoardIdAndMemberId(boardEntity.getBoardId(), principalId);

            //기존 내역이 존재하는 경우
            if(result.isPresent()){
                BoardHistory boardHistory = result.get();

                //조회 일시를 현재로 변경
                boardHistory.changeViewDateTimeToNow();
            }
            //신규 조회일 경우
            else{
                BoardHistory boardHistory = BoardHistory.builder()
                        .board(boardEntity)
                        .member(Member.builder().memberId(principalId).build())
                        .viewDateTime(LocalDateTime.now())
                        .build();

                boardHistoryRepository.save(boardHistory);
            }
        }
    }

    /**
     * 태그 목록 조회
     */
    @Transactional(readOnly = true)
    public List<TagCategoryResDto> getBoardTagList() {
        List<TagCategory> result = tagCategoryRepository.findAll();
        return result.stream().map(tag -> new TagCategoryResDto(tag)).collect(Collectors.toList());
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void removeBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));
        boardRepository.deleteById(boardId);
        List<BoardImage> imageInfoArr = board.getBoardImageList();

        //s3에서 삭제된 게시글 이미지 삭제
        if (imageInfoArr.size() != 0) {
            for (BoardImage imageInfo : imageInfoArr) {
                imageService.imageRemove(imageInfo.getFilePath() + "/" + imageInfo.getFileNm());
            }
        }
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void modifyBoard(BoardUpdateDto boardUpdateDto) {

        Board board = boardRepository.findById(boardUpdateDto.getBoardId()).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        //if(boardUpdateDto.getCategoryId() != null)  board.changeCategoryId(boardUpdateDto.getCategoryId());
        if(boardUpdateDto.getBoardTitle() != null)  board.changeTitle(boardUpdateDto.getBoardTitle());
        if(boardUpdateDto.getBoardContent() != null)  board.changeContent(boardUpdateDto.getBoardContent());

    }
}