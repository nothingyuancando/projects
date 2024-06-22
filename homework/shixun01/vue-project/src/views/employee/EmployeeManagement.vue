<template>
  <div class="employee-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>员工管理</h2>

        <div style="display: flex; justify-content: flex-end;">
          <el-button plain @click="dialogFormVisible = true">
            新增员工
          </el-button>
        </div>

        <el-dialog v-model="dialogFormVisible" title="新增员工" width="500">
          <el-form :model="form">
            <el-form-item label="姓名：" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="密码：" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>

            <el-form-item label="职位：" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>


            <el-form-item label="电话：" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>

          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取消</el-button>
              <el-button type="primary" @click="addEmployee">
                确认
              </el-button>
            </div>
          </template>
        </el-dialog>
      </div>

      <hr/>
      <div class="table-section">
        <el-table :data="employees" style="width: 100%">
          <el-table-column prop="id" label="员工编号"></el-table-column>
          <el-table-column prop="name" label="员工姓名"></el-table-column>
          <el-table-column prop="position" label="权限"></el-table-column>
          <el-table-column prop="phone" label="电话"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="dialogFormVisible = true" size="mini">编辑</el-button>
              <el-button @click="handleDelete(scope.row)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>

        </el-table>
      </div>
    </el-card>
</div>
</template>

<script setup>
import { ref } from 'vue'
import { reactive} from 'vue'
import {ElMessage} from "element-plus";

let dialogFormVisible = ref(false)
const formLabelWidth = '140px'

const form = reactive({
  name: '',
})



import {employeeListService} from '@/api/employee.js'
const employeeList = async ()=>{
let result = await employeeListService();
employees.value = result.data;

}
employeeList();

import {employeeAddService} from "@/api/employee.js";
const addEmployee=async () => {

let result = await employeeAddService(form.valueOf());

ElMessage.success(result.msg?result.msg:"添加成功")
  employeeList();
  dialogFormVisible = false;
}



</script>

<style scoped lang="scss">
.employee-management {
  padding: 20px;
  background: #f5f5f5;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;



}

.el-card {
  width: 100%;
  height: 100%;
}

.form-section {
  margin-bottom: 20px;
}

.table-section {
  width: 100%;
}

.el-input,
.el-select,
.el-date-picker {
  width: 300px;
}

.el-table {
  width: 100%;
}

.employee-management-container {
  padding: 20px;
}
</style>
