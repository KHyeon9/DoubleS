<template>
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ps ps--active-y">
    <div class="container-fluid py-4">
      <div class="d-sm-flex justify-content-between">
        <div>
          <a href="javascript:;" class="btn btn-icon bg-gradient-primary">
            New order
          </a>
        </div>
      </div>
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">공지사항</h5>
              <p class="text-sm mb-0">
                새로 올라온 공지사항을 확인해보세요.
              </p>
            </div>
            <div class="p-3">
              테이블 들어갈 자리
              <!-- 페이징 -->
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

</script>
<style scoped>
  .active a {
    color: white;
  }
</style>