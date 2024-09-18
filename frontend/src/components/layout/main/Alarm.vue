<template>
  <div class="row">
        <div class="col-lg-10 col-md-10 mx-auto">
          <div class="card mt-4">
            <div class="card-header p-3">
              <h5 class="mb-0">Alarm List</h5>
            </div>
            <div class="alarm-body card-body p-3 pb-0">
              <div class="alert alert-primary alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple primary alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-secondary alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple secondary alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-primary alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple primary alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-success alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple success alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-danger alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple danger alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-warning alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple warning alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-info alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple info alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-warning alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple warning alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-light alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple light alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-dark alert-dismissible text-white" role="alert">
                <span class="text-sm">A simple dark alert with <a href="javascript:;" class="alert-link text-white">an example link</a>. Give it a click if you like.</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              
            </div>
            <!-- 페이징 -->
            <nav class="mt-4 nav nav-bar d-flex justify-content-end" aria-label="Page navigation">
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
</template>
<script setup>

</script>
<style scoped>
  .active a {
    color: white;
  }

  .alarm-body {
    height: 755px;
  }
</style>