<template>
  <div class="container-fluid py-4">
    <div class="row d-flex justify-content-center align-items-center">
      <div class="col-md-12">
        <div class="card">
          <div class="card-header p-3">
            <h4 class="mb-0">Todo List</h4>
            <p class="text-sm mb-0">
              일정들을 추가해보세요.
            </p>
          </div>
          <div class="card-body">
            <form class="d-flex justify-content-center align-items-center mb-4">
              <div :class="['input-group input-group-outline my-3', { 'is-focused': isTaskInputFocused || taskInput }]">
                <label class="form-label">새로운 할 일을 입력해주세요.</label>
                <input 
                  id="taskInput" 
                  v-model="taskInput" 
                  type="text" 
                  class="form-control"
                  @focus="handleTaskInputFocus"
                  @blur="handleTaskInputBlur"
                >
              </div>
              <div class="ms-3 mt-3 col-4">
                <button href="javascript:;" class="btn bg-gradient-dark dropdown-toggle" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
                  중요도
                </button>
                <ul class="dropdown-menu dropdown-menu-lg-start px-2 py-3" aria-labelledby="navbarDropdownMenuLink2" style="">
                  <li>
                    <a class="dropdown-item border-radius-md">
                      낮음
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item border-radius-md">
                      중간
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item border-radius-md">
                      높음
                    </a>
                  </li>
                </ul>
                <button type="submit" class="btn bg-gradient-dark ms-2">일정 추가</button>
              </div>
            </form>
            <div class="mt-3 mb-6">
              <h6>Todo 진행률</h6>
              <div class="mt-3">
                <div class="progress rounded-pill">
                  <div class="progress-bar rounded-pill bg-gradient-info" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;"> 60%</div>
                </div>
              </div>
            </div>
            <table class="table mb-4">
              <thead>
                <tr>
                  <th scope="col">내용</th>
                  <th scope="col">중요도</th>
                  <th scope="col">날짜</th>
                  <th scope="col"></th>
                </tr>
              </thead>
              <tbody class="text-md">
                <tr class="fw-normal">
                  <td class="align-middle">
                    <span>Call Sam For payments</span>
                  </td>
                  <td class="align-middle">
                    <h6 class="mb-0"><span class="badge bg-danger">높음</span></h6>
                  </td>
                  <td class="align-middle">
                    <span>2024/08/16</span>
                  </td>
                  <td class="align-middle">
                    <a href="#!"><i class="material-icons text-success me-4">check</i></a>
                    <a href="#!"><i class="material-icons text-danger">delete</i></a>
                  </td>
                </tr>
                <tr class="fw-normal">
                  <td class="align-middle">Make payment to Bluedart</td>
                  <td class="align-middle">
                    <h6 class="mb-0"><span class="badge bg-success">낮음</span></h6>
                  </td>
                  <td class="align-middle">
                    <span>2024/08/16</span>
                  </td>
                  <td class="align-middle">
                    <a href="#!"><i class="material-icons text-success me-4">check</i></a>
                    <a href="#!"><i class="material-icons text-danger">delete</i></a>
                  </td>
                </tr>
                <tr class="fw-normal">
                  <td class="align-middle">Office rent</td>
                  <td class="align-middle">
                    <h6 class="mb-0"><span class="badge bg-warning">중간</span></h6>
                  </td>
                  <td class="align-middle">
                    <span>2024/08/16</span>
                  </td>
                  <td class="align-middle">
                    <a href="#!"><i class="material-icons text-success me-4">check</i></a>
                    <a href="#!"><i class="material-icons text-danger">delete</i></a>
                  </td>
                </tr>
                <tr class="fw-normal">
                  <td class="align-middle">Office grocery shopping</td>
                  <td class="align-middle">
                    <h6 class="mb-0"><span class="badge bg-danger">높음</span></h6>
                  </td>
                  <td class="align-middle">
                    <span>2024/08/16</span>
                  </td>
                  <td class="align-middle">
                    <a href="#!"><i class="material-icons text-success me-4">check</i></a>
                    <a href="#!"><i class="material-icons text-danger">delete</i></a>
                  </td>
                </tr>
                <tr class="fw-normal">
                  <td class="align-middle">Ask for Lunch to Clients</td>
                  <td class="align-middle">
                    <h6 class="mb-0"><span class="badge bg-success">낮음</span></h6>
                  </td>
                  <td class="align-middle">
                    <span>2024/08/16</span>
                  </td>
                  <td class="align-middle">
                    <a href="#!"><i class="material-icons text-success me-4">check</i></a>
                    <a href="#!"><i class="material-icons text-danger">delete</i></a>
                  </td>
                </tr>
                <tr class="fw-normal">
                  <td class="border-0 align-middle">Create weekly tasks plan</td>
                  <td class="border-0 align-middle">
                    <h6 class="mb-0"><span class="badge bg-warning">중간</span></h6>
                  </td>
                  <td class="align-middle">
                    <span>2024/08/16</span>
                  </td>
                  <td class="align-middle">
                    <a href="#!"><i class="material-icons text-success me-4">check</i></a>
                    <a href="#!"><i class="material-icons text-danger">delete</i></a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="card-footer text-end p-3">
            <button class="btn bg-gradient-dark">홈으로</button>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue';

const taskInput =  ref('');
const isTaskInputFocused = ref(false);

const handleTaskInputFocus = () => {
  isTaskInputFocused.value = true;
};

const handleTaskInputBlur = () => {
  isTaskInputFocused.value = false;
};

</script>
<style scoped>
  .todoTitle {
    font-size: 1em;
  }

  .progress {
    height: 20px;
  }

  .progress-bar {
    height: 20px;
  }
</style>