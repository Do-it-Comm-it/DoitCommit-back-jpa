package com.web.doitcommit.service.image;

import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.domain.files.ImageRepository;
import com.web.doitcommit.domain.files.MemberImageRepository;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ImageServiceTest {

    @Autowired ImageService imageService;
    @Autowired MemberRepository memberRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired MemberImageRepository memberImageRepository;


    /**
     * 회원 이미지 저장
     */
    @Test
    void 회원이미지_저장() throws Exception{
        //given
        Member member = createMember("test@naver.com", "testNickname", "testUsername", "asd");
        MultipartFile imageFile = createImage("updateFile", "updateFilename.jpeg");

        //when
        Long imageId = imageService.imageMemberRegister(member, imageFile);

        //then
        Image image = imageRepository.findById(imageId).get();
    }



    private MultipartFile createImage(String name, String originalFilename) {
        return new MockMultipartFile(name, originalFilename, "image/jpeg", "some-image".getBytes());
    }

    private Member createMember(String email, String nickname, String username, String oAuthId) {
        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password("1111")
                .username(username)
                .provider(AuthProvider.GOOGLE)
                .interestTechSet(new HashSet<>(Arrays.asList("java")))
                .oauthId(oAuthId)
                .build();

        return memberRepository.save(member);
    }
}