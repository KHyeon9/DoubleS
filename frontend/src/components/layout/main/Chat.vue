<template>
  <div class="page-header min-height-300 border-radius-xl mt-4" style="background-image: url('https://images.unsplash.com/photo-1531512073830-ba890ca4eba2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&amp;ixlib=rb-1.2.1&amp;auto=format&amp;fit=crop&amp;w=1920&amp;q=80');">
    <span class="mask  bg-gradient-primary  opacity-6"></span>
  </div>
  <div class="card card-body mx-3 mx-md-4 mt-n6">
    <div class="row gx-4 mb-2">
      <div class="col-auto my-auto">
        <div class="h-100">
          <h5 class="mb-1">
            Richard Davis
          </h5>
          <p class="mb-0 font-weight-normal text-sm">
            CEO / Co-Founder
          </p>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="row mt-4">
        <div class="col-md-5 col-lg-4">
          <div class="card blur shadow-blur max-height-vh-70 overflow-auto overflow-x-hidden mb-5 mb-lg-0">
            <div class="card-header p-3">
              <h6>Friends</h6>
              <div class="input-group input-group-outline">
                <label class="form-label">Search contact</label>
                <input type="text" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
              </div>
            </div>
            <div class="card-body p-2">
              <a href="javascript:;" class="d-block p-2 border-radius-lg bg-gradient-primary">
                <div class="d-flex p-2">
                  <div class="ms-3">
                    <div class="justify-content-between align-items-center">
                      <h6 class="text-white mb-0">Charlie Watson
                        <span class="badge badge-success"></span>
                      </h6>
                      <p class="text-white mb-0 text-sm">Typing...</p>
                    </div>
                  </div>
                </div>
              </a>
              <a href="javascript:;" class="d-block p-2">
                <div class="d-flex p-2">
                  <div class="ms-3">
                    <h6 class="mb-0">Jane Doe</h6>
                    <p class="text-muted text-xs mb-2">1 hour ago</p>
                    <span class="text-muted text-sm col-11 p-0 text-truncate d-block">Computer users and programmers</span>
                  </div>
                </div>
              </a>
              <a href="javascript:;" class="d-block p-2">
                <div class="d-flex p-2">
                  <div class="ms-3">
                    <h6 class="mb-0">Mila Skylar</h6>
                    <p class="text-muted text-xs mb-2">24 min ago</p>
                    <span class="text-muted text-sm col-11 p-0 text-truncate d-block">You can subscribe to receive weekly...</span>
                  </div>
                </div>
              </a>
              <a href="javascript:;" class="d-block p-2">
                <div class="d-flex p-2">
                  <div class="ms-3">
                    <h6 class="mb-0">Sofia Scarlett</h6>
                    <p class="text-muted text-xs mb-2">7 hours ago</p>
                    <span class="text-muted text-sm col-11 p-0 text-truncate d-block">It’s an effective resource regardless..</span>
                  </div>
                </div>
              </a>
              <a href="javascript:;" class="d-block p-2">
                <div class="d-flex p-2">
                  <div class="ms-3">
                    <div class="justify-content-between align-items-center">
                      <h6 class="mb-0">Tom Klein</h6>
                      <p class="text-muted text-xs mb-2">1 day ago</p>
                    </div>
                    <span class="text-muted text-sm col-11 p-0 text-truncate d-block">Be sure to check it out if your dev pro...</span>
                  </div>
                </div>
              </a>
            </div>
          </div>
        </div>
        <div class="col-md-7 col-lg-8">
          <div class="card blur shadow-blur max-height-vh-70">
            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
              <div class="bg-gradient-primary shadow-primary border-radius-lg p-3">
                <div class="row">
                  <div class="col-md-9 col-lg-10">
                    <div class="d-flex align-items-center">
                      <div class="ms-3">
                        <h6 class="mb-0 d-block text-white">Charlie Watson</h6>
                        <span class="text-sm text-white opacity-8">last seen today at 1:53am</span>
                      </div>
                    </div>
                  </div>
                  <div class="col-1 my-auto">
                    <button class="btn btn-icon-only text-white mb-0 me-3 me-sm-0" type="button" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Video call">
                      <i class="material-icons">videocam</i>
                    </button>
                  </div>
                  <div class="col-1 my-auto">
                    <div class="dropdown">
                      <button class="btn btn-icon-only text-white mb-0" type="button" data-bs-toggle="dropdown">
                        <i class="material-icons">settings</i>
                      </button>
                      <ul class="dropdown-menu dropdown-menu-end me-sm-n2 p-2" aria-labelledby="chatmsg">
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Profile
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Mute conversation
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Block
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Clear chat
                          </a>
                        </li>
                        <li>
                          <hr class="dropdown-divider">
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md text-danger" href="javascript:;">
                            Delete chat
                          </a>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card-body overflow-auto overflow-x-hidden">
              <div class="row justify-content-start mb-4">
                <div class="col-auto">
                  <div class="card ">
                    <div class="card-body py-2 px-3">
                      <p class="mb-1">
                        It contains a lot of good lessons about effective practices
                      </p>
                      <div class="d-flex align-items-center text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>3:14am</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row justify-content-end text-right mb-4">
                <div class="col-auto">
                  <div class="card bg-gradient-primary">
                    <div class="card-body py-2 px-3 text-white">
                      <p class="mb-1">
                        Can it generate daily design links that include essays and data visualizations ?<br>
                      </p>
                      <div class="d-flex align-items-center justify-content-end text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>4:42pm</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row mt-4">
                <div class="col-md-12 text-center">
                  <span class="badge text-dark">Wed, 3:27pm</span>
                </div>
              </div>
              <div class="row justify-content-start mb-4">
                <div class="col-auto">
                  <div class="card ">
                    <div class="card-body py-2 px-3">
                      <p class="mb-1">
                       Yeah! Responsive Design is geared towards those trying to build web apps
                      </p>
                      <div class="d-flex align-items-center text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>4:31pm</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row justify-content-end text-right mb-4">
                <div class="col-auto">
                  <div class="card bg-gradient-primary">
                    <div class="card-body py-2 px-3 text-white">
                      <p class="mb-1">
                        xcellent, I want it now !
                      </p>
                      <div class="d-flex align-items-center justify-content-end text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>4:42pm</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row justify-content-start mb-4">
                <div class="col-auto">
                  <div class="card ">
                    <div class="card-body py-2 px-3">
                      <p class="mb-1">
                        You can easily get it; The content here is all free
                      </p>
                      <div class="d-flex align-items-center text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>4:42pm</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row justify-content-end text-right mb-4">
                <div class="col-auto">
                  <div class="card bg-gradient-primary">
                    <div class="card-body py-2 px-3 text-white">
                      <p class="mb-1">
                        Awesome, blog is important source material for anyone who creates apps? <br>
                        Beacuse these blogs offer a lot of information about website development.
                      </p>
                      <div class="d-flex align-items-center justify-content-end text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>4:42pm</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row justify-content-start mb-4">
                <div class="col-5">
                  <div class="card ">
                    <div class="card-body p-2">
                      <div class="col-12 p-0">
                        test
                      </div>
                      <div class="d-flex align-items-center text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>4:47pm</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row justify-content-end text-right mb-4">
                <div class="col-auto">
                  <div class="card bg-gradient-primary">
                    <div class="card-body py-2 px-3 text-white">
                      <p class="mb-0">
                        At the end of the day … the native dev apps is where users are
                      </p>
                      <div class="d-flex align-items-center justify-content-end text-sm opacity-6">
                        <i class="ni ni-check-bold text-sm me-1"></i>
                        <small>4:42pm</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row justify-content-start">
                <div class="col-auto">
                  <div class="card ">
                    <div class="card-body py-2 px-3">
                      <p class="mb-0">
                        Charlie is Typing...
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card-footer d-block">
              <form class="align-items-center">
                <div class="input-group input-group-outline d-flex">
                  <label class="form-label">Type your message</label>
                  <input type="text" class="form-control form-control-lg" onfocus="focused(this)" onfocusout="defocused(this)">
                  <button class="btn bg-gradient-primary mb-0">
                    <i class="material-icons">send</i>
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>

</script>
<style scoped>
  
</style>