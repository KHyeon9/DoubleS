<template>
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ps ps--active-y">
    <div class="container-fluid py-4">
      <div class="d-sm-flex justify-content-between">
        <div>
          <a href="javascript:;" class="btn btn-icon bg-gradient-primary">
            New Notice
          </a>
        </div>
      </div>
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header pb-0">
              <h5 class="mb-3">공지사항</h5>
              <p class="text-sm mb-0">
                새로 올라온 공지사항을 확인해보세요.
              </p>
            </div>
            <div class="p-1">
              <div class="card-body">
                <div class="col-12">
                  <table class="table align-items-center justify-content-center mt-0 mb-0">
                    <thead>
                      <tr>
                        <th class="col-1 text-uppercase text-secondary text-xs font-weight-bolder opacity-12">번호</th>
                        <th class="col-6 text-uppercase text-secondary text-xs font-weight-bolder opacity-12">제목</th>
                        <th class="col-3 text-uppercase text-secondary text-xs font-weight-bolder opacity-7 ps-2">작성자</th>
                        <th class="col-2 text-uppercase text-secondary text-xs font-weight-bolder opacity-7 ps-2">작성일</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">4</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">Title1</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <p class="text-sm font-weight-bold mb-0">Adm</p>
                        </td>
                        <td>
                          <span class="text-xs font-weight-bold">2024-07-30</span>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">3</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">Title1</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <p class="text-sm font-weight-bold mb-0">Adm</p>
                        </td>
                        <td>
                          <span class="text-xs font-weight-bold">2024-07-30</span>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">2</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">Title1</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <p class="text-sm font-weight-bold mb-0">Adm</p>
                        </td>
                        <td>
                          <span class="text-xs font-weight-bold">2024-07-30</span>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">1</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <div class="d-flex px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">Title1</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <p class="text-sm font-weight-bold mb-0">Adm</p>
                        </td>
                        <td>
                          <span class="text-xs font-weight-bold">2024-07-30</span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <!-- 페이징 -->
              <nav class="mt-4 nav nav-bar d-flex justify-content-end p-3" aria-label="Page navigation">
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

</script>
<style scoped>
  .active a {
    color: white;
  }
</style>