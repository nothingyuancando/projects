<template>
  <div class="employee-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>员工管理</h2>
        <div style="display: flex; justify-content: space-between;">
          <el-input v-model="searchQuery" placeholder="搜索员工" style="width: 300px;"></el-input>
          <el-button plain @click="openAddDialog">新增员工</el-button>
        </div>
      </div>

      <hr/>

      <div class="table-section">
        <el-table :data="filteredEmployees" style="width: 100%">
          <el-table-column prop="id" label="员工编号"></el-table-column>
          <el-table-column prop="name" label="员工姓名"></el-table-column>
          <el-table-column prop="position" label="职位"></el-table-column>
          <el-table-column prop="phone" label="电话"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="openEditDialog(scope.row)" size="mini">编辑</el-button>
              <el-button @click="openDeleteDialog(scope.row)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>


      </div>

    </el-card>

    <el-dialog v-model="dialogFormVisible" :title="isEdit ? '编辑员工' : '新增员工'" width="500">
      <el-form :model="form" :rules="rules" ref="employeeForm" label-width="100px">
        <el-form-item label="姓名：" prop="name">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input v-model="form.password" autocomplete="off" type="password" />
        </el-form-item>
        <el-form-item label="职位：" prop="position">
          <el-input v-model="form.position" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电话：" prop="phone">
          <el-input v-model="form.phone" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="isEdit ? editEmployee() : addEmployee()">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="dialogDeleteVisible" title="删除确认" width="500">
      <span>确定要删除员工 {{ selectedEmployee.name }} 吗？</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteEmployee">删除</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { ElMessage } from 'element-plus';

const dialogFormVisible = ref(false);
const dialogDeleteVisible = ref(false);
const isEdit = ref(false);
const formLabelWidth = '100px';

const form = reactive({
  id: null,
  name: '',
  password: '',
  position: '',
  phone: ''
});

const employees = ref([
  { id: 1, name: '张三', position: '经理', phone: '12345678901' },
  { id: 2, name: '李四', position: '工程师', phone: '12345678902' },
  { id: 3, name: '王五', position: '技术员', phone: '12345678903' }
]);

const searchQuery = ref('');

const filteredEmployees = computed(() => {
  return employees.value.filter(employee =>
      employee.name.includes(searchQuery.value) ||
      employee.position.includes(searchQuery.value) ||
      employee.phone.includes(searchQuery.value)
  );
});

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入电话', trigger: 'blur' }]
};

const selectedEmployee = ref(null);

const openAddDialog = () => {
  resetForm();
  isEdit.value = false;
  dialogFormVisible.value = true;
};

const openEditDialog = (employee) => {
  Object.assign(form, employee);
  isEdit.value = true;
  dialogFormVisible.value = true;
};

const openDeleteDialog = (employee) => {
  selectedEmployee.value = employee;
  dialogDeleteVisible.value = true;
};

const resetForm = () => {
  form.id = null;
  form.name = '';
  form.password = '';
  form.position = '';
  form.phone = '';
};

const addEmployee = async () => {
  const valid = await validateForm();
  if (valid) {
    const newEmployee = { ...form, id: Date.now() };
    employees.value.push(newEmployee);
    ElMessage.success('员工添加成功');
    dialogFormVisible.value = false;
  }
};

const editEmployee = async () => {
  const valid = await validateForm();
  if (valid) {
    const index = employees.value.findIndex(emp => emp.id === form.id);
    if (index !== -1) {
      employees.value[index] = { ...form };
      ElMessage.success('员工信息更新成功');
      dialogFormVisible.value = false;
    }
  }
};

const deleteEmployee = () => {
  employees.value = employees.value.filter(emp => emp.id !== selectedEmployee.value.id);
  ElMessage.success('员工删除成功');
  dialogDeleteVisible.value = false;
};

const validateForm = () => {
  return new Promise((resolve) => {
    const formRef = this.$refs.employeeForm;
    if (formRef) {
      formRef.validate((valid) => {
        resolve(valid);
      });
    } else {
      resolve(false);
    }
  });
};
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

.table-section {
  width: 100%;
}

.el-input {
  width: 300px;
}

.el-table {
  width: 100%;
}



</style>
