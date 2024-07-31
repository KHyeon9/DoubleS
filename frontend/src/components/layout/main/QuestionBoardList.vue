<template>
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ps ps--active-y">
    <div class="container-fluid py-4">
      <div class="d-sm-flex justify-content-between">
        <div>
          <a href="javascript:;" class="btn btn-icon bg-gradient-primary">
            New Post
          </a>
        </div>
        <div class="d-flex">
          <div class="dropdown d-inline">
            <a href="javascript:;" class="btn btn-outline-dark dropdown-toggle" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
            Filters
            </a>
            <ul class="dropdown-menu dropdown-menu-lg-start px-2 py-3" aria-labelledby="navbarDropdownMenuLink2" style="">
              <li><a class="dropdown-item border-radius-md" href="javascript:;">Status: Paid</a></li>
              <li><a class="dropdown-item border-radius-md" href="javascript:;">Status: Refunded</a></li>
              <li><a class="dropdown-item border-radius-md" href="javascript:;">Status: Canceled</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">질문 게시판</h5>
              <p class="text-sm mb-0">
                궁금한 것을 공유해보세요.
              </p>
            </div>
            <div class="p-3">
              <ul class="list-group">
                <li class="list-group-item border-0 d-flex p-4 mb-2 bg-gray-100 border-radius-lg">
                  <router-link class="col-12" to="/main/question_board/detail">
                    <div class="d-flex flex-column">
                        <div class="fw-bolder mb-3 fs-5">Title</div>
                        <span class="mb-2 text-s">content content content content content content content content</span>
                        <div class="text-dark mb-0 mt-1"><i class="material-icons text-sm me-2">sell</i>Tag</div>
                        <div class="ms-auto board_info">
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">thumb_up</i><span class="font-weight-bold">13</span></div>
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">mode_comment</i><span class="font-weight-bold">3</span></div>
                        </div>
                    </div>
                  </router-link>
                </li>
                <li class="list-group-item border-0 d-flex p-4 mb-2 mt-3 bg-gray-100 border-radius-lg">
                  <router-link class="col-12" to="/main/question_board/detail">
                    <div class="d-flex flex-column">
                        <div class="fw-bolder mb-3 fs-5">Title2</div>
                        <span class="mb-2 text-s">content content content content content content content content</span>
                        <div class="text-dark mb-0 mt-1"><i class="material-icons text-sm me-2">sell</i>Tag</div>
                        <div class="ms-auto board_info">
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">thumb_up</i><span class="font-weight-bold">0</span></div>
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">mode_comment</i><span class="font-weight-bold">3</span></div>
                        </div>
                    </div>
                  </router-link>
                </li>
                <li class="list-group-item border-0 d-flex p-4 mb-2 mt-3 bg-gray-100 border-radius-lg">
                  <router-link class="col-12" to="/main/question_board/detail">
                    <div class="d-flex flex-column">
                        <div class="fw-bolder mb-3 fs-5">Title3</div>
                        <span class="mb-2 text-s">content content content content content content content content</span>
                        <div class="text-dark mb-0 mt-1"><i class="material-icons text-sm me-2">sell</i>Tag</div>
                        <div class="ms-auto board_info">
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">thumb_up</i><span class="font-weight-bold">9</span></div>
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">mode_comment</i><span class="font-weight-bold">2</span></div>
                        </div>
                    </div>
                  </router-link>
                </li>
              </ul>
              <nav class="mt-4 nav nav-bar d-flex justify-content-end" aria-label="Page navigation">
                <ul class="pagination">
                  <li class="page-item"><a class="page-link" href="#">‹</a></li>
                  <li class="page-item active"><a class="page-link " href="#" >1</a></li>
                  <li class="page-item"><a class="page-link" href="#">2</a></li>
                  <li class="page-item"><a class="page-link" href="#">3</a></li>
                  <li class="page-item"><a class="page-link" href="#">›</a></li>
                </ul>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
<script setup>
  import { useIndexStore } from '../../../store/IndexStore';

  const store = useIndexStore();
</script>
<style scoped>
  .board_info {
    display: flex;
  }

  .active a {
    color: white;
  }

</style>