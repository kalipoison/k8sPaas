<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品</el-breadcrumb-item>
      <el-breadcrumb-item>商品列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图 -->
    <el-card>
      <!-- 搜索 添加 -->
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input placeholder="请输入商品名" v-model="queryInfo.query" clearable @clear="getProductlist">
            <el-button slot="append" icon="el-icon-search" @click="getProductlist"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true">添加 商品</el-button>
        </el-col>
      </el-row>
      <el-divider></el-divider>
      <!-- 商品 列表区域 -->
      <el-table :data="productlist" border stripe>
        <!-- stripe: 斑马条纹 border：边框-->
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="prodName" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="现价"></el-table-column>
        <el-table-column prop="brief" label="描述"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
        <el-table-column prop="createTime" label="录入时间"></el-table-column>
        <el-table-column prop="updateTime" label="修改时间"></el-table-column>
        <el-table-column prop="putawayTime" label="上架时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-tooltip
              class="item"
              effect="dark"
              content="编辑 商品 信息"
              :enterable="false"
              placement="top"
            >
              <el-button
                type="warning"
                icon="el-icon-edit"
                size="mini"
                content="编辑 商品 信息"
                circle
                @click="showEditDialog(scope.row)"
              ></el-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="dark"
              content="删除 商品"
              :enterable="false"
              placement="top"
            >
              <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                content="删除 商品"
                circle
                @click="removeProduct(scope.row)"
              ></el-button>
            </el-tooltip>

          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[1, 5, 10, 15]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totle"
      ></el-pagination>
    </el-card>

    <!-- 添加 Product 的对话框 -->
    <el-dialog title="新增 Product" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
      <!-- 内容主体 -->
      <el-form
        :model="addProductForm"
        ref="addProductFormRef"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="prodName">
          <el-input v-model="addProductForm.prodName" @input="change($event)"></el-input>
        </el-form-item>
        <el-form-item label="现价" prop="price">
          <el-input v-model="addProductForm.price" @input="change($event)"></el-input>
        </el-form-item>
        <el-form-item label="简要描述" prop="brief">
          <el-input v-model="addProductForm.brief" @input="change($event)"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="addProductForm.status" placeholder="请选择">
            <el-option
              key="0"
              label="禁用"
              value="0">
            </el-option>
            <el-option
              key="1"
              label="正常"
              value="1">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="详细描述" prop="content">
          <el-input type="textarea" rows="5" v-model="addProductForm.content" @input="change($event)"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addProduct">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑 Product 的对话框 -->
    <el-dialog title="编辑 Service" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <!-- 内容主体 -->
      <el-form
        :model="editProductForm"
        ref="editProductFormRef"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="prodName">
          <el-input v-model="editProductForm.prodName" @input="change($event)"></el-input>
        </el-form-item>
        <el-form-item label="现价" prop="price">
          <el-input v-model="editProductForm.price" @input="change($event)"></el-input>
        </el-form-item>
        <el-form-item label="简要描述" prop="brief">
          <el-input v-model="editProductForm.brief" @input="change($event)"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editProductForm.status" placeholder="请选择">
            <el-option
              key="0"
              label="禁用"
              value="0">
            </el-option>
            <el-option
              key="1"
              label="正常"
              value="1">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="详细描述" prop="content">
          <el-input type="textarea" rows="5" v-model="editProductForm.content" @input="change($event)"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editProduct">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data () {
    return {
      // 获取Pod列表查询参数对象
      queryInfo: {
        query: '',
        // 当前页数
        pagenum: 1,
        // 每页显示多少数据
        pagesize: 5
      },
      productlist: [],
      totle: 0,
      // 添加 Product 对话框
      addDialogVisible: false,
      // Product 添加
      addProductForm: {
        prodName: '',
        price: '',
        brief: '',
        content: '',
        status: '',
      },
      // 编辑 Product 对话框
      editDialogVisible: false,
      // 编辑 Product
      editProductForm: {
        prodName: '',
        price: '',
        brief: '',
        content: '',
        status: '',
      },
    }
  },
  created () {
    this.getProductlist()
  },
  methods: {
    async getProductlist () {
        const { data: res } = await this.$http.get('/auth/product', {
          params : {
            prodName : this.queryInfo.query,
          }
        })
        console.info('getProductlist', res)
        if (!res.success) {
            return this.$message.error(res.message)
        }
        this.productlist = res.data;
        this.productlist.forEach(v => {
          v.status = v.status == 0 ? '禁用' : '正常'
        });
        this.totle = res.count;
        console.info("userlist", this.productlist, this.totle);
    },
    // 监听 pagesize改变的事件
    handleSizeChange (newSize) {
      // console.log(newSize)
      this.queryInfo.pagesize = newSize
      this.getProductlist()
    },
    // 监听 页码值 改变事件
    handleCurrentChange (newSize) {
      // console.log(newSize)
      this.queryInfo.pagenum = newSize
      this.getProductlist()
    },
    // 监听 添加 Product 对话框的关闭事件
    addDialogClosed () {
      this.$refs.addProductFormRef.resetFields()
    },
    // 添加 Product
    addProduct () {
      console.info('addProduct this.addProductForm', this.addProductForm)
      // 提交请求前，表单预验证
      this.$refs.addProductFormRef.validate(async valid => {
        // 表单预校验失败
        if (!valid) return
        const { data: res } = await this.$http.post('/auth/product', {
          ...this.addProductForm,
        })
        if (!res.success) {
          return this.$message.error(res.message)
        }
        this.$message.success("创建 商品 成功")
        console.info('商品', res)
        // 隐藏添加Pod对话框
        this.addDialogVisible = false
        this.getProductlist()
      })
    },
    // 展开 编辑 Product 对话框
    async showEditDialog (serviceInfo) {
      this.editDialogVisible = true
      const { data: res } = await this.$http.get('/auth/product/detail', {
        params : {
          productId: serviceInfo.prodId
        }
      })
      if (!res.success) {
        this.$message.error(res.message)
      }
      this.editProductForm = res.data
    },
    // 更新 Product 信息
    async editProduct (serviceInfo) {
      const { data: res } = await this.$http.put('/auth/product', {
        prodId: this.editProductForm.prodId,
        prodName: this.editProductForm.prodName,
        price: this.editProductForm.price,
        brief: this.editProductForm.brief,
        content: this.editProductForm.content,
        status: this.editProductForm.status,
      })
      this.editDialogVisible = false
      this.getProductlist()
    },
    // 删除 Product
    async removeProduct (serviceInfo) {
      const confirmResult = await this.$confirm(
        '此操作将永久删除该 商品 , 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)
      if (confirmResult !== 'confirm') {
        return this.$message.info('已取消删除')
      }
      console.info('delete 商品', serviceInfo);
      const { data: res } = await this.$http.delete('/auth/product', {
        params: {
          productId : serviceInfo.prodId
        }
      })
      console.info('res', res)
      if (!res.success) return this.$message.error('删除 商品 失败！')
      this.$message.success('删除 商品 成功！')
      this.getProductlist()
    },
    // 监听修改用户对话框的关闭事件
    editDialogClosed () {
      this.$refs.editProductFormRef.resetFields()
    },
    change (e) {
      this.$forceUpdate()
    },
  }
}
</script>

<style lang="less" scoped>
</style>
