package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.AllController;
import net.dlyt.qyds.common.dto.ext.AllControllerExt;
import net.dlyt.qyds.dao.AllControllerMapper;
import net.dlyt.qyds.dao.ext.AllControllerMapperExt;
import net.dlyt.qyds.web.service.AllControllerService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("allControllerService")
public class AllControllerServiceImpl implements AllControllerService {
    protected final Logger log = LoggerFactory.getLogger(MmbMasterServiceImpl.class);

    @Autowired
    private AllControllerMapperExt allControllerMapperExt;

    @Autowired
    private AllControllerMapper allControllerMapper;

    @Override
    public List<AllControllerExt> selectAll(AllControllerExt ext) {
        return allControllerMapperExt.selectAll(ext);
    }

    @Override
    public int getAllDataCount(AllControllerExt ext) {
        return allControllerMapperExt.getAllDataCount(ext);
    }

    @Override
    public JSONObject selectBySelective(AllController allController) {
        JSONObject json = new JSONObject();
        try {
            if (allController != null) {
                // 获取信息
                AllControllerExt rAllController = null;
                List<AllControllerExt> allControllerExts = allControllerMapperExt.selectBySelective(allController);
                if (allControllerExts!=null&&allControllerExts.size()!=0){
                     rAllController = allControllerExts.get(0);
                }
                if (rAllController == null) {
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "全局控制信息不存在");
                } else {
                    json.put("data", rAllController);
                    json.put("resultCode", Constants.NORMAL);
                }
            } else {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "用户信息不存在");
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    @Override
    public JSONObject updateByPrimaryKeySelective(AllController allController) {
        JSONObject json = new JSONObject();

        try {

            if (allController == null || StringUtils.isEmpty(allController.getAllControllerId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }
            int ret = allControllerMapper.updateByPrimaryKeySelective(allController);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    @Override
    public JSONObject insertSelective(AllController allController) {
        JSONObject json = new JSONObject();

        try {
            if (allController == null || StringUtils.isEmpty(allController.getAllControllerId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }
            int ret = allControllerMapper.insertSelective(allController);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }
}
