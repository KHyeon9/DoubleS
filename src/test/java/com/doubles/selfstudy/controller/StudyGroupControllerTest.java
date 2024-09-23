package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.StudyGroupRequest;
import com.doubles.selfstudy.dto.studygroup.StudyGroupDto;
import com.doubles.selfstudy.dto.studygroup.UserStudyGroupDto;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.StudyGroupFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.fixture.UserStudyGroupFixture;
import com.doubles.selfstudy.service.StudyGroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class StudyGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudyGroupService studyGroupService;

    @Test
    @WithMockUser
    void 스터디_그룹_생성_성공() throws Exception {
        // Given
        String studyGroupName = "studyGroupName";
        String description = "description";

        // When & Then
        mockMvc.perform(post("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsBytes(new StudyGroupRequest(studyGroupName, description))
                        )
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_생성시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given
        String studyGroupName = "studyGroupName";
        String description = "description";

        // When & Then
        mockMvc.perform(post("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsBytes(new StudyGroupRequest(studyGroupName, description))
                        )
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_수정_성공() throws Exception {
        // Given
        String studyGroupName = "modify_studyGroupName";
        String description = "modify_description";

        // When
        when(studyGroupService.modifyStudyGroup(any(), eq(studyGroupName), eq(description)))
                .thenReturn(StudyGroupDto.fromEntity(StudyGroupFixture.get(studyGroupName, description)));

        // Then
        mockMvc.perform(put("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupRequest(studyGroupName, description)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_수정시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given
        String studyGroupName = "modify_studyGroupName";
        String description = "modify_description";

        // When
        when(studyGroupService.modifyStudyGroup(any(), eq(studyGroupName), eq(description)))
                .thenReturn(StudyGroupDto.fromEntity(StudyGroupFixture.get()));

        // Then
        mockMvc.perform(put("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsBytes(new StudyGroupRequest(studyGroupName, description))
                        )
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_수정시_요청자가_다른_경우_에러_발생() throws Exception {
        // Given
        String studyGroupName = "modify_studyGroupName";
        String description = "modify_description";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupService).modifyStudyGroup(any(), eq(studyGroupName), eq(description));

        // Then
        mockMvc.perform(put("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsBytes(new StudyGroupRequest(studyGroupName, description))
                        )
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_수정시_그룹이_없는_경우_에러_발생() throws Exception {
        // Given
        String studyGroupName = "modify_studyGroupName";
        String description = "modify_description";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.STUDY_GROUP_NOT_FOUND))
                .when(studyGroupService).modifyStudyGroup(any(), eq(studyGroupName), eq(description));

        // Then
        mockMvc.perform(put("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsBytes(new StudyGroupRequest(studyGroupName, description))
                        )
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.STUDY_GROUP_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_수정시_리더가_아닌_경우_에러_발생() throws Exception {
        // Given
        String studyGroupName = "modify_studyGroupName";
        String description = "modify_description";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupService).modifyStudyGroup(any(), eq(studyGroupName), eq(description));

        // Then
        mockMvc.perform(put("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsBytes(new StudyGroupRequest(studyGroupName, description))
                        )
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_삭제_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(delete("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_삭제시_로그인이_안된_경우_에러_반환() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(delete("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_삭제시_요청자가_다른_경우_에러_반환() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupService).deleteStudyGroup(any());

        // Then
        mockMvc.perform(delete("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_삭제시_그룹이_없는_경우_에러_반환() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.STUDY_GROUP_NOT_FOUND))
                .when(studyGroupService).deleteStudyGroup(any());

        // Then
        mockMvc.perform(delete("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.STUDY_GROUP_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_삭제시_리더가_아닌_경우_에러_반환() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupService).deleteStudyGroup(any());

        // Then
        mockMvc.perform(delete("/api/main/study_group")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_초대_알림_전송_성공() throws Exception {
        // Given
        String inviteUserId = "inviteUserId";

        // When&Then
        mockMvc.perform(post("/api/main/study_group/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("inviteUserId", inviteUserId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_초대_알림_전송시_로그인을_안한_경우_에러_발생() throws Exception {
        // Given
        String inviteUserId = "inviteUserId";

        // When&Then
        mockMvc.perform(post("/api/main/study_group/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("inviteUserId", inviteUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_가입_성공() throws Exception {
        // Given
        String leaderUserId = "leaderUserId";

        // When & Then
        mockMvc.perform(post("/api/main/study_group/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("leaderUserId", leaderUserId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_가입시_로그인이_안된_경우_에러_반환() throws Exception {
        // Given
        String leaderUserId = "leaderUserId";

        // When & Then
        mockMvc.perform(post("/api/main/study_group/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("leaderUserId", leaderUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_가입시_그룹이_꽉찬_경우_에러_반환() throws Exception {
        // Given
        String leaderUserId = "leaderUserId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.STUDY_GROUP_FULL))
                .when(studyGroupService).joinStudyGroupMember(any(), any());

        // Then
        mockMvc.perform(post("/api/main/study_group/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("leaderUserId", leaderUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.STUDY_GROUP_FULL.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_가입시_초대_유저가_없는_경우_에러_반환() throws Exception {
        // Given
        String leaderUserId = "leaderUserId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND))
                .when(studyGroupService).joinStudyGroupMember(any(), any());

        // Then
        mockMvc.perform(post("/api/main/study_group/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("leaderUserId", leaderUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_가입시_초대시_리더가_아닌_경우_에러_반환() throws Exception {
        // Given
        String leaderUserId = "leaderUserId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupService).joinStudyGroupMember(any(), any());

        // Then
        mockMvc.perform(post("/api/main/study_group/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("leaderUserId", leaderUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룸_리더_변경에_성공() throws Exception {
        // Given
        String changeLeaderId = "changeLeaderId";
        UserAccount changeUser = UserAccountFixture.get(changeLeaderId, "password");

        // When
        when(studyGroupService.changeStudyGroupLeader(any(), eq(changeLeaderId)))
                .thenReturn(UserStudyGroupDto.fromEntity(UserStudyGroupFixture.get(changeUser)));

        // Then
        mockMvc.perform(put("/api/main/study_group/change_leader")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("changeLeaderId", changeLeaderId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룸_리더_변경시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given
        String changeLeaderId = "changeLeaderId";
        UserAccount changeUser = UserAccountFixture.get(changeLeaderId, "password");

        // When
        when(studyGroupService.changeStudyGroupLeader(any(), eq(changeLeaderId)))
                .thenReturn(UserStudyGroupDto.fromEntity(UserStudyGroupFixture.get(changeUser)));

        // Then
        mockMvc.perform(put("/api/main/study_group/change_leader")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("changeLeaderId", changeLeaderId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룸_리더_변경시_리더가_아니거나_스터디_그룹이_다른_경우_에러_발생() throws Exception {
        // Given
        String changeLeaderId = "changeLeaderId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupService).changeStudyGroupLeader(any(), eq(changeLeaderId));

        // Then
        mockMvc.perform(put("/api/main/study_group/change_leader")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("changeLeaderId", changeLeaderId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룸_리더_변경시_유저가_없는_에러_발생() throws Exception {
        // Given
        String changeLeaderId = "changeLeaderId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND))
                .when(studyGroupService).changeStudyGroupLeader(any(), eq(changeLeaderId));

        // Then
        mockMvc.perform(put("/api/main/study_group/change_leader")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("changeLeaderId", changeLeaderId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룸_리더_변경시_스터디_그룹이_없는_경우_에러_발생() throws Exception {
        // Given
        String changeLeaderId = "changeLeaderId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_STUDY_GROUP_NOT_FOUND))
                .when(studyGroupService).changeStudyGroupLeader(any(), eq(changeLeaderId));

        // Then
        mockMvc.perform(put("/api/main/study_group/change_leader")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("changeLeaderId", changeLeaderId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_STUDY_GROUP_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_강제_퇴장_성공() throws Exception {
        // Given
        String deleteUserId = "deleteUserId";

        // When & Then
        mockMvc.perform(delete("/api/main/study_group/force_exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("deleteUserId", deleteUserId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_강제_퇴장시_로그인이_안된_경우_에러_반환() throws Exception {
        // Given
        String deleteUserId = "deleteUserId";

        // When & Then
        mockMvc.perform(delete("/api/main/study_group/force_exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("deleteUserId", deleteUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_강제_퇴장시_삭제할_유저가_없는_경우_에러_반환() throws Exception {
        // Given
        String deleteUserId = "deleteUserId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND))
                .when(studyGroupService).forceExitStudyGroupMember(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/study_group/force_exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("deleteUserId", deleteUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_강제_퇴장시_리더가_아닌_경우_에러_반환() throws Exception {
        // Given
        String deleteUserId = "deleteUserId";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupService).forceExitStudyGroupMember(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/study_group/force_exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("deleteUserId", deleteUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_탈퇴_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(delete("/api/main/study_group/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_탈퇴시_로그인_안한_경우_에러_발생() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(delete("/api/main/study_group/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_탈퇴시_유저가_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND))
                .when(studyGroupService).exitStudyGroupMySelf(any());

        // Then
        mockMvc.perform(delete("/api/main/study_group/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_탈퇴시_가입된_스터디_그룹이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_STUDY_GROUP_NOT_FOUND))
                .when(studyGroupService).exitStudyGroupMySelf(any());

        // Then
        mockMvc.perform(delete("/api/main/study_group/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_STUDY_GROUP_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_리더가_그룹원_2명이상_있는데_탈퇴할_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.LEADER_NOT_EXIT))
                .when(studyGroupService).exitStudyGroupMySelf(any());

        // Then
        mockMvc.perform(delete("/api/main/study_group/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.LEADER_NOT_EXIT.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 나의_스터디_그룹의_가입_인원_리스트_조회_성공() throws Exception {
        // Given
        List<UserStudyGroupDto> myStudyGroupMember =
                studyGroupService.studyGroupMemberList(any());

        // When
        when(myStudyGroupMember).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/study_group/member")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 나의_스터디_그룹의_가입_인원_리스트를_로그인하지_않고_조회한_경우_에러_반환() throws Exception {
        // Given
        List<UserStudyGroupDto> myStudyGroupMember =
                studyGroupService.studyGroupMemberList(any());

        // When
        when(myStudyGroupMember).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/study_group/member")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 나의_스터디_그룹이_없는_경우_에러_반환() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_STUDY_GROUP_NOT_FOUND))
                .when(studyGroupService).studyGroupMemberList(any());

        // Then
        mockMvc.perform(get("/api/main/study_group/member")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_STUDY_GROUP_NOT_FOUND.getStatus().value()));
    }
}