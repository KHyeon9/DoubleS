<template>
  <div class="container-fluid my-3 py-3">
    <div class="row mb-5">
      <div class="col-lg-12 mt-lg-0 mt-4">
        <div class="card card-body" id="profile">
          <div class="row align-items-center">
            <div class="col-sm-auto col-8 my-auto">
              <div class="h-100">
                <h5 class="mb-1 font-weight-bolder">
                  Richard Davis
                </h5>
                <p class="mb-0 font-weight-normal text-sm">
                  CEO / Co-Founder
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="card mt-4" id="basic-info">
          <div class="card-header">
            <h5>Basic Info</h5>
          </div>
          <div class="card-body pt-0">
            <div class="row">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label class="font-weight-bolder">닉네임</label>
                  <input type="text" class="form-control" placeholder="닉네임" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label class="font-weight-bolder">Email</label>
                  <input type="email" class="form-control" placeholder="example@email.com" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label class="font-weight-bolder">자기소개</label>
                  <textarea type="text" class="form-control" 
                            placeholder="자기소개를 입력해주세요." 
                            rows="2"
                            onfocus="focused(this)" onfocusout="defocused(this)"
                  >
                  </textarea>
                </div>
              </div>
            </div>
            <button class="btn bg-gradient-dark btn-sm float-end mt-6 mb-0">Update Info</button>
          </div>
        </div>
        <div class="card mt-4" id="password">
          <div class="card-header">
            <h5>Change Password</h5>
          </div>
          <div class="card-body pt-0">
            <div class="input-group input-group-outline">
              <label class="form-label">Current password</label>
              <input type="password" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
            </div>
            <div class="input-group input-group-outline my-4">
              <label class="form-label">New password</label>
              <input type="password" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
            </div>
            <div class="input-group input-group-outline">
              <label class="form-label">Confirm New password</label>
              <input type="password" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
            </div>
            <button class="btn bg-gradient-dark btn-sm float-end mt-6 mb-0">Update password</button>
          </div>
        </div>
        <div class="card mt-4" id="delete">
          <div class="card-body">
            <div class="d-flex align-items-center mb-sm-0 mb-4">
              <div class="w-50">
                <h5>Delete Account</h5>
                <p class="text-sm mb-0">Once you delete your account, there is no going back. Please be certain.</p>
              </div>
              <div class="w-50 text-end">
                <button class="btn btn-outline-secondary mb-3 mb-md-0 ms-auto" type="button" name="button">Deactivate</button>
                <button class="btn bg-gradient-danger mb-0 ms-2" type="button" name="button">Delete Account</button>
              </div>
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