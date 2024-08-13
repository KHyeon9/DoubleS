<template>
  <div class="container-fluid px-2 px-md-4">
    <div class="page-header min-height-300 border-radius-xl mt-4" style="background-image: url('https://images.unsplash.com/photo-1531512073830-ba890ca4eba2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80');">
      <span class="mask  bg-gradient-primary  opacity-6"></span>
    </div>
    <div class="card card-body mx-3 mx-md-4 mt-n6">
      <div class="row gx-4 mb-2">

        <div class="col-auto my-auto">
          <div class="h-100">
            <h4 class="mb-1">
              nickname
            </h4>
            <p class="mb-0 font-weight-normal text-md">
              userId
            </p>
          </div>
        </div>
        <div class="col-lg-4 col-md-6 my-sm-auto ms-sm-auto me-sm-0 mx-auto mt-3">
          <div class="nav-wrapper position-relative end-0">
            <ul class="nav nav-pills nav-fill p-1" role="tablist">
              <li class="nav-item">
                <a class="nav-link mb-0 px-0 py-1 " role="tab">
                  <i class="material-icons text-lg position-relative">email</i>
                  <span class="ms-1">Messages</span>
                </a>
              </li>
              <li class="nav-item">
                <router-link 
                  class="nav-link mb-0 px-0 py-1 " role="tab"
                  :to="`/main/profile/modify/${userId}`"
                >
                  <i class="material-icons text-lg position-relative">settings</i>
                  <span class="ms-1">Modify</span>
                </router-link>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="row">
          <div class="col-12">
            <div class="card card-plain h-100">
              <div class="card-header pb-0 p-3">
                <div class="row">
                  <div class="col-md-8 d-flex align-items-center">
                    <h6 class="mb-0">유저 정보</h6>
                  </div>
                  <div class="col-md-4 text-end">
                    <a href="javascript:;">
                      <i class="fas fa-user-edit text-secondary text-sm" data-bs-toggle="tooltip" data-bs-placement="top" title="Edit Profile"></i>
                    </a>
                  </div>
                </div>
              </div>
              <div class="card-body p-3">
                <p class="text-md">
                  user memo 띄우고 줄바꿈 가능하게
                </p>
                <hr class="horizontal gray-light my-4">
                <ul class="list-group">
                  <li class="list-group-item border-0 ps-0 text-md"><strong class="text-dark">Email:</strong> &nbsp; alecthompson@mail.com</li>
                  <li class="list-group-item border-0 ps-0 text-md"><strong class="text-dark">스터디 그룹 여부:</strong> &nbsp; 참여중</li>
                </ul>
              </div>
              <div class="col-12 mt-4">
              <div class="mb-3 ps-3">
                <h6 class="mb-1">최근 게시물</h6>
              </div>
              <div class="row">
                <div class="col-xl-3 col-md-6 mb-xl-0 mb-4">
                  <div class="card card-blog card-plain">
                    <div class="card-body p-3">
                      <a href="javascript:;">
                        <h6>
                          title 글자 수 제한 둬야 할거 같음
                        </h6>
                      </a>
                      <p class="mb-4 text-sm">
                        content 글자 수 제한 둬야 할거 같음
                      </p>
                      <div class="d-flex align-items-center justify-content-between">
                        <button type="button" class="btn btn-outline-primary btn-sm mb-0">View</button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-xl-0 mb-4">
                  <div class="card card-blog card-plain">
                    <div class="card-body p-3">
                      <a href="javascript:;">
                        <h6>
                          title 글자 수 제한 둬야 할거 같음
                        </h6>
                      </a>
                      <p class="mb-4 text-sm">
                        content 글자 수 제한 둬야 할거 같음
                      </p>
                      <div class="d-flex align-items-center justify-content-between">
                        <button type="button" class="btn btn-outline-primary btn-sm mb-0">View</button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-xl-0 mb-4">
                  <div class="card card-blog card-plain">
                    <div class="card-body p-3">
                      <a href="javascript:;">
                        <h6>
                          title 글자 수 제한 둬야 할거 같음
                        </h6>
                      </a>
                      <p class="mb-4 text-sm">
                        content 글자 수 제한 둬야 할거 같음
                      </p>
                      <div class="d-flex align-items-center justify-content-between">
                        <button type="button" class="btn btn-outline-primary btn-sm mb-0">View</button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-xl-0 mb-4">
                  <div class="card card-blog card-plain">
                    <div class="card-body p-3">
                      <a href="javascript:;">
                        <h6>
                          title 글자 수 제한 둬야 할거 같음
                        </h6>
                      </a>
                      <p class="mb-4 text-sm">
                        content 글자 수 제한 둬야 할거 같음
                      </p>
                      <div class="d-flex align-items-center justify-content-between">
                        <button type="button" class="btn btn-outline-primary btn-sm mb-0">View</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { useAuthStore } from '../../../store/authStore';
import { computed } from 'vue';

const authStore = useAuthStore();
const userId = computed(() => authStore.userId);

</script>
<style scoped>
  
</style>