<template>

<div class="login-box">
    <el-form
    ref="ruleFormRef"
    style="max-width: 600px"
    :model="ruleForm"
    status-icon
    :rules="rules"
    label-width="auto"
    class="login-form"
  >
    <el-form-item label="账号：" prop="userName">
      <el-input v-model="ruleForm.userName"  autocomplete="off" />
    </el-form-item>
    <el-form-item label="密码：" prop="Password">
      <el-input
        v-model="ruleForm.Password"
        type="password"
        autocomplete="off"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm(ruleFormRef)">
        登录
      </el-button>
      <el-button @click="resetForm(ruleFormRef)">注册</el-button>
    </el-form-item>
  </el-form> 
</div>

</template>
<script lang="ts">
import { defineComponent, reactive ,toRefs} from 'vue';

export default defineComponent({
    
    setup(){
       
        const data = reactive({
            ruleForm:{
userName:'',
Password:'',
            },

            rules:{
                userName:[
                    {required:true,message:'请输入有效账号',trigger:'blur'},
{min:3,max:5,message:'长度不对',trigger:'blur'},
                ],

Password:[
    {required:true,message:'请输入密码',trigger:'blur'},
    {min:3,max:5,message:'长度不对',trigger:'blur'},

],

            }
        })
        return {
            ...toRefs(data)
        }
    }
})

</script>

<style lang="scss" scoped>
.login-box{
    width:100%;
    height:100%;
    background:url("../assets/loginimg.jpg");
    
    .login-form{

        
    }
    }
</style>

