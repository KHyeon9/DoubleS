package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.StudyGroupFixture;
import com.doubles.selfstudy.fixture.UserStudyGroupFixture;
import com.doubles.selfstudy.repository.ChatRoomRepository;
import com.doubles.selfstudy.repository.UserAccountCacheRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import com.doubles.selfstudy.repository.UserStudyGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static com.doubles.selfstudy.fixture.UserAccountFixture.get;
import static com.doubles.selfstudy.fixture.UserAccountFixture.getAdmin;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
class UserAccountServiceTest {

    @Autowired
    private UserAccountService userAccountService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private UserAccountCacheRepository userAccountCacheRepository;
    @MockBean
    private UserStudyGroupRepository userStudyGroupRepository;
    @MockBean
    private ChatRoomRepository chatRoomRepository;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_작동하는_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        String email = "test@email.com";
        String nickname = "nickname";

        UserAccount fixture = get(userId, password, email, nickname);

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userAccountRepository.save(any())).thenReturn(fixture);

        // Then
        assertDoesNotThrow(() -> userAccountService.regist(userId, password, email, nickname));
    }

    @Test
    void 회원가입시_아이디가_중복되면_에러를_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        String email = "test@email.com";
        String nickname = "nickname";

        UserAccount fixture = get(userId, password, email, nickname);

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userAccountRepository.save(any())).thenReturn(fixture);

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class, () -> userAccountService.regist(userId, password, email, nickname));
        assertEquals(ErrorCode.DUPLICATED_USER_ID, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_작동하는_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        UserAccount fixture = get(userId, password);

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        // Then
        assertDoesNotThrow(() -> userAccountService.login(userId, password));
    }

    @Test
    void 로그인시_userId로_가입한_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.login(userId, password));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_비밀번호가_틀린_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        String wrongPassword = "wrongPassword";
        UserAccount fixture = get(userId, password);

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.login(userId, wrongPassword));

        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }

    @Test
    void 유저_정보_수정_성공() {
        // Given
        String userId = "userId";
        String password = "password";
        UserAccount fixture = get(userId, password);

        String nickname = "nickname";
        String email = "email@email.com";
        String memo = "memo test";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(userAccountRepository.saveAndFlush(any())).thenReturn(
                get(userId, password, nickname, email, memo)
        );

        // Then
        assertDoesNotThrow(() -> userAccountService.modifiyUserInfo(userId, nickname, email, memo));
    }

    @Test
    void 유저_정보_수정시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String nickname = "nickname";
        String email = "email@email.com";
        String memo = "test memo";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.modifiyUserInfo(userId, nickname, email, memo));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 유저_비밀번호_수정_성공() {
        // Given
        String userId = "userId";
        String nowPassword = "now_password";
        String changePassword = "change_password";

        UserAccount fixture = get(userId, nowPassword);
        UserAccount changeFixture = get(userId, changePassword);

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(encoder.matches(nowPassword, fixture.getPassword())).thenReturn(true);
        when(userAccountRepository.saveAndFlush(any())).thenReturn(changeFixture);

        // Then
        assertDoesNotThrow(() -> userAccountService.modifiyUserPassword(userId, nowPassword, changePassword));
    }

    @Test
    void 유저_비밀번호_수정시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String nowPassword = "now_password";
        String changePassword = "change_password";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.modifiyUserPassword(userId, nowPassword, changePassword));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
    
    @Test
    void 유저_비밀번호_수정시_현재_비밀번호가_틀린_경우_에러_반환() {
        // Given
        String userId = "userId";
        String nowPassword = "now_password";
        String changePassword = "change_password";

        UserAccount fixture = get(userId, nowPassword);

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(encoder.matches(nowPassword, fixture.getPassword())).thenReturn(false);

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.modifiyUserPassword(userId, nowPassword, changePassword));

        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }

    @Test
    void 유저_삭제_성공() {
        // Given
        String userId = "userId";

        StudyGroup studyGroup = StudyGroupFixture.get();
        UserStudyGroup userStudyGroupMember = UserStudyGroupFixture.getMember(userId, studyGroup);
        UserAccount userAccount = userStudyGroupMember.getUserAccount();
        UserAccount admin = getAdmin("admin", "password");

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userAccountRepository.findById("admin"))
                .thenReturn(Optional.of(admin));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupMember));
        when(chatRoomRepository.findAllByUser(userAccount))
                .thenReturn(List.of());

        // Then
        assertDoesNotThrow(() -> userAccountService.deleteUserAccount(userId));
    }

    @Test
    void 유저_삭제시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.deleteUserAccount(userId));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 유저_삭제시_스터디_그룹_리더인_경우_에러_반환() {
        // Given
        String userId = "userId";
        StudyGroup studyGroup = StudyGroupFixture.get();
        UserStudyGroup userStudyGroupLeader = UserStudyGroupFixture.getLeader(userId, studyGroup);
        UserAccount userAccount = userStudyGroupLeader.getUserAccount();
        UserAccount admin = getAdmin("admin", "password");

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userAccountRepository.findById("admin"))
                .thenReturn(Optional.of(admin));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeader));
        when(chatRoomRepository.findAllByUser(userAccount))
                .thenReturn(List.of());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.deleteUserAccount(userId));

        assertEquals(ErrorCode.LEADER_NOT_EXIT, e.getErrorCode());
    }

    @Test
    void 유저_정보_조회_성공() {
        // Given
        String userId = "userId";
        String password = "password";
        UserAccount fixture = get(userId, password);
        UserStudyGroup userStudyGroup = UserStudyGroupFixture.get(fixture);

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(userStudyGroupRepository.findByUserAccount(fixture))
                .thenReturn(Optional.of(userStudyGroup));

        // Then
        assertDoesNotThrow(() -> userAccountService.getUserInfo(userId));
    }

    @Test
    void 유저_정보_조회시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> userAccountService.getUserInfo(userId));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}