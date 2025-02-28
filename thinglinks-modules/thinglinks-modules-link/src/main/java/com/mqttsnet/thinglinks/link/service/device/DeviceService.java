package com.mqttsnet.thinglinks.link.service.device;

import com.mqttsnet.thinglinks.link.api.domain.device.entity.Device;
import com.mqttsnet.thinglinks.link.api.domain.device.model.DeviceParams;

import java.util.Collection;
import java.util.List;

/**
 * @Description: 设备管理业务层接口
 * @Author: ShiHuan Sun
 * @E-mail: 13733918655@163.com
 * @Website: http://thinglinks.mqttsnet.com
 * @CreateDate: 2021/12/26$ 0:27$
 * @UpdateUser: ShiHuan Sun
 * @UpdateDate: 2021/12/26$ 0:27$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface DeviceService {


    int deleteByPrimaryKey(Long id);

    int insert(Device record);

    int insertOrUpdate(Device record);

    int insertOrUpdateSelective(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);

    int updateBatch(List<Device> list);

    int updateBatchSelective(List<Device> list);

    int batchInsert(List<Device> list);

    int updateConnectStatusByClientId(String updatedConnectStatus, String clientId);

    Device findOneByClientIdAndUserNameAndPasswordAndDeviceStatusAndProtocolType(String clientId, String userName, String password, String deviceStatus, String protocolType);

    List<Device> findByAll(Device device);

    Device findOneById(Long id);


    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    public Device selectDeviceById(Long id);

    /**
     * 查询设备管理列表
     *
     * @param device 设备管理
     * @return 设备管理集合
     */
    public List<Device> selectDeviceList(Device device);

    /**
     * 新增设备管理
     *
     * @param deviceParams 设备管理
     * @return 结果
     */
    public int insertDevice(DeviceParams deviceParams) throws Exception;

    /**
     * 修改设备管理
     *
     * @param deviceParams 设备管理
     * @return 结果
     */
    public int updateDevice(DeviceParams deviceParams) throws Exception;

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键集合
     * @return 结果
     */
    public int deleteDeviceByIds(Long[] ids);

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    public int deleteDeviceById(Long id);


    Device findOneByClientId(String clientId);


    Device findOneByClientIdAndDeviceIdentification(String clientId, String deviceIdentification);


    Device findOneByDeviceIdentification(String deviceIdentification);


    Device findOneByClientIdOrderByDeviceIdentification(String clientId);


    Device findOneByClientIdOrDeviceIdentification(String clientId, String deviceIdentification);


    /**
     * 设备信息缓存失效
     *
     * @param clientId
     * @return
     */
    Boolean cacheInvalidation(String clientId);

    /**
     * 批量断开设备连接端口
     *
     * @param ids
     * @return
     */
    Boolean disconnect(Long[] ids);


    Long countDistinctClientIdByConnectStatus(String connectStatus);

    /**
     * 根据产品标识查询所属的设备标识
     *
     * @param productIdentification
     * @return
     */
    List<String> selectByProductIdentification(String productIdentification);

    /**
     * 客户端身份认证
     *
     * @param clientIdentifier 客户端
     * @param username         用户名
     * @param password         密码
     * @param deviceStatus     设备状态
     * @param protocolType     协议类型
     * @return
     */
    Boolean clientAuthentication(String clientIdentifier, String username, String password, String deviceStatus, String protocolType);


    List<Device> findAllByIdIn(Collection<Long> idCollection);


    List<Device> findAllByProductIdentification(String productIdentification);

    /**
     * 查询设备详细信息
     *
     * @param id
     * @return
     */
    public DeviceParams selectDeviceModelById(Long id);
}

