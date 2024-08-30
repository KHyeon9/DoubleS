<template>
  <div v-if="studyGroupName === ''" class="d-sm-flex justify-content-between mb-3">
    <div>
      <router-link to="/main/study_group/new" class="btn btn-icon bg-gradient-primary">
        New Study Group
      </router-link>
    </div>
  </div>
  <div class="row">
    <div class="col-12">
      <div v-if="studyGroupName !== ''" class="card card-body mb-5">
        <div class="row align-items-center">
          <div class="col-12 my-auto">
            <div class="h-100">
              <div class="d-flex justify-content-between align-items-center">
                <h5 class="mb-1 font-weight-bolder">
                  {{ studyGroupData.studyGroupName }}
                </h5>
                <router-link 
                  :to="`/main/study_group/modify/${studyGroupData.id}`"
                  class="me-2 mt-2"
                >
                  <i class="material-icons text-lg position-relative">settings</i>
                </router-link>
              </div>
              <p class="mb-0 font-weight-normal text-sm">
                {{ studyGroupData.description }}
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="card my-4">
        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
          <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
            <h6 class="text-white text-capitalize ps-3">스터디 그룹원</h6>
          </div>
        </div>
        <div class="card-body px-0 pb-2">
          <div class="table-responsive p-0">
            <!-- 스터디 그룹 정보가 없을 때 -->
            <div v-if="studyGroupName === ''" class="m-5">
              <p class="text-center">스터디 그룹에 가입되어 있지 않습니다.</p>
            </div>

            <!-- 스터디 그룹 정보가 있을 때 -->
            <table v-else class="table align-items-center mb-0">
              <thead>
                <tr>
                  <th class="text-uppercase text-secondary text-xs font-weight-bolder opacity-7">유저 정보</th>
                  <th class="text-uppercase text-secondary text-xs font-weight-bolder opacity-7 ps-2">직책</th>
                  <th class="text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-7">가입 날짜</th>
                  <th class="text-secondary opacity-7"></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="userInfo in studyGroupMemberList" :key="userInfo.userId">
                  <td>
                    <div class="d-flex px-2 py-1">
                      <div class="d-flex flex-column justify-content-center">
                        <h6 class="mb-0 text-sm">{{ userInfo.nickname }} ({{ userInfo.userId }})</h6>
                        <p class="text-xs text-secondary mb-0">{{ userInfo.email }}</p>
                      </div>
                    </div>
                  </td>
                  <td>
                    <p class="text-sm font-weight-bold mb-0">{{ userInfo.position }}</p>
                  </td>
                  <td class="align-middle text-center">
                    <span class="text-secondary text-sm font-weight-bold">{{ formatDate(userInfo.joinedAt) }}</span>
                  </td>
                  <td class="align-middle">
                    <a v-if="myPosition === 'Leader' && userInfo.userId !== userId" class="text-secondary font-weight-bold text-sm" role="button">
                      <i class="material-icons text-lg position-relative pointer">logout</i>
                    </a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-12">
      <div class="card my-4">
        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
          <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
            <h6 class="text-white text-capitalize ps-3">스터디 그룹 게시판</h6>
          </div>
        </div>
        <div class="card-body px-0 pb-2">
          <div class="table-responsive p-0">
            <!-- 스터디 그룹 정보가 없을 때 -->
            <div v-if="studyGroupName === ''" class="m-5">
              <p class="text-center">스터디 그룹에 가입되어 있지 않습니다.</p>
            </div>

            <!-- 스터디 그룹 게시글이 없을 때 -->
            <div v-else-if="studyGroupBoardList.length == 0" class="m-5">
              <p class="text-center">게시글이 없습니다.</p>
            </div>

            <!-- 스터디 그룹 정보가 있을 때 -->
            <table v-else class="table align-items-center justify-content-center mb-0">
              <thead>
                <tr>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Project</th>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Budget</th>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Status</th>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder text-center opacity-7 ps-2">Completion</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <div class="d-flex px-2">
                      <div>
                        <img src="/assets/img/small-logos/logo-asana.svg" class="avatar avatar-sm rounded-circle me-2" alt="spotify">
                      </div>
                      <div class="my-auto">
                        <h6 class="mb-0 text-sm">Asana</h6>
                      </div>
                    </div>
                  </td>
                  <td>
                    <p class="text-sm font-weight-bold mb-0">$2,500</p>
                  </td>
                  <td>
                    <span class="text-xs font-weight-bold">working</span>
                  </td>
                  <td class="align-middle text-center">
                    <div class="d-flex align-items-center justify-content-center">
                      <span class="me-2 text-xs font-weight-bold">60%</span>
                      <div>
                        <div class="progress">
                          <div class="progress-bar bg-gradient-info" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;"></div>
                        </div>
                      </div>
                    </div>
                  </td>
                  <td class="align-middle">
                    <button class="btn btn-link text-secondary mb-0">
                      <i class="fa fa-ellipsis-v text-xs"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <nav  v-if="studyGroupName !== ''" class="mt-4 me-3 nav nav-bar d-flex justify-content-between" aria-label="Page navigation">
            <!-- Create Board 버튼 -->
            <router-link to="/main/study_group/board/new" class="btn btn-icon bg-gradient-primary ms-3">
              Create Board
            </router-link>
            <ul class="pagination">
              <li class="page-item" :class="{ disabled: page === 0}">
                <a class="page-link" @click.prevent="prevPageAndFetch">‹</a>
              </li>
              <li 
                v-for="pageNumber in paginatedPageNumbers"
                :key="pageNumber"
                class="page-item"
                :class="{ active: pageNumber - 1 === page }"
              >
                <a class="page-link " @click.prevent="goToPageAndFetch(pageNumber - 1)">{{ pageNumber }}</a>
              </li>
              <li class="page-item" :class="{ disabled: page === totalPages - 1 }">
                <a class="page-link" @click.prevent="nextPageAndFetch">›</a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import apiClient from '../../../config/authConfig';
  import { ref, onMounted, computed } from 'vue';
  import { useFormat } from '../../../utils/format';
  import { useAuthStore } from '../../../store/authStore.js';

  const authStore = useAuthStore();
  const userId = authStore.userId;
  const studyGroupData = ref({});
  const myPosition = ref('');
  const studyGroupMemberList = ref([]);
  const studyGroupBoardList = ref([]);

  const { formatDate } = useFormat();


  const getStudyGroupInfo = async () => {
    try {
      const response = await apiClient.get('/main/study_group');

      console.log(response.data.result);
      studyGroupData.value = response.data.result;

    } catch (error) {
      if (error.response.data.resultCode === 'USER_STUDY_GROUP_NOT_FOUND') {
        studyGroupName.value = '';
        studyGroupDescription = '';
      } else {
        console.log('스터디 정보를 가져오지 못했습니다.', error);
        alert('스터디 정보를 가져오는데 오류가 생겼습니다.');
      }
    }
  }

  const getStudyGroupMemberList = async () => {
    try{
      const response = await apiClient.get('/main/study_group/member');
      
      const memberList = response.data.result;

      studyGroupMemberList.value = memberList;

      for(let member of memberList) {
        if (member.userId === userId) {
          myPosition.value = member.position;
          break;
        }
      }

      console.log(myPosition.value);

    } catch (error) {
      console.log('스터디 그룹원을 가져오지 못했습니다.', error);
      alert('스터디 그룹원 정보를 가져오는데 오류가 생겼습니다.');
    }
  }

  onMounted(() => {
    getStudyGroupInfo();
    getStudyGroupMemberList();
  });
</script>
<style scoped>
  .active a {
    color: white;
  }
</style>