<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="边设备唯一标识" prop="did" label-width="120px">
        <div v-if="this.queryParams.did">
          <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
            {{ searchValue }}
          </el-tag>
        </div>
        <el-button v-if="!this.queryParams.did" type="primary" icon="el-icon-plus" size="mini" plain
          @click="drawer = !drawer; search = true">
          边设备信息
        </el-button>
      </el-form-item>
      <el-form-item label="子设备唯一标识" prop="nodeId" label-width="182px">
        <el-input v-model="queryParams.nodeId" placeholder="请输入设备唯一标识" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="子设备名称" prop="nodeName" label-width="100px">
        <el-input v-model="queryParams.nodeName" placeholder="请输入设备名称" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="子设备标识" prop="deviceId" label-width="120px">
        <el-input v-model="queryParams.deviceId" placeholder="请输入子设备标识" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item v-if="advancedSearch" label="应用ID" prop="appId" label-width="120px">
        <el-input v-model="queryParams.appId" placeholder="请输入应用ID" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item v-if="advancedSearch" label="厂商ID" prop="manufacturerId" label-width="80px">
        <el-input v-model="queryParams.manufacturerId" placeholder="请输入厂商ID" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item v-if="advancedSearch" label="设备型号" prop="model" label-width="100px">
        <el-input v-model="queryParams.model" placeholder="请输入设备型号" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item v-if="advancedSearch" label="连接状态" prop="connectStatus" label-width="120px">
        <el-select v-model="queryParams.connectStatus" placeholder="请选择连接状态" clearable size="small">
          <el-option v-for="dict in dict.type.link_device_connect_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="advancedSearch" label="设备影子" prop="shadowEnable" label-width="120px">
        <el-select v-model="queryParams.shadowEnable" placeholder="请选择是否支持设备影子" clearable size="small">
          <el-option v-for="dict in dict.type.link_deviceInfo_shadow_enable" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="advancedSearch" label="状态" prop="status" label-width="80px">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option v-for="dict in dict.type.business_data_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button :icon="icon" size="mini" @click="advancedSearch_toggle($event)">高级搜索</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['link:deviceInfo:add']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['link:deviceInfo:edit']">修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['link:deviceInfo:remove']">删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button plain icon="el-icon-refresh" size="mini" type="primary" :disabled="multiple"
          @click="initializeTheDataModel">
          产品初始化
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['link:deviceInfo:export']">导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deviceInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="边设备唯一标识" align="center" prop="edgeDevicesIdentification" />
      <el-table-column label="应用ID" align="center" prop="appId" />
      <el-table-column label="设备唯一标识" align="center" prop="nodeId" />
      <el-table-column label="设备名称" align="center" prop="nodeName" />
      <el-table-column label="子设备标识" align="center" prop="deviceId" width="200px" />
      <el-table-column label="设备描述" align="center" prop="description" />
      <el-table-column label="厂商ID" align="center" prop="manufacturerId" />
      <el-table-column label="设备型号" align="center" prop="model" />
      <el-table-column label="连接状态" align="center" prop="connectStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.link_device_connect_status" :value="scope.row.connectStatus" />
        </template>
      </el-table-column>
      <el-table-column label="是否支持设备影子" align="center" prop="shadowEnable">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.link_deviceInfo_shadow_enable" :value="scope.row.shadowEnable" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.business_data_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center" width="200">
        <template slot-scope="scope">
          <span style="margin-right:10px">
            <el-tooltip class="item" effect="light" content="修改" placement="top">
              <el-button circle size="mini" type="primary" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['link:deviceInfo:edit']">
              </el-button>
            </el-tooltip>
          </span>

          <span style="margin-right:10px">
            <el-tooltip class="item" effect="light" content="删除" placement="top">
              <el-button circle size="mini" type="primary" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['link:deviceInfo:remove']">
              </el-button>
            </el-tooltip>
          </span>
          <span style="margin-right:10px">
            <el-tooltip class="item" content="产品初始化" effect="light" placement="top">
              <el-button circle icon="el-icon-refresh" size="mini" type="primary"
                @click="initializeTheDataModel(scope.row)">
              </el-button>
            </el-tooltip>
          </span>
          <span style="margin-right:10px">
            <el-tooltip class="item" effect="light" content="设备影子" placement="top">
              <router-link :to="{ name: 'equipmentShadow', query: { id: scope.row.id } }">
                <el-button circle size="mini" type="primary" icon="el-icon-s-operation"></el-button>
              </router-link>
            </el-tooltip>
          </span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改子设备管理对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="40%" append-to-body
      @closed="search = false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row style="display: flex;justify-content: space-between;">
          <el-col :span="10">
            <el-form-item label="边设备" prop="did">
              <div v-if="this.form.did">
                <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false"
                  @close="handleClose(tag)">
                  {{ edgeDevicesIdentification }}
                </el-tag>
              </div>
              <el-button v-if="!this.form.did" type="primary" icon="el-icon-plus" size="mini" plain
                @click="drawer = !drawer">边设备信息
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="应用ID" prop="appId">
              <el-input v-model="form.appId" :disabled='set ? true : false' placeholder="请输入应用ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display: flex;justify-content: space-between;">
          <el-col :span="10">
            <el-form-item label="设备唯一标识" prop="nodeId">
              <el-input v-model="form.nodeId" :disabled='set ? true : false' placeholder="请输入设备唯一标识" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="设备名称" prop="nodeName">
              <el-input v-model="form.nodeName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display: flex;justify-content: space-between;">
          <el-col :span="10">
            <el-form-item label="子设备标识" prop="deviceId">
              <el-input v-model="form.deviceId" :disabled='set ? true : false' placeholder="请输入子设备标识" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="设备描述" prop="description">
              <el-input v-model="form.description" placeholder="请输入设备描述" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display: flex;justify-content: space-between;">
          <el-col :span="10">
            <el-form-item label="厂商ID" prop="manufacturerId">
              <el-input v-model="form.manufacturerId" :disabled='set ? true : false' placeholder="请输入厂商ID" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="设备型号" prop="model">
              <el-input v-model="form.model" :disabled='set ? true : false' placeholder="请输入设备型号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display: flex;justify-content: space-between;">
          <el-col :span="10">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态(字典值：0启用  1停用)">
                <el-option v-for="dict in dict.type.business_data_status" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="是否支持设备影子" prop="shadowEnable">
              <el-select v-model="form.shadowEnable" placeholder="请选择是否支持设备影子">
                <el-option v-for="dict in dict.type.link_deviceInfo_shadow_enable" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display: flex;justify-content: space-between;">
          <el-col :span="22">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-drawer title="边设备信息" :visible.sync="drawer" :before-close="handleClose">
      <el-row>
        <el-table ref="myTable" highlight-current-row v-loading="loading" :data="deviceList"
          @row-click="deviceListItem">
          <el-table-column align="center" label="id" prop="id" />
          <el-table-column align="center" label="设备标识" prop="deviceIdentification" />
          <el-table-column align="center" label="客户端标识" prop="clientId" />
        </el-table>
        <pagination v-show="deviceTotal > 0" :limit.sync="queryParamsD.pageSize" :page.sync="queryParamsD.pageNum"
          :total="deviceTotal" @pagination="getDeviceList" />
      </el-row>
      <el-row style="padding:20px 30px">
        <el-button type="primary" @click="drawer = !drawer; confirm()">确 定</el-button>
        <el-button @click="drawer = !drawer; cancelD()">取 消</el-button>
      </el-row>
    </el-drawer>
  </div>
</template>
<script>
import { listDeviceInfo, getDeviceInfo, delDeviceInfo, addDeviceInfo, updateDeviceInfo, refreshDeviceInfoDataModel } from "@/api/link/deviceInfo";
import { listDevice, } from "@/api/link/device/device";
export default {
  name: "DeviceInfo",
  dicts: ['link_device_connect_status', 'link_deviceInfo_shadow_enable', 'business_data_status'],
  data() {
    return {
      // 设备档案表格数据
      search: false,
      searchValue: "",
      oldSearchValue: '',
      deviceTotal: 0,
      did: '',
      oldDid: '',
      edgeDevicesIdentification: '',
      oldEdgeDevicesIdentification: "",
      dynamicTags: [],
      deviceList: [],
      drawer: false,
      // 边设备查询参数
      queryParamsD: {
        pageNum: 1,
        pageSize: 10,
        clientId: null,
        deviceIdentification: null,
        deviceName: null,
        connector: null,
        deviceStatus: null,
        connectStatus: null,
        isWill: null,
        deviceTags: null,
        productIdentification: null,
        protocolType: null,
        deviceType: null,
      },
      set: false,//修改禁用标识
      // 高级搜索切换
      advancedSearch: false,
      // 高级搜索icon
      icon: "el-icon-arrow-down",
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 子设备管理表格数据
      deviceInfoList: [],
      deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        did: null,
        appId: null,
        nodeId: null,
        nodeName: null,
        deviceId: null,
        description: null,
        manufacturerId: null,
        model: null,
        connectStatus: null,
        shadowEnable: null,
        shadowTableName: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        did: [
          { required: true, message: "边设备不能为空", trigger: "blur" }
        ],
        appId: [
          { required: true, message: "应用ID不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态(字典值：0启用  1停用)不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDeviceList();
  },
  methods: {
    handleClose(tag) {
      if (this.search) {
        this.queryParams.did = ''
      } else {
        this.form.did = ''
      }
    },
    confirm() {
      if (this.search) {
        this.searchValue = this.oldSearchValue
        this.queryParams.did = this.oldDid
        this.dynamicTags = [this.searchValue]
      } else {
        this.form.did = this.oldDid
        this.edgeDevicesIdentification = this.oldEdgeDevicesIdentification
        this.dynamicTags = [this.edgeDevicesIdentification]
      }
    },
    cancelD() {
      if (this.search) {
        this.queryParams.did = this.oldDid
        this.dynamicTags = [this.searchValue]
      } else {
        this.form.did = this.oldDid
        this.dynamicTags = [this.edgeDevicesIdentification]
      }
    },
    deviceListItem(row) {
      if (this.search) {
        this.oldDid = row.id
        this.oldSearchValue = row.deviceIdentification
      } else {
        this.oldDid = row.id
        this.oldEdgeDevicesIdentification = row.deviceIdentification
      }
    },
    //初始化数据模型
    initializeTheDataModel(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否初始化"' + ids + '"的数据项？')
        .then(function () {
          console.log(ids);
          return refreshDeviceInfoDataModel(ids);
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess("初始化成功");
        })
        .catch(() => { });
    },
    // 高级搜索切换显示隐藏
    advancedSearch_toggle() {
      this.advancedSearch = !this.advancedSearch;
      // 切換icon
      if (this.advancedSearch) {
        this.icon = "el-icon-arrow-up";
      } else {
        this.icon = "el-icon-arrow-down";
      }
    },
    /** 查询设备档案列表 */
    getDeviceList() {
      this.loading = true;
      listDevice(this.queryParamsD).then((response) => {
        this.deviceList = response.data.device.rows;
        this.deviceTotal = response.data.device.total;
        this.loading = false;
      });
    },
    /** 查询子设备管理列表 */
    getList() {
      this.loading = true;
      listDeviceInfo(this.queryParams).then(response => {
        console.log(response.rows);
        this.deviceInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.edgeDevicesIdentification = ''
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        did: null,
        appId: null,
        nodeId: null,
        nodeName: null,
        deviceId: null,
        description: null,
        manufacturerId: null,
        model: null,
        connectStatus: null,
        shadowEnable: null,
        shadowTableName: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      console.log(this.queryParams);
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.set = false
      this.reset();
      this.form.did = ''
      this.edgeDevicesIdentification = ''
      this.open = true;
      this.search = false
      this.title = "添加子设备管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.set = true;
      const id = row.id || this.ids
      getDeviceInfo(id).then(response => {
        console.log(response.data);
        this.form = response.data;
        this.oldDid = this.form.did
        this.edgeDevicesIdentification = response.data.edgeDevicesIdentification
        this.oldEdgeDevicesIdentification = response.data.edgeDevicesIdentification
        this.dynamicTags = [this.edgeDevicesIdentification]
        this.open = true;
        this.search = false
        this.title = "修改子设备管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        console.log(this.form);
        if (valid) {
          if (this.form.id != null) {
            updateDeviceInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDeviceInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除子设备管理编号为"' + ids + '"的数据项？').then(function () {
        return delDeviceInfo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('link/deviceInfo/export', {
        ...this.queryParams
      }, `link_deviceInfo.xlsx`)
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
