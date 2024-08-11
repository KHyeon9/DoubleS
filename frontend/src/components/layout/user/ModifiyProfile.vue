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
                  <label>First Name</label>
                  <input type="text" class="form-control" placeholder="Alec" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label>Last Name</label>
                  <input type="text" class="form-control" placeholder="Thompson" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label>Email</label>
                  <input type="email" class="form-control" placeholder="example@email.com" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label>Confirm Email</label>
                  <input type="email" class="form-control" placeholder="example@email.com" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label>Your location</label>
                  <input type="text" class="form-control" placeholder="Sydney, A" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
            </div>
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